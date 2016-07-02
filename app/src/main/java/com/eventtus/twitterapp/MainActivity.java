package com.eventtus.twitterapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.eventtus.twitterapp.activities.FollowersActivity;
import com.eventtus.twitterapp.data.TwitterUsers;
import com.eventtus.twitterapp.data.Followers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Globals.TWITTER_KEY, Globals.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        // give permission to StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getViews();
        checkIfAlreadyLoggedin();

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                Globals.session = result.data;

                long userid = Globals.session.getUserId();
                Globals.userID = Long.toString(userid);
                Globals.userName = Globals.session.getUserName();

                storeSessionInSharedPref();

                getFollowers();

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("Error", "Login with Twitter failure", exception);
                Toast.makeText(getApplicationContext(), "Connection Error !!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    void getViews(){
        String pdw = "Loading.. Please wait";
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage(pdw);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
    }

    void checkIfAlreadyLoggedin(){
        getSessionFromSharedPref();
        if(Globals.session!=null){
            long userid = Globals.session.getUserId();
            Globals.userID = Long.toString(userid);
            Globals.userName = Globals.session.getUserName();
            getFollowers();
        }
    }

    void getFollowers(){
        pd.show();
        new Globals.MyTwitterApiClient(Globals.session).getCustomService().show(Globals.userID, Globals.userName, true, true, 50, new Callback<TwitterUsers>() {
            @Override
            public void success(Result<TwitterUsers> result) {
                for (int i=0;i<result.data.users.size();i++){
                    Followers fObject = new Followers();
                    fObject.setName(result.data.users.get(i).name);
                    fObject.setHandle(result.data.users.get(i).screenName);
                    fObject.setDescription(result.data.users.get(i).description);
                    fObject.setProfImage(result.data.users.get(i).profileImageUrl);
                    fObject.setBgImage(result.data.users.get(i).profileBackgroundImageUrl);
                    Globals.followersList.add(fObject);
                }
                storeDataInSharedPref();
                if(pd.isShowing()) pd.dismiss();
                Intent intent = new Intent(MainActivity.this, FollowersActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(TwitterException e) {
                Log.e("Mabr0o0o0o0o0ok !! ", "failed to download data");
                Toast.makeText(getApplicationContext(), "Connection Error !!",Toast.LENGTH_LONG).show();
                getDataFromSharedPref();
                if(pd.isShowing()) pd.dismiss();
                Intent intent = new Intent(MainActivity.this, FollowersActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void storeSessionInSharedPref(){
        SharedPreferences.Editor prefsEditor = Globals.prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Globals.session);
        prefsEditor.putString("Session", json);
        prefsEditor.commit();
    }

    void getSessionFromSharedPref(){
        Globals.prefs = getSharedPreferences("TwitterApp", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String str = Globals.prefs.getString("Session", "");
        TwitterSession sessionObj = gson.fromJson(str, TwitterSession.class);
        Globals.session = sessionObj;
    }

    void storeDataInSharedPref(){
        SharedPreferences.Editor editor = Globals.prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Globals.followersList);
        editor.putString("FollowersData", json);
        editor.commit();
    }

    void getDataFromSharedPref(){
        Globals.prefs = getSharedPreferences("TwitterApp", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Globals.prefs.getString("FollowersData", null);
        Type type = new TypeToken<ArrayList<Followers>>() {}.getType();
        Globals.followersList = gson.fromJson(json, type);
    }

}// end of class
