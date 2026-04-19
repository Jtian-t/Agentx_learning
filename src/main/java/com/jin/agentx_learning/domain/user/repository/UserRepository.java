package com.jin.agentx_learning.domain.user.repository;


import com.jin.agentx_learning.domain.user.model.UserEntity;
import com.jin.agentx_learning.infrastructure.repository.MyBatisPlusExtRepository;
import org.apache.ibatis.annotations.Mapper;



/** 模型仓储接口 */
@Mapper
public interface UserRepository extends MyBatisPlusExtRepository<UserEntity> {

}