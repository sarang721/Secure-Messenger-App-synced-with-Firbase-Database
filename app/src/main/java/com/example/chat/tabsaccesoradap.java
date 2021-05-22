package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class tabsaccesoradap extends FragmentPagerAdapter {


    public tabsaccesoradap(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                chatsfragment chat=new chatsfragment();
                return chat;
            case 1:
                requestsfragment requestfragment=new requestsfragment();
                return requestfragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Chats";
            case 1:
                return "Requests";
            default:
                return null;
        }

    }
}
