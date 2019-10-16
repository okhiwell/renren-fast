package io.renren.modules.test.service;

import io.renren.modules.test.entity.InterfaceTestEntity;

import java.util.List;
import java.util.Map;

/**
 * 接口测试用例表
 * caoting@gmail.com
 * @date 2019-06-19 11:32:35
 */
public interface InterfaceTestService {
    /**
     * 根据ID，查询性能测试用例
     */
    InterfaceTestEntity queryObject(Long caseId);

    /**
     * 查询性能测试用例列表
     */
    List<InterfaceTestEntity> queryList(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存性能测试用例
     */
    void save(InterfaceTestEntity apiCase);

    /**
     * 更新性能测试用例信息
     */
    void update(InterfaceTestEntity apiCase);

    /**
     * 批量删除
     */
    void deleteBatch(Long[] caseIds);

    /**
     * 批量更新性能测试用例信息
     */
    int updateBatch(Long[] caseIds, int status);

    //TODO
    List<InterfaceTestEntity> queryByApiType(String apiType);


    /**
     * 查询指定类型总数
     */
    // TODO
    int queryTotalByApiType(String aipType);
}

