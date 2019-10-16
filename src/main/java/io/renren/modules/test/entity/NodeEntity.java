package io.renren.modules.test.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zyanycall@gmail.com on 15:24.
 */

@Setter
@Getter
public class NodeEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * IP地址
     */
    @NotBlank(message="IP地址不能为空")
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    private String realip;


    /**
     * VIP地址
     */
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    private String vip;

    /**
     * 端口号
     */
    @NotBlank(message="ssh端口号不能为空")
    @Min(value = 0)
    private String port;

    @NotBlank(message="用户名不能为空")
    private String userName;

    @NotBlank(message="密码不能为空")
    private String password;

    @NotBlank(message="root密码不能为空")
    private String rootPassword;

    private String description;


    public String getRealip() {
        return realip;
    }

    public void setRealip(String realip) {
        this.realip = realip;
    }

    @Override
    public String toString() {
        return "NodeEntity{" +
                "realip='" + realip + '\'' +
                ", vip='" + vip + '\'' +
                ", port='" + port + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", rootPassword='" + rootPassword + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
