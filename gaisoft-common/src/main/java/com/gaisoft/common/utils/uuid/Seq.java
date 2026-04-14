package com.gaisoft.common.utils.uuid;

import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.common.utils.StringUtils;
import java.util.concurrent.atomic.AtomicInteger;

public class Seq {
    public static final String commSeqType = "COMMON";
    public static final String uploadSeqType = "UPLOAD";
    private static AtomicInteger commSeq = new AtomicInteger(1);
    private static AtomicInteger uploadSeq = new AtomicInteger(1);
    private static final String machineCode = "A";

    public static String getId() {
        return Seq.getId(commSeqType);
    }

    public static String getId(String type) {
        AtomicInteger atomicInt = commSeq;
        if (uploadSeqType.equals(type)) {
            atomicInt = uploadSeq;
        }
        return Seq.getId(atomicInt, 3);
    }

    public static String getId(AtomicInteger atomicInt, int length) {
        String result = DateUtils.dateTimeNow();
        result = result + machineCode;
        result = result + Seq.getSeq(atomicInt, length);
        return result;
    }

    private static synchronized String getSeq(AtomicInteger atomicInt, int length) {
        int value = atomicInt.getAndIncrement();
        int maxSeq = (int)Math.pow(10.0, length);
        if (atomicInt.get() >= maxSeq) {
            atomicInt.set(1);
        }
        return StringUtils.padl(value, length);
    }
}
