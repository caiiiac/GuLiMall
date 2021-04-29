package com.caiiiac.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caiiiac.gulimall.product.entity.BrandEntity;
import com.caiiiac.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {

        BrandEntity brandEntity = new BrandEntity();

//        brandEntity.setName("苹果");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");

//        brandEntity.setBrandId(6L);
//        brandEntity.setDescript("美国");
//        brandService.updateById(brandEntity);
//        System.out.println("更新成功");

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id",1));
        list.forEach((item) -> {
            System.out.println(item);
        });
    }

}
