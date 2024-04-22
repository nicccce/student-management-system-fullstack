package com.teach.javafxclient.request;

/**

 JwtResponse是JWT数据返回对象，用于包含客户登录的信息。

 字段说明：

 tokenType: 令牌类型
 id: 用户ID
 username: 用户登录名
 accessToken: 登录客户加密数据串，用于向后端进行权限验证的请求传输
 roles: 用户角色，包括ROLE_ADMIN、ROLE_STUDENT和ROLE_TEACHER
 */
public class JwtResponse {
    private String tokenType;
    private Integer id;
    private String username;
    private String accessToken;
    private String roles;

    /**
     获取令牌类型
     @return 令牌类型
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     设置令牌类型
     @param tokenType 令牌类型
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /*
     获取用户ID
     @return 用户ID
     */
    public Integer getId() {
        return id;
    }

    /**
     设置用户ID
     @param id 用户ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     获取用户登录名
     @return 用户登录名
     */
    public String getUsername() {
        return username;
    }

    /**
     设置用户登录名
     @param username 用户登录名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     获取登录客户加密数据串
     @return 登录客户加密数据串
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     设置登录客户加密数据串
     @param accessToken 登录客户加密数据串
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     获取用户角色
     @return 用户角色
     */
    public String getRoles() {
        return roles;
    }

    /**
     设置用户角色
     @param roles 用户角色
     */
    public void setRoles(String roles) {
        this.roles = roles;
    }
}