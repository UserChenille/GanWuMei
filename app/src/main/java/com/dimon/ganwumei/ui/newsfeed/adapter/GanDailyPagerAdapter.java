package com.dimon.ganwumei.ui.newsfeed.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dimon.ganwumei.ui.newsfeed.fragment.GanDailyFragment;
import com.socks.library.KLog;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * Created by Dimon on 2016/5/3.
 */
public class GanDailyPagerAdapter extends FragmentPagerAdapter {
    Date mDate;

    public GanDailyPagerAdapter(FragmentManager fm, Date date) {
        super(fm);
        mDate = date;
    }

    @Override
    public Fragment getItem(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        calendar.add(Calendar.DATE, -position);
        KLog.a(calendar.get(Calendar.YEAR) +"/"+ calendar.get(Calendar.MONTH)+ "/" + calendar.get(Calendar.DAY_OF_MONTH));
        return GanDailyFragment.newInstance(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int getCount() {
        return 5;
    }
}
