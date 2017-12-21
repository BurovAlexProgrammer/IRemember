package ru.avb.iremember;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class Category {
    int id = NO_SPECIFIED;
    String label;
    int condition = Condition.NOTSELECTED;
    int iconId = NO_SPECIFIED;
    int parentCategory = NO_SPECIFIED;
    int orderNumber = NO_SPECIFIED;
    DateTime dateCreated;
    DateTime dateModified;
    int initialValue = NO_SPECIFIED;
    int finalValue = NO_SPECIFIED;
    boolean finalValueEnabled;
    boolean negativeValueEnabled;
    boolean predictionEnabled;
    int predictionPeriod = NO_SPECIFIED;
    int everageValue =NO_SPECIFIED;
    boolean everageValueCalculateEnabled;
    int everageValueCalculateEventcount = NO_SPECIFIED;
    boolean favoriteEnabled;
    int favoriteOrderNumber =NO_SPECIFIED;
    String unitLabel = "";

    public static final int NO_CATEGORY = -999999999, NO_SPECIFIED = -999999998;

    public static final class Condition{
        public static final int NOTSELECTED=0, UNIT=1, DATE=2, DATETIME=3;
        public static final String[] labels = {"Not selected", "unit", "date", "date time"};
    }
}
