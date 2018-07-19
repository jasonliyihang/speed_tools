package com.liyihang.jason;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class CXConfig  {

    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String DEFAULT_CACHE_DIR_PATH = SDCARD_DIR + "/PLDroidPlayer";

    public static final boolean is_debug =true;

    public static final String test_host_url ="http://192.168.0.200:8380/";

    public static final String release_host_url ="http://192.168.0.200:8380/";

    public static final int login_delay_time_go_home=1400;

    public static final int refresh_header_min_time=740;

    public static final int load_more_min_time=470;

    public static final int delay_go_run_time=147;

    public static final String version_app ="1.0";

    public static final String post ="post";

    public static final String app_router_pre="cx://chenxing.net/app/";

    public static String network_err_msg;

    public static String devices_id =null;

    public static final String history_search_key_array="history_search_key_array";

    public static final String user_history_list_data="user_history_list_data";

    public static final String wifi_network_call_me="wifi_network_call_me";

    public static final String new_msg_network_call_me="new_msg_network_call_me";

    public static final String zt_url_host_path="http://192.168.0.20:8080/";


    //static data-----------------------------------------------------------------------------------------------



    public static String getHostUrl(){
        return is_debug ? test_host_url : release_host_url;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static void initConfig(Context context) {
        // devices id
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(manager.getDeviceId() == null || manager.getDeviceId().equals("")) {
            if (Build.VERSION.SDK_INT >= 23) {
                devices_id = manager.getDeviceId(0);
            }
        }else{
            devices_id = manager.getDeviceId();
        }
        if (devices_id ==null)
        {
            devices_id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

    }

    public static final String url_id_login ="user_login";
    public static final String url_id_checking="msg_login_checking";
    public static final String url_id_reg="url_id_reg";
    public static final String url_id_category="url_id_category";
    public static final String url_id_lang_list="url_id_lang_list";
    public static final String url_id_index_list="url_id_index_list_";
    public static final String url_id_movie_list="url_id_movie_list";
    public static final String url_id_xsp_list="url_id_xsp_list";
    public static final String url_id_user_info="url_id_user_info";
    public static final String url_id_login_out="url_id_login_out";
    public static final String url_id_update_user_info="url_id_update_user_info";
    public static final String url_id_upload_token="url_id_upload_token";
    public static final String url_id_news_ccontent ="url_id_news_ccontent";
    public static final String url_id_pl_list="url_id_pl_list";
    public static final String url_id_add_pl="url_id_add_pl";
    public static final String url_id_click_like="url_id_click_like";
    public static final String url_id_pl_add_like="url_id_pl_add_like";
    public static final String url_id_search_news="url_id_search_news";
    public static final String url_id_hot_search_key="url_id_hot_search_key";
    public static final String url_id_zt_list="url_id_zt_list";
    public static final String url_id_zt_xiangguang="url_id_zt_xiangguang";
    public static final String url_id_search_specialId="url_id_search_specialId";
    public static final String url_id_collection="url_id_collection";
    public static final String url_id_del_collection="url_id_del_collection";
    public static final String url_id_collection_list="url_id_collection_list";
    public static final String url_id_get_config="url_id_get_config";
    public static final String url_id_new_category="url_id_new_category";
    public static final String url_id_add_click="url_id_add_click";
    public static final String url_id_check_update="url_id_check_update";

}
