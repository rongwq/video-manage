package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.IntegralType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.RegActivityConfig;
import com.ruoyi.system.service.IRegActivityConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IUserIntegralRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * H5注册活动对外接口
 *
 * @author ruoyi
 */
@Api("H5注册活动对外接口")
@RestController
@RequestMapping("/api/regActivity")
@Slf4j
public class RegActivityApiController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private IRegActivityConfigService regActivityConfigService;

    @Autowired
    private IUserIntegralRecordService userIntegralRecordService;

    /**
     * 获取当前注册活动配置信息
     */
    @ApiOperation("获取当前注册活动配置")
    @GetMapping("/config")
    @Anonymous
    public R<RegActivityConfigVO> getActivityConfig()
    {
        RegActivityConfig config = regActivityConfigService.getValidRegActivityConfig();
        RegActivityConfigVO vo = new RegActivityConfigVO();
        if (config != null)
        {
            vo.setActivityEnabled(true);
            vo.setGiveIntegral(config.getGiveIntegral());
            vo.setActivityName(config.getActivityName());
            vo.setEndTime(config.getEndTime());
        }
        else
        {
            vo.setActivityEnabled(false);
        }
        return R.ok(vo);
    }

    /**
     * H5用户注册接口（带活动赠送积分）
     */
    @ApiOperation("H5用户注册（带活动赠送积分）")
    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    @Anonymous
    @Log(title = "H5用户注册", businessType = BusinessType.INSERT)
    public R<RegisterResultVO> register(@RequestBody RegisterParam param)
    {
        // 参数校验
        if (StringUtils.isEmpty(param.getUserName()))
        {
            return R.fail("用户名不能为空");
        }
        if (StringUtils.isEmpty(param.getPassword()))
        {
            return R.fail("密码不能为空");
        }
        if (param.getUserName().length() < 2 || param.getUserName().length() > 20)
        {
            return R.fail("账户长度必须在2到20个字符之间");
        }
        if (param.getPassword().length() < 5 || param.getPassword().length() > 20)
        {
            return R.fail("密码长度必须在5到20个字符之间");
        }

        SysUser user = new SysUser();
        user.setUserName(param.getUserName());

        // 检查用户名是否已存在
        if (!userService.checkUserNameUnique(user))
        {
            return R.fail("注册用户'" + param.getUserName() + "'失败，登录账号已存在");
        }

        // 检查手机号是否已存在
        if (StringUtils.isNotEmpty(param.getPhonenumber()))
        {
            user.setPhonenumber(param.getPhonenumber());
            if (!userService.checkPhoneUnique(user))
            {
                return R.fail("注册用户'" + param.getUserName() + "'失败，手机号码已存在");
            }
        }

        // 设置用户信息
        user.setNickName(StringUtils.isNotEmpty(param.getNickName()) ? param.getNickName() : param.getUserName());
        user.setPassword(SecurityUtils.encryptPassword(param.getPassword()));
        user.setEmail(param.getEmail());
        user.setPhonenumber(param.getPhonenumber());
        user.setRemark(param.getRemark());
        // 设置默认角色和部门
        user.setRoleIds(new Long[]{100L});
        user.setDeptId(100L);

        // 保存用户
        int result = userService.insertUser(user);
        if (result <= 0)
        {
            return R.fail("注册失败，请联系系统管理人员");
        }

        // 获取注册成功的用户
        SysUser newUser = userService.selectUserByUserName(param.getUserName());

        RegisterResultVO resultVO = new RegisterResultVO();
        resultVO.setUserId(newUser.getUserId());
        resultVO.setUserName(newUser.getUserName());

        // 检查是否有有效的注册活动
        RegActivityConfig activityConfig = regActivityConfigService.getValidRegActivityConfig();
        if (activityConfig != null && activityConfig.getGiveIntegral() != null && activityConfig.getGiveIntegral() > 0)
        {
            try
            {
                // 赠送积分
                Integer giveIntegral = activityConfig.getGiveIntegral().intValue();
                userIntegralRecordService.saveUserIntegralRecord(
                        newUser.getUserId(),
                        null,
                        giveIntegral,
                        IntegralType.REG_GIFT,
                        "新用户注册赠送积分，活动：" + activityConfig.getActivityName()
                );
                resultVO.setGiftIntegral(giveIntegral);
                resultVO.setGiftSuccess(true);
                log.info("用户[{}]注册成功，赠送积分：{}", param.getUserName(), giveIntegral);
            }
            catch (Exception e)
            {
                log.error("用户[{}]注册赠送积分失败：{}", param.getUserName(), e.getMessage());
                resultVO.setGiftSuccess(false);
                resultVO.setGiftIntegral(0);
            }
        }
        else
        {
            resultVO.setGiftSuccess(false);
            resultVO.setGiftIntegral(0);
        }

        return R.ok(resultVO);
    }

    /**
     * 注册参数
     */
    @Data
    public static class RegisterParam implements Serializable
    {
        private static final long serialVersionUID = 1L;

        /** 用户名 */
        @NotBlank(message = "用户名不能为空")
        private String userName;

        /** 密码 */
        @NotBlank(message = "密码不能为空")
        private String password;

        /** 昵称 */
        private String nickName;

        /** 手机号 */
        private String phonenumber;

        /** 邮箱 */
        private String email;

        /** 备注 */
        private String remark;
    }

    /**
     * 注册结果
     */
    @Data
    public static class RegisterResultVO implements Serializable
    {
        private static final long serialVersionUID = 1L;

        /** 用户ID */
        private Long userId;

        /** 用户名 */
        private String userName;

        /** 是否赠送积分成功 */
        private Boolean giftSuccess;

        /** 赠送积分数量 */
        private Integer giftIntegral;
    }

    /**
     * 活动配置VO
     */
    @Data
    public static class RegActivityConfigVO implements Serializable
    {
        private static final long serialVersionUID = 1L;

        /** 活动是否开启 */
        private Boolean activityEnabled;

        /** 赠送积分 */
        private Long giveIntegral;

        /** 活动名称 */
        private String activityName;

        /** 活动结束时间 */
        private java.util.Date endTime;
    }
}
