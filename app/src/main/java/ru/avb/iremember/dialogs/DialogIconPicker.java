package ru.avb.iremember.dialogs;

import android.app.DialogFragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;

import ru.avb.iremember.R;

/**
 * Created by Alex on 03.10.2017.
 */

public class DialogIconPicker extends DialogFragment  {
    TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_icon_picker, null);
        initViews(v);
        return v;
    }

    void initViews(View v) {
        tabHost = v.findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Icon");
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag1");

        String[] data = {"1","2","3","4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_icon, R.id.imageView, data);
        GridView gridView = v.findViewById(R.id.gridView);
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.drawable.cat_minivan);
        gridView.addView(imageView);
//        gridView.setAdapter(adapter);
    }
}
