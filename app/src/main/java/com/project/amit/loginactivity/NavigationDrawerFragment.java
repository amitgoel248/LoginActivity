package com.project.amit.loginactivity;


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
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements adapter_nav.ClickListener{



    private RecyclerView recyclerView;
    public static final String PREF_FILE_NAME ="testpref";
    public static final String KEY_USER_LEARNED_DRAWER ="user_learned_drawer";
    private ActionBarDrawerToggle mdrawertoggle;
    private DrawerLayout mdrawerlayout;
    private adapter_nav adapter;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mUserLearnedDrawer=Boolean.valueOf( readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if(savedInstanceState!=null)
        {
            mFromSavedInstanceState=true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.drawerList);
        adapter=new adapter_nav(getActivity(),getdata());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;

    }
    public static List<Information_nav> getdata()
    {
        List<Information_nav> data=new ArrayList<>();
        int[] icons={R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
        String[] titles={"Levels","Contests","Question of the day","Todo","Credits","Developers"};
        for(int i=0;i<titles.length && i<icons.length;i++)
        {
           Information_nav current=new Information_nav();
            current.iconid=icons[i];
            current.title=titles[i];
            data.add(current);
        }
        return data;
    }

    public void setup(int fragmentid,DrawerLayout drawerLayout,Toolbar toolbar)
    {
        containerView=getActivity().findViewById(fragmentid);
        mdrawerlayout=drawerLayout;
        mdrawertoggle=new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer)
                {
                    mUserLearnedDrawer=true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if(!mUserLearnedDrawer && !mFromSavedInstanceState)
        {
           mdrawerlayout.openDrawer(containerView);
        }

        mdrawerlayout.setDrawerListener(mdrawertoggle);
        mdrawerlayout.post(new Runnable() {
            @Override
            public void run() {
                  mdrawertoggle.syncState();
            }
        });
    }
    public void saveToPreferences(Context context,String preferenceName,String preferenceValue)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
    public String readFromPreferences(Context context, String preferenceName, String defaultValue)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

    @Override
    public void itemClicked(View view, int position) {
    }
}
