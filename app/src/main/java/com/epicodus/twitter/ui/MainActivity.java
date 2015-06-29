package com.epicodus.twitter.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.twitter.R;
import com.epicodus.twitter.adapters.TweetAdapter;
import com.epicodus.twitter.models.Tweet;
import com.epicodus.twitter.models.User;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {
    public static String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences mPreferences;
    private User mUser;
    private EditText mTweetText;
    private Button mSubmitButton;
    private ImageView mSearchImage;
    private ArrayList<Tweet> mTweets;
    private TweetAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getApplicationContext().getSharedPreferences("twitter", Context.MODE_PRIVATE);
        mSearchImage = (ImageView) findViewById(R.id.searchButtonImage);
        mTweetText = (EditText) findViewById(R.id.newTweetEdit);
        mSubmitButton = (Button) findViewById(R.id.tweetSubmitButton);
        mTweets = (ArrayList) Tweet.all();

        mAdapter = new TweetAdapter(this, mTweets);
        setListAdapter(mAdapter);

        if (!isRegistered()) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = mTweetText.getText().toString();
                Tweet tweet = new Tweet(tweetContent, mUser);
                tweet.save();
                tweet.parseHashTags();
                mTweets.add(tweet);
                mAdapter.notifyDataSetChanged();

                // Clears input and hides keyboard
                mTweetText.setText("");
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        mSearchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isRegistered() {
        String username = mPreferences.getString("username", null);
        if (username != null) {
            setUser(username);
            return true;
        } else {
            return false;
        }
    }

    private void setUser(String username) {
        User user = User.find(username);
        if (user != null) {
            mUser = user;
        } else {
            mUser = new User(username);
            mUser.save();
        }
        Toast.makeText(this, "Welcome " + mUser.getName(), Toast.LENGTH_LONG).show();
    }
}
