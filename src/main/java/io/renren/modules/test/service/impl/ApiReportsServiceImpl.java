package io.renren.modules.test.service.impl;

import io.renren.common.exception.RRException;
import io.renren.modules.test.dao.ApiTestReportsDao;
import io.renren.modules.test.entity.ApiTestFileEntity;
import io.renren.modules.test.utils.ApiTestUtils;
import io.renren.modules.test.utils.CommonUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import io.renren.modules.test.entity.ApiTestReportsEntity;
import io.renren.modules.test.service.ApiTestReportsService;


@Service("testApiCaseReportsService")
public class ApiReportsServiceImpl implements ApiTestReportsService {

    Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private ApiTestReportsDao apiTestReportsDao;

    @Autowired
    private ApiTestUtils apiTestUtils;


    @Override
    public ApiTestReportsEntity queryObject(Long reportId) {
        return apiTestReportsDao.queryObject(reportId);
    }

    @Override
    public List<ApiTestReportsEntity> queryList(Map<String, Object> map) {
        return apiTestReportsDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return apiTestReportsDao.queryTotal(map);
    }

    @Override
    public void save(ApiTestReportsEntity apiCaseReports) {
        apiTestReportsDao.save(apiCaseReports);
    }

    @Override
    public void update(ApiTestReportsEntity apiCaseReports) {
        apiTestReportsDao.update(apiCaseReports);
    }

    @Override
    public void deleteBatch(Long[] reportIds){
        Arrays.asList(reportIds).stream().forEach(reportId -> {
            ApiTestReportsEntity apiTestReport = apiTestReportsDao.queryObject((Long) reportId);
            String FilePath = apiTestUtils.getReportPath()+File.separator + apiTestReport.getReportName();

            try {
                FileUtils.forceDelete(new File(FilePath));
            } catch (FileNotFoundException e) {
                logger.error("要删除的文件找不到(删除成功)  " + e.getMessage());
            } catch (IOException e) {
                throw new RRException("删除文件异常失败", e);
            }
            //删除缓存
//            StressTestUtils.samplingStatCalculator4File.remove(fileId);
        });
        apiTestReportsDao.deleteBatch(reportIds);

    }

    @Override
    public void deleteBatchCsv(Long[] reportIds) {

    }

    @Override
    public void deleteBatchXml(Long[] reportIds) {

    }

    @Override
    public void createReport(Long[] reportIds) {// TODO

    }

    @Override
    public void createReport(ApiTestReportsEntity reportsEntity) {// TODO
    }

    @Override
    public void deleteReportCSV(ApiTestReportsEntity stressCaseReports) {

    }

    @Override
    public void deleteReportXml(ApiTestReportsEntity stressCaseReports) {

    }

    @Override
    public FileSystemResource getZipFile(ApiTestReportsEntity reportsEntity) throws IOException {
        String reportName = reportsEntity.getReportName();
        if(reportName.endsWith("/html/index.html")){
            //测试报告文件目录
            String reportPathDir = apiTestUtils.getReportPath()+File.separator+reportName.substring(0, reportName.indexOf("html"));
            logger.info(reportPathDir);
            //如果测试报告文件目录不存在，直接打断
            File reportDir = new File(reportPathDir);
            if (!reportDir.exists()) {
                throw new RRException("请先生成测试报告！");
            }

            //zip文件名
            String reportZipPath = reportPathDir + ".zip";

            FileSystemResource zipFileResource = new FileSystemResource(reportZipPath);

            if (!zipFileResource.exists()) {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(reportZipPath), Charset.forName("GBK"));
                try {
                    File zipFile = new File(reportPathDir);
                    CommonUtils.zip(out, zipFile, zipFile.getName());
                } finally {
                    out.close();
                }
            }
            return zipFileResource;

        }else{
            FileSystemResource zipFileResource = new FileSystemResource( apiTestUtils.getReportPath()+File.separator+reportName);
            return zipFileResource;
        }
    }
}