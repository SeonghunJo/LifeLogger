package seonghunjo.com.lifelogger;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int UPDATE_LIST = 0;

    static final LatLng SEOUL = new LatLng(37.56, 126.97);

    private GoogleMap map;
    private Marker marker;
    private LocationManager locationManager;

    private ArrayList<Log> logList;
    private RecyclerView recyclerView;

    private LogRecyclerViewAdapter recyclerAdapter;

    public Intent detailIntent;
    public Intent writeIntent;

    SQLiteDatabase db;
    String dbName = "LogList.db";
    String tableName = "LogTable";
    int dbMode = Context.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                writeIntent = new Intent(getApplicationContext(), WriteLogActivity.class);
                startActivity(writeIntent);
            }
        });

        // Set Google Map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        marker = map.addMarker(new MarkerOptions().position(SEOUL).title("I am here"));
        marker.showInfoWindow();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                LatLng curPos = new LatLng(lat, lng);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPos, 15));
                marker.setPosition(curPos);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location update
        if (isGPSEnabled) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }


        logList = new ArrayList<Log>();


        //logList.add(new Log(37.56, 126.97, "First", "memo1", Log.LogType.TYPE_NONE));
        //logList.add(new Log(37.56, 126.97, "Second", "memo2", Log.LogType.TYPE_NONE));
        //logList.add(new Log(37.56, 126.97, "Third", "memo3", Log.LogType.TYPE_NONE));

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new LogRecyclerViewAdapter(this, logList, R.layout.list_item);
        recyclerView.setAdapter(recyclerAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case UPDATE_LIST:
                data.getStringExtra("title", "No Title");
                data.getStringExtra("content", "");

                // insert data;

                break;
            default:
                break;
        }
    }

    public void GetLogListFromDB()
    {

    }

    // Table 생성
    public void createTable() {
        try {
            String sql =
                    "create table " + tableName +
                    "(id integer primary key autoincrement type integer not null, " +
                    "lat real not null, lng real not null, " +
                    "title text not null, date text not null, comment text not null default '')";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Log.d("Lab sqlite", "error: " + e);
        }
    }

    // Table 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    // Data 추가
    public int insertData(int type, double lat, double lng, String title, String date, String comment) {

        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("lat", lat);
        values.put("lng", lng);
        values.put("title", title);
        values.put("date", date);
        values.put("comment", comment);

        int id = (int)db.insert(tableName, null, values);
        if(id < 0) {
            // Insert Error
            android.util.Log.i("LifeLogger", "SQL Insert Fail : " + title);
        }

        return id;
    }

    // Data 업데이트
    public void updateData(int index, String name) {
        String sql = "update " + tableName + " set name = '" + name + "' where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 삭제
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 읽기(꺼내오기)
    public void selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(1);
            //Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_SHORT).show();
            android.util.Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");
        }
        result.close();
    }

    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
            //Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_SHORT).show();
            android.util.Log.d("lab_sqlite", "index= " + id + " name=" + name);

            results.moveToNext();
        }
        results.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            // TODO : Setting Activity
            detailIntent = new Intent(getApplicationContext(), LogDetailActivity.class);
            startActivity(detailIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
