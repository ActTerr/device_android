package mac.yk.devicemanagement.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    int space;

    public MyItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view)!=0){
            outRect.top=space;
        }
    }
}
