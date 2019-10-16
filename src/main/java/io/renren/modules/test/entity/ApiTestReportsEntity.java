package io.renren.modules.test.entity;


import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * 性能测试报告文件表
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-18 14:32:11
 */
public class ApiTestReportsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long reportId;
	/**
	 * 所关联的用例
	 */
	private Long caseId;
	/**
	 * 所关联的用例文件
	 */
	private Long fileId;
	/**
	 * 测试报告名称
	 */
	private String originName;
	/**
	 * 避免跨系统编码错误，实际文件存储的报告名，名称和测试报告文件夹名称一致
	 */
	private String reportName;
	/**
	 * 测试结果文件大小
	 */
	private Long fileSize;
	/**
	 * 状态  0：初始状态  1：正在运行  2：成功执行  3：运行出现异常
	 */
	private Integer status;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date addTime;
	/**
	 * 提交用户id
	 */
	private String addBy;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 修改用户id
	 */
	private String updateBy;

	private File file;


	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}


	public void setAddBy(String addBy) {
		this.addBy = addBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}


	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getAddBy() {
		return addBy;
	}

	@Override
	public String toString() {
		return "ApiTestReportsEntity{" +
				"reportId=" + reportId +
				", caseId=" + caseId +
				", fileId=" + fileId +
				", originName='" + originName + '\'' +
				", reportName='" + reportName + '\'' +
				", fileSize=" + fileSize +
				", status=" + status +
				", remark='" + remark + '\'' +
				", addTime=" + addTime +
				", addBy='" + addBy + '\'' +
				", updateTime=" + updateTime +
				", updateBy='" + updateBy + '\'' +
				", file=" + file +
				'}';
	}
}
