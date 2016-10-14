package ru.avb.iremember;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Алекс on 22.08.2016.
 */
public class User {
    public boolean isAuthorized;
    String  displayName,
            email,
            id,
            photoUrl;
    Date lastSync;
    Calendar lastSyncCalendar;


    public String getId() {return this.id;}
    public String getDisplayName() {return this.displayName;}
    public boolean getNameDefined() {if (this.displayName == G.NONE_STRING) return false; else return true;}
    public boolean getEmailDefined() {if (this.email == G.NONE_STRING) return false; else return true;}
    public boolean getPhotoUrlDefined() {if (this.photoUrl == G.NONE_STRING) return false; else return true;}
    public boolean getLastSyncDefined() {
        if (this.lastSync==null) G.Log("G.user.lastSync == null");
        if (G.NONE_DATE==null) G.Log("G.NONE_DATE == null");
        if (this.lastSync == G.NONE_DATE) return false; else return true;
    }
    public String getEmail() {return this.email;}
    public String getPhotoUrl() {return this.photoUrl;}
    public Date getLastSync() {return this.lastSync;}
    public void setId(String id) {this.id = id;}
    public void setId(int id) {this.id = String.valueOf(id);}
    public void setDisplayName(String displayName) {this.displayName = displayName;}
    public void setEmail(String email) {this.email = email;}
    public void setLastSync(int y, int m, int d) {this.lastSync = new Date(y,m,d);}


    public User() {
        this.id = G.NONE_STRING;
        this.isAuthorized=false;
        this.displayName = G.NONE_STRING;
        this.email = G.NONE_STRING;
        this.photoUrl = G.NONE_STRING;
        this.lastSync = G.NONE_DATE;
        this.lastSyncCalendar = G.NONE_CALENDAR;
    }

    public User(String id, String displayName, String email, String photoUrl) {
        this.id = id;
        if (id == G.NONE_STRING) isAuthorized=false; else isAuthorized=true;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.lastSync = G.NONE_DATE;
        this.lastSyncCalendar = G.NONE_CALENDAR;
    };

    public void logData() {
        G.Log("----Log User Data:----");
        if (this==null) {G.Log("G.user is NULL."); G.Log("----------------------"); return;}
        G.Log("id: "+this.id);
        G.Log("Name: "+this.displayName);
        G.Log("eMail: "+this.email);
        G.Log("photoURL: "+this.photoUrl);
        G.Log("lastSync: "+this.lastSync.toString());
        G.Log("lastSyncCalendar: "+this.lastSyncCalendar.toString());
        G.Log("----------------------");
    }


    public void getDataFromGoogle() {
        G.Log("User.getDataFromGoogleSignInResult..");
        if (Google.signInAccount==null) Google.setAccount(Google.lastSignInResult);
        GoogleSignInAccount account = Google.signInAccount;
        G.user = new User(account.getId(), account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());
    }

    public void clearData() {
        G.Log("User.clearData..");
        this.isAuthorized = false;
        this.id = G.NONE_STRING;
        this.email = G.NONE_STRING;
        this.displayName = G.NONE_STRING;
        this.photoUrl = G.NONE_STRING;
        this.lastSync = G.NONE_DATE;
        this.lastSyncCalendar = G.NONE_CALENDAR;
    }
}

