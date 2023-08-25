package io.ram.payment.controller.deposit;

import io.ram.customer.service.CustomerWalletService;
import io.ram.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class PaymentController {
    @Autowired
    private CustomerWalletService customerWalletService;

    @PostMapping("/user/create")
    public R createCustomer(){

        return R.success();
    }
}
