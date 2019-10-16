package io.renren.modules.test.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.common.annotation.SysLog;
import io.renren.common.utils.Query;
import io.renren.modules.test.entity.ThirdToolsEntity;
import io.renren.modules.test.service.ThirdToolsService;
import io.renren.modules.test.utils.StressTestUtils;
import io.renren.modules.test.utils.ThirdToolsUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 三方工具表
 * 包含个子菜单下的公用控制器
 * @date 2019-06-19 18:41:49
 */
@RestController
@RequestMapping("/test/tool")
public class ThirdToolsController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ThirdToolsService thirdToolsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("test:stress:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(StressTestUtils.filterParms(params));
        List<ThirdToolsEntity> toolList = thirdToolsService.queryList(query);
        int total = thirdToolsService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(toolList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }


    /**
     * 监控子列表
     */
    @RequestMapping("/sublist/moniter")
    @RequiresPermissions("test:stress:sublist")
    public R sublistMoniter(@RequestParam Map<String, Object> params){
        params.put("toolsType", ThirdToolsUtils.TOOLS_TYPE_MONITER);
        Query query = new Query(StressTestUtils.filterParms(params));
        List<ThirdToolsEntity> toolList = thirdToolsService.queryListByType(query);
        int total = thirdToolsService.queryTotalByType(query);
        PageUtils pageUtil = new PageUtils(toolList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }


    /**
     * 大数据子列表
     */
    @RequestMapping("/sublist/bigdata")
    @RequiresPermissions("test:stress:sublist")
    public R sublistBigData(@RequestParam Map<String, Object> params){
        params.put("toolsType", ThirdToolsUtils.TOOLS_TYPE_BIGDATA);
        Query query = new Query(StressTestUtils.filterParms(params));
        List<ThirdToolsEntity> toolList = thirdToolsService.queryListByType(query);
        int total = thirdToolsService.queryTotalByType(query);
        PageUtils pageUtil = new PageUtils(toolList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * docker类子列表
     */
    @RequestMapping("/sublist/docker")
    @RequiresPermissions("test:stress:sublist")
    public R sublistDocker(@RequestParam Map<String, Object> params){
        params.put("toolsType", ThirdToolsUtils.TOOLS_TYPE_DOCKER);
        Query query = new Query(StressTestUtils.filterParms(params));
        List<ThirdToolsEntity> toolList = thirdToolsService.queryListByType(query);
        int total = thirdToolsService.queryTotalByType(query);
        PageUtils pageUtil = new PageUtils(toolList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }


    /**
     * 其他子列表
     */
    @RequestMapping("/sublist/helper")
    @RequiresPermissions("test:stress:sublist")
    public R sublistHelper(@RequestParam Map<String, Object> params){
        params.put("toolsType", ThirdToolsUtils.TOOLS_TYPE_HELPER);
        Query query = new Query(StressTestUtils.filterParms(params));
        List<ThirdToolsEntity> toolList = thirdToolsService.queryListByType(query);
        int total = thirdToolsService.queryTotalByType(query);
        PageUtils pageUtil = new PageUtils(toolList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }





    /**
     * 信息
     */
    @RequestMapping("/info/{toolsId}")
    @RequiresPermissions("test:stress:info")
    public R info(@PathVariable("toolsId") Long toolsId){
		ThirdToolsEntity toolsmoniter = thirdToolsService.queryObject(toolsId);
        return R.ok().put("toolsMoniter", toolsmoniter);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("test:stress:save")
    public R save(@RequestBody ThirdToolsEntity toolsmoniter){
		thirdToolsService.save(toolsmoniter);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("test:stress:update")
    public R update(@RequestBody ThirdToolsEntity toolsmoniter){
		thirdToolsService.update(toolsmoniter);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("test:stress:delete")
    public R delete(@RequestBody Long[] toolsIds){
		thirdToolsService.deleteBatch(toolsIds);
        return R.ok();
    }




    /**
     * 立即执行启动进程。
     */
    @SysLog("立即执行启动三方进程")
    @RequestMapping("/run")
    @RequiresPermissions("test:stress:run")
    public R run(@RequestBody Long[] toolIds) {
        return R.ok(thirdToolsService.run(toolIds));
    }

    /**
     * 立即停止进程。
     */
    @SysLog("立即停止三方进程")
    @RequestMapping("/stop")
    @RequiresPermissions("test:stress:stop")
    public R stop(@RequestBody Long[] toolIds) {
        thirdToolsService.stop(toolIds);
        return R.ok();
    }

}
