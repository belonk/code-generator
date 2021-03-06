/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.generator.config.builder;

import cn.bookingsmart.annotation.GenerationType;
import cn.bookingsmart.annotation.Id;
import cn.bookingsmart.constant.DBType;
import cn.bookingsmart.exception.MybatisFrameworkException;
import cn.bookingsmart.query.BasePageQuery;
import cn.bookingsmart.query.datatable.DataTableQuery;
import cn.bookingsmart.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.rules.PaginationType;
import com.belonk.commons.util.string.StringPool;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * 配置汇总 传递给文件生成工具
 *
 * @author YangHu, tangguo, hubin
 * @since 2016-08-30
 */
public class ConfigBuilder {

    /**
     * 模板路径配置信息
     */
    private final TemplateConfig template;
    /**
     * 数据库配置
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * SQL连接
     */
    private Connection connection;
    /**
     * SQL语句类型
     */
    private IDbQuery dbQuery;
    private String superEntityClass;
    private String superMapperClass;
    /**
     * service超类定义
     */
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    private String superQueryClass;
    /**
     * 数据库表信息
     */
    private List<TableInfo> tableInfoList;
    /**
     * 包配置详情
     */
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;
    /**
     * 策略配置
     */
    private StrategyConfig strategyConfig;
    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;
    /**
     * 注入配置信息
     */
    private InjectionConfig injectionConfig;
    /**
     * 是否支持注释
     */
    private boolean commentSupported;

    /**
     * 在构造器中处理配置
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param template         模板配置
     * @param globalConfig     全局配置
     */
    public ConfigBuilder(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig,
                         TemplateConfig template, GlobalConfig globalConfig) {
        // 全局配置
        if (null == globalConfig) {
            this.globalConfig = new GlobalConfig();
        } else {
            this.globalConfig = globalConfig;
        }

        // 模板配置
        if (null == template) {
            this.template = new TemplateConfig();
        } else {
            this.template = template;
        }

        // 包配置
        if (null == packageConfig) {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), new PackageConfig());
        } else {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), packageConfig);
        }

        // 数据源配置
        this.dataSourceConfig = dataSourceConfig;
        handlerDataSource(dataSourceConfig);

        // 策略配置
        if (null == strategyConfig) {
            this.strategyConfig = new StrategyConfig();
        } else {
            this.strategyConfig = strategyConfig;
        }

        // SQLITE 数据库不支持注释获取
        commentSupported = true;

        handlerStrategy(this.strategyConfig);
    }

    // ************************ 曝露方法 BEGIN*****************************

    /**
     * 所有包配置信息
     *
     * @return 包配置
     */
    public Map<String, String> getPackageInfo() {
        return packageInfo;
    }

    /**
     * 所有路径配置
     *
     * @return 路径配置
     */
    public Map<String, String> getPathInfo() {
        return pathInfo;
    }

    public String getSuperEntityClass() {
        return superEntityClass;
    }

    public String getSuperMapperClass() {
        return superMapperClass;
    }

    /**
     * 获取超类定义
     *
     * @return 完整超类名称
     */
    public String getSuperServiceClass() {
        return superServiceClass;
    }

    public String getSuperServiceImplClass() {
        return superServiceImplClass;
    }

    public String getSuperControllerClass() {
        return superControllerClass;
    }

    public String getSuperQueryClass() {
        return superQueryClass;
    }

    /**
     * 表信息
     *
     * @return 所有表信息
     */
    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    public ConfigBuilder setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
        return this;
    }

    /**
     * 模板路径配置信息
     *
     * @return 所以模板路径配置信息
     */
    public TemplateConfig getTemplate() {
        return template == null ? new TemplateConfig() : template;
    }

    // ****************************** 曝露方法 END**********************************

    /**
     * 处理包配置
     *
     * @param template  TemplateConfig
     * @param outputDir
     * @param config    PackageConfig
     */
    private void handlerPackage(TemplateConfig template, String outputDir, PackageConfig config) {
        // 包信息
        packageInfo = new HashMap<>(8);
        packageInfo.put(ConstVal.MODULE_NAME, config.getModuleName());
        packageInfo.put(ConstVal.ENTITY, joinPackage(config.getParent(), config.getEntity()));
        packageInfo.put(ConstVal.QUERY, joinPackage(config.getParent(), config.getQuery()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(config.getParent(), config.getMapper()));
        if (config.isXmlSaveToResource()) {
            packageInfo.put(ConstVal.XML, outputDir + StringPool.SLASH + "src/main/resources/" + config.getXmlResourceDir() + StringPool.SLASH + config.getModuleName());
        } else {
            packageInfo.put(ConstVal.XML, joinPackage(config.getParent(), config.getXml()));
        }
        packageInfo.put(ConstVal.SERVICE, joinPackage(config.getParent(), config.getService()));
        packageInfo.put(ConstVal.SERVICE_IMPL, joinPackage(config.getParent(), config.getServiceImpl()));
        packageInfo.put(ConstVal.CONTROLLER, joinPackage(config.getParent(), config.getController()));

        String scriptPath = globalConfig.getScriptPath();
        String pagePath = globalConfig.getPagePath();
        packageInfo.put(ConstVal.PAGE, outputDir + StringPool.SLASH + pagePath + StringPool.SLASH + config.getModuleName());
        packageInfo.put(ConstVal.SCRIPT, outputDir + StringPool.SLASH + scriptPath + StringPool.SLASH + config.getModuleName());

        String javaPath = globalConfig.getJavaPath();
        // 自定义路径
        Map<String, String> configPathInfo = config.getPathInfo();
        if (null != configPathInfo) {
            pathInfo = configPathInfo;
        } else {
            // 生成路径信息
            pathInfo = new HashMap<>(6);
            setPathInfo(pathInfo, template.getEntity(), outputDir + StringPool.SLASH + javaPath, ConstVal.ENTITY_PATH, ConstVal.ENTITY);
            setPathInfo(pathInfo, template.getQuery(), outputDir + StringPool.SLASH + javaPath, ConstVal.QUERY_PATH, ConstVal.QUERY);
            setPathInfo(pathInfo, template.getMapper(), outputDir + StringPool.SLASH + javaPath, ConstVal.MAPPER_PATH, ConstVal.MAPPER);
            if (config.isXmlSaveToResource()) {
                pathInfo.put(ConstVal.XML_PATH, packageInfo.get(ConstVal.XML));
            } else {
                setPathInfo(pathInfo, template.getXml(), outputDir + StringPool.SLASH + javaPath, ConstVal.XML_PATH, ConstVal.XML);
            }
            setPathInfo(pathInfo, template.getService(), outputDir + StringPool.SLASH + javaPath, ConstVal.SERVICE_PATH, ConstVal.SERVICE);
            setPathInfo(pathInfo, template.getServiceImpl(), outputDir + StringPool.SLASH + javaPath, ConstVal.SERVICE_IMPL_PATH, ConstVal.SERVICE_IMPL);
            setPathInfo(pathInfo, template.getController(), outputDir + StringPool.SLASH + javaPath, ConstVal.CONTROLLER_PATH, ConstVal.CONTROLLER);
            // 页面
            pathInfo.put(ConstVal.PAGE_PATH, packageInfo.get(ConstVal.PAGE));
            pathInfo.put(ConstVal.SCRIPT_PATH, packageInfo.get(ConstVal.SCRIPT));
        }
    }

    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
        if (StringUtils.isNotEmpty(template)) {
            pathInfo.put(path, joinPath(outputDir, packageInfo.get(module)));
        }
    }

    /**
     * 处理数据源配置
     *
     * @param config DataSourceConfig
     */
    private void handlerDataSource(DataSourceConfig config) {
        connection = config.getConn();
        dbQuery = config.getDbQuery();
    }


    /**
     * 处理数据库表 加载数据库表、列、注释相关数据集
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(StrategyConfig config) {
        processTypes(config);
        tableInfoList = getTablesInfo(config);
    }

    /**
     * 处理superClassName,IdClassType,IdStrategy配置
     *
     * @param config 策略配置
     */
    private void processTypes(StrategyConfig config) {
        if (StringUtils.isEmpty(config.getSuperServiceClass())) {
            superServiceClass = ConstVal.SUPER_SERVICE_CLASS;
        } else {
            superServiceClass = config.getSuperServiceClass();
        }
        if (config.isPagination()) {
            superServiceImplClass = ConstVal.SUPER_MYBATIS_PAGE_SERVICE_IMPL_CLASS;
            superServiceClass = ConstVal.SUPER_PAGE_SERVICE_CLASS;
        } else {
            superServiceClass = ConstVal.SUPER_SERVICE_CLASS;
            superServiceImplClass = ConstVal.SUPER_MYBATIS_SERVICE_IMPL_CLASS;
        }
        if (StringUtils.isEmpty(config.getSuperMapperClass())) {
            superMapperClass = ConstVal.SUPER_MAPPER_CLASS;
        } else {
            superMapperClass = config.getSuperMapperClass();
        }
        if (StringUtils.isEmpty(config.getSuperControllerClass())) {
            superControllerClass = ConstVal.SUPER_CONTROLLER_CLASS;
        } else {
            superControllerClass = config.getSuperControllerClass();
        }
        if (StringUtils.isEmpty(config.getSuperEntityClass())) {
            superEntityClass = ConstVal.SUPER_ENTITY_CLASS;
        } else {
            superEntityClass = config.getSuperEntityClass();
        }

        // 父级查询对象定义
        if (StringUtils.isEmpty(config.getSuperQueryClass())) {
            if (config.getPaginationType() == PaginationType.DATATABLE) {
                superQueryClass = ConstVal.SUPER_DATATABLE_QUERY_CLASS;
            } else {
                superQueryClass = ConstVal.SUPER_BASE_QUERY_CLASS;
            }
        } else {
            superQueryClass = config.getSuperQueryClass();
        }
    }

    /**
     * 处理表对应的类名称
     *
     * @param tableList 表名称
     * @param strategy  命名策略
     * @param config    策略配置项
     * @return 补充完整信息后的表
     */
    private List<TableInfo> processTable(List<TableInfo> tableList, NamingStrategy strategy, StrategyConfig config) {
        String[] tablePrefix = config.getTablePrefix();
        for (TableInfo tableInfo : tableList) {
            String entityName;
            INameConvert nameConvert = strategyConfig.getNameConvert();
            if (null != nameConvert) {
                // 自定义处理实体名称
                entityName = nameConvert.entityNameConvert(tableInfo);
            } else {
                entityName = NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategy, tablePrefix));
            }
            if (StringUtils.isNotEmpty(globalConfig.getQueryName())) {
                tableInfo.setQueryName(globalConfig.getQueryName());
            } else {
                tableInfo.setQueryName(entityName + ConstVal.QUERY);
            }
            if (StringUtils.isNotEmpty(globalConfig.getEntityName())) {
                tableInfo.setConvert(true);
                tableInfo.setEntityName(String.format(globalConfig.getEntityName(), entityName));
            } else {
                tableInfo.setEntityName(strategyConfig, entityName);
            }
            if (StringUtils.isNotEmpty(globalConfig.getMapperName())) {
                tableInfo.setMapperName(String.format(globalConfig.getMapperName(), entityName));
            } else {
                tableInfo.setMapperName(entityName + ConstVal.MAPPER);
            }
            if (StringUtils.isNotEmpty(globalConfig.getXmlName())) {
                tableInfo.setXmlName(String.format(globalConfig.getXmlName(), entityName));
            } else {
                tableInfo.setXmlName(entityName + ConstVal.MAPPER);
            }
            if (StringUtils.isNotEmpty(globalConfig.getServiceName())) {
                tableInfo.setServiceName(String.format(globalConfig.getServiceName(), entityName));
            } else {
                tableInfo.setServiceName(entityName + ConstVal.SERVICE);
            }
            if (StringUtils.isNotEmpty(globalConfig.getServiceImplName())) {
                tableInfo.setServiceImplName(String.format(globalConfig.getServiceImplName(), entityName));
            } else {
                tableInfo.setServiceImplName(entityName + ConstVal.SERVICE_IMPL);
            }
            if (StringUtils.isNotEmpty(globalConfig.getControllerName())) {
                tableInfo.setControllerName(String.format(globalConfig.getControllerName(), entityName));
            } else {
                tableInfo.setControllerName(entityName + ConstVal.CONTROLLER);
            }
            // 检测导入包
            checkImportPackages(tableInfo);
        }
        return tableList;
    }

    /**
     * 检测导入包
     *
     * @param tableInfo ignore
     */
    private void checkImportPackages(TableInfo tableInfo) {
        if (StringUtils.isNotEmpty(strategyConfig.getSuperEntityClass())) {
            // 自定义父类
            tableInfo.getImportPackages().add(strategyConfig.getSuperEntityClass());
        }
        if (null != globalConfig.getIdType()) {
            // 指定需要 IdType 场景
            tableInfo.getImportPackages().add(GenerationType.class.getCanonicalName());
            tableInfo.getImportPackages().add(Id.class.getCanonicalName());
        }
        // 添加导入父级Query包
        if (strategyConfig.getPaginationType() == PaginationType.DATATABLE) {
            tableInfo.getImportPackages().add(DataTableQuery.class.getCanonicalName());
        } else {
            tableInfo.getImportPackages().add(BasePageQuery.class.getCanonicalName());
        }
    }

    /**
     * 获取所有的数据库表信息
     */
    private List<TableInfo> getTablesInfo(StrategyConfig config) {
        boolean isInclude = (null != config.getInclude() && config.getInclude().length > 0);
        boolean isExclude = (null != config.getExclude() && config.getExclude().length > 0);
        if (isInclude && isExclude) {
            throw new RuntimeException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        }
        //所有的表信息
        List<TableInfo> tableList = new ArrayList<>();

        //需要反向生成或排除的表信息
        List<TableInfo> includeTableList = new ArrayList<>();
        List<TableInfo> excludeTableList = new ArrayList<>();

        //不存在的表名
        Set<String> notExistTables = new HashSet<>();
        try {
            String tablesSql = dbQuery.tablesSql();
            TableInfo tableInfo;
            try (PreparedStatement preparedStatement = connection.prepareStatement(tablesSql);
                 ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    String tableName = results.getString(dbQuery.tableName());
                    if (StringUtils.isNotEmpty(tableName)) {
                        tableInfo = new TableInfo();
                        tableInfo.setName(tableName);

                        if (commentSupported) {
                            String tableComment = results.getString(dbQuery.tableComment());
                            if (config.isSkipView() && "VIEW".equals(tableComment)) {
                                // 跳过视图
                                continue;
                            }
                            tableInfo.setComment(tableComment);
                        }

                        if (isInclude) {
                            for (String includeTable : config.getInclude()) {
                                // 忽略大小写等于 或 正则 true
                                if (tableNameMatches(includeTable, tableName)) {
                                    includeTableList.add(tableInfo);
                                } else {
                                    notExistTables.add(includeTable);
                                }
                            }
                        } else if (isExclude) {
                            for (String excludeTable : config.getExclude()) {
                                // 忽略大小写等于 或 正则 true
                                if (tableNameMatches(excludeTable, tableName)) {
                                    excludeTableList.add(tableInfo);
                                } else {
                                    notExistTables.add(excludeTable);
                                }
                            }
                        }
                        tableList.add(tableInfo);
                    } else {
                        System.err.println("当前数据库为空！！！");
                    }
                }
            }
            // 将已经存在的表移除，获取配置中数据库不存在的表
            for (TableInfo tabInfo : tableList) {
                notExistTables.remove(tabInfo.getName());
            }
            if (notExistTables.size() > 0) {
                System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
            }

            // 需要反向生成的表信息
            if (isExclude) {
                tableList.removeAll(excludeTableList);
                includeTableList = tableList;
            }
            if (!isInclude && !isExclude) {
                includeTableList = tableList;
            }
            // 性能优化，只处理需执行表字段 github issues/219
            includeTableList.forEach(ti -> convertTableFields(ti, config));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return processTable(includeTableList, config.getNaming(), config);
    }

    /**
     * 表名匹配
     *
     * @param setTableName 设置表名
     * @param dbTableName  数据库表单
     * @return ignore
     */
    private boolean tableNameMatches(String setTableName, String dbTableName) {
        return setTableName.equalsIgnoreCase(dbTableName)
                || StringUtils.matches(setTableName, dbTableName);
    }

    /**
     * 将字段信息与表信息关联
     *
     * @param tableInfo 表信息
     * @param config    命名策略
     * @return ignore
     */
    private TableInfo convertTableFields(TableInfo tableInfo, StrategyConfig config) {
        boolean haveId = false;
        List<TableField> fieldList = new ArrayList<>();
        List<TableField> commonFieldList = new ArrayList<>();
        DBType dbType = dbQuery.dbType();
        String tableName = tableInfo.getName();
        try {
            String tableFieldsSql = dbQuery.tableFieldsSql();
            tableFieldsSql = String.format(tableFieldsSql, tableName);
            try (
                    PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql);
                    ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    TableField field = new TableField();
                    String columnName = results.getString(dbQuery.fieldName());
                    // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
                    boolean isId;
                    String key = results.getString(dbQuery.fieldKey());
                    isId = StringUtils.isNotEmpty(key) && "PRI".equals(key.toUpperCase());

                    // 处理ID
                    if (isId && !haveId) {
                        // 主键标记
                        field.setKeyFlag(true);
                        // 判断是否是自动增长
                        if (dbQuery.isKeyIdentity(results)) {
                            field.setKeyIdentityFlag(true);
                        }
                        haveId = true;
                    } else {
                        field.setKeyFlag(false);
                    }
                    // 自定义字段查询
                    String[] fcs = dbQuery.fieldCustom();
                    if (null != fcs) {
                        Map<String, Object> customMap = new HashMap<>(fcs.length);
                        for (String fc : fcs) {
                            customMap.put(fc, results.getObject(fc));
                        }
                        field.setCustomMap(customMap);
                    }
                    // 处理其它信息
                    field.setName(columnName);
                    field.setType(results.getString(dbQuery.fieldType()));
                    field.setJdbcType(parseJdbcType(field.getType()));
                    // 处理主键类型
                    if (isId) {
                        String pkType = field.getType();
                        if (pkType.contains("varchar") || pkType.contains("char")) {
                            tableInfo.setKeyType("String");
                            tableInfo.getImportPackages().add(String.class.getCanonicalName());
                        } else {
                            tableInfo.setKeyType("Long");
                            tableInfo.getImportPackages().add(Long.class.getCanonicalName());
                        }
                    }

                    INameConvert nameConvert = strategyConfig.getNameConvert();
                    if (null != nameConvert) {
                        field.setPropertyName(nameConvert.propertyNameConvert(field));
                    } else {
                        field.setPropertyName(strategyConfig, processName(field.getName(), config.getNaming()));
                    }
                    field.setColumnType(dataSourceConfig.getTypeConvert().processTypeConvert(globalConfig, field.getType()));
                    if (commentSupported) {
                        field.setComment(results.getString(dbQuery.fieldComment()));
                    }
                    if (strategyConfig.includeSuperEntityColumns(field.getName())) {
                        // 跳过公共字段
                        commonFieldList.add(field);
                        continue;
                    }
                    fieldList.add(field);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception：" + e.getMessage());
        }
        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
        return tableInfo;
    }

    private JDBCType parseJdbcType(String type) {
        JDBCType jdbcType = null;
        if (type.startsWith("varchar")) {
            jdbcType = JDBCType.VARCHAR;
        } else if (type.startsWith("char")) {
            jdbcType = JDBCType.CHAR;
        } else if (type.startsWith("bigint")) {
            jdbcType = JDBCType.BIGINT;
        } else if (type.startsWith("int")) {
            jdbcType = JDBCType.INTEGER;
        } else if (type.startsWith("binary")) {
            jdbcType = JDBCType.BINARY;
        } else if (type.startsWith("bit")) {
            jdbcType = JDBCType.BIT;
        } else if (type.startsWith("blob")) {
            jdbcType = JDBCType.BLOB;
        } else if (type.startsWith("datetime")) {
            jdbcType = JDBCType.TIMESTAMP;
        } else if (type.startsWith("date")) {
            jdbcType = JDBCType.DATE;
        } else if (type.startsWith("decimal")) {
            jdbcType = JDBCType.DECIMAL;
        } else if (type.startsWith("double")) {
            jdbcType = JDBCType.DOUBLE;
        } else if (type.startsWith("float")) {
            jdbcType = JDBCType.FLOAT;
            // } else if (type.startsWith("num")) {
            // } else if (type.startsWith("json")) {
            // } else if (type.startsWith("linestring")) {
            // } else if (type.startsWith("mediumblob")) {
            // } else if (type.startsWith("mediumblob")) {
            // } else if (type.startsWith("mediumtext")) {
            // } else if (type.startsWith("multilinestring")) {
            // } else if (type.startsWith("multipoint")) {
            // } else if (type.startsWith("multipolygon")) {
            // } else if (type.startsWith("numeric")) {
            // } else if (type.startsWith("numeric")) {
            // } else if (type.startsWith("point")) {
            // } else if (type.startsWith("polygon")) {
            // } else if (type.startsWith("real")) {
            // } else if (type.startsWith("set")) {
            // } else if (type.startsWith("year")) {
        } else if (type.startsWith("varbinary")) {
            jdbcType = JDBCType.VARBINARY;
        } else if (type.startsWith("tinytext")) {
            jdbcType = JDBCType.VARCHAR;
        } else if (type.startsWith("tinyint")) {
            jdbcType = JDBCType.TINYINT;
        } else if (type.startsWith("tinyblob")) {
            jdbcType = JDBCType.BLOB;
        } else if (type.startsWith("timestamp")) {
            jdbcType = JDBCType.TIMESTAMP;
        } else if (type.startsWith("time")) {
            jdbcType = JDBCType.TIME;
        } else if (type.startsWith("text")) {
            jdbcType = JDBCType.LONGVARCHAR;
        } else if (type.startsWith("smallint")) {
            jdbcType = JDBCType.SMALLINT;
        } else if (type.startsWith("longtext")) {
            jdbcType = JDBCType.LONGNVARCHAR;
        } else if (type.startsWith("longblob")) {
            jdbcType = JDBCType.BLOB;
        } else if (type.startsWith("mediumint")) {
            jdbcType = JDBCType.INTEGER;
        } else if (type.startsWith("binary")) {
            jdbcType = JDBCType.BINARY;
        } else {
            throw new MybatisFrameworkException("Unsupported database type : " + type);
        }
        return jdbcType;
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }

    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isEmpty(parent)) {
            return subPackage;
        }
        return parent + StringPool.DOT + subPackage;
    }

    /**
     * 处理字段名称
     *
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy) {
        return processName(name, strategy, strategyConfig.getFieldPrefix());
    }

    /**
     * 处理表/字段名称
     *
     * @param name     ignore
     * @param strategy ignore
     * @param prefix   ignore
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        boolean removePrefix = false;
        if (prefix != null && prefix.length != 0) {
            removePrefix = true;
        }
        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }

    public StrategyConfig getStrategyConfig() {
        return strategyConfig;
    }

    public ConfigBuilder setStrategyConfig(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public ConfigBuilder setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    public InjectionConfig getInjectionConfig() {
        return injectionConfig;
    }

    public ConfigBuilder setInjectionConfig(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }
}
