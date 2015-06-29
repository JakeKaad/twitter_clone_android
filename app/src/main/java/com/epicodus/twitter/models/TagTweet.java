package com.epicodus.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 6/23/15.
 */
@Table(name = "tag_tweets", id = "_id")
public class TagTweet extends Model {

    @Column(name = "Tweet")
    public Tweet mTweet;

    @Column(name = "Tag")
    public Tag mTag;

    public TagTweet(Tag tag, Tweet tweet) {
        super();
        mTweet = tweet;
        mTag = tag;
    }

    public TagTweet() {
        super();
    }


    public static TagTweet find(int id) {
        return new Select()
                .from(TagTweet.class)
                .where("_id = ?", id)
                .executeSingle();
    }

    public static List<TagTweet> findByTag(int id) {
        return new Select()
                .from(TagTweet.class)
                .where("Tag = ?", id)
                .execute();
    }

}
