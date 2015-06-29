package com.epicodus.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by jake on 6/23/15.
 */
@Table(name = "Users", id = "_id")
public class User extends Model {

    @Column(name = "Name")
    private String mName;

    public User() {
        super();
    }

    public User(String name) {
        super();
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public static User find(String name) {
        return new Select()
                .from(User.class)
                .where("Name = ?", name)
                .executeSingle();
    }
}
