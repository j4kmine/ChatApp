package com.example.asus.chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ASUS on 3/27/2018.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                RequestsFragment requestsFragment =new RequestsFragment();
                return requestsFragment;
            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 2:
                RequestsFragment requestsFragment1 = new RequestsFragment();
                return  requestsFragment1;
            default:
                return  null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }
    //magic method in giving name
    public  CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "REQUEST";
            case 1:
                return "CHATS";
            case 2:
                return "FRIEND";
            default:
                return null;
        }

    }
}
