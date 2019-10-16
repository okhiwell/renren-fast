package io.renren.modules.test.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.renren.common.annotation.SysLog;
import io.renren.common.utils.Query;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.dao.SysUserTokenDao;
import io.renren.modules.sys.entity.SysUserTokenEntity;
import io.renren.modules.test.entity.ApiTestFileEntity;
import io.renren.modules.test.service.ApiTestFileService;
import io.renren.modules.test.utils.StressTestUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 接口测试用例文件表
 *
 * @author caoting
 * @email caoting1104@gmail.com
 * @date 2019-06-19 12:12:46
 */
@RestController
@RequestMapping("/test/apiFile")
public class ApiTestFileController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiTestFileService apiTestFileService;


    @Autowired
    SysUserTokenDao sysUserTokenDao;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("test:stress:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(StressTestUtils.filterParms(params));
        List<ApiTestFileEntity> jobList = apiTestFileService.queryList(query);
        int total = apiTestFileService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(jobList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{fileId}")
    @RequiresPermissions("test:stress:info")
    public R info(@PathVariable("fileId") Long fileId){
		ApiTestFileEntity testApiCaseFile = apiTestFileService.queryObject(fileId);
        return R.ok().put("apiCaseReport", testApiCaseFile);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("test:stress:save")
    public R save(@RequestBody ApiTestFileEntity testApiCaseFile){
		apiTestFileService.save(testApiCaseFile);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("test:stress:update")
    public R update(@RequestBody ApiTestFileEntity testApiCaseFile){
        ValidatorUtils.validateEntity(testApiCaseFile);

        if (testApiCaseFile.getFileIdList() != null && testApiCaseFile.getFileIdList().length > 0) {
            apiTestFileService.updateStatusBatch(testApiCaseFile);
        } else {
            apiTestFileService.update(testApiCaseFile);
        }

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("test:stress:delete")
    public R delete(@RequestBody Long[] fileIds){
		apiTestFileService.deleteBatch(fileIds);

        return R.ok();
    }



    /**
     * 立即执行接口测试脚本。
     */
    @SysLog("立即执行接口测试用例脚本文件")
    @RequestMapping("/run")
    @RequiresPermissions("test:stress:run")
    public R run(@RequestBody Map<String, Object> params) {
        logger.info(params.toString());
        return R.ok(apiTestFileService.run(params));
    }

    /**
     * 立即停止接口测试脚本，仅有使用api方式时，才可以单独停止。
     */
    @SysLog("立即停止接口测试用例脚本文件")
    @RequestMapping("/stop")
    @RequiresPermissions("test:stress:stop")
    public R stop(@RequestBody Long[] fileIds) {
        apiTestFileService.stop(fileIds);
        return R.ok();
    }



    /**
     * 下载文件
     */
    @RequestMapping("/downloadFile/{fileId}")
    @RequiresPermissions("test:stress:fileDownLoad")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileId") Long fileId) throws IOException {
        ApiTestFileEntity apiTestFile = apiTestFileService.queryObject(fileId);
        logger.info("downloadFile-ApiTestFileEntity"+apiTestFile.toString());
        FileSystemResource fileResource = new FileSystemResource(apiTestFileService.getFilePath(apiTestFile));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Content-Disposition",
                "attachment;filename=" + apiTestFile.getOriginName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        SysUserTokenEntity sysUserTokenEntity = sysUserTokenDao.queryByUserId(1L);
        headers.add("token",sysUserTokenEntity.getToken());
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));


        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(fileResource.contentLength())
                .body(new InputStreamResource(fileResource.getInputStream()));
    }


}
