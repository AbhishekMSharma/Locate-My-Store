package com.abhishekmsharma.locatemystore2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Abhishek Sharma on 14-04-2015.
 */
public class ConsumeWebSoapServiceObject extends AsyncTask<Void, Void, SoapObject> {


    private static String METHOD_NAME = "";


    private static String SOAP_ACTION = "http://tempuri.org/";

    private static ArrayList<Object> parameters ;
    private static ArrayList<Object> names;
    private static String p;
    private static String n;


    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://webapplication37412.azurewebsites.net/WebService1.asmx";
    SoapObject result;
    SoapPrimitive soapPrimitive;
    private ProgressDialog pDialog;
    private Context c;


    public ConsumeWebSoapServiceObject(String s,ArrayList<Object> parameters,ArrayList<Object> names) {
        //c = context;
        this.METHOD_NAME = s;
        this.parameters = parameters;

        this.names=names;
        this.p = p;
        this.n = n;
        SOAP_ACTION = "http://tempuri.org/";
        SOAP_ACTION = SOAP_ACTION + METHOD_NAME;


    }



    @Override
    protected SoapObject doInBackground(Void... params) {
        {


            try
            {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                for(int i=0;i<this.parameters.size();i++)
                {

                    request.addProperty(this.names.get(i)+"", this.parameters.get(i)+"");

                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);

                result=(SoapObject)envelope.getResponse();


                // result=(SoapPrimitive)envelope.getResponse();
                //   Log.i("consumeSoapWebservice", "respone" + soapPrimitive.toString());

            } catch (Exception e)
            {
                Log.i("consumeSoapWebservice", "exception" + e.toString());

            }
        }
        return  result;

    }
}
