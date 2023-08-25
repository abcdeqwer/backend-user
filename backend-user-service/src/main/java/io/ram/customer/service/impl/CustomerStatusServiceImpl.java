package io.ram.customer.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import io.ram.customer.entity.CustomerStatus;
import io.ram.customer.mapper.CustomerStatusMapper;
import io.ram.customer.service.CustomerStatusService;
import org.springframework.stereotype.Service;

/**
 * 用户状态表 服务层实现。
 *
 * @author warren
 * @since 2023-08-25
 */
@Service
public class CustomerStatusServiceImpl extends ServiceImpl<CustomerStatusMapper, CustomerStatus> implements CustomerStatusService {

}
