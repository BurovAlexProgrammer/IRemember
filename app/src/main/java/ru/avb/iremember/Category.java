package ru.avb.iremember;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by sbm on 29.12.2015.
 */
public class Category {
    String name;
    int iconId;
    Category(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public final class Condition{
        public static final int NOTSELECTED=0, UNIT=1, TIME=2;
    }
}
