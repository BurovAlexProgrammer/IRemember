package ru.avb.iremember;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class WelcomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        G.Log("================WELCOME ACTIVITY=================");
        Options.initializeOptions(this);
        G.Log("Options.locale: " + Options.locale);
        setContentView(R.layout.welcome_activity);
        Button buttonOk = (Button) findViewById(R.id.buttonOk);
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        Button buttonLanguage = (Button) findViewById(R.id.buttonLanguage);
        Button buttonSignOut = (Button) findViewById(R.id.button_sign_out);
        updateLangButton();

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.buttonOk) {
                    confirm();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                if (v.getId() == R.id.buttonCancel) {
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
                if (v.getId() == R.id.buttonLanguage) {
                    Intent intent = new Intent(WelcomeActivity.this, LanguageActivity.class);
                    startActivityForResult(intent, G.REQUEST_CHANGE_LANGUAGE);
                }
                if (v.getId() == R.id.button_sign_in) signIn();
                if (v.getId() == R.id.button_sign_out) signOut();

            }
        };

        buttonOk.setOnClickListener(onClick);
        buttonCancel.setOnClickListener(onClick);
        buttonLanguage.setOnClickListener(onClick);
        findViewById(R.id.button_sign_in).setOnClickListener(onClick);
        buttonSignOut.setOnClickListener(onClick);

        //----------Google----------
       // Google.signInInitialize(this,this,this);
        //---------------------------
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == G.REQUEST_CHANGE_LANGUAGE) {
        }
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == G.REQUEST_SIGN_IN_GOOGLE) {
            Google.lastSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            boolean isSuccess = Google.handleSignInResult(Google.lastSignInResult);
            updateUI(isSuccess);
        }
    }

    public void confirm() {
        Options.writeOption(Options.KEY_NEED_WELCOME, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
/*        if (Google.apiClient != null)
            Google.apiClient.connect();
        updateLangButton();*/
    }

    @Override
    protected void onStop() {
/*        if (Google.apiClient != null && Google.apiClient.isConnected()) {
            Google.apiClient.disconnect();
        }*/
        super.onStop();
    }

    //=======================GOOGLE AUTH====================================
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("log_app", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        G.Toast(this, "Connection failed: ErrorCode = " + connectionResult.getErrorCode());
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(Google.apiClient);
        startActivityForResult(signInIntent,G.REQUEST_SIGN_IN_GOOGLE);
    }

    public void signOut() {
        G.Log("signOut()");
        Auth.GoogleSignInApi.signOut(Google.apiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        G.Log("Google Sing-Out successed. Status:"+status);
                        G.user.clearData();
                        updateUI(false);
                    }
                }
        );
    }

    public void updateUI(boolean connected) {
        G.Log("updateUI()");
        if (connected) {
            findViewById(R.id.button_sign_in).setVisibility(View.INVISIBLE);
            findViewById(R.id.button_sign_out).setVisibility(View.VISIBLE);
            findViewById(R.id.signInGoogle_description).setVisibility(View.INVISIBLE);
            findViewById(R.id.youAreSignedInAs).setVisibility(View.VISIBLE);
            findViewById(R.id.nameAndEmail).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nameAndEmail)).setText(G.user.displayName+ "  (" +G.user.email+ ")" );
        }
        else {
            findViewById(R.id.button_sign_in).setVisibility(View.VISIBLE);
            findViewById(R.id.button_sign_out).setVisibility(View.INVISIBLE);
            findViewById(R.id.signInGoogle_description).setVisibility(View.VISIBLE);
            findViewById(R.id.youAreSignedInAs).setVisibility(View.INVISIBLE);
            findViewById(R.id.nameAndEmail).setVisibility(View.INVISIBLE);
        }
        //
    }

    public void updateLangButton() {
        G.Log("updateLangButton()");
        G.Log("locale: "+Options.locale);
        int imageId = R.mipmap.locale_default;
        switch (Options.locale) {
            case "default": imageId=R.mipmap.locale_default; break;
            case "ru": imageId=R.mipmap.locale_ru; break;
            case "en": imageId=R.mipmap.locale_en; break;

        }
        //Drawable image = getResources().getDrawable( imageId );
        Drawable image = ResourcesCompat.getDrawable(getResources(), imageId, null);
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds( 0, 0, w-5, h-5 );
        //Drawable image = getResources().getDrawable(imageId);
        //((Button)findViewById(R.id.buttonLanguage)).setCompoundDrawablesWithIntrinsicBounds(image, null, null, null);
        ((Button)findViewById(R.id.buttonLanguage)).setCompoundDrawables( image, null, null, null );

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
