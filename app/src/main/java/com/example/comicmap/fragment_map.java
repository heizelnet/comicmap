package com.example.comicmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

public class fragment_map extends Fragment {

    private PhotoView photoView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint, t_paint;
    private info_adapter adapter = new info_adapter();
    private circle_info_dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        photoView = view.findViewById(R.id.photo_view);
        photoView.setMaximumScale(5.0f);

        //photoView.setImageResource(R.drawable.south_34);
        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.south_34, dimensions);
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.south_34);
        bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(bitmap);
        paint = new Paint();

        paint.setColor(Color.argb(120, 164, 198, 57));
        photoView.setImageBitmap(bitmap);
        photoView.setOnPhotoTapListener(new PhotoTapListener());
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("exploit", "Long clicked");
                return false;
            }
        });
        return view;
    }

    private class PhotoTapListener implements OnPhotoTapListener {
        @Override
        public void onPhotoTap(ImageView view, float x, float y) {

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            Log.e("exploit", "Percentage : " + x + ", " + y + " , " + view.getId());
            Log.e("exploit", "View Location : " + bitmap.getWidth() + ", " + bitmap.getHeight());
            canvas.drawCircle(bitmap.getWidth() * x, bitmap.getHeight() * y, 60/metrics.density, paint);
            view.invalidate();

            adapter.setItems(new sample_circle().getItems());
            dialog = new circle_info_dialog(fragment_map.this, adapter);
            dialog.show();
            Log.e("exploit", "DPI : " + metrics.density);
        }
    }


}
