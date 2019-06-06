package com.yc.jianjiao.controller;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okrx2.adapter.ObservableBody;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.yc.jianjiao.base.User;
import com.yc.jianjiao.bean.BaseListBean;
import com.yc.jianjiao.bean.BaseResponseBean;
import com.yc.jianjiao.bean.DataBean;
import com.yc.jianjiao.callback.JsonConvert;
import com.yc.jianjiao.callback.NewsCallback;
import com.yc.jianjiao.utils.Constants;
import com.yc.jianjiao.utils.cache.ShareSessionIdCache;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：yc on 2018/6/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CloudApi {


    private static final String url =
//            "39.104.66.84";
            "103.253.13.162";
//            "finet.asuscomm.com:8082";

/*    public static final String SERVLET_IMG_URL = "http://" +
            url;*/

    public static final String SERVLET_URL = "http://" + url + "/video/api/";


    public static final String TEST_URL = ""; //测试

    private static final String TAG = "CloudApi";

    private CloudApi() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }





    /**
     * 通用list数据
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> list(int pageNumber, String url) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + url)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 下级评论
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> commentPageLevel(int pageNumber, int type, String id) {
        String url = null;
        if (type == 0){
            url = CloudApi.commentPageLevel;
        }else {
            url = CloudApi.commentPageAWCLevel;
        }
        PostRequest<BaseResponseBean<BaseListBean<DataBean>>> post = OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + url);
        if (type == 0){
            post.params("circleCommentId", id);
        }else {
            post.params("videoCommentId", id);
        }
        return post
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 消息列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> noticePage(int pageNumber, int type) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "notice/page")
                .params("type", type)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 消息列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> commentPageAWC(int pageNumber, String id) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "sv/video/comment/pageAWC")
                .params("videoId", id)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 动态详情列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> commentPage(int pageNumber, String circleId) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "circle/comment/page")
                .params("circleId", circleId)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 圈列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> circlePage(int pageNumber, String text) {
        PostRequest<BaseResponseBean<BaseListBean<DataBean>>> post = OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "circle/page");
        if (!StringUtils.isEmpty(text)){
            post.params("content", text);
        }
        return post
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 首页视频分类列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> videoPageLabelType(String id, int type) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "sv/video/pageLabelType")
                .params("pageNumber", Constants.pageNumber)
                .params("pageSize", 100)
                .params("labelId", id)
                .params("type", type)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 什么值得看
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>>videoPageSecondaryLabel(List<String> listId, int pageNumber) {
        PostRequest<BaseResponseBean<BaseListBean<DataBean>>> post = OkGo.post(SERVLET_URL + "sv/video/pageWhatWorthSeeing");
        StringBuilder sb = new StringBuilder();
        for (String id:listId){
            sb.append(id).append(",");
        }
        String s = sb.deleteCharAt(sb.length() - 1).toString();
        post.params("labelArr", s);
        post.params("count", listId.size());
        return post
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("type", 98)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 搜索视频/发现视频列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> videoPageSearch(int pageNumber, List<String> listId, String search) {
        PostRequest<BaseResponseBean<BaseListBean<DataBean>>> post = OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "sv/video/pageSearchMultiple");
        if (!StringUtils.isEmpty(search)){
            post.params("search", search);
        }
        if (listId != null && listId.size() != 0){
//            post.addUrlParams("labelId", listId);
            StringBuilder sb = new StringBuilder();
            for (String id:listId){
                sb.append(id).append(",");
            }
            String s = sb.deleteCharAt(sb.length() - 1).toString();
            post.params("labelArr", s);
            post.params("count", listId.size());
        }
        return post
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }
    /**
     * 弹幕列表
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> screenPage() {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "bullet/screen/page")
                .params("pageNumber", Constants.pageNumber)
                .params("pageSize", 1000)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  首页顶部分类列表
     */
    public static final String labelHome = "label/home";
    /**
     *  我的推广邀请列表
     */
    public static final String userInvitationPage = "user/invitation/page";
    /**
     *  意见箱 提交投诉/举报 标签列表
     */
    public static final String labelListProblem = "label/listProblem";
    /**
     *  历史记录列表
     */
    public static final String historyPage = "sv/video/history/page";
    /**
     *  我的收藏视频列表
     */
    public static final String pageCollect = "sv/video/pageCollect";
    /**
     *  热门推荐列表
     */
    public static final String starListHot = "star/listHot";
    /**
     *  专区热门列表
     */
    public static final String starPageSpecial = "star/pageSpecial";
    /**
     *  热门关键词列表
     */
    public static final String hotListHot = "hot/listHot";
    /**
     *  视频顶部标签列表
     */
    public static final String labelListVideo = "label/listVideo";
    /**
     *  首页什么值得看
     */
    public static final String classifyListHomeVideo = "sv/video/label/classify/listHomeVideo";
    /**
     *  下级评论列表
     */
    public static final String commentPageLevel = "circle/comment/pageLevel";
    /**
     *  下级评论列表
     */
    public static final String commentPageAWCLevel = "sv/video/comment/pageAWCLevel";

    /**
     * 通用list 2
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> list2(String url) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + url)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * D发现分类/视频首页分类/底部导航栏管理 列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> labelListLabel(String id) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "label/listLabel")
                .params("id", id)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 什么值得看标签列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> classifyListLabel(String id) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "sv/video/label/classify/listLabel")
                .params("videoLabelClassifyId", id)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 首页顶部分类列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> labelHome() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + labelHome)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 首页筛选推荐列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> labelListFiltrate(String id) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "label/listFiltrate")
                .params("id", id)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 热门关键词 列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> hotListHot(int type) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "hot/listHot")
                .params("type", type)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 剧集筛选分类列表列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> labelListScreen(String id) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "label/listScreen")
                .params("id", id)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 视频分类列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> labelListVideo(String id) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "label/listVideo")
                .params("id", id)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 火爆交流群列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> flockList(int type) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "flock/list")
                .params("type", type)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 视频广告轮播列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> svAdvList(String videoId) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "sv/adv/list")
                .params("videoId", videoId)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *首页剧集分类推荐列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> labelListHomeClassifyRecommend(String id) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "label/listHomeClassifyRecommend")
                .params("id", id)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *人气榜列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> starListPopular() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "star/listPopular")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *专区热门推荐列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> starGetSpecialHot() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "star/getSpecialHot")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *专区热门推荐列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> starPageSpecial() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "star/pageSpecial")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *首页剧集分类推荐列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> advList(int type) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "adv/adv/list")
                .params("type", type)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *大帅哥标签管理列表
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> labelListBigHandsomeGuyLabel() {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "label/listBigHandsomeGuyLabel")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  获取登陆验证码
     */
    public static final String userGetLoginCode = "user/getLoginCode";
    /**
     *  更换绑定手机号获取验证码
     */
    public static final String userGetBindingPhoneCode = "user/getBindingPhoneCode";
    /**
     * 通用获取验证码
     */
    public static Observable<Response<BaseResponseBean>> userGetRegisterCode(String url, String phone) {
        return OkGo.<BaseResponseBean>post(SERVLET_URL + url)
                .params("mobile", phone)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<BaseResponseBean>() {
                })
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 3.1.1登陆接口
     */
    public static Observable<JSONObject> userLogin(String mobile, String code, String shareCode) {
        return OkGo.<JSONObject>post(SERVLET_URL + "user/loginCode")
                .params("mobile", mobile)
                .params("code", code)
                .params("shareCode", shareCode)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 用户基本信息
     */
    public static Observable<JSONObject> userInformation() {
        return OkGo.<JSONObject>post(SERVLET_URL + "user/information")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 圈 用户添加赞/踩 ,并且 赞/踩 互换 /  取消 赞/踩
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> circleSavePraise(String circleId, int state) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "circle/savePraise")
                .params("circleId", circleId)
                .params("state", state)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 视频详细
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> videoDetail(String videoId) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/detail")
                .params("videoId", videoId)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 视频投诉
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> videoSaveComplaints(String videoId) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/saveComplaints")
                .params("videoId", videoId)
                .params("content", "黄色视频")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 视频收藏
     */
    public static Observable<JSONObject> videoSaveCollect(String videoId, int collect) {
        return OkGo.<JSONObject>post(SERVLET_URL + "sv/video/saveCollect")
                .params("videoId", videoId)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 圈子 用户添加赞/踩 ,并且 赞/踩 互换 /  取消 赞/踩
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> commentSavePraise(String circleId, int state) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "circle/comment/savePraise")
                .params("circleCommentId", circleId)
                .params("state", state)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 评论 用户添加赞/踩 ,并且 赞/踩 互换 /  取消 赞/踩
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> videoCommentSavePraise(String circleId, int state) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/comment/savePraise")
                .params("videoCommentId", circleId)
                .params("state", state)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 动态详情
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> circleDetail(String circleId) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "circle/detail")
                .params("circleId", circleId)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 视频发布评论
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> commentSaveAWC(String videoId, String content) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/comment/saveAWC")
                .params("videoId", videoId)
                .params("content", content)
                .params("emojiContent", EncodeUtils.base64Encode2String(content.getBytes()))
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 视频发布回复评论
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> commentSaveAWCLevel(
            String videoId, String content, String replyUserId, String byReplyUserId, String parentId, int commentType, String id
    ) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/comment/saveAWCLevel")
                .params("videoId", videoId)
                .params("content", content)
                .params("emojiContent", EncodeUtils.base64Encode2String(content.getBytes()))
                .params("replyUserId", User.getInstance().getUserId())
                .params("byReplyUserId", byReplyUserId)
                .params("videoCommentId", parentId)
                .params("type", commentType)
                .params("id", id)
                .params("level_emoji_content", EncodeUtils.base64Encode2String(content.getBytes()))
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 圈 发布评论
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> commentSave(String circleId, String content) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "circle/comment/save")
                .params("circleId", circleId)
                .params("content", content)
                .params("emojiContent", EncodeUtils.base64Encode2String(content.getBytes()))
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }



    /**
     * 圈 	发布回复评论
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> commentSaveLevel(String circleId, String content, String replyUserId, String byReplyUserId, String parentId, int commentType, String id) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "circle/comment/saveLevel")
                .params("circleId", circleId)
                .params("content", content)
                .params("emojiContent", EncodeUtils.base64Encode2String(content.getBytes()))
                .params("replyUserId", replyUserId)
                .params("byReplyUserId", byReplyUserId)
                .params("parentId", parentId)
                .params("id", id)
                .params("type", commentType)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 意见箱 提交投诉/举报
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> problemSave(String title, String content) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "problem/save")
                .params("title", title)
                .params("content", content)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 点击播放量
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> videoAddPlayback(String id) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/addPlayback")
                .params("videoId", id)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 弹幕评价 保存
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> screenSave(String content) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "bullet/screen/save")
                .params("content", content)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 明星详细
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> starDetail(String id) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "star/detail")
                .params("id", id)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }




    /**
     * 历史记录批量删除
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> historyDeleteBatches(List<String> listStr) {
        PostRequest<BaseResponseBean<DataBean>> post = OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/history/deleteBatches");
        post.addUrlParams("arrVideoHistory", listStr);
        return post
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 我的收藏视频批量删除
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> videoDeleteBatches(List<String> listStr) {
        PostRequest<BaseResponseBean<DataBean>> post = OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/deleteBatches");
        for (String id:listStr){
        }
        post.addUrlParams("videoCollectId", listStr);
        return post
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 更换绑定手机号
     */
    public static Observable<JSONObject> userChangePhone(String phone, String code) {
        return OkGo.<JSONObject>post(SERVLET_URL + "user/loginCode")
                .params("mobile", phone)
                .params("code", code)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }
    /**
     * APP基本详细接口
     */
    public static Observable<JSONObject> basicGet() {
        return OkGo.<JSONObject>post(SERVLET_URL + "app/basic/get")
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 个人基本资料
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userUpdate(String head, String nick) {
        PostRequest<BaseResponseBean<DataBean>> post = OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/update");
        if (!StringUtils.isEmpty(head)){
            post.params("head", new File(head));
        }
        if (!StringUtils.isEmpty(nick)){
            post.params("nick", nick);
        }
        return ((PostRequest<BaseResponseBean<DataBean>>) post)
                .params("sessionId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


}