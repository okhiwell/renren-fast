package io.renren.modules.test.dao;

import io.renren.modules.sys.dao.BaseDao;
import io.renren.modules.test.entity.InterfaceTestEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 性能测试用例表
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-19 11:32:35
 */
@Mapper
public interface InterfaceTestDao extends BaseDao<InterfaceTestEntity> {
    int updateBatch(Map<String, Object> map);

    List<InterfaceTestEntity> queryByApiType(@Param("apiType") String apiType);
    int queryTotalByApiType(@Param("apiType") String apiType);
}
