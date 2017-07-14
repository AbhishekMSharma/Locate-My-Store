package com.abhishekmsharma.locatemystore2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class viewProducts extends ActionBarActivity {
    String [] products;
    String [] stores;
    String [] addresses;
    String [] contact;
    String [] lati;
    String [] longi;
    String [] categories;
    private static Product [] listOfProducts1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<Object> names = new ArrayList<>();
        AsyncTask<Void, Void, SoapObject> se = new ConsumeWebSoapServiceObject("getProductNames", parameters, names);
        se.execute();
        try {
            int tmp = se.get().getPropertyCount();
            Log.e("Tmp size ", tmp + "");
            products = new String[tmp];
            categories = new String[tmp+1];
            categories[0] = "All";
            listOfProducts1 = new Product[tmp];
            for (int i = 0; i < tmp; i++) {
                listOfProducts1[i] = new Product();
                listOfProducts1[i].p_Name = ((SoapObject) (se.get().getProperty(i))).getProperty(0).toString();
                listOfProducts1[i].p_Id = ((SoapObject) (se.get().getProperty(i))).getProperty(1).toString();
                listOfProducts1[i].p_Specs = ((SoapObject) (se.get().getProperty(i))).getProperty(2).toString();
                listOfProducts1[i].p_Category = ((SoapObject) (se.get().getProperty(i))).getProperty(3).toString();
                products[i] = listOfProducts1[i].p_Name;
                categories[i+1] = listOfProducts1[i].p_Category;
            }
        } catch (Exception e) {
            Log.e("Exception ", e + "");
        }

        String newCategories[] = removeDuplicates(categories);
        ListAdapter theAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, newCategories);


        Spinner spinner = (Spinner) findViewById(R.id.categoryDropDown);
        spinner.setAdapter((android.widget.SpinnerAdapter) theAdapter1);


        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, products);


        ListView theListView = (ListView) findViewById(R.id.listView);
        fillList();

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String searchQuery="";
                String pname="";
                String pdesc="";
                String enteredText = String.valueOf(adapterView.getItemAtPosition(i));
                for (int j = 0; j < products.length; j++) {
                   // Log.e("Product name is " + listOfProducts1[j].p_Name, "Product entered is " + textView.getText());
                    if (enteredText.equals(listOfProducts1[j].p_Name)) {
                        searchQuery = listOfProducts1[j].p_Id;
                        pname = listOfProducts1[j].p_Name;
                        pdesc = listOfProducts1[j].p_Specs;
                        break;
                    }
                }


                ArrayList<Object> parameters = new ArrayList<>();
                ArrayList<Object> names = new ArrayList<>();

                parameters.add(0,searchQuery);
                names.add(0,"Product_ID");
                AsyncTask<Void, Void, SoapObject> se=new ConsumeWebSoapServiceObject("StoreIDList",parameters,names);
                se.execute();

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
                    for (int j = 0; j < tmp; j++)
                    {
                        abc = ((SoapObject)(se.get().getProperty(j))).getProperty(1).toString();
                        stores[j] = abc;
                        abc = ((SoapObject)(se.get().getProperty(j))).getProperty(2).toString();
                        addresses[j] = abc;
                        abc = ((SoapObject)(se.get().getProperty(j))).getProperty(3).toString();
                        contact[j] = abc;
                        abc = ((SoapObject)(se.get().getProperty(j))).getProperty(5).toString();
                        lati[j] = abc;
                        abc = ((SoapObject)(se.get().getProperty(j))).getProperty(6).toString();
                        longi[j] = abc;
                    }

                    Intent intent = new Intent(getApplicationContext(),searchResults.class);
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
        });

        Button b1 = (Button) findViewById(R.id.searchButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner s1 = (Spinner) findViewById(R.id.categoryDropDown);
                if(String.valueOf(s1.getSelectedItem()).equals("All"))
                {
                    ArrayList<Object> parameters = new ArrayList<>();
                    ArrayList<Object> names = new ArrayList<>();


                    AsyncTask<Void, Void, SoapObject> se = new ConsumeWebSoapServiceObject("getProductNames", parameters, names);
                    se.execute();
                    try {
                        int tmp = se.get().getPropertyCount();
                        Log.e("Tmp size ", tmp + "");
                        products = new String[tmp];
                        listOfProducts1 = new Product[tmp];
                        for (int i = 0; i < tmp; i++) {
                            listOfProducts1[i] = new Product();
                            listOfProducts1[i].p_Name = ((SoapObject) (se.get().getProperty(i))).getProperty(0).toString();
                            listOfProducts1[i].p_Id = ((SoapObject) (se.get().getProperty(i))).getProperty(1).toString();
                            listOfProducts1[i].p_Specs = ((SoapObject) (se.get().getProperty(i))).getProperty(2).toString();
                            listOfProducts1[i].p_Category = ((SoapObject) (se.get().getProperty(i))).getProperty(3).toString();
                            products[i] = listOfProducts1[i].p_Name;
                        }
                        fillList();
                    } catch (Exception e) {
                        Log.e("Exception ", e + "");
                    }
                }
                else {
                    ArrayList<Object> parameters = new ArrayList<>();
                    ArrayList<Object> names = new ArrayList<>();


                    parameters.add(0, String.valueOf(s1.getSelectedItem()));
                    names.add(0, "category");

                    AsyncTask<Void, Void, SoapObject> se = new ConsumeWebSoapServiceObject("getProductNamesByCategory", parameters, names).execute();
                    try {
                        int tmp = se.get().getPropertyCount();
                        Log.e("Tmp size ", tmp + "");
                        products = new String[tmp];
                        listOfProducts1 = new Product[tmp];
                        for (int i = 0; i < tmp; i++) {
                            listOfProducts1[i] = new Product();
                            listOfProducts1[i].p_Name = ((SoapObject) (se.get().getProperty(i))).getProperty(0).toString();
                            listOfProducts1[i].p_Id = ((SoapObject) (se.get().getProperty(i))).getProperty(1).toString();
                            listOfProducts1[i].p_Specs = ((SoapObject) (se.get().getProperty(i))).getProperty(2).toString();
                            listOfProducts1[i].p_Category = ((SoapObject) (se.get().getProperty(i))).getProperty(3).toString();
                            products[i] = listOfProducts1[i].p_Name;
                        }
                        fillList();
                    } catch (Exception e) {
                        Log.e("Exception ", e + "");
                    }
                }
            }
        });

        Spinner s1 = (Spinner) findViewById(R.id.categoryDropDown);

    }

    public void fillList()
    {
        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, products);
        ListView theListView = (ListView) findViewById(R.id.listView);
        theListView.setAdapter(theAdapter);
    }


    public static String[] removeDuplicates(String[] arr)
    {
        Set<String> alreadyPresent = new HashSet<>();
        String[] whitelist = new String[arr.length];
        int i = 0;

        for (String element : arr) {
            if (alreadyPresent.add(element)) {
                whitelist[i++] = element;
            }
        }

        return Arrays.copyOf(whitelist, i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_view_products, menu);
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
