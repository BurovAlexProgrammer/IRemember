package ru.avb.iremember;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        G.Log("================MAIN ACTIVITY=================");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Options.initializeOptions(this);

        if (Options.readOption(Options.KEY_NEED_WELCOME, true)) {
            //Run Welcome activity
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivityForResult(intent, G.REQUEST_WELCOME_FROM_MAIN);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        List<Category> categories;
        categories = new ArrayList<>();
        categories.add(new Category("Авто", R.mipmap.flag_usa));
        categories.add(new Category("Вода", R.mipmap.flag_usa));

        CategoryAdapter cAdapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(cAdapter);
        //---------------------------
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        G.Log("MainActivity.onActivityResult..");
        //Welcome request
        if (requestCode==G.REQUEST_WELCOME_FROM_MAIN) {
            G.Log("ActivityResult from Welcome.Activity");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        G.Log("MainActivity.onResume()..");
    }

    @Override
    protected void onStart() {
        super.onStart();
        G.Log("MainActivity.onStart()..");
        Options.initializeOptions(this);
        G.user.logData();
    }

    //==============================CATEGORY ADAPTER====================================
    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {
        int categoryCount = 0;
        private List<Category> categories;

        CategoryAdapter(List<Category> categories) {
            this.categories = categories;
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            CardView card;
            ImageView icon;
            TextView name;

            MyHolder(View v) {
                super(v);
                card = (CardView) v.findViewById(R.id.category_card);
                icon = (ImageView) v.findViewById(R.id.category_icon);
                name = (TextView) v.findViewById(R.id.category_name);
            }
        }

        //Длина списка
        @Override
        public int getItemCount() {
            categoryCount = categories.size();
            return (categoryCount + 1); //с учетом "Добавить новый элемент"
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //G.Log("Category.onCreateVHolder..");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false);
            MyHolder mh = new MyHolder(v);
            return mh;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        //Отбор
        @Override
        public void onBindViewHolder(final MyHolder holder, int position) {
            //G.Log("Category.onBind..");

            if (position < categoryCount) {
                holder.icon.setImageResource(R.mipmap.flag_usa);
            }
            if (position == categoryCount) {
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        G.Log("AddNewCategory..");
                    }
                });
                holder.icon.setImageResource(R.mipmap.flag_usa);
                holder.name.setText("asdf");
            }
        }
    }
    //=========================================================================================

}
