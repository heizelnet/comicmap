package com.heizelnet.comicmap.fragment;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.heizelnet.comicmap.OAuth.APIClient;
import com.heizelnet.comicmap.OAuth.TokenProcess;
import com.heizelnet.comicmap.R;

import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class favorite_info_dialog extends Dialog {
    private Button update_button;
    private RadioGroup radioGroup;
    private EditText editText;
    private favorite_info_adapter.ItemViewHolder holder;
    private TokenProcess apiInterface;
    private retrofit2.Call<ResponseBody> responseBodyCall;
    private int wcid;
    private SQLiteDatabase mDatabase;
    private int favorite_color;

    public favorite_info_dialog(Context context, int themeResId) { super(context, themeResId); }
    public favorite_info_dialog(Context context, boolean cancelable, OnCancelListener cancelListener) { super(context, cancelable, cancelListener); }
    public favorite_info_dialog(@NonNull Context context, @NonNull favorite_info_adapter.ItemViewHolder holder, int wcid, SQLiteDatabase mDatabase) {
        super(context);
        this.holder = holder;
        this.wcid = wcid;
        this.mDatabase = mDatabase;
        setCancelable( true );
        setCanceledOnTouchOutside( true );

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
        setContentView(R.layout.favorite_add_item);
        favorite_color = 4;

        update_button = findViewById(R.id.favorite_add_button);
        radioGroup = findViewById(R.id.radioGroup);
        editText = findViewById(R.id.editText_fv);
        
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> favorite_color = getRadioColor(i)
        );
        
        update_button.setOnClickListener(view -> {
            setCancelable( false );
            setCanceledOnTouchOutside( false );
            view.setEnabled(false);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("wcid", wcid);
            hashMap.put("color", favorite_color);
            hashMap.put("memo", editText.getText());
            apiInterface = APIClient.getClient(TokenProcess.API_URL).create(TokenProcess.class);
            responseBodyCall = apiInterface.updateFavorite(hashMap);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String memo = String.valueOf(editText.getText());
                    memo = memo.replace("\"", "").replace("\'", "");
                    String query = String.format(Locale.KOREA, "update circle_info set favorite=%d, memo='%s' where wid=%d", favorite_color, memo, wcid);
                    mDatabase.execSQL(query);
                    Toast.makeText(getContext(), "Update!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    setCancelable( true );
                    setCanceledOnTouchOutside( true );
                    view.setEnabled(true);
                    Toast.makeText(getContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    public int getRadioColor(int res) {
        int color = 0;
        
        if(res == R.id.radioButton)
            color = 1;
        else if(res == R.id.radioButton2)
            color = 2;
        else if(res == R.id.radioButton3)
            color = 3;
        else if(res == R.id.radioButton4)
            color = 4;
        else if(res == R.id.radioButton5)
            color = 5;
        else if(res == R.id.radioButton6)
            color = 6;
        else if(res == R.id.radioButton7)
            color = 7;
        else if(res == R.id.radioButton8)
            color = 8;
        else if(res == R.id.radioButton9)
            color = 9;

        return color;
    }

}
