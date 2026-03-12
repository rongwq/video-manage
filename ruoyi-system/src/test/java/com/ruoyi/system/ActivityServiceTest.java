package com.ruoyi.system;

import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.ruoyi.system.domain.Activity;
import com.ruoyi.system.service.IActivityService;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 活动管理Service单元测试
 *
 * @author ruoyi
 * @date 2024-11-13
 */
@SpringBootTest(classes = com.ruoyi.system.RuoYiSystemApplication.class)
public class ActivityServiceTest
{
    @Autowired
    private IActivityService activityService;

    /**
     * 测试新增活动
     */
    @Test
    public void testInsertActivity()
    {
        Activity activity = new Activity();
        activity.setTitle("测试活动");
        activity.setImageUrl("https://example.com/test.jpg");
        activity.setLinkUrl("https://example.com/activity");
        activity.setStatus("0");
        activity.setExpireTime(new Date(System.currentTimeMillis() + 86400000)); // 1天后过期
        activity.setSortOrder(1L);
        activity.setRemark("测试备注");
        activity.setCreateBy("admin");

        int result = activityService.insertActivity(activity);
        assertTrue(result > 0, "新增活动失败");
        assertNotNull(activity.getId(), "活动ID不应为空");
        System.out.println("新增活动成功，ID: " + activity.getId());
    }

    /**
     * 测试查询活动
     */
    @Test
    public void testSelectActivityById()
    {
        // 先创建一个活动
        Activity activity = new Activity();
        activity.setTitle("查询测试活动");
        activity.setImageUrl("https://example.com/query.jpg");
        activity.setLinkUrl("https://example.com/query");
        activity.setStatus("0");
        activity.setExpireTime(new Date(System.currentTimeMillis() + 86400000));
        activity.setSortOrder(2L);
        activity.setCreateBy("admin");
        activityService.insertActivity(activity);

        // 查询活动
        Activity queryResult = activityService.selectActivityById(activity.getId());
        assertNotNull(queryResult, "查询结果不应为空");
        assertEquals("查询测试活动", queryResult.getTitle(), "活动标题不匹配");
        assertEquals("0", queryResult.getStatus(), "活动状态不匹配");
        System.out.println("查询活动成功: " + queryResult);
    }

    /**
     * 测试查询活动列表
     */
    @Test
    public void testSelectActivityList()
    {
        // 先创建几个活动
        for (int i = 0; i < 3; i++) {
            Activity activity = new Activity();
            activity.setTitle("列表测试活动" + i);
            activity.setImageUrl("https://example.com/list" + i + ".jpg");
            activity.setLinkUrl("https://example.com/list" + i);
            activity.setStatus("0");
            activity.setExpireTime(new Date(System.currentTimeMillis() + 86400000 * (i + 1)));
            activity.setSortOrder((long) i);
            activity.setCreateBy("admin");
            activityService.insertActivity(activity);
        }

        // 查询列表
        Activity queryParam = new Activity();
        queryParam.setStatus("0");
        List<Activity> list = activityService.selectActivityList(queryParam);
        assertNotNull(list, "活动列表不应为空");
        assertTrue(list.size() >= 3, "活动列表数量应大于等于3");
        System.out.println("查询活动列表成功，共" + list.size() + "条记录");
    }

    /**
     * 测试修改活动
     */
    @Test
    public void testUpdateActivity()
    {
        // 先创建一个活动
        Activity activity = new Activity();
        activity.setTitle("修改前活动");
        activity.setImageUrl("https://example.com/before.jpg");
        activity.setLinkUrl("https://example.com/before");
        activity.setStatus("0");
        activity.setExpireTime(new Date(System.currentTimeMillis() + 86400000));
        activity.setSortOrder(5L);
        activity.setCreateBy("admin");
        activityService.insertActivity(activity);

        // 修改活动
        activity.setTitle("修改后活动");
        activity.setStatus("1");
        activity.setSortOrder(10L);
        activity.setUpdateBy("admin");
        int result = activityService.updateActivity(activity);
        assertTrue(result > 0, "修改活动失败");

        // 验证修改结果
        Activity updated = activityService.selectActivityById(activity.getId());
        assertEquals("修改后活动", updated.getTitle(), "活动标题未更新");
        assertEquals("1", updated.getStatus(), "活动状态未更新");
        assertEquals(10L, updated.getSortOrder().longValue(), "排序号未更新");
        System.out.println("修改活动成功: " + updated);
    }

    /**
     * 测试删除活动
     */
    @Test
    public void testDeleteActivityById()
    {
        // 先创建一个活动
        Activity activity = new Activity();
        activity.setTitle("删除测试活动");
        activity.setImageUrl("https://example.com/delete.jpg");
        activity.setLinkUrl("https://example.com/delete");
        activity.setStatus("0");
        activity.setExpireTime(new Date(System.currentTimeMillis() + 86400000));
        activity.setSortOrder(99L);
        activity.setCreateBy("admin");
        activityService.insertActivity(activity);

        Long id = activity.getId();
        assertNotNull(id, "活动ID不应为空");

        // 删除活动
        int result = activityService.deleteActivityById(id);
        assertTrue(result > 0, "删除活动失败");

        // 验证删除结果
        Activity deleted = activityService.selectActivityById(id);
        assertNull(deleted, "活动应已被删除");
        System.out.println("删除活动成功，ID: " + id);
    }

    /**
     * 测试批量删除活动
     */
    @Test
    public void testDeleteActivityByIds()
    {
        // 先创建多个活动
        Long[] ids = new Long[3];
        for (int i = 0; i < 3; i++) {
            Activity activity = new Activity();
            activity.setTitle("批量删除测试活动" + i);
            activity.setImageUrl("https://example.com/batch" + i + ".jpg");
            activity.setLinkUrl("https://example.com/batch" + i);
            activity.setStatus("0");
            activity.setExpireTime(new Date(System.currentTimeMillis() + 86400000));
            activity.setSortOrder((long) (100 + i));
            activity.setCreateBy("admin");
            activityService.insertActivity(activity);
            ids[i] = activity.getId();
        }

        // 批量删除
        int result = activityService.deleteActivityByIds(ids);
        assertTrue(result == 3, "批量删除应删除3条记录");

        // 验证删除结果
        for (Long id : ids) {
            Activity deleted = activityService.selectActivityById(id);
            assertNull(deleted, "活动ID " + id + " 应已被删除");
        }
        System.out.println("批量删除活动成功，共删除" + result + "条记录");
    }

    /**
     * 测试按标题搜索活动
     */
    @Test
    public void testSelectActivityByTitle()
    {
        // 创建特定标题的活动
        Activity activity = new Activity();
        activity.setTitle("搜索特定标题活动");
        activity.setImageUrl("https://example.com/search.jpg");
        activity.setLinkUrl("https://example.com/search");
        activity.setStatus("0");
        activity.setExpireTime(new Date(System.currentTimeMillis() + 86400000));
        activity.setSortOrder(50L);
        activity.setCreateBy("admin");
        activityService.insertActivity(activity);

        // 按标题搜索
        Activity queryParam = new Activity();
        queryParam.setTitle("搜索特定");
        List<Activity> list = activityService.selectActivityList(queryParam);
        assertTrue(list.size() > 0, "应找到匹配的活动");
        System.out.println("按标题搜索成功，找到" + list.size() + "条记录");
    }

    /**
     * 测试按状态筛选活动
     */
    @Test
    public void testSelectActivityByStatus()
    {
        // 创建不同状态的活动
        Activity activity1 = new Activity();
        activity1.setTitle("启用状态活动");
        activity1.setImageUrl("https://example.com/enabled.jpg");
        activity1.setLinkUrl("https://example.com/enabled");
        activity1.setStatus("0");
        activity1.setExpireTime(new Date(System.currentTimeMillis() + 86400000));
        activity1.setSortOrder(60L);
        activity1.setCreateBy("admin");
        activityService.insertActivity(activity1);

        Activity activity2 = new Activity();
        activity2.setTitle("停用状态活动");
        activity2.setImageUrl("https://example.com/disabled.jpg");
        activity2.setLinkUrl("https://example.com/disabled");
        activity2.setStatus("1");
        activity2.setExpireTime(new Date(System.currentTimeMillis() + 86400000));
        activity2.setSortOrder(61L);
        activity2.setCreateBy("admin");
        activityService.insertActivity(activity2);

        // 查询启用状态的活动
        Activity queryParam = new Activity();
        queryParam.setStatus("0");
        List<Activity> enabledList = activityService.selectActivityList(queryParam);
        assertTrue(enabledList.stream().allMatch(a -> "0".equals(a.getStatus())), "所有记录状态应为启用");

        // 查询停用状态的活动
        queryParam.setStatus("1");
        List<Activity> disabledList = activityService.selectActivityList(queryParam);
        assertTrue(disabledList.stream().allMatch(a -> "1".equals(a.getStatus())), "所有记录状态应为停用");

        System.out.println("按状态筛选成功，启用:" + enabledList.size() + ", 停用:" + disabledList.size());
    }
}
