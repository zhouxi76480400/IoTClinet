package me.zhouxi.iot.nfc.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import me.zhouxi.iot.R;
import me.zhouxi.iot.client.nfc.object.NFCKeyObject;

/**
 * Created by zhouxi on 25/10/2017.
 */

public class NFCCardAdapter extends RecyclerView.Adapter<NFCCardAdapter.NFCCardAdapterViewHolder> {

    private List<NFCKeyObject> dataSource;

    private boolean isShowModify;

    public void setShowModify(boolean isShowModify){
        if(this.isShowModify != isShowModify){
            this.isShowModify = isShowModify;
            notifyDataSetChanged();
        }
    }

    public boolean isShowModify(){
        return isShowModify;
    }

    public NFCCardAdapter(List<NFCKeyObject> keyObjects){
        super();
        this.dataSource = keyObjects;
    }

    @Override
    public NFCCardAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nfc_card_adapter,parent,false);
        return new NFCCardAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NFCCardAdapterViewHolder holder, int position) {
        NFCKeyObject nfcKeyObject = dataSource.get(position);
        holder.tv_card_number.setText(addSpace(String.valueOf(nfcKeyObject.create_time)));
    }

    @Override
    public int getItemCount() {
        if(dataSource != null)
            return dataSource.size();
        return 0;
    }

    public class NFCCardAdapterViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;

        public TextView tv_card_number;

        public NFCCardAdapterViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            resizeCardView(cardView);
            tv_card_number = (TextView) itemView.findViewById(R.id.tv_card_number);
            setTextBottomMargin(tv_card_number);
        }

        private void resizeCardView(CardView cardView){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager =
                    (WindowManager) cardView.getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
            int cardViewTrueWidth = displayMetrics.widthPixels - layoutParams.leftMargin - layoutParams.rightMargin;
            int cardViewHeight = (int) (cardViewTrueWidth * 0.63);
            layoutParams.height = cardViewHeight;
            cardView.setRadius(cardViewTrueWidth / 40);
            cardView.setCardElevation(cardViewTrueWidth / 80);
        }

        private void setTextBottomMargin(TextView textView){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
            int cardViewHeight = layoutParams.height;
            FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) textView.getLayoutParams();
            lp2.bottomMargin = cardViewHeight / 4;
        }
    }

    private String addSpace(String string){
        StringBuilder stringBuilder = new StringBuilder();
        char [] array = string.toCharArray();
        for(int i = 0 ; i < array.length ; i ++ ){
            stringBuilder.append(array[i]);
            if((i+1) % 4 == 0) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }
}
