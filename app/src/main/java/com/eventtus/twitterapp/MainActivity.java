package com.eventtus.twitterapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TwitterLoginButton loginButton;
    ProgressDialog pd;
    Spinner spinner;
    List<String> savedLoginsList;

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
        getAllSharedPref();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, savedLoginsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                Globals.session = result.data;

                long userid = Globals.session.getUserId();
                Globals.userID = Long.toString(userid);
                Globals.userName = Globals.session.getUserName();

                storeSessionInSharedPref(Globals.userName, Globals.session);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        Map<String,?> keys = Globals.prefs.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            if (entry.getKey().equals(item)) {
                Globals.prefs = getSharedPreferences("TwitterApp", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String str = Globals.prefs.getString(entry.getKey(), "");
                TwitterSession sessionObj = gson.fromJson(str, TwitterSession.class);
                Globals.session = sessionObj;
                long userid = Globals.session.getUserId();
                Globals.userID = Long.toString(userid);
                Globals.userName = Globals.session.getUserName();
                getFollowers();
            }
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    void getViews(){
        String pdw = "Loading.. Please wait";
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage(pdw);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        spinner = (Spinner) findViewById(R.id.spinner_list);
        spinner.setOnItemSelectedListener(this);
        savedLoginsList = new ArrayList<String>();
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
                storeDataInSharedPref(Globals.userName,Globals.followersList);
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

    void storeSessionInSharedPref(String nameKey, TwitterSession session){
        SharedPreferences.Editor prefsEditor = Globals.prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(session);
        prefsEditor.putString(nameKey, json);
        prefsEditor.commit();
    }

    void storeDataInSharedPref(String key, ArrayList<Followers> list){
        SharedPreferences.Editor editor = Globals.prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key+"-data", json);
        editor.commit();
    }

    void getDataFromSharedPref(){
        Globals.prefs = getSharedPreferences("TwitterApp", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = Globals.prefs.getString(Globals.userName+"-data", null);
        Type type = new TypeToken<ArrayList<Followers>>() {}.getType();
        Globals.followersList = gson.fromJson(json, type);
    }

    void getAllSharedPref(){
        savedLoginsList.add("Select from saved Users");
        Globals.prefs = getSharedPreferences("TwitterApp", Context.MODE_PRIVATE);
            Map<String,?> keys = Globals.prefs.getAll();

            for(Map.Entry<String,?> entry : keys.entrySet()){
                if(!entry.getKey().contains("-data")){
                    savedLoginsList.add(entry.getKey());
                }

                Log.d("map values",entry.getKey() + " : " +
                        entry.getValue().toString());
            }
    }

}// end of class
