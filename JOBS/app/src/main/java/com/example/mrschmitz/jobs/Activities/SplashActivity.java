package com.example.mrschmitz.jobs.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mrschmitz.jobs.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private static final String UNCHANGED_CONFIG_VALUE = "CHANGE-ME";
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";

    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        int splashTimeout = 500;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                    startSignedInActivity();
                    finish();
                    return;
                }

                if (isGoogleMisconfigured()) {
                    toast(R.string.google_label_missing_config);
                }

                if (isFacebookMisconfigured()) {
                    toast(R.string.facebook_label_missing_config);
                }

                if (isTwitterMisconfigured()) {
                    toast(R.string.twitter_label_missing_config);
                }

                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setTheme(getSelectedTheme())
                                .setLogo(getSelectedLogo())
                                .setAvailableProviders(getSelectedProviders())
                                .build(),
                        RC_SIGN_IN);
            }
        }, splashTimeout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

        toast(R.string.unknown_response);
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startSignedInActivity();
            finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                toast(R.string.sign_in_cancelled);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                toast(R.string.no_internet_connection);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                toast(R.string.unknown_error);
                return;
            }
        }

        toast(R.string.unknown_sign_in_response);
    }

    private void startSignedInActivity() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    @MainThread
    @StyleRes
    private int getSelectedTheme() {
        return AuthUI.getDefaultTheme();
    }

    @MainThread
    @DrawableRes
    private int getSelectedLogo() {
        return AuthUI.NO_LOGO;
    }

    @MainThread
    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
        if (!isGoogleMisconfigured()) {
            selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
        }
        if (!isFacebookMisconfigured()) {
            selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
        }
        if (!isTwitterMisconfigured()) {
            selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());
        }
        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        return selectedProviders;
    }

    @MainThread
    private boolean isGoogleMisconfigured() {
        return UNCHANGED_CONFIG_VALUE.equals(getString(R.string.default_web_client_id));
    }

    @MainThread
    private boolean isFacebookMisconfigured() {
        return UNCHANGED_CONFIG_VALUE.equals(getString(R.string.facebook_application_id));
    }

    @MainThread
    private boolean isTwitterMisconfigured() {
        List<String> twitterConfigs = Arrays.asList(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret)
        );

        return twitterConfigs.contains(UNCHANGED_CONFIG_VALUE);
    }

    @MainThread
    private void toast(@StringRes int errorMessageRes) {
        Toast.makeText(this, errorMessageRes, Toast.LENGTH_SHORT).show();
    }

}


