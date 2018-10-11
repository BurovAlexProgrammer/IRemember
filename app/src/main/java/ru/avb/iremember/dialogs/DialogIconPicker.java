package ru.avb.iremember.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import ru.avb.iremember.G;
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
        tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.iconPicker_tabIcon));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.iconPicker_tabColor));
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTabByTag("tag1");

        ((TextView)v.findViewById(android.R.id.title)).setText("CHOOSE YOUR ICON!!");

/*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_icon, R.id.imageView, data);


        GridView gridView = (GridView) v.findViewById(R.id.icon_gridView);

        ImageView imageView = new ImageView(getActivity());

        imageView.setImageResource(R.drawable.cat_minivan);
        //gridView.addView(imageView);

        gridView.setAdapter(adapter);
  */
        GridView gridView = (GridView) v.findViewById(R.id.icon_gridView);
        gridView.setAdapter(new ImageAdapter(v.getContext()));
    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return G.categoryIconIds.length;
        }

        public Object getItem(int position) {
            //return Resources.getSystem().getDrawable(G.categoryIconIds[position]);
            return G.categoryIconIds[position];
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            //if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setImageResource(G.categoryIconIds[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
                imageView.setPadding(4, 4, 4, 4);


            //imageView.setImageResource(G.categoryIconIds[position]);
            return imageView;
        }
    }


}
