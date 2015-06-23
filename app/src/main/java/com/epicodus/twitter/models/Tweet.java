package com.epicodus.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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
}
