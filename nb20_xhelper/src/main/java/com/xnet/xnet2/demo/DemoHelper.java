package com.xnet.xnet2.demo;

import com.xnet.xnet2.core.ServerError;
import com.xnet.xnet2.core.Xhelper;
import com.xnet.xnet2.impl.XNormalCallback;
import com.xnet.xnet2.log.Logg;

import org.xutils.common.Callback;
import org.xutils.http.request.UriRequest;

/*
 * Created by qianli.ma on 2019/1/8 0008.
 */
public class DemoHelper {

    public void get(String userName, String password, String serurl) {
        Xhelper<Demobean> xhelper = new Xhelper<Demobean>().xUrl(serurl);
        DemoParam loginParam = new DemoParam(userName, password);
        xhelper.xParam(loginParam).xPost(new XNormalCallback<Demobean>() {
            
            @Override
            public void getUriRequest(UriRequest uriRequest) {
                super.getUriRequest(uriRequest);
                // 该方法重写是为了方便programmer对响应体参数进行监测, 外部可选择性实现
            }

            @Override
            public void successByValue(Demobean loginbean) {
                // 请求成功并且有json返回
                String uid = loginbean.getUid();
                Logg.t("xhelper").ii("Get Uid success: " + uid);
            }

            @Override
            public void successNoValue() {
                // 请求成功但没有实例json返回
            }

            @Override
            public void appError(Throwable ex) {
                // APP错误, 如错误的链接
            }

            @Override
            public void serverError(ServerError error) {
                // 服务器返回错误, 如密码错误
            }

            @Override
            public void cancel(Callback.CancelledException cex) {
                // 取消请求
            }

            @Override
            public void finish() {
                // 请求结束
            }

            @Override
            public void kickOff(ServerError serverError) {
                super.kickOff(serverError);
                // 被踢出时--> 可自由选择性重写该方法, 否则按照默认处理
            }

            @Override
            public void successByXmlOther(String xml) {
                
            }
        });
    }

    /* -------------------------------------------- impl -------------------------------------------- */
    private OnFinishListener onFinishListener;

    // Inteerface--> 接口OnFinishListener
    public interface OnFinishListener {
        void finish();
    }

    // 对外方式setOnFinishListener
    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    // 封装方法finishNext
    private void finishNext() {
        if (onFinishListener != null) {
            onFinishListener.finish();
        }
    }

    private OnCancelListener onCancelListener;

    // Inteerface--> 接口OnCancelListener
    public interface OnCancelListener {
        void cancel(Callback.CancelledException cex);
    }

    // 对外方式setOnCancelListener
    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    // 封装方法cancelNext
    private void cancelNext(Callback.CancelledException cex) {
        if (onCancelListener != null) {
            onCancelListener.cancel(cex);
        }
    }

    private OnServerErrorListener onServerErrorListener;

    // Inteerface--> 接口OnServerErrorListener
    public interface OnServerErrorListener {
        void serverError(ServerError error);
    }

    // 对外方式setOnServerErrorListener
    public void setOnServerErrorListener(OnServerErrorListener onServerErrorListener) {
        this.onServerErrorListener = onServerErrorListener;
    }

    // 封装方法serverErrorNext
    private void serverErrorNext(ServerError error) {
        if (onServerErrorListener != null) {
            onServerErrorListener.serverError(error);
        }
    }

    private OnAppErrorListener onAppErrorListener;

    // Inteerface--> 接口OnAppErrorListener
    public interface OnAppErrorListener {
        void appError(Throwable ex);
    }

    // 对外方式setOnAppErrorListener
    public void setOnAppErrorListener(OnAppErrorListener onAppErrorListener) {
        this.onAppErrorListener = onAppErrorListener;
    }

    // 封装方法appErrorNext
    private void appErrorNext(Throwable attr) {
        if (onAppErrorListener != null) {
            onAppErrorListener.appError(attr);
        }
    }

    private OnSuccessNoValueListener onSuccessNoValueListener;

    // Inteerface--> 接口OnSuccessNoValueListener
    public interface OnSuccessNoValueListener {
        void SuccessNoValue();
    }

    // 对外方式setOnSuccessNoValueListener
    public void setOnSuccessNoValueListener(OnSuccessNoValueListener onSuccessNoValueListener) {
        this.onSuccessNoValueListener = onSuccessNoValueListener;
    }

    // 封装方法SuccessNoValueNext
    private void SuccessNoValueNext() {
        if (onSuccessNoValueListener != null) {
            onSuccessNoValueListener.SuccessNoValue();
        }
    }

    private OnSuccessByValueListener onSuccessByValueListener;

    // Inteerface--> 接口OnSuccessByValueListener
    public interface OnSuccessByValueListener {
        void SuccessByValue(Demobean devicesBean);
    }

    // 对外方式setOnSuccessByValueListener
    public void setOnSuccessByValueListener(OnSuccessByValueListener onSuccessByValueListener) {
        this.onSuccessByValueListener = onSuccessByValueListener;
    }

    // 封装方法SuccessByValueNext
    private void SuccessByValueNext(Demobean attr) {
        if (onSuccessByValueListener != null) {
            onSuccessByValueListener.SuccessByValue(attr);
        }
    }

}
