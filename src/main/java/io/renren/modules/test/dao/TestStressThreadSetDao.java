package io.renren.modules.test.dao;

import io.renren.modules.test.entity.TestStressThreadSetEntity;
import io.renren.modules.sys.dao.BaseDao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 线程组管理
 * 
 * @author smooth-z
 * @email 
 * @date 2019-03-26 09:48:06
 */
@Mapper
public interface TestStressThreadSetDao extends BaseDao<TestStressThreadSetEntity> {
	
	/**
	 * 按文件编号删除线程组配置项
	 */
	int deleteByFileId(Object id);
	
	/**
	 * 按文件编号批量删除线程组配置项
	 */
	int deleteBatchByFileIds(Object[] id);
	
	/**
	 * 获取不包含配置项的菜单列表
	 */
	List<TestStressThreadSetEntity> queryNotSetList();
	
	/**
	 * 获取脚本文件编号下的线程组配置信息
	 */
	List<TestStressThreadSetEntity> queryListByFileId(Object id);
}
