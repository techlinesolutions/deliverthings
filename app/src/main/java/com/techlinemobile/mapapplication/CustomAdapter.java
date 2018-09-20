package com.techlinemobile.mapapplication;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> imageUrl;
    ArrayList<String> desc;
    ArrayList<String> id;
    ArrayList<String> address;
    ArrayList<String> lat;
    ArrayList<String> lng;


    Context context;
    private static String TAG = "CUSTOM_ADAPTOR";

    public CustomAdapter(Context context, ArrayList<String> imageUrl,
                         ArrayList<String> desc,
                         ArrayList<String> id,
                         ArrayList<String> address,
                         ArrayList<String> lat,
                         ArrayList<String> lng)

    {
        this.context = context;
        this.imageUrl = imageUrl;
        this.desc = desc;
        this.id = id;
        this.address = address;
        this.lat = lat;
        this.lng = lng;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        // set the data in items
        holder.tvDesc.setText(desc.get(position));
        holder.tvAddress.setText(address.get(position));
        holder.tvLat.setText(lat.get(position));
        holder.tvLong.setText(lng.toString());
        holder.ivImage.setImageURI(Uri.parse(imageUrl.get(position)));
        Glide.with(context)
                .load(imageUrl.get(position))
                .into(holder.ivImage);
//        Picasso.with(MainThread.).load(imageUrl.get(position)).into(holder.ivImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("DISPLAY_NAME", desc.get(position));
                Log.d(TAG, "desc.get(position) is :" + desc.get(position));

                Intent intent = new Intent(context.getApplicationContext(), MapActivity.class);
                intent.putExtra("DESC_NAME", desc.get(position));
                intent.putExtra("ADDRESS", address.get(position));
                intent.putExtra("LAT", lat.get(position));
                intent.putExtra("LNG", lng.get(position));
                intent.putExtra("IMAGE_URL", imageUrl.get(position));

                context.startActivity(intent);
                Toast.makeText(context, desc.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return imageUrl.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDesc, tvAddress,
                tvLat, tvLong;
        ImageView ivImage;
        // init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            ivImage = itemView.findViewById(R.id.thumbnail);
            tvDesc = itemView.findViewById(R.id.desc);
            tvAddress = itemView.findViewById(R.id.address);
            tvLat = itemView.findViewById(R.id.lat);
            tvLong = itemView.findViewById(R.id.lng);
        }
    }
}
