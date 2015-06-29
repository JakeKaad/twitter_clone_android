package com.epicodus.twitter.ui;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.twitter.R;
import com.epicodus.twitter.adapters.TweetAdapter;
import com.epicodus.twitter.models.Tag;
import com.epicodus.twitter.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends ListActivity {

    private EditText mSearchText;
    private Button mSearchButton;
    private TextView mEmptyText;
    private TweetAdapter mAdapter;
    private ArrayList<Tweet> mTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchText = (EditText) findViewById(R.id.searchText);
        mSearchButton = (Button) findViewById(R.id.searchButton);
        mEmptyText = (TextView) findViewById(android.R.id.empty);
        mTweets = new ArrayList<>();
        mAdapter = new TweetAdapter(this, mTweets);
        setListAdapter(mAdapter);

    mSearchButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTweets.clear();
            String tag = "#" + mSearchText.getText().toString();
            Tag hashtag = Tag.find(tag);

            if (hashtag != null) {
                hashtag.addTweetsToList(mTweets);
            } else {
                mEmptyText.setText("No tweets found with " + tag);
            }
            mAdapter.notifyDataSetChanged();
        }
    });
    }
}

