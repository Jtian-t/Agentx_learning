package com.jin.agentx_learning.domain.user.repository;

import com.jin.agentx_learning.domain.user.model.UserSettingsEntity;
import com.jin.agentx_learning.infrastructure.repository.MyBatisPlusExtRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSettingsRepository extends MyBatisPlusExtRepository<UserSettingsEntity> {
}
