package com.caiiiac.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.gulimall.order.entity.OrderReturnReasonEntity;

import java.util.Map;

/**
 * ้่ดงๅๅ 
 *
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-29 15:43:26
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

