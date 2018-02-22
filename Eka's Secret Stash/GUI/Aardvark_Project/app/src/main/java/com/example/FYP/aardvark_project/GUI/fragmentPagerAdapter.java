/**
 * Programmer: Eka Nugraha Pratama
 *
 * Responsible for managing tabbed view in Project View Activity
 *
 * ## Tabbed view only has "Fragment_project_view"
 * */

package com.example.FYP.aardvark_project.GUI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter
{
    private final List<String> tabTitleList = new ArrayList<>();
    private final List<Fragment> tabFragmentList = new ArrayList<>();

    public FragmentPagerAdapter(FragmentManager fm)
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
