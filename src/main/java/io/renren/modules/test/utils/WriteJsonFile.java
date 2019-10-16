package io.renren.modules.test.utils;

import io.renren.common.utils.DateUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author CT
 * @date 2019/6/2914:03
 * @describe WriteJsonFile用于
 */

public class WriteJsonFile {


    public static void WriteConfigJson(String args,String filePath) {
        String src = filePath;

        File file = new File(src);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            if(file.exists()){
                SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN_4DIR);//设置日期格式
                String date = df.format(new Date());
                String filePathNew = filePath.substring(0,filePath.lastIndexOf("."))+"_"+date+".json";
                file.renameTo(new File(filePathNew));
            }
            file.delete();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(args);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
