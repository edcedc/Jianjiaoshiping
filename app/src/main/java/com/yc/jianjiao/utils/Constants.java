package com.yc.jianjiao.utils;

import android.os.Environment;

import com.yc.jianjiao.base.User;

/**
 * Created by yc on 2017/9/30.
 */

public class Constants {

   public static final int pageSize = 10;
   public static final int pageNumber = 1;

   public static final String ShareID = "5bc9f7e8f1f556233100068d";

   public static final String WX_APPID = "wx99dda0a17471bc88";
   public static final String WX_SECRER = "5e8b64483b9c6122922f1b6262b82a1a";
   public static final String QQ_APPID = "1107890734";
   public static final String QQ_KEY = "QF8dwXayGiU1ybmF";

   public static final String mainPath = Environment.getExternalStorageDirectory() + "/jianjiao/";
   public static final String imgUrl = mainPath + "img/";
   public static final String videoUrl = mainPath + "video/";

   public static final String ZFB_PAY = "2018102061789029";

   public static final String BIG_IMG = "大帅哥背景图";

   public static final int  VIDEO_TYPE = 1;
   public static final int CIRCLE_TYPE = 0;

}
