package io.renren.modules.test.service;


import io.renren.modules.test.entity.MockApiInfoFullEntity;
import java.util.List;
import java.util.Map;

/**
 * @author caoting
 * @email caoting@gmail.com
 * @date 2019-06-27 13:40:12
 */
public interface MockApiInfoFullService {
    /**
     * 根据ID，查询性能mock接口
     */
    MockApiInfoFullEntity queryObject(String url);

    /**
     * 查询性能mock接口列表
     */
    List<MockApiInfoFullEntity> queryList(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存性能mock接口
     */
    void save(MockApiInfoFullEntity apiMockEntity);

    /**
     * 更新性能mock接口信息
     */
    void update(MockApiInfoFullEntity apiMockEntity);
    
    void insertOrUpdate(MockApiInfoFullEntity apiMockEntity);

    /**
     * 批量删除
     */
    void deleteBatch(String[] urls);

    /**
     * 批量更新性能mock接口信息
     */
    void updateBatch(List<MockApiInfoFullEntity> apiMockEntity);

    List<MockApiInfoFullEntity> queryByMethod(String method);

    /**
     * 查询指定类型总数
     */
    int queryTotalByMethod(String method);

    List<MockApiInfoFullEntity> queryByUser(String user);

    List<MockApiInfoFullEntity> queryAll();

}

