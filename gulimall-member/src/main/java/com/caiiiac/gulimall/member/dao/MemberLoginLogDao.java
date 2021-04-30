package com.caiiiac.gulimall.member.dao;

import com.caiiiac.gulimall.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-29 15:26:22
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
