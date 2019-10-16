package io.renren.modules.test.controller;


import io.renren.common.annotation.SysLog;
import io.renren.common.exception.RRException;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.test.entity.StressTestSlaveEntity;
import io.renren.modules.test.entity.ThirdToolsEntity;
import io.renren.modules.test.service.StressTestSlaveService;
import io.renren.modules.test.service.ThirdToolsService;
import io.renren.modules.test.utils.Constant;
import io.renren.modules.test.utils.SSH2Utils;
import io.renren.modules.test.utils.StressTestUtils;
import io.renren.modules.test.utils.ThirdToolsUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 小工具
 * 主要包括对docker容器、镜像的批量操作；
 * 终端运行linux命令
 * @date 2019-06-19 18:41:49
 */
@RestController
@RequestMapping("/test/docker")
public class DockerToolsController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ThirdToolsService thirdToolsService;

    @Autowired
    private StressTestSlaveService stressTestSlaveService;

    private Constant constant=new Constant();

    /**
     * 删除容器
     */
    @RequestMapping("/container/delete")
    @RequiresPermissions("test:stress:delete")
    public R deleteContainer(@RequestBody String keyword){

        StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP(constant.getSmallToolsOpIpKey(), "22");
        logger.info("SmallTools Op NodeEntity:" + stressTestSlaveEntity.toString());
        SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);

        String cmds1 = "docker ps -a | grep "+ keyword +" | awk -F' ' '{ print $1 }' | xargs docker rm -f";
        String res1 = ssh2Utils.runCommand(cmds1);
        logger.info("deleteContainer:"+res1);

        // 验证
        String cmdscheck = "docker ps -a | grep "+ keyword +" ";
        String rescmdscheck = ssh2Utils.runCommand(cmdscheck);
        logger.info("Check leteContainer:"+rescmdscheck);
        if(!rescmdscheck.isEmpty()){
            throw new RRException("容器未全部删除！");
        }
        return R.ok().put("LogExec:",res1).put("LogCheck",cmdscheck);
    }


    /**
     * 删除镜像
     */
    @RequestMapping("/image/delete")
    @RequiresPermissions("test:stress:delete")
    public R deleteImage(@RequestBody String keyword){
        StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP(constant.getSmallToolsOpIpKey(), "22");
        logger.info("SmallTools Op NodeEntity:" + stressTestSlaveEntity.toString());
        SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);
        String cmds1 = "docker image ls | grep  "+keyword+"  | awk -F' ' '{ print $3}' | xargs docker rmi -f";
        String res1 = ssh2Utils.runCommand(cmds1);
        logger.info("deleteContainer:"+res1);

        // 验证
        String cmdscheck = "docker image ls | grep "+ keyword +" ";
        String rescmdscheck = ssh2Utils.runCommand(cmdscheck);
        logger.info("Check delete Image:"+rescmdscheck);
        if(!rescmdscheck.isEmpty()){
            throw new RRException("镜像未全部删除！");
        }
        return R.ok().put("LogExec:",res1).put("LogCheck",cmdscheck);
    }



    /**
     * 启动容器
     */
    @SysLog("启动容器")
    @RequestMapping("/container/start")
    @RequiresPermissions("test:stress:start")
    public R startContainer(@RequestBody String keyword) {
        StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP(constant.getSmallToolsOpIpKey(), "22");
        logger.info("SmallTools Op NodeEntity:" + stressTestSlaveEntity.toString());
        SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);

        String cmds1 = "docker ps -a | grep "+ keyword +" | awk -F' ' '{ print $1 }' | xargs docker start";
        String res1 = ssh2Utils.runCommand(cmds1);
        logger.info("startContainer:"+res1);


        // 验证
        String cmdscheck = "docker ps -a | grep "+ keyword +" ";
        String rescmdscheck = ssh2Utils.runCommand(cmdscheck);
        logger.info("Check leteContainer:"+rescmdscheck);
        if(!rescmdscheck.toLowerCase().contains("up")){
            throw new RRException("容器未全部启动！");
        }
        return R.ok().put("LogExec:",res1).put("LogCheck",cmdscheck);
    }

    /**
     * 停止容器
     */
    @SysLog("停止容器")
    @RequestMapping("/container/stop")
    @RequiresPermissions("test:stress:stop")
    public R stopContainer(@RequestBody String keyword) {
        StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP(constant.getSmallToolsOpIpKey(), "22");
        logger.info("SmallTools Op NodeEntity:" + stressTestSlaveEntity.toString());
        SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);

        String cmds1 = "docker ps -a | grep "+ keyword +" | awk -F' ' '{ print $1 }' | xargs docker stop";
        String res1 = ssh2Utils.runCommand(cmds1);
        logger.info("stopContainer:"+res1);

        // 验证
        String cmdscheck = "docker ps -a | grep "+ keyword +" ";
        String rescmdscheck = ssh2Utils.runCommand(cmdscheck);
        logger.info("Check leteContainer:"+rescmdscheck);
        if(!rescmdscheck.toLowerCase().contains("exited")){
            throw new RRException("容器未全部停止！");
        }
        return R.ok().put("LogExec:",res1).put("LogCheck",cmdscheck);
    }


    /**
    * 终端运行linux命令
     */
    @SysLog("运行Linux命令")
    @RequestMapping("/container/runlinuxcommand")
    @RequiresPermissions("test:stress:runlinuxcommand")
    public R runLinuxCommand(@RequestBody String keyword) {
        StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP(constant.getSmallToolsOpIpKey(), "22");
        logger.info("SmallTools Op NodeEntity:" + stressTestSlaveEntity.toString());
        SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);

        String cmds1 = keyword;
        String res1 = ssh2Utils.runCommand(cmds1);
        logger.info("runLinuxCommand:"+res1);

        // 未进行验证操作
        return R.ok().put("LogExec:",res1);
    }


}
