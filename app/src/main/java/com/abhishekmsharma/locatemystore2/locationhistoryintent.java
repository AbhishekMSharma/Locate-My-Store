package com.abhishekmsharma.locatemystore2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class locationhistoryintent extends ActionBarActivity {

    LocationDatabaseHandler dbHandler;
    List<LocationHistory> locations2 = new ArrayList<LocationHistory>();
    String locations[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationhistoryintent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHandler = new LocationDatabaseHandler(getApplicationContext());
        List<LocationHistory> addableLocations = dbHandler.getAllLocations();
        int count = dbHandler.getLocationsCount();
        locations = new String[count];

        for(int i=0;i<count;i++)
        {
            List<LocationHistory> l = dbHandler.getAllLocations();
            LocationHistory lh2 = l.get(i);
            locations[i] = lh2.getlName();
        }
        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, locations);
        ListView theListView = (ListView) findViewById(R.id.listView);
        theListView.setAdapter(theAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_locationhistoryintent, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
