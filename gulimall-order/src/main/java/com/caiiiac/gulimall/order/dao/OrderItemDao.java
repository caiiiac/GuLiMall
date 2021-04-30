package com.caiiiac.gulimall.order.dao;

import com.caiiiac.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-29 15:43:26
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
