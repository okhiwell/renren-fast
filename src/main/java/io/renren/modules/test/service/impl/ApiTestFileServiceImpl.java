package io.renren.modules.test.service.impl;

import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.modules.test.dao.*;
import io.renren.modules.test.entity.*;
import io.renren.modules.test.service.ApiTestFileService;
import io.renren.modules.test.service.StressTestSlaveService;
import io.renren.modules.test.utils.ApiTestUtils;
import io.renren.modules.test.utils.SSH2Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang.SystemUtils.*;

@Service("ApiTestFileService")
public class ApiTestFileServiceImpl implements ApiTestFileService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StressTestSlaveService stressTestSlaveService;

    static {
        final List<URL> jars = new LinkedList<>();
        final String JAVA_HOME = System.getenv().get("JAVA_HOME");
        final String initial_classpath = System.getenv().get("CLASSPATH"); //System.getProperty(JAVA_CLASS_PATH);

        System.out.println(initial_classpath);
        System.out.println(JAVA_HOME);
        /*
         * Does the system support UNC paths? If so, may need to fix them up
         * later
         */

        // Add standard jar locations to initial classpath
        StringBuilder classpath = new StringBuilder();
        File[] libDirs = new File[]{new File(JAVA_HOME + File.separator + "lib"),// $NON-NLS-1$ $NON-NLS-2$
                new File(JAVA_HOME + File.separator + "jre" + File.separator + "lib"),// $NON-NLS-1$ $NON-NLS-2$
                new File(JAVA_HOME + File.separator + "jre" + File.separator + "lib"+File.separator + "ext")};
        for (File libDir : libDirs) {
            File[] libJars = libDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (libJars == null) {
                new Throwable("Could not access " + libDir).printStackTrace(); // NOSONAR No logging here
                continue;
            }
            Arrays.sort(libJars); // Bug 50708 Ensure predictable order of jars
            for (File libJar : libJars) {
                try {
                    String s = libJar.getPath();

                    // Fix path to allow the use of UNC URLs

                    if (s.startsWith("\\\\") && !s.startsWith("\\\\\\")) {// $NON-NLS-1$ $NON-NLS-2$
                        s = "\\\\" + s;// $NON-NLS-1$
                    } else if (s.startsWith("//") && !s.startsWith("///")) {// $NON-NLS-1$ $NON-NLS-2$
                        s = "//" + s;// $NON-NLS-1$
                    }


                    jars.add(new File(s).toURI().toURL());// See Java bug 4496398
                    classpath.append(File.pathSeparator);
                    classpath.append(s);
                } catch (MalformedURLException e) { // NOSONAR
                    new Exception("Error adding jar:"+libJar.getAbsolutePath(), e);
                }
            }
        }

        // ClassFinder needs the classpath
        System.setProperty(JAVA_CLASS_PATH, initial_classpath + classpath.toString());
    }
    


    @Autowired
    private ApiTestFileDao apiTestFileDao;

    @Autowired
    private ApiTestReportsDao apiTestReportsDao;


    @Autowired
    private InterfaceTestDao interfaceTestDao;

    @Autowired
    private ApiTestUtils apiTestUtils;


    @Override
    public ApiTestFileEntity queryObject(Long fileId) {
        return apiTestFileDao.queryObject(fileId);
    }

    @Override
    public List<ApiTestFileEntity> queryList(Map<String, Object> map) {
        return apiTestFileDao.queryList(map);
    }

    @Override
    public List<ApiTestFileEntity> queryList(Long caseId) {
        return apiTestFileDao.queryEntity(caseId);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return apiTestFileDao.queryTotal();
    }

    @Override
    public void save(ApiTestFileEntity apiCaseFile) {
        apiTestFileDao.save(apiCaseFile);
    }

    @Override
    @Transactional
    public void save(MultipartFile multipartFile, String filePath, InterfaceTestEntity apiCase, ApiTestFileEntity apiCaseFile) {
        try {
            String fileMd5 = DigestUtils.md5Hex(multipartFile.getBytes());
            apiCaseFile.setFileMd5(fileMd5);
        } catch (IOException e) {
            throw new RRException("获取上传文件的MD5失败！", e);
        }
        if (apiCaseFile.getFileId() != null && apiCaseFile.getFileId() > 0L) {
            // 替换文件，同时修改添加时间，便于前端显示。
            apiCaseFile.setAddTime(new Date());
            update(apiCaseFile);
        } else {
            // 保存文件，同时解决第一次保存文件时实体没有写入用例名称
            apiCaseFile.setCaseName(apiCase.getCaseName());
            save(apiCaseFile);
        }
        // 肯定存在已有的用例信息
        interfaceTestDao.update(apiCase);
        logger.info("filePath-----------------:"+filePath);
        apiTestUtils.saveFile(multipartFile, filePath);
    }

    @Override
    public void update(ApiTestFileEntity apiTestFile) {
        apiTestFileDao.update(apiTestFile);
    }

    @Override
    public void update(ApiTestFileEntity apiTestFile, ApiTestReportsEntity apiTestReports) {
        update(apiTestFile);
        if (apiTestReports != null) {
            apiTestReportsDao.update(apiTestReports);
        }
    }

    @Override
    @Transactional
    public void update(MultipartFile multipartFile, String filePath, InterfaceTestEntity apiCase, ApiTestFileEntity apiCaseFile) {
        try {
            String fileMd5 = DigestUtils.md5Hex(multipartFile.getBytes());
            apiCaseFile.setFileMd5(fileMd5);
        } catch (IOException e) {
            throw new RRException("获取上传文件的MD5失败！", e);
        }
        update(apiCaseFile);
        interfaceTestDao.update(apiCase);
        logger.info("filePath-update:"+filePath);
        apiTestUtils.saveFile(multipartFile, filePath);
    }

    @Override
    public void updateStatusBatch(ApiTestFileEntity apiTestFile) {
        Map<String, Object> map = new HashMap<>();
        map.put("fileIdList", apiTestFile.getFileIdList());
        map.put("reportStatus", apiTestFile.getReportStatus());
        map.put("webchartStatus", apiTestFile.getWebchartStatus());
        map.put("debugStatus", apiTestFile.getDebugStatus());
        apiTestFileDao.updateStatusBatch(map);
    }

    @Override
    @Transactional
    public void deleteBatch(Object[] fileIds) {
        Arrays.asList(fileIds).stream().forEach(fileId -> {
            ApiTestFileEntity apiTestFile = queryObject((Long) fileId);
            String casePath = apiTestUtils.getCasePath();
            String FilePath = casePath + File.separator + apiTestFile.getFileName();

            try {
                FileUtils.forceDelete(new File(FilePath));
            } catch (FileNotFoundException e) {
                logger.error("要删除的文件找不到(删除成功)  " + e.getMessage());
            } catch (IOException e) {
                throw new RRException("删除文件异常失败", e);
            }
            //删除缓存
//            StressTestUtils.samplingStatCalculator4File.remove(fileId);
        });
        apiTestFileDao.deleteBatch(fileIds);
    }

    @Override
    public String run(Map<String, Object> params) {
        logger.info("start running +++++++++++++++++++++++++++++++");
        Object fileIds = params.get("fileIds");
        Object env = params.get("env");
        Object env1 = params.get("env1");
        logger.info("params-fileIds:"+fileIds);
        logger.info("params-env:"+env);
        logger.info("params-env1:"+env1);

//        Object apitype = params.get("apitype");

        String fileIdsStr = fileIds.toString() ;
        String replacStr = fileIdsStr.substring(1,fileIdsStr.length()-1);
        String[] split = replacStr.split(",");


        logger.info(System.getProperty(JAVA_CLASS_PATH));

        Boolean rpcFlag = Boolean.FALSE;
        Boolean localFlag = env.toString().toLowerCase().contains("127.0.0.1");

        HashMap<String,String> infoCase = new HashMap<>(); //保存case根目录与测试文件的索引
        HashMap<String,String> infoFile = new HashMap<>(); //保存case与最终报告文件的路径
        HashMap<String,ApiTestReportsEntity> apiTestReportsEntityHashMap = new HashMap<>(); //保存case与报告实体的索引
        for(String tmpinsfiled:split) { //遍历每个文件  这里限制只能够选中一个， 否则会影响rpcFlag的取值
            if(!tmpinsfiled.isEmpty()) {
                Long insfiledid = Long.valueOf(tmpinsfiled);
                ApiTestFileEntity apiTestFileEntity = apiTestFileDao.queryObject(insfiledid);
                Long caseId = apiTestFileEntity.getCaseId();
                InterfaceTestEntity interfaceTestEntity = interfaceTestDao.queryObject(caseId);
                rpcFlag = interfaceTestEntity.getApiType().toLowerCase().contains("rpc");




                String baseDir = interfaceTestEntity.getBaseDir();
                String originName = apiTestFileEntity.getOriginName();
                String finalName = apiTestFileEntity.getFileName().split("/")[0];
                infoCase.put(originName, baseDir);
                infoFile.put(originName,finalName);

                ApiTestReportsEntity apiTestReportsEntity = new ApiTestReportsEntity();
                apiTestReportsEntity.setCaseId(caseId);
                apiTestReportsEntity.setOriginName(originName);
//                String desReportDir = finalName+"/html/index.html"; //只针对nose
//                if(originName.endsWith(".xml")){
//                    desReportDir = finalName+"/index.html"; //针对TestNG的
//                }
//                apiTestReportsEntity.setReportName(desReportDir);
                apiTestReportsEntity.setFileId(insfiledid);
                apiTestReportsEntityHashMap.put(originName,apiTestReportsEntity);
            }
        }


        if (rpcFlag){
            if(localFlag) { // 报告的生成存在问题， 本地因为ExecuteException 无法正常生成报告
                CommandLine cmdLine = new CommandLine("java");
                // 设置参数，-n 命令行模式
                // cmdLine.addArgument("-Xdebug");
                // -t 设置JMX脚本路径
                // cmdLine.addArgument("-Xrunjdwp:transport=dt_socket,address=9003,server=y,suspend=n");
                cmdLine.addArgument("-cp");
                if (System.getProperty("os.name").contains("Windows")) {
                    cmdLine.addArgument("${CLASSPATH}:${DIR}\\target\\*:${DIR}\\target\\test-classes\\:${DIR}\\target\\classes\\ ");
                } else {
                    cmdLine.addArgument("${CLASSPATH}:${DIR}/target/*:${DIR}/target/test-classes/:${DIR}/target/classes/ ");
                }
                cmdLine.addArgument("org.testng.TestNG");
                cmdLine.addArgument("${DIR}/${xml_file} ");

                for (String originName : infoCase.keySet()) { //遍历每个文件
                    Map map = new HashMap();
                    map.put("CLASSPATH", System.getProperty(JAVA_CLASS_PATH));
                    map.put("DIR", infoCase.get(originName));    // System.getProperty("user.dir") TestNg工程有用，平台工具没用
                    map.put("xml_file", originName);
                    logger.info(map.toString());
                    cmdLine.setSubstitutionMap(map);
                    logger.info(cmdLine.toString());
                    DefaultExecutor executor = new DefaultExecutor();
                    try {
                        executor.execute(cmdLine);  // 结果异常（报告已经生成的情况下）
                    } catch (ExecuteException e) {
                        e.printStackTrace();

                        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN_4DIR);//设置日期格式
                        String date = df.format(new Date());

                        String reportDir = apiTestUtils.getReportPath()+"/"+infoFile.get(originName)+"/"+date;
                        String cmds1 = "mkdir -p "+reportDir+" && cd "+infoCase.get(originName)+" && cp -f ./reports/index.html "+reportDir;
                        Runtime runtime=Runtime.getRuntime();
                        Process exec = null;
                        try {
                            exec = runtime.exec(cmds1);
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                        ApiTestReportsEntity apiTestReportsEntity = apiTestReportsEntityHashMap.get(originName);
                        apiTestReportsEntity.setReportName(infoFile.get(originName)+"/"+date+File.separator+"index.html");
                        apiTestReportsDao.save(apiTestReportsEntity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                for(String xml_file:infoCase.keySet()) { //遍历每个文件
                    String baseDir = infoCase.get(xml_file);
                    String cmd = "java  -cp "+System.getProperty(JAVA_CLASS_PATH)+":"+baseDir+"/target/*:"+baseDir+"/target/test-classes/:"+baseDir+"/target/classes/ "+" org.testng.TestNG " +baseDir+"/"+xml_file;
                    if ( System.getProperty("os.name").contains("Windows")) {
                        cmd = "java  -cp "+System.getProperty(JAVA_CLASS_PATH)+":"+baseDir+"\\target\\*:"+baseDir+"\\target\\test-classes\\:"+baseDir+"\\target\\classes\\ "+" org.testng.TestNG " +baseDir+"\\"+xml_file;
                    }
                    logger.info(cmd);
                    StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP(env.toString(), "22");
                    SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);
                    try{
                        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN_4DIR);//设置日期格式
                        String date = df.format(new Date());
                        ssh2Utils.runCommand(cmd);
                        String reportDir = apiTestUtils.getReportPath()+"/"+infoFile.get(xml_file)+"/"+date;
                        String cmds1 = "mkdir -p "+reportDir+" && ls -al "+reportDir;
                        String s = ssh2Utils.runCommand(cmds1);
                        logger.info(s);
                        String cmds2 = "cd "+baseDir+" && cp -f ./reports/index.html "+reportDir;
                        String ss = ssh2Utils.runCommand(cmds2);
                        logger.info(ss);
                        ApiTestReportsEntity apiTestReportsEntity = apiTestReportsEntityHashMap.get(xml_file);
                        apiTestReportsEntity.setReportName(infoFile.get(xml_file)+"/"+date+File.separator+"index.html");
                        apiTestReportsDao.save(apiTestReportsEntity);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }else{//http接口
            for(String originName : infoCase.keySet()) { //遍历每个文件
                SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN_4DIR);//设置日期格式
                String date = df.format(new Date());
                String cmds1 = "cd "+ infoCase.get(originName) +"  && find ./ -name " +originName;
                String reportDir = apiTestUtils.getReportPath()+"/"+infoFile.get(originName)+"/"+date;
                String cmds2 = "mkdir -p "+reportDir+" && ls -al "+reportDir;
                String cmds4 = "cd "+reportDir+" && allure generate  ./  -o  ./html"; //" --with-xunit --xunit-file="+apiTestUtils.getReportPath()+"/report_nose_"+ date+ ".xml
                if(localFlag){ // TODO  包括cmd3命令  及 报告 入库
                    Runtime runtime=Runtime.getRuntime();
                    Process exec = null;
                    try {
                        String cmds3 = "cd "+ infoCase.get(originName) +" && nosetests -s -v "+ originName +" --with-allure --logdir="+reportDir;
                        exec = runtime.exec(cmds1);
                        DataOutputStream outputSteam1 = new DataOutputStream(exec.getOutputStream());
                        exec = runtime.exec(cmds2);
                        DataOutputStream outputSteam = new DataOutputStream(exec.getOutputStream());
                        logger.info(outputSteam.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    StressTestSlaveEntity stressTestSlaveEntity = stressTestSlaveService.queryObjectFromIP(env.toString(), "22");
                    logger.info("stressTestSlaveEntity:" + stressTestSlaveEntity.toString());
                    SSH2Utils ssh2Utils = new SSH2Utils(stressTestSlaveEntity.getIp(), stressTestSlaveEntity.getUserName(), stressTestSlaveEntity.getPasswd(), 22);
                    try{
                        String s = ssh2Utils.runCommand(cmds1);
                        String finalcase = s.substring(2,s.length()-3).replace("/",".");
                        String ss = ssh2Utils.runCommand(cmds2);
                        logger.info(ss);
                        String cmds3 = "cd "+ infoCase.get(originName) +" && nosetests -s -v "+ finalcase + " --with-allure --logdir="+reportDir;
                        String s1 = ssh2Utils.runCommand(cmds3);
                        logger.info(s1);
                        String s2 = ssh2Utils.runCommand(cmds4);
                        logger.info(s2);
                        ApiTestReportsEntity apiTestReportsEntity = apiTestReportsEntityHashMap.get(originName);
                        apiTestReportsEntity.setReportName(infoFile.get(originName)+"/"+date+File.separator+"/html/index.html");
                        apiTestReportsDao.save(apiTestReportsEntity);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

        }
        return "running";
    }

    @Override
    public void stop(Long[] fileIds) {
        logger.info("stop running ~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public String getFilePath(ApiTestFileEntity apiTestFile) {
        String casePath = apiTestUtils.getCasePath();
        String FilePath = casePath + File.separator + apiTestFile.getFileName();
        logger.info("filepath---getFilePath:"+FilePath);
        return FilePath;
    }
}