package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.entity.Video;
import com.ruoyi.common.core.domain.entity.VideoAd;
import com.ruoyi.common.exception.ServiceException;

import java.util.List;
import java.util.regex.Pattern;

public class VideoValidator {

    private static final Pattern TITLE_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9\\-_]+$");
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://.+\\.(mp4|avi|mov|rmvb|flv|wmv|mkv)$", Pattern.CASE_INSENSITIVE);
    private static final int TITLE_MAX_LENGTH = 100;
    private static final int URL_MAX_LENGTH = 500;
    private static final int MONEY_MAX_VALUE = 9999;

    public static void validateVideo(Video video, boolean isUpdate) {
        validateTitle(video.getTitle(), isUpdate, video.getId());
        validateUrl(video.getUrl());
        validateMoney(video.getMoney(), video.getType());
        validateTypeLogic(video.getType(), video.getMoney(), video.getAdList());
    }

    public static void validateTitle(String title, boolean isUpdate, Long videoId) {
        if (StringUtils.isEmpty(title)) {
            throw new ServiceException("视频标题不能为空");
        }
        
        title = title.trim();
        
        if (title.length() < 1 || title.length() > TITLE_MAX_LENGTH) {
            throw new ServiceException("标题长度需在 1-100 字符之间");
        }
        
        if (!TITLE_PATTERN.matcher(title).matches()) {
            throw new ServiceException("标题包含非法特殊字符，仅允许中文、英文、数字、-_");
        }
    }

    public static void validateUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new ServiceException("视频 URL 不能为空");
        }
        
        url = url.trim();
        
        if (url.length() > URL_MAX_LENGTH) {
            throw new ServiceException("视频 URL 长度不能超过 500 字符");
        }
        
        if (!URL_PATTERN.matcher(url).matches()) {
            throw new ServiceException("视频 URL 格式错误，仅支持 mp4/avi/mov/rmvb/flv/wmv/mkv 格式");
        }
    }

    public static void validateMoney(Integer money, String type) {
        if (StringUtils.isEmpty(type)) {
            throw new ServiceException("视频类型不能为空");
        }
        
        int moneyValue = (money == null) ? 0 : money;
        
        if ("2".equals(type)) {
            if (moneyValue <= 0) {
                throw new ServiceException("收费视频的金额不能为空且必须大于 0");
            }
        } else {
            if (moneyValue != 0) {
                throw new ServiceException("非收费视频类型的金额需为 0");
            }
        }
        
        if (moneyValue > MONEY_MAX_VALUE) {
            throw new ServiceException("金额不能超过 9999");
        }
    }

    public static void validateTypeLogic(String type, Integer money, List<VideoAd> adList) {
        if (StringUtils.isEmpty(type)) {
            return;
        }
        
        int moneyValue = (money == null) ? 0 : money;
        boolean hasAdList = adList != null && !adList.isEmpty();
        
        switch (type) {
            case "1":
                if (hasAdList) {
                    throw new ServiceException("免费无广告视频的广告列表必须为空");
                }
                if (moneyValue != 0) {
                    throw new ServiceException("免费无广告视频的金额必须为 0");
                }
                break;
            case "2":
                if (moneyValue <= 0) {
                    throw new ServiceException("收费视频的金额必须大于 0");
                }
                break;
            case "3":
                if (!hasAdList) {
                    throw new ServiceException("免费有广告视频的广告列表不能为空");
                }
                if (moneyValue != 0) {
                    throw new ServiceException("免费有广告视频的金额必须为 0");
                }
                break;
            default:
                throw new ServiceException("视频类型不合法，仅支持 1、2、3");
        }
    }

    public static void validateNumericField(String fieldName, String value) {
        if (StringUtils.isNotEmpty(value)) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ServiceException(fieldName + " 必须为数字");
            }
        }
    }
}
