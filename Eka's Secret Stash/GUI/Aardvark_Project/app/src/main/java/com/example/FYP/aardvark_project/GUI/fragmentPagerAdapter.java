package com.example.FYP.aardvark_project.GUI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for managing tabbed view in Project View Activity
 * */
public class fragmentPagerAdapter extends FragmentPagerAdapter
{
    private final List<String> tabTitleList = new ArrayList<>();
    private final List<Fragment> tabFragmentList = new ArrayList<>();

    public fragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {return tabTitleList.get(position);}

    @Override
    public Fragment getItem(int position)
    {
        return tabFragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return tabFragmentList.size();
    }

    /**creates a new tab, it takes in the content of the tab and the title*/
    public void addFragment(Fragment fragment, String title) {
        tabFragmentList.add(fragment);
        tabTitleList.add(title);
    }
}
