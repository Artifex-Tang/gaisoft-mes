package com.gaisoft.web.controller.tool;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="UserEntity", description="用户实体")
class UserEntity {
    @ApiModelProperty(value="用户ID")
    private Integer userId;
    @ApiModelProperty(value="用户名称")
    private String username;
    @ApiModelProperty(value="用户密码")
    private String password;
    @ApiModelProperty(value="用户手机")
    private String mobile;

    public UserEntity() {
    }

    public UserEntity(Integer userId, String username, String password, String mobile) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
