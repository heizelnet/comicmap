package com.example.comicmap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class permission_check_dialog extends Dialog {

    private Fragment fragment;

    public permission_check_dialog(@NonNull Context context) {
        super(context);
    }
    public permission_check_dialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public permission_check_dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public permission_check_dialog(Fragment fragment) {
        super(fragment.getContext());
        this.fragment = fragment;

        setCancelable( false );
        setCanceledOnTouchOutside( false );


        Window window = getWindow();
        if( window != null ) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes(params);
            window.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.permission_check_layout);

    }

}

