package com.xnet.xnet2.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/1/9 0009.
 */
public class Fidbean implements Serializable {
    private String fid;

    public Fidbean() {
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
