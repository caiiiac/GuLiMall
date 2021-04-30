package com.caiiiac.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiiiac.common.utils.PageUtils;
import com.caiiiac.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-29 15:26:21
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

