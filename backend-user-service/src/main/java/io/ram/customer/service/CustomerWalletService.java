package io.ram.customer.service;

import com.mybatisflex.core.service.IService;
import io.ram.customer.entity.CustomerWallet;
import io.ram.domain.req.UpdateBalanceReq;

/**
 * 用户钱包表 服务层。
 *
 * @author warren
 * @since 2023-08-25
 */
public interface CustomerWalletService extends IService<CustomerWallet> {

    void updateBalance(UpdateBalanceReq req);
}
