package com.caiiiac.gulimall.product.service.impl;

import com.caiiiac.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.common.utils.Query;

import com.caiiiac.gulimall.product.dao.CategoryDao;
import com.caiiiac.gulimall.product.entity.CategoryEntity;
import com.caiiiac.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

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

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findCatelogPath(catelogId, paths);

        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     * @param category
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);

        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    /**
     * 递归查找父 id
     * @param catelogId
     * @param paths
     * @return
     */
    private List<Long> findCatelogPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity entity = this.getById(catelogId);
        if (entity.getParentCid() != 0) {
            findCatelogPath(entity.getParentCid(), paths);
        }
        return paths;
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