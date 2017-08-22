package ru.avb.iremember.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import ru.avb.iremember.Category;
import ru.avb.iremember.DB;
import ru.avb.iremember.G;
import ru.avb.iremember.HomeActivity;
import ru.avb.iremember.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCategories.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCategories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCategories extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    HomeActivity thisActivity;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentCategories() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCategories.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCategories newInstance(String param1, String param2) {
        FragmentCategories fragment = new FragmentCategories();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        DB.setDbName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisActivity = (HomeActivity) inflater.getContext();
        //((HomeActivity)thisActivity)
        //TODO must be on updateUI
        //thisActivity.faButton.setVisibility(View.VISIBLE);

        View v = inflater.inflate(R.layout.fragment_categories,null);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.main_recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        List<Category> categories;
        categories = new ArrayList<>();
        //categories.add(new Category("Авто", R.mipmap.flag_usa));
        //categories.add(new Category("Вода", R.mipmap.flag_usa));

        CategoryAdapter cAdapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(cAdapter);
        thisActivity.updateUI();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
}
