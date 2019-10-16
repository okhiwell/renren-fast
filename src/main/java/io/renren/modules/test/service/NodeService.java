package io.renren.modules.test.service;

import io.renren.modules.test.entity.NodeEntity;

import java.util.List;
import java.util.Map;

/**
 * 性能测试用例
 * 
 */
public interface NodeService {

	/**
	 * 根据ID，查询子节点信息
	 */
	NodeEntity queryObject(String nodeIp);

	/**
	 * 根据IP及端口查询，查询子节点信息
	 */
	NodeEntity queryObjectFromVIP(String vip);

	
	/**
	 * 查询子节点列表
	 */
	List<NodeEntity> queryList(Map<String, Object> map);
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);
	
	/**
	 * 保存
	 */
	void save(NodeEntity node);

	/**
	 * 更新
	 */
	void update(NodeEntity node);

	void insertOrUpdate(NodeEntity node);
	
	/**
	 * 批量删除
	 */
	void deleteBatch(String[] nodeIPs);

}
