package ru.avb.iremember;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.zip.Inflater;

/**
 * Created by Alex on 06.12.2016.
 */

public class testActivity extends Activity {
    String[] data = {"a","b","c","d","e","f","g","h"};
    int space;
    GridView grid;
    //ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gallery);

        printSecreenInfo();
        G.Log("----- Density: "+getDensityBucket(getResources()));
        space = getResources().getDisplayMetrics().densityDpi/12;
        //adapter = new ArrayAdapter<String>(this, R.layout.item_icon_gallery, R.id.textView, data);

        grid = (GridView)findViewById(R.id.grid);
        grid.setAdapter(new ImageAdapter(this));
        adjustGridView();
        //G.Log("Color count:"+);
    }

    private void adjustGridView() {
        //grid.setNumColumns(GridView.AUTO_FIT);
        //grid.setColumnWidth(50);
        //grid.setVerticalSpacing(8);
        //grid.setHorizontalSpacing(8);
        grid.setNumColumns(7);
        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
    }




    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return matColorsInt.length ;
            //return categoryIconIDs.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
            //return matColors.getColor(position, Color.BLACK);
            //return categoryIconIDs[position];
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            //LinearLayout cardView = (LinearLayout)findViewById(R.id.root);

            //G.Log(cardView.toString());
            ImageView imageView;
            if (parent!=null) parent.setPadding(space,0,space,space);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_icon_gallery, parent, false);
                ImageView img = (ImageView)convertView.findViewById(R.id.image);
            }

            ImageView img = (ImageView)convertView.findViewById(R.id.image);
            TextView text = (TextView)convertView.findViewById(R.id.text);

            text.setText("");
            //img.setLayoutParams();
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //img.setBackgroundColor(matColorsInt[position]);

            Random random=new Random();
            int currColor = Color.parseColor(matColors.getString(position));
            ColorFilter cf = new PorterDuffColorFilter(currColor, PorterDuff.Mode.MULTIPLY);
            img.setImageResource(R.drawable.cat_bus);
            img.setColorFilter(cf);

            //imageView.setImageResource(categoryIconIDs[position]);
            return convertView;
        }

        // references to our images
        private Integer[] categoryIconIDs = {
                R.drawable.cat_bus, R.drawable.cat_drop,
                R.drawable.cat_health, R.drawable.cat_home,
                R.drawable.cat_jeep, R.drawable.cat_mechanic,
                R.drawable.cat_minivan, R.drawable.cat_pass_car,
                R.drawable.cat_shopping, R.drawable.cat_tablets,
                R.drawable.cat_test_tube, R.drawable.cat_timer,
                R.drawable.cat_truck
        };

        private TypedArray matColors = getResources().obtainTypedArray(R.array.mat_color_array);
        int[] matColorsInt = getResources().getIntArray(R.array.mat_color_array);
    }

    void printSecreenInfo(){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        G.Log("density :" +  metrics.density);
        // density interms of dpi
        G.Log("D density :" +  metrics.densityDpi);
        // horizontal pixel resolution
        G.Log("width pix :" +  metrics.widthPixels);
        // actual horizontal dpi
        G.Log("xdpi :" +  metrics.xdpi);
        // actual vertical dpi
        G.Log("ydpi :" +  metrics.ydpi);
    }

    public static String getDensityBucket(Resources resources) {
        switch (resources.getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return "unknown";
        }
    }
}
