package com.xnet.xnet2.impl;

import com.xnet.xnet2.bean.KickOffBean;
import com.xnet.xnet2.core.ServerError;
import com.xnet.xnet2.listener.XNormalListener;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.request.UriRequest;

/*
 * Created by qianli.ma on 2019/1/25 0025.
 */
public abstract class XNormalCallback<T> implements XNormalListener<T> {

    @Override
    public void getUriRequest(UriRequest uriRequest) {
        // 该方法重写是为了方便programmer对响应体参数进行监测, 外部可选择性实现
    }

    @Override
    public void successByXmlOther(String xml) {
        // 该方法重写是为了传递XML等其他格式的数据字符
    }

    @Override
    public void kickOff(ServerError serverError) {
        KickOffBean kickBean = new KickOffBean();
        kickBean.setKickOffTime(System.currentTimeMillis());
        kickBean.setError(serverError);
        EventBus.getDefault().postSticky(kickBean);
    }
}
