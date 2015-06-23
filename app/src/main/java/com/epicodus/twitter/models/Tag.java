package com.epicodus.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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
}
