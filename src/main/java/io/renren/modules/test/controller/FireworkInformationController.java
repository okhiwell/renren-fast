package io.renren.modules.test.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.common.utils.Query;
import io.renren.datasources.DataSourceNames;
import io.renren.datasources.DynamicDataSource;
import io.renren.modules.test.entity.NodeEntity;
import io.renren.modules.test.utils.StressTestUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.test.entity.FireworkInformationEntity;
import io.renren.modules.test.service.FireworkInformationService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-21 23:03:01
 */
@RestController
@RequestMapping("test/nodedetail")
public class FireworkInformationController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FireworkInformationService fireworkInformationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("test:stress:list")
    public R list(@RequestParam Map<String, Object> params){

        Query query = new Query(StressTestUtils.filterParms(params));
        DynamicDataSource.setDataSource(DataSourceNames.SECOND);
        List<FireworkInformationEntity> stressTestList = fireworkInformationService.queryList(query);
        int total = fireworkInformationService.queryTotal(query);
        DynamicDataSource.setDataSource(DataSourceNames.FIRST);
        PageUtils pageUtil = new PageUtils(stressTestList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("test:stress:info")
    public R info(@PathVariable("id") String id){
        logger.info(id);
		FireworkInformationEntity fireworkInformation = fireworkInformationService.queryObjectFromAllIP(id);
        return R.ok().put("fireworkInformation", fireworkInformation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("test:stress:save")
    public R save(@RequestBody FireworkInformationEntity fireworkInformation){
		fireworkInformationService.save(fireworkInformation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("test:stress:update")
    public R update(@RequestBody FireworkInformationEntity fireworkInformation){
		fireworkInformationService.update(fireworkInformation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("test:stress:delete")
    public R delete(@RequestBody String[] ips){
		fireworkInformationService.deleteBatch(ips);
        return R.ok();
    }

}
