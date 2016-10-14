package ru.avb.iremember;

import android.content.Context;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Алекс on 25.05.2016.
 */
public class GoogleSingIn {
    public Context context;
    public static final int RC_SIGN_IN = 10000;
    public GoogleApiClient googleApiClient;
    public GoogleSignInOptions googleSignInOptions;
    public GoogleSingIn (Context context) {
        context = this.context;
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }
}
