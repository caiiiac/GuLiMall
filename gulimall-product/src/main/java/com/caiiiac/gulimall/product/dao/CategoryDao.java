package com.caiiiac.gulimall.product.dao;

import com.caiiiac.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-28 16:46:01
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
