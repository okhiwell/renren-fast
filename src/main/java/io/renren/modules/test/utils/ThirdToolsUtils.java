package io.renren.modules.test.utils;

import io.renren.common.exception.RRException;
import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.sys.service.SysConfigService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author CT
 * @date 2019/6/1814:58
 * @describe ApiTestUtils用于
 */
@Component
public class ThirdToolsUtils {

    private static SysConfigService sysConfigService = (SysConfigService) SpringContextUtils.getBean("sysConfigService");


    public final static String API_CASES_HOME_KEY = "API_CASES_HOME_KEY";
    public final static String API_CASES_REPLACE_FILE_KEY= "API_CASES_REPLACE_FILE_KEY";
    public final static String API_REPORT_HOME_KEY = "API_REPORT_HOME_KEY";

    public final static String TOOLS_TYPE_MONITER = "moniter";
    public final static String TOOLS_TYPE_BIGDATA = "bigdata";
    public final static String TOOLS_TYPE_HELPER = "helper";
    public final static String TOOLS_TYPE_DOCKER = "docker";



}
