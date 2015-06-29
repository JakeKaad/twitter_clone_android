package com.epicodus.twitter.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 6/23/15.
 */
@Table(name = "Tags", id = "_id")
public class Tag extends Model {

    @Column(name = "Tag")
    private String mTag;

    public Tag() {
        super();
    }

    public Tag(String tag) {
        super();
        mTag = tag;
    }

    public static Tag newTag(String word) {
        Tag tag = find(word);

        if (tag == null) {
            tag = new Tag(word);
            tag.save();
        }

        return tag;
    }

    public static Tag find(String tag) {
        return new Select()
                .from(Tag.class)
                .where("Tag = ?", tag)
                .executeSingle();
    }

    public List<Tweet> getTweets() {
        List<TagTweet> joins = new Select()
                .from(TagTweet.class)
                .where("Tag = ?", this.getId())
                .execute();

        List<Tweet> tweets = new ArrayList<>();

        for (TagTweet join : joins) {
            tweets.add(join.mTweet);
        }
        return tweets;
    }

    public void addTweetsToList(ArrayList<Tweet> tweets) {
        List<Tweet> tweetsToAdd = getTweets();

        for (Tweet tweet : tweetsToAdd) {
            if (tweet != null) {
                tweets.add(tweet);
            }
        }
    }
}
