package io.renren.modules.test.service;

import io.renren.modules.test.entity.FireworkInformationEntity;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-21 23:03:01
 */
public interface FireworkInformationService {

    /**
     * 根据ID，查询子节点信息
     */
    FireworkInformationEntity queryObject(Long nodeIp);

    /**
     * 根据IP及端口查询，查询子节点信息
     */
    FireworkInformationEntity queryObjectFromAllIP(String ipall);


    /**
     * 查询子节点列表
     */
    List<FireworkInformationEntity> queryList(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存
     */
    void save(FireworkInformationEntity node);

    /**
     * 更新
     */
    void update(FireworkInformationEntity node);

    /**
     * 批量删除
     */
    void deleteBatch(String[] nodeIPs);
}

