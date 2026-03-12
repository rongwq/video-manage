package com.ruoyi.web.controller.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.system.domain.PromotionActivity;
import com.ruoyi.system.service.IPromotionActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PromotionActivityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IPromotionActivityService promotionActivityService;

    @InjectMocks
    private PromotionActivityController promotionActivityController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(promotionActivityController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testList() throws Exception {
        PromotionActivity activity = new PromotionActivity();
        activity.setId(1L);
        activity.setTitle("测试活动");

        when(promotionActivityService.selectPromotionActivityList(any())).thenReturn(Arrays.asList(activity));

        mockMvc.perform(get("/system/promotion/list"))
                .andExpect(status().isOk());

        verify(promotionActivityService, times(1)).selectPromotionActivityList(any());
    }

    @Test
    void testGetInfo() throws Exception {
        PromotionActivity activity = new PromotionActivity();
        activity.setId(1L);
        activity.setTitle("测试活动");

        when(promotionActivityService.selectPromotionActivityById(1L)).thenReturn(activity);

        mockMvc.perform(get("/system/promotion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));

        verify(promotionActivityService, times(1)).selectPromotionActivityById(1L);
    }

    @Test
    void testAdd() throws Exception {
        PromotionActivity activity = new PromotionActivity();
        activity.setTitle("新增活动");
        activity.setImageUrl("http://example.com/image.jpg");
        activity.setLinkUrl("http://example.com");
        activity.setStatus("0");
        activity.setOrderNum(1L);
        activity.setExpireTime(new Date());

        when(promotionActivityService.insertPromotionActivity(any())).thenReturn(1);

        mockMvc.perform(post("/system/promotion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activity)))
                .andExpect(status().isOk());

        verify(promotionActivityService, times(1)).insertPromotionActivity(any());
    }

    @Test
    void testEdit() throws Exception {
        PromotionActivity activity = new PromotionActivity();
        activity.setId(1L);
        activity.setTitle("修改活动");
        activity.setStatus("1");

        when(promotionActivityService.updatePromotionActivity(any())).thenReturn(1);

        mockMvc.perform(put("/system/promotion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activity)))
                .andExpect(status().isOk());

        verify(promotionActivityService, times(1)).updatePromotionActivity(any());
    }

    @Test
    void testRemove() throws Exception {
        when(promotionActivityService.deletePromotionActivityByIds(any())).thenReturn(1);

        mockMvc.perform(delete("/system/promotion/1,2"))
                .andExpect(status().isOk());

        verify(promotionActivityService, times(1)).deletePromotionActivityByIds(any());
    }
}
