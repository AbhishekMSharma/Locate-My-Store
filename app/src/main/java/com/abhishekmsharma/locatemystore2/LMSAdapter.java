package com.abhishekmsharma.locatemystore2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Abhishek Sharma on 13/04/2015.
 */
public class LMSAdapter extends RecyclerView.Adapter<LMSAdapter.MyViewHolder> {
    private final LayoutInflater inflater;
    List<Information> data = Collections.emptyList();
    private Context context;


    public LMSAdapter(Context context, List<Information> data)
    {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        //Toast.makeText(null,data.size(),Toast.LENGTH_SHORT);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Information current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);



    }

    @Override
    public int getItemCount() {
        // Toast.makeText(null,"Hello",Toast.LENGTH_SHORT);
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }


        @Override
        public void onClick(View view)
        {
            if(getPosition() == 0)
                context.startActivity(new Intent(context,viewProducts.class));
            else if(getPosition()==3)
                context.startActivity((new Intent(context,locationhistoryintent.class)));
        }
    }
    /*public interface ClickListener0
    {
        public void itemClicked(View v, int position);
    }*/
}
