package com.yc.jianjiao.base;

/**
 * Created by edison on 2018/5/7.
 */

public interface IBaseListView extends IBaseView {

    void setRefreshLayoutMode(int totalRow);

    void setData(Object data);

    void showLoadEmpty();

}
