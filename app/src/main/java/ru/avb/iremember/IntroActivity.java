package ru.avb.iremember;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import ru.avb.iremember.fragments.First;
import ru.avb.iremember.fragments.FragmentAccount;
import ru.avb.iremember.fragments.Second;
import ru.avb.iremember.fragments.Third;

/**
 * Created by Alex on 01.12.2016.
 */

public class IntroActivity extends AppIntro implements First.OnFragmentInteractionListener, Second.OnFragmentInteractionListener, Third.OnFragmentInteractionListener {
    First firstFragment = new First();
    Second secondFragment = new Second();
    Third thirdFragment = new Third();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(firstFragment);
        addSlide(secondFragment);
        addSlide(thirdFragment);

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setSkipText(getResources().getString(R.string.skip));
        setDoneText(getString(R.string.done));
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent();
        setResult(G.Result.CANCEL, intent);
        this.finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent();
        setResult(G.Result.OK, intent);
        this.finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
        try {
            if (newFragment==secondFragment) {}//Do something with newFragment
        } catch (Exception e) {G.LogException(e);}
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
