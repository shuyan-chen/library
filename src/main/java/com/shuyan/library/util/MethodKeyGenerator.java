package com.shuyan.library.util;

import java.util.Arrays;

public final class MethodKeyGenerator {
    public static String generateKey(String methodName, Object[] args) {
        return methodName + "-" + Arrays.deepHashCode(args);
    }

}
