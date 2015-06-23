package com.epicodus.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by jake on 6/23/15.
 */
@Table(name = "tag_tweets", id = "_id")
public class TagTweets extends Model {

    @Column(name = "Tweet")
    public Tweet mTweet;

    @Column(name = "Tag")
    public Tag mTag;

    public TagTweets(Tweet tweet, Tag tag) {
        super();
        mTweet = tweet;
        mTag = tag;
    }

}
