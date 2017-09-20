package hu.d2.offsitesr.ui.view.component;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by csabinko on 2017.09.15..
 */

public class VerticalSpaceItemDecoration extends DividerItemDecoration {
    private final int spacing;

    public VerticalSpaceItemDecoration(Context context, int orientation, int spacing) {
        super(context, orientation);
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = spacing;
        outRect.top = spacing;
    }
}
