package ru.avb.iremember;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivityCopy extends AppCompatActivity implements DialogOkCancel.OnCompleteListener {
    public DialogFragment dialogNeedRestart;
    public RecyclerView recycler;
    public RecyclerView.LayoutManager layoutManager;
    public LanguageAdapter languageAdapter;
    public List<Language> languageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_activity);

        recycler = (RecyclerView)findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        languageList = new ArrayList<>();
        languageList.add(new Language("ru", R.string.languageRuLabel, R.mipmap.locale_ru));
        languageList.add(new Language("default", R.string.defaultLabel, R.drawable.locale_default));
        languageList.add(new Language("en", R.string.languageEnLabel, R.mipmap.locale_en));

        languageAdapter = new LanguageAdapter(languageList);
        recycler.setAdapter(languageAdapter);

        Button buttonOk = (Button)findViewById(R.id.buttonOk);
        Button buttonCancel = (Button)findViewById(R.id.buttonCancel);
        View.OnClickListener onClickButtons = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.buttonOk) ask();//confirm();
                if (v.getId()==R.id.buttonCancel) cancel();
            }
        };
        buttonOk.setOnClickListener(onClickButtons);
        buttonCancel.setOnClickListener(onClickButtons);

        dialogNeedRestart = new DialogOkCancel();
    }

    //==============================CATEGORY ADAPTER====================================
    public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyHolder> {
        public boolean initialized = false;
        public Context thisContext;
        public String selectedLocale;
        public LinearLayout lastSelectedLayout;
        public int selectedItemBackgroundId, itemBackgroundId;

        int languageCount = 0;
        private List<Language> languages;

        public LanguageAdapter(List<Language> languages) {
            this.languages = languages;
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            CardView card;
            LinearLayout layout;
            ImageView icon;
            TextView name;

            MyHolder(View v) {
                super(v);
                card = (CardView) v.findViewById(R.id.category_card);
                layout = (LinearLayout)v.findViewById(R.id.card_layout);
                icon = (ImageView) v.findViewById(R.id.category_icon);
                name = (TextView) v.findViewById(R.id.category_name);
            }
        }

        //Длина списка
        @Override
        public int getItemCount() {
            languageCount = languages.size();
            return (languageCount);
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            G.Log("Language.onCreateVHolder..");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_language, parent, false);
            MyHolder mh = new MyHolder(v);
            return mh;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            G.Log("onAttachedToRecyclerView()");
        }

        //Отбор
        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            G.Log("Category.onBind..");
/*
            if (!initialized) {
                G.Log("Recycler initialization");
                thisContext = holder.icon.getContext();
                itemBackgroundId = getColorIdFromAttr(thisContext, R.attr.colorCardview);
                G.Log("Color itemBgr: "+itemBackgroundId);
                selectedItemBackgroundId =getColorIdFromAttr(thisContext, R.attr.colorCardviewSelected);
                G.Log("Color itemSelectedBgr: "+selectedItemBackgroundId);
                initialized = true;
            }
            holder.icon.setImageResource(languages.get(position).iconId);
            holder.name.setText(getResources().getString(languages.get(position).labelId));
            holder.layout.setBackgroundColor(getColorIdFromAttr(thisContext, R.attr.colorCardview));
            View.OnClickListener onClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    selectCardItem(holder, position);
                    selectedLocale = languages.get(position).locale.toString();
                    G.Log("Selected locale : '"+selectedLocale+"'");
                }
            };

            holder.layout.setOnClickListener(onClick);
            */
        }

        @Override
        public void onViewRecycled(MyHolder holder) {
            super.onViewRecycled(holder);
            G.Log("onViewRecycler()");
        }
    }
    //=========================================================================================
    public class Language {
        Locale locale;
        int labelId;
        int iconId;
        Language(String locale, int labelId, int iconId) {
            this.locale = new Locale(locale);
            this.labelId = labelId;
            this.iconId = iconId;
        }
    }

    private void selectCardItem(LanguageAdapter.MyHolder holder,int position) {
        G.Log("selectCardItem(position="+position+")..");
        if (languageAdapter.lastSelectedLayout != null) {
            G.Log("lastSelectedItem is exist");
            languageAdapter.lastSelectedLayout.setBackgroundColor(languageAdapter.itemBackgroundId);
        }
        holder.layout.setBackgroundColor(languageAdapter.selectedItemBackgroundId);
        languageAdapter.lastSelectedLayout = holder.layout;
    }

    public int getColorIdFromAttr(Context context, int attr) {
        int[] attrs = new int[] {attr};
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs);
        return a.getColor(0, getResources().getColor(R.color.colorCardview_onException));
    }

    public void confirm() {
        G.Log("Save locale: = "+languageAdapter.selectedLocale);
        Options.initPreferences(this);
        Options.prefEditor.putString(Options.KEY_LOCALE, languageAdapter.selectedLocale);
        Options.prefEditor.commit();
        G.Log("Load locale: = "+Options.sharedPref.getString(Options.KEY_LOCALE, "none"));

        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        LanguageActivityCopy.this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void cancel() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void ask() {
        dialogNeedRestart.show(getFragmentManager(), G.Tag.NEED_RESTART);
    }

    @Override
    public void onCompleteDialog(Bundle bundle) {
//        String request = bundle.getString(G.KEY_REQUEST);
//        String tag = bundle.getString(G.KEY_TAG);
//        if (request == G.REQUEST_NEED_RESTART) {
//            int result = bundle.getInt(G.KEY_RESULT);
//            G.Log("From Dialog tag: '"+tag+"'. Result="+result);
//            if (result == G.Result.OK) {confirm();}
//            if (result == G.Result.CANCEL) {cancel();}
//        }
    }
}
