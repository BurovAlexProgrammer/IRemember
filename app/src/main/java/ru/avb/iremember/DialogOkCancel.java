package ru.avb.iremember;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Алекс on 25.05.2016.
 */
public class DialogOkCancel extends DialogFragment{
    public String dialogTag = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogTag = this.getTag();
        G.Log("DialogOkCancel tag:'"+dialogTag+"'");
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_ok_cancel, null);
        if (dialogTag == G.Tag.NEED_RESTART) {
            getDialog().setTitle(R.string.changeLanguageNeedRestart_title);
            //((TextView)v.findViewById(R.id.title)).setText(R.string.changeLanguageNeedRestart_title);
            ((TextView)v.findViewById(R.id.message)).setText(R.string.changeLanguageNeedRestart_descript);
            Button buttonOk = (Button)v.findViewById(R.id.buttonOk);
            buttonOk.setText(R.string.restart);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putInt(G.KEY_REQUEST, G.Request.NEED_RESTART);
                    args.putInt(G.KEY_RESULT, G.Result.OK);
                    args.putString(G.KEY_TAG, G.Tag.NEED_RESTART);
                    completeListener.onCompleteDialog(args);
                    dismiss();
                }
            });
            v.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putInt(G.KEY_REQUEST, G.Request.NEED_RESTART);
                    args.putInt(G.KEY_RESULT, G.Result.CANCEL);
                    args.putString(G.KEY_TAG, G.Tag.NEED_RESTART);
                    completeListener.onCompleteDialog(args);
                    dismiss();
                }
            });
        }
        return v;
    }

    public static interface OnCompleteListener {
        public abstract void onCompleteDialog(Bundle bundle);
    }
    private OnCompleteListener completeListener;

    // make sure the Activity implemented it
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.completeListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }


}
