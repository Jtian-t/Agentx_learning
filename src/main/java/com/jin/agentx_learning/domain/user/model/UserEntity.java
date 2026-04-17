package com.jin.agentx_learning.domain.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jin.agentx_learning.infrastructure.entity.BaseEntity;
import com.jin.agentx_learning.infrastructure.exception.BusinessException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

@TableName("users")
public class UserEntity extends BaseEntity {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String nickname;
    private String email;
    private String phone;
    private String password;
    private String githubId;
    private String githubLogin;
    private String avatarUrl;  // avatarUrl 👉 头像 URL
    private String loginPlatform; //作用：标记用户是从哪个平台登录 / 注册的。
    private Boolean isAdmin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public String getGithubLogin() {
        return githubLogin;
    }

    public void setGithubLogin(String githubLogin) {
        this.githubLogin = githubLogin;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLoginPlatform() {
        return loginPlatform;
    }

    public void setLoginPlatform(String loginPlatform) {
        this.loginPlatform = loginPlatform;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return Boolean.TRUE.equals(isAdmin);
    }

    public void valid() {
        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(githubId)) {
            throw new BusinessException("必须使用邮箱、手机号或GitHub账号来作为账号");
        }
    }
}
