package io.renren.modules.test.dao;

import io.renren.modules.sys.dao.BaseDao;
import io.renren.modules.test.entity.NodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface NodeDao extends BaseDao<NodeEntity> {

    /**
     * 批量更新
     */
    int updateBatch(Map<String, Object> map);
    NodeEntity queryObjectFromVIP(@Param("vip") String vip);
    void insertOrUpdate(NodeEntity node);
}
