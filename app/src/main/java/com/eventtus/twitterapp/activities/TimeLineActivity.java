package com.eventtus.twitterapp.activities;

import android.app.ListActivity;
import android.os.Bundle;

import com.eventtus.twitterapp.Globals;
import com.eventtus.twitterapp.R;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUi;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;


public class TimeLineActivity extends ListActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(Globals.TWITTER_KEY, Globals.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetUi());

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(Globals.followersList.get(Globals.selectedFollowerPosition).getHandle())
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, userTimeline);
        setListAdapter(adapter);

    }
}
