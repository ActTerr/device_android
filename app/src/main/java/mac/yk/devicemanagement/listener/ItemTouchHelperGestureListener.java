package mac.yk.devicemanagement.listener;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public  abstract class ItemTouchHelperGestureListener<T> extends GestureDetector.SimpleOnGestureListener {
        RecyclerView recyclerView;
    Class<T> mClazz;

    public ItemTouchHelperGestureListener(RecyclerView recyclerView,Class<T> clazz) {
        this.recyclerView = recyclerView;
        mClazz=clazz;
    }

    @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onItemClick((T) vh);
            }
            return true;
        }

        //长点击事件，本例不需要不处理
        //@Override
        //public void onLongPress(MotionEvent e) {
        //    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
        //    if (child!=null) {
        //        RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
        //        onItemLongClick(vh);
        //    }
        //}

        public abstract void onItemClick(T t);
        //public abstract void onItemLongClick(RecyclerView.ViewHolder vh);
    }