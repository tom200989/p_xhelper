package com.xnet.xnet2.core;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcl.token.ndk.ServerEncrypt;
import com.xnet.xnet2.bean.Fidbean;
import com.xnet.xnet2.bean.Logbean;
import com.xnet.xnet2.impl.XNormalCallback;
import com.xnet.xnet2.impl.XTransferCallback;
import com.xnet.xnet2.listener.XBaseListener;
import com.xnet.xnet2.listener.XFotaListener;
import com.xnet.xnet2.log.Logg;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/*
 * Created by qianli.ma on 2019/1/7 0007.
 */
public class Xhelper<T> {

    /**
     * 基础地址 (首次需要设置)
     */
    public static String BASE_URL = "https://www.alcatel-move.com";

    /**
     * API KEY (首次需要设置)
     */
    public static String KEY = "";

    /**
     * TOKEN (首次需要设置)
     */
    public static String ACCESS_TOKEN = "";

    /**
     * 1个用户 = 1个UID (首次需要设置)
     */
    public static String UID = "";

    /**
     * 语言 (首次需要设置)
     */
    public static String LANGUAGE = "";

    /**
     * 加密后的请求
     */
    public static String AUTHORIZATION = "";

    /**
     * 超时时间 (30s)
     */
    private static final int TIMEOUT = 30000;

    /**
     * 是否打印框架日志 (按需求设置, T:打印)
     */
    public static boolean PRINT_TAG = false;

    /**
     * 是否打印头部信息 (T: 打印)
     */
    public static boolean PRINT_HEAD = false;

    /**
     * 是否打印传输进度 (T: 打印)
     */
    public static boolean PRINT_PROGRESS = false;

    /**
     * SSL工厂对象
     */
    public static SSLSocketFactory sslSocketFactory;

    /**
     * SSL-hostname对象
     */
    public static HostnameVerifier hostnameVerifier;

    /**
     * 日志标记
     */
    private static final String TAG = "xhelper";

    /**
     * 用于存储网络请求对象
     */
    private static final List<Callback.Cancelable> cancelList = new ArrayList<>();

    // 请求方式
    private static final int GET = 0;
    private static final int POST = 1;
    private static final int PUT = 2;
    private static final int PATCH = 3;
    private static final int HEAD = 4;
    private static final int DELETE = 7;
    private static final int OPTIONS = 8;
    private static final int TRACE = 9;
    private static final int CONNECT = 10;

    // 特殊错误ID处理
    private static final int KICK_OFF = 11;

    /**
     * 可变参数(请求提交所需)
     */
    @Nullable
    private Object object;

    /**
     * 返回数据是否使用XML模式解析
     */
    private boolean isXmlOther = false;

    /**
     * 请求使用自定义的全路径(自定义优先)
     */
    private String selfUrl;

    /**
     * 接口地址(请求提交所需)
     */
    private String secUrl;

    /**
     * 日志对象
     */
    private Logg lgg;

    // 请求实体 (用于批量取消请求时使用)
    private Callback.Cancelable requestCancelable;
    private Callback.Cancelable uploadCancelable;
    private Callback.Cancelable downCancelable;
    private Callback.Cancelable fotaCancelable;

    // 日志json临时缓存
    private String tempParamJson = "";

    /* -------------------------------------------- public -------------------------------------------- */

    public Xhelper() {
        lgg = Logg.t(TAG).openClose(PRINT_TAG);
    }

    public Xhelper<T> xUrl(String secUrl) {
        this.secUrl = secUrl;
        return this;
    }

    public Xhelper<T> xParam(@Nullable Object object) {
        this.object = object;
        return this;
    }

    public Xhelper<T> xXml(boolean isXml) {
        this.isXmlOther = isXml;
        return this;
    }

    public Xhelper<T> xSelfUrl(String selfUrl) {
        this.selfUrl = selfUrl;
        return this;
    }


    /**
     * 请求(get 方式)
     *
     * @param resp 响应
     */
    public void xGet(XNormalCallback<T> resp) {
        request(GET, resp);
    }

    /**
     * 请求(post 方式)
     *
     * @param resp 响应
     */
    public void xPost(XNormalCallback<T> resp) {
        request(POST, resp);
    }

    /**
     * 请求(put 方式)
     *
     * @param resp 响应
     */
    public void xPut(XNormalCallback<T> resp) {
        request(PUT, resp);
    }

    /**
     * 请求(delete 方式)
     *
     * @param resp 响应
     */
    public void xDelete(XNormalCallback<T> resp) {
        request(DELETE, resp);
    }

    /**
     * 请求(patch 方式)
     *
     * @param resp 响应
     */
    public void xPatch(XNormalCallback<T> resp) {
        request(PATCH, resp);
    }

    /**
     * 请求(head 方式)
     *
     * @param resp 响应
     */
    public void xHead(XNormalCallback<T> resp) {
        request(HEAD, resp);
    }

    /**
     * 请求(options 方式)
     *
     * @param resp 响应
     */
    public void xOptions(XNormalCallback<T> resp) {
        request(OPTIONS, resp);
    }

    /**
     * 请求(trace 方式)
     *
     * @param resp 响应
     */
    public void xTrace(XNormalCallback<T> resp) {
        request(TRACE, resp);
    }

    /**
     * 请求(connect 方式)
     *
     * @param resp 响应
     */
    public void xConnect(XNormalCallback<T> resp) {
        request(CONNECT, resp);
    }

    /**
     * 取消全部的网络请求
     */
    public static void xCancelAllRequest() {
        for (Callback.Cancelable cancelable : cancelList) {
            if (cancelable != null) {
                cancelable.cancel();
            }
        }
    }

    /* -------------------------------------------- private -------------------------------------------- */

    /**
     * 普通请求
     *
     * @param type     请求方式
     * @param listener 响应回调
     */
    private void request(int type, final XNormalCallback<T> listener) {
        // 判断当前请求的接口是否为登陆接口(首次使用) -- 是: 创建日志文件夹
        if (this.secUrl != null) {
            if (this.secUrl.contains("account/login")) {
                Logg.createdLogDir();
            }
        }
        // 创建日志对象
        Logbean logbean = new Logbean();
        logbean.setUrl(secUrl);

        // TOAT: 2019/11/19 0019  测试: 是否能写入并追加
        // Logg.writeToSD("test write log..." + System.currentTimeMillis() + "..." + secUrl + "\n");

        printNormal("prepare to request params");
        printHead();
        // 1.请求参数
        RequestParams params = getNormalRequestParams(type);
        // 2.区分类型(默认POST)
        HttpMethod httpMethod = HttpMethod.POST;
        httpMethod = getHttpMethod(type, httpMethod);
        // 3.发起请求
        if (TextUtils.isEmpty(selfUrl)) {
            printNormal("The Http method is " + printHttpType(type) + "; Start to request url is : [" + BASE_URL + secUrl + "]");
        } else {
            printNormal("The Http method is " + printHttpType(type) + "; Start to request url is : [" + selfUrl + "]");
        }

        /*
         * 如果请求的结果没有具体的实体参数, 则服务器返回 {"error_id": 0, "error_msg":"ok"}
         * 如果请求的结果带有具体的实体参数, 则服务器返回对应的实参json {"aaa": 0, "bbb": "demo"}
         * 因此只有服务器返回具体实体参数时, 才需要进行json转换
         * */
        HttpMethod finalHttpMethod = httpMethod;
        requestCancelable = x.http().request(httpMethod, params, new Callback.CommonCallback<String>() {

            @Override
            public void responseBody(UriRequest uriRequest) {
                // 打印
                printUriRequest(uriRequest);
                listener.getUriRequest(uriRequest);
            }

            @Override
            public void onSuccess(String result) {
                // 封装日志对象
                logbean.setResponceCode(String.valueOf("200"));// code
                logbean.setResponceBody(result);// body
                printResponseSuccess(result);

                if (isXmlOther) {// 是否为XML等其他格式
                    listener.successByXmlOther(result);
                    printNormal("[Request] & [xml or other reback] Successful");
                } else {
                    /*
                     * 如果请求的结果没有具体的实体参数, 则服务器返回 {"error_id": 0, "error_msg":"ok"}
                     * 如果请求的结果带有具体的实体参数, 则服务器返回对应的实参json {"aaa": 0, "bbb": "demo"}
                     * 因此只有服务器返回具体实体参数时, 才需要进行json转换
                     * */
                    if (!result.contains("error_id")) {
                        T t = toBean(result, listener);
                        listener.successByValue(t);
                        printNormal("[Request] & [json reback] & [turn bean] Successful");
                    } else {
                        listener.successNoValue();
                        printNormal("Request Successful but not value reback");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                logbean.setResponceCode(String.valueOf("404"));// code
                logbean.setError(ex.getMessage());// error
                handleError(ex, listener);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                logbean.setResponceCode(String.valueOf("403"));// code
                logbean.setCancel(cex.getMessage());// cancel
                listener.cancel(cex);
                printCancel(cex);
            }

            @Override
            public void onFinished() {
                cancelList.remove(requestCancelable);
                listener.finish();
                printFinish();
                // 把日志写出去(#ma#{..})
                // String content = "#ma#" + JSONObject.toJSONString(logbean);
                logbean.setRequestMethod(finalHttpMethod.toString());// method
                logbean.setRequestParam(params.toJSONString());// param
                Logg.writeToSD(mergeLog(logbean));
            }
        });
        cancelList.add(requestCancelable);
    }

    /**
     * 拼接成python能解析的字符串
     *
     * @param logbean 日志对象
     * @return 字符
     */
    private String mergeLog(Logbean logbean) {
        StringBuffer buffer = new StringBuffer();
        // {'url':'xxxxxx',.....}
        buffer.append("#ma#{");
        buffer.append("\'").append("date").append("\'").append(":").append("\'").append(System.currentTimeMillis() / 1000).append("\'").append(",");
        buffer.append("\'").append("url").append("\'").append(":").append("\'").append(logbean.getUrl()).append("\'").append(",");
        buffer.append("\'").append("requestMethod").append("\'").append(":").append("\'").append(logbean.getRequestMethod()).append("\'").append(",");
        buffer.append("\'").append("responceCode").append("\'").append(":").append("\'").append(logbean.getResponceCode()).append("\'").append(",");
        buffer.append("\'").append("responceBody").append("\'").append(":").append("\'").append(logbean.getResponceBody()).append("\'").append(",");
        buffer.append("\'").append("requestParam").append("\'").append(":").append("\'").append(tempParamJson).append("\'").append(",");
        buffer.append("\'").append("error").append("\'").append(":").append("\'").append(logbean.getError()).append("\'").append(",");
        buffer.append("\'").append("cancel").append("\'").append(":").append("\'").append(logbean.getCancel()).append("\'");
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * 切换请求方式类型
     *
     * @param type       类型代号
     * @param httpMethod 请求方式
     * @return 请求方式对象
     */
    private HttpMethod getHttpMethod(int type, HttpMethod httpMethod) {
        switch (type) {
            case GET:
                httpMethod = HttpMethod.GET;
                break;
            case POST:
                httpMethod = HttpMethod.POST;
                break;
            case PUT:
                httpMethod = HttpMethod.PUT;
                break;
            case DELETE:
                httpMethod = HttpMethod.DELETE;
                break;
            case PATCH:
                httpMethod = HttpMethod.PATCH;
                break;
            case HEAD:
                httpMethod = HttpMethod.HEAD;
                break;
            case OPTIONS:
                httpMethod = HttpMethod.OPTIONS;
                break;
            case TRACE:
                httpMethod = HttpMethod.TRACE;
                break;
            case CONNECT:
                httpMethod = HttpMethod.CONNECT;
                break;
        }
        return httpMethod;
    }

    /**
     * FOTA升级
     *
     * @param url fota服务器地址
     */
    public void fotaUpgrade(String url, XFotaListener listener) {
        // 创建日志对象
        Logbean logbean = new Logbean();
        logbean.setUrl(url);

        printNormal("prepare to request params");
        printHead();
        RequestParams fotaParams = getFotaParams(url);
        printNormal("The Http method is " + printHttpType(GET) + "; Start to request url is : [" + url + "]");
        fotaCancelable = x.http().get(fotaParams, new Callback.CommonCallback<String>() {
            @Override
            public void responseBody(UriRequest uriRequest) {
                printUriRequest(uriRequest);
                listener.getUriRequest(uriRequest);
            }

            @Override
            public void onSuccess(String result) {
                // 封装日志对象
                logbean.setResponceBody(result);// body
                logbean.setResponceCode(String.valueOf("200"));// code

                printResponseSuccess(result);
                listener.trippleSuccess(result);
                printNormal(result);
            }

            @Override
            public void onError(Throwable ex, boolean b) {
                logbean.setResponceCode(String.valueOf("404"));// code
                logbean.setError(ex.getMessage());// error
                listener.appError(ex);
                printAppError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                logbean.setResponceCode(String.valueOf("403"));// code
                logbean.setCancel(cex.getMessage());// cancel
                listener.cancel(cex);
                printCancel(cex);
            }

            @Override
            public void onFinished() {
                cancelList.remove(fotaCancelable);
                listener.finish();
                printFinish();
                logbean.setRequestMethod("GET");// method
                logbean.setRequestParam(fotaParams.toJSONString());// param
                Logg.writeToSD(mergeLog(logbean));
            }
        });
        cancelList.add(fotaCancelable);
    }


    /**
     * 上传图片
     *
     * @param url      上传链接
     * @param file     图片文件
     * @param listener 回调监听
     */
    public void uploadImage(String url, File file, XTransferCallback listener) {
        // 创建日志对象
        Logbean logbean = new Logbean();
        logbean.setUrl(url);

        printNormal("prepare to request params");
        printHead();
        RequestParams imageParams = getImageParams(url, file);// 准备参数 (如"/v1.0/fs?type=image&duration=0")
        printNormal("The Http method is " + printHttpType(POST) + "; Start to request url is : [" + BASE_URL + url + "]");
        // 开始请求
        uploadCancelable = x.http().post(imageParams, new Callback.ProgressCallback<String>() {// 开始请求

            @Override
            public void responseBody(UriRequest uriRequest) {
                printUriRequest(uriRequest);
                listener.getUriRequest(uriRequest);
            }

            @Override
            public void onWaiting() {
                listener.waiting();
                printNormal("--> wait to upload");
            }

            @Override
            public void onStarted() {
                listener.start();
                printNormal("--> start to upload");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                listener.loading(total, current, isDownloading);
                if (PRINT_PROGRESS) {
                    printNormal(" progress--> total: " + total + ";current: " + current + ";isDownloading: " + isDownloading);
                }
            }

            @Override
            public void onSuccess(String result) {
                logbean.setResponceBody(result);// body
                logbean.setResponceCode(String.valueOf("200"));// code
                if (result.contains("fid")) {
                    String fid = JSON.parseObject(result, Fidbean.class).getFid();
                    listener.success(fid);
                    printNormal("upload successfule, the [Fid] is : " + fid);
                } else {
                    printNormal("upload successfule, the [Fid] is : Null");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                logbean.setResponceCode(String.valueOf("404"));// code
                logbean.setError(ex.getMessage());// error
                handleError(ex, listener);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                logbean.setResponceCode(String.valueOf("403"));// code
                listener.cancel(cex);
                printCancel(cex);
            }

            @Override
            public void onFinished() {
                cancelList.remove(uploadCancelable);
                listener.finish();
                printFinish();
                logbean.setRequestMethod("POST");// method
                logbean.setRequestParam(imageParams.toJSONString());// param
                Logg.writeToSD(mergeLog(logbean));
            }
        });
        cancelList.add(uploadCancelable);
    }

    /**
     * 下载文件
     *
     * @param url      下载链接
     * @param fid      需要下载的文件的ID
     * @param savePath 下载文件需要保存的本地路径
     * @param listener 下载监听
     */
    public void downFile(String url, String fid, String savePath, XTransferCallback listener) {
        // 创建日志对象
        Logbean logbean = new Logbean();
        logbean.setUrl(url);

        printNormal("prepare to request params");
        printHead();
        RequestParams downParam = getDownParam(url, fid, savePath);// 准备参数(如"/v1.0/fs/")
        printNormal("The Http method is " + printHttpType(GET) + "; Start to request url is : [" + BASE_URL + url + fid + "]");
        downCancelable = x.http().get(downParam, new Callback.ProgressCallback<File>() {

            @Override
            public void responseBody(UriRequest uriRequest) {
                printUriRequest(uriRequest);
                listener.getUriRequest(uriRequest);
            }

            @Override
            public void onWaiting() {
                listener.waiting();
                printNormal("--> wait to download");
            }

            @Override
            public void onStarted() {
                listener.start();
                printNormal("--> start to download");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                listener.loading(total, current, isDownloading);
                if (PRINT_PROGRESS) {
                    printNormal(" progress--> total: " + total + ";current: " + current + ";isDownloading: " + isDownloading);
                }
            }

            @Override
            public void onSuccess(File file) {
                logbean.setResponceBody(file.getName());// body
                logbean.setResponceCode(String.valueOf("200"));// code
                listener.success(file);
                printNormal("download success, the [file path] is : " + file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                logbean.setResponceCode(String.valueOf("404"));// code
                logbean.setError(ex.getMessage());// error
                handleError(ex, listener);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                logbean.setResponceCode(String.valueOf("403"));// code
                logbean.setCancel(cex.getMessage());// cancel
                listener.cancel(cex);
                printCancel(cex);
            }

            @Override
            public void onFinished() {
                cancelList.remove(downCancelable);
                listener.finish();
                printFinish();
                logbean.setRequestMethod("GET");// method
                logbean.setRequestParam(downParam.toJSONString());// param
                Logg.writeToSD(mergeLog(logbean));
            }
        });
        cancelList.add(downCancelable);
    }

    /**
     * 普通请求参数
     *
     * @return 请求参数
     */
    private RequestParams getNormalRequestParams(int type) {
        String uri = TextUtils.isEmpty(selfUrl) ? BASE_URL + secUrl : selfUrl;
        RequestParams requestParams = new RequestParams(uri);
        // 设置SSL
        requestParams = setSSL(requestParams);
        // 基本信息
        requestParams.setConnectTimeout(TIMEOUT);
        // 头部信息
        requestParams.addHeader("Authorization", getAuthorization());
        requestParams.addHeader("Content-Type", "application/json");
        requestParams.addHeader("Accept-Language", LANGUAGE);
        requestParams.addHeader("User-Agent", android.os.Build.MANUFACTURER + "-" + android.os.Build.MODEL);

        // 参数
        if (object != null) {
            if (!(object instanceof HashMap)) {// 因为涉及到fota升级的格式 -- 此处需要区分 -- 如果是hashmap则采用表单形式
                requestParams.setAsJsonContent(true);
                String contentJson = JSON.toJSONString(object);
                requestParams.setBodyContent(contentJson);
                tempParamJson = contentJson;// 此处为了日志打印, 写出本地时需要用到 tempParamJson
                if (PRINT_HEAD) {
                    printRequestJson(contentJson);
                }
            } else {
                HashMap<String, String> order = (HashMap<String, String>) object;
                StringBuilder builder = new StringBuilder();
                for (String key : order.keySet()) {
                    String value = order.get(key);
                    requestParams.addBodyParameter(key, value);
                    builder.append(key).append(" = ").append(value);
                }
                tempParamJson = builder.toString();// 此处为了日志打印, 写出本地时需要用到 tempParamJson
                printNormal(builder.toString());
            }
        }
        return requestParams;
    }

    /**
     * 获取FOTA升级参数
     *
     * @param url 升级连接
     * @return 升级参数
     */
    private RequestParams getFotaParams(String url) {
        this.secUrl = url;// 此处只为了日志打印, 不参与具体的请求
        RequestParams requestParams = new RequestParams(url);
        // 设置SSL
        requestParams = setSSL(requestParams);
        // 基本信息
        requestParams.setConnectTimeout(TIMEOUT);
        // 头部信息
        requestParams.addHeader("Authorization", getAuthorization());
        requestParams.addHeader("Content-Type", "application/json");
        requestParams.addHeader("Accept-Language", LANGUAGE);
        requestParams.addHeader("User-Agent", android.os.Build.MANUFACTURER + "-" + android.os.Build.MODEL);
        // 参数
        if (object != null) {
            requestParams.setAsJsonContent(true);
            String contentJson = JSON.toJSONString(object);
            requestParams.setBodyContent(contentJson);
            tempParamJson = contentJson;// 此处为了日志打印, 写出本地时需要用到 tempParamJson
            if (PRINT_HEAD) {
                printRequestJson(contentJson);
            }

        }
        return requestParams;
    }

    /**
     * 获取上传图片的参数
     *
     * @param file 待上传文件
     * @return 参数对象
     */
    private RequestParams getImageParams(String url, File file) {
        this.secUrl = url;// 此处只为了日志打印, 不参与具体的请求
        // 准备参数
        RequestParams requestParams = new RequestParams(BASE_URL + url);
        // 设置SSL
        requestParams = setSSL(requestParams);
        // 基本信息
        requestParams.setConnectTimeout(TIMEOUT);
        // 头部信息
        requestParams.addHeader("Authorization", getAuthorization());
        requestParams.addHeader("Accept-Language", LANGUAGE);
        requestParams.addHeader("User-Agent", android.os.Build.MANUFACTURER + "-" + android.os.Build.MODEL);
        // 参数
        requestParams.addBodyParameter("file", file);
        tempParamJson = "upload param is file:" + file.getName();// 此处为了日志打印, 写出本地时需要用到 tempParamJson
        requestParams.setMultipart(true);/* 这一句必须要加才能上传成功 */
        return requestParams;
    }

    /**
     * 下载参数
     *
     * @param fid      文件ID
     * @param savePath 下载保存的路径
     * @return 参数对象
     */
    private RequestParams getDownParam(String url, String fid, String savePath) {
        this.secUrl = url;// 此处只为了日志打印, 不参与具体的请求
        // 准备参数 (selfUrl: 全路径优先, 如果开发人员设置了的话)
        String uri = TextUtils.isEmpty(selfUrl) ? BASE_URL + url + fid : selfUrl;
        RequestParams requestParams = new RequestParams(uri);
        // 设置SSL
        requestParams = setSSL(requestParams);
        // 基本信息
        requestParams.setConnectTimeout(TIMEOUT);
        // 头部信息
        requestParams.addHeader("Authorization", getAuthorization());
        requestParams.addHeader("Accept-Language", LANGUAGE);
        requestParams.addHeader("User-Agent", android.os.Build.MANUFACTURER + "-" + android.os.Build.MODEL);
        // 保存路径
        requestParams.setSaveFilePath(savePath);
        tempParamJson = "download param savepath is :" + savePath;// 此处为了日志打印, 写出本地时需要用到 tempParamJson
        return requestParams;
    }

    /**
     * 设置SSL
     *
     * @param requestParams 原参数
     * @return 带SSL的参数
     */
    private RequestParams setSSL(RequestParams requestParams) {
        if (sslSocketFactory != null) {
            requestParams.setSslSocketFactory(sslSocketFactory);
        }
        if (hostnameVerifier != null) {
            requestParams.setHostnameVerifier(hostnameVerifier);
        }
        return requestParams;
    }

    /**
     * 获取加密的认证信息
     */
    private String getAuthorization() {
        ServerEncrypt encrypt = new ServerEncrypt(UID);
        String key = "key=" + KEY + ";";
        String token = "token=" + ACCESS_TOKEN + ";";
        String sign = "sign=" + encrypt.getSign() + ";";
        String timeStamp = "timestamp=" + encrypt.getTimestamp() + ";";
        String newToken = "newtoken=" + encrypt.getNewtoken() + ";";
        AUTHORIZATION = key + token + sign + timeStamp + newToken;
        return AUTHORIZATION;
    }

    /**
     * 转换数据为bean
     *
     * @param result 返回字符
     */
    private T toBean(String result, XNormalCallback listenenr) {
        /* 注意: XNormalCallback是抽象类, 实现了XNormalListener接口, 所以要使用getGenericSuperclass的方式 */
        Type genericInterfaces = listenenr.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genericInterfaces).getActualTypeArguments();
        return JSONObject.parseObject(result, params[0]);
    }

    private void handleError(Throwable ex, XBaseListener listener) {
        if (ex instanceof HttpException) {// 此处要过滤, 非服务器返回的message强转成HttpException会出错
            String errJson = ((HttpException) ex).getResult();
            if (errJson.contains("error_id")) {
                ServerError error = JSON.parseObject(errJson, ServerError.class);
                if (error.getError_id() == KICK_OFF) {// 帐号被踢时
                    // 切断所有正在请求的连接
                    xCancelAllRequest();
                    // 回到踢出接口
                    listener.kickOff(error);
                } else {// 其他普通情况
                    listener.serverError(error);
                }
                printServerError(error.getError_id(), error.getError_field(), error.getError_msg());
            } else {
                listener.appError(ex);
                printAppError(ex);
            }
        } else {
            listener.appError(ex);
            printAppError(ex);
        }
    }

    /* -------------------------------------------- log -------------------------------------------- */

    /**
     * 打印Urirequest的hashcode
     *
     * @param uriRequest Urirequest
     */
    private void printUriRequest(UriRequest uriRequest) {
        lgg.ii(uriRequest.toString());
    }

    /**
     * 打印头部请求参数
     */
    private void printHead() {
        if (PRINT_HEAD) {
            lgg.ii("Request body: \n"// 请求头信息
                           + "{\n\turl: " + (TextUtils.isEmpty(selfUrl) ? BASE_URL + secUrl : selfUrl) // url
                           + "\n\tkey: " + KEY // key
                           + "\n\taccess_token: " + ACCESS_TOKEN // access token
                           + "\n\tuid: " + UID // uid
                           + "\n\tlanguage: " + LANGUAGE // language
                           + "\n\ttimeout: " + TIMEOUT // timeout
                           + "\n\tauthorization: " + AUTHORIZATION // authorization
            );
        }
    }

    /**
     * 普通打印
     *
     * @param content 备注
     */
    private void printNormal(String content) {
        lgg.ii("\n" + "-------- " + "以下是打印的内容" + " --------" + "\n" + content);
    }

    /**
     * 服务器返回成功(未进行转换)
     *
     * @param json 原文
     */
    private void printResponseSuccess(String json) {
        lgg.ii("--> url: [" + (TextUtils.isEmpty(selfUrl) ? secUrl : selfUrl) + "] request Successful");
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + "------ response json ------" + "\n");
        json = json.replace(",", "," + "\n");
        builder.append(json);
        lgg.ii(builder.toString());
    }

    /**
     * 打印请求json
     *
     * @param json 原文
     */
    private void printRequestJson(String json) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + "------ request json ------" + "\n");
        json = json.replace(",", "," + "\n");
        builder.append(json);
        lgg.ii(builder.toString());
    }

    /**
     * 服务器返回失败
     *
     * @param err_id    error_id
     * @param err_field error_field
     * @param err_msg   error_msg
     */
    private void printServerError(int err_id, String err_field, String err_msg) {
        lgg.ee("<-- url: [" + (TextUtils.isEmpty(selfUrl) ? secUrl : selfUrl) + "] request server Error");
        lgg.ee("<-- { error_id = " + err_id + "; error_field = " + err_field + "; error_msg = " + err_msg + "; }");
    }

    /**
     * APP请求出错
     *
     * @param ex 错误对象
     */
    private void printAppError(Throwable ex) {
        lgg.ee("<-- url: [" + (TextUtils.isEmpty(selfUrl) ? secUrl : selfUrl) + "] request Error");
        lgg.ee("<-- { error_message = " + ex.getMessage() + " }");
    }

    /**
     * APP请求取消
     *
     * @param cex 错误对象
     */
    private void printCancel(Callback.CancelledException cex) {
        lgg.ww("<-- url: [" + (TextUtils.isEmpty(selfUrl) ? secUrl : selfUrl) + "] request app Cancel");
        lgg.ww("<-- { cancel_message = " + cex.getMessage() + " }");
    }

    /**
     * 请求结束
     */
    private void printFinish() {
        lgg.vv("<-- url: [" + secUrl + "] request Finish");
    }

    /**
     * 请求类型描述
     *
     * @param type 类型
     * @return 描述
     */
    private String printHttpType(int type) {
        String httpName = "UNKNOWN";
        httpName = type == 0 ? "GET" : httpName;
        httpName = type == 1 ? "POST" : httpName;
        httpName = type == 2 ? "PUT" : httpName;
        httpName = type == 3 ? "PATCH" : httpName;
        httpName = type == 4 ? "HEAD" : httpName;
        httpName = type == 7 ? "DELETE" : httpName;
        httpName = type == 8 ? "OPTIONS" : httpName;
        httpName = type == 9 ? "TRACE" : httpName;
        httpName = type == 10 ? "CONNECT" : httpName;
        return httpName;
    }
}
