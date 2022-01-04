package com.yc.basis.entity;

import java.io.Serializable;

//用户
public class User implements Serializable {
    public String name, photo, id;
    //time过期时间 到秒
    public long time;
    public String timeText;
    public boolean isVip;
    public String vipId="";

}
