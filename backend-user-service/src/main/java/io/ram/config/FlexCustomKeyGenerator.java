package io.ram.config;

import com.alibaba.nacos.common.utils.RandomUtils;
import com.mybatisflex.core.keygen.IKeyGenerator;
import com.mybatisflex.core.keygen.impl.FlexIDKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.Inet4Address;

@Slf4j
public class FlexCustomKeyGenerator implements IKeyGenerator {



    @Override
    public Object generate(Object entity, String keyColumn) {
        Long workId = getWorkId();
        log.info("work id :{}",workId);
        return new FlexIDKeyGenerator(workId).generate(entity,keyColumn);
    }


    /**
     * workId使用IP生成
     *
     * @return workId
     */
    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums = sums + b;
            }
            return (long) (sums % 32);
        } catch (Exception e) {
            // 失败就随机
            long workId = RandomUtils.nextLong(0, 99);
            log.info("获取地址失败，随机work id:{}", workId);
            return workId;
        }
    }
}
