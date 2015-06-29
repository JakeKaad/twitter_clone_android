package com.epicodus.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.twitter.R;

public class RegisterActivity extends ActionBarActivity {

    private EditText mNameEdit;
    private Button mLoginButtonl;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameEdit = (EditText) findViewById(R.id.nameText);
        mLoginButtonl = (Button) findViewById(R.id.registerButton);
        mPreferences = getApplicationContext().getSharedPreferences("twitter", Context.MODE_PRIVATE);

        mLoginButtonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEdit.getText().toString();
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("username", name);
                editor.commit();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
