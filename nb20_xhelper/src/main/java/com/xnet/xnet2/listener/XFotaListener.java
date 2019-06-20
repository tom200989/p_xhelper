package com.xnet.xnet2.listener;

/*
 * Created by qianli.ma on 2019/4/11 0011.
 */
public interface XFotaListener extends XBaseListener {

    /**
     * FOTA升级触发成功
     *
     * @param result 触发结果
     */
    void trippleSuccess(String result);
}
