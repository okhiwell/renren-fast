package io.renren.datasources;

/**
 * 增加多数据源，在此配置
 * 步骤2：多数据源切面处理类，实现数据库的选择 对应配置文件中的命名
 * @date 2017/8/18 23:46
 */
public interface DataSourceNames {
    String FIRST = "first";
    String SECOND = "second";

}
