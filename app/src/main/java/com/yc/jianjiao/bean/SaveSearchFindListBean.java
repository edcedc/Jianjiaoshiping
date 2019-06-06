package com.yc.jianjiao.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Created by edison on 2018/11/19.
 */

public class SaveSearchFindListBean extends LitePalSupport {

    private String message;
    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
