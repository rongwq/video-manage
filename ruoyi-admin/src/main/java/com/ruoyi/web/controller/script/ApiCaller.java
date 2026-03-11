package com.ruoyi.web.controller.script;

import com.ruoyi.common.utils.http.HttpUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApiCaller {

    private static final String API_URL = "http://v.juhe.cn/toutiao/index"; // 替换为实际API地址
    private static final String OUTPUT_FILE = "d://test/output.txt"; // 输出文件路径

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            try {
                String response = HttpUtils.sendGet(API_URL);
                writeToFile(response, OUTPUT_FILE);
                System.out.println("API response written to file successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        // 初始延迟0秒，之后每隔5分钟执行一次
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
    }



    private static void writeToFile(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content);
            writer.newLine();
        }
    }
}
