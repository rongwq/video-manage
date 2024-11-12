package com.ruoyi.web.controller.app;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.domain.UserCollectRecord;
import com.ruoyi.system.domain.UserLikeRecord;
import com.ruoyi.system.domain.UserPlayRecord;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IUserCollectRecordService;
import com.ruoyi.system.service.IUserLikeRecordService;
import com.ruoyi.system.service.IUserPlayRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 我的接口
 *
 * @author rongwq
 */
@Api("我的接口")
@RestController
@RequestMapping("/api/my")
public class MyApiController extends BaseController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IUserLikeRecordService userLikeRecordService;
    @Autowired
    private IUserCollectRecordService userCollectRecordService;
    @Autowired
    private IUserPlayRecordService userPlayRecordService;

    @ApiOperation("获取用户播放记录")
    @GetMapping("/video/userPlayList")
    public R userPlayList() {
        startPage();
        UserPlayRecord userPlayRecord = new UserPlayRecord();
        userPlayRecord.setUserId(getUserId());
        List<UserPlayRecord> list = userPlayRecordService.selectUserPlayRecordList(userPlayRecord);
        return R.ok(list);
    }

    @ApiOperation("获取用户点赞记录")
    @GetMapping("/video/userLikeList")
    public R userLikeList() {
        startPage();
        UserLikeRecord record = new UserLikeRecord();
        record.setUserId(getUserId());
        List<UserLikeRecord> list = userLikeRecordService.selectUserLikeRecordList(record);
        return R.ok(list);
    }

    @ApiOperation("获取用户收藏记录")
    @GetMapping("/video/userCollectList")
    public R userCollectList() {
        startPage();
        UserCollectRecord record = new UserCollectRecord();
        record.setUserId(getUserId());
        List<UserCollectRecord> list = userCollectRecordService.selectUserCollectRecordList(record);
        return R.ok(list);
    }
}
