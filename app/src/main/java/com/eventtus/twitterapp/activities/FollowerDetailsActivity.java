package com.eventtus.twitterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventtus.twitterapp.Globals;
import com.eventtus.twitterapp.R;
import com.eventtus.twitterapp.utilities.ImageLoader;


public class FollowerDetailsActivity extends AppCompatActivity {

    TextView name,handle,description;
    ImageView profileImage,bgImage;
    Button tweetsBtn;
    public ImageLoader imageLoader;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_details);

        getViews();

        name.setText(Globals.followersList.get(Globals.selectedFollowerPosition).getName());
        handle.setText(Globals.followersList.get(Globals.selectedFollowerPosition).getHandle());
        description.setText(Globals.followersList.get(Globals.selectedFollowerPosition).getDescription());

        imageLoader = new ImageLoader(this);
        imageLoader.DisplayImage(Globals.followersList.get(Globals.selectedFollowerPosition).getProfImage(), profileImage);
        if(!Globals.followersList.get(Globals.selectedFollowerPosition).getBgImage().equals("")){
            imageLoader.DisplayImage(Globals.followersList.get(Globals.selectedFollowerPosition).getBgImage(), bgImage);
        }

        tweetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FollowerDetailsActivity.this, TimeLineActivity.class);
                startActivity(i);
            }
        });

    }

    void getViews(){
        name = (TextView) findViewById(R.id.name_indetails);
        handle = (TextView) findViewById(R.id.handle_indetails);
        description = (TextView) findViewById(R.id.description_indetails);
        profileImage = (ImageView) findViewById(R.id.profile_details_image);
        bgImage = (ImageView) findViewById(R.id.profile_details_bg);
        tweetsBtn = (Button) findViewById(R.id.tweets_button);
    }
}
