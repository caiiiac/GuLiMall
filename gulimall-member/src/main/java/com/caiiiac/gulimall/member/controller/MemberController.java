package com.caiiiac.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.caiiiac.gulimall.member.feign.CouponFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caiiiac.gulimall.member.entity.MemberEntity;
import com.caiiiac.gulimall.member.service.MemberService;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.common.utils.R;



/**
 * 会员
 *
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-29 15:26:21
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;

    /**
     * 测试远程调用
     * @return
     */
    @RequestMapping("coupons")
    public R test() {
        MemberEntity entity = new MemberEntity();
        entity.setNickname("霸王");
        R memberCoupons = couponFeignService.memberCoupons();
        return R.ok().put("member", entity)
                .put("coupons", memberCoupons.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
