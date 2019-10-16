package io.renren.modules.test.dao;

import io.renren.modules.sys.dao.BaseDao;
import io.renren.modules.test.entity.ThirdToolsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 三方工具表
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-19 18:41:49
 */
@Mapper
public interface ThirdToolsDao extends BaseDao<ThirdToolsEntity> {

    int queryTotalByType(Map<String, Object> toolsType);

    List<ThirdToolsEntity> queryListByType(Map<String, Object> toolsType);
}
