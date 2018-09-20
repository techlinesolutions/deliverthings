package com.techlinemobile.mapapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap m_map;
    private boolean mapReady;
    private double long_val, lat_val;
    private Context context=this;
    private String TAG= "MAP";
    private Bundle extras;
    private String desc, pic_url, address;
    private TextView tvDesc, tvAddress;
    private ImageView ivThumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapReady=true;
        m_map = map;
        //LatLng newYork = new LatLng(40.7484,-73.9857);
        extras = getIntent().getExtras();
        if (extras != null) {
            lat_val = Double.parseDouble(extras.getString("LAT"));
            Log.d(TAG, "lat_val is :" + lat_val);

            long_val = Double.parseDouble(extras.getString("LNG"));
            Log.d(TAG, "long_val is :" + long_val);

            desc = extras.getString("DESC_NAME");
            Log.d(TAG, "desc is :" + desc);

            address = extras.getString("ADDRESS");
            Log.d(TAG, "address is :" + address);

            pic_url = extras.getString("IMAGE_URL");
            Log.d(TAG, "pic_url is :" + pic_url);
        }
        else{
            Toast.makeText(context, "Showing New York as Default", Toast.LENGTH_SHORT).show();

            LatLng newYork = new LatLng(40.7484,-73.9857);
            return;
        }
        LatLng newYork = new LatLng(lat_val,long_val);
        CameraPosition target = CameraPosition.builder().target(newYork).zoom(14).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        tvDesc = findViewById(R.id.desc);
        tvAddress = findViewById(R.id.address);
        ivThumb = findViewById(R.id.thumbnail);
        tvDesc.setText(desc);
        tvAddress.setText(address);
        Glide.with(context)
                .load(pic_url)
                .into(ivThumb);
    }
}
