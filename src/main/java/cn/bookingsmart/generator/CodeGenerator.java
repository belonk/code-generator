package cn.bookingsmart.generator;

import cn.bookingsmart.annotation.GenerationType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.rules.PaginationType;

import java.util.ArrayList;
import java.util.List;

/**
 * mysql 代码生成器
 *
 * @author sun
 * @since 2019-08-10
 */
public class CodeGenerator {
    private String modelName;
    private String[] tableName;
    private String author;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private boolean restController;
    private boolean pagination;
    private PaginationType paginationType;

    private static final CodeGenerator INSTANCE = new CodeGenerator();

    public static CodeGenerator getInstance() {
        return INSTANCE;
    }

    private CodeGenerator() {

    }

    public void generate(DataSourceConfig dsc, GlobalConfig gc, PackageConfig pc, StrategyConfig sc, InjectionConfig cfg) {
        if (gc == null) {
            throw new IllegalArgumentException("GlobalConfig must not be null.");
        }
        if (dsc == null) {
            throw new IllegalArgumentException("DataSourceConfig must not be null.");
        }
        if (pc == null) {
            throw new IllegalArgumentException("PackageConfig must not be null.");
        }
        if (sc == null) {
            throw new IllegalArgumentException("StrategyConfig must not be null.");
        }
        if (cfg == null) {

        }
        // 代码生成器
        AutoGenerator generator = new AutoGenerator();
        generator.setGlobalConfig(gc);
        generator.setDataSource(dsc);
        generator.setPackageInfo(pc);
        generator.setStrategy(sc);
        generator.setCfg(cfg);
        generator.execute();
    }

    /**
     * 完全自定义配置后生成。
     *
     * @param configBuilder 配置构建器
     */
    public void generate(ConfigBuilder configBuilder) {
        if (configBuilder == null) {
            throw new IllegalArgumentException("ConfigBuilder must not be null.");
        }
        AutoGenerator generator = new AutoGenerator();
        generator.setConfig(configBuilder);
        generator.execute();
    }

    public CodeGenerator init(String modelName, String[] tableName, String author) {
        this.modelName = modelName;
        this.tableName = tableName;
        this.author = author;
        return this;
    }

    public CodeGenerator dbParams(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        return this;
    }

    public CodeGenerator otherParams(boolean restController, boolean pagination, PaginationType paginationType) {
        this.restController = restController;
        this.pagination = pagination;
        if (pagination) {
            this.paginationType = paginationType;
        }
        return this;
    }

    public void generate() {
        // 代码生成器
        AutoGenerator generator = new AutoGenerator();

        // 全局配置

        GlobalConfig gc = new GlobalConfig();
        final String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath);
        gc.setAuthor(author);
        gc.setIdType(GenerationType.INPUT);

        // 数据源配置

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://" + dbUrl + "?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(dbUser);
        dsc.setPassword(dbPassword);
        generator.setDataSource(dsc);

        // 包配置

        final PackageConfig pc = new PackageConfig();
        pc.setModuleName(modelName);
        pc.setParent("cn.bookingsmart");

        // 策略配置

        StrategyConfig sc = new StrategyConfig();
        sc.setNaming(NamingStrategy.underline_to_camel);
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
        sc.setInclude(tableName);
        sc.setEntityLombokModel(true);
        // 分页
        sc.setPagination(pagination);
        if (pagination) {
            sc.setPaginationType(paginationType);
        }
        // 表名前缀
        sc.setTablePrefix("t_");
        sc.setControllerMappingHyphenStyle(true);
        sc.setRestControllerStyle(restController);
        generator.setStrategy(sc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        cfg.setFileOutConfigList(focList);
        generate(dsc, gc, pc, sc, cfg);
    }

}
