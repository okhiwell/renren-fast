package io.renren.modules.test.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.test.entity.TestStressThreadSetEntity;
import io.renren.modules.test.service.TestStressThreadSetService;
import io.renren.common.annotation.SysLog;
import io.renren.common.utils.R;


/**
 * 线程组管理
 * @author smooth
 * @email 
 * @date 2019-03-26 09:48:06
 */
@RestController
@RequestMapping("/test/teststressthreadset")
public class TestStressThreadSetController {
	@Autowired
	private TestStressThreadSetService testStressThreadSetService;
	
	/**
	 * 所有配置列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("test:teststressthreadset:list")
	public List<TestStressThreadSetEntity> list(){
		List<TestStressThreadSetEntity> testStressThreadSetList = testStressThreadSetService.queryList(new HashMap<String, Object>());

		return testStressThreadSetList;
	}
	
	/**
	 * 选择配置项(添加、修改配置项)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("test:teststressthreadset:select")
	public R select(){
		//查询列表数据
		List<TestStressThreadSetEntity> testStressThreadSetList = testStressThreadSetService.queryNotSetList();
		
		//添加顶级菜单
		TestStressThreadSetEntity root = new TestStressThreadSetEntity();
		root.setSetId("0");
		root.setName("一级菜单");
		root.setParentId("-1");
		root.setOpen(true);
		testStressThreadSetList.add(root);
		
		return R.ok().put("testStressThreadSetList", testStressThreadSetList);
	}	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{setId}")
	@RequiresPermissions("test:teststressthreadset:info")
	public R info(@PathVariable("setId") String setId){
		TestStressThreadSetEntity testStressThreadSet = testStressThreadSetService.queryObject(setId);
		
		return R.ok().put("testStressThreadSet", testStressThreadSet);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("test:teststressthreadset:save")
	public R save(@RequestBody TestStressThreadSetEntity testStressThreadSet){
		testStressThreadSetService.save(testStressThreadSet);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("test:teststressthreadset:update")
	public R update(@RequestBody TestStressThreadSetEntity testStressThreadSet){
		testStressThreadSetService.update(testStressThreadSet);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("test:teststressthreadset:delete")
	public R delete(@RequestBody String[] setIds){
		testStressThreadSetService.deleteBatch(setIds);
		
		return R.ok();
	}
	
	/**
     * 将线程组配置同步到对应脚本文件中。
	 * @throws DocumentException 
     */
    @SysLog("将线程组配置同步到对应脚本文件中")
    @RequestMapping("/synchronizeJmx")
    @RequiresPermissions("test:teststressthreadset:synchronizeJmx")
    public R synchronizeJmx(@RequestBody Long fileId) throws DocumentException {
    	testStressThreadSetService.synchronizeJmx(fileId);
        return R.ok();
    }
}
