package com.xnet.xnet2.bean;

/*
 * Created by qianli.ma on 2019/11/19 0019.
 */
public class Logbean {

    private String url;// http
    private String requestMethod;// GET POST
    private String responceCode;// 200 302
    private String responceBody;// {..}
    private String requestParam;// {..}
    private String error = "0";// {..}
    private String cancel = "0";// {..}
    private String date = "";// 2019-08-09

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResponceBody() {
        return responceBody;
    }

    public void setResponceBody(String responceBody) {
        this.responceBody = responceBody;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getResponceCode() {
        return responceCode;
    }

    public void setResponceCode(String responceCode) {
        this.responceCode = responceCode;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }
}
