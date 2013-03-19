package com.example.taximap.navigation;

import java.util.HashMap;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class MultiTabActivity extends FragmentActivity {
	private TabHost tabHost = null;
	private TabManager tabManager = null;

	@Override
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_frame);
	tabHost = (TabHost)findViewById(android.R.id.tabhost);
	tabHost.setup();
	//Initialize the tabs
	this.tabManager = new TabManager(this, this.tabHost, android.R.id.tabcontent);
	Bundle args = new Bundle();
	TabSpec tabspec;
	//first frame
	args.putInt(¡°layoutResource¡±, R.layout.test_frame);
	tabspec = this.tabHost.newTabSpec(¡°Tab1¡å);
	tabspec.setIndicator(¡°Tab1¡å);
	this.tabManager.addTab(tabspec, TabFragment.class, args);
	//second frame
	args = new Bundle();
	args.putInt(¡°layoutResource¡±, R.layout.test_frame2);
	tabspec = this.tabHost.newTabSpec(¡°Tab2¡å);
	tabspec.setIndicator(¡°Tab2¡å);
	this.tabManager.addTab(tabspec, TabFragment.class, args);
	//set current tab is not exist
	if (savedInstanceState != null){
	this.tabHost.setCurrentTab(savedInstanceState.getInt(¡°tab¡±));
	}else{
	this.tabHost.setCurrentTab(0);
	}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
	super.onSaveInstanceState(outState);
	outState.putString(¡°tab¡±, this.tabHost.getCurrentTabTag());
}

	/**
	 * helper class
	 * 
	 * @author
	 * 
	 */
	public static class TabManager implements OnTabChangeListener {
		private FragmentActivity activity = null;
		private TabHost tabhost = null;
		private int containerId = 0;
		private final HashMap<String, TabInfo> tabInfoMap = new HashMap<String, TabInfo>();
		private TabInfo currentTab = null;

		/**
		 * TabInfo including tab tag, tab class, tab arguments, tab fragment
		 * 
		 * @author
		 * 
		 */
		static final class TabInfo {
			private final String tag;
			private final Class<?> klass;
			private final Bundle args;
			private Fragment fragment = null;

			TabInfo(String tag, Class<?> klass, Bundle args) {
				this.tag = tag;
				this.klass = klass;
				this.args = args;
			}
		}

		/**
		 * A dummy tab by default, later it will replace by fragment
		 * 
		 * @author
		 * 
		 */
		static class DummyTabFactory implements TabContentFactory {
			private Context context;

			DummyTabFactory(Context context) {
				this.context = context;
			}

			public View createTabContent(String tag) {
				View view = new View(this.context);
				view.setMinimumHeight(0);
				view.setMinimumWidth(0);
				return view;
			}
		}

		public TabManager(FragmentActivity activity, TabHost tabhost,
				int containerId) {
			this.activity = activity;
			this.tabhost = tabhost;
			this.containerId = containerId;
			this.tabhost.setOnTabChangedListener(this);
		}

		public void addTab(TabSpec tabspec, Class<?> klass, Bundle args) {
			tabspec.setContent(new DummyTabFactory(this.activity));
			String tag = tabspec.getTag();
			TabInfo tabInfo = new TabInfo(tag, klass, args);
			// check is the tab already exist. If so deactivate it bcoz the
			// initiate state is not show.
			tabInfo.fragment = this.activity.getSupportFragmentManager()
					.findFragmentByTag(tag);
			if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
				FragmentTransaction transaction = this.activity
						.getSupportFragmentManager().beginTransaction();
				transaction.detach(tabInfo.fragment).commit();
			}
			this.tabInfoMap.put(tag, tabInfo);
			this.tabhost.addTab(tabspec);
		}

		public void onTabChanged(String tag) {
			TabInfo newTab = this.tabInfoMap.get(tag);
			if (this.currentTab == newTab) {
				return;
			}
			FragmentTransaction transaction = this.activity
					.getSupportFragmentManager().beginTransaction();
			// detach current tab
			if (this.currentTab != null && this.currentTab.fragment != null) {
				transaction.detach(this.currentTab.fragment);
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this.activity,
							newTab.klass.getName(), newTab.args);
					transaction.add(this.containerId, newTab.fragment,
							newTab.tag);
				} else {
					transaction.attach(newTab.fragment);
				}
			}
			this.currentTab = newTab;
			transaction.commit();
			this.activity.getSupportFragmentManager()
					.executePendingTransactions();
		}
	}

	/**
	 * The Tab Fragment
	 * 
	 * @author
	 * 
	 */
	public static class TabFragment extends Fragment {
		private int layoutResource;

		/**
		 * this method will be called when call Fragment.
		 * 
		 * @param resource
		 * @return
		 */
		static TabFragment newInstance(int resource){
	TabFragment fragment = new TabFragment();
	Bundle args = new Bundle();
	args.putInt(¡°layoutResource¡±, resource);
	fragment.setArguments(args);
	return fragment;
	}

		public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	Bundle args = this.getArguments();
	layoutResource = args.getInt(¡°layoutResource¡±);
	}

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(layoutResource, container, false);
		}
	}
}
