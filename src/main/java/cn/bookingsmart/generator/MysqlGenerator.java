package cn.bookingsmart.generator;

import cn.bookingsmart.annotation.GenerationType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * mysql 代码生成器
 * </p>
 *
 * @author jobob
 * @since 2018-09-12
 */
public class MysqlGenerator {
    /**
     * RUN THIS
     */
    public static void main(String[] args) {
        String modelName = "user";
        String tableName = "t_user";
        String author = "belonk";

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置

        GlobalConfig gc = new GlobalConfig();
        final String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(author);
        gc.setOpen(false);
        // 自动生成BaseResultMap
        gc.setBaseResultMap(true);
        gc.setIdType(GenerationType.INPUT);
        // 测试用
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123");
        mpg.setDataSource(dsc);

        // 包配置

        final PackageConfig pc = new PackageConfig();
        pc.setModuleName(modelName);
        pc.setParent("cn.bookingsmart");
        mpg.setPackageInfo(pc);

        // 策略配置

        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude(tableName);
        strategy.setEntityLombokModel(true);
        // 表名前缀
        strategy.setTablePrefix("t_");
        strategy.setControllerMappingHyphenStyle(true);
        // strategy.setRestControllerStyle(false);
        mpg.setStrategy(strategy);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        mpg.execute();
    }

}
