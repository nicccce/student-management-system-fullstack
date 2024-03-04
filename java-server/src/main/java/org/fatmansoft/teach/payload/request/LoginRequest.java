package org.fatmansoft.teach.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * LoginRequest 登录请求数据类
 * String username 用户名
 * String password 密码
 */
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}