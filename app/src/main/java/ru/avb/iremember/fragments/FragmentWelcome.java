package ru.avb.iremember.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.avb.iremember.Category;
import ru.avb.iremember.G;
import ru.avb.iremember.Google;
import ru.avb.iremember.HomeActivity;
import ru.avb.iremember.LanguageActivity;
import ru.avb.iremember.Options;
import ru.avb.iremember.R;
import ru.avb.iremember.WelcomeActivity;

public class FragmentWelcome extends Fragment {

    private HomeActivity parent;
    private View root;

    private OnFragmentInteractionListener mListener;


    Button buttonOk;
    Button buttonSignIn;
    Button buttonSignOut;
    Button buttonLanguage;
    Button buttonCancel;

    public FragmentWelcome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = ((HomeActivity)getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_welcome,null);
        G.Log("================WELCOME ACTIVITY=================");

        buttonOk = (Button) root.findViewById(R.id.buttonOk);
        buttonCancel = (Button) root.findViewById(R.id.buttonCancel);
        buttonLanguage = (Button) root.findViewById(R.id.buttonLanguage);
        buttonSignOut = (Button) root.findViewById(R.id.button_sign_out);
        buttonSignIn = (Button) root.findViewById(R.id.button_sign_in);

        updateLangButton();

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.buttonOk) {
                    confirm();
                    parent.goToFragment(G.homeFragment, G.homeFragmentTitle);
                }
                if (v.getId() == R.id.buttonCancel) {
                    parent.goToFragment(G.homeFragment, G.homeFragmentTitle);
                }
                if (v.getId() == R.id.buttonLanguage) {
                    Intent intent = new Intent(parent, LanguageActivity.class);
                    startActivityForResult(intent, G.Request.CHANGE_LANGUAGE);
                }
                if (v.getId() == R.id.button_sign_in) parent.signIn();
                if (v.getId() == R.id.button_sign_out) parent.signOut();

            }
        };

        buttonOk.setOnClickListener(onClick);
        buttonCancel.setOnClickListener(onClick);
        buttonLanguage.setOnClickListener(onClick);
        buttonSignIn.setOnClickListener(onClick);
        buttonSignOut.setOnClickListener(onClick);
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }

    public void updateUI() {
        boolean connected = Google.SingIn.getSuccess();//Google.lastSignInResult.isSuccess();
        G.Log("updateUI()");
        if (connected) {
            buttonSignIn.setVisibility(View.INVISIBLE);
            buttonSignOut.setVisibility(View.VISIBLE);
            root.findViewById(R.id.signInGoogle_description).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.youAreSignedInAs).setVisibility(View.VISIBLE);
            root.findViewById(R.id.nameAndEmail).setVisibility(View.VISIBLE);
            ((TextView)root.findViewById(R.id.nameAndEmail)).setText(G.user.getDisplayName()+ "  (" +G.user.getEmail()+ ")" );
        }
        else {
            buttonSignIn.setVisibility(View.VISIBLE);
            buttonSignOut.setVisibility(View.INVISIBLE);
            root.findViewById(R.id.signInGoogle_description).setVisibility(View.VISIBLE);
            root.findViewById(R.id.youAreSignedInAs).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.nameAndEmail).setVisibility(View.INVISIBLE);
        }
        //
    }

    public void updateLangButton() {
        G.Log("updateLangButton()");
        G.Log("locale: "+Options.locale);
        int imageId = R.drawable.locale_default;
        switch (Options.locale) {
            case "default": imageId=R.drawable.locale_default; break;
            case "ru": imageId=R.mipmap.locale_ru; break;
            case "en": imageId=R.mipmap.locale_en; break;

        }
        //Drawable image = getResources().getDrawable( imageId );
        Drawable image = ResourcesCompat.getDrawable(getResources(), imageId, null);
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds( 0, 0, w-5, h-5 );
        //Drawable image = getResources().getDrawable(imageId);
        //((Button)findViewById(R.id.buttonLanguage)).setCompoundDrawablesWithIntrinsicBounds(image, null, null, null);
        buttonLanguage.setCompoundDrawables( image, null, null, null );

    }

    public void confirm() {
        Options.writeOption(Options.KEY_NEED_WELCOME, false);
    }
}
