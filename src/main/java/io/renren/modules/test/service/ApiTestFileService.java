package io.renren.modules.test.service;


import io.renren.modules.test.entity.InterfaceTestEntity;
import io.renren.modules.test.entity.ApiTestFileEntity;
import io.renren.modules.test.entity.ApiTestReportsEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 接口测试用例文件管理类
 *
 * @author caoting
 * @email caoting1104@gmail.com
 * @date 2019-06-19 12:12:46
 */
public interface ApiTestFileService {

    /**
     * 根据ID，查询文件
     */
    ApiTestFileEntity queryObject(Long fileId);

    /**
     * 查询文件列表
     */
    List<ApiTestFileEntity> queryList(Map<String, Object> map);

    /**
     * 查询文件列表
     */
    List<ApiTestFileEntity> queryList(Long caseId);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存接口测试用例文件
     */
    void save(ApiTestFileEntity apiCaseFile);

    /**
     * 保存性能测试用例文件
     */
    void save(MultipartFile file, String filePath, InterfaceTestEntity apiCase, ApiTestFileEntity apiCaseFile);

    /**
     * 更新性能测试用例信息
     */
    void update(ApiTestFileEntity apiTestFile);

    /**
     * 更新性能测试用例信息
     */
    void update(ApiTestFileEntity apiTestFile, ApiTestReportsEntity apiTestReports);

    /**
     * 更新性能测试用例信息
     */
    void update(MultipartFile file, String filePath, InterfaceTestEntity apiCase, ApiTestFileEntity apiCaseFile);

    /**
     * 批量更新性能测试用例状态
     */
    void updateStatusBatch(ApiTestFileEntity apiTestFile);

    /**
     * 批量删除
     */
    void deleteBatch(Object[] fileIds);

    /**
     * 立即执行
     */
    String run(Map<String, Object> params);

    /**
     * 立即停止
     */
    void stop(Long[] fileIds);

    /**
     * 获取文件路径，是文件的真实绝对路径
     */
    String getFilePath(ApiTestFileEntity apiTestFile);
    
}

