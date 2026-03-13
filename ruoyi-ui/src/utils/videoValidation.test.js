/**
 * videoValidation.js 单元测试
 * 使用 Node.js 简单测试框架
 *
 * 运行方式: node src/utils/videoValidation.test.js
 */

const {
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
  validateVideo
} = require('./videoValidation')

// 简单的测试框架
let passedTests = 0
let failedTests = 0

function test(name, fn) {
  try {
    fn()
    console.log(`✅ PASS: ${name}`)
    passedTests++
  } catch (error) {
    console.log(`❌ FAIL: ${name}`)
    console.log(`   Error: ${error.message}`)
    failedTests++
  }
}

function assertEqual(actual, expected, message) {
  if (actual !== expected) {
    throw new Error(`${message || 'Assertion failed'}: expected ${expected}, got ${actual}`)
  }
}

function assertTrue(value, message) {
  if (value !== true) {
    throw new Error(message || 'Expected true but got false')
  }
}

function assertFalse(value, message) {
  if (value !== false) {
    throw new Error(message || 'Expected false but got true')
  }
}

// ==================== 常量测试 ====================

test('常量定义正确', () => {
  assertEqual(TITLE_MAX_LENGTH, 100, 'TITLE_MAX_LENGTH should be 100')
  assertEqual(TITLE_MIN_LENGTH, 1, 'TITLE_MIN_LENGTH should be 1')
  assertEqual(URL_MAX_LENGTH, 500, 'URL_MAX_LENGTH should be 500')
  assertEqual(MONEY_MAX_VALUE, 9999, 'MONEY_MAX_VALUE should be 9999')
})

test('视频类型枚举定义正确', () => {
  assertEqual(VIDEO_TYPE.FREE, '1', 'FREE type should be 1')
  assertEqual(VIDEO_TYPE.VIP, '2', 'VIP type should be 2')
  assertEqual(VIDEO_TYPE.FREE_HAVE_AD, '3', 'FREE_HAVE_AD type should be 3')
})

// ==================== 标题校验测试 ====================

test('validateTitle - 合法标题应该通过', () => {
  const result = validateTitle('测试视频标题')
  assertTrue(result.valid, 'Valid title should pass')
  assertEqual(result.message, '', 'Error message should be empty')
})

test('validateTitle - 空标题应该失败', () => {
  const result = validateTitle('')
  assertFalse(result.valid, 'Empty title should fail')
  assertTrue(result.message.includes('不能为空'), 'Error message should contain 不能为空')
})

test('validateTitle - null标题应该失败', () => {
  const result = validateTitle(null)
  assertFalse(result.valid, 'Null title should fail')
})

test('validateTitle - undefined标题应该失败', () => {
  const result = validateTitle(undefined)
  assertFalse(result.valid, 'Undefined title should fail')
})

test('validateTitle - 超过100字符应该失败', () => {
  const longTitle = 'a'.repeat(101)
  const result = validateTitle(longTitle)
  assertFalse(result.valid, 'Title over 100 chars should fail')
  assertTrue(result.message.includes('1-100'), 'Error message should contain length info')
})

test('validateTitle - 100字符边界值应该通过', () => {
  const title = 'a'.repeat(100)
  const result = validateTitle(title)
  assertTrue(result.valid, 'Title with 100 chars should pass')
})

test('validateTitle - 1字符边界值应该通过', () => {
  const result = validateTitle('a')
  assertTrue(result.valid, 'Title with 1 char should pass')
})

test('validateTitle - 包含非法字符应该失败', () => {
  const result = validateTitle('测试标题@#$%')
  assertFalse(result.valid, 'Title with special chars should fail')
  assertTrue(result.message.includes('非法'), 'Error message should contain 非法')
})

test('validateTitle - 允许的特殊字符应该通过', () => {
  const result = validateTitle('测试标题-Test_123')
  assertTrue(result.valid, 'Title with allowed special chars should pass')
})

test('validateTitle - 去除首尾空格后合法应该通过', () => {
  const result = validateTitle('  测试标题  ')
  assertTrue(result.valid, 'Title with trimmed spaces should pass')
})

test('validateTitle - 纯中文应该通过', () => {
  const result = validateTitle('中文视频标题')
  assertTrue(result.valid, 'Chinese title should pass')
})

test('validateTitle - 纯英文应该通过', () => {
  const result = validateTitle('EnglishTitle')
  assertTrue(result.valid, 'English title should pass')
})

test('validateTitle - 纯数字应该通过', () => {
  const result = validateTitle('123456')
  assertTrue(result.valid, 'Numeric title should pass')
})

// ==================== URL校验测试 ====================

test('validateUrl - 合法HTTP URL应该通过', () => {
  const result = validateUrl('http://example.com/video.mp4')
  assertTrue(result.valid, 'Valid HTTP URL should pass')
})

test('validateUrl - 合法HTTPS URL应该通过', () => {
  const result = validateUrl('https://example.com/video.mp4')
  assertTrue(result.valid, 'Valid HTTPS URL should pass')
})

test('validateUrl - 空URL应该失败', () => {
  const result = validateUrl('')
  assertFalse(result.valid, 'Empty URL should fail')
  assertTrue(result.message.includes('不能为空'), 'Error message should contain 不能为空')
})

test('validateUrl - null URL应该失败', () => {
  const result = validateUrl(null)
  assertFalse(result.valid, 'Null URL should fail')
})

test('validateUrl - FTP协议应该失败', () => {
  const result = validateUrl('ftp://example.com/video.mp4')
  assertFalse(result.valid, 'FTP URL should fail')
  assertTrue(result.message.includes('格式错误'), 'Error message should contain format error')
})

test('validateUrl - 非视频格式应该失败', () => {
  const result = validateUrl('https://example.com/video.txt')
  assertFalse(result.valid, 'Non-video format should fail')
  assertTrue(result.message.includes('仅支持'), 'Error message should contain format support info')
})

test('validateUrl - 超过500字符应该失败', () => {
  const longUrl = 'https://example.com/' + 'a'.repeat(500) + '.mp4'
  const result = validateUrl(longUrl)
  assertFalse(result.valid, 'URL over 500 chars should fail')
  assertTrue(result.message.includes('500'), 'Error message should contain 500')
})

test('validateUrl - 500字符边界值应该通过', () => {
  const url = 'https://example.com/' + 'a'.repeat(489) + '.mp4' // 总长度500
  const result = validateUrl(url)
  assertTrue(result.valid, 'URL with 500 chars should pass')
})

// 测试各种视频格式
test('validateUrl - MP4格式应该通过', () => {
  const result = validateUrl('https://example.com/video.mp4')
  assertTrue(result.valid, 'MP4 format should pass')
})

test('validateUrl - AVI格式应该通过', () => {
  const result = validateUrl('https://example.com/video.avi')
  assertTrue(result.valid, 'AVI format should pass')
})

test('validateUrl - MOV格式应该通过', () => {
  const result = validateUrl('https://example.com/video.mov')
  assertTrue(result.valid, 'MOV format should pass')
})

test('validateUrl - RMVB格式应该通过', () => {
  const result = validateUrl('https://example.com/video.rmvb')
  assertTrue(result.valid, 'RMVB format should pass')
})

test('validateUrl - FLV格式应该通过', () => {
  const result = validateUrl('https://example.com/video.flv')
  assertTrue(result.valid, 'FLV format should pass')
})

test('validateUrl - WMV格式应该通过', () => {
  const result = validateUrl('https://example.com/video.wmv')
  assertTrue(result.valid, 'WMV format should pass')
})

test('validateUrl - MKV格式应该通过', () => {
  const result = validateUrl('https://example.com/video.mkv')
  assertTrue(result.valid, 'MKV format should pass')
})

test('validateUrl - 大写格式应该通过（不区分大小写）', () => {
  const result = validateUrl('https://example.com/video.MP4')
  assertTrue(result.valid, 'Uppercase format should pass')
})

// ==================== 金额校验测试 ====================

test('validateMoney - 收费视频合法金额应该通过', () => {
  const result = validateMoney(100, VIDEO_TYPE.VIP)
  assertTrue(result.valid, 'Valid money for VIP should pass')
})

test('validateMoney - 收费视频金额为null应该失败', () => {
  const result = validateMoney(null, VIDEO_TYPE.VIP)
  assertFalse(result.valid, 'Null money for VIP should fail')
  assertTrue(result.message.includes('收费视频'), 'Error message should contain 收费视频')
})

test('validateMoney - 收费视频金额为undefined应该失败', () => {
  const result = validateMoney(undefined, VIDEO_TYPE.VIP)
  assertFalse(result.valid, 'Undefined money for VIP should fail')
})

test('validateMoney - 收费视频金额为空字符串应该失败', () => {
  const result = validateMoney('', VIDEO_TYPE.VIP)
  assertFalse(result.valid, 'Empty string money for VIP should fail')
})

test('validateMoney - 收费视频金额为0应该失败', () => {
  const result = validateMoney(0, VIDEO_TYPE.VIP)
  assertFalse(result.valid, 'Zero money for VIP should fail')
  assertTrue(result.message.includes('大于 0'), 'Error message should contain 大于 0')
})

test('validateMoney - 收费视频金额为负数应该失败', () => {
  const result = validateMoney(-10, VIDEO_TYPE.VIP)
  assertFalse(result.valid, 'Negative money for VIP should fail')
})

test('validateMoney - 收费视频金额超过9999应该失败', () => {
  const result = validateMoney(10000, VIDEO_TYPE.VIP)
  assertFalse(result.valid, 'Money over 9999 for VIP should fail')
  assertTrue(result.message.includes('9999'), 'Error message should contain 9999')
})

test('validateMoney - 收费视频金额为1边界值应该通过', () => {
  const result = validateMoney(1, VIDEO_TYPE.VIP)
  assertTrue(result.valid, 'Money with 1 for VIP should pass')
})

test('validateMoney - 收费视频金额为9999边界值应该通过', () => {
  const result = validateMoney(9999, VIDEO_TYPE.VIP)
  assertTrue(result.valid, 'Money with 9999 for VIP should pass')
})

test('validateMoney - 免费视频金额为0应该通过', () => {
  const result = validateMoney(0, VIDEO_TYPE.FREE)
  assertTrue(result.valid, 'Zero money for FREE should pass')
})

test('validateMoney - 免费视频金额为null应该通过', () => {
  const result = validateMoney(null, VIDEO_TYPE.FREE)
  assertTrue(result.valid, 'Null money for FREE should pass')
})

test('validateMoney - 免费视频金额为undefined应该通过', () => {
  const result = validateMoney(undefined, VIDEO_TYPE.FREE)
  assertTrue(result.valid, 'Undefined money for FREE should pass')
})

test('validateMoney - 免费视频金额有值应该失败', () => {
  const result = validateMoney(100, VIDEO_TYPE.FREE)
  assertFalse(result.valid, 'Money for FREE should fail')
  assertTrue(result.message.includes('非收费视频'), 'Error message should contain 非收费视频')
})

test('validateMoney - 免费含广告视频金额为0应该通过', () => {
  const result = validateMoney(0, VIDEO_TYPE.FREE_HAVE_AD)
  assertTrue(result.valid, 'Zero money for FREE_HAVE_AD should pass')
})

test('validateMoney - 免费含广告视频金额有值应该失败', () => {
  const result = validateMoney(50, VIDEO_TYPE.FREE_HAVE_AD)
  assertFalse(result.valid, 'Money for FREE_HAVE_AD should fail')
})

// ==================== 视频类型校验测试 ====================

test('validateType - 有效类型1应该通过', () => {
  const result = validateType('1')
  assertTrue(result.valid, 'Valid type 1 should pass')
})

test('validateType - 有效类型2应该通过', () => {
  const result = validateType('2')
  assertTrue(result.valid, 'Valid type 2 should pass')
})

test('validateType - 有效类型3应该通过', () => {
  const result = validateType('3')
  assertTrue(result.valid, 'Valid type 3 should pass')
})

test('validateType - 空类型应该失败', () => {
  const result = validateType('')
  assertFalse(result.valid, 'Empty type should fail')
})

test('validateType - null类型应该失败', () => {
  const result = validateType(null)
  assertFalse(result.valid, 'Null type should fail')
})

test('validateType - undefined类型应该失败', () => {
  const result = validateType(undefined)
  assertFalse(result.valid, 'Undefined type should fail')
})

test('validateType - 无效类型应该失败', () => {
  const result = validateType('9')
  assertFalse(result.valid, 'Invalid type should fail')
})

// ==================== 逻辑关联校验测试 ====================

test('validateLogicRelation - 免费无广告视频合法应该通过', () => {
  const video = { type: VIDEO_TYPE.FREE, money: 0, adList: [] }
  const result = validateLogicRelation(video)
  assertTrue(result.valid, 'Valid FREE video should pass')
})

test('validateLogicRelation - 免费无广告视频有广告应该失败', () => {
  const video = { type: VIDEO_TYPE.FREE, money: 0, adList: [{}] }
  const result = validateLogicRelation(video)
  assertFalse(result.valid, 'FREE video with ads should fail')
  assertTrue(result.message.includes('免费无广告'), 'Error message should contain 免费无广告')
})

test('validateLogicRelation - 免费无广告视频有金额应该失败', () => {
  const video = { type: VIDEO_TYPE.FREE, money: 100, adList: [] }
  const result = validateLogicRelation(video)
  assertFalse(result.valid, 'FREE video with money should fail')
})

test('validateLogicRelation - 收费视频合法应该通过', () => {
  const video = { type: VIDEO_TYPE.VIP, money: 100 }
  const result = validateLogicRelation(video)
  assertTrue(result.valid, 'Valid VIP video should pass')
})

test('validateLogicRelation - 免费含广告视频合法应该通过', () => {
  const video = { type: VIDEO_TYPE.FREE_HAVE_AD, money: 0, adList: [{}] }
  const result = validateLogicRelation(video)
  assertTrue(result.valid, 'Valid FREE_HAVE_AD video should pass')
})

test('validateLogicRelation - 免费含广告视频无广告应该失败', () => {
  const video = { type: VIDEO_TYPE.FREE_HAVE_AD, money: 0, adList: [] }
  const result = validateLogicRelation(video)
  assertFalse(result.valid, 'FREE_HAVE_AD video without ads should fail')
  assertTrue(result.message.includes('免费含广告'), 'Error message should contain 免费含广告')
})

test('validateLogicRelation - 免费含广告视频有金额应该失败', () => {
  const video = { type: VIDEO_TYPE.FREE_HAVE_AD, money: 100, adList: [{}] }
  const result = validateLogicRelation(video)
  assertFalse(result.valid, 'FREE_HAVE_AD video with money should fail')
})

// ==================== 完整校验测试 ====================

test('validateVideo - 新增场景合法视频应该通过', () => {
  const video = {
    title: '测试标题',
    url: 'https://example.com/video.mp4',
    type: VIDEO_TYPE.FREE,
    money: 0,
    adList: []
  }
  const result = validateVideo(video, ValidationScene.ADD)
  assertTrue(result.valid, 'Valid video for ADD should pass')
  assertEqual(result.errors.length, 0, 'Errors array should be empty')
})

test('validateVideo - 修改场景合法视频应该通过', () => {
  const video = {
    id: 1,
    title: '测试标题',
    url: 'https://example.com/video.mp4',
    type: VIDEO_TYPE.FREE,
    money: 0,
    adList: []
  }
  const result = validateVideo(video, ValidationScene.UPDATE)
  assertTrue(result.valid, 'Valid video for UPDATE should pass')
})

test('validateVideo - 修改场景无ID应该失败', () => {
  const video = {
    title: '测试标题',
    url: 'https://example.com/video.mp4',
    type: VIDEO_TYPE.FREE,
    money: 0
  }
  const result = validateVideo(video, ValidationScene.UPDATE)
  assertFalse(result.valid, 'Video without ID for UPDATE should fail')
})

test('validateVideo - null视频应该失败', () => {
  const result = validateVideo(null, ValidationScene.ADD)
  assertFalse(result.valid, 'Null video should fail')
})

test('validateVideo - 多个错误应该全部返回', () => {
  const video = {
    title: '',
    url: 'invalid-url',
    type: VIDEO_TYPE.VIP,
    money: null
  }
  const result = validateVideo(video, ValidationScene.ADD)
  assertFalse(result.valid, 'Invalid video should fail')
  assertTrue(result.errors.length > 1, 'Should have multiple errors')
})

test('validateVideo - 收费视频合法应该通过', () => {
  const video = {
    title: '测试标题',
    url: 'https://example.com/video.mp4',
    type: VIDEO_TYPE.VIP,
    money: 100
  }
  const result = validateVideo(video, ValidationScene.ADD)
  assertTrue(result.valid, 'Valid VIP video should pass')
})

test('validateVideo - 免费含广告视频合法应该通过', () => {
  const video = {
    title: '测试标题',
    url: 'https://example.com/video.mp4',
    type: VIDEO_TYPE.FREE_HAVE_AD,
    money: 0,
    adList: [{}]
  }
  const result = validateVideo(video, ValidationScene.ADD)
  assertTrue(result.valid, 'Valid FREE_HAVE_AD video should pass')
})

// ==================== 测试报告 ====================

console.log('\n================== 测试报告 ==================')
console.log(`总测试数: ${passedTests + failedTests}`)
console.log(`通过: ${passedTests}`)
console.log(`失败: ${failedTests}`)
console.log('==============================================\n')

if (failedTests > 0) {
  process.exit(1)
}
