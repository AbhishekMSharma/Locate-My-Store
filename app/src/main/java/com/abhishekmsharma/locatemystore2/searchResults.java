package com.abhishekmsharma.locatemystore2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class searchResults extends ActionBarActivity {
    String [] storeNames;
    String [] addresses;
    String [] contact;
    String [] lati;
    String [] longi;
    String pname;
    String pdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();
        storeNames = intent.getStringArrayExtra("listofstores");
        addresses = intent.getStringArrayExtra("listofaddresses");
        contact = intent.getStringArrayExtra("listofcontacts");
        lati = intent.getStringArrayExtra("listoflats");
        longi = intent.getStringArrayExtra("listoflongs");
        pname = intent.getStringExtra("productname");
        pdesc = intent.getStringExtra("productdesc");
        //Log.e("Store 1: ", storeNames[0]);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ContactAdapter ca = new ContactAdapter(this,storeNames,addresses,contact,lati,longi,pname,pdesc);
        recList.setAdapter(ca);


        TextView pname2 = (TextView) findViewById(R.id.productTitle);
        TextView pdesc2 = (TextView) findViewById(R.id.productDesc);
        pname2.setText(pname);
        pdesc2.setText(pdesc);




    }
    public  void showDialog()
    {
        /*Dialog dialog = new Dialog(searchResults.this);
        dialog.setContentView(R.layout.list);
        ListView lv = (ListView ) dialog.findViewById(R.id.lv);
        dialog.setTitle("ListView");
        dialog.show();*/
        Log.e("In ShowDialog", "Here");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_search_results, menu);
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
