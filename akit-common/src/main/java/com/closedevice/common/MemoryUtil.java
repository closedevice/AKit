package com.closedevice.common;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author liudongdong
 * @date 2018/1/12
 */

public class MemoryUtil {
    /**
     * KB
     */
    public final static long RAM_SIZE;
    public static final String PROC_MEMINFO = "/proc/meminfo";

    static {
        RAM_SIZE = getTotalMemory();
    }

    private static long getTotalMemory() {
        long memory = 0;
        try {
            FileReader reader = new FileReader(PROC_MEMINFO);
            BufferedReader bufferedReader = new BufferedReader(reader, 8192);
            String str2 = bufferedReader.readLine();
            String[] arr = str2.split("\\s+");
            memory = Long.valueOf(arr[1]);
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memory;
    }
}
