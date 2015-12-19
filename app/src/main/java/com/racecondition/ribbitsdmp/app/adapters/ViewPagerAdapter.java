package com.racecondition.ribbitsdmp.app.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.racecondition.ribbitsdmp.app.R;
import com.racecondition.ribbitsdmp.app.ui.FriendsFragment;
import com.racecondition.ribbitsdmp.app.ui.InboxFragment;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;


    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch(position) {
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
       /* Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2).toUpperCase(l);
        }*/
        return null;
    }

    public int getIcon(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_tab_inbox;
            case 1:
                return R.drawable.ic_tab_friends;
        }

            return R.drawable.ic_tab_inbox;
    }

}