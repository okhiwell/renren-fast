package io.renren.modules.test.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 线程组管理
 * 
 * @author smooth
 * @email 
 * @date 2019-03-26 09:48:06
 */
public class TestStressThreadSetEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String setId;
	//所属ID，一级配置为0
	private String parentId;
	//父配置项名
	private String parentName;
	//配置名称
	private String name;
	//配置项
	private String key;
	//配置内容
	private String value;
	//类型   0：脚本   1：线程组   2：配置
	private Integer type;
	//配置说明
	private String explain;
	//排序（配置项编号）
	private Integer orderNum;
	//所属脚本文件ID
	private Long fileId;
	/**
	 * ztree属性
	 */
	private Boolean open;
	private List<?> list;

	/**
	 * 设置：
	 */
	public void setSetId(String setId) {
		this.setId = setId;
	}
	/**
	 * 获取：
	 */
	public String getSetId() {
		return setId;
	}
	/**
	 * 设置：所属ID，一级配置为0
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：所属ID，一级配置为0
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * 设置：配置名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：配置名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：配置项
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * 获取：配置项
	 */
	public String getKey() {
		return key;
	}
	/**
	 * 设置：配置内容
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取：配置内容
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 设置：类型   0：脚本   1：线程组   2：配置
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型   0：脚本   1：线程组   2：配置
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：配置说明
	 */
	public void setExplain(String explain) {
		this.explain = explain;
	}
	/**
	 * 获取：配置说明
	 */
	public String getExplain() {
		return explain;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序（配置项编号）
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：所属脚本文件ID
	 */
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	/**
	 * 获取：所属脚本文件ID
	 */
	public Long getFileId() {
		return fileId;
	}
	
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}
	
	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}


	@Override
	public String toString() {
		return "TestStressThreadSetEntity{" +
				"setId='" + setId + '\'' +
				", parentId='" + parentId + '\'' +
				", parentName='" + parentName + '\'' +
				", name='" + name + '\'' +
				", key='" + key + '\'' +
				", value='" + value + '\'' +
				", type=" + type +
				", explain='" + explain + '\'' +
				", orderNum=" + orderNum +
				", fileId=" + fileId +
				", open=" + open +
				", list=" + list +
				'}';
	}
}
