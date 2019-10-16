package io.renren.modules.test.utils;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author CT
 * @date 2019/6/2016:02
 * @describe StreamHandler用于
 */

public class StreamHandler extends Thread{

    org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    InputStream m_inputStream;
    String m_type;

    public StreamHandler(InputStream is, String type){
        this.m_inputStream = is;
        this.m_type = type;
    }

    @Override
    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;

//        ArrayList<String> res = new  ArrayList<String>();

        try{
            //设置编码方式，否则输出中文时容易乱码
            isr = new InputStreamReader(m_inputStream, "GBK");
            br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null){
                logger.info("PRINT > " + m_type + " : " + line);
            }
        }catch (IOException ioe){
                ioe.printStackTrace();
        }finally{
            try{
                br.close();
                isr.close();
            }catch (IOException ex){
                logger.error(StreamHandler.class.getName());
            }
        }
        }
    
}
