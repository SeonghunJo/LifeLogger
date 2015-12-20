package seonghunjo.com.lifelogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LogDetailActivity extends AppCompatActivity {

    private GoogleMap map;
    private Marker marker;

    private LatLng latLng;

    private TextView detailTitle;
    private TextView detailDate;
    private TextView detailContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        double lat = intent.getDoubleExtra("lat", 37.56);
        double lng = intent.getDoubleExtra("lng", 126.97);

        detailTitle = (TextView)findViewById(R.id.detail_title);
        detailDate = (TextView)findViewById(R.id.detail_date);
        detailContent = (TextView)findViewById(R.id.detail_content);

        detailTitle.setText(intent.getStringExtra("title"));
        detailDate.setText(intent.getStringExtra("date"));
        detailContent.setText(intent.getStringExtra("content"));

        latLng = new LatLng(lat, lng);
        // Set Google Map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.detail_map)).getMap();
        marker = map.addMarker(new MarkerOptions().position(latLng).title("Here"));
        marker.showInfoWindow();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle("Log");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
