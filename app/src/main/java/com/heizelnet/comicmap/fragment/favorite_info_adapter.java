package com.heizelnet.comicmap.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.heizelnet.comicmap.DataBaseHelper;
import com.heizelnet.comicmap.Logger;
import com.heizelnet.comicmap.MyApplication;
import com.heizelnet.comicmap.OAuth.APIClient;
import com.heizelnet.comicmap.OAuth.TokenProcess;
import com.heizelnet.comicmap.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class favorite_info_adapter extends RecyclerView.Adapter<favorite_info_adapter.ItemViewHolder>{

    private ArrayList<favorite_instance> mData;
    private Context context;
    private TokenProcess apiInterface;
    private retrofit2.Call<ResponseBody> responseBodyCall;
    private SQLiteDatabase mDatabase;
    private CookieManager cookieManager;
    private Point size;

    public favorite_info_adapter(ArrayList<favorite_instance> data) {
        cookieManager = CookieManager.getInstance();
        cookieManager.acceptCookie();
        cookieManager.setAcceptCookie(true);
        this.mData = data;
    }

    @NonNull
    @Override
    public favorite_info_adapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        try {
            mDatabase = new DataBaseHelper(context).openDataBase();
        } catch(IOException e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view_ = inflater.inflate(R.layout.favorite_info_item, parent, false);
        favorite_info_adapter.ItemViewHolder holder = new favorite_info_adapter.ItemViewHolder(view_);

        //Set Favorite Button Listener
        holder.favorite_button.setImageDrawable(context.getDrawable(R.drawable.favorite_on));
        holder.favorite_button.setOnClickListener(view -> {
            HashMap<String, Object> postData = new HashMap<>();
            TokenProcess apiInterface_ = APIClient.getClient(TokenProcess.API_URL).create(TokenProcess.class);
            retrofit2.Call<ResponseBody> responseBodyCall_;
            String query_favorite = String.format(Locale.KOREA, "select favorite from circle_info where wid=%d", Integer.parseInt(mData.get(holder.getAdapterPosition()).getWid()));
            Cursor cur_ = mDatabase.rawQuery(query_favorite, null);
            cur_.moveToFirst();

            view.setEnabled(false);
            int position = holder.getAdapterPosition();
            Logger.e("exploit", "position : " + position);
            //Delete favorite
            if(cur_.getInt(cur_.getColumnIndex("favorite")) > 0) {
                AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_circle);
                holder.favorite_button.setImageDrawable(animatedVectorDrawableCompat);
                animatedVectorDrawableCompat.start();
                responseBodyCall_ = apiInterface_.deleteFavorite(mData.get(holder.getAdapterPosition()).getWid());
                responseBodyCall_.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Logger.e("exploit", response.body().string());
                            String query = String.format(Locale.KOREA, "update circle_info set favorite=%d where wid=%d", 0, Integer.parseInt(mData.get(holder.getAdapterPosition()).getWid()));
                            mDatabase.execSQL(query);
                            animatedVectorDrawableCompat.stop();
                            holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_off));
                            view.setEnabled(true);
                            Toast.makeText(context, "DELETED!", Toast.LENGTH_SHORT).show();
                        }catch(Exception e) {
                            e.printStackTrace();
                            animatedVectorDrawableCompat.stop();
                            holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_on));
                            view.setEnabled(true);
                            Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        animatedVectorDrawableCompat.stop();
                        holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_on));
                        view.setEnabled(true);
                        Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            //Add favorite
            else {
                AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_circle);
                holder.favorite_button.setImageDrawable(animatedVectorDrawableCompat);
                animatedVectorDrawableCompat.start();
                postData.put("wcid", mData.get(holder.getAdapterPosition()).getWid());
                postData.put("color", 4);
                postData.put("memo", "");
                responseBodyCall_ = apiInterface_.addFavorite(postData);
                responseBodyCall_.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Logger.e("exploit", response.body().string());
                            String query = String.format(Locale.KOREA, "update circle_info set favorite=%d where wid=%d", 4, Integer.parseInt(mData.get(holder.getAdapterPosition()).getWid()));
                            mDatabase.execSQL(query);
                            animatedVectorDrawableCompat.stop();
                            holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_on));
                            view.setEnabled(true);
                            Toast.makeText(context, "ADDED!", Toast.LENGTH_SHORT).show();
                        } catch(Exception e) {
                            e.printStackTrace();
                            animatedVectorDrawableCompat.stop();
                            holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_off));
                            view.setEnabled(true);
                            Toast.makeText(context, "Exception..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        animatedVectorDrawableCompat.stop();
                        holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_off));
                        view.setEnabled(true);
                        Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            cur_.close();

        });

        //Set URL Button Listener
        holder.button_pixiv.setOnClickListener(view -> {
            Uri uri = Uri.parse(mData.get(holder.getAdapterPosition()).getPixivUrl());
            if(mData.get(holder.getAdapterPosition()).isPixivUrl())
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });
        holder.button_twitter.setOnClickListener(view -> {
            Uri uri = Uri.parse(mData.get(holder.getAdapterPosition()).getTwitterUrl());
            if(mData.get(holder.getAdapterPosition()).isTwitterUrl())
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });
        holder.button_nico.setOnClickListener(view -> {
            Uri uri = Uri.parse(mData.get(holder.getAdapterPosition()).getNicoUrl());
            if(mData.get(holder.getAdapterPosition()).isNicoUrl())
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });

        //Set EditButton with dialog



        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorite_info_dialog dialog = new favorite_info_dialog(context, holder, Integer.parseInt(mData.get(holder.getAdapterPosition()).getWid()), mDatabase);
                dialog.show();

                //Set Dialog Layout size
                Window window = dialog.getWindow();
                window.setLayout((int)(size.x * 0.95f), ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull favorite_info_adapter.ItemViewHolder holder, int position) {

        favorite_instance data = mData.get(position);
        Resources res = context.getResources();
        holder.textName2.setText(res.getString(R.string.row_Name, data.getName()));
        holder.textAuthor2.setText(res.getString(R.string.row_Author, data.getAuthor()));
        holder.textGenre.setText(res.getString(R.string.row_Genre, data.getgenre()));
        holder.textDay2.setText(res.getString(R.string.row_Day, data.getDay()));
        holder.textCircle.setText(res.getString(R.string.row_Circle, data.getCircle()));

        String query_favorite = String.format(Locale.KOREA, "select favorite from circle_info where wid=%d", Integer.parseInt(data.getWid()));
        Cursor cur_ = mDatabase.rawQuery(query_favorite, null);
        cur_.moveToFirst();
        Logger.e("exploit", "color code : " + cur_.getInt(cur_.getColumnIndex("favorite")));

        holder.view.setBackgroundColor(context.getResources().getColor(getColor(cur_.getInt(cur_.getColumnIndex("favorite")))));
        cur_.close();

        //Set Author URL Image button
        if(data.isPixivUrl())
            holder.button_pixiv.setImageDrawable(context.getResources().getDrawable(R.drawable.pixiv_on_));
        else
            holder.button_pixiv.setImageDrawable(context.getDrawable(R.drawable.pixiv_off_));

        if(data.isTwitterUrl())
            holder.button_twitter.setImageDrawable(context.getResources().getDrawable(R.drawable.twitter_on_));
        else
            holder.button_twitter.setImageDrawable(context.getDrawable(R.drawable.twitter_off_));

        if(data.isNicoUrl())
            holder.button_nico.setImageDrawable(context.getResources().getDrawable(R.drawable.niconico_on_));
        else
            holder.button_nico.setImageDrawable(context.getDrawable(R.drawable.niconico_off_));

        if(data.getMemo() != null)
            holder.textMemo.setText(data.getMemo());

        //Circle Cut Image Set
        cookieManager.acceptCookie();
        cookieManager.setAcceptCookie(true);
        apiInterface = APIClient.getClient(TokenProcess.CATALOG_URL).create(TokenProcess.class);
        responseBodyCall = apiInterface.getDetailJson(cookieManager.getCookie("https://webcatalog.circle.ms/"),data.getWid());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String url = response.body().string().split("\"CircleCutUrl\":\"")[1].split("\",\"")[0];
                    Glide.with(holder.itemView.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.info_icon).dontAnimate().into(holder.imageView);
                } catch (Exception e) {
                    Glide.with(holder.itemView.getContext()).load(R.drawable.info_icon).into(holder.imageView);
                    //Log.e("exploit", "exception.. fail load image.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Glide.with(holder.itemView.getContext()).load(R.drawable.info_icon).into(holder.imageView);
                //Log.e("exploit", "exception.. Network Connect fail..");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textName2, textAuthor2, textGenre, textDay2, textCircle, textMemo;
        ImageView imageView;
        ImageButton button_pixiv, button_twitter, button_nico, favorite_button, edit_button;
        View view;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageThumb_fv);
            textName2 = itemView.findViewById(R.id.text_Name2_fv);
            textAuthor2 = itemView.findViewById(R.id.text_Author2_fv);
            textGenre = itemView.findViewById(R.id.text_Genre_fv);
            textDay2 = itemView.findViewById(R.id.text_Day2_fv);
            textCircle = itemView.findViewById(R.id.text_Circle_fv);
            button_pixiv = itemView.findViewById(R.id.imageButton_pixiv_fv);
            button_twitter = itemView.findViewById(R.id.imageButton_twitter_fv);
            button_nico = itemView.findViewById(R.id.imageButton_nico_fv);
            favorite_button = itemView.findViewById(R.id.favorite_circle_button_fv);
            edit_button = itemView.findViewById(R.id.favorite_edit_button_fv);
            view = itemView.findViewById(R.id.favorite_color_border);
            textMemo = itemView.findViewById(R.id.textView_memo);

        }
    }

    public int getColor(int favorite) {
        int color = 0;
        switch (favorite){
            case 1:
                color = R.color.circle_color1;
                break;
            case 2:
                color = R.color.circle_color2;
                break;
            case 3:
                color = R.color.circle_color3;
                break;
            case 4:
                color = R.color.circle_color4;
                break;
            case 5:
                color = R.color.circle_color5;
                break;
            case 6:
                color = R.color.circle_color6;
                break;
            case 7:
                color =R.color.circle_color7;
                break;
            case 8:
                color = R.color.circle_color8;
                break;
            case 9:
                color = R.color.circle_color9;
                break;
        }
        return color;
    }

}
