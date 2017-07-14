package com.abhishekmsharma.locatemystore2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import org.ksoap2.serialization.SoapObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements LocationListener {
   Toolbar toolbar;
    private static String[] products = {"Product 1 ", "Product 2"};
    String [] stores;
    String [] addresses;
    String[] contact;
    String[] lati;
    String[] longi;
    Location l;
    ProgressDialog pDialog;
    private static Product [] listOfProducts1;

    List<LocationHistory> locations = new ArrayList<LocationHistory>();
    LocationDatabaseHandler dbHandler;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);






        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        TextView tv = (TextView) findViewById(R.id.locationText);







        //mMap.setMyLocationEnabled(true);

        //tv.setText(String.valueOf(mMap.getMyLocation()));

        //start

        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<Object> names = new ArrayList<>();

        AsyncTask<Void, Void, SoapObject> se=new ConsumeWebSoapServiceObject("getProductNames",parameters,names).execute();
        try
        {
            int tmp = se.get().getPropertyCount();
            products = new String[tmp];
            listOfProducts1 = new Product[tmp];
            for (int i = 0; i <tmp; i++)
            {
                SoapObject ab = ((SoapObject)(se.get().getProperty(i)));
                listOfProducts1[i]=new  Product();
                listOfProducts1[i].p_Name = products[i] = ab.getProperty(0).toString();
                listOfProducts1[i].p_Id= ab.getProperty(1).toString();
                listOfProducts1[i].p_Specs= ab.getProperty(2).toString();
                listOfProducts1[i].p_Category= ab.getProperty(3).toString();
                //products[i] =  listOfProducts1[i].p_Name;
            }
        }
        catch(Exception e)
        {
            Log.e("Exception ",e+"");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, products);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.editText2);
        textView.setAdapter(adapter);


        textView.setOnEditorActionListener
        (
            new TextView.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEARCH)
                    {
                        //Toast.makeText(MainActivity.this,"Searched",Toast.LENGTH_SHORT).show();
                        handled = true;
                        searchNearStores();
                    }
                    return handled;

                 }

            }
        );

        //end



        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout), toolbar);


            Button b = (Button) findViewById(R.id.searchButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAllStores();
            }
        });

        final Button b2 = (Button) findViewById(R.id.nearButton);

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchNearStores();
                    }
                });

        final SeekBar sb = (SeekBar) findViewById(R.id.seekBar);
        b2.setText("Near Me (Within "+sb.getProgress()+" km)");
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                b2.setText("Near Me (Within "+sb.getProgress()+" km)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    public void searchNearStores()
    {
        String searchQuery = "";
        String pname="";
        String pdesc="";
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.editText2);
        String enteredText = String.valueOf(textView.getText());
        for(int i=0;i<products.length;i++)
        {
            //Log.e("Product name is " + listOfProducts1[i].p_Name, "Product entered is "+textView.getText());
            if (enteredText.equals(listOfProducts1[i].p_Name))
            {
                searchQuery = listOfProducts1[i].p_Id;
                pname = listOfProducts1[i].p_Name;
                pdesc = listOfProducts1[i].p_Specs;
                break;
            }
        }


        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<Object> names = new ArrayList<>();

        SeekBar sb = (SeekBar) findViewById(R.id.seekBar);


        parameters.add(0,l.getLatitude());
        parameters.add(1,l.getLongitude());
        //Log.e("Lat and Log: ", ""+l.getLatitude()+" and "+ l.getLongitude());
        parameters.add(2,(double) sb.getProgress());
        parameters.add(3,searchQuery);
        names.add(0,"yourLatitude");
        names.add(1,"yourLongitude");
        names.add(2,"distanceRangeInKM");
        names.add(3,"productId");
        AsyncTask<Void, Void, SoapObject> se=new ConsumeWebSoapServiceObject("StoresByDistanceAndProduct",parameters,names).execute();

        try
        {
            int tmp = se.get().getPropertyCount();
            Product[] listOfProducts = new Product[tmp];
            String abc = "555";

            stores = new String[tmp];
            addresses = new String[tmp];
            contact = new String[tmp];
            lati = new String[tmp];
            longi = new String[tmp];
            for (int i = 0; i < tmp; i++)
            {
                SoapObject ab = ((SoapObject)(se.get().getProperty(i)));
                //abc = ((SoapObject)(se.get().getProperty(i))).getProperty(1).toString();
                stores[i] = ab.getProperty(1).toString();
                //abc = ((SoapObject)(se.get().getProperty(i))).getProperty(2).toString();
                addresses[i] = ab.getProperty(2).toString();
                //abc = ((SoapObject)(se.get().getProperty(i))).getProperty(3).toString();
                contact[i] = ab.getProperty(3).toString();
                //abc = ((SoapObject)(se.get().getProperty(i))).getProperty(5).toString();
                lati[i] = ab.getProperty(5).toString();
                //abc = ((SoapObject)(se.get().getProperty(i))).getProperty(6).toString();
                longi[i] = ab.getProperty(6).toString();
            }

            Intent intent = new Intent(this,searchResults.class);
            intent.putExtra("listofstores",stores);
            intent.putExtra("listofaddresses",addresses);
            intent.putExtra("listofcontacts",contact);
            intent.putExtra("listoflats",lati);
            intent.putExtra("listoflongs",longi);
            intent.putExtra("productname",pname);
            intent.putExtra("productdesc",pdesc);
            startActivity(intent);
        }
        catch (Exception e)
        {
            Log.e("Exception occured: ", String.valueOf(e));
        }
    }
    public void searchAllStores()
    {
        String searchQuery = "";
        String pname="";
        String pdesc="";
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.editText2);
        String enteredText = String.valueOf(textView.getText());
        for(int i=0;i<products.length;i++)
        {
            Log.e("Product name is " + listOfProducts1[i].p_Name, "Product entered is "+textView.getText());
            if (enteredText.equals(listOfProducts1[i].p_Name))
            {
                searchQuery = listOfProducts1[i].p_Id;
                pname = listOfProducts1[i].p_Name;
                pdesc = listOfProducts1[i].p_Specs;
                break;
            }
        }


        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<Object> names = new ArrayList<>();

        parameters.add(0,searchQuery);
        names.add(0,"Product_ID");
        AsyncTask<Void, Void, SoapObject> se=new ConsumeWebSoapServiceObject("StoreIDList",parameters,names).execute();

        try
        {
            int tmp = se.get().getPropertyCount();
            Product[] listOfProducts = new Product[tmp];
            String abc = "555";
            stores = new String[tmp];
            addresses = new String[tmp];
            contact = new String[tmp];
            lati = new String[tmp];
            longi = new String[tmp];
            for (int i = 0; i < tmp; i++)
            {
                abc = ((SoapObject)(se.get().getProperty(i))).getProperty(1).toString();
                stores[i] = abc;
                abc = ((SoapObject)(se.get().getProperty(i))).getProperty(2).toString();
                addresses[i] = abc;
                abc = ((SoapObject)(se.get().getProperty(i))).getProperty(3).toString();
                contact[i] = abc;
                abc = ((SoapObject)(se.get().getProperty(i))).getProperty(5).toString();
                lati[i] = abc;
                abc = ((SoapObject)(se.get().getProperty(i))).getProperty(6).toString();
                longi[i] = abc;
            }
            Intent intent = new Intent(this,searchResults.class);
            intent.putExtra("listofstores",stores);
            intent.putExtra("listofaddresses",addresses);
            intent.putExtra("listofcontacts",contact);
            intent.putExtra("listoflats",lati);
            intent.putExtra("listoflongs",longi);
            intent.putExtra("productname",pname);
            intent.putExtra("productdesc",pdesc);
            startActivity(intent);
        }
        catch (Exception e)
        {
            Log.e("Exception occured: ", String.valueOf(e));
        }
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
            return true;
        }
        if(id ==  R.id.navigate)
        {
            startActivity(new Intent(this, SubActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public static void checkButton()
    {

    }
    public static void openActivity(int i, Context c)
    {
        Toast.makeText(null,i,Toast.LENGTH_SHORT).show();
        if(i==1)
        {
            Intent it = new Intent(c,SubActivity.class);
            c.startActivity(it);
        }
        if(i==2)
        {
            Intent it = new Intent(c,viewProducts.class);
            c.startActivity(it);
        }
    }
    public void onLocationChanged(Location location) {
        //Toast.makeText(Homescreen.this,"In OnLocationChanged",Toast.LENGTH_SHORT).show();
        location.getLatitude();
        location.getLongitude();
        l=location;

        String myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();
        TextView tv = (TextView) findViewById(R.id.locationText);
        tv.setText(myLocation);

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                //for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                  //  strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                //}
                strReturnedAddress.append(returnedAddress.getAddressLine(1)).append("\n");
                tv.setText(strReturnedAddress.toString());

                dbHandler = new LocationDatabaseHandler(getApplicationContext());
                LocationHistory lh = new LocationHistory(0,strReturnedAddress.toString(),String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
                dbHandler.createLocation(lh);
                locations.add(lh);
                List<LocationHistory> addableLocations = dbHandler.getAllLocations();
                int count = dbHandler.getLocationsCount();
                for(int i=0;i<count;i++)
                {
                    locations.add(addableLocations.get(i));
                }
                for(int i=0;i<count;i++)
                {
                    List<LocationHistory> l = dbHandler.getAllLocations();
                    LocationHistory lh2 = l.get(i);
                    String s = lh2.getlName();
                    //Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
                }



                String locations2[] = new String[dbHandler.getLocationsCount()];
                for(int i=0;i<dbHandler.getLocationsCount();i++)
                {
                    List<LocationHistory> l = dbHandler.getAllLocations();
                    LocationHistory lh2 = l.get(i);
                    locations2[i] = lh2.getlName();

                }

                //textView2.showDropDown();

            }
            else
            {
                tv.setText("No Address returned!");
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            tv.setText("Canont get Address!");
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
