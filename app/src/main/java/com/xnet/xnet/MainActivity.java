package com.xnet.xnet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.xnet.xnet2.core.Xhelper;
import com.xnet.xnet2.demo.LoginAction;

import java.io.InputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 测试登陆button
        findViewById(R.id.btLogin).setOnClickListener(v -> toLogin());
        // 显示raw文件
        ImageView imageView = findViewById(R.id.ivRaw);
        InputStream inputStream = getResources().openRawResource(R.raw.kraw);
        
    }

    private void toLogin() {
        // 初始化
        String USERNAME = "161058323@qq.com";
        String PASSWORD = "acz9bdw5";

        Xhelper.BASE_URL = "https://www.alcatel-move.com";
        String SECURL = "/v1.0/account/login";
        Xhelper.KEY = "vEWZapEpW5OezzEs5Su44xAbCiy9-arCJz7eoLJfjac2h1r4VF0";
        Xhelper.UID = "";
        Xhelper.ACCESS_TOKEN = "";
        Xhelper.LANGUAGE = "en";
        Xhelper.PRINT_TAG = true;
        // 调用登陆请求
        LoginAction loginAction = new LoginAction();
        loginAction.login(USERNAME, PASSWORD, SECURL);
    }
}
