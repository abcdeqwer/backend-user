package io.ram.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.keygen.KeyGeneratorFactory;
import com.mybatisflex.core.query.QueryColumnBehavior;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisFlexConfiguration implements MyBatisFlexCustomizer {
    private static final Logger logger = LoggerFactory
            .getLogger("mybatis-flex-sql");

    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {
        //开启审计功能
        AuditManager.setAuditEnable(true);

        //设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage ->
                logger.info("{},{}ms", auditMessage.getFullSql()
                        , auditMessage.getElapsedTime())
        );
        QueryColumnBehavior.setSmartConvertInToEquals(true);
        QueryColumnBehavior.setIgnoreFunction(o -> "".equals(o));
        KeyGeneratorFactory.register("flex",new FlexCustomKeyGenerator());

    }
}
