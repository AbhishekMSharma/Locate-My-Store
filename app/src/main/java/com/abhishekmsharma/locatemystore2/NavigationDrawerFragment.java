package com.abhishekmsharma.locatemystore2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment{

    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LMSAdapter adapter;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstance;

    private View containerView;

    private RecyclerView recyclerView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       mUserLearnedDrawer = Boolean.valueOf(readFromPreference(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));

        if(savedInstanceState!=null)
        {
            mFromSavedInstance = true;
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new LMSAdapter(getActivity(),getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;

    }

    public static List<Information> getData()
    {
        List<Information> data = new ArrayList<>();
        int [] icons = {R.drawable.ic_action_picture,R.drawable.ic_action_favorite,
         R.drawable.ic_action_camera,R.drawable.ic_action_map,
                R.drawable.ic_action_about};
        String[] title = {"View Products","My Favorites",
                "Scan Barcode","Location History","About"};
        for(int i=0;i<icons.length && i<title.length;i++)
        {
            Information current = new Information();
            current.iconId = icons[i];
            current.title = title[i];
            data.add(current);
        }
        return data;
    }

    public void setUp(int fragmentID, DrawerLayout drawerLayout, final Toolbar toolbar)
    {
        containerView = getActivity().findViewById(fragmentID);
        mDrawerLayout=drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if(!mUserLearnedDrawer)
                {
                    mUserLearnedDrawer = true;
                    saveToPreference(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if(slideOffset<0.3) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };


        if(!mUserLearnedDrawer && !mFromSavedInstance)
        {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreference(Context context,String preferenceName,String preferenceValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreference(Context context,String preferenceName,String defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

}
