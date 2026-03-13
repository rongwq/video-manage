/**
 * 视频字段校验工具
 * 用于视频新增/修改时的字段校验
 * 与后端 VideoValidationUtils 保持一致
 */

/** 视频标题最大长度 */
export const TITLE_MAX_LENGTH = 100
/** 视频标题最小长度 */
export const TITLE_MIN_LENGTH = 1
/** URL最大长度 */
export const URL_MAX_LENGTH = 500
/** 金额最大值 */
export const MONEY_MAX_VALUE = 9999

/** 视频类型枚举 */
export const VIDEO_TYPE = {
  FREE: '1',        // 免费无广告
  VIP: '2',         // 收费
  FREE_HAVE_AD: '3' // 免费有广告
}

/** 标题校验正则：仅允许中文、英文、数字、-_ */
const TITLE_PATTERN = /^[\u4e00-\u9fa5a-zA-Z0-9\-_]+$/
/** URL格式校验正则 */
const URL_PATTERN = /^(http|https):\/\/.*/
/** 视频格式校验正则 */
const VIDEO_FORMAT_PATTERN = /.*\.(mp4|avi|mov|rmvb|flv|wmv|mkv)$/i

/**
 * 校验场景枚举
 */
export const ValidationScene = {
  ADD: 'ADD',
  UPDATE: 'UPDATE'
}

/**
 * 校验视频标题
 * 规则：
 * 1. 非空
 * 2. 长度 1~100 字符
 * 3. 不包含特殊字符（仅允许中文、英文、数字、-_）
 * @param {string} title 标题
 * @returns {Object} { valid: boolean, message: string }
 */
export function validateTitle(title) {
  // 去除首尾空格
  const trimmedTitle = title ? title.trim() : ''

  // 非空校验
  if (!trimmedTitle) {
    return { valid: false, message: '视频标题不能为空' }
  }

  // 长度校验
  const length = trimmedTitle.length
  if (length < TITLE_MIN_LENGTH || length > TITLE_MAX_LENGTH) {
    return { valid: false, message: '标题长度需在 1-100 字符之间' }
  }

  // 字符范围校验
  if (!TITLE_PATTERN.test(trimmedTitle)) {
    return { valid: false, message: '标题包含非法特殊字符，仅允许中文、英文、数字、-_' }
  }

  return { valid: true, message: '' }
}

/**
 * 校验视频URL
 * 规则：
 * 1. 非空
 * 2. 格式为合法视频 URL（http/https 开头）
 * 3. 支持常见视频格式
 * 4. 长度≤500 字符
 * @param {string} url URL
 * @returns {Object} { valid: boolean, message: string }
 */
export function validateUrl(url) {
  // 去除首尾空格
  const trimmedUrl = url ? url.trim() : ''

  // 非空校验
  if (!trimmedUrl) {
    return { valid: false, message: '视频 URL 不能为空' }
  }

  // 长度校验
  if (trimmedUrl.length > URL_MAX_LENGTH) {
    return { valid: false, message: '视频 URL 长度不能超过 500 字符' }
  }

  // URL格式校验
  if (!URL_PATTERN.test(trimmedUrl)) {
    return { valid: false, message: '视频 URL 格式错误，必须以 http:// 或 https:// 开头' }
  }

  // 视频格式校验
  if (!VIDEO_FORMAT_PATTERN.test(trimmedUrl)) {
    return { valid: false, message: '仅支持 mp4、avi、mov、rmvb、flv、wmv、mkv 格式的视频' }
  }

  return { valid: true, message: '' }
}

/**
 * 校验金额
 * 规则：
 * 1. 收费视频(type=2)时必填且数值 > 0
 * 2. 非收费视频(type≠2)时必须为 null/0
 * 3. 数值≤9999
 * @param {number} money 金额
 * @param {string} type 视频类型
 * @returns {Object} { valid: boolean, message: string }
 */
export function validateMoney(money, type) {
  const isVipType = type === VIDEO_TYPE.VIP

  if (isVipType) {
    // 收费视频时金额必填且 > 0
    if (money === null || money === undefined || money === '') {
      return { valid: false, message: '收费视频时金额不能为空' }
    }
    const numMoney = Number(money)
    if (isNaN(numMoney) || numMoney <= 0) {
      return { valid: false, message: '金额必须大于 0' }
    }
    if (numMoney > MONEY_MAX_VALUE) {
      return { valid: false, message: '金额不能超过 9999' }
    }
  } else {
    // 非收费视频时金额必须为 null 或 0
    if (money !== null && money !== undefined && money !== '' && Number(money) !== 0) {
      return { valid: false, message: '非收费视频时金额需为 0 或空' }
    }
  }

  return { valid: true, message: '' }
}

/**
 * 校验视频类型
 * @param {string} type 视频类型
 * @returns {Object} { valid: boolean, message: string }
 */
export function validateType(type) {
  if (!type) {
    return { valid: false, message: '视频类型不能为空' }
  }
  const validTypes = [VIDEO_TYPE.FREE, VIDEO_TYPE.VIP, VIDEO_TYPE.FREE_HAVE_AD]
  if (!validTypes.includes(type)) {
    return { valid: false, message: '未知的视频类型' }
  }
  return { valid: true, message: '' }
}

/**
 * 校验逻辑关联关系
 * 免费无广告(type=1)：adList 必须为空，且 money 必须为 0
 * 收费视频(type=2)：money 必须 > 0
 * 免费有广告(type=3)：adList 必须非空，且 money 必须为 0
 * @param {Object} video 视频对象
 * @returns {Object} { valid: boolean, message: string }
 */
export function validateLogicRelation(video) {
  const { type, money, adList } = video

  const typeValidation = validateType(type)
  if (!typeValidation.valid) {
    return typeValidation
  }

  switch (type) {
    case VIDEO_TYPE.FREE: // 免费无广告
      if (adList && adList.length > 0) {
        return { valid: false, message: '免费无广告视频不能配置广告列表' }
      }
      if (money !== null && money !== undefined && money !== '' && Number(money) !== 0) {
        return { valid: false, message: '免费无广告视频金额必须为 0' }
      }
      break

    case VIDEO_TYPE.VIP: // 收费视频
      // 金额校验在 validateMoney 中处理
      break

    case VIDEO_TYPE.FREE_HAVE_AD: // 免费有广告
      if (!adList || adList.length === 0) {
        return { valid: false, message: '免费含广告视频必须配置广告列表' }
      }
      if (money !== null && money !== undefined && money !== '' && Number(money) !== 0) {
        return { valid: false, message: '免费含广告视频金额必须为 0' }
      }
      break
  }

  return { valid: true, message: '' }
}

/**
 * 执行完整校验
 * @param {Object} video 视频对象
 * @param {string} scene 校验场景 ADD/UPDATE
 * @returns {Object} { valid: boolean, errors: string[] }
 */
export function validateVideo(video, scene) {
  const errors = []

  if (!video) {
    return { valid: false, errors: ['视频对象不能为空'] }
  }

  // 修改场景需要校验ID
  if (scene === ValidationScene.UPDATE) {
    if (!video.id || video.id <= 0) {
      errors.push('视频ID不能为空')
    }
  }

  // 基础字段校验
  const titleResult = validateTitle(video.title)
  if (!titleResult.valid) {
    errors.push(titleResult.message)
  }

  const urlResult = validateUrl(video.url)
  if (!urlResult.valid) {
    errors.push(urlResult.message)
  }

  const moneyResult = validateMoney(video.money, video.type)
  if (!moneyResult.valid) {
    errors.push(moneyResult.message)
  }

  // 逻辑关联校验
  const logicResult = validateLogicRelation(video)
  if (!logicResult.valid) {
    errors.push(logicResult.message)
  }

  return {
    valid: errors.length === 0,
    errors: errors
  }
}

/**
 * 生成 Element UI 表单校验规则
 * @returns {Object} 表单校验规则对象
 */
export function generateFormRules() {
  // 自定义校验函数
  const titleValidator = (rule, value, callback) => {
    const result = validateTitle(value)
    if (result.valid) {
      callback()
    } else {
      callback(new Error(result.message))
    }
  }

  const urlValidator = (rule, value, callback) => {
    const result = validateUrl(value)
    if (result.valid) {
      callback()
    } else {
      callback(new Error(result.message))
    }
  }

  const moneyValidator = (rule, value, callback, form) => {
    const result = validateMoney(value, form.type)
    if (result.valid) {
      callback()
    } else {
      callback(new Error(result.message))
    }
  }

  return {
    title: [
      { required: true, message: '视频标题不能为空', trigger: 'blur' },
      { validator: titleValidator, trigger: 'blur' }
    ],
    url: [
      { required: true, message: '视频 URL 不能为空', trigger: 'blur' },
      { validator: urlValidator, trigger: 'blur' }
    ],
    type: [
      { required: true, message: '视频类型不能为空', trigger: 'change' }
    ],
    money: [
      { validator: (rule, value, callback) => moneyValidator(rule, value, callback, this.form), trigger: 'change' },
      { validator: (rule, value, callback) => moneyValidator(rule, value, callback, this.form), trigger: 'blur' }
    ]
  }
}

/**
 * 生成 Element UI 表单校验规则（使用闭包获取当前表单数据）
 * @param {Function} getForm 获取表单数据的函数
 * @returns {Object} 表单校验规则对象
 */
export function generateFormRulesWithForm(getForm) {
  // 自定义校验函数
  const titleValidator = (rule, value, callback) => {
    const result = validateTitle(value)
    if (result.valid) {
      callback()
    } else {
      callback(new Error(result.message))
    }
  }

  const urlValidator = (rule, value, callback) => {
    const result = validateUrl(value)
    if (result.valid) {
      callback()
    } else {
      callback(new Error(result.message))
    }
  }

  const moneyValidator = (rule, value, callback) => {
    const form = getForm()
    const result = validateMoney(value, form.type)
    if (result.valid) {
      callback()
    } else {
      callback(new Error(result.message))
    }
  }

  return {
    title: [
      { required: true, message: '视频标题不能为空', trigger: 'blur' },
      { validator: titleValidator, trigger: 'blur' }
    ],
    url: [
      { required: true, message: '视频 URL 不能为空', trigger: 'blur' },
      { validator: urlValidator, trigger: 'blur' }
    ],
    type: [
      { required: true, message: '视频类型不能为空', trigger: 'change' }
    ],
    money: [
      { validator: moneyValidator, trigger: 'change' },
      { validator: moneyValidator, trigger: 'blur' }
    ]
  }
}

export default {
  TITLE_MAX_LENGTH,
  TITLE_MIN_LENGTH,
  URL_MAX_LENGTH,
  MONEY_MAX_VALUE,
  VIDEO_TYPE,
  ValidationScene,
  validateTitle,
  validateUrl,
  validateMoney,
  validateType,
  validateLogicRelation,
  validateVideo,
  generateFormRules,
  generateFormRulesWithForm
}
