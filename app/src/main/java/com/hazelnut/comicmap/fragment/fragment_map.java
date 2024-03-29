package com.hazelnut.comicmap.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.hazelnut.comicmap.DataBaseHelper;
import com.hazelnut.comicmap.DataSharedPreference;
import com.hazelnut.comicmap.ItemSpinnerAdapter;
import com.hazelnut.comicmap.Logger;
import com.hazelnut.comicmap.MapSpinnerItem;
import com.hazelnut.comicmap.MyApplication;
import com.hazelnut.comicmap.R;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class fragment_map extends Fragment {

    private PhotoView photoView;
    private Bitmap bitmap;
    private Canvas canvas;
    private BitmapDrawable drawable;
    private ImageButton imageButton;
    private Paint paint;
    private circle_info_adapter adapter;
    private circle_info_dialog dialog;
    private SQLiteDatabase mDataBase;
    private boolean toggle, toggle_search;
    private int location_x, location_y;
    private MapSpinnerItem spinnerHall, spinnerDay;
    private int day;
    private float circle_pixel, map_width, map_height, map_pixel;
    private String hallName;
    private String[] Hall, Day;
    private Point size;
    private DataSharedPreference dataSharedPreference = new DataSharedPreference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mDataBase = new DataBaseHelper(getContext()).openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Set Default Settings
        toggle = false;
        toggle_search = false;
        size = new Point();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        display.getSize(size);

        //Set Default Map with bitmap layer
        map_pixel = getResources().getInteger(R.integer.W12_width);
        circle_pixel = Float.parseFloat(getResources().getString(R.string.W12_circle_pixel));
        drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.w12_1);
        bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        map_width = (float)bitmap.getWidth();
        map_height = (float)bitmap.getHeight();
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(150, 158, 246, 25));

        //Set Spinner Adapter data
        Hall = getResources().getStringArray(R.array.HallArray);
        Day = getResources().getStringArray(R.array.DayArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        //Set Default Settings & Set Map with bitmap layer & Set TapListener
        imageButton = view.findViewById(R.id.AnimationButton);
        photoView = view.findViewById(R.id.photo_view);
        photoView.setMaximumScale(7.0f);
        photoView.setImageBitmap(bitmap);
        photoView.setOnPhotoTapListener(new PhotoTapListener());



        //Set Search Default Settings
        final AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.editTextAuto);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(MyApplication.getAppContext(),
                android.R.layout.simple_dropdown_item_1line, dataSharedPreference.getStringArrayList(dataSharedPreference.SEARCH_BOX)));
        autoCompleteTextView.setOnItemClickListener((adapterView, view13, i, l) -> {

            //Hide Keyboard & Focus
            InputMethodManager inputManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            autoCompleteTextView.clearFocus();


            //This part will be changed prepareStatement query..
            String condition = autoCompleteTextView.getText().toString().split(" : ")[0];
            String value = autoCompleteTextView.getText().toString().split(" : ")[1];
            String query = "select Hall, Day, location_x, location_y from circle_info where " + condition + "=\"" + value + "\"";
            //(?=?)
            Cursor cur = mDataBase.rawQuery(query, null);
            cur.moveToFirst();

            String Hall = cur.getString(cur.getColumnIndex("Hall"));
            day = cur.getInt(cur.getColumnIndex("Day"));
            location_x = cur.getInt(cur.getColumnIndex("location_x"));
            location_y = cur.getInt(cur.getColumnIndex("location_y"));
            cur.close();

            toggle_search = true;
            if(Hall.equals("W12")) {
                spinnerHall.setSelection(0);
            } if(Hall.equals("W34")) {
                spinnerHall.setSelection(1);
            } if(Hall.equals("S12")) {
                spinnerHall.setSelection(2);
            } if(Hall.equals("S34")) {
                spinnerHall.setSelection(3);
            }
            spinnerDay.setSelection(day - 1);

        });


        //Set Spinner Adapter
        spinnerHall = (MapSpinnerItem) view.findViewById(R.id.spinner3);
        spinnerDay =  (MapSpinnerItem) view.findViewById(R.id.spinner4);
        spinnerHall.setAdapter(new ItemSpinnerAdapter(getActivity().getApplicationContext(), Hall));
        spinnerDay.setAdapter(new ItemSpinnerAdapter(getActivity().getApplicationContext(), Day));

        //Set Spinner Listener

        spinnerHall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String spinner_day_item;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        hallName = "W12";
                        break;
                    case 1:
                        hallName = "W34";
                        break;
                    case 2:
                        hallName = "S12";
                        break;
                    case 3:
                        hallName = "S34";
                        break;
                }
                circle_pixel = Float.parseFloat(getResources().getString(getResId(hallName + "_circle_pixel", R.string.class)));
                map_pixel = getResources().getInteger(getResId(hallName+"_width", R.integer.class));
                spinner_day_item = spinnerDay.getSelectedItemPosition() < 0 ? "_" + "1" : "_" + (spinnerDay.getSelectedItemPosition() + 1);
                drawable = (BitmapDrawable) getResources().getDrawable(getResId(hallName.toLowerCase() + spinner_day_item, R.drawable.class));
                Logger.e("exploit", "selected_Hall");

                if(!toggle_search) {
                    bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                    map_width = bitmap.getWidth();
                    map_height = bitmap.getHeight();
                    canvas.setBitmap(bitmap);
                    photoView.setImageBitmap(bitmap);
                    toggle = false;
                    imageButton.setImageDrawable(getContext().getDrawable(R.drawable.favorite_off));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "Select Hall", Toast.LENGTH_SHORT).show(); }
        });
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = position + 1;
                String spinner_day_item = "_" + day;
                drawable = (BitmapDrawable) getResources().getDrawable(getResId(hallName.toLowerCase() + spinner_day_item, R.drawable.class));

                bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                map_width = bitmap.getWidth();
                map_height = bitmap.getHeight();
                canvas.setBitmap(bitmap);
                photoView.setImageBitmap(bitmap);

                //Set search point when Search function On (toggle_search == true)
                //Go to spinnerDay
                if(toggle_search) {
                    float density = (map_width / map_pixel);
                    float point_x = ((location_x * circle_pixel) + (circle_pixel / 3)) * density;
                    float point_y = ((location_y * circle_pixel) + (circle_pixel / 2)) * density;
                    paint.setColor(Color.argb(150, 158, 246, 25));
                    canvas.drawCircle(point_x , point_y, circle_pixel, paint);
                    photoView.invalidate();
                    toggle_search = false;
                    Logger.e("exploit", "Search Value , point_x : "+point_x +" point_y : "+point_y);
                }
                toggle = false;
                imageButton.setImageDrawable(getContext().getDrawable(R.drawable.favorite_off));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "Select Day", Toast.LENGTH_SHORT).show();
            }
        });

        //favorite button listener
        imageButton.setOnClickListener(v -> {
            if(!toggle) {
                imageButton.setImageDrawable(getContext().getDrawable(R.drawable.favorite_on));

                Logger.e("exploit", "Favorite Function Activated");
                //Test code for favorite function
                //canvas.setBitmap(bitmap);
                String format = "select * from circle_info " +
                        "where Hall like '%" + hallName + "%' and Day=" + day + " and favorite >0";

                Logger.e("exploit", "favorite_query : " + format);
;               Cursor cur = mDataBase.rawQuery(format, null);
                cur.moveToFirst();
                if(cur.getCount() != 0) {
                    while (true) {
                        try {
                            float density = (map_width / map_pixel);
                            float point_x = ((cur.getInt(cur.getColumnIndex("location_x")) * circle_pixel) + (circle_pixel / 3)) * density;
                            float point_y = ((cur.getInt(cur.getColumnIndex("location_y")) * circle_pixel) + (circle_pixel / 2)) * density;
                            int favorite_color = cur.getInt(cur.getColumnIndex("favorite"));
                            paint.setColor(getResources().getColor(getColor(favorite_color)));
                            Logger.e("exploit", "point_x : "+point_x +" point_y : "+point_y+" favorite : "+favorite_color);
                            canvas.drawCircle(point_x, point_y, (circle_pixel * 2) / 3, paint);
                        } catch(Exception e) { break; }
                        cur.moveToNext();
                    }
                    cur.close();
                }
                photoView.invalidate();
                toggle = true;

            } else {
                imageButton.setImageDrawable(getContext().getDrawable(R.drawable.favorite_off));
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
            float density = (map_width / map_pixel);
            Logger.e("exploit", "DPI : " + density + ", resource width : " + map_pixel + ", circle_pixel : " + circle_pixel);
            location_x = (int)Math.floor((map_width / density * x) / circle_pixel);
            location_y = (int)Math.floor((map_height / density * y) / circle_pixel);
            String query = "select * from circle_info " +
                    "where Hall like '%" + hallName + "%' and Day=" + day + " and location_x=" + location_x + " and location_y=" + location_y;

            Cursor cur = mDataBase.rawQuery(query, null);
            cur.moveToFirst();
            Logger.e("exploit", "Query : " + query);

            //iterate query add items to dialog
            ArrayList<circle_instance> items = new ArrayList<>();
            if(cur.getCount() != 0) {
                while (true) {
                    try {
                        items.add(new circle_instance(cur.getInt(cur.getColumnIndex("wid")), cur.getString(cur.getColumnIndex("Name")),
                                cur.getString(cur.getColumnIndex("Author")),
                                cur.getString(cur.getColumnIndex("Genre")), String.valueOf(day), cur.getString(cur.getColumnIndex("circle")),
                                cur.getInt(cur.getColumnIndex("IsPixivRegistered")), cur.getInt(cur.getColumnIndex("IsTwitterRegistered")),
                                cur.getInt(cur.getColumnIndex("IsNiconicoRegistered")), cur.getString(cur.getColumnIndex("PixivUrl")),
                                cur.getString(cur.getColumnIndex("TwitterUrl")), cur.getString(cur.getColumnIndex("NiconicoUrl"))));
                    } catch (Exception e) {
                        break;
                    }
                    cur.moveToNext();
                }

                //Set Dialog Items (Circle A, Circle B)
                adapter = new circle_info_adapter(items);
                dialog = new circle_info_dialog(fragment_map.this, adapter);
                dialog.show();

                //Set Dialog Layout size
                Window window = dialog.getWindow();
                window.setLayout((int)(size.x * 0.95f), ViewGroup.LayoutParams.WRAP_CONTENT);

            }
            cur.close();

            Logger.e("exploit", "View Length : " + map_width + ", " + map_height);
            Logger.e("exploit", "Percentage : " + x + ", " + y + " , " + view.getId());
            Logger.e("exploit", "Check Location : " + location_x + ", " + location_y);

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public int getColor(int value) {
        int resource_id = 0;
        switch (value) {
            case 0:
                resource_id = R.color.whiteTextColor;
                break;
            case 1:
                resource_id = R.color.circle_color1;
                break;
            case 2:
                resource_id = R.color.circle_color2;
                break;
            case 3:
                resource_id = R.color.circle_color3;
                break;
            case 4:
                resource_id = R.color.circle_color4;
                break;
            case 5:
                resource_id = R.color.circle_color5;
                break;
            case 6:
                resource_id = R.color.circle_color6;
                break;
            case 7:
                resource_id = R.color.circle_color7;
                break;
            case 8:
                resource_id = R.color.circle_color8;
                break;
            case 9:
                resource_id = R.color.circle_color9;
                break;
        }
        return resource_id;
    }


    //this code looks like so good... excellent..
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}