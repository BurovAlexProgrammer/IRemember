package ru.avb.iremember;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import ru.avb.iremember.asyncs.checkLastSync;
import ru.avb.iremember.fragments.FragmentAccount;
import ru.avb.iremember.fragments.FragmentCategories;
import ru.avb.iremember.fragments.FragmentSettings;
import ru.avb.iremember.fragments.FragmentWelcome;

import static ru.avb.iremember.G.homeFragmentManager;
import static ru.avb.iremember.G.user;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Toolbar toolbar;
    NavigationView navView;
    View navHeader;
    TextView navHeader_accountName;
    TextView navHeader_accountEmail;
    ImageView navHeader_accountAvatar;
    DrawerLayout drawer;
    ActionBarDrawerToggle drawerToggle;
    FragmentCategories fragmentCategories;
    FragmentSettings fragmentSettings;
    FragmentAccount fragmentAccount;
    FragmentWelcome fragmentWelcome;
    FloatingActionButton faButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        G.Log("================HOME ACTIVITY=================");
        initialViews(HomeActivity.this);

        user = new User(this);     //init static user
        Options.initializeOptions(this);

        //Show initial fragment
        //goToFragment(fragmentAccount, getString(R.string.title_account));
        Google.signInInitialize(this, this, this);
        googleSilentSignIn();
        //need welcome
        G.Log("onPostCreate");
        if (Options.readOption(Options.KEY_NEED_WELCOME, true)) {
            goToFragment(fragmentWelcome, getString(R.string.title_welcome));
        }

        updateUI();
}

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        int id = item.getItemId();

        if (id == R.id.nav_categories) {
            fragmentTransaction.replace(R.id.container, fragmentCategories);
            getSupportActionBar().setTitle(R.string.title_categories);}
        else if (id == R.id.nav_settings) {
            fragmentTransaction.replace(R.id.container, fragmentSettings);
            getSupportActionBar().setTitle(R.string.title_settings);}
        else if (id == R.id.nav_account) {
            fragmentTransaction.replace(R.id.container, fragmentAccount);
            getSupportActionBar().setTitle(R.string.title_account);}
        else if (id == R.id.nav_share) {

        }

        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        G.Log("HomeActivity.onResume..");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        G.Log("HomeActivity.onActivityResult..");
        //Welcome request
        if (requestCode==G.REQUEST_WELCOME_FROM_MAIN) {
            G.Log("ActivityResult from Welcome.Activity");
            Google.signInInitialize(this, this, this);
            googleSilentSignIn();
            updateUI();
        }
        if (requestCode==G.REQUEST_CHANGE_LANGUAGE) {
            G.Log("ActivityResult from HomeActivity.Settings");
            G.Log("Result: "+data.toString());
            updateUI();
        }
        if (requestCode==G.REQUEST_SIGN_IN_GOOGLE) {
            G.Log("ActivityResult from Google.signIn");
            Google.lastSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            boolean isSuccess = Google.handleSignInResult(this, Google.lastSignInResult);
            G.Log("isSuccess: "+isSuccess);
            updateUI();
        }
//        if (requestCode==G.REQUEST_RESOLVE_DRIVE_CONNECTION) {
//            Google.driveApi.connect();
//        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        G.Log("ERROR!! Connection Failed!");
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, G.REQUEST_RESOLVE_DRIVE_CONNECTION);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
        }
        if (Google.isResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                Google.isResolvingError = true;
                result.startResolutionForResult(this, G.REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                Google.apiClient.connect();
            }
        } else {
            G.Log("ErrorCode: "+result.getErrorCode());
            Google.isResolvingError = true;
        }
    }


    private void initialViews(AppCompatActivity content) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        faButton = (FloatingActionButton) findViewById(R.id.fab);
        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        fragmentCategories = new FragmentCategories();
        fragmentSettings = new FragmentSettings();
        fragmentAccount = new FragmentAccount();
        fragmentWelcome = new FragmentWelcome();

        navHeader = navView.getHeaderView(0);
        navHeader_accountName = (TextView) navHeader.findViewById(R.id.nav_header_nane_account);
        navHeader_accountEmail = (TextView) navHeader.findViewById(R.id.nav_header_email);
        navHeader_accountAvatar = (ImageView) navHeader.findViewById(R.id.nav_header_avatar);

        G.homeFragment = fragmentCategories;
        G.homeFragmentManager = getFragmentManager();
    }

    public void updateUI () {
        G.Log("UpdateUI..");
        updateFragmentsUI();
        //Navigation header
        if (user.isAuthorized()) UrlImageViewHelper.setUrlDrawable(navHeader_accountAvatar, user.photoUrl,R.mipmap.placeholder_account);
        else navHeader_accountAvatar.setImageResource(R.mipmap.placeholder_account);

        if (user.isAuthorized()) navHeader_accountName.setText(user.displayName);
        else navHeader_accountName.setText(R.string.spaceholder_accountName);

        if (user.isAuthorized()) {
            navHeader_accountEmail.setVisibility(View.VISIBLE);
            navHeader_accountEmail.setText(user.email);
        }
        else {
            navHeader_accountEmail.setVisibility(View.GONE);
            navHeader_accountEmail.setText(R.string.spaceholder_accountEmail);
        }
    }
    public void signIn() {
        Google.signInInitialize(this,this,this);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(Google.apiClient);
        startActivityForResult(signInIntent,G.REQUEST_SIGN_IN_GOOGLE);
    }

    public void signOut() {
        G.Log("signOut()");
        if (Google.apiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(Google.apiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            G.Log("Google Sing-Out successed. Status:" + status);
                            user.clearData();
                            updateUI();
                        }
                    }
            );
        }
        else {
            G.Log("Google.signInApi is not connected");
            user.clearData();
            updateUI();
        }
    }
    private void googleSilentSignIn() {
        G.Log("Google.silentSignIn..");
        // Try silent sign-in with Google Sign In API
        OptionalPendingResult<GoogleSignInResult> pendResult =
                Auth.GoogleSignInApi.silentSignIn(Google.apiClient);
        if (pendResult.isDone()) {
            Google.lastSignInResult = pendResult.get();
            Google.handleSignInResult(HomeActivity.this, Google.lastSignInResult);
            updateUI();
        }
        else {
            //showProgress();
            pendResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgress();
                    Google.handleSignInResult(HomeActivity.this, googleSignInResult);
                    checkLastSync sync = new checkLastSync(HomeActivity.this);
                    sync.execute(this);
                    //updateUI();
                }
            });
        }
    }

    public void updateFragmentsUI() {
//        if (toolbar.getTitle().equals(getString(R.string.title_account))) {
//            G.Log("Update from activity FragmentAccount");
            if (fragmentAccount.isVisible()) {
                ((FragmentAccount)getFragmentManager().findFragmentById(R.id.container)).updateUI();
            }
//        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void goToFragment(Fragment fragment, String title) {
        G.Log("goToFragment("+title+")");
        FragmentTransaction fragmentTransaction = homeFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(title);
    }
}
