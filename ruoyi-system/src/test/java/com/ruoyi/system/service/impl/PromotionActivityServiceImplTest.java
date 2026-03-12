package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.PromotionActivity;
import com.ruoyi.system.mapper.PromotionActivityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PromotionActivityServiceImplTest {

    @Mock
    private PromotionActivityMapper promotionActivityMapper;

    @InjectMocks
    private PromotionActivityServiceImpl promotionActivityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSelectPromotionActivityById() {
        PromotionActivity activity = new PromotionActivity();
        activity.setId(1L);
        activity.setTitle("测试活动");

        when(promotionActivityMapper.selectPromotionActivityById(1L)).thenReturn(activity);

        PromotionActivity result = promotionActivityService.selectPromotionActivityById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试活动", result.getTitle());
        verify(promotionActivityMapper, times(1)).selectPromotionActivityById(1L);
    }

    @Test
    void testSelectPromotionActivityList() {
        PromotionActivity activity = new PromotionActivity();
        activity.setId(1L);
        activity.setTitle("测试活动");

        when(promotionActivityMapper.selectPromotionActivityList(any())).thenReturn(Arrays.asList(activity));

        List<PromotionActivity> result = promotionActivityService.selectPromotionActivityList(new PromotionActivity());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(promotionActivityMapper, times(1)).selectPromotionActivityList(any());
    }

    @Test
    void testInsertPromotionActivity() {
        PromotionActivity activity = new PromotionActivity();
        activity.setTitle("新增活动");
        activity.setImageUrl("http://example.com/image.jpg");
        activity.setLinkUrl("http://example.com");
        activity.setStatus("0");
        activity.setOrderNum(1L);
        activity.setExpireTime(new Date());

        when(promotionActivityMapper.insertPromotionActivity(any())).thenReturn(1);

        int result = promotionActivityService.insertPromotionActivity(activity);

        assertEquals(1, result);
        assertNotNull(activity.getCreateTime());
        verify(promotionActivityMapper, times(1)).insertPromotionActivity(any());
    }

    @Test
    void testUpdatePromotionActivity() {
        PromotionActivity activity = new PromotionActivity();
        activity.setId(1L);
        activity.setTitle("修改活动");
        activity.setStatus("1");

        when(promotionActivityMapper.updatePromotionActivity(any())).thenReturn(1);

        int result = promotionActivityService.updatePromotionActivity(activity);

        assertEquals(1, result);
        assertNotNull(activity.getUpdateTime());
        verify(promotionActivityMapper, times(1)).updatePromotionActivity(any());
    }

    @Test
    void testDeletePromotionActivityByIds() {
        Long[] ids = {1L, 2L};

        when(promotionActivityMapper.deletePromotionActivityByIds(ids)).thenReturn(2);

        int result = promotionActivityService.deletePromotionActivityByIds(ids);

        assertEquals(2, result);
        verify(promotionActivityMapper, times(1)).deletePromotionActivityByIds(ids);
    }

    @Test
    void testDeletePromotionActivityById() {
        when(promotionActivityMapper.deletePromotionActivityById(1L)).thenReturn(1);

        int result = promotionActivityService.deletePromotionActivityById(1L);

        assertEquals(1, result);
        verify(promotionActivityMapper, times(1)).deletePromotionActivityById(1L);
    }
}
