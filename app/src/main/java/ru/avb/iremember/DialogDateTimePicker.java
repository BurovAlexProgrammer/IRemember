package ru.avb.iremember;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;

/**
 * Created by Alex on 04.09.2017.
 */

public class DialogDateTimePicker extends DialogFragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_date_time_picker, null);
        //v.findViewById(R.id.button_ok).setOnClickListener(this);
        //v.findViewById(R.id.button_cancel).setOnClickListener(this);
        initViews(v);
        return v;
    }


    @Override
    public void onClick(View view) {
        
    }

    private void initViews(View v) {
        TabHost tabHost = v.findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Вкладка один");
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Вкладка 2");
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Nhbfasf");
        tabSpec.setContent(R.id.tab3);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag2");

    }
}
