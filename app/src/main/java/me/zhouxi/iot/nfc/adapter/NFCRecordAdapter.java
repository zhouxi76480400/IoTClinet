package me.zhouxi.iot.nfc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.zhouxi.iot.R;
import me.zhouxi.iot.client.nfc.object.OpenDoorRecordObject;

/**
 * Created by zhouxi on 17年11月1日.
 */

public class NFCRecordAdapter extends RecyclerView.Adapter<NFCRecordAdapter.NFCRecordAdapterViewHolder> {

    private List<OpenDoorRecordObject> dataSource;

    private RecyclerView recyclerView;

    public NFCRecordAdapter(List<OpenDoorRecordObject> list,RecyclerView recyclerView){
        super();
        this.dataSource = list;
        this.recyclerView = recyclerView;
    }

    @Override
    public NFCRecordAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nfc_record_adapter,parent,false);
        return new NFCRecordAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NFCRecordAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class NFCRecordAdapterViewHolder extends RecyclerView.ViewHolder{

        public NFCRecordAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
