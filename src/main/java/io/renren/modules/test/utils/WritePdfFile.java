package io.renren.modules.test.utils;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.render.RenderAPI;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import io.renren.common.utils.DateUtils;
import io.renren.modules.test.entity.MockApiInfoFullEntity;
import org.dom4j.DocumentException;
import org.json.JSONException;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


/********************
 * @auther:
 * @decription:
 * @date: 4/13/2018
 * @version: 0.0.1
 **********************/
public class WritePdfFile {

    @Test
    public void  TestCreatePdf(){
        List<MockApiInfoFullEntity> infoWrite = new ArrayList<MockApiInfoFullEntity>();
        WriteApiDescPdf( infoWrite,"API描述文档","test.pdf");
    }


    public void WriteApiDescPdf(List<MockApiInfoFullEntity> args, String title,String filePath) {
        String src = filePath;

        File file = new File(src);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            if(file.exists()){
                SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN_4DIR);//设置日期格式
                String date = df.format(new Date());
                String filePathNew = filePath.substring(0,filePath.lastIndexOf("."))+"_"+date+".pdf";
                file.renameTo(new File(filePathNew));
            }
            file.delete();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        generatePDF( args,title,filePath);
    }



    // 通用的生成报告的模板
    public void generatePDF(List<MockApiInfoFullEntity> basicInfo, String title, String filename){
        try {
            //BaseFont baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font titleFont = new Font(baseFont,30,Font.BOLD);
            Font caseFont = new Font(baseFont, 20, Font.BOLD);
            Font stepFont = new Font(baseFont, 15);

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            FileOutputStream os = new FileOutputStream(filename);
            PdfWriter wt = PdfWriter.getInstance(document, os);
            wt.setStrictImageSequence(true);
            // 页眉页脚
            Rectangle rect = new Rectangle(36, 54, 559, 788);
            wt.setBoxSize("art", rect);
            document.open();
            Paragraph para_title = new Paragraph(title,titleFont);
            para_title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(para_title);
            document.addTitle(title);
            document.addAuthor("CT");
            document.addCreationDate();


            this.addContent(document,basicInfo,caseFont,stepFont);
            document.close();
            os.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (com.itextpdf.text.DocumentException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void addContent(Document doc, java.util.List<MockApiInfoFullEntity> basicInfo, Font caseFont, Font stepFont) throws DocumentException, JSONException, com.itextpdf.text.DocumentException {
        if (!basicInfo.isEmpty()) {
            ApiTestUtils apiTestUtils = new ApiTestUtils();
            String mockeServer = apiTestUtils.getMockService();
            Integer index = 0;
            for (MockApiInfoFullEntity entry : basicInfo) {
                index += 1;
                String key = entry.getUrl().replace("_","/");
                Paragraph para1 = new Paragraph("接口"+index.toString()+"   "+key, caseFont);
                para1.setAlignment(Paragraph.ALIGN_LEFT);
                doc.add(para1);

                Paragraph paraUrl = new Paragraph("调用地址："+mockeServer+key, stepFont);
                paraUrl.setAlignment(Paragraph.ALIGN_LEFT);
                doc.add(paraUrl);

                Paragraph paraMethod = new Paragraph("请求方式："+entry.getMethod(), stepFont);
                paraMethod.setAlignment(Paragraph.ALIGN_LEFT);
                doc.add(paraMethod);

                Paragraph paraHeader = new Paragraph("请求头部："+entry.getHeaders(), stepFont);
                paraHeader.setAlignment(Paragraph.ALIGN_LEFT);
                doc.add(paraHeader);

                Paragraph paraParams = new Paragraph("请求参数："+entry.getQueries(), stepFont);
                paraParams.setAlignment(Paragraph.ALIGN_LEFT);
                doc.add(paraParams);


                Paragraph paraResponse = new Paragraph("响应信息："+entry.getJson(), stepFont);
                paraResponse.setAlignment(Paragraph.ALIGN_LEFT);
                doc.add(paraResponse);

                Paragraph paraRemark = new Paragraph("接口描述："+entry.getRemark(), stepFont);
                paraRemark.setAlignment(Paragraph.ALIGN_LEFT);
                doc.add(paraRemark);

            }
        }

    }

}
