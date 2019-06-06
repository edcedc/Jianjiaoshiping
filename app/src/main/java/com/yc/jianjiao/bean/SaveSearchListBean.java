package com.yc.jianjiao.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Created by edison on 2018/11/19.
 */

public class SaveSearchListBean extends LitePalSupport {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
