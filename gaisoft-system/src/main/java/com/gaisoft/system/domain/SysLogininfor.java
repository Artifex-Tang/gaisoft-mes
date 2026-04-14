package com.gaisoft.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import java.util.Date;

public class SysLogininfor
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="序号", cellType=Excel.ColumnType.NUMERIC)
    private Long infoId;
    @Excel(name="用户账号")
    private String userName;
    @Excel(name="登录状态", readConverterExp="0=成功,1=失败")
    private String status;
    @Excel(name="登录地址")
    private String ipaddr;
    @Excel(name="登录地点")
    private String loginLocation;
    @Excel(name="浏览器")
    private String browser;
    @Excel(name="操作系统")
    private String os;
    @Excel(name="提示消息")
    private String msg;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Excel(name="访问时间", width=30.0, dateFormat="yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    public Long getInfoId() {
        return this.infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpaddr() {
        return this.ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation() {
        return this.loginLocation;
    }

    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }

    public String getBrowser() {
        return this.browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return this.os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
