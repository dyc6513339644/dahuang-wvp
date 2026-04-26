package com.ruoyi.rtsp.service.impl;

import com.ruoyi.rtsp.service.IRtspStreamPushService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: 陈江灿
 * @CreateTime: 2025-06-10
 */
@Service
public class RtspStreamPushServiceImpl implements IRtspStreamPushService {

    private final ConcurrentHashMap<String, Process> streamProcessMap = new ConcurrentHashMap<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public boolean startPush(String streamId, String rtspUrl, String pushUrl) {
        if (streamProcessMap.containsKey(streamId)) {
            stopPush(streamId);
        }
        String[] command = new String[]{
                "ffmpeg",
                "-rtsp_transport", "tcp",
                "-i", rtspUrl,
                "-map", "0:v",
                "-map", "0:a?",
                "-c:v", "copy",
                "-c:a", "aac",
                "-f", "flv",
                pushUrl
        };
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            executorService.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[ffmpeg-" + streamId + "] " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            streamProcessMap.put(streamId, process);
            executorService.submit(() -> {
                try {
                    int exitCode = process.waitFor();
                    System.out.println("ffmpeg process for streamId " + streamId + " exited with code " + exitCode);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    streamProcessMap.remove(streamId);
                }
            });
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean stopPush(String streamId) {
        return false;
    }

}
