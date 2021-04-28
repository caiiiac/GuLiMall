package com.caiiiac.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.gulimall.product.entity.CategoryEntity;

import java.util.Map;

/**
 * 商品三级分类
 *
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-28 16:46:01
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

