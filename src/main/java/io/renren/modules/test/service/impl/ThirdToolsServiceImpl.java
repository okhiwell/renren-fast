package io.renren.modules.test.service.impl;

import io.renren.common.exception.RRException;
import io.renren.modules.test.dao.ThirdToolsDao;
import io.renren.modules.test.entity.StressTestSlaveEntity;
import io.renren.modules.test.entity.ThirdToolsEntity;
import io.renren.modules.test.service.StressTestSlaveService;
import io.renren.modules.test.service.ThirdToolsService;
import io.renren.modules.test.utils.SSH2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.tomcat.jni.Time.sleep;

@Service("toolsmoniterService")
public class ThirdToolsServiceImpl implements ThirdToolsService {


    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StressTestSlaveService stressTestSlaveService;

    @Autowired
    private ThirdToolsDao thirdToolsDao;

    @Override
    public ThirdToolsEntity queryObject(Long toolsId) {
        return thirdToolsDao.queryObject(toolsId);
    }

    @Override
    public List<ThirdToolsEntity> queryList(Map<String, Object> map) {
        return thirdToolsDao.queryList(map);
//        return thirdToolsDao.queryListByType(map.get("toolsType").toString());
    }

    @Override
    public List<ThirdToolsEntity> queryListByType(Map<String, Object> map) {
        return thirdToolsDao.queryListByType(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return thirdToolsDao.queryTotal();
//        return thirdToolsDao.queryTotalByType(map.get("toolsType").toString());

    }

    @Override
    public int queryTotalByType(Map<String, Object> map) {
        return thirdToolsDao.queryTotalByType(map);
    }

    @Override
    public void save(ThirdToolsEntity thirdToolsEntity) {
        thirdToolsDao.save(thirdToolsEntity);
    }

    @Override
    public void update(ThirdToolsEntity thirdToolsEntity) {
        thirdToolsDao.update(thirdToolsEntity);
    }

    @Override
    public void deleteBatch(Long[] toolsIds) {
        thirdToolsDao.deleteBatch(toolsIds);
    }

    @Override
    public String run(Long[] toolIds) {
        List<String> pidList = new ArrayList<>();
        Arrays.asList(toolIds).stream().forEach(toolId -> {
            ThirdToolsEntity thirdToolsEntity = queryObject((Long) toolId);
            String startCommandTotalStr = thirdToolsEntity.getStartCommand();
            String[] startCommandList = thirdToolsEntity.getStartCommand().split("&amp;&amp;");
            String url = thirdToolsEntity.getToolsUrl().replace("//", "");
            String ip = url.substring(url.indexOf(":") + 1, url.lastIndexOf(":"));
            String portSub = url.substring(url.lastIndexOf(":") + 1);

            String port = portSub;
            if(portSub.contains("/")){
                port = portSub.substring(0,portSub.indexOf("/"));
            }

            Integer status =  thirdToolsEntity.getStatus();
            Boolean checkJps = Boolean.FALSE;
            StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP(ip, "22");
            logger.info("stressTestSlaveEntity:" + stressTestSlaveEntity.toString());
            SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);

            // 1、先监听端口是否已经启动：
            String cmds1 = "netstat -nlp | grep " + port;
            String res1 = ssh2Utils.runCommand(cmds1);
            logger.info(res1);
            if (res1.contains(port)) {
                if(status==0){
                    String pid1 = res1.substring(res1.indexOf("LISTEN") + 10, res1.indexOf("/")).trim();
                    thirdToolsEntity.setStatus(Integer.valueOf(pid1));
                    thirdToolsDao.update(thirdToolsEntity);
                }
                try{
                    throw new RRException("进程已经运行");
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {//2、运行启动命令
                for(String startCommand:startCommandList){ // 一次接收多条命令
                    if(startCommand.endsWith(".sh")){ //脚本类
                        String desDir = startCommand.substring(0,startCommand.lastIndexOf("/"));
                        String script = "."+startCommand.substring(startCommand.lastIndexOf("/"));
                        String cmd3 = "cd "+desDir+" && "+script;
                        String res2 = ssh2Utils.runCommand(cmd3);
                        logger.info(res2);
                    }else{ // 直接命令行
                        String res2 = ssh2Utils.runCommand(startCommand);
                        logger.info(res2);
                        if(startCommand.contains("hadoop") || (startCommand.contains("hbase"))){
                            checkJps = Boolean.TRUE;
                        }
                    }
                }
                // 3. 验证端口是否已经启动 : 考虑进程启动时间较长，设计了3次重试的策略
                String res3 = ssh2Utils.runCommand(cmds1);
                logger.info(res3);
                if (!res3.contains(port)) {
                    if(startCommandTotalStr.contains("docker")){
                        String stopCommand = startCommandTotalStr.replace("start","stop");
                        if(startCommandTotalStr.contains("&amp;&amp;")){
                            stopCommand = stopCommand.substring(0,stopCommand.indexOf("&amp;&amp;"));
                        }
                        String resStop = ssh2Utils.runCommand(stopCommand);
                        logger.info(resStop);
                    }
                    throw new RRException("进程未正常启动");
                } else {
                    String pid = res3.substring(res3.indexOf("LISTEN") + 8, res3.indexOf("/")).trim();
                    pidList.add(pid);
                    logger.info(pid);
                    thirdToolsEntity.setStatus(Integer.valueOf(pid));
                    thirdToolsDao.update(thirdToolsEntity);

                    if(checkJps){
                        String resJps = ssh2Utils.runCommand("jps |grep -v jar | grep -v war| awk -F' ' '{print $2}'");
                        logger.info(resJps);
                        if (resJps.contains("DataNode") && resJps.contains("SecondaryNameNode") && resJps.contains("NameNode") &&
                                resJps.contains("NodeManager")  && resJps.contains("ResourceManager")  && resJps.contains("HMaster")  &&
                                resJps.contains("QuorumPeerMain") && resJps.contains("HRegionServer")){
                            logger.info("Start hadoop-hbase Successful");
                        }else{
                            throw new RRException("Haoop-Hbase的Jps检测异常");
                        }
                    }
                }
            }
        });
        return pidList.toString();
    }

    @Override
    public void stop(Long[] toolIds) {
        List<String> pidList = new ArrayList<>();
        Arrays.asList(toolIds).stream().forEach(toolId -> {
            ThirdToolsEntity thirdToolsEntity = queryObject((Long) toolId);
            String url = thirdToolsEntity.getToolsUrl().replace("//", "");
            String startCommand = thirdToolsEntity.getStartCommand();
            String ip = url.substring(url.indexOf(":") + 1, url.lastIndexOf(":"));
            String portSub = url.substring(url.lastIndexOf(":") + 1);

            String port = portSub;
            if(portSub.contains("/")){
                port = portSub.substring(0,portSub.indexOf("/"));
            }

            StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP(ip, "22");
            logger.info("stressTestSlaveEntity:" + stressTestSlaveEntity.toString());
            SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);

            // 1、先监听端口是否已经启动：
            String cmds1 = "netstat -nlp | grep " + port;
            String res1 = ssh2Utils.runCommand(cmds1);
            logger.info(res1);
            if (!res1.contains(port)) {
                try{ // 存在已经已经被kill，但是status状态还未改变，导致前端一致无法改变这个状体的bug
                    thirdToolsEntity.setStatus(Integer.valueOf(0));
                    thirdToolsDao.update(thirdToolsEntity);
                    throw new RRException("进程尚未启动，无法停止");
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {//2、获取进程pid进行kill
                if(startCommand.contains("service") || (startCommand.contains("docker") && startCommand.contains("start"))){
                    String stopCommand = startCommand.replace("start","stop");
                    if(startCommand.contains("&amp;&amp;")){
                        stopCommand = stopCommand.substring(0,stopCommand.indexOf("&amp;&amp;"));
                    }
                    String resStop = ssh2Utils.runCommand(stopCommand);
                    logger.info(resStop);
                }

                if(startCommand.contains("docker-compose")){ // 针对docker-compose 方式启动的场景
                    String stopCommand = startCommand.replace("up","down");
                    if(startCommand.contains("&amp;&amp;")){
                        stopCommand = stopCommand.replace("&amp;&amp;"," && ");
                    }
                    String resStop = ssh2Utils.runCommand(stopCommand);
                    logger.info(resStop);
                }

                String pid = res1.substring(res1.indexOf("LISTEN") + 8, res1.indexOf("/")).trim();
                String cmds2 = "kill -9 "+pid;
                String res2 = ssh2Utils.runCommand(cmds2);
                logger.info(res2);
                thirdToolsEntity.setStatus(Integer.valueOf(0));
                thirdToolsDao.update(thirdToolsEntity);
            }
        });
    }
}