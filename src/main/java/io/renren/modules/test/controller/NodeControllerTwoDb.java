package io.renren.modules.test.controller;

import io.renren.common.annotation.SysLog;
import io.renren.common.exception.RRException;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.datasources.DataSourceNames;
import io.renren.datasources.DynamicDataSource;
import io.renren.modules.test.entity.NodeEntity;
import io.renren.modules.test.service.NodeService;
import io.renren.modules.test.utils.StressTestUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 节点管理，启动了多源数据库的第二个数据库
 */
@RestController
@RequestMapping("/test/dbnode")
public class NodeControllerTwoDb {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NodeService nodeService;

    @Autowired
    private StressTestUtils stressTestUtils;

    /**
     * 分布式节点列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("test:stress:nodeList")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(StressTestUtils.filterParms(params));
        DynamicDataSource.setDataSource(DataSourceNames.SECOND);
        List<NodeEntity> stressTestList = nodeService.queryList(query);
        int total = nodeService.queryTotal(query);
        DynamicDataSource.setDataSource(DataSourceNames.FIRST);
        PageUtils pageUtil = new PageUtils(stressTestList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据真实IP查节点信息
     */
    @RequestMapping("/info/{nodeIp}")
    @RequiresPermissions("test:stress:nodeInfo")
    public R info(@PathVariable("nodeIp") String nodeIp) {
        DynamicDataSource.setDataSource(DataSourceNames.SECOND);
        NodeEntity node = nodeService.queryObject(nodeIp.replace("_","."));
        logger.info(node.toString());
        DynamicDataSource.setDataSource(DataSourceNames.FIRST);
        return R.ok().put("testNode", node);
    }


    /**
     * 根据真实IP或者虚IP查节点信息
     */
    @RequestMapping("/infoall/{nodeIp}")
    @RequiresPermissions("test:stress:nodeInfo")
    public R infoVip(@PathVariable("nodeIp") String nodeIp) {
        logger.info("Node Query:"+nodeIp);
        DynamicDataSource.setDataSource(DataSourceNames.SECOND);
        NodeEntity node = nodeService.queryObjectFromVIP(nodeIp.replace("_","."));
        DynamicDataSource.setDataSource(DataSourceNames.FIRST);
        return R.ok().put("testNode", node);
    }



    /**
     * 保存节点
     */
    @SysLog("保存节点信息")
    @RequestMapping("/save")
    @RequiresPermissions("test:stress:nodeSave")
    public R save(@RequestBody NodeEntity node) {
        ValidatorUtils.validateEntity(node);
        DynamicDataSource.setDataSource(DataSourceNames.SECOND);
        nodeService.save(node);
        DynamicDataSource.setDataSource(DataSourceNames.FIRST);
        return R.ok();
    }

    /**
     * 修改节点信息
     */
    @SysLog("修改节点信息")
    @RequestMapping("/update")
    @RequiresPermissions("test:stress:nodeUpdate")
    public R update(@RequestBody NodeEntity node) {
        ValidatorUtils.validateEntity(node);
        DynamicDataSource.setDataSource(DataSourceNames.SECOND);
        nodeService.update(node);
        DynamicDataSource.setDataSource(DataSourceNames.FIRST);
        return R.ok();
    }


    /**
     * 保存
     */
    @RequestMapping("/saveorupdate")
    @RequiresPermissions("test:stress:saveorupdate")
    public R saveOrUpdate(@RequestBody NodeEntity node) {
        ValidatorUtils.validateEntity(node);
        DynamicDataSource.setDataSource(DataSourceNames.SECOND);
        NodeEntity nodeEntity = nodeService.queryObject(node.getRealip());
        if(nodeEntity==null){
            nodeService.save(node);
        }else{
            if(nodeEntity.getRealip().equalsIgnoreCase(node.getRealip())) {
                nodeService.insertOrUpdate(node);
            }else{
                throw new RRException("不允许修改IP的值");
            }
        }
        DynamicDataSource.setDataSource(DataSourceNames.FIRST);
        return R.ok();
    }


    /**
     * 删除节点
     */
    @SysLog("删除节点")
    @RequestMapping("/delete")
    @RequiresPermissions("test:stress:nodeDelete")
    public R delete(@RequestBody String[] nodeIPs) {
        logger.info("Node delete:"+nodeIPs.toString());
        DynamicDataSource.setDataSource(DataSourceNames.SECOND);
        nodeService.deleteBatch(nodeIPs);
        DynamicDataSource.setDataSource(DataSourceNames.FIRST);
        return R.ok();
    }
}
