package org.fatmansoft.teach.payload.response;

/**
 * JwtResponse JWT数据返回对象 包含客户登录的信息
 * String token token字符串
 * String type JWT 类型
 * Integer id 用户的ID user_id
 * String username 用户的登录名
 * String role 用户角色 ROLE_ADMIN, ROLE_STUDENT, ROLE_TEACHER
 */

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private String role;

    public JwtResponse(String accessToken, Integer id, String username, String role) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
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

    public String getRoles() {
        return role;
    }
}