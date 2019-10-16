package io.renren.modules.test.dao;


import io.renren.modules.sys.dao.BaseDao;
import io.renren.modules.test.entity.MockApiInfoFullEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-27 13:40:12
 */
@Mapper
public interface MockApiInfoFullDao extends BaseDao<MockApiInfoFullEntity> {
     void updateBatch(List<MockApiInfoFullEntity> apiMockEntity);

    List<MockApiInfoFullEntity> queryByMethod(@Param("method") String method);
    int queryTotalByMethod(@Param("method") String method);

    void insertOrUpdate(MockApiInfoFullEntity apiMockEntity);

    List<MockApiInfoFullEntity> queryByUser(@Param("user") String user);

    List<MockApiInfoFullEntity> queryAll();
}
