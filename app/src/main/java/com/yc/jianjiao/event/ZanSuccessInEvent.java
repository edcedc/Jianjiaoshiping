package com.yc.jianjiao.event;

/**
 * Created by edison on 2019/3/21.
 */

public class ZanSuccessInEvent {

    public int position;
    public int isPraise;

    public ZanSuccessInEvent(int position, int isPraise) {
        this.position = position;
        this.isPraise = isPraise;
    }
}
