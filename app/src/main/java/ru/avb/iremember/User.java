package ru.avb.iremember;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Алекс on 22.08.2016.
 */
public class User {
    public boolean isAuthorized;
    String  displayName,
            email,
            id,
            photoUrl;
    public DateTime lastSync;
    private String lastSyncText;


    public String getId() {return this.id;}
    public String getDisplayName() {return this.displayName;}
    public boolean getNameDefined() {if (this.displayName == G.NONE_STRING) return false; else return true;}
    public boolean getEmailDefined() {if (this.email == G.NONE_STRING) return false; else return true;}
    public boolean getPhotoUrlDefined() {if (this.photoUrl == G.NONE_STRING) return false; else return true;}
    public boolean getLastSyncDefined() {
        if (this.lastSync==null) {G.Log("G.user.lastSync == null"); return false;}
        if (this.lastSync == G.NONE_DATETIME) return false; else return true;
    }
    public String getEmail() {return this.email;}
    public String getPhotoUrl() {return this.photoUrl;}
    public DateTime getLastSync() {
        return this.lastSync;
    }
    public void setId(String id) {this.id = id;}
    public void setId(int id) {this.id = String.valueOf(id);}
    public void setDisplayName(String displayName) {this.displayName = displayName;}
    public void setEmail(String email) {this.email = email;}
    public void setLastSync(DateTime dateTime) {
        this.lastSync = dateTime;
    }


    public User() {
        this.id = G.NONE_STRING;
        this.isAuthorized=false;
        this.displayName = G.NONE_STRING;
        this.email = G.NONE_STRING;
        this.photoUrl = G.NONE_STRING;
        this.lastSync = G.NONE_DATETIME;
    }

    public User(String id, String displayName, String email, String photoUrl) {
        this.id = id;
        if (id == G.NONE_STRING) isAuthorized=false; else isAuthorized=true;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.lastSync = G.NONE_DATETIME;
    };

    public void logData() {
        G.Log("----Log User Data:----");
        if (this==null) {G.Log("G.user is NULL."); G.Log("----------------------"); return;}
        G.Log("id: "+this.id);
        G.Log("Name: "+this.displayName);
        G.Log("eMail: "+this.email);
        G.Log("photoURL: "+this.photoUrl);
        G.Log("lastSync: "+this.lastSync.toString());
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
        this.lastSync = G.NONE_DATETIME;
    }

    public String getLastSyncText(Context context, DateTime dateTime) {
        Resources r = context.getResources();
        String sBegin = r.getString(R.string.lastSync)+": ";
        String datePattern = "";
        String timePattern = "";
        if (dateTime==G.NONE_DATETIME) return sBegin+r.getString(R.string.never);
            if (dateTime.getDayOfYear()!=DateTime.now().getDayOfYear()) {
                datePattern = "dd.MM.yyyy";
                DateTimeFormatter formatter = DateTimeFormat.forPattern(datePattern);
                String date = formatter.print(dateTime);
                return sBegin+r.getString(R.string.at_date)+" "+date;}
            else { //The same day
                datePattern=""; timePattern="HH:mm";
                DateTimeFormatter formatter = DateTimeFormat.forPattern(timePattern);
                String time = formatter.print(dateTime);
                return sBegin+r.getString(R.string.today)+" "+r.getString(R.string.at_time)+" "+time;}
    }

    public void setLastSyncText(String lastSyncText) {
        this.lastSyncText = lastSyncText;
    }
    public String getLastSyncText() {
        return this.lastSyncText;
    }
}

