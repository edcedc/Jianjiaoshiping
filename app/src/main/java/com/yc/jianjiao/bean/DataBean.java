package com.yc.jianjiao.bean;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yc on 2017/8/17.
 */

public class DataBean implements Serializable {

    private String name;
    private int img;
    private boolean isSelect = false;
    private int position;
    private double price;
    private String title;
    private String content;
    private String image;
    private String click_image;
    private int type;
    private String id;
    private String startTime;
    private boolean isSelected;
    private boolean allSelected = false;
    private String nick;
    private String head;
    private String brief;//个性签名
    private String label_name;
    private String cover_image;
    private double mark;//评分
    private String video_desc;
    private String remark;
    private int tread;//踩
    private int praise;//赞
    private int share;//分享
    private int comment_number;//评论费用
    private int state;
    private String subhead;//副标题
    private String emoji_content;
    private String create_time;
    private int isPraise;
    private String user_id;
    private String parent_id;
    private String video_url;
    private String circle_id;
    private String reply_user_id;
    private String message;
    private int isCirclePraise;
    private int circlePraise;
    private String reply_nick;
    private int isCollect;
    private String video_id;
    private String registration_protocol;
    private String cover;
    private String video_history_id;
    private String video_collect_id;
    private String url;
    private String autograph;//个性签名
    private int number;
    private String parent;
    private int play_time;
    private String ids;
    private String level_content;
    private String level_emoji_content;

    public String getClick_image() {
        return click_image;
    }

    public String getLevel_emoji_content() {
        return level_emoji_content;
    }

    public String getLevel_content() {
        return level_content;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public int getPlay_time() {
        return play_time;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getParent() {
        return parent;
    }

    public int getNumber() {
        return number;
    }

    public String getAutograph() {
        return autograph;
    }

    public String getUrl() {
        return url;
    }

    public String getVideo_collect_id() {
        return video_collect_id;
    }

    public String getVideo_history_id() {
        return video_history_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getRegistration_protocol() {
        return registration_protocol;
    }

    public String getVideo_id() {
        return video_id;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public String getReply_nick() {
        return reply_nick;
    }

    public void setReply_nick(String reply_nick) {
        this.reply_nick = reply_nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getIsCirclePraise() {
        return isCirclePraise;
    }

    public void setIsCirclePraise(int isCirclePraise) {
        this.isCirclePraise = isCirclePraise;
    }

    public void setCirclePraise(int circlePraise) {
        this.circlePraise = circlePraise;
    }

    public int getCirclePraise() {
        return circlePraise;
    }
    public String getMessage() {
        return message;
    }

    public String getReply_user_id() {
        return reply_user_id;
    }

    public void setReply_user_id(String reply_user_id) {
        this.reply_user_id = reply_user_id;
    }

    public String getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(String circle_id) {
        this.circle_id = circle_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getEmoji_content() {
        return emoji_content;
    }

    public void setEmoji_content(String emoji_content) {
        this.emoji_content = emoji_content;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public void setTread(int tread) {
        this.tread = tread;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getComment_number() {
        return comment_number;
    }

    public void setComment_number(int comment_number) {
        this.comment_number = comment_number;
    }

    public int getShare() {
        return share;
    }

    public int getPraise() {
        return praise;
    }

    public int getTread() {
        return tread;
    }

    public String getRemark() {
        return remark;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public double getMark() {
        return mark;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getLabel_name() {
        return label_name;
    }

    public String getBrief() {
        return brief;
    }

    public String getHead() {
        return head;
    }

    public String getNick() {
        return nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isAllSelected() {
        return allSelected;
    }

    public void setAllSelected(boolean allSelected) {
        this.allSelected = allSelected;
    }

    private List<DataBean> listSubclassification;

    public List<DataBean> getListSubclassification() {
        return listSubclassification;
    }

    public void setListSubclassification(List<DataBean> listSubclassification) {
        this.listSubclassification = listSubclassification;
    }

    private List<DataBean> listVideoSeries;

    public List<DataBean> getListVideoSeries() {
        return listVideoSeries;
    }

    private List<DataBean> listVideo;

    public List<DataBean> getListVideo() {
        return listVideo;
    }

    private List<DataBean> imageList;

    public List<DataBean> getImageList() {
        return imageList;
    }

    private List<DataBean> listStar;

    public List<DataBean> getListStar() {
        return listStar;
    }

    private List<DataBean> listStarVideo;

    public List<DataBean> getListStarVideo() {
        return listStarVideo;
    }

    private PageCommentBean pageComment;

    public PageCommentBean getPageComment() {
        return pageComment;
    }

    public void setPageComment(PageCommentBean pageComment) {
        this.pageComment = pageComment;
    }

    public static class PageCommentBean{

        private int totalRow;

        public int getTotalRow() {
            return totalRow;
        }

        public List<DataBean> list;

        public List<DataBean> getList() {
            return list;
        }

        public void setList(List<DataBean> list) {
            this.list = list;
        }
    }


    private CircleCommentBean circleComment;

    public CircleCommentBean getCircleComment() {
        return circleComment;
    }

    public void setCircleComment(CircleCommentBean circleComment) {
        this.circleComment = circleComment;
    }

    public static class CircleCommentBean{

        private int totalRow;

        public int getTotalRow() {
            return totalRow;
        }

        public List<DataBean> list;

        public List<DataBean> getList() {
            return list;
        }

        public void setList(List<DataBean> list) {
            this.list = list;
        }
    }

    private PageCommentsLowerLevelBean pageCommentsLowerLevel;

    public PageCommentsLowerLevelBean getPageCommentsLowerLevel() {
        return pageCommentsLowerLevel;
    }

    public static class PageCommentsLowerLevelBean {
        public List<DataBean> list;

        public List<DataBean> getList() {
            return list;
        }
    }


    public static class CirclePraiseBean{

        private int state;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}