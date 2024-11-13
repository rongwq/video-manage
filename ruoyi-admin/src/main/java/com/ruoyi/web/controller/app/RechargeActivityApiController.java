package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.domain.RechargeActivity;
import com.ruoyi.system.domain.RechargeKey;
import com.ruoyi.system.domain.UserIntegralRecord;
import com.ruoyi.system.service.IRechargeActivityService;
import com.ruoyi.system.service.IRechargeKeyService;
import com.ruoyi.system.service.IUserIntegralRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 充值活动Controller
 *
 * @author rongwq
 * @date 2024-11-13
 */
@RestController
@RequestMapping("/api/activity")
public class RechargeActivityApiController extends BaseController {
    @Autowired
    private IRechargeActivityService rechargeActivityService;
    @Autowired
    private IUserIntegralRecordService userIntegralRecordService;
    @Autowired
    private IRechargeKeyService rechargeKeyService;

    /**
     * 查询充值活动列表
     */
    @GetMapping("/list")
    @Anonymous
    public R list() {
        RechargeActivity rechargeActivity = new RechargeActivity();
        List<RechargeActivity> list = rechargeActivityService.selectRechargeActivityList(rechargeActivity);
        return R.ok(list);
    }

    /**
     * 充值
     *
     * @param rechargeKey
     * @return
     */
    @PostMapping("/recharge")
    public R recharge(@RequestBody RechargeKey rechargeKey) {
        rechargeKey.setUserId(getUserId());
        rechargeActivityService.recharge(rechargeKey);
        return R.ok();
    }

    /**
     * 获取用户充值记录
     * @return
     */
    @GetMapping("/rechargeList")
    public R rechargeList() {
        startPage();
        Long userId = getUserId();
        UserIntegralRecord query = new UserIntegralRecord();
        query.setUserId(userId);
        List<UserIntegralRecord> list = userIntegralRecordService.selectUserIntegralRecordList(query);
        if(list.isEmpty()){
            return R.ok("暂无充值记录");
        }
        for (UserIntegralRecord userIntegralRecord : list) {
            RechargeKey rechargeKey = rechargeKeyService.selectRechargeKeyById(userIntegralRecord.getRecordId());
            if(rechargeKey!=null){
                userIntegralRecord.setRechargeCode(rechargeKey.getCode());
            }
        }
        return R.ok(list);
    }
}
