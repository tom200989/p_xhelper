package com.xnet.xnet2.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.util.Objects;

/*
 * Created by qianli.ma on 2020/4/27 0027.
 */
public class OtherUtils {

    /**
     * 获取适配android Q的SD卡读写路径
     *
     * @param context 域
     * @param dirName 需要创建的路径(如: /applications/log)
     * @return 适配android Q的SD卡读写路径
     */
    public static String get_android_Q_SD_Path(Context context, String dirName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath() + dirName;
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + dirName;
    }
}
