package io.renren.modules.test.utils;

import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.sys.service.SysConfigService;

/**
 * @author CT
 * @date 2019/7/322:12
 * @describe Constant用于
 */

public class Constant {

    private static SysConfigService sysConfigService = (SysConfigService) SpringContextUtils.getBean("sysConfigService");

    public final static String SMALL_TOOLS_OP_IP_KEY = "SMALL_TOOLS_OP_IP_KEY";

    public String getSmallToolsOpIpKey() {
        return sysConfigService.getValue(SMALL_TOOLS_OP_IP_KEY);
    }

}
