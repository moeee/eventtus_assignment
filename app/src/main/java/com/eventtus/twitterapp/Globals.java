package com.eventtus.twitterapp;

import android.content.SharedPreferences;

import com.eventtus.twitterapp.data.Followers;
import com.eventtus.twitterapp.data.TwitterUsers;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;

import retrofit.http.GET;
import retrofit.http.Query;


public class Globals {

    public static final String TWITTER_KEY = "2h7w3kj7xOHKTrTCGWU70wkgu";
    public static final String TWITTER_SECRET = "UJsfczo3vELkxkRazfKjIColBy2CRuHz8aw6zuzNHB9IwFj3jz";
    public static TwitterSession session;
    public static String userID="";
    public static String userName="";
    public static SharedPreferences prefs;
    public static ArrayList<Followers> followersList = new ArrayList<Followers>();
    public static int selectedFollowerPosition;

    public static class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }

        public CustomService getCustomService() {
            return getService(CustomService.class);
        }

    }

    public interface CustomService {
        @GET("/1.1/followers/list.json")
        void show(@Query("user_id") String userId,
                  @Query("screen_name") String var,
                  @Query("skip_status") Boolean var1,
                  @Query("include_user_entities") Boolean var2,
                  @Query("count") Integer var3, Callback<TwitterUsers> cb);
    }
}
