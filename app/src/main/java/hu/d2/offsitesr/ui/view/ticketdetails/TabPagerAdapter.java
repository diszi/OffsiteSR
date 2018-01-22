package hu.d2.offsitesr.ui.view.ticketdetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csabinko on 2017.10.11..
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addTab(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    /*
    *  - tab title / the last tab will be icon
    */
    @Override
    public CharSequence getPageTitle(int position) {
        if (position ==3 ){
            return null;
        }else{
            return mFragmentTitleList.get(position);
        }
    }
}
