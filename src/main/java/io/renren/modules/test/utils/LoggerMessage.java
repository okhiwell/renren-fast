package io.renren.modules.test.utils;

/**
 * @author CT
 * @date 2019/7/29:35
 * @describe LoggerMessage用于
 */


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志消息实体
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoggerMessage {
    private String body;
    private String timestamp;
    private String threadName;
    private String className;
    private String level;
    private String exception;
    private String cause;
}
