package com.example.holoto.blueskytv.TvListView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.holoto.blueskytv.R;
import com.example.holoto.blueskytv.UrlDB.MyTvurl;

import java.util.List;

public class TvlistAdpater extends RecyclerView.Adapter<TvlistAdpater.ViewHolder>
{
    private View.OnClickListener listener;
    private mylistenerjk mylistenerjk1;
    List<MyTvurl> list;
    public static interface mylistenerjk{
        void listclick1(View view,int pos);
}
    public TvlistAdpater(List<MyTvurl> a)
    {
        list=a;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public ViewHolder(View item)
        {

            super(item);
            textView=(TextView) item.findViewById(R.id.Tv_List_menu);
//            textView.setFocusable(true);
//            textView.setFocusableInTouchMode(true);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View Tvlistview=inflater.inflate(R.layout.tv_list_view,parent,false);
        final ViewHolder viewHolder=new ViewHolder(Tvlistview);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("dd", "onClick: dd1" );
//            }
//        });
//        Tvlistview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mylistenerjk1.listclick1(viewHolder.textView,viewHolder.getLayoutPosition());
//            }
//        });
//        viewHolder.itemView.setOnClickListener(listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
//        final String title=list.get(position).getName().substring(2);
        final String title=list.get(position).getName();
        holder.textView.setText(title);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylistenerjk1.listclick1(holder.itemView,position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clicklister(mylistenerjk mylistenerjk)
    {
        this.mylistenerjk1=mylistenerjk;
    }

}
