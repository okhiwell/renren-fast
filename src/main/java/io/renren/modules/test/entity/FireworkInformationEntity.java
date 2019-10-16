package io.renren.modules.test.entity;


import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author caoting
 * @email sunlightcs@gmail.com
 * @date 2019-06-21 23:03:01
 */
public class FireworkInformationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 机器名
	 */
	private String name;
	/**
	 * 局域网ip
	 */
	private String privateip;
	/**
	 * 公网ip
	 */
	private String publicip;
	/**
	 * 环境名 dev/test/prod
	 */
	private String use;
	/**
	 * 时区
	 */
	private String zoneid;
	/**
	 * cpu核数
	 */
	private String cpu;
	/**
	 * 内存
	 */
	private String memory;
	/**
	 * 磁盘大小
	 */
	private String datadisk;
	/**
	 * 添加时间
	 */
	private Date time;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrivateip() {
		return privateip;
	}

	public void setPrivateip(String privateip) {
		this.privateip = privateip;
	}

	public String getPublicip() {
		return publicip;
	}

	public void setPublicip(String publicip) {
		this.publicip = publicip;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getZoneid() {
		return zoneid;
	}

	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getDatadisk() {
		return datadisk;
	}

	public void setDatadisk(String datadisk) {
		this.datadisk = datadisk;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


	@Override
	public String toString() {
		return "FireworkInformationEntity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", privateip='" + privateip + '\'' +
				", publicip='" + publicip + '\'' +
				", use='" + use + '\'' +
				", zoneid='" + zoneid + '\'' +
				", cpu='" + cpu + '\'' +
				", memory='" + memory + '\'' +
				", datadisk='" + datadisk + '\'' +
				", time=" + time +
				'}';
	}
}
