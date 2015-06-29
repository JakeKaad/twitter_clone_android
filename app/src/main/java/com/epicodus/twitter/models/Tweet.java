package com.epicodus.twitter.models;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.epicodus.twitter.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by jake on 6/23/15.
 */
@Table(name = "Tweets", id = "_id")
public class Tweet extends Model {

    @Column(name = "Content")
    private String mContent;

    @Column(name = "CreatedAt")
    private long mCreatedAt;

    @Column(name = "User")
    private User mUser;


    public Tweet() {
        super();
    }

    public Tweet(String content, User user) {
        mContent = content;
        mUser = user;
        mCreatedAt = new Date().getTime();
    }

public void parseHashTags() {
    String[] words = mContent.split("\\s+");

    for (String word : words) {
        if (word.startsWith("#")) {
            Tag tag = Tag.newTag(word);
            TagTweet tagTweet = new TagTweet(tag, this);
            tagTweet.save();
        }
    }
}

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
    }

    public User getUser() {
       return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getFormattedTime(Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat(context.getString(R.string.formatted_time));
        formatter.setTimeZone(TimeZone.getTimeZone(context.getString(R.string.timezone)));
        return formatter.format(mCreatedAt);
    }

    public static List<Tweet> all() {
        return new Select()
                .from(Tweet.class)
                .execute();
    }
}
