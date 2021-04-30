package com.caiiiac.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.gulimall.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-29 15:55:54
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

