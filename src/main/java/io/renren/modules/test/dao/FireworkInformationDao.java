package io.renren.modules.test.dao;

import io.renren.modules.sys.dao.BaseDao;
import io.renren.modules.test.entity.FireworkInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-21 23:03:01
 */
@Mapper
public interface FireworkInformationDao extends BaseDao<FireworkInformationEntity> {


    int updateBatch(Map<String, Object> map);
    FireworkInformationEntity queryObjectFromAllIP(@Param("vip") String vip);
}
