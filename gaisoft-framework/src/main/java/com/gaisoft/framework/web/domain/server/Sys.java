package com.gaisoft.framework.web.domain.server;

public class Sys {
    private String computerName;
    private String computerIp;
    private String userDir;
    private String osName;
    private String osArch;

    public String getComputerName() {
        return this.computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getComputerIp() {
        return this.computerIp;
    }

    public void setComputerIp(String computerIp) {
        this.computerIp = computerIp;
    }

    public String getUserDir() {
        return this.userDir;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    public String getOsName() {
        return this.osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsArch() {
        return this.osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }
}
