package com.gaisoft.framework.manager.factory;

import com.gaisoft.common.utils.LogUtils;
import com.gaisoft.common.utils.ServletUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.ip.AddressUtils;
import com.gaisoft.common.utils.ip.IpUtils;
import com.gaisoft.common.utils.spring.SpringUtils;
import com.gaisoft.system.domain.SysLogininfor;
import com.gaisoft.system.domain.SysOperLog;
import com.gaisoft.system.service.ISysLogininforService;
import com.gaisoft.system.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger((String)"sys-user");

    public static TimerTask recordLogininfor(final String username, final String status, final String message, final Object ... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString((String)ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask(){

            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP((String)ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock((Object)ip));
                s.append(address);
                s.append(LogUtils.getBlock((Object)username));
                s.append(LogUtils.getBlock((Object)status));
                s.append(LogUtils.getBlock((Object)message));
                sys_user_logger.info(s.toString(), args);
                String os = userAgent.getOperatingSystem().getName();
                String browser = userAgent.getBrowser().getName();
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                if (StringUtils.equalsAny((CharSequence)status, (CharSequence[])new CharSequence[]{"Success", "Logout", "Register"})) {
                    logininfor.setStatus("0");
                } else if ("Error".equals(status)) {
                    logininfor.setStatus("1");
                }
                ((ISysLogininforService)SpringUtils.getBean(ISysLogininforService.class)).insertLogininfor(logininfor);
            }
        };
    }

    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask(){

            @Override
            public void run() {
                operLog.setOperLocation(AddressUtils.getRealAddressByIP((String)operLog.getOperIp()));
                ((ISysOperLogService)SpringUtils.getBean(ISysOperLogService.class)).insertOperlog(operLog);
            }
        };
    }
}
