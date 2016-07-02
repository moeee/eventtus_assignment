package com.eventtus.twitterapp.data;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

//data model
public class TwitterUsers {
    @SerializedName("users")
    public final List<User> users;

    public TwitterUsers(List<User> users) {
        this.users = users;
    }
}
