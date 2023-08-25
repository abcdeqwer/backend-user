package io.ram.payment.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import io.ram.payment.entity.TransferLog;
import io.ram.payment.mapper.TransferLogMapper;
import io.ram.payment.service.TransferLogService;
import org.springframework.stereotype.Service;

/**
 * 额度记录变更表 服务层实现。
 *
 * @author warren
 * @since 2023-08-25
 */
@Service
public class TransferLogServiceImpl extends ServiceImpl<TransferLogMapper, TransferLog> implements TransferLogService {

}
