package io.renren.modules.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author CT
 * @date 2019/6/210:00
 * @describe CommonUtils用于
 */

public class CommonUtils {

    /**
     * 递归打包文件夹/文件
     */
    public static void zip(ZipOutputStream zOut, File file, String name) throws IOException {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            name += "/";
            zOut.putNextEntry(new ZipEntry(name));
            for (File listFile : listFiles) {
                zip(zOut, listFile, name + listFile.getName());
            }
        } else {
            FileInputStream in = new FileInputStream(file);
            try {
                zOut.putNextEntry(new ZipEntry(name));
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zOut.write(buffer, 0, len);
                    zOut.flush();
                }
            } finally {
                in.close();
            }
        }
    }

}
