package edu.training.droidbountyhunter.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.training.droidbountyhunter.R;
import edu.training.droidbountyhunter.fragments.AboutFragment;
import edu.training.droidbountyhunter.fragments.ListFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private Fragment[] fragments;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        fragments = new Fragment[3];
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (fragments[position] == null){
            if (position < 2){
                fragments[position] = new ListFragment();
                Bundle arguments = new Bundle();
                arguments.putInt(ListFragment.ARG_SECTION_NUMBER, position);
                fragments[position].setArguments(arguments);
            }else {
                fragments[position] = new AboutFragment();
            }
        }
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_fugitive);
            case 1:
                return mContext.getString(R.string.title_captured);
            case 2:
                return mContext.getString(R.string.title_about);
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}
