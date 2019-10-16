package io.renren.modules.test.service;

import io.renren.modules.test.entity.ThirdToolsEntity;

import java.util.List;
import java.util.Map;

/**
 * 三方工具表
 *
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-19 18:41:49
 */
public interface ThirdToolsService {

    /**
     * 根据ID，查询三方工具表
     */
    ThirdToolsEntity queryObject(Long toolsId);

    /**
     * 查询查询三方工具表
     */
    List<ThirdToolsEntity> queryList(Map<String, Object> map);

    /**
     * 查询查询三方工具表
     */
    List<ThirdToolsEntity> queryListByType(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 查询总数
     * @return
     */
    int queryTotalByType(Map<String, Object> map);

    /**
     * 保存查询三方工具表
     */
    void save(ThirdToolsEntity thirdToolsEntity);

    /**
     * 更新查询三方工具表
     */
    void update(ThirdToolsEntity thirdToolsEntity);

    /**
     * 批量删除
     */
    void deleteBatch(Long[] toolsIds);


    String run(Long[] toolIds);

    void stop(Long[] toolIds);
}

