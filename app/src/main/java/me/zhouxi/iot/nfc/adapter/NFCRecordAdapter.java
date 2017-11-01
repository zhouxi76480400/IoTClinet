package me.zhouxi.iot.nfc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zhouxi.iot.R;
import me.zhouxi.iot.client.nfc.object.OpenDoorRecordObject;

/**
 * Created by zhouxi on 17年11月1日.
 */

public class NFCRecordAdapter extends RecyclerView.Adapter<NFCRecordAdapter.NFCRecordAdapterViewHolder> {

    private List<OpenDoorRecordObject> dataSource;

    private RecyclerView recyclerView;

    private SimpleDateFormat simpleDateFormat;

    public NFCRecordAdapter(List<OpenDoorRecordObject> list,RecyclerView recyclerView){
        super();
        this.dataSource = list;
        this.recyclerView = recyclerView;
        this.simpleDateFormat = new SimpleDateFormat(recyclerView.getContext().getString(R.string.sdf)
                , Locale.getDefault());
    }

    @Override
    public NFCRecordAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nfc_record_adapter,parent,false);
        return new NFCRecordAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NFCRecordAdapterViewHolder holder, int position) {
        OpenDoorRecordObject openDoorRecordObject = dataSource.get(position);
        Date date = new Date(openDoorRecordObject.time * 1000);
        holder.tv1.setText(String.format("%s:%s",recyclerView.getContext().getString(R.string.time),
                simpleDateFormat.format(date)));
        holder.tv2.setText(String.format("%s:%s",recyclerView.getContext().getString(R.string.card),
                openDoorRecordObject.device));
        holder.tv3.setText(String.format("%s:%s",recyclerView.getContext().getString(R.string.device),
                openDoorRecordObject.name));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class NFCRecordAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView tv1;
        public TextView tv2;
        public TextView tv3;

        public NFCRecordAdapterViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
        }
    }
}
