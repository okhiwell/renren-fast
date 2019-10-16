package io.renren.modules.test.dao;

import io.renren.modules.sys.dao.BaseDao;
import io.renren.modules.test.entity.ApiTestReportsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 性能测试报告文件表
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-18 14:32:11
 */
@Mapper
public interface ApiTestReportsDao extends BaseDao<ApiTestReportsEntity> {
    int deleteBatchByCaseIds(Object[] id);
}
