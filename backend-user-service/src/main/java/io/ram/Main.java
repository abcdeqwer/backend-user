package io.ram;

import io.ram.customer.entity.Customer;
import io.ram.customer.service.CustomerService;
import io.ram.domain.R;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@MapperScan("io.ram.**.mapper")
@RestController
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableTransactionManagement
public class Main {
    @Autowired
    private CustomerService customerService;
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @RequestMapping("/")
    @Transactional
    public R<?> index(){
        customerService.save(Customer.builder().username("test").build());
        return R.success();
    }
}