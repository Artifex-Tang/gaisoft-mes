package com.gaisoft.quartz.util;

import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.spring.SpringUtils;
import com.gaisoft.quartz.domain.SysJob;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class JobInvokeUtil {
    public static void invokeMethod(SysJob sysJob) throws Exception {
        String invokeTarget = sysJob.getInvokeTarget();
        String beanName = JobInvokeUtil.getBeanName(invokeTarget);
        String methodName = JobInvokeUtil.getMethodName(invokeTarget);
        List<Object[]> methodParams = JobInvokeUtil.getMethodParams(invokeTarget);
        if (!JobInvokeUtil.isValidClassName(beanName)) {
            Object bean = SpringUtils.getBean((String)beanName);
            JobInvokeUtil.invokeMethod(bean, methodName, methodParams);
        } else {
            Object bean = Class.forName(beanName).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            JobInvokeUtil.invokeMethod(bean, methodName, methodParams);
        }
    }

    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (StringUtils.isNotNull(methodParams) && methodParams.size() > 0) {
            Method method = bean.getClass().getMethod(methodName, JobInvokeUtil.getMethodParamsType(methodParams));
            method.invoke(bean, JobInvokeUtil.getMethodParamsValue(methodParams));
        } else {
            Method method = bean.getClass().getMethod(methodName, new Class[0]);
            method.invoke(bean, new Object[0]);
        }
    }

    public static boolean isValidClassName(String invokeTarget) {
        return StringUtils.countMatches((CharSequence)invokeTarget, (CharSequence)".") > 1;
    }

    public static String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore((String)invokeTarget, (String)"(");
        return StringUtils.substringBeforeLast((String)beanName, (String)".");
    }

    public static String getMethodName(String invokeTarget) {
        String methodName = StringUtils.substringBefore((String)invokeTarget, (String)"(");
        return StringUtils.substringAfterLast((String)methodName, (String)".");
    }

    public static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StringUtils.substringBetweenLast((String)invokeTarget, (String)"(", (String)")");
        if (StringUtils.isEmpty((String)methodStr)) {
            return null;
        }
        String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        LinkedList<Object[]> classs = new LinkedList<Object[]>();
        for (int i = 0; i < methodParams.length; ++i) {
            String str = StringUtils.trimToEmpty((String)methodParams[i]);
            if (StringUtils.startsWithAny((CharSequence)str, (CharSequence[])new CharSequence[]{"'", "\""})) {
                classs.add(new Object[]{StringUtils.substring((String)str, (int)1, (int)(str.length() - 1)), String.class});
                continue;
            }
            if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                classs.add(new Object[]{Boolean.valueOf(str), Boolean.class});
                continue;
            }
            if (StringUtils.endsWith((CharSequence)str, (CharSequence)"L")) {
                classs.add(new Object[]{Long.valueOf(StringUtils.substring((String)str, (int)0, (int)(str.length() - 1))), Long.class});
                continue;
            }
            if (StringUtils.endsWith((CharSequence)str, (CharSequence)"D")) {
                classs.add(new Object[]{Double.valueOf(StringUtils.substring((String)str, (int)0, (int)(str.length() - 1))), Double.class});
                continue;
            }
            classs.add(new Object[]{Integer.valueOf(str), Integer.class});
        }
        return classs;
    }

    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class[] classs = new Class[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Class)os[1];
            ++index;
        }
        return classs;
    }

    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classs = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = os[0];
            ++index;
        }
        return classs;
    }
}
