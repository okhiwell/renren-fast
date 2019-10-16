package io.renren.modules.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.renren.modules.test.dao.StressTestFileDao;
import io.renren.modules.test.dao.TestStressThreadSetDao;
import io.renren.modules.test.entity.StressTestFileEntity;
import io.renren.modules.test.entity.TestStressThreadSetEntity;
import io.renren.modules.test.service.TestStressThreadSetService;
import io.renren.modules.test.utils.StressTestUtils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 线程组管理
 * 
 * @author smooth
 * @email 
 * @date 2019-03-25 12:08:42
 */
@Service("testStressThreadSetService")
public class TestStressThreadSetServiceImpl implements TestStressThreadSetService {
	@Autowired
	private TestStressThreadSetDao testStressThreadSetDao;
	@Autowired
    private StressTestFileDao stressTestFileDao;
	@Autowired
    private StressTestUtils stressTestUtils;
	
	@Override
	public TestStressThreadSetEntity queryObject(String setId){
		return testStressThreadSetDao.queryObject(setId);
	}
	
	@Override
	public List<TestStressThreadSetEntity> queryList(Map<String, Object> map){
		return testStressThreadSetDao.queryList(map);
	}
	
	@Override
    public List<TestStressThreadSetEntity> queryListByFileId(Long fileId) {
        return testStressThreadSetDao.queryListByFileId(fileId);
    }
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return testStressThreadSetDao.queryTotal(map);
	}
	
	@Override
	public List<TestStressThreadSetEntity> queryNotSetList() {
		return testStressThreadSetDao.queryNotSetList();
	}
	
	@Override
	public void save(TestStressThreadSetEntity testStressThreadSet){
		testStressThreadSetDao.save(testStressThreadSet);
	}
	
	@Override
	public void update(TestStressThreadSetEntity testStressThreadSet){
		testStressThreadSetDao.update(testStressThreadSet);
	}
	
	@Override
	public void delete(Long setId){
		testStressThreadSetDao.delete(setId);
	}
	
	@Override
	public void deleteBatch(String[] setIds){
		testStressThreadSetDao.deleteBatch(setIds);
	}
	
	@Override
	public void saveBatch(List <TestStressThreadSetEntity> testStressThreadSetList){
		testStressThreadSetDao.saveBatch(testStressThreadSetList);
	}
	
	/**
     * 获取脚本的线程组配置数据入库。
	 * @throws DocumentException 
     */
	int k;//线程组配置编号（确保入库与同步脚本的相对位置顺序）
	String uuid_p="";//线程组主键ID编号
	
	//@Override
	//@Transactional
	public void jmxSaveNodes(String filePath, StressTestFileEntity stressTestFile) throws DocumentException {
		SAXReader reader = new SAXReader();
		File file = new File(filePath);
		Long fileId = stressTestFile.getFileId();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Document document = reader.read(file);
        Element root = document.getRootElement();
        List<TestStressThreadSetEntity> tThreadSetEntityList=new ArrayList<TestStressThreadSetEntity>( );
        TestStressThreadSetEntity stressThreadSetEntity = null;
        stressThreadSetEntity = setSThreadSetEntity(
        		uuid,
        		"0",
        		stressTestFile.getOriginName(),
        		"运行模式",
        		"",
        		0,
        		"所属用例："+stressTestFile.getCaseName()+"； 对应脚本："+stressTestFile.getFileName(),
        		0,
        		fileId);
        tThreadSetEntityList.add(stressThreadSetEntity);
        getNodes(root,fileId,uuid,tThreadSetEntityList);//从根节点开始遍历所有节点
        saveBatch(tThreadSetEntityList);
	}
	
    /**
	 * 从指定节点开始,递归遍历所有子节点
	 * @author smooth-z
	 */
	public void getNodes(Element node, Long fileId, String uuid, List<TestStressThreadSetEntity> tThreadSetEntityList) {
		//System.out.println("--------------------");
		if(node.getName().equals("TestPlan")){ //jmx脚本的测试计划属性值
			List<Element> listElem = node.elements("boolProp"); 
			String TestPlanValue = "";
			for(Element TestPlanElem:listElem){
				if(TestPlanElem.getTextTrim().equals("true")) TestPlanValue += TestPlanElem.attributeValue("name")+";";
			}
			tThreadSetEntityList.get(0).setValue(TestPlanValue.substring(0,TestPlanValue.length()-1));
		}
		TestStressThreadSetEntity stressThreadSetEntity = null;
		//当前节点的名称、文本内容和属性
		//System.out.println("当前节点名称："+node.getName());//当前节点名称
		//System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称
		List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
		String keyValueStr = "ThreadGroup.on_sample_error;ThreadGroup.num_threads;"
				+ "Threads initial delay;Start users count;Start users count burst;"
				+ "Start users period;Stop users count;Stop users period;flighttime;rampUp;"
				+ "LoopController.loops;ThreadGroup.ramp_time;ThreadGroup.scheduler;"
				+ "ThreadGroup.duration;ThreadGroup.delay";
		String namesStr = "取样器报错后动作;线程组线程数;"
				+ "初始等待时间(s);每次启动线程数;初次启动线程数;"
				+ "每次启动线程数间隔时间(s);每次停止线程数;每次停止线程数间隔时间(s);峰值压力持续时间(s);线程启动完毕时间/加压时间(s);"
				+ "循环次数(-1为永远循环);Ramp-Up Period(s);开启调度器;"
				+ "调度持续时间(s);调度启动延迟(s)";
		String[] keyStr = keyValueStr.split(";");
		String[] nameStr = namesStr.split(";");
		attrfor: for(Attribute attr:listAttr){//遍历当前节点的所有属性
			//String attrName=attr.getName();//属性名称
			String keyName=attr.getValue();//属性的值
			//System.out.println("属性名称："+name+"属性值："+value);
			if(keyName.equals("ThreadGroup.on_sample_error")){ //jmx脚本的线程组属性值
				uuid_p=UUID.randomUUID().toString().replaceAll("-", "");
				stressThreadSetEntity = setSThreadSetEntity(
						uuid_p,
						uuid,node.getParent().attribute(2).getValue(),
						"enabled",
						node.getParent().attributeValue("enabled"),
						1,
						"线程组类别："+node.getParent().getName(),
						++k,
						fileId);
				tThreadSetEntityList.add(stressThreadSetEntity);
				System.out.println("---------线程组"+k+"-----------");
				if(node.getParent().getName().endsWith("UltimateThreadGroup")){ //jp@gc - Ultimate Thread Group类别
					//UltimateThreadGroup线程组太特殊，不适合循环逐个配置
					List<Element> elistElem = node.getParent().elements().get(0).elements("collectionProp");
					for(Element elem:elistElem){
						String skeyName=elem.attributeValue("name");
						String skeyValue="";
						List<Element> slistElem = elem.elements("stringProp");
						for(Element selem:slistElem){
							skeyValue+=selem.getTextTrim()+";";
						}
						stressThreadSetEntity = setSThreadSetEntity(
								UUID.randomUUID().toString().replaceAll("-", ""),
								uuid_p,
								"线程计划"+skeyName,
								skeyName,
								skeyValue.substring(0,skeyValue.length() - 1),
								2,
								"配置项",
								++k,
								fileId);
						tThreadSetEntityList.add(stressThreadSetEntity);
					}
				}
			}
			for (int i = 0 ; i <keyStr.length ; i++ ) { //jmx脚本的线程组具体配置项
				if(keyName.equals(keyStr[i]) && !node.getParent().getName().equals("LoopController") && 
						(!node.getName().equals("intProp") || (node.getName().equals("intProp") && 
								node.getParent().getParent().getName().equals("ThreadGroup")))){
					stressThreadSetEntity = setSThreadSetEntity(
							UUID.randomUUID().toString().replaceAll("-", ""),
							uuid_p,
							nameStr[i],
							keyName,
							node.getTextTrim(),
							2,
							"配置项",
							++k,
							fileId);
					tThreadSetEntityList.add(stressThreadSetEntity);
					System.out.print(nameStr[i]+":"+keyName+"="+node.getTextTrim()+"\n");
					break attrfor;
				}
			}
		}
		
		//递归遍历当前节点所有的子节点
		List<Element> listElement=node.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			getNodes(e,fileId,uuid,tThreadSetEntityList);//递归
		}
	}

	/**
	 * 填充线程组配置信息数据
	 * @author smooth-z
	 */
	public TestStressThreadSetEntity setSThreadSetEntity(
			String setId, String parentId, String name,
			String key, String value, int type,
			String explain, int orderNum, Long fileId){

		TestStressThreadSetEntity stressThreadSetEntity = new TestStressThreadSetEntity();
		stressThreadSetEntity.setSetId(setId);
		stressThreadSetEntity.setParentId(parentId);
		stressThreadSetEntity.setName(name);
		stressThreadSetEntity.setKey(key);
		stressThreadSetEntity.setValue(value);
		stressThreadSetEntity.setType(type);
		stressThreadSetEntity.setExplain(explain);
		stressThreadSetEntity.setOrderNum(orderNum);
		stressThreadSetEntity.setFileId(fileId);
		return stressThreadSetEntity;
	}
	
	/**
     * 从库中获取线程组配置并同步到指定脚本
     * @author smooth-z
	 * @throws DocumentException 
     */
    @Override
    @Transactional
    public void synchronizeJmx(Long fileId) throws DocumentException {
    	SAXReader reader = new SAXReader();
    	StressTestFileEntity stressTestFile = stressTestFileDao.queryObject(fileId);
		File file = new File(stressTestUtils.getCasePath()+File.separator+stressTestFile.getFileName());
    	Document document = reader.read(file);
        Element root = document.getRootElement();
        List<TestStressThreadSetEntity> stressThreadSetEntityList = queryListByFileId(fileId);
        setNodes(root,stressThreadSetEntityList);
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new FileOutputStream(file));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
	 * 从指定节点开始,递归遍历所有子节点
	 * @author smooth-z
	 */
	public void setNodes(Element node, List<TestStressThreadSetEntity> stressThreadSetEntityList) {
		String attrNameValue = node.attributeValue("name");//获取属性名为name所对应的值
		String attrParentValue = null;//获取父节点属性名为name所对应的值
		if(node.getParent()!=null){
			attrParentValue = node.getParent().attributeValue("name");
		}
		Entity : for (TestStressThreadSetEntity stressThreadSetEntity : stressThreadSetEntityList) {
    		//System.out.println(stressThreadSetEntity.getOrderNum()+"--------"+stressThreadSetEntity.getName());
    		if(stressThreadSetEntity.getType()==0){
    			String[] testPlanValues = stressThreadSetEntity.getValue().split(";|；");
    			Values : for(int i = 0 ; i < testPlanValues.length ; i++){
    				if( attrNameValue != null && attrNameValue.equals(testPlanValues[i]) ){
    					node.setText("true");
    					if(i == testPlanValues.length-1){
    						//修改完的配置项就移出循环，避免重复操作
							stressThreadSetEntityList.remove(stressThreadSetEntity);
							break Entity;
						}
    					break Values;
    				}
    			}
    		}
    		else if(stressThreadSetEntity.getType()==1 && node.getName().endsWith("ThreadGroup")){
    			node.attribute(stressThreadSetEntity.getKey()).setValue(stressThreadSetEntity.getValue());
				//修改完的配置项就移出循环，避免重复操作
				stressThreadSetEntityList.remove(stressThreadSetEntity);
    			break;
    		}else{
    			if( attrParentValue != null && attrParentValue.equals(stressThreadSetEntity.getKey())){
    				//jp@gc - Ultimate Thread Group的配置项值修改
    				List<Element> listElem = node.getParent().elements("stringProp");
    				String[] ultiThreadGroupValues = stressThreadSetEntity.getValue().split(";|；");
        			ValueU : for(int i = 0 ; i < ultiThreadGroupValues.length ; i++){
        				if(listElem.get(i).equals(node)){
        					node.setText(ultiThreadGroupValues[i]);
        					if(i == ultiThreadGroupValues.length-1){
        						//修改完的配置项就移出循环，避免重复操作
    							stressThreadSetEntityList.remove(stressThreadSetEntity);
    							break Entity;
    						}
        					break ValueU;
        				}
        				
        			}
    			}
    			if( attrNameValue != null && attrNameValue.equals(stressThreadSetEntity.getKey()) &&
						!node.getName().equals("collectionProp") && !node.getParent().getName().equals("LoopController") ){
    				String getValue=stressThreadSetEntity.getValue();
    				String nodeName=node.getName();
    				if(nodeName.equals("intProp") && !node.getParent().getParent().getName().equals("ThreadGroup")){
    					//非ThreadGroup线程组的【循环次数】配置项都忽略不设置
    					break;
    				}
    				if(nodeName.equals("intProp") && !getValue.equals("-1")){//ThreadGroup线程组的【循环次数】配置项
    					node.setName("stringProp");
    				}
    				if(nodeName.equals("stringProp") && getValue.equals("-1")){//ThreadGroup线程组的【循环次数】配置项
    					node.setName("intProp");
    				}  				
    				node.setText(getValue);
					//普通配置项值修改，修改完的配置项就移出循环，避免重复操作
					stressThreadSetEntityList.remove(stressThreadSetEntity);
    				break;
    			}
    		}
    	}
		//递归遍历当前节点所有的子节点
		List<Element> listElement=node.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			setNodes(e,stressThreadSetEntityList);//递归
		}
	}
}
