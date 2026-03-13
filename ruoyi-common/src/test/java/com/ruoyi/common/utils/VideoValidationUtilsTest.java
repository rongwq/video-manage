package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.entity.Video;
import com.ruoyi.common.exception.ServiceException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * VideoValidationUtils 单元测试
 *
 * @author ruoyi
 */
public class VideoValidationUtilsTest {

    // ==================== 标题校验测试 ====================

    @Test
    public void testValidateTitle_Success() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateTitle("测试视频标题", result);
        assertTrue("合法标题应该通过校验", result.isValid());
    }

    @Test
    public void testValidateTitle_Empty() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateTitle("", result);
        assertFalse("空标题应该校验失败", result.isValid());
        assertTrue("错误信息应该包含不能为空", result.getErrorMessage().contains("不能为空"));
    }

    @Test
    public void testValidateTitle_Null() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateTitle(null, result);
        assertFalse("null标题应该校验失败", result.isValid());
    }

    @Test
    public void testValidateTitle_TooLong() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        StringBuilder longTitle = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            longTitle.append("a");
        }
        VideoValidationUtils.validateTitle(longTitle.toString(), result);
        assertFalse("超过100字符的标题应该校验失败", result.isValid());
        assertTrue("错误信息应该包含长度限制", result.getErrorMessage().contains("1-100"));
    }

    @Test
    public void testValidateTitle_InvalidChars() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateTitle("测试标题@#$", result);
        assertFalse("包含非法字符的标题应该校验失败", result.isValid());
        assertTrue("错误信息应该包含非法字符提示", result.getErrorMessage().contains("非法特殊字符"));
    }

    @Test
    public void testValidateTitle_WithSpaces() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateTitle("  测试标题  ", result);
        assertTrue("去除空格后的合法标题应该通过校验", result.isValid());
    }

    @Test
    public void testValidateTitle_AllowedSpecialChars() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateTitle("测试标题-Test_123", result);
        assertTrue("包含允许的特殊字符(-_)应该通过校验", result.isValid());
    }

    // ==================== URL校验测试 ====================

    @Test
    public void testValidateUrl_Success() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateUrl("https://example.com/video.mp4", result);
        assertTrue("合法URL应该通过校验", result.isValid());
    }

    @Test
    public void testValidateUrl_Empty() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateUrl("", result);
        assertFalse("空URL应该校验失败", result.isValid());
        assertTrue("错误信息应该包含不能为空", result.getErrorMessage().contains("不能为空"));
    }

    @Test
    public void testValidateUrl_InvalidProtocol() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateUrl("ftp://example.com/video.mp4", result);
        assertFalse("非http/https协议应该校验失败", result.isValid());
        assertTrue("错误信息应该包含格式错误", result.getErrorMessage().contains("格式错误"));
    }

    @Test
    public void testValidateUrl_InvalidFormat() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateUrl("https://example.com/video.txt", result);
        assertFalse("非视频格式应该校验失败", result.isValid());
        assertTrue("错误信息应该包含格式不支持", result.getErrorMessage().contains("仅支持"));
    }

    @Test
    public void testValidateUrl_TooLong() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        StringBuilder longUrl = new StringBuilder("https://example.com/");
        for (int i = 0; i < 500; i++) {
            longUrl.append("a");
        }
        longUrl.append(".mp4");
        VideoValidationUtils.validateUrl(longUrl.toString(), result);
        assertFalse("超过500字符的URL应该校验失败", result.isValid());
        assertTrue("错误信息应该包含长度限制", result.getErrorMessage().contains("500"));
    }

    @Test
    public void testValidateUrl_VariousVideoFormats() {
        String[] validFormats = {".mp4", ".avi", ".mov", ".rmvb", ".flv", ".wmv", ".mkv"};
        for (String format : validFormats) {
            VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
            VideoValidationUtils.validateUrl("https://example.com/video" + format, result);
            assertTrue(format + " 格式应该通过校验", result.isValid());
        }
    }

    @Test
    public void testValidateUrl_CaseInsensitive() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateUrl("https://example.com/video.MP4", result);
        assertTrue("大写格式应该通过校验", result.isValid());
    }

    // ==================== 金额校验测试 ====================

    @Test
    public void testValidateMoney_VipTypeValid() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(100, "2", result);
        assertTrue("收费视频合法金额应该通过校验", result.isValid());
    }

    @Test
    public void testValidateMoney_VipTypeNull() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(null, "2", result);
        assertFalse("收费视频金额不能为空", result.isValid());
        assertTrue("错误信息应该包含收费视频", result.getErrorMessage().contains("收费视频"));
    }

    @Test
    public void testValidateMoney_VipTypeZero() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(0, "2", result);
        assertFalse("收费视频金额必须大于0", result.isValid());
        assertTrue("错误信息应该包含大于0", result.getErrorMessage().contains("大于 0"));
    }

    @Test
    public void testValidateMoney_VipTypeNegative() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(-10, "2", result);
        assertFalse("收费视频金额不能为负数", result.isValid());
    }

    @Test
    public void testValidateMoney_VipTypeTooLarge() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(10000, "2", result);
        assertFalse("收费视频金额不能超过9999", result.isValid());
        assertTrue("错误信息应该包含9999", result.getErrorMessage().contains("9999"));
    }

    @Test
    public void testValidateMoney_FreeTypeValid() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(0, "1", result);
        assertTrue("免费视频金额为0应该通过校验", result.isValid());
    }

    @Test
    public void testValidateMoney_FreeTypeNull() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(null, "1", result);
        assertTrue("免费视频金额为空应该通过校验", result.isValid());
    }

    @Test
    public void testValidateMoney_FreeTypeWithMoney() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(100, "1", result);
        assertFalse("免费视频不能有金额", result.isValid());
        assertTrue("错误信息应该包含非收费视频", result.getErrorMessage().contains("非收费视频"));
    }

    @Test
    public void testValidateMoney_FreeAdTypeValid() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(0, "3", result);
        assertTrue("免费含广告视频金额为0应该通过校验", result.isValid());
    }

    @Test
    public void testValidateMoney_FreeAdTypeWithMoney() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(50, "3", result);
        assertFalse("免费含广告视频不能有金额", result.isValid());
    }

    // ==================== 完整校验测试（新增场景） ====================

    @Test
    public void testValidateForAdd_Success() {
        Video video = createValidVideo("1"); // 免费无广告
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForAdd(video);
        assertTrue("合法视频新增应该通过校验", result.isValid());
    }

    @Test
    public void testValidateForAdd_VipSuccess() {
        Video video = createValidVideo("2"); // 收费视频
        video.setMoney(100);
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForAdd(video);
        assertTrue("合法收费视频新增应该通过校验", result.isValid());
    }

    @Test
    public void testValidateForAdd_FreeAdSuccess() {
        Video video = createValidVideo("3"); // 免费有广告
        List<Object> adList = new ArrayList<>();
        adList.add(new Object());
        video.setAdList(adList);
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForAdd(video);
        assertTrue("合法免费含广告视频新增应该通过校验", result.isValid());
    }

    @Test
    public void testValidateForAdd_MultipleErrors() {
        Video video = new Video();
        video.setTitle(""); // 空标题
        video.setUrl("invalid-url"); // 非法URL
        video.setType("2"); // 收费视频
        video.setMoney(null); // 金额为空

        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForAdd(video);
        assertFalse("多个错误应该校验失败", result.isValid());
        assertTrue("应该包含多个错误信息", result.getErrors().size() > 1);
    }

    // ==================== 完整校验测试（修改场景） ====================

    @Test
    public void testValidateForUpdate_Success() {
        Video video = createValidVideo("1");
        video.setId(1L);
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForUpdate(video);
        assertTrue("合法视频修改应该通过校验", result.isValid());
    }

    @Test
    public void testValidateForUpdate_NoId() {
        Video video = createValidVideo("1");
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForUpdate(video);
        assertFalse("修改场景ID不能为空", result.isValid());
        assertTrue("错误信息应该包含ID", result.getErrorMessage().contains("ID"));
    }

    @Test
    public void testValidateForUpdate_InvalidId() {
        Video video = createValidVideo("1");
        video.setId(0L);
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForUpdate(video);
        assertFalse("修改场景ID必须大于0", result.isValid());
    }

    // ==================== 逻辑关联校验测试 ====================

    @Test
    public void testValidateLogicRelation_FreeWithAd() {
        Video video = createValidVideo("1");
        List<Object> adList = new ArrayList<>();
        adList.add(new Object());
        video.setAdList(adList);

        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForAdd(video);
        assertFalse("免费无广告视频不能配置广告", result.isValid());
        assertTrue("错误信息应该包含免费无广告", result.getErrorMessage().contains("免费无广告"));
    }

    @Test
    public void testValidateLogicRelation_FreeWithMoney() {
        Video video = createValidVideo("1");
        video.setMoney(100);

        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForAdd(video);
        assertFalse("免费无广告视频不能有金额", result.isValid());
        assertTrue("错误信息应该包含免费无广告", result.getErrorMessage().contains("免费无广告"));
    }

    @Test
    public void testValidateLogicRelation_FreeAdWithoutAd() {
        Video video = createValidVideo("3");
        // adList为空

        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForAdd(video);
        assertFalse("免费含广告视频必须配置广告", result.isValid());
        assertTrue("错误信息应该包含免费含广告", result.getErrorMessage().contains("免费含广告"));
    }

    @Test
    public void testValidateLogicRelation_FreeAdWithMoney() {
        Video video = createValidVideo("3");
        List<Object> adList = new ArrayList<>();
        adList.add(new Object());
        video.setAdList(adList);
        video.setMoney(100);

        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForAdd(video);
        assertFalse("免费含广告视频不能有金额", result.isValid());
        assertTrue("错误信息应该包含免费含广告", result.getErrorMessage().contains("免费含广告"));
    }

    @Test
    public void testValidateLogicRelation_InvalidType() {
        Video video = createValidVideo("9"); // 无效类型

        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateForAdd(video);
        assertFalse("无效类型应该校验失败", result.isValid());
        assertTrue("错误信息应该包含未知类型", result.getErrorMessage().contains("未知"));
    }

    // ==================== 标题唯一性校验测试 ====================

    @Test
    public void testValidateTitleUnique_AddSuccess() {
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateTitleUnique("新标题", null, false);
        assertTrue("新增时标题不存在应该通过校验", result.isValid());
    }

    @Test
    public void testValidateTitleUnique_AddFail() {
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateTitleUnique("已存在标题", null, true);
        assertFalse("新增时标题已存在应该校验失败", result.isValid());
        assertTrue("错误信息应该包含已存在", result.getErrorMessage().contains("已存在"));
    }

    @Test
    public void testValidateTitleUnique_UpdateSuccess() {
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateTitleUnique("新标题", 1L, false);
        assertTrue("修改时标题不存在应该通过校验", result.isValid());
    }

    @Test
    public void testValidateTitleUnique_UpdateFail() {
        VideoValidationUtils.ValidationResult result = VideoValidationUtils.validateTitleUnique("其他视频标题", 1L, true);
        assertFalse("修改时标题被其他视频使用应该校验失败", result.isValid());
        assertTrue("错误信息应该包含其他视频", result.getErrorMessage().contains("其他视频"));
    }

    // ==================== 异常抛出测试 ====================

    @Test(expected = ServiceException.class)
    public void testValidateAndThrow_Invalid() {
        Video video = new Video();
        video.setTitle(""); // 空标题
        VideoValidationUtils.validateAndThrow(video, VideoValidationUtils.ValidationScene.ADD);
    }

    @Test
    public void testValidateAndThrow_Valid() {
        Video video = createValidVideo("1");
        try {
            VideoValidationUtils.validateAndThrow(video, VideoValidationUtils.ValidationScene.ADD);
            // 应该正常执行，不抛出异常
        } catch (ServiceException e) {
            fail("合法视频不应该抛出异常");
        }
    }

    // ==================== 边界值测试 ====================

    @Test
    public void testValidateTitle_BoundaryMin() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateTitle("a", result); // 1个字符
        assertTrue("最小长度边界值应该通过校验", result.isValid());
    }

    @Test
    public void testValidateTitle_BoundaryMax() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        StringBuilder title = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            title.append("a");
        }
        VideoValidationUtils.validateTitle(title.toString(), result);
        assertTrue("最大长度边界值应该通过校验", result.isValid());
    }

    @Test
    public void testValidateMoney_BoundaryMin() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(1, "2", result); // 最小有效值
        assertTrue("金额最小边界值应该通过校验", result.isValid());
    }

    @Test
    public void testValidateMoney_BoundaryMax() {
        VideoValidationUtils.ValidationResult result = new VideoValidationUtils.ValidationResult();
        VideoValidationUtils.validateMoney(9999, "2", result); // 最大有效值
        assertTrue("金额最大边界值应该通过校验", result.isValid());
    }

    // ==================== 辅助方法 ====================

    private Video createValidVideo(String type) {
        Video video = new Video();
        video.setTitle("测试视频标题");
        video.setUrl("https://example.com/video.mp4");
        video.setType(type);
        video.setMoney(0);
        return video;
    }
}
