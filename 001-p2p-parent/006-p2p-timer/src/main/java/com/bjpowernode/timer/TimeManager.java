package com.bjpowernode.timer;
import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.service.IncomeRecordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

/**
 *
 */
@Controller
public class TimeManager {
    @Reference(interfaceClass = IncomeRecordService.class,version = "1.0.0",check = false,timeout = 15000)
    private IncomeRecordService incomeRecordService;

    /**
     * 生成收益计划
     * @throws Exception
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomePlan() throws Exception {

        System.out.println("==生成收益计划开始==");
        incomeRecordService.generateIncomePlan();
        System.out.println("==生成收益计划结束==");
    }

    /**
     * 返还收益计划
     * @throws Exception
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomeMoney() throws Exception {

        System.out.println("==返还收益计划开始==");
        incomeRecordService.generateIncomeMoney();
        System.out.println("==返还收益计划结束==");
    }
}
