package com.dhht.nana.network;

/**
 * @author Tyhj
 * @date 2019/7/1
 */

public class Result<T> {

    int ret;

    String msg;

    T data;


    public Result(int ret, String msg, T data) {
        this.ret = ret;
        this.msg = msg;
        this.data = data;
    }


    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
