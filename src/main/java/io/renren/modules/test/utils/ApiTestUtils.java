package io.renren.modules.test.utils;

import io.renren.common.exception.RRException;
import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.sys.service.SysConfigService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static io.renren.common.utils.ConfigConstant.OS_NAME_LC;

/**
 * @author CT
 * @date 2019/6/1814:58
 * @describe ApiTestUtils用于
 */
@Component
public class ApiTestUtils {

    private static SysConfigService sysConfigService = (SysConfigService) SpringContextUtils.getBean("sysConfigService");

    //0：初始状态  1：正在运行  2：成功执行  3：运行出现异常
    public static final Integer INITIAL = 0;
    public static final Integer RUNNING = 1;
    public static final Integer RUN_SUCCESS = 2;
    public static final Integer RUN_ERROR = 3;



    public final static String API_CASES_HOME_KEY = "API_CASES_HOME_KEY";
    public final static String API_CASES_REPLACE_FILE_KEY= "API_CASES_REPLACE_FILE_KEY";
    public final static String API_REPORT_HOME_KEY = "API_REPORT_HOME_KEY";

    public final static String API_MOCK_SERVICE_KEY = "API_MOCK_SERVICE_KEY"; //http://localhost:9410
    public final static String API_MOCK_SERVICE_CONF_FILE_KEY = "API_MOCK_SERVICE_CONF_FILE_KEY";
    public final static String API_MOCK_SERVICE_DOC_FILE_KEY = "API_MOCK_SERVICE_DOC_FILE_KEY";
    public final static String API_MOCK_SERVICE_PDF_FILE_KEY = "API_MOCK_SERVICE_PDF_FILE_KEY";
    public final static String API_MOCK_SERVICE_PATH_KEY = "API_MOCK_SERVICE_PATH_KEY";

    public String getCasePath() {
        return sysConfigService.getValue(API_CASES_HOME_KEY);
    }
    public String getReportPath(){return sysConfigService.getValue(API_REPORT_HOME_KEY);}
    public String getMockService() {return sysConfigService.getValue(API_MOCK_SERVICE_KEY);}
    public String getMockServiceConfFilePath() {return sysConfigService.getValue(API_MOCK_SERVICE_CONF_FILE_KEY);}
    public String getMockServiceDocFilePath() {return sysConfigService.getValue(API_MOCK_SERVICE_DOC_FILE_KEY);}
    public String getMockServicePdfFilePath() {return sysConfigService.getValue(API_MOCK_SERVICE_PDF_FILE_KEY);}

    public boolean isReplaceFile() {
        return Boolean.valueOf(sysConfigService.getValue(API_CASES_REPLACE_FILE_KEY));
    }

    public void saveFile(MultipartFile multipartFile, String filePath) {
        try {
            File file = new File(filePath);
            FileUtils.forceMkdirParent(file);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RRException("保存文件异常失败", e);
        }
    }

    public String getMockServicePath() {
        return sysConfigService.getValue(API_MOCK_SERVICE_PATH_KEY);
    }
}
