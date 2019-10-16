package io.renren.modules.test.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.renren.common.annotation.SysLog;
import io.renren.common.exception.RRException;
import io.renren.common.utils.Query;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.dao.SysUserTokenDao;
import io.renren.modules.sys.entity.SysUserTokenEntity;
import io.renren.modules.test.utils.ApiTestUtils;
import io.renren.modules.test.utils.StressTestUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.test.entity.ApiTestReportsEntity;
import io.renren.modules.test.service.ApiTestReportsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 接口测试报告文件表
 *
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-18 14:32:11
 */
@RestController
@RequestMapping("/test/apiReports")
public class ApiTestReportsController {
    @Autowired
    private ApiTestReportsService apiTestReportsService;

    @Autowired
    SysUserTokenDao sysUserTokenDao;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("test:stress:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(StressTestUtils.filterParms(params));
        List<ApiTestReportsEntity> reportList = apiTestReportsService.queryList(query);

        int total = apiTestReportsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(reportList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }




    /**
     * 信息
     */
    @RequestMapping("/info/{reportId}")
    @RequiresPermissions("test:stress:info")
    public R info(@PathVariable("reportId") Long reportId){
		ApiTestReportsEntity testApiCaseReports = apiTestReportsService.queryObject(reportId);
        return R.ok().put("apiCaseReports", testApiCaseReports);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("test:stress:save")
    public R save(@RequestBody ApiTestReportsEntity testApiCaseReports){
		apiTestReportsService.save(testApiCaseReports);

        return R.ok();
    }
    

    /**
     * 修改性能测试用例报告文件
     */
    @SysLog("修改性能测试用例报告文件")
    @RequestMapping("/update")
    @RequiresPermissions("test:stress:reportUpdate")
    public R update(@RequestBody ApiTestReportsEntity apiCaseReport) {
        ValidatorUtils.validateEntity(apiCaseReport);
        apiTestReportsService.update(apiCaseReport);
        return R.ok();
    }

    /**
     * 删除指定测试报告及文件
     */
    @SysLog("删除接口测试报告")
    @RequestMapping("/delete")
    @RequiresPermissions("test:stress:reportDelete")
    public R delete(@RequestBody Long[] reportIds) {
        apiTestReportsService.deleteBatch(reportIds);
        return R.ok();
    }

    /**
     * 删除指定测试报告的测试结果文件，目的是避免占用空间太大
     */
    @SysLog("删除接口测试报告结果文件")
    @RequestMapping("/deleteCsv")
    @RequiresPermissions("test:stress:reportDeleteCsv")
    public R deleteCsv(@RequestBody Long[] reportIds) {
        apiTestReportsService.deleteBatchCsv(reportIds);
        return R.ok();
    }

    
    /**
     * 删除指定测试报告的测试结果文件，目的是避免占用空间太大
     */
    @SysLog("删除接口测试报告结果文件")
    @RequestMapping("/deleteXml")
    @RequiresPermissions("test:stress:reportDeleteXml")
    public R deleteXml(@RequestBody Long[] reportIds) {
        apiTestReportsService.deleteBatchXml(reportIds);
        return R.ok();
    }

    /**
     * 生成测试报告及文件
     */
    @SysLog("生成接口测试报告")
    @RequestMapping("/createReport")
    @RequiresPermissions("test:stress:reportCreate")
    public R createReport(@RequestBody Long[] reportIds) {
        for (Long reportId : reportIds) {
            ApiTestReportsEntity apiTestReport = apiTestReportsService.queryObject(reportId);

            //首先判断，如果file_size为0或者空，说明没有结果文件，直接报错打断。
            if (apiTestReport.getFileSize() == 0L || apiTestReport.getFileSize() == null) {
                throw new RRException("找不到测试结果文件，无法生成测试报告！");
            }
            //如果测试报告文件目录已经存在，说明生成过测试报告，直接打断
            if (ApiTestUtils.RUN_SUCCESS.equals(apiTestReport.getStatus())) {
                throw new RRException("已经存在测试报告不要重复创建！");
            }
            apiTestReportsService.createReport(apiTestReport);
        }
        return R.ok();
    }


    /**
     * 下载测试报告zip包
     */
    @SysLog("下载测试报告zip包")
    @RequestMapping("/downloadReport/{reportId}")
    @RequiresPermissions("test:stress:reportDownLoad")
    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable("reportId") Long reportId) throws IOException {
        ApiTestReportsEntity reportsEntity = apiTestReportsService.queryObject(reportId);
        FileSystemResource zipFile = apiTestReportsService.getZipFile(reportsEntity);

        String reportName = reportsEntity.getReportName();
        String attachFilePath = reportName;
        if(reportName.endsWith("/html/index.html")){
            attachFilePath = reportName.substring(0, reportName.indexOf("html"))+".zip";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Content-Disposition",
                "attachment;filename=" + attachFilePath);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        SysUserTokenEntity sysUserTokenEntity = sysUserTokenDao.queryByUserId(1L);
        headers.add("token",sysUserTokenEntity.getToken());
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));


        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(zipFile.contentLength())
                .body(new InputStreamResource(zipFile.getInputStream()));
    }

}
