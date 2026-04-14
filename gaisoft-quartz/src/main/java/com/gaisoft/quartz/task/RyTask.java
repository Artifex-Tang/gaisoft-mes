package com.gaisoft.quartz.task;

import com.gaisoft.common.utils.StringUtils;
import org.springframework.stereotype.Component;

@Component(value="ryTask")
public class RyTask {
    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format((String)"执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", (Object[])new Object[]{s, b, l, d, i}));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }
}
