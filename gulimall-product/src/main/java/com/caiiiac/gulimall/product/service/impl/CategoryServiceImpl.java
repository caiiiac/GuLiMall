package com.caiiiac.gulimall.product.service.impl;

import com.caiiiac.common.utils.R;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.common.utils.Query;

import com.caiiiac.gulimall.product.dao.CategoryDao;
import com.caiiiac.gulimall.product.entity.CategoryEntity;
import com.caiiiac.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);

        List<CategoryEntity> level1Menus = entities.stream().filter(entity ->
             entity.getParentCid() == 0
        ).peek((menu) -> menu.setChildren(getChildrens(menu, entities))
        ).sorted(Comparator.comparingInt(item -> (Optional.ofNullable(item.getSort()).orElse(0)))
        ).collect(Collectors.toList());
        return level1Menus;
    }

    /**
     * 批量删除
     * @param asList
     */
    @Override
    public void removeCategoryByIds(List<Long> asList) {
        // TODO 检查当前删除项,是否被引用
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * 递归查找所有菜单的子菜单
     * @param entity
     * @param list
     * @return
     */
    private List<CategoryEntity> getChildrens(CategoryEntity entity, List<CategoryEntity> list) {

        List<CategoryEntity> childrens = list.stream().filter(item ->
                item.getParentCid().equals(entity.getCatId())
        ).peek((item) -> {
            item.setChildren(getChildrens(item, list));
        }).sorted(Comparator.comparingInt(item -> (Optional.ofNullable(item.getSort()).orElse(0)))
        ).collect(Collectors.toList());

        return childrens;
    }

}