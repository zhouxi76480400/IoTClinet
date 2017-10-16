package me.zhouxi.iot.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import java.util.List;

import me.zhouxi.iot.R;

/**
 * Created by zhouxi on 7/10/2017.
 */

public class MainActivityRecyclerViewAdapter extends
        RecyclerView.Adapter<MainActivityRecyclerViewAdapter.MainActivityRecyclerViewAdapterViewHolder> {

    private RecyclerView recyclerView;

    private List dataSource;

    public MainActivityRecyclerViewAdapter(RecyclerView recyclerView, List dataSource) {
        super();
        this.recyclerView = recyclerView;
        this.dataSource = dataSource;
    }

    @Override
    public MainActivityRecyclerViewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View allView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_main_activity_recycler_view_adapter, parent, false);
        return new MainActivityRecyclerViewAdapterViewHolder(allView);
    }

    @Override
    public void onBindViewHolder(MainActivityRecyclerViewAdapterViewHolder holder, int position) {
        setCardViewMargin(holder,position);


    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class MainActivityRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public View press_view;

        public MainActivityRecyclerViewAdapterViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            press_view = itemView.findViewById(R.id.press_view);
            press_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    /**
     * setCardViewMargin
     * @param holder
     * @param position
     */
    private void setCardViewMargin(MainActivityRecyclerViewAdapterViewHolder holder, int position){
        int card_view_margin_px = holder.itemView.getContext()
                .getResources().getDimensionPixelSize(R.dimen.card_view_margin);
        LinearLayout.LayoutParams card_view_lp = (LinearLayout.LayoutParams) holder.cardView.getLayoutParams();
        if(position == 0){
            // the first
            card_view_lp.setMargins(card_view_margin_px,card_view_margin_px,card_view_margin_px,card_view_margin_px/2);
        }else if(position == dataSource.size() - 1){
            // the last
            card_view_lp.setMargins(card_view_margin_px,card_view_margin_px/2,card_view_margin_px,card_view_margin_px);
        }else{
            card_view_lp.setMargins(card_view_margin_px,card_view_margin_px/2,card_view_margin_px,card_view_margin_px/2);
        }
    }

//    private int lastLayoutPosition = -1;
//
//    @Override
//    public void onViewAttachedToWindow(MainActivityRecyclerViewAdapterViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        int position = holder.getLayoutPosition();
//        int height = holder.itemView.getLayoutParams().height;
//
//        int deltaY = (int) (height / 2.0f);
//        if(position > lastLayoutPosition){
//        } else if(position < lastLayoutPosition) {
//            deltaY = 0 - deltaY;
//        }else {
//            return;
//        }
//
//        lastLayoutPosition = position;
//
//        holder.itemView.setTranslationY(deltaY);
//        holder.itemView.setAlpha(0.f);
//        holder.itemView.animate()
//                .translationY(0).alpha(1.f)
//                .setInterpolator(new LinearInterpolator())
//                .setDuration(300)
//                .start();
//    }
//
//    @Override
//    public void onViewDetachedFromWindow(MainActivityRecyclerViewAdapterViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//    }
}
