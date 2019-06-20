package com.xnet.xnet2.bean;

import com.xnet.xnet2.core.ServerError;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/1/28 0028.
 */
public class KickOffBean implements Serializable {
    private long kickOffTime;// 被踢时间
    private ServerError error;// 错误信息

    public KickOffBean() {
    }

    public long getKickOffTime() {
        return kickOffTime;
    }

    public void setKickOffTime(long kickOffTime) {
        this.kickOffTime = kickOffTime;
    }

    public ServerError getError() {
        return error;
    }

    public void setError(ServerError error) {
        this.error = error;
    }
}
