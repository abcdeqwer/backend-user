import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.ColumnConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

public class CodeGen {

    public static void main(String[] args) {
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://43.134.186.33:3306/ram?characterEncoding=utf-8");
        dataSource.setUsername("ram");
        dataSource.setPassword("TYwKRD7wMjxT3GfK");

        //创建配置内容，两种风格都可以。
        GlobalConfig globalConfig = createGlobalConfigUseStyle1();
        //GlobalConfig globalConfig = createGlobalConfigUseStyle2();

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        //生成代码
        generator.generate();
    }

    public static GlobalConfig createGlobalConfigUseStyle1() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();
        //设置生成地址
        globalConfig.getPackageConfig().setSourceDir("/Users/warren/code/backend-user/backend-user-service/src/main/java/");

        //设置根包
        globalConfig.setBasePackage("io.ram.payment");

        //设置表前缀和只生成哪些表
        globalConfig.setTablePrefix("t_");
        globalConfig.setGenerateTable("t_customer","t_customer_status","t_customer_wallet","t_deposit_log","t_transfer_log");

        //设置生成 entity 并启用 Lombok
        globalConfig.setEntityGenerateEnable(true);
        globalConfig.setEntityWithLombok(true);
//        globalConfig.setEntityOverwriteEnable(true);

        globalConfig.getStrategyConfig().setVersionColumn("revision");

        //设置生成 mapper
        globalConfig.setMapperGenerateEnable(true);
        globalConfig.setServiceGenerateEnable(true);
        globalConfig.setServiceImplGenerateEnable(true);
        //可以单独配置某个列
//        ColumnConfig columnConfig = new ColumnConfig();
//        columnConfig.setColumnName("merchant_id");
//        columnConfig.setTenantId(true);
//        globalConfig.setColumnConfig(columnConfig);
        ColumnConfig idConfig = new ColumnConfig();
        idConfig.setColumnName("id");
        idConfig.setKeyType(KeyType.Generator);
        idConfig.setKeyValue("flex");
        globalConfig.setColumnConfig(idConfig);
        ColumnConfig updateConfig = new ColumnConfig();
        updateConfig.setColumnName("updated_time");
        updateConfig.setOnUpdateValue("now()");
        globalConfig.setColumnConfig(updateConfig);
        ColumnConfig createConfig = new ColumnConfig();
        createConfig.setColumnName("created_time");
        createConfig.setOnInsertValue("now()");
        globalConfig.setColumnConfig(createConfig);
//        columnConfig.setLarge(true);
//        columnConfig.setVersion(true);
//        globalConfig.setColumnConfig("tb_account", columnConfig);

        return globalConfig;
    }

}