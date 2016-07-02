package com.eventtus.twitterapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

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

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                Globals.session = result.data;

                long userid = Globals.session.getUserId();
                Globals.userID = Long.toString(userid);
                Globals.userName = Globals.session.getUserName();

                System.out.println("attia successfully logged in .. hi "+Globals.session.getUserName());

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
}
