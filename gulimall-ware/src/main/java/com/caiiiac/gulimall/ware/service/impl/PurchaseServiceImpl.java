package com.caiiiac.gulimall.ware.service.impl;

import com.caiiiac.common.constant.WareConstant;
import com.caiiiac.gulimall.ware.entity.PurchaseDetailEntity;
import com.caiiiac.gulimall.ware.service.PurchaseDetailService;
import com.caiiiac.gulimall.ware.service.WareSkuService;
import com.caiiiac.gulimall.ware.vo.MergeVo;
import com.caiiiac.gulimall.ware.vo.PurchaseDoneVo;
import com.caiiiac.gulimall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.common.utils.Query;

import com.caiiiac.gulimall.ware.dao.PurchaseDao;
import com.caiiiac.gulimall.ware.entity.PurchaseEntity;
import com.caiiiac.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0)
                .or().eq("status", 1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());

            this.save(purchaseEntity);

            purchaseId = purchaseEntity.getId();
        }

        List<Long> items = mergeVo.getItems();
        Long finalPuchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(item -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item);
            detailEntity.setPurchaseId(finalPuchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(finalPuchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    @Transactional
    @Override
    public void received(List<Long> ids) {
        // 确认当前采购是新建或已分配状态
        List<PurchaseEntity> collect = ids.stream().map(id -> this.getById(id))
                .filter(item -> {
                    if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                            item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                        return true;
                    }
                    return false;
                }).map(item -> {
                    item.setStatus(WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());
                    return item;
                }).collect(Collectors.toList());

        // 改变采购单的状态
        this.updateBatchById(collect);

        // 改变采购项的状态
        collect.forEach(item -> {
            List<PurchaseDetailEntity> detailEntities = purchaseDetailService.listDetailByPUrchaseId(item.getId());
            detailEntities.stream().map(entity -> {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(entity.getId());
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return detailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        });
    }

    @Override
    public void done(PurchaseDoneVo doneVo) {
        Long id = doneVo.getId();

        // 改变采购项状态
        boolean flag = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();

        List<PurchaseDetailEntity> updates = new ArrayList<>();
        for (PurchaseItemDoneVo vo : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (vo.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(vo.getStatus());
            } else {
                // 将成功采购的进行入库
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                PurchaseDetailEntity entity = purchaseDetailService.getById(vo.getItemId());

                wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());

                updates.add(detailEntity);
            }
            detailEntity.setId(vo.getItemId());
            updates.add(detailEntity);
        }
        purchaseDetailService.updateBatchById(updates);

        // 改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);


    }

}