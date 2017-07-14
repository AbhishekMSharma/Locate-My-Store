package com.abhishekmsharma.locatemystore2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Sharma on 15-04-2015.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    static String[] storeNames;
    static String[] addresses;
    static String[] contact;
    static String[] lati;
    static String[] longi;
    static String pname;
    static String pdesc;
    private static Context context;
    int abc;

    public ContactAdapter(Context context, String[] storeNames, String[] addresses, String[] contact,
                          String[] lati, String[] longi, String pname, String pdesc)
    {
        this.context = context;
        this.storeNames = storeNames;
        this.addresses = addresses;
        this.contact = contact;
        this.lati = lati;
        this.longi = longi;
        this.pname = pname;
        this.pdesc = pdesc;
    }


    @Override
    public int getItemCount() {
        try {
            for (int i = storeNames.length - 1; i >= 0; i--)
                Log.e("Store name is: ", storeNames[i]);
        } catch (Exception e) {
            Log.e("LOLWA ", "" + e);
        }
        return storeNames.length;
    }
    public String calculateDistance(String a, String b)
    {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Location near_locations=new Location("locationA");
        double a2 = Double.parseDouble(a);
        double b2 = Double.parseDouble(b);
        near_locations.setLatitude(a2);
        near_locations.setLongitude(b2);

        double distance=location.distanceTo(near_locations);
        distance = distance/1000;
        String s = String.valueOf(distance);
        s = s.substring(0,7);
        s = s+" km";
        return s;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i)
    {
        String s = storeNames[i];
        contactViewHolder.vName.setText(s);
        s = addresses[i];
        contactViewHolder.address.setText(s);
        s=contact[i];
        contactViewHolder.contact.setText(s);
        s = calculateDistance(lati[i],longi[i]);
        contactViewHolder.distance.setText(s);

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.searchrow, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vName;
        protected TextView address;
        protected TextView contact;
        protected TextView distance;

        public ContactViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            vName = (TextView) v.findViewById(R.id.title);
            address = (TextView) v.findViewById(R.id.addrText);
            contact = (TextView) v.findViewById(R.id.contactText);
            distance = (TextView) v.findViewById(R.id.txtName);

        }

        @Override
        public void onClick(View view) {
            Log.e("Hello", "World");
            //searchResults s = new searchResults();
            //s.showDialog();
            final int a = getPosition();
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                    context);
            builderSingle.setTitle(storeNames[a]);
            //builderSingle.setMessage("Hello\n1\n2");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_list_item_1);
            arrayAdapter.add("View in Map");
            arrayAdapter.add("Call Store");

            builderSingle.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            if(strName.equals("View in Map"))
                            {
                                //String s = "http://maps.google.com/?q=" + lati[a] + "," + longi[a];
                                //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s)));


                                AlertDialog.Builder builderSingle2 = new AlertDialog.Builder(
                                        context);
                                builderSingle2.setTitle("Choose Map Type");
                                //builderSingle.setMessage("Hello\n1\n2");
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        context,
                                        android.R.layout.simple_list_item_1);
                                arrayAdapter.add("Map View");
                                arrayAdapter.add("Satellite View");
                                arrayAdapter.add("Hybrid View");
                                builderSingle2.setAdapter(arrayAdapter,
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String strName = arrayAdapter.getItem(which);
                                                if(strName.equals("Map View"))
                                                {
                                                    String s = "http://maps.google.com/maps?z=14&t=m&q=" + lati[a] + "," + longi[a];
                                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s)));
                                                }
                                                else if(strName.equals("Satellite View"))
                                                {
                                                    String s = "http://maps.google.com/maps?z=14&t=k&q=" + lati[a] + "," + longi[a];
                                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s)));
                                                }
                                                else if(strName.equals("Hybrid View"))
                                                {
                                                    String s = "http://maps.google.com/maps?z=14&t=h&q=" + lati[a] + "," + longi[a];
                                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s)));
                                                }
                                            }
                                        });
                                builderSingle2.show();


                            }
                            else if(strName.equals("Call Store"))
                            {
                                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:9833378516")));
                            }
                        }
                    });
            builderSingle.show();

        }
    }
}


