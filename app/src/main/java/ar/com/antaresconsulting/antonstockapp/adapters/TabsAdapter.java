package ar.com.antaresconsulting.antonstockapp.adapters;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;


public class TabsAdapter extends FragmentStatePagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
{
    private final Context mContext;
    private final TabHost mTabHost;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

    static final class TabInfo
    {
        private String tag;
        private Class<?> clss;
        private Bundle args;

        TabInfo(String _tag, Class<?> _class, Bundle _args)
        {
            tag = _tag;
            clss = _class;
            args = _args;
        }
        void updateArgs(Bundle argsNew){
        	args = argsNew;
        }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory
    {
        private final Context mContext;

        public DummyTabFactory(Context context)
        {
            mContext = context;
        }

        public View createTabContent(String tag)
        {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager)
    {
        super(activity.getFragmentManager());
        mContext = activity;
        mTabHost = tabHost;
        mViewPager = pager;
        mTabHost.setOnTabChangedListener(this);
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args)
    {
        tabSpec.setContent(new DummyTabFactory(mContext));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);
        mTabs.add(info);
        mTabHost.addTab(tabSpec);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }
    
    public void onTabChanged(String tabId)
    {
        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    public void onPageSelected(int position)
    {
        // Unfortunately when TabHost changes the current tab, it kindly
        // also takes care of putting focus on it when not in touch mode.
        // The jerk.
        // This hack tries to prevent this from pulling focus out of our
        // ViewPager.
        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);
        widget.setDescendantFocusability(oldFocusability);
    }
    
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    
    public void onPageScrollStateChanged(int state)
    {
    }
   
    public void clearAllTabs() {
		for (int i = 0; i < mTabs.size(); i++) {
			mTabs.remove(i);
		}
	}    
}