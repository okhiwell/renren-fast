package io.renren.modules.test.controller;

import java.io.File;
import java.util.*;

import io.renren.common.annotation.SysLog;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.test.entity.ApiTestFileEntity;
import io.renren.modules.test.service.ApiTestFileService;
import io.renren.modules.test.utils.ApiTestUtils;
import io.renren.modules.test.utils.StressTestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.test.entity.InterfaceTestEntity;
import io.renren.modules.test.service.InterfaceTestService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * 接口测试用例表
 *
 * @author caoting
 * @email caoting1104@gmail.com
 * @date 2019-06-19 11:32:35
 */
@RestController
@RequestMapping("/test/api")
public class InterfaceTestController {


    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InterfaceTestService interfaceTestService;

    @Autowired
    private ApiTestUtils apiTestUtils;

    @Autowired
    private ApiTestFileService apiTestFileService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("test:stress:list")
    public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(StressTestUtils.filterParms(params));
        List<InterfaceTestEntity> apiTestList = interfaceTestService.queryList(query);
        int total = interfaceTestService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(apiTestList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }



    /**
     * 基于接口类别进行展示列表
     */
    @RequestMapping("/apitypelist")
    @RequiresPermissions("test:stress:apitypelist")
    public R listByApiType(@RequestParam Map<String, Object> params){
        Query query = new Query(StressTestUtils.filterParms(params));
        String apiType = (String)params.get("apitype");
        List<InterfaceTestEntity> apiTestList = interfaceTestService.queryByApiType(apiType);
        int total = interfaceTestService.queryTotalByApiType(apiType);
        PageUtils pageUtil = new PageUtils(apiTestList, total, query.getLimit(),query.getPage());
        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{caseId}")
    @RequiresPermissions("test:stress:info")
    public R info(@PathVariable("caseId") Long caseId){
		InterfaceTestEntity testApiCase = interfaceTestService.queryObject(caseId);
		logger.info(testApiCase.toString());
        return R.ok().put("apiCase", testApiCase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("test:stress:save")
    public R save(@RequestBody InterfaceTestEntity apiTestCase){
        ValidatorUtils.validateEntity(apiTestCase);
        // 生成用例时即生成用例的文件夹名，上传附件时才会将此名称落地成为文件夹。
        if (StringUtils.isEmpty(apiTestCase.getCaseDir())) {
            Date caseAddTime = new Date();
            String caseAddTimeStr = DateUtils.format(caseAddTime, DateUtils.DATE_TIME_PATTERN_4DIR);
            //random使用时间种子的随机数,避免了轻度并发造成文件夹重名.
            String caseFilePath = caseAddTimeStr + new Random(System.nanoTime()).nextInt(1000);
            apiTestCase.setCaseDir(caseFilePath);
        }

        interfaceTestService.save(apiTestCase);

        return R.ok();
    }



    /**
     * 修改接口测试用例
     */
    @SysLog("修改接口测试用例")
    @RequestMapping("/update")
    @RequiresPermissions("test:stress:update")
    public R update(@RequestBody InterfaceTestEntity apiTestCase) {
        ValidatorUtils.validateEntity(apiTestCase);

        interfaceTestService.update(apiTestCase);

        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/upload")
    @RequiresPermissions("test:stress:upload")
    public R upload(@RequestParam("files") MultipartFile multipartFile, MultipartHttpServletRequest request) {

        if (multipartFile.isEmpty()) {
            //throw new RRException("上传文件不能为空");
            // 为了前端fileinput组件提示使用。
            return R.ok().put("error","上传文件不能为空");
        }

        String originName = multipartFile.getOriginalFilename();
        //用例文件名可以是汉字毕竟是唯一标识用例内容的.
        //用例的参数化文件不允许包含汉字,避免Linux系统读取文件报错.
        String suffix = originName.substring(originName.lastIndexOf("."));
        if (!".jmx".equalsIgnoreCase(suffix) && originName.length() != originName.getBytes().length) {
            //throw new RRException("非脚本文件名不能包含汉字");
            return R.ok().put("error","非脚本文件名不能包含汉字");
        }

        String caseId = request.getParameter("caseIds");
        //允许文件名不同但是文件内容相同,因为不同的文件名对应不同的用例.
        InterfaceTestEntity apiCase = interfaceTestService.queryObject(Long.valueOf(caseId));
        //主节点master的用于保存Jmeter用例及文件的地址
        String casePath = apiTestUtils.getCasePath();

        Map<String, Object> query = new HashMap<String, Object>();
        query.put("originName", originName);
        // fileList中最多有一条记录
        List<ApiTestFileEntity> fileList = apiTestFileService.queryList(query);
        //数据库中已经存在同名文件
        if (!fileList.isEmpty()) {
            // 不允许上传同名文件
            if (!apiTestUtils.isReplaceFile()) {
                //throw new RRException("系统中已经存在此文件记录！不允许上传同名文件！");
                return R.ok().put("error","系统中已经存在此文件记录！不允许上传同名文件！");
            } else {// 允许上传同名文件方式是覆盖。
                for (ApiTestFileEntity apiCaseFile : fileList) {
                    // 如果是不同用例，但是要上传同名文件，是不允许的，这是数据库的唯一索引要求的。
                    if (!Long.valueOf(caseId).equals(apiCaseFile.getCaseId())) {
                        //throw new RRException("其他用例已经包含此同名文件！");
                        return R.ok().put("error","其他用例已经包含此同名文件！");
                    }
                    // 目的是从名称上严格区分脚本。而同名脚本不同项目模块甚至标签
                    String filePath = casePath + File.separator + apiCaseFile.getFileName();
                    apiTestFileService.save(multipartFile, filePath, apiCase, apiCaseFile);
                }
            }
        } else {// 新上传文件
            ApiTestFileEntity apiCaseFile = new ApiTestFileEntity();
            apiCaseFile.setOriginName(originName);

            //主节点master文件夹名称
            //主节点master会根据apiCase的添加时间及随机数生成唯一的文件夹,用来保存用例文件及参数化文件.
            //master的文件分开放(web页面操作无感知),slave的参数化文件统一放.
            Date caseAddTime = apiCase.getAddTime();
            String caseAddTimeStr = DateUtils.format(caseAddTime, DateUtils.DATE_TIME_PATTERN_4DIR);
            String caseFilePath;
            if (StringUtils.isEmpty(apiCase.getCaseDir())) {
                //random使用时间种子的随机数,避免了轻度并发造成文件夹重名.
                caseFilePath = caseAddTimeStr + new Random(System.nanoTime()).nextInt(1000);
                apiCase.setCaseDir(caseFilePath);
            } else {
                caseFilePath = apiCase.getCaseDir();
            }

            String filePath;
            apiCaseFile.setFileName(caseFilePath + File.separator + originName);
            filePath = casePath + File.separator + caseFilePath + File.separator + originName;


            //保存文件信息
            apiCaseFile.setCaseId(Long.valueOf(caseId));
            apiTestFileService.save(multipartFile, filePath, apiCase, apiCaseFile);
        }

        return R.ok();
    }

 

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("test:stress:delete")
    public R delete(@RequestBody Long[] caseIds){

        for (Long caseId : caseIds) {
            List<ApiTestFileEntity> fileList = apiTestFileService.queryList(caseId);
            if(!fileList.isEmpty()){ //判断是否有关联脚本文件
                ArrayList fileIdList = new ArrayList();
                for (ApiTestFileEntity stressTestFile : fileList) {
                    fileIdList.add(stressTestFile.getFileId());
                }
                apiTestFileService.deleteBatch(fileIdList.toArray());
            }
        }
        if (caseIds.length > 0){
            interfaceTestService.deleteBatch(caseIds);
        }

        return R.ok();
    }

}
