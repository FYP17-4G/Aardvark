package com.example.ekanugrahapratama.aardvark_project;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class GUI_project_view extends AppCompatActivity
{
    private String projectUniqueID = new String();
    private String projectTitle = new String();

    private projectView_fragmentPagerAdapter projectViewFragmentAdapter;
    private ViewPager viewPager;

    private GUI_fragment_project_view mainView = new GUI_fragment_project_view();
    private GUI_fragment_project_view_permutation permView = new GUI_fragment_project_view_permutation();

    private App_Framework framework;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        framework = new App_Framework(this);

        //ACCESS THE PASSED PARAMETERS FROM GUI_adaptr.java
        projectUniqueID = getIntent().getStringExtra("project_view_unique_ID"); //USE THIS LATRE
        this.projectTitle = getIntent().getStringExtra("project_view_title");
        setTitle(projectTitle);

        projectViewFragmentAdapter = new projectView_fragmentPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewPager_projectView);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(tabSelectedListener);

    }

    private void setupViewPager(ViewPager viewPager)
    {
        projectViewFragmentAdapter.addFragment(mainView, "Main View");
        projectViewFragmentAdapter.addFragment(permView, "Permutation View");
        viewPager.setAdapter(projectViewFragmentAdapter);
    }

    /**Listener for when the tab is changed, main page in project view will pass the data to permutation view*/
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener()
    {
        @Override
        public void onTabSelected(TabLayout.Tab tab)
        {
            permView.setCipherText(mainView.getCipherText());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}
