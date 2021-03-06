package de.danoeh.antennapod.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import de.danoeh.antennapod.R;

public class EpisodesFragment extends Fragment {

    public static final String TAG = "EpisodesFragment";
    private static final String PREF_LAST_TAB_POSITION = "tab_position";

    private static final int POS_NEW_EPISODES = 0;
    private static final int POS_ALL_EPISODES = 1;
    private static final int POS_FAV_EPISODES = 2;
    private static final int TOTAL_COUNT = 3;


    private TabLayout tabLayout;
    private ViewPager viewPager;

    //Mandatory Constructor
    public EpisodesFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.episodes_label);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        viewPager = rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new EpisodesPagerAdapter());

        // Give the TabLayout the ViewPager
        tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save our tab selection
        SharedPreferences prefs = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_LAST_TAB_POSITION, tabLayout.getSelectedTabPosition());
        editor.apply();
    }

    @Override
    public void onStart() {
        super.onStart();

        // restore our last position
        SharedPreferences prefs = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        int lastPosition = prefs.getInt(PREF_LAST_TAB_POSITION, 0);
        viewPager.setCurrentItem(lastPosition);
    }

    public class EpisodesPagerAdapter extends FragmentPagerAdapter {

        public EpisodesPagerAdapter() {
            super(getChildFragmentManager());
        }

        @Override
        @NonNull
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new NewEpisodesFragment();
                case 1:
                    return new AllEpisodesFragment();
                default:
                    return new FavoriteEpisodesFragment();
            }
        }

        @Override
        public int getCount() {
            return TOTAL_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case POS_ALL_EPISODES:
                    return getString(R.string.all_episodes_short_label);
                case POS_NEW_EPISODES:
                    return getString(R.string.new_episodes_label);
                case POS_FAV_EPISODES:
                    return getString(R.string.favorite_episodes_label);
                default:
                    return super.getPageTitle(position);
            }
        }
    }
}
