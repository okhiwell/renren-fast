package io.renren.modules.test.service;

import io.renren.modules.test.entity.StressTestFileEntity;
import io.renren.modules.test.entity.TestStressThreadSetEntity;
import org.dom4j.DocumentException;

import java.util.List;
import java.util.Map;

/**
 * 线程组管理
 * 
 * @author smooth
 * @email 
 * @date 2019-03-25 10:08:53
 */
public interface TestStressThreadSetService {
	
	/**
	 * 获取不包含配置项的列表
	 */
	List<TestStressThreadSetEntity> queryNotSetList();
	
	TestStressThreadSetEntity queryObject(String setId);
	
	List<TestStressThreadSetEntity> queryList(Map<String, Object> map);
	
	//以文件编号查询脚本的线程组配置信息
	List<TestStressThreadSetEntity> queryListByFileId(Long fileId);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TestStressThreadSetEntity testStressThreadSet);
	
	void update(TestStressThreadSetEntity testStressThreadSet);
	
	void delete(Long setId);
	
	void deleteBatch(String[] setIds);

	void saveBatch(List <TestStressThreadSetEntity> testStressThreadSetList);

	/**
     * 从指定脚本获取线程组配置并入库
     */
	void jmxSaveNodes(String filePath, StressTestFileEntity stressTestFile) throws DocumentException;
	
	/**
     * 从库中获取线程组配置并同步到指定脚本
     */
    void synchronizeJmx(Long fileId) throws DocumentException;
}
