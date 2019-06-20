package com.xnet.xnet2.listener;

import com.xnet.xnet2.core.ServerError;

import org.xutils.common.Callback;
import org.xutils.http.request.UriRequest;

/*
 * Created by qianli.ma on 2019/1/11 0011.
 */
public interface XBaseListener {

    /**
     * 获取响应请求时的其他实体内容(如头部信息, 请求体, 响应体等)
     *
     * @param uriRequest 其他实体内容
     */
    void getUriRequest(UriRequest uriRequest);

    /**
     * 客户端错误
     */
    void appError(Throwable ex);

    /**
     * 服务器错误
     */
    void serverError(ServerError error);

    /**
     * 请求取消
     */
    void cancel(Callback.CancelledException cex);

    /**
     * 账户被踢
     */
    void kickOff(ServerError error);

    /**
     * 请求完成(无论成功与否)
     */
    void finish();

    /**
     * 请求成功(服务器返回XML等其他格式的字符)
     *
     * @param xml XML等其他格式的字符
     */
    void successByXmlOther(String xml);
}
