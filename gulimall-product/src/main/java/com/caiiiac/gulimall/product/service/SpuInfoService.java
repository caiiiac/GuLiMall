package com.caiiiac.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.gulimall.product.entity.SpuInfoDescEntity;
import com.caiiiac.gulimall.product.entity.SpuInfoEntity;
import com.caiiiac.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-28 16:46:01
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

