package io.renren.modules.test.entity;


import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-27 13:40:12
 */
public class MockApiInfoFullEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@NotBlank(message="url不能为空")
	private String url;
	/**
	 * 
	 */
	@NotBlank(message="方法不能为空")
	private String method;

	/**
	 * 所属项目
	 */
	@NotBlank(message="项目不能为空")
	private String project;

	/**
	 * 模块
	 */
	@NotBlank(message="模块不能为空")
	private String module;

	/**
	 * 
	 */
	private String headers;
	/**
	 * 
	 */
	private String cookies;
	/**
	 * 
	 */
	private String xpaths;
	/**
	 * 
	 */
	private String forms;
	/**
	 * 
	 */
	private String file;
	/**
	 * 
	 */
	private Integer status = 0;

	private String queries;
	/**
	 * 
	 */
	private String json;
	/**
	 * 
	 */
	private String factory;
	/**
	 * 
	 */
	private String text;
	/**
	 * 
	 */
	private String jsonPaths;
	/**
	 * 
	 */
	private String version;
	/**
	 * 
	 */
	private String pathResource;


	/**
	 * 提交用例的人
	 */
	private String addBy;

	/**
	 * 修改用例的人
	 */
	private String updateBy;

	/**
	 * 提交的时间
	 */
	private Date addTime;

	/**
	 * 更新的时间
	 */
	private Date updateTime;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String remark;

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url.replace("/","_");
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public String getXpaths() {
		return xpaths;
	}

	public void setXpaths(String xpaths) {
		this.xpaths = xpaths;
	}

	public String getForms() {
		return forms;
	}

	public void setForms(String forms) {
		this.forms = forms;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getQueries() {
		return queries;
	}

	public void setQueries(String queries) {
		this.queries = queries;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getJsonPaths() {
		return jsonPaths;
	}

	public void setJsonPaths(String jsonPaths) {
		this.jsonPaths = jsonPaths;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPathResource() {
		return pathResource;
	}

	public void setPathResource(String pathResource) {
		this.pathResource = pathResource;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAddBy() {
		return addBy;
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

	@Override
	public String toString() {
		return "MockApiInfoFullEntity{" +
				"url='" + url + '\'' +
				", method='" + method + '\'' +
				", project='" + project + '\'' +
				", module='" + module + '\'' +
				", headers='" + headers + '\'' +
				", cookies='" + cookies + '\'' +
				", xpaths='" + xpaths + '\'' +
				", forms='" + forms + '\'' +
				", file='" + file + '\'' +
				", status=" + status +
				", queries='" + queries + '\'' +
				", json='" + json + '\'' +
				", factory='" + factory + '\'' +
				", text='" + text + '\'' +
				", jsonPaths='" + jsonPaths + '\'' +
				", version='" + version + '\'' +
				", pathResource='" + pathResource + '\'' +
				", addBy='" + addBy + '\'' +
				", updateBy='" + updateBy + '\'' +
				", addTime=" + addTime +
				", updateTime=" + updateTime +
				", remark='" + remark + '\'' +
				'}';
	}
}
