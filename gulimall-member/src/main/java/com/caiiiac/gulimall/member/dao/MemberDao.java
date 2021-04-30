package com.caiiiac.gulimall.member.dao;

import com.caiiiac.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author caiiiac
 * @email caiiiac@163.com
 * @date 2021-04-29 15:26:21
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
