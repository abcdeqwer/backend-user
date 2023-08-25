package io.ram.customer.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import io.ram.customer.entity.Customer;
import io.ram.customer.mapper.CustomerMapper;
import io.ram.customer.service.CustomerService;
import org.springframework.stereotype.Service;

/**
 * 用户表 服务层实现。
 *
 * @author warren
 * @since 2023-08-23
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
