package com.teach.javafxclient.request;
/**
 * JwtResponse JWT数据返回对象 包含客户登录的信息
 * String tokenType token字符串
 * Integer id 用户的ID user_id
 * String username 用户的登录名
 * String accessToken 登录客户加密数据串 请求是要传到后端进行权限验证
 * String roles 用户角色 ROLE_ADMIN, ROLE_STUDENT, ROLE_TEACHER
 */

public class JwtResponse {
    private String tokenType;
    private Integer id;
    private String username;
    private String accessToken;
    private String roles;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}