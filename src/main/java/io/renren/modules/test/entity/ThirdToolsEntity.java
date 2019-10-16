package io.renren.modules.test.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 三方工具表
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-19 18:41:49
 */
public class ThirdToolsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long toolsId;
	/**
	 * 工具名
	 */
	private String toolsName;
	/**
	 * 工具URL链接
	 */
	private String toolsUrl;
	/**
	 * 工具启动命令
	 */
	private String startCommand;
	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status=0;
	/**
	 * 拥有者名字
	 */
	private String operator;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date addTime;
	/**
	 * 拥有者用户id
	 */
	private Long addBy;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 修改用户id
	 */
	private Long updateBy;


	public String getToolsType() {
		return toolsType;
	}

	public void setToolsType(String toolsType) {
		this.toolsType = toolsType;
	}

	/**
	 * 三方工具类型：暂定moniter（默认：监控类）  bigdata（大数据类）  helper（其他工具类）
	 */
	private String toolsType;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getToolsId() {
		return toolsId;
	}

	public void setToolsId(Long toolsId) {
		this.toolsId = toolsId;
	}

	public String getToolsName() {
		return toolsName;
	}

	public void setToolsName(String toolsName) {
		this.toolsName = toolsName;
	}

	public String getToolsUrl() {
		return toolsUrl;
	}

	public void setToolsUrl(String toolsUrl) {
		this.toolsUrl = toolsUrl;
	}

	public String getStartCommand() {
		return startCommand;
	}

	public void setStartCommand(String startCommand) {
		this.startCommand = startCommand;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Long getAddBy() {
		return addBy;
	}

	public void setAddBy(Long addBy) {
		this.addBy = addBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	@Override
	public String toString() {
		return "ThirdToolsEntity{" +
				"toolsId=" + toolsId +
				", toolsName='" + toolsName + '\'' +
				", toolsUrl='" + toolsUrl + '\'' +
				", startCommand='" + startCommand + '\'' +
				", status=" + status +
				", operator='" + operator + '\'' +
				", remark='" + remark + '\'' +
				", addTime=" + addTime +
				", addBy=" + addBy +
				", updateTime=" + updateTime +
				", updateBy=" + updateBy +
				", toolsType='" + toolsType + '\'' +
				'}';
	}
}
