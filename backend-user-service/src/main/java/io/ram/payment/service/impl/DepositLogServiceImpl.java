package io.ram.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import io.ram.config.DateUtil;
import io.ram.domain.req.deposit.DepositCreateReq;
import io.ram.domain.req.fy.SubmitOrderReq;
import io.ram.domain.resp.fy.SubmitOrderResp;
import io.ram.enums.Currency;
import io.ram.enums.DepositStatus;
import io.ram.exception.BizException;
import io.ram.payment.entity.DepositLog;
import io.ram.payment.mapper.DepositLogMapper;
import io.ram.payment.service.DepositLogService;
import io.ram.payment.service.FyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * 存款表 服务层实现。
 *
 * @author warren
 * @since 2023-08-25
 */
@Service
@Validated
public class DepositLogServiceImpl extends ServiceImpl<DepositLogMapper, DepositLog> implements DepositLogService {
    @Autowired
    private FyService fyService;
    @Value("${fy.token}")
    private String fyToken;
    @Value("${fy.notify.url}")
    private String notifyUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createFromFyChannel(DepositCreateReq req) {
        var date = DateUtil.getNowByUTC();
        DepositLog depositLog = DepositLog.builder()
                .depositAmount(req.getAmount())
                .merchantId(req.getMerchantIds().get(0))
                .customerId(req.getCustomerId())
                .currency(Currency.BRL)
                .status(DepositStatus.INIT)
                .build();
        this.save(depositLog);
        SubmitOrderReq fyReq = SubmitOrderReq.builder()
                .MerchantID("80")
                .OrderId(depositLog.getId() + "")
                .Date(DateUtil.formatToyyyyMMddHHmmss(date))
                .NotifyUrl(notifyUrl)
                .CallBackUrl("http://www.baidu.com")
                .Amount(req.getAmount() + "")
                .MerchantNumber("1005")
                .build();
        fyReq.doSign(fyToken);
        SubmitOrderResp submitOrderResp = fyService.submitOrder(fyReq);
        if (StrUtil.equalsIgnoreCase(submitOrderResp.getCode(), "success")) {
            return submitOrderResp.getUrl();
        } else {
            throw new BizException.Init("error call fy service," + JSONUtil.toJsonStr(submitOrderResp));
        }
    }
}
