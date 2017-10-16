package me.zhouxi.iot.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import me.zhouxi.iot.R;
import me.zhouxi.iot.client.find_server.ServerObject;

/**
 * Created by zhouxi on 11/10/2017.
 */

public class FindServerRecyclerViewAdapter extends
        RecyclerView.Adapter<FindServerRecyclerViewAdapter.FindServerRecyclerViewAdapterViewHolder> {

    public interface FindServerRecyclerViewAdapterListener {

        void findServerRecyclerViewAdapterListenerOnItemPress(int position);

    }

    private FindServerRecyclerViewAdapterListener listener;

    public void setListener(FindServerRecyclerViewAdapterListener newListener){
        this.listener = newListener;
    }

    public FindServerRecyclerViewAdapterListener getListener(){
        return this.listener;
    }

    private RecyclerView recyclerView;

    private List<ServerObject> serverObjectList;

    public FindServerRecyclerViewAdapter(RecyclerView recyclerView,List<ServerObject> serverObjectList) {
        super();
        this.recyclerView = recyclerView;
        this.serverObjectList = serverObjectList;
    }

    @Override
    public FindServerRecyclerViewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_find_server_activity_recycler_view_adapter,parent,false);
        return new FindServerRecyclerViewAdapterViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(FindServerRecyclerViewAdapterViewHolder holder, int position) {
        ServerObject serverObject = serverObjectList.get(position);
        holder.tv_name.setText(serverObject.name);
        holder.tv_ip.setText(serverObject.ip);
        holder.tv_version.setText(serverObject.version);
    }

    @Override
    public int getItemCount() {
        return serverObjectList.size();
    }

    public class FindServerRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_name, tv_ip, tv_version ;

        public LinearLayout linear_layout;

        public FindServerRecyclerViewAdapterViewHolder(View itemView, final FindServerRecyclerViewAdapterListener listener) {
            super(itemView);
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
            linear_layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.findServerRecyclerViewAdapterListenerOnItemPress(getLayoutPosition());
                    }
                }
            });
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_ip = (TextView) itemView.findViewById(R.id.tv_ip);
            tv_version = (TextView) itemView.findViewById(R.id.tv_version);
        }
    }
}
