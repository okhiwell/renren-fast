package io.renren.modules.test.service;

import io.renren.modules.test.entity.ApiTestReportsEntity;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 性能测试报告文件表
 *
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-18 14:32:11
 */
public interface ApiTestReportsService {

    /**
     * 根据ID，查询文件
     */
    ApiTestReportsEntity queryObject(Long reportId);

    /**
     * 查询文件列表
     */
    List<ApiTestReportsEntity> queryList(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存性能测试用例文件
     */
    void save(ApiTestReportsEntity stressCaseReports);

    /**
     * 更新性能测试用例信息
     */
    void update(ApiTestReportsEntity stressCaseReports);

    /**
     * 批量删除
     */
    void deleteBatch(Long[] reportIds);

    /**
     * 批量删除测试报告的来源CSV文件
     */
    void deleteBatchCsv(Long[] reportIds);

    /**
     * 批量删除测试报告的来源Xml文件
     */
    void deleteBatchXml(Long[] reportIds);

    /**
     * 生成测试报告
     */
    void createReport(Long[] reportIds);

    /**
     * 生成测试报告
     */
    void createReport(ApiTestReportsEntity reportsEntity);

    /**
     * 批量删除测试报告的来源CSV文件
     */
    void deleteReportCSV(ApiTestReportsEntity stressCaseReports);

    void deleteReportXml(ApiTestReportsEntity stressCaseReports);


    FileSystemResource getZipFile(ApiTestReportsEntity reportsEntity) throws IOException;
}

