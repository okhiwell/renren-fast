package io.renren.modules.test.controller;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.renren.common.exception.RRException;
import io.renren.common.utils.Query;
import io.renren.modules.test.entity.MockApiInfoFullEntity;
import io.renren.modules.test.entity.NodeEntity;
import io.renren.modules.test.entity.StressTestSlaveEntity;
import io.renren.modules.test.service.MockApiInfoFullService;
import io.renren.modules.test.service.StressTestSlaveService;
import io.renren.modules.test.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
 * 
 *
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-27 13:40:12
 */
@RestController
@RequestMapping("/test/mock")
public class MockApiInfoFullController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiTestUtils apiTestUtils;


    @Autowired
    private MockApiInfoFullService mockApiInfoFullService;

    @Autowired
    private StressTestSlaveService stressTestSlaveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("test:stress:list")
    public R list(@RequestParam Map<String, Object> params) {
        logger.info(params.toString());
        Query query = new Query(StressTestUtils.filterParms(params));
        List<MockApiInfoFullEntity> apiMockTestList = mockApiInfoFullService.queryList(query);
        int total = mockApiInfoFullService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(apiMockTestList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{url}")
    @RequiresPermissions("test:stress:mock")
    public R info(@PathVariable("url") String url) {
        MockApiInfoFullEntity testMockApiinfofull = mockApiInfoFullService.queryObject(url);
        return R.ok().put("testMockApiinfofull", testMockApiinfofull);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("test:stress:save")
    public R save(@RequestBody MockApiInfoFullEntity testMockApiinfofull) {
        mockApiInfoFullService.save(testMockApiinfofull);
        return R.ok();
    }


    /**
     * 保存
     */
    @RequestMapping("/saveorupdate")
    @RequiresPermissions("test:stress:saveorupdate")
    public R saveOrUpdate(@RequestBody MockApiInfoFullEntity testMockApiinfofull) {
        MockApiInfoFullEntity mockApiInfoFullEntity = mockApiInfoFullService.queryObject(testMockApiinfofull.getUrl());
        if(mockApiInfoFullEntity==null){
            mockApiInfoFullService.save(testMockApiinfofull);
        }else{
            if(mockApiInfoFullEntity.getUrl().equalsIgnoreCase(testMockApiinfofull.getUrl())) {
                mockApiInfoFullService.insertOrUpdate(testMockApiinfofull);
            }else{
                throw new RRException("不允许修改URL的值");
            }
        }

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("test:stress:update")
    public R update(@RequestBody MockApiInfoFullEntity testMockApiinfofull) {
        mockApiInfoFullService.update(testMockApiinfofull);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("test:stress:delete")
    public R delete(@RequestBody String[] urls) {
        mockApiInfoFullService.deleteBatch(urls);
        return R.ok();
    }


    /**
     * 删除历史配置文件
     */
    @RequestMapping("/deletefile")
    @RequiresPermissions("test:stress:deletefile")
    public R deleteFile(@RequestBody String[] urls) {
        String fileFile = apiTestUtils.getMockServiceConfFilePath();
        String fileDir = fileFile.substring(0, fileFile.lastIndexOf("/"));

        StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP("localhost", "22");
        logger.info("stressTestSlaveEntity:" + stressTestSlaveEntity.toString());
        SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);

        // 1、先监听端口是否已经启动：
        String cmds1 = "cd  " + fileDir + " && rm -f conf_*.json && ls -al";
        String res1 = ssh2Utils.runCommand(cmds1);
        logger.info(res1);
        return R.ok();
    }



    /**
     * 导出文件(配置/export/confile与API接口/export/pdfile)
     */
    @RequestMapping("/export/{filetype}")
    @RequiresPermissions("test:stress:export")
    public ResponseEntity<InputStreamResource> exportConfFile(@PathVariable("filetype") String filetype) throws IOException{

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));

        FileSystemResource fileResource = new FileSystemResource(apiTestUtils.getMockServicePdfFilePath());
        if(filetype.equals("confile")) {
            headers.add("Content-Disposition","attachment;filename=" + "conf.json");
            fileResource = new FileSystemResource(apiTestUtils.getMockServiceConfFilePath());
        }else{
            headers.add("Content-Disposition","attachment;filename=" + "API_DESC.pdf");
        }

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(fileResource.contentLength())
                .body(new InputStreamResource(fileResource.getInputStream()));
    }

//    @Test
//    public void testTemp(){
//        String fileFile = "/mnt/share/codes/PycharmProjects/smartwork/firework/control/api/conf.json";
//        String fileDir = fileFile.substring(0,fileFile.lastIndexOf("/"));
//        logger.info("Mock Conf File Dir:"+fileDir);
//
//        StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP("localhost", "22");
//        logger.info("stressTestSlaveEntity:" + stressTestSlaveEntity.toString());
//        SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);
//
//        // 1、先监听端口是否已经启动：
//        String cmds1 = "cd  " + fileDir + " && rm -f conf_*.json && ls -al";
//        String res1 = ssh2Utils.runCommand(cmds1);
//        logger.info(res1);
//
//
//
//    }




    /**
     * 重新生成conf文件
     */
    @RequestMapping("/genrateconffile")
    @RequiresPermissions("test:stress:genrateconffile")
    public R reMakeConfFile(@RequestBody MockApiInfoFullEntity testMockApiinfofull){
        // 重新写conf文件
        List<MockApiInfoFullEntity> listMockApiinfofull = mockApiInfoFullService.queryAll();

        JSONArray  json_list = new JSONArray();

        for(MockApiInfoFullEntity mockEntity:listMockApiinfofull){
            JSONObject json_entity = JSONObject.fromObject(mockEntity);
            JSONObject json_entity_write = new JSONObject();

            JSONObject request = new JSONObject();
            JSONObject response = new JSONObject();

            request.put("uri",mockEntity.getUrl().replace("_","/"));
            request.put("method",mockEntity.getMethod().toUpperCase());

            String header = mockEntity.getHeaders();
            if(header!=null && !header.isEmpty()) {
                request.put("headers", JSONObject.fromObject(header));
            }

            String cookies = mockEntity.getCookies();
            if(cookies!=null && !cookies.isEmpty()) {
                request.put("cookies", JSONObject.fromObject(cookies));
            }

            String forms = mockEntity.getForms();
            if(forms!=null && !forms.isEmpty()){
                request.put("forms",JSONObject.fromObject(forms));
            }

            String query = mockEntity.getQueries();
            if(query!=null && !query.isEmpty()) {
                request.put("queries", JSONObject.fromObject(query));
            }

            json_entity_write.put("request",request);

            response.put("json",JSONObject.fromObject(mockEntity.getJson()));
            json_entity_write.put("response",response);

            json_list.add(json_entity_write);
        }

        String toWriteString = json_list.toString();
        logger.info("Write File String:"+toWriteString);
        String fileFile = apiTestUtils.getMockServiceConfFilePath();
        WriteJsonFileThread writeJsonFileThread = new WriteJsonFileThread(toWriteString,fileFile);
        writeJsonFileThread.start();
        logger.info("Write Conf File Doing!");

        WritePdfFileThread writePdfFileThread = new WritePdfFileThread(listMockApiinfofull,"API描述文档",apiTestUtils.getMockServicePdfFilePath());
        writePdfFileThread.start();
        logger.info("Write PDF File Doing!");

        return R.ok();
    }


    /**
     * 对接口可用性进行校验
     */
    @RequestMapping("/valid/{url}")
    @RequiresPermissions("test:stress:valid")
    public R valid(@PathVariable("url") String url) {
        MockApiInfoFullEntity testMockApiinfofull = mockApiInfoFullService.queryObject(url);
        String header = testMockApiinfofull.getHeaders();
        String cookies = testMockApiinfofull.getCookies();
        String params = testMockApiinfofull.getQueries();
        String type_api = testMockApiinfofull.getMethod();
        String mock_url = testMockApiinfofull.getUrl().replace("_","/");

        logger.info(testMockApiinfofull.toString());


        OkHttpClient client = new OkHttpClient();



        Request.Builder request1 = new Request.Builder();
        String urlReq = apiTestUtils.getMockService() + mock_url; // 还需要再拼接

        if(header!=null && !header.isEmpty()){
            try{
                JSONObject json_header = JSONObject.fromObject(header);
                for(Object keyins:json_header.keySet()){
                    logger.info("Header-key:"+keyins.toString()+"Header-value:"+json_header.get(keyins).toString());
                    request1.addHeader(keyins.toString(),json_header.get(keyins).toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if(cookies!=null && !cookies.isEmpty()){
            try{
                JSONObject json_cookies = JSONObject.fromObject(cookies);
                for(Object keyins:json_cookies.keySet()){
                    logger.info("Cookies-key:"+keyins.toString()+"Cookies-value:"+json_cookies.get(keyins).toString());
                    request1.addHeader(keyins.toString(),json_cookies.get(keyins).toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(type_api.toLowerCase().equalsIgnoreCase("delete")){
            logger.info("Method-Type:Delete");
            request1.delete();
        }else if(type_api.toLowerCase().equalsIgnoreCase("put")){
            okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(null, "" );
            logger.info("Method-Type:Put");
            request1.put(requestBody);
        }else if(type_api.toLowerCase().equalsIgnoreCase("get")){
            logger.info("Method-Type:Get");
            request1.get();
        }else{
            okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(null, "" );
            logger.info("Method-Type:Post");
            request1.post(requestBody);
        }

        if(params!=null && !params.isEmpty()){
            try {
                urlReq += "?";
                JSONObject json_params = JSONObject.fromObject(params);
                for (Object keyins : json_params.keySet()) {
                    logger.info("Params-key:" + keyins.toString() + "Params-value:" + json_params.get(keyins).toString());
                    urlReq += keyins.toString() + "=" + json_params.get(keyins).toString() + "&";
                }
                urlReq = urlReq.substring(0, urlReq.length() - 1);
                logger.info("url:"+urlReq);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        request1.url(urlReq);
        Request request = request1.build();
        try {
            Response response = client.newCall(request).execute();
            logger.info("Enter------------->");
            String body = response.body().string();
//            InputStream inputStream = response.body().byteStream();
//
//            byte[] buf = new byte[1024];
//            StringBuffer sb = new StringBuffer();
//            try {
//                while (inputStream.read(buf) != -1) {
//                    sb.append(new String(buf, "utf-8"));
//                }
//            }catch (IOException e) {
//                e.printStackTrace();
//            }

            logger.info("response.body():"+body);
            if(body.equals(testMockApiinfofull.getJson())){
                logger.info("response.body():"+body+"     testMockApiinfofull.getJson():"+testMockApiinfofull.getJson());
            }
            testMockApiinfofull.setStatus(1);
            mockApiInfoFullService.update(testMockApiinfofull);
            throw new RRException("Mock接口"+mock_url+"校验成功!  响应数据:"+response.body().toString());
        } catch (IOException e) {
            throw new RRException("Mock接口"+mock_url+"校验不成功,请再check");
        }finally{
            return R.ok();
        }
    }









    


}
