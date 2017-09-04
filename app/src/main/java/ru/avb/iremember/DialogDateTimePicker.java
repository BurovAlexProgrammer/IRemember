package ru.avb.iremember;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Alex on 04.09.2017.
 */

public class DialogDateTimePicker extends DialogFragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_date_time_picker, null);
        v.findViewById(R.id.button_ok).setOnClickListener(this);
        v.findViewById(R.id.button_cancel).setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View view) {
        
    }
}
