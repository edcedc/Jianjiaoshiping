package com.yc.jianjiao.presenter;

import android.os.Handler;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.view.impl.CollectionContract;
import com.yc.jianjiao.view.impl.DownloadContract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/30.
 */

public class DownloadPresenter extends DownloadContract.Presenter{
    @Override
    public void onRequest(int pagerNumber) {
        mView.showLoadDataing();
        final List<DataBean> list = new ArrayList<>();
        final List<File> files = FileUtils.listFilesInDir(Constants.videoUrl);
        if (files != null && files.size() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (File file : files) {
                        DataBean bean = new DataBean();
                        String fileName = FileUtils.getFileName(file);
                        bean.setTitle(fileName.substring(0, fileName.length() - 4));
                        bean.setCover(file.toString());
                        bean.setBrief(FileUtils.getFileSize(file));
                        list.add(bean);
                        LogUtils.e(fileName, FileUtils.getFileSize(file), file.toString());
                    }
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setData(list);
                            mView.hideLoading();
                        }
                    });
                }
            }).start();
        }else {
            mView.showLoadEmpty();
        }
    }
}
