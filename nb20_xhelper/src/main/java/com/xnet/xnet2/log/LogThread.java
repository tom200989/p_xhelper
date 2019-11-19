package com.xnet.xnet2.log;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
 * Created by qianli.ma on 2019/11/19 0019.
 */
public class LogThread extends Thread {

    // 日志集合
    public static List<String> logList = new ArrayList<>();
    // 日志文件夹
    private String logDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/applications/log";
    // 循环控制标记
    private static boolean loopFlag = true;

    public LogThread() {
        createdLogDirOut();
    }

    /**
     * 停止循环(停止线程前使用)
     */
    public static void killLoop() {
        loopFlag = false;
    }

    /**
     * 添加新的内容到集合末尾
     *
     * @param content 新内容
     */
    public static void addContentToList(String content) {
        logList.add(content);
    }

    /**
     * 外部主动创建文件夹
     */
    public static void createdLogDirOut() {
        synchronized (Object.class) {
            try {
                /* 定义LOG文件的格式: sdcard/applications/log/159289121238798 */
                // 3.查询文件夹是否存在
                String appsPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/applications";
                File appFile = new File(appsPath);
                // 4.如果［applications］文件夹不存在 -- 创建
                if (!appFile.exists() || !appFile.isDirectory()) {
                    // 4.1.创建 applications 目录
                    appFile.mkdir();
                    // 4.2.再创建 log 目录
                    File logFile = new File(appFile, "log");
                    logFile.mkdir();
                } else {
                    // 4.如果［applications］文件夹存在
                    List<File> toDelFileList = new ArrayList<>();
                    File[] files = appFile.listFiles();
                    boolean isLogDirExists = false;// 检查Log文件夹是否存在
                    for (File tempF : files) {
                        // 4.1.并且找到了log文件夹
                        if (tempF.isDirectory() & tempF.getName().contains("log")) {
                            isLogDirExists = true;
                            // 4.2.遍历并删除7天前的文件
                            File[] logFiles = tempF.listFiles();
                            if (logFiles.length > 0) {
                                // 4.3.获取当前时间并计算出7天前的最值
                                long currentTime = System.currentTimeMillis();
                                long sevenTime = 7 * 24 * 3600 * 1000;
                                long minTime = currentTime - sevenTime;
                                // 4.4.取出文件名并比较, 如果小于最小值则删除这个文件夹
                                for (File logFile : logFiles) {
                                    long logFileName = Long.valueOf(logFile.getName());
                                    if (logFileName < minTime) {
                                        toDelFileList.add(logFile);
                                    }
                                }
                                // 4.5.遍历临时删除集合并删除对应文件
                                for (int i = 0; i < toDelFileList.size(); i++) {
                                    toDelFileList.get(i).delete();
                                }
                                toDelFileList.clear();
                                // System.out.println("ma-file: deleted file" );
                            }
                        }
                    }
                    // 判断最终是否没有LOG文件夹 -- 无:创建
                    if (!isLogDirExists) {
                        File logFile = new File(appFile, "log");
                        logFile.mkdir();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logg.i("xhelper", "create log dir error: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        super.run();
        while (loopFlag) {
            try {
                if (logList.size() > 0) {
                    // 把第一个元素写出去(追加形式)
                    String content = logList.get(0);
                    File needFile = getNeedFile();
                    if (needFile != null) {
                        FileOutputStream out = new FileOutputStream(needFile, true);
                        BufferedOutputStream bo = new BufferedOutputStream(out);
                        bo.write(content.getBytes(StandardCharsets.UTF_8));
                        bo.close();
                        out.close();
                        // 移除第一个元素
                        logList.remove(0);
                    }
                }
                sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取所需要写入的文件路径
     *
     * @return 所需要写入的文件路径
     */
    @SuppressLint("UseSparseArrays")
    private File getNeedFile() {
        try {
            File logDir = new File(logDirPath);
            File[] files = logDir.listFiles();
            if (files.length > 0) {
                HashMap<Long, File> logMap = new HashMap<>();
                List<Long> logNameList = new ArrayList<>();
                for (File file : files) {
                    // 1231545645613 -- file
                    logMap.put(Long.valueOf(file.getName()), file);
                    logNameList.add(Long.valueOf(file.getName()));
                }
                // 找出文件中最近的文件名
                Long max = Collections.max(logNameList);
                // 如果文件不是今天, 则重新创建, 否则直接使用当前最近的文件
                if (System.currentTimeMillis() - 24 * 3600 * 1000 >= max) {
                    File file = new File(logDir + "/" + System.currentTimeMillis());
                    if (!file.exists() || file.isDirectory()) {
                        file.createNewFile();
                    }
                    return file;
                } else {
                    File file = logMap.get(max);
                    if (!file.exists() || file.isDirectory()) {
                        file.createNewFile();
                    }
                    return file;
                }
            } else {
                // 如果log文件夹为空, 第一次创建, 则创建日期文件
                File file = new File(logDir + "/" + System.currentTimeMillis());
                if (!file.exists() || file.isDirectory()) {
                    file.createNewFile();
                }
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
