package io.renren.modules.test.dao;


import io.renren.modules.sys.dao.BaseDao;
import io.renren.modules.test.entity.ApiTestFileEntity;
import io.renren.modules.test.entity.StressTestFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 性能测试用例文件表
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-19 12:12:46
 */
@Mapper
public interface ApiTestFileDao extends BaseDao<ApiTestFileEntity> {
    int deleteBatchByCaseIds(@Param("caseIds") Object[] caseIds);

    List<StressTestFileEntity> queryListForDelete(Map<String, Object> map);

    StressTestFileEntity queryObjectForClone(Map<String, Object> map);

    List<ApiTestFileEntity> queryEntity(@Param("caseId") Long caseId);

    int updateStatusBatch(Map<String, Object> map);
}
