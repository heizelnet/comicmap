package com.example.comicmap.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.comicmap.DataBaseHelper;
import com.example.comicmap.DataSharedPreference;
import com.example.comicmap.ItemSpinnerAdapter;
import com.example.comicmap.MyApplication;
import com.example.comicmap.R;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class fragment_map extends Fragment {

    private PhotoView photoView;
    private Bitmap bitmap;
    private Canvas canvas;
    private BitmapDrawable drawable;
    private ImageButton imageButton;
    private Paint paint;
    private circle_info_adapter adapter = new circle_info_adapter();
    private circle_info_dialog dialog;
    private SQLiteDatabase mDataBase;
    private boolean toggle;
    private Spinner spinnerHall, spinnerDay;
    private int day;
    private float circle_pixel, map_width, map_height, map_pixel;
    private String hallName;
    private Point size;
    private DataSharedPreference dataSharedPreference = new DataSharedPreference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mDataBase = new DataBaseHelper(getContext()).openDataBase();

        //Set Default Settings
        toggle = false;
        imageButton = view.findViewById(R.id.AnimationButton);
        photoView = view.findViewById(R.id.photo_view);
        photoView.setMaximumScale(7.0f);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        //Set Search Default Settings
        final AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.editTextAuto);
        //ChipGroup chipGroup = view.findViewById(R.id.ChipGroup);
        //final Chip chip = new Chip(getActivity());
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(MyApplication.getAppContext(),
                android.R.layout.simple_dropdown_item_1line, dataSharedPreference.getStringArrayList(dataSharedPreference.SEARCH_BOX)));
        autoCompleteTextView.setOnItemClickListener((adapterView, view13, i, l) -> {
/*
            chip.setText(autoCompleteTextView.getText().toString());
            chip.setCheckable(false);
            chip.setCloseIconVisible(true);
            chip.setTextAppearanceResource(R.style.ChipTextStyle);

            chipGroup.addView(chip);
            chip.setOnCloseIconClickListener(view12 -> chipGroup.removeView(chip));
            autoCompleteTextView.clearFocus();
            autoCompleteTextView.getText().clear();
 */
            autoCompleteTextView.clearFocus();
        });
        

        //Set Map with bitmap layer
        map_pixel = getResources().getInteger(R.integer.west_12_width);
        circle_pixel = Float.parseFloat(getResources().getString(R.string.west_circle_pixel));
        drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.west_12);
        bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        map_width = (float)bitmap.getWidth();
        map_height = (float)bitmap.getHeight();
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(120, 164, 198, 57));
        photoView.setImageBitmap(bitmap);

        //Set Spinner Adapter
        String[] Hall, Day;
        spinnerHall =(Spinner) view.findViewById(R.id.spinner3);
        spinnerDay = (Spinner) view.findViewById(R.id.spinner4);
        Hall = getResources().getStringArray(R.array.HallArray);
        Day = getResources().getStringArray(R.array.DayArray);
        spinnerHall.setAdapter(new ItemSpinnerAdapter(getActivity().getApplicationContext(), Hall));
        spinnerDay.setAdapter(new ItemSpinnerAdapter(getActivity().getApplicationContext(), Day));
        spinnerHall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        hallName = "W12";
                        circle_pixel = Float.parseFloat(getResources().getString(R.string.west_circle_pixel));
                        map_pixel = getResources().getInteger(R.integer.west_12_width);
                        drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.west_12);
                        break;
                    case 1:
                        hallName = "W34";
                        circle_pixel = Float.parseFloat(getResources().getString(R.string.west_circle_pixel));
                        map_pixel = getResources().getInteger(R.integer.west_34_width);
                        drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.west_34);
                        break;
                    case 2:
                        hallName = "S12";
                        circle_pixel = Float.parseFloat(getResources().getString(R.string.south_circle_pixel));
                        map_pixel = getResources().getInteger(R.integer.south_12_width);
                        drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.south_12);
                        break;
                    case 3:
                        hallName = "S34";
                        circle_pixel = Float.parseFloat(getResources().getString(R.string.south_circle_pixel));
                        drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.south_34);
                        map_pixel = getResources().getInteger(R.integer.south_34_width);
                        break;
                }
                bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                map_width = bitmap.getWidth();
                map_height = bitmap.getHeight();
                canvas.setBitmap(bitmap);
                photoView.setImageBitmap(bitmap);

                if(toggle) {
                    imageButton.setImageDrawable(getContext().getDrawable(R.drawable.unfill_heart));
                    AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageButton.getDrawable();
                    animatedVectorDrawable.start();
                    toggle = false;
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "Select Day", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerHall.setSelection(0);
        spinnerDay.setSelection(0);

        //Set TapListener
        photoView.setOnPhotoTapListener(new PhotoTapListener());

        //Set LongClickListener
        photoView.setOnLongClickListener(view1 -> {
            Log.e("exploit", "Long clicked");
            return false;
        });

        //favorite button listener
        imageButton.setOnClickListener(v -> {
            if(!toggle) {
                imageButton.setImageDrawable(getContext().getDrawable(R.drawable.fill_heart));
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageButton.getDrawable();
                animatedVectorDrawable.start();

                Log.e("exploit", "Favorite Function Activated");
                //Test code for favorite function
                //canvas.setBitmap(bitmap);
                canvas.drawCircle(map_width * (float)0.86, map_height * (float)0.65, circle_pixel / 2, paint);
                canvas.drawCircle(map_width * (float)0.68, map_height * (float)0.56, circle_pixel/ 2, paint);
                photoView.invalidate();
                toggle = true;

            } else {
                imageButton.setImageDrawable(getContext().getDrawable(R.drawable.unfill_heart));
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageButton.getDrawable();
                animatedVectorDrawable.start();
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
            Log.e("exploit", "DPI : " + density + ", resource width : " + map_pixel + ", circle_pixel : " + circle_pixel);
            int location_x = (int)Math.floor((map_width / density * x) / circle_pixel);
            int location_y = (int)Math.floor((map_height / density * y) / circle_pixel);
            String query = "select * from circle_info where Hall like '%" + hallName +
                    "%' and Day=" + day + " and location_x=" + location_x +
                    " and location_y=" + location_y;

            Cursor cur = mDataBase.rawQuery(query, null);
            cur.moveToFirst();
            Log.e("exploit", "Query : " + query);

            //iterate query add items to dialog
            ArrayList<circle_instance> items = new ArrayList<>();
            if(cur.getCount() != 0) {
                while (true) {
                    try {
                        items.add(new circle_instance("Sample", cur.getString(cur.getColumnIndex("Name")),
                                cur.getString(cur.getColumnIndex("Author")),
                                hallName, String.valueOf(day), cur.getString(cur.getColumnIndex("circle"))));
                    } catch (Exception e) {
                        break;
                    }
                    cur.moveToNext();
                }

                //Set Dialog Items (Circle A, Circle B)
                adapter.setItems(items);
                dialog = new circle_info_dialog(fragment_map.this, adapter);
                dialog.show();

                //Set Dialog Layout size
                Window window = dialog.getWindow();
                window.setLayout((int)(size.x * 0.95f), ViewGroup.LayoutParams.WRAP_CONTENT);

            }
            cur.close();

            Log.e("exploit", "View Length : " + map_width + ", " + map_height);
            Log.e("exploit", "Percentage : " + x + ", " + y + " , " + view.getId());
            Log.e("exploit", "Check Location : " + location_x + ", " + location_y);

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}