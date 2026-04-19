package com.jin.agentx_learning.domain.user.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.agentx_learning.domain.user.auth.model.AuthSettingEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthSettingRepository extends BaseMapper<AuthSettingEntity> {
}
