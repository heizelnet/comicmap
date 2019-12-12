package com.heizelnet.comicmap;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeDeleter extends ItemTouchHelper.SimpleCallback {
    /**
     * Creates a Callback for the given drag and swipe allowance. These values serve as
     * defaults
     * and if you want to customize behavior per ViewHolder, you can override
     * {#getSwipeDirs(RecyclerView, ViewHolder)}
     * and / or { #getDragDirs(RecyclerView, ViewHolder)}.
     *
     * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
     *                  composed of { #LEFT}, { #RIGHT}, { #START}, {
     *                  #END},
     *                  { #UP} and { #DOWN}.
     * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
     *                  composed of { #LEFT}, { #RIGHT}, { #START}, {
     *                  #END},
     *                  { #UP} and { #DOWN}.
     */
    private CheckListAdapter mAdapter;
    private final ColorDrawable background;


    public SwipeDeleter(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        background = new ColorDrawable(ContextCompat.getColor(MyApplication.getAppContext(), R.color.gray2));
    }

    public SwipeDeleter(CheckListAdapter adapter) {
        //super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        super(0,ItemTouchHelper.LEFT);
        mAdapter = adapter;
        background = new ColorDrawable(ContextCompat.getColor(MyApplication.getAppContext(), R.color.gray2));

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) { // Swiping to the right
            /*
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
             */

        } else if (dX < -0.45) { // Swiping to the left
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
    }
}
