package com.example.comicmap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class fragment_map extends Fragment {

    private PhotoView photoView;
    private Bitmap bitmap;
    private Canvas canvas;
    private BitmapDrawable drawable;
    private ImageButton imageButton;
    private Paint paint;
    private info_adapter adapter = new info_adapter();
    private circle_info_dialog dialog;
    private SQLiteDatabase mDataBase;
    private boolean toggle;
    private int map_width, map_height;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mDataBase = new DataBaseHelper(getContext()).openDataBase();

        toggle = false;
        imageButton = view.findViewById(R.id.AnimationButton);
        photoView = view.findViewById(R.id.photo_view);
        photoView.setMaximumScale(6.0f);

        //Set Map with bitmap layer
        map_width = getResources().getInteger(R.integer.west_34_width);
        map_height = getResources().getInteger(R.integer.west_34_height);
        drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.west_34);
        bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(120, 164, 198, 57));
        photoView.setImageBitmap(bitmap);

        //Set TapListener
        photoView.setOnPhotoTapListener(new PhotoTapListener());
        photoView.setOnLongClickListener(view1 -> {
            Log.e("exploit", "Long clicked");
            return false;
        });

        imageButton.setOnClickListener(v -> {
            if(!toggle) {
                imageButton.setImageDrawable(getContext().getDrawable(R.drawable.fill_heart));
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageButton.getDrawable();
                animatedVectorDrawable.start();

                Log.e("exploit", "Favorite Function Activated");
                //Test code for favorite function
                canvas.drawCircle(bitmap.getWidth() * (float)0.86, bitmap.getHeight() * (float)0.65, (float)22.5, paint);
                canvas.drawCircle(bitmap.getWidth() * (float)0.68, bitmap.getHeight() * (float)0.56, (float)22.5, paint);
                canvas.drawCircle(bitmap.getWidth() * (float)0.35, bitmap.getHeight() * (float)0.27, (float)22.5, paint);
                canvas.drawCircle(bitmap.getWidth() * (float)0.72, bitmap.getHeight() * (float)0.53, (float)22.5, paint);
                canvas.drawCircle(bitmap.getWidth() * (float)0.53, bitmap.getHeight() * (float)0.72, (float)22.5, paint);
                photoView.invalidate();
                toggle = true;

            } else {
                imageButton.setImageDrawable(getContext().getDrawable(R.drawable.unfill_heart));
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageButton.getDrawable();
                animatedVectorDrawable.start();
                drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.west_34);
                bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                canvas.drawBitmap(bitmap, 0, 0, null);
                photoView.invalidate();
                toggle = false;
            }
        });
        return view;
    }

    private class PhotoTapListener implements OnPhotoTapListener {
        @Override
        public void onPhotoTap(ImageView view, float x, float y) {



            //get location & add query
            int density = ((int)bitmap.getWidth() / map_width);
            Log.e("exploit", "DPI : " + density + ", resource width : " + map_width);
            int location_x = (int)Math.floor((bitmap.getWidth() / density * x) / 19);
            int location_y = (int)Math.floor((bitmap.getHeight() / density * y) / 19);
            String query = "select * from circle_info where Hall like '%W34%' and Day=3 and location_x="+location_x+" and location_y="+location_y;
            Cursor cur = mDataBase.rawQuery(query, null);
            cur.moveToFirst();

            //iterate query add items to dialog
            ArrayList<circle> items = new ArrayList<>();
            if(cur.getCount() != 0) {
                while (true) {
                    try {
                        Log.e("exploit", "Circle Result :" + cur.getString(cur.getColumnIndex("circle")));
                        items.add(new circle("Sample", cur.getString(cur.getColumnIndex("Name")),
                                cur.getString(cur.getColumnIndex("Author")),
                                "W34", "3", cur.getString(cur.getColumnIndex("circle"))));
                    } catch (Exception e) {
                        break;
                    }
                    cur.moveToNext();
                }
                adapter.setItems(items);
                dialog = new circle_info_dialog(fragment_map.this, adapter);
                dialog.show();
            }
            cur.close();

            Log.e("exploit", "View Length : " + bitmap.getWidth() + ", " + bitmap.getHeight());
            Log.e("exploit", "Percentage : " + x + ", " + y + " , " + view.getId());
            Log.e("exploit", "Check Location : " + location_x + ", " + location_y);

            Log.e("exploit", "Drawable Length : " + drawable.getIntrinsicWidth() + ", " + drawable.getIntrinsicHeight());

        }
    }


}

/* bitmap Invalidate code
 canvas.drawCircle(bitmap.getWidth() * x, bitmap.getHeight() * y, 60/metrics.density, paint);
 view.invalidate();

    older version location check;

            int ceil_x = (int)Math.ceil((bitmap.getWidth() * x ) / 32);
            int ceil_y = (int)Math.ceil((bitmap.getHeight() * y ) / 32);
            int floor_x = (int)Math.floor((bitmap.getWidth() * x ) / 32);
            int floor_y = (int)Math.floor((bitmap.getHeight() * y ) / 32);

             //String query = "select * from circle_info where Hall like '%S34%' and Day=2 and location_x between " + floor_x + " and " +ceil_x + " and location_y between " + floor_y + " and " + ceil_y;

             Get Density of resource
                         DisplayMetrics metrics = getResources().getDisplayMetrics();
                         metrics.density

 */
