package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.entity.Video;
import com.ruoyi.common.enums.VideoType;
import com.ruoyi.common.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 视频字段校验工具类
 * 用于视频新增/修改时的字段校验
 *
 * @author ruoyi
 */
public class VideoValidationUtils {

    /** 视频标题最大长度 */
    public static final int TITLE_MAX_LENGTH = 100;
    /** 视频标题最小长度 */
    public static final int TITLE_MIN_LENGTH = 1;
    /** URL最大长度 */
    public static final int URL_MAX_LENGTH = 500;
    /** 金额最大值 */
    public static final int MONEY_MAX_VALUE = 9999;

    /** 标题允许的特殊字符 */
    private static final String TITLE_SPECIAL_CHARS = "-_";
    /** 标题校验正则：仅允许中文、英文、数字、-_ */
    private static final Pattern TITLE_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9\\-_]+$");
    /** URL格式校验正则 */
    private static final Pattern URL_PATTERN = Pattern.compile("^(http|https)://.*$");
    /** 视频格式校验正则 */
    private static final Pattern VIDEO_FORMAT_PATTERN = Pattern.compile(".*\\.(mp4|avi|mov|rmvb|flv|wmv|mkv)$", Pattern.CASE_INSENSITIVE);

    /**
     * 校验场景枚举
     */
    public enum ValidationScene {
        ADD("新增"),
        UPDATE("修改");

        private final String desc;

        ValidationScene(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 校验结果
     */
    public static class ValidationResult {
        private boolean valid;
        private List<String> errors;

        public ValidationResult() {
            this.valid = true;
            this.errors = new ArrayList<>();
        }

        public void addError(String error) {
            this.errors.add(error);
            this.valid = false;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getErrorMessage() {
            return String.join("; ", errors);
        }
    }

    /**
     * 执行完整校验（新增场景）
     *
     * @param video 视频对象
     * @return 校验结果
     */
    public static ValidationResult validateForAdd(Video video) {
        return validate(video, ValidationScene.ADD);
    }

    /**
     * 执行完整校验（修改场景）
     *
     * @param video 视频对象
     * @return 校验结果
     */
    public static ValidationResult validateForUpdate(Video video) {
        return validate(video, ValidationScene.UPDATE);
    }

    /**
     * 执行完整校验
     *
     * @param video 视频对象
     * @param scene 校验场景
     * @return 校验结果
     */
    public static ValidationResult validate(Video video, ValidationScene scene) {
        ValidationResult result = new ValidationResult();

        if (video == null) {
            result.addError("视频对象不能为空");
            return result;
        }

        // 修改场景需要校验ID
        if (scene == ValidationScene.UPDATE) {
            validateId(video.getId(), result);
        }

        // 基础字段校验
        validateTitle(video.getTitle(), result);
        validateUrl(video.getUrl(), result);
        validateMoney(video.getMoney(), video.getType(), result);

        // 逻辑关联校验
        validateLogicRelation(video, result);

        return result;
    }

    /**
     * 校验ID（修改场景）
     */
    private static void validateId(Long id, ValidationResult result) {
        if (id == null || id <= 0) {
            result.addError("视频ID不能为空");
        }
    }

    /**
     * 校验视频标题
     * 规则：
     * 1. 非空
     * 2. 长度 1~100 字符
     * 3. 不包含特殊字符（仅允许中文、英文、数字、-_）
     */
    public static void validateTitle(String title, ValidationResult result) {
        // 去除首尾空格
        if (StringUtils.isNotEmpty(title)) {
            title = title.trim();
        }

        // 非空校验
        if (StringUtils.isEmpty(title)) {
            result.addError("视频标题不能为空");
            return;
        }

        // 长度校验
        int length = title.length();
        if (length < TITLE_MIN_LENGTH || length > TITLE_MAX_LENGTH) {
            result.addError("标题长度需在 1-100 字符之间");
            return;
        }

        // 字符范围校验
        if (!TITLE_PATTERN.matcher(title).matches()) {
            result.addError("标题包含非法特殊字符，仅允许中文、英文、数字、-_");
        }
    }

    /**
     * 校验视频URL
     * 规则：
     * 1. 非空
     * 2. 格式为合法视频 URL（http/https 开头）
     * 3. 支持常见视频格式
     * 4. 长度≤500 字符
     */
    public static void validateUrl(String url, ValidationResult result) {
        // 去除首尾空格
        if (StringUtils.isNotEmpty(url)) {
            url = url.trim();
        }

        // 非空校验
        if (StringUtils.isEmpty(url)) {
            result.addError("视频 URL 不能为空");
            return;
        }

        // 长度校验
        if (url.length() > URL_MAX_LENGTH) {
            result.addError("视频 URL 长度不能超过 500 字符");
            return;
        }

        // URL格式校验
        if (!URL_PATTERN.matcher(url).matches()) {
            result.addError("视频 URL 格式错误，必须以 http:// 或 https:// 开头");
            return;
        }

        // 视频格式校验
        if (!VIDEO_FORMAT_PATTERN.matcher(url).matches()) {
            result.addError("仅支持 mp4、avi、mov、rmvb、flv、wmv、mkv 格式的视频");
        }
    }

    /**
     * 校验金额
     * 规则：
     * 1. 收费视频(type=2)时必填且数值 > 0
     * 2. 非收费视频(type≠2)时必须为 null/0
     * 3. 数值≤9999
     */
    public static void validateMoney(Integer money, String type, ValidationResult result) {
        boolean isVipType = VideoType.VIP.getCode().equals(type);

        if (isVipType) {
            // 收费视频时金额必填且 > 0
            if (money == null) {
                result.addError("收费视频时金额不能为空");
                return;
            }
            if (money <= 0) {
                result.addError("金额必须大于 0");
                return;
            }
            if (money > MONEY_MAX_VALUE) {
                result.addError("金额不能超过 9999");
            }
        } else {
            // 非收费视频时金额必须为 null 或 0
            if (money != null && money != 0) {
                result.addError("非收费视频时金额需为 0 或空");
            }
        }
    }

    /**
     * 校验逻辑关联关系
     * 免费无广告(type=1)：adList 必须为空，且 money 必须为 0
     * 收费视频(type=2)：money 必须 > 0（已在validateMoney中校验）
     * 免费有广告(type=3)：adList 必须非空，且 money 必须为 0
     */
    private static void validateLogicRelation(Video video, ValidationResult result) {
        String type = video.getType();
        Integer money = video.getMoney();
        List<?> adList = video.getAdList();

        if (StringUtils.isEmpty(type)) {
            result.addError("视频类型不能为空");
            return;
        }

        switch (type) {
            case "1": // 免费无广告
                if (adList != null && !adList.isEmpty()) {
                    result.addError("免费无广告视频不能配置广告列表");
                }
                if (money != null && money != 0) {
                    result.addError("免费无广告视频金额必须为 0");
                }
                break;

            case "2": // 收费视频
                // 金额校验在 validateMoney 中处理
                break;

            case "3": // 免费有广告
                if (adList == null || adList.isEmpty()) {
                    result.addError("免费含广告视频必须配置广告列表");
                }
                if (money != null && money != 0) {
                    result.addError("免费含广告视频金额必须为 0");
                }
                break;

            default:
                result.addError("未知的视频类型: " + type);
        }
    }

    /**
     * 校验标题唯一性（后端额外校验）
     * 新增时全量校验，修改时排除自身 ID
     *
     * @param title    标题
     * @param videoId  视频ID（修改时传入，新增时传null）
     * @param exists   是否已存在相同标题的视频
     * @return 校验结果
     */
    public static ValidationResult validateTitleUnique(String title, Long videoId, boolean exists) {
        ValidationResult result = new ValidationResult();

        if (StringUtils.isEmpty(title)) {
            result.addError("视频标题不能为空");
            return result;
        }

        if (exists) {
            if (videoId == null) {
                // 新增场景
                result.addError("视频标题已存在，请更换标题");
            } else {
                // 修改场景
                result.addError("视频标题已被其他视频使用，请更换标题");
            }
        }

        return result;
    }

    /**
     * 校验并抛出异常（便捷方法）
     *
     * @param video 视频对象
     * @param scene 校验场景
     */
    public static void validateAndThrow(Video video, ValidationScene scene) {
        ValidationResult result = validate(video, scene);
        if (!result.isValid()) {
            throw new ServiceException(result.getErrorMessage());
        }
    }
}
