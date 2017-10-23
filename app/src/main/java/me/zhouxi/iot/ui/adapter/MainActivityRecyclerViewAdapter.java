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
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import me.zhouxi.iot.R;
import me.zhouxi.iot.object.ItemDetailObject;

/**
 * Created by zhouxi on 7/10/2017.
 */

public class MainActivityRecyclerViewAdapter extends
        RecyclerView.Adapter<MainActivityRecyclerViewAdapter.MainActivityRecyclerViewAdapterViewHolder> {

    public interface MainActivityRecyclerViewAdapterOnItemPressListener {

        void onMainActivityRecyclerViewAdapterOnItemPressListener
                (MainActivityRecyclerViewAdapter adapter, int position);

    }

    private MainActivityRecyclerViewAdapterOnItemPressListener listener;

    public void setMainActivityRecyclerViewAdapterOnItemPressListener
            (MainActivityRecyclerViewAdapterOnItemPressListener listener){
        this.listener = listener;
    }

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
        return new MainActivityRecyclerViewAdapterViewHolder(allView,this);
    }

    @Override
    public void onBindViewHolder(MainActivityRecyclerViewAdapterViewHolder holder, int position) {
        setCardViewMargin(holder,position);
        Object obj = dataSource.get(position);
        String name = null;
        String desc = null;
        if(obj instanceof ItemDetailObject){
            ItemDetailObject itemDetailObject = (ItemDetailObject) obj;
            name = itemDetailObject.name;
            desc = itemDetailObject.desc;
        }
        if(name != null)
            holder.tv_name.setText(name);
        if(desc != null)
            holder.tv_desc.setText(desc);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class MainActivityRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public View press_view;

        public TextView tv_name;

        public TextView tv_desc;

        public MainActivityRecyclerViewAdapterViewHolder(View itemView,
                                                         final MainActivityRecyclerViewAdapter adapter) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            press_view = itemView.findViewById(R.id.press_view);
            press_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int pos = getLayoutPosition();
                        listener.onMainActivityRecyclerViewAdapterOnItemPressListener(adapter,pos);
                    }
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

}
