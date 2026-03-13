package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.entity.Video;
import com.ruoyi.common.core.domain.entity.VideoAd;
import com.ruoyi.common.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VideoValidator 单元测试")
class VideoValidatorTest {

    @Test
    @DisplayName("验证标题 - 正常情况")
    void validateTitle_success() {
        assertDoesNotThrow(() -> VideoValidator.validateTitle("测试视频标题", false, null));
        assertDoesNotThrow(() -> VideoValidator.validateTitle("Test-Video_123", false, null));
        assertDoesNotThrow(() -> VideoValidator.validateTitle("中文English混合123", false, null));
    }

    @Test
    @DisplayName("验证标题 - 标题为空")
    void validateTitle_empty() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTitle(null, false, null));
        assertEquals("视频标题不能为空", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTitle("", false, null));
        assertEquals("视频标题不能为空", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTitle("   ", false, null));
        assertEquals("视频标题不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("验证标题 - 长度超出限制")
    void validateTitle_tooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            sb.append("a");
        }
        String longTitle = sb.toString();
        
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTitle(longTitle, false, null));
        assertEquals("标题长度需在 1-100 字符之间", exception.getMessage());
    }

    @Test
    @DisplayName("验证标题 - 包含非法特殊字符")
    void validateTitle_invalidChars() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTitle("测试@视频", false, null));
        assertEquals("标题包含非法特殊字符，仅允许中文、英文、数字、-_", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTitle("test#video", false, null));
        assertEquals("标题包含非法特殊字符，仅允许中文、英文、数字、-_", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTitle("视频 标题", false, null));
        assertEquals("标题包含非法特殊字符，仅允许中文、英文、数字、-_", exception.getMessage());
    }

    @Test
    @DisplayName("验证URL - 正常情况")
    void validateUrl_success() {
        assertDoesNotThrow(() -> VideoValidator.validateUrl("http://example.com/video.mp4"));
        assertDoesNotThrow(() -> VideoValidator.validateUrl("https://example.com/video.avi"));
        assertDoesNotThrow(() -> VideoValidator.validateUrl("https://example.com/path/to/video.mov"));
        assertDoesNotThrow(() -> VideoValidator.validateUrl("http://example.com/video.mkv"));
    }

    @Test
    @DisplayName("验证URL - URL为空")
    void validateUrl_empty() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateUrl(null));
        assertEquals("视频 URL 不能为空", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateUrl(""));
        assertEquals("视频 URL 不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("验证URL - 长度超出限制")
    void validateUrl_tooLong() {
        StringBuilder sb = new StringBuilder("https://example.com/");
        for (int i = 0; i < 500; i++) {
            sb.append("a");
        }
        sb.append(".mp4");
        String longUrl = sb.toString();
        
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateUrl(longUrl));
        assertEquals("视频 URL 长度不能超过 500 字符", exception.getMessage());
    }

    @Test
    @DisplayName("验证URL - 格式错误")
    void validateUrl_invalidFormat() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateUrl("ftp://example.com/video.mp4"));
        assertEquals("视频 URL 格式错误，仅支持 mp4/avi/mov/rmvb/flv/wmv/mkv 格式", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateUrl("https://example.com/video.txt"));
        assertEquals("视频 URL 格式错误，仅支持 mp4/avi/mov/rmvb/flv/wmv/mkv 格式", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateUrl("example.com/video.mp4"));
        assertEquals("视频 URL 格式错误，仅支持 mp4/avi/mov/rmvb/flv/wmv/mkv 格式", exception.getMessage());
    }

    @Test
    @DisplayName("验证金额 - 收费视频金额正常")
    void validateMoney_paidVideo_success() {
        assertDoesNotThrow(() -> VideoValidator.validateMoney(100, "2"));
        assertDoesNotThrow(() -> VideoValidator.validateMoney(1, "2"));
        assertDoesNotThrow(() -> VideoValidator.validateMoney(9999, "2"));
    }

    @Test
    @DisplayName("验证金额 - 收费视频金额为空或小于等于0")
    void validateMoney_paidVideo_invalid() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateMoney(null, "2"));
        assertEquals("收费视频的金额不能为空且必须大于 0", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateMoney(0, "2"));
        assertEquals("收费视频的金额不能为空且必须大于 0", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateMoney(-1, "2"));
        assertEquals("收费视频的金额不能为空且必须大于 0", exception.getMessage());
    }

    @Test
    @DisplayName("验证金额 - 非收费视频金额必须为0")
    void validateMoney_freeVideo_mustBeZero() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateMoney(100, "1"));
        assertEquals("非收费视频类型的金额需为 0", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateMoney(1, "3"));
        assertEquals("非收费视频类型的金额需为 0", exception.getMessage());
    }

    @Test
    @DisplayName("验证金额 - 非收费视频金额为0正常")
    void validateMoney_freeVideo_success() {
        assertDoesNotThrow(() -> VideoValidator.validateMoney(0, "1"));
        assertDoesNotThrow(() -> VideoValidator.validateMoney(null, "1"));
        assertDoesNotThrow(() -> VideoValidator.validateMoney(0, "3"));
        assertDoesNotThrow(() -> VideoValidator.validateMoney(null, "3"));
    }

    @Test
    @DisplayName("验证金额 - 超过最大值")
    void validateMoney_exceedMax() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateMoney(10000, "2"));
        assertEquals("金额不能超过 9999", exception.getMessage());
    }

    @Test
    @DisplayName("验证金额 - 类型为空")
    void validateMoney_typeEmpty() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateMoney(100, null));
        assertEquals("视频类型不能为空", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateMoney(100, ""));
        assertEquals("视频类型不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("验证类型逻辑 - 免费无广告视频正常")
    void validateTypeLogic_freeNoAd_success() {
        assertDoesNotThrow(() -> VideoValidator.validateTypeLogic("1", 0, null));
        assertDoesNotThrow(() -> VideoValidator.validateTypeLogic("1", null, null));
    }

    @Test
    @DisplayName("验证类型逻辑 - 免费无广告视频有广告列表")
    void validateTypeLogic_freeNoAd_hasAdList() {
        List<VideoAd> adList = new ArrayList<>();
        adList.add(new VideoAd());
        
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTypeLogic("1", 0, adList));
        assertEquals("免费无广告视频的广告列表必须为空", exception.getMessage());
    }

    @Test
    @DisplayName("验证类型逻辑 - 免费无广告视频金额不为0")
    void validateTypeLogic_freeNoAd_moneyNotZero() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTypeLogic("1", 100, null));
        assertEquals("免费无广告视频的金额必须为 0", exception.getMessage());
    }

    @Test
    @DisplayName("验证类型逻辑 - 收费视频正常")
    void validateTypeLogic_paid_success() {
        assertDoesNotThrow(() -> VideoValidator.validateTypeLogic("2", 100, null));
        assertDoesNotThrow(() -> VideoValidator.validateTypeLogic("2", 1, new ArrayList<>()));
    }

    @Test
    @DisplayName("验证类型逻辑 - 收费视频金额小于等于0")
    void validateTypeLogic_paid_moneyInvalid() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTypeLogic("2", 0, null));
        assertEquals("收费视频的金额必须大于 0", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTypeLogic("2", null, null));
        assertEquals("收费视频的金额必须大于 0", exception.getMessage());
    }

    @Test
    @DisplayName("验证类型逻辑 - 免费有广告视频正常")
    void validateTypeLogic_freeWithAd_success() {
        List<VideoAd> adList = new ArrayList<>();
        adList.add(new VideoAd());
        
        assertDoesNotThrow(() -> VideoValidator.validateTypeLogic("3", 0, adList));
    }

    @Test
    @DisplayName("验证类型逻辑 - 免费有广告视频广告列表为空")
    void validateTypeLogic_freeWithAd_emptyAdList() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTypeLogic("3", 0, null));
        assertEquals("免费有广告视频的广告列表不能为空", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTypeLogic("3", 0, new ArrayList<>()));
        assertEquals("免费有广告视频的广告列表不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("验证类型逻辑 - 免费有广告视频金额不为0")
    void validateTypeLogic_freeWithAd_moneyNotZero() {
        List<VideoAd> adList = new ArrayList<>();
        adList.add(new VideoAd());
        
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTypeLogic("3", 100, adList));
        assertEquals("免费有广告视频的金额必须为 0", exception.getMessage());
    }

    @Test
    @DisplayName("验证类型逻辑 - 类型不合法")
    void validateTypeLogic_invalidType() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTypeLogic("4", 0, null));
        assertEquals("视频类型不合法，仅支持 1、2、3", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateTypeLogic("0", 0, null));
        assertEquals("视频类型不合法，仅支持 1、2、3", exception.getMessage());
    }

    @Test
    @DisplayName("验证完整视频对象 - 免费无广告视频")
    void validateVideo_freeNoAd() {
        Video video = new Video();
        video.setTitle("测试视频");
        video.setUrl("https://example.com/video.mp4");
        video.setType("1");
        video.setMoney(0);
        video.setAdList(null);

        assertDoesNotThrow(() -> VideoValidator.validateVideo(video, false));
    }

    @Test
    @DisplayName("验证完整视频对象 - 收费视频")
    void validateVideo_paid() {
        Video video = new Video();
        video.setTitle("收费视频");
        video.setUrl("https://example.com/paid.mp4");
        video.setType("2");
        video.setMoney(100);
        video.setAdList(null);

        assertDoesNotThrow(() -> VideoValidator.validateVideo(video, false));
    }

    @Test
    @DisplayName("验证完整视频对象 - 免费有广告视频")
    void validateVideo_freeWithAd() {
        Video video = new Video();
        video.setTitle("免费有广告视频");
        video.setUrl("https://example.com/free.mp4");
        video.setType("3");
        video.setMoney(0);
        
        List<VideoAd> adList = new ArrayList<>();
        adList.add(new VideoAd());
        video.setAdList(adList);

        assertDoesNotThrow(() -> VideoValidator.validateVideo(video, false));
    }

    @Test
    @DisplayName("验证数字字段 - 正常情况")
    void validateNumericField_success() {
        assertDoesNotThrow(() -> VideoValidator.validateNumericField("播放量", "100"));
        assertDoesNotThrow(() -> VideoValidator.validateNumericField("点赞量", "0"));
        assertDoesNotThrow(() -> VideoValidator.validateNumericField("金额", ""));
        assertDoesNotThrow(() -> VideoValidator.validateNumericField("金额", null));
    }

    @Test
    @DisplayName("验证数字字段 - 非数字字符")
    void validateNumericField_invalid() {
        ServiceException exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateNumericField("播放量", "abc"));
        assertEquals("播放量 必须为数字", exception.getMessage());

        exception = assertThrows(ServiceException.class, 
            () -> VideoValidator.validateNumericField("点赞量", "12.5"));
        assertEquals("点赞量 必须为数字", exception.getMessage());
    }
}
