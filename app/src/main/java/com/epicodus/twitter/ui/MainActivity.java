package com.epicodus.twitter.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okio.BufferedSink;


public class MainActivity extends ListActivity {
    public static String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences mPreferences;
    private User mUser;
    private EditText mTweetText;
    private Button mSubmitButton;
    private ImageView mSearchImage;
    private ArrayList<Tweet> mTweets;
    private TweetAdapter mAdapter;
    private TextView mBreakTheCodeLabel;
    private String mBearerToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getApplicationContext().getSharedPreferences("twitter", Context.MODE_PRIVATE);
        mSearchImage = (ImageView) findViewById(R.id.searchButtonImage);
        mTweetText = (EditText) findViewById(R.id.newTweetEdit);
        mSubmitButton = (Button) findViewById(R.id.tweetSubmitButton);
        mBreakTheCodeLabel = (TextView) findViewById(R.id.breakTheCodeLabel);

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

        mBreakTheCodeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String twitterKey = "YOUR_TWITTER_KEY";
                String twitterSecret = "YOUR_TWITTER_SECRET";
                String oauthTokenUrl = "https://api.twitter.com/oauth2/token";


                String twitterBody = "grant_type=client_credentials";
                MediaType mediaType = MediaType.parse("x-www-form-urlencoded; charset=utf-8");

                try {
                    String urlApiKey = URLEncoder.encode(twitterKey, "UTF-8");
                    String urlApiSecret = URLEncoder.encode(twitterSecret, "UTF-8");
                    String keyAndSecret = urlApiKey + ":" + urlApiSecret;
                    String base64Encoded = Base64.encodeToString(keyAndSecret.getBytes(), Base64.NO_WRAP);

                    if (isNetworkAvailable()) {
                        OkHttpClient client = new OkHttpClient();


                        Request oauthRequest = new Request.Builder()
                                .url(oauthTokenUrl)
                                .addHeader("Authorization", "Basic " + base64Encoded)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                                .post(RequestBody.create(mediaType, twitterBody))
                                .build();

                        Call oauthCall = client.newCall(oauthRequest);
                        oauthCall.enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Log.v("TWITTER_FAILURE", "AUTH REQUEST FAILED");
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                try {
                                    String jsonData = response.body().string();
                                    Log.v(TAG, jsonData);
                                    JSONObject jsonTweets = new JSONObject(jsonData);
                                    mBearerToken = jsonTweets.getString("access_token");
                                    Log.v(TAG, mBearerToken);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setBreakTheCodeTweets();
                                        }
                                    });
                                } catch (JSONException e) {
                                    Log.e(TAG, "exception caught: ", e);
                                }
                            }
                        });


                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setBreakTheCodeTweets() {
        OkHttpClient client = new OkHttpClient();
        String twitterSearchUrl = "https://api.twitter.com/1.1/search/tweets.json?q=%23breakthecode";

        Request searchRequest = new Request.Builder()
                .url(twitterSearchUrl)
                .addHeader("Authorization", "Bearer " + mBearerToken)
                .addHeader("Content-Type", "application/json")
                .build();

        Call searchCall = client.newCall(searchRequest);
        searchCall.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("TWITTER_FAILURE", "SEARCH REQUEST FAILED");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try{
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    JSONObject jsonTweets = new JSONObject(jsonData);
                } catch (JSONException e) {
                    Log.e(TAG, "exception caught: ", e);
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
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
