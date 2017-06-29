package com.stgobain.samuha.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.stgobain.samuha.R;
import com.stgobain.samuha.fragments.EventFragmet;
import com.stgobain.samuha.fragments.HUBFragment;
import com.stgobain.samuha.fragments.HomeFragment;
import com.stgobain.samuha.fragments.MascotFragment;
import com.stgobain.samuha.fragments.MemoriesFragment;
import com.stgobain.samuha.fragments.SABFragment;
import com.stgobain.samuha.fragments.TeamFragment;
import com.stgobain.samuha.fragments.ThemeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vignesh on 15-06-2017.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String HOME = "Home";
    private static final String THEME ="Theme";
    private static final String TEAM ="Teams";
    private static final String EVENTS = "Events";
    private static final String MASCOTS = "Mascots";
    private static final String MEMORIES = "Memories";
    private static final String HUB = "HUB";
    private static final String SAB = "SAB";

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
       /* View view = findViewById(android.R.id.content);
        Animation mLoadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        mLoadAnimation.setDuration(2000);
        view.startAnimation(mLoadAnimation);*/
        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setLogo(R.drawable.samuha_logo);
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(5);
        tabLayout.setupWithViewPager(viewPager);

    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), HOME);
        adapter.addFragment(new ThemeFragment(),THEME);
        adapter.addFragment(new MascotFragment(), MASCOTS);
        adapter.addFragment(new TeamFragment(),TEAM);
        adapter.addFragment(new EventFragmet(), EVENTS);
        adapter.addFragment(new MemoriesFragment(),MEMORIES);
        adapter.addFragment(new HUBFragment(),HUB);
        adapter.addFragment(new SABFragment(),SAB);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
