package com.ruoyi.wvp.utils;

import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 系统信息采集（纯 JDK 实现，不依赖 OSHI / JNA）。
 * 说明：原实现基于 OSHI（依赖 JNA 5.x），与海康 ISUP SDK 所需的 JNA 3.0.9 冲突，
 * 导致 oshi.SystemInfo 静态初始化失败（NoClassDefFoundError）。
 * 现改为 JDK 自带的 OperatingSystemMXBean + java.io.File 实现，工程统一使用 JNA 3.0.9。
 * 原创参考：https://blog.csdn.net/xiaozhangnomoney/article/details/107769147
 */
@Slf4j
public class SystemInfoUtils {

    private static final OperatingSystemMXBean OS_BEAN =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    /**
     * 获取cpu使用率（0~1）
     */
    public static double getCpuInfo() throws InterruptedException {
        // 采样间隔，使使用率更接近实时
        TimeUnit.SECONDS.sleep(1);
        double load = OS_BEAN.getSystemCpuLoad();
        return load < 0 ? 0 : load;
    }

    /**
     * 获取内存使用率（0~1）
     */
    public static double getMemInfo() {
        long total = OS_BEAN.getTotalPhysicalMemorySize();
        long free = OS_BEAN.getFreePhysicalMemorySize();
        if (total <= 0) {
            return 0;
        }
        return (total - free) * 1.0 / total;
    }

    /**
     * 获取网络上行和下载速率（Mbps）。
     * 纯 JDK 在 Windows 下无法获取实时流量，降级返回 0；
     * Linux 下读取 /proc/net/dev 两次采样差值计算。
     */
    public static Map<String, Double> getNetworkInterfaces() {
        HashMap<String, Double> map = new HashMap<>();
        String os = System.getProperty("os.name", "");
        if (os.startsWith("Linux")) {
            try {
                long[] before = readProcNetDevBytes();
                Thread.sleep(1000);
                long[] after = readProcNetDevBytes();
                long rx = Math.max(0, after[0] - before[0]);
                long tx = Math.max(0, after[1] - before[1]);
                // 字节/秒 -> Mbps（*8 / 1024 / 1024）
                map.put("in", rx * 8.0 / 1048576.0);
                map.put("out", tx * 8.0 / 1048576.0);
                return map;
            } catch (Exception e) {
                log.warn("[读取 /proc/net/dev 失败] {}", e.getMessage());
            }
        }
        map.put("in", 0.0);
        map.put("out", 0.0);
        return map;
    }

    /**
     * 读取 /proc/net/dev 的接收/发送总字节数 [rx, tx]
     */
    private static long[] readProcNetDevBytes() throws Exception {
        long rx = 0, tx = 0;
        java.nio.file.Path path = java.nio.file.Paths.get("/proc/net/dev");
        for (String line : java.nio.file.Files.readAllLines(path)) {
            int colon = line.indexOf(':');
            if (colon < 0) {
                continue;
            }
            String[] parts = line.substring(colon + 1).trim().split("\\s+");
            if (parts.length >= 9) {
                rx += Long.parseLong(parts[0]);   // 接收字节
                tx += Long.parseLong(parts[8]);   // 发送字节
            }
        }
        return new long[]{rx, tx};
    }

    /**
     * 获取带宽总值（Mbps）。纯 JDK 难以跨平台获取网卡速率，降级返回 0
     */
    public static long getNetworkTotal() {
        return 0L;
    }

    /**
     * 获取进程数。Linux 下统计 /proc 下数字目录；其他平台返回 -1
     */
    public static int getProcessesCount() {
        String os = System.getProperty("os.name", "");
        if (os.startsWith("Linux")) {
            File proc = new File("/proc");
            File[] dirs = proc.listFiles(f -> f.isDirectory() && f.getName().matches("\\d+"));
            return dirs == null ? -1 : dirs.length;
        }
        return -1;
    }

    public static double formatUnits(long value, long prefix) {
        return (double) value / (double) prefix;
    }

    /**
     * 获取磁盘信息（单位 GB）
     */
    public static List<Map<String, Object>> getDiskInfo() {
        List<Map<String, Object>> result = new ArrayList<>();
        String osName = System.getProperty("os.name");
        List<String> pathArray = new ArrayList<>();
        if (osName.startsWith("Mac OS")) {
            pathArray.add("/");
        } else if (osName.startsWith("Windows")) {
            for (char c = 'A'; c <= 'Z'; c++) {
                String drive = c + ":\\";
                if (new File(drive).exists()) {
                    pathArray.add(drive);
                }
            }
        } else {
            pathArray.add("/");
            pathArray.add("/home");
        }
        for (String path : pathArray) {
            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("path", path);
            File partitionFile = new File(path);
            infoMap.put("use", (partitionFile.getTotalSpace() - partitionFile.getFreeSpace()) / 1024.0 / 1024.0 / 1024.0);
            infoMap.put("free", partitionFile.getFreeSpace() / 1024.0 / 1024.0 / 1024.0);
            result.add(infoMap);
        }
        return result;
    }
}
