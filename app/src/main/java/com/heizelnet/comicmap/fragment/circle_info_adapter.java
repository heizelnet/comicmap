package com.heizelnet.comicmap.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.MediaSession;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.heizelnet.comicmap.DataBaseHelper;
import com.heizelnet.comicmap.Logger;
import com.heizelnet.comicmap.LoginSharedPreference;
import com.heizelnet.comicmap.MyApplication;
import com.heizelnet.comicmap.OAuth.APIClient;
import com.heizelnet.comicmap.OAuth.TokenProcess;
import com.heizelnet.comicmap.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class circle_info_adapter extends RecyclerView.Adapter<circle_info_adapter.ItemViewHolder> {

    private ArrayList<circle_instance> mData;
    private Context context;
    private TokenProcess apiInterface;
    private retrofit2.Call<ResponseBody> responseBodyCall;
    private LoginSharedPreference loginSharedPreference = new LoginSharedPreference();
    private SQLiteDatabase mDatabase;
    private CookieManager cookieManager;

    public circle_info_adapter(ArrayList<circle_instance> data) {
        cookieManager = CookieManager.getInstance();
        cookieManager.acceptCookie();
        cookieManager.setAcceptCookie(true);
        this.mData = data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        mDatabase = new DataBaseHelper(context).openDataBase();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.circle_info_item, parent, false);
        circle_info_adapter.ItemViewHolder holder = new circle_info_adapter.ItemViewHolder(view);
        int position = holder.getAdapterPosition();

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        circle_instance data = mData.get(position);
        Resources res = context.getResources();
        holder.textName2.setText(res.getString(R.string.row_Name, data.getName()));
        holder.textAuthor2.setText(res.getString(R.string.row_Author, data.getAuthor()));
        holder.textGenre.setText(res.getString(R.string.row_Genre, data.getgenre()));
        holder.textDay2.setText(res.getString(R.string.row_Day, data.getDay()));
        holder.textCircle.setText(res.getString(R.string.row_Circle, data.getCircle()));



        //Set Author URL Image button
        if(data.isPixivUrl()) {
            holder.button_pixiv.setImageDrawable(context.getResources().getDrawable(R.drawable.pixiv_on_));
            holder.button_pixiv.setOnClickListener(view -> {
                Uri uri = Uri.parse(data.getPixivUrl());
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            });
        } else
            holder.button_pixiv.setImageDrawable(context.getDrawable(R.drawable.pixiv_off_));

        if(data.isTwitterUrl()) {
            holder.button_twitter.setImageDrawable(context.getResources().getDrawable(R.drawable.twitter_on_));
            holder.button_twitter.setOnClickListener(view -> {
                Uri uri = Uri.parse(data.getTwitterUrl());
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            });
        } else
            holder.button_twitter.setImageDrawable(context.getDrawable(R.drawable.twitter_off_));

        if(data.isNicoUrl()) {
            holder.button_nico.setImageDrawable(context.getResources().getDrawable(R.drawable.niconico_on_));
            holder.button_nico.setOnClickListener(view -> {
                Uri uri = Uri.parse(data.getNicoUrl());
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            });
        } else
            holder.button_nico.setImageDrawable(context.getDrawable(R.drawable.niconico_off_));



        //Set favorite or not
        String query_favorite = String.format(Locale.KOREA, "select favorite from circle_info where wid=%d", Integer.parseInt(data.getWid()));
        Cursor cur = mDatabase.rawQuery(query_favorite, null);
        cur.moveToFirst();

        if(cur.getInt(cur.getColumnIndex("favorite")) > 0)
            holder.favorite_button.setImageDrawable(context.getDrawable(R.drawable.favorite_on));
        else
            holder.favorite_button.setImageDrawable(context.getDrawable(R.drawable.favorite_off));
        cur.close();



        //Set Favorite Button Listener
        holder.favorite_button.setOnClickListener(view -> {
            HashMap<String, Object> postData = new HashMap<>();
            TokenProcess apiInterface_ = APIClient.getClient(TokenProcess.API_URL).create(TokenProcess.class);
            retrofit2.Call<ResponseBody> responseBodyCall_;
            Cursor cur_ = mDatabase.rawQuery(query_favorite, null);
            cur_.moveToFirst();

            view.setEnabled(false);
            //Delete favorite
            if(cur_.getInt(cur.getColumnIndex("favorite")) > 0) {
                holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.circular_progress));
                responseBodyCall_ = apiInterface_.deleteFavorite(data.getWid());
                responseBodyCall_.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Logger.e("exploit", response.body().string());
                            String query = String.format(Locale.KOREA, "update circle_info set favorite=%d where wid=%d", 0, Integer.parseInt(data.getWid()));
                            mDatabase.execSQL(query);
                            holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_off));
                            view.setEnabled(true);
                        }catch(Exception e) {
                            e.printStackTrace();
                            holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_on));
                            view.setEnabled(true);
                            Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_on));
                        view.setEnabled(true);
                        Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            //Add favorite
            else {
                holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.circular_progress));
                postData.put("wcid", data.getWid());
                postData.put("color", 4);
                postData.put("memo", "");
                responseBodyCall_ = apiInterface_.addFavorite(postData);
                responseBodyCall_.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Logger.e("exploit", response.body().string());
                            String query = String.format(Locale.KOREA, "update circle_info set favorite=%d where wid=%d", 4, Integer.parseInt(data.getWid()));
                            mDatabase.execSQL(query);
                            holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_on));
                            view.setEnabled(true);
                        } catch(Exception e) {
                            e.printStackTrace();
                            holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_off));
                            view.setEnabled(true);
                            Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        holder.favorite_button.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.favorite_off));
                        view.setEnabled(true);
                        Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            cur_.close();

        });



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
        TextView textName2, textAuthor2, textGenre, textDay2, textCircle;
        ImageView imageView;
        ImageButton button_pixiv, button_twitter, button_nico, favorite_button;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageThumb);
            textName2 = itemView.findViewById(R.id.text_Name2);
            textAuthor2 = itemView.findViewById(R.id.text_Author2);
            textGenre = itemView.findViewById(R.id.text_Genre);
            textDay2 = itemView.findViewById(R.id.text_Day2);
            textCircle = itemView.findViewById(R.id.text_Circle);
            button_pixiv = itemView.findViewById(R.id.imageButton_pixiv);
            button_twitter = itemView.findViewById(R.id.imageButton_twitter);
            button_nico = itemView.findViewById(R.id.imageButton_nico);
            favorite_button = itemView.findViewById(R.id.favorite_circle_button);

        }
    }
}
