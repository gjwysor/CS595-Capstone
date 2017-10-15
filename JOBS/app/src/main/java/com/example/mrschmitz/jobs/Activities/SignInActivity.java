package com.example.mrschmitz.jobs.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mrschmitz.jobs.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button buttonGoogle = findViewById(R.id.buttonGoogle);
        buttonGoogle.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        Button buttonFacebook = findViewById(R.id.buttonFacebook);
        buttonFacebook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        Button buttonTwitter = findViewById(R.id.buttonTwitter);
        buttonTwitter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}
