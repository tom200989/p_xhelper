package com.xnet.xnet2.impl;

import com.xnet.xnet2.listener.XTransferListener;

import org.xutils.http.request.UriRequest;

/*
 * Created by qianli.ma on 2019/4/11 0011.
 */
public abstract class XTransferCallback implements XTransferListener {
    
    @Override
    public void getUriRequest(UriRequest uriRequest) {
        // 该方法重写是为了方便programmer对响应体参数进行监测, 外部可选择性实现
    }

    @Override
    public void successByXmlOther(String xml) {
        // 该方法重写是为了传递XML等其他格式的数据字符
    }
}
