package ru.avb.iremember;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UITestActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Options.currentTheme = Options.spReadTheme(this);
        Options.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uitest);

        findViewById(R.id.button_theme1).setOnClickListener(this);
        findViewById(R.id.button_theme2).setOnClickListener(this);
        findViewById(R.id.button_theme3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button_theme1) Options.applyTheme(this, Options.THEME_DEFAULT);
        if (v.getId()==R.id.button_theme2) Options.applyTheme(this, Options.THEME_BLACK);
        if (v.getId()==R.id.button_theme3) Options.applyTheme(this, Options.THEME_GRAY);
    }
}
