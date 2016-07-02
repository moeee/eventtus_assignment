package com.eventtus.twitterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.eventtus.twitterapp.Globals;
import com.eventtus.twitterapp.R;
import com.eventtus.twitterapp.data.Followers;
import com.eventtus.twitterapp.data.FollowersAdapter;
import com.eventtus.twitterapp.data.TwitterUsers;
import com.eventtus.twitterapp.lazylist.PullAndLoadListView;
import com.eventtus.twitterapp.lazylist.PullToRefreshListView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import java.util.ArrayList;


public class FollowersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Followers> ListItems = new ArrayList<Followers>();
    FollowersAdapter adapter;
    PullAndLoadListView lazylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        lazylist = (PullAndLoadListView) findViewById(R.id.lazylist);
        for(int i = 0; i< Globals.followersList.size(); i++){
            ListItems.add(Globals.followersList.get(i));
        }

        adapter= new FollowersAdapter(this, ListItems);
        lazylist.setAdapter(adapter);
        lazylist.setOnItemClickListener(this);

        // Set a listener to be invoked when the list should be refreshed.
        ((PullAndLoadListView) lazylist)
                .setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

                    public void onRefresh() {
                        // refresh the list here.
                        getPullRefreshFollowers();
                    }
                });

        // set a listener to be invoked when the list reaches the end
        ((PullAndLoadListView) lazylist)
                .setOnLoadMoreListener(new PullAndLoadListView.OnLoadMoreListener() {

                    public void onLoadMore() {
                        // load more items at the end of list
//                        getLoadmMoreNews();
                    }
                });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stubs
        try{
            Globals.selectedFollowerPosition = position-1;
            Intent intent = new Intent(this, FollowerDetailsActivity.class);
            startActivity(intent);

        }catch( IndexOutOfBoundsException e){
            Log.d("Bounds Exception", "caught");
        }
    }

    void getPullRefreshFollowers(){

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
                    adapter.notifyDataSetChanged();
                    lazylist.onRefreshComplete();
                }

                @Override
                public void failure(TwitterException e) {
                    Log.e("Mabr0o0o0o0o0ok !! ", "failed to download data");
                }
            });

    }
}// end of class
