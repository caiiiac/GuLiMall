package com.caiiiac.gulimall.coupon.service.impl;

import com.caiiiac.common.to.MemberPrice;
import com.caiiiac.common.to.SkuReductionTo;
import com.caiiiac.gulimall.coupon.entity.MemberPriceEntity;
import com.caiiiac.gulimall.coupon.entity.SkuLadderEntity;
import com.caiiiac.gulimall.coupon.service.MemberPriceService;
import com.caiiiac.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.common.utils.Query;

import com.caiiiac.gulimall.coupon.dao.SkuFullReductionDao;
import com.caiiiac.gulimall.coupon.entity.SkuFullReductionEntity;
import com.caiiiac.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo reductionTo) {

        // sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(reductionTo.getSkuId());
        skuLadderEntity.setFullCount(reductionTo.getFullCound());
        skuLadderEntity.setDiscount(reductionTo.getDiscount());
        skuLadderEntity.setAddOther(reductionTo.getCountStatus());
        if (reductionTo.getFullCound() > 0) {
            skuLadderService.save(skuLadderEntity);
        }

        // sms_sku_full_reduction
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(reductionTo, reductionEntity);
        if (reductionEntity.getFullPrice().compareTo(BigDecimal.ZERO) == 1) {
            this.save(reductionEntity);
        }

        // sms_member_price
        List<MemberPrice> memberPrices = reductionTo.getMemberPrice();

        List<MemberPriceEntity> collect = memberPrices.stream().map(item -> {
            MemberPriceEntity priceEntity = new MemberPriceEntity();
            priceEntity.setSkuId(reductionTo.getSkuId());
            priceEntity.setMemberLevelId(item.getId());
            priceEntity.setMemberLevelName(item.getName());
            priceEntity.setAddOther(1);
            return priceEntity;
        }).filter(item ->
            item.getMemberPrice().compareTo(BigDecimal.ZERO) == 1
        ).collect(Collectors.toList());
        memberPriceService.saveBatch(collect);
    }

}