package com.yc.basis.http;

public abstract class BaseHttpListener {

    public abstract void success(Object o);

    public abstract void error(String msg);

    public void value(String... s) {
    }

}
