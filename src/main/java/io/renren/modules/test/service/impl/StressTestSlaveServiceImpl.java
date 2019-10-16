package io.renren.modules.test.service.impl;

import io.renren.common.exception.RRException;
import io.renren.modules.test.dao.StressTestSlaveDao;
import io.renren.modules.test.entity.StressTestSlaveEntity;
import io.renren.modules.test.service.StressTestSlaveService;
import io.renren.modules.test.utils.SSH2Utils;
import io.renren.modules.test.utils.StressTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

@Service("stressTestSlaveService")
public class StressTestSlaveServiceImpl implements StressTestSlaveService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StressTestSlaveDao stressTestSlaveDao;

    @Autowired
    private StressTestUtils stressTestUtils;

    @Override
    public StressTestSlaveEntity queryObject(Long slaveId) {
        return stressTestSlaveDao.queryObject(slaveId);
    }

    @Override
    public StressTestSlaveEntity queryObjectFromIP(String ip,String port) {
        return stressTestSlaveDao.queryObjectFromIP(ip,port);
    }

    @Override
    public List<StressTestSlaveEntity> queryList(Map<String, Object> map) {
        return stressTestSlaveDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return stressTestSlaveDao.queryTotal(map);
    }

    @Override
    public void save(StressTestSlaveEntity stressTestSlave) {
        stressTestSlaveDao.save(stressTestSlave);
    }

    @Override
    public void update(StressTestSlaveEntity stressTestSlave) {
        stressTestSlaveDao.update(stressTestSlave);
    }

    @Override
    public void deleteBatch(Long[] slaveIds) {
        stressTestSlaveDao.deleteBatch(slaveIds);
    }

    /**
     * 批量切换节点的状态
     */
    @Override
    public void updateBatchStatus(List<Long> slaveIds, Integer status) {
        //当前是向所有的分布式节点推送这个，阻塞操作+轮询，并非多线程，因为本地同步网卡会是瓶颈。
        //使用for循环传统写法
        //采用了先给同一个节点机传送多个文件的方式，因为数据库的连接消耗优于节点机的链接消耗
        for (Long slaveId : slaveIds) {
            StressTestSlaveEntity slave = queryObject(slaveId);

            // 跳过本机节点
            if (!"127.0.0.1".equals(slave.getIp().trim())) {
                runOrDownSlave(slave, status);
            }

            //更新数据库
            slave.setStatus(status);
            update(slave);
        }
    }

    /**
     * 启动/停止单节点
     * @param slave 节点对象
     */
    private void runOrDownSlave(StressTestSlaveEntity slave, Integer status) {
        SSH2Utils ssh2Util = new SSH2Utils(slave.getIp(), slave.getUserName(),
                slave.getPasswd(), Integer.parseInt(slave.getSshPort()));
        //如果是启用节点
        if (StressTestUtils.ENABLE.equals(status)) {
        	//启动前先检查进程，避免重复启动导致端口占用
            String psStr = ssh2Util.runCommand("ps -efww|grep -w 'jmeter-server'|grep -v grep|cut -c 9-15");
            logger.info("check jmeter-server:"+psStr);
            if(psStr.equals("")) {
                throw new RRException(slave.getSlaveName() + " 节点机连接失败！");
            }
            if(!psStr.equals("null")){
            	//本身已经是启用状态
                logger.info("check slave.getStatus():"+slave.getStatus().toString());
            	if (StressTestUtils.ENABLE.equals(slave.getStatus())){
            		throw new RRException(slave.getSlaveName() + " 已经启动不要重复启动！");
            	} else {
            		throw new RRException(slave.getSlaveName() + " 节点机进程有冲突，请先校准！");
            	}
            }
        	// 避免跨系统的问题，远端由于都时linux服务器，则文件分隔符统一为/，不然同步文件会报错。
            String jmeterServer = slave.getHomeDir() + "/bin/jmeter-server";
            String md5Str = ssh2Util.runCommand("md5sum " + jmeterServer + " | cut -d ' ' -f1");
            logger.info("md5Str:"+md5Str);
            if (!checkMD5(md5Str)) {
                throw new RRException(slave.getSlaveName() + " 节点路径错误！找不到jmeter-server启动文件！");
            }
            //首先创建目录，会遇到重复创建
            ssh2Util.runCommand("mkdir -p " + slave.getHomeDir() + "/bin/stressTestCases");
            //启动节点
            String enableResult = ssh2Util.runCommand(
                    "cd " + slave.getHomeDir() + "/bin/stressTestCases/" + "\n" +
                    "sh " + "../jmeter-server -Djava.rmi.server.hostname=" + slave.getIp());

            String psStrCheck = ssh2Util.runCommand("ps -efww|grep -w 'jmeter-server'|grep -v grep|cut -c 9-15");
            logger.info(psStrCheck);
            if (psStrCheck==null) {
                throw new RRException(slave.getSlaveName() + " jmeter-server启动节点失败！请先尝试在节点机命令执行");
            }
        }
        // 禁用节点
        if (StressTestUtils.DISABLE.equals(status)) {
            //禁用远程节点，当前是直接kill掉
            //kill掉就不用判断结果了，不抛异常即OK，但需要判断是否连接失败
            String psStr = ssh2Util.runCommand("ps -efww|grep -w 'jmeter-server'|grep -v grep|cut -c 9-15|xargs kill -9");

            if(psStr.equals("")){
                //如果连接失败，节点失效，状态直接更新为禁用（刷新页面才能看到）
                slave.setStatus(status);
                update(slave);
                throw new RRException(slave.getSlaveName() + " 节点机连接失败！");
            }
        }
    }

    /**
     * 使用正则表达式校验MD5合法性
     */
    public boolean checkMD5(String md5Str) {
        return Pattern.matches("^([a-fA-F0-9]{32})$", md5Str);
    }
    
    /**
     * 根据数据库中各节点状态，重置一遍后台进程状态
     */
    @Override
    public void batchReloadStatus(){
    	//当前是向所有的分布式节点推送这个，阻塞操作+轮询，并非多线程，因为本地同步网卡会是瓶颈。
    	//先处理已禁用的进程
        Map query = new HashMap<>();
        query.put("status", StressTestUtils.DISABLE);      
        List<StressTestSlaveEntity> stressTestSlaveList = stressTestSlaveDao.queryList(query);
        for (StressTestSlaveEntity slave : stressTestSlaveList) {
            // 本机配置IP为127.0.0.1，没配置localhost
            if ("127.0.0.1".equals(slave.getIp().trim())) {
            	continue;
            }else{
            	SSH2Utils ssh2Util = new SSH2Utils(slave.getIp(), slave.getUserName(),
                        slave.getPasswd(), Integer.parseInt(slave.getSshPort()));
                //界面状态显示禁用，重置过程就是杀死节点已存在的进程
                ssh2Util.runCommand("ps -efww|grep -w 'jmeter-server'|grep -v grep|cut -c 9-15|xargs kill -9");
            }
        }
        //再处理已启用的进程
        query.put("status", StressTestUtils.ENABLE);       
        List<StressTestSlaveEntity> stressTestSlaveList2 = stressTestSlaveDao.queryList(query);
        for (StressTestSlaveEntity slave : stressTestSlaveList2) {
        	// 本机配置IP为127.0.0.1，没配置localhost
            if ("127.0.0.1".equals(slave.getIp().trim())) {
            	continue;
            }else{
            	SSH2Utils ssh2Util = new SSH2Utils(slave.getIp(), slave.getUserName(),
                        slave.getPasswd(), Integer.parseInt(slave.getSshPort()));
                //界面状态显示启用，后台不存在进程就远程启动
                String psStr = ssh2Util.runCommand("ps -efww|grep -w 'jmeter-server'|grep -v grep|cut -c 9-15");
                if(psStr.equals("null")){
                	runOrDownSlave(slave,1);
                }
            }
        }
    }
}
