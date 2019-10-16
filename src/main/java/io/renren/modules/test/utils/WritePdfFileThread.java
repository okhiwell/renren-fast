package io.renren.modules.test.utils;

import io.renren.modules.test.entity.MockApiInfoFullEntity;

import java.util.List;

/**
 * @author CT
 * @date 2019/6/2914:05
 * @describe WriteFileThread用于
 */

public class WritePdfFileThread extends Thread{
    List<MockApiInfoFullEntity> toWriteString = null;//定义线程内变量
    String filePath = "";
    String title = "";
    WritePdfFile wpdf = new WritePdfFile();

    public WritePdfFileThread( List<MockApiInfoFullEntity> stringJson, String title, String filePath){
        //定义带参数的构造函数,达到初始化线程内变量的值
        this.toWriteString = stringJson;
        this.filePath = filePath;
        this.title = title;
    }

    @Override
    public void run() {
        wpdf.generatePDF(this.toWriteString, this.title,this.filePath);
    }
}