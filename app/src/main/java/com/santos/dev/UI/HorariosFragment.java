package com.santos.dev.UI;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.santos.dev.Adapters.FragmentsTabAdapter;
import com.santos.dev.MainActivity;
import com.santos.dev.R;
import com.santos.dev.UI.Calendario.JuevesFragment;
import com.santos.dev.UI.Calendario.LunesFragment;
import com.santos.dev.UI.Calendario.MartesFragment;
import com.santos.dev.UI.Calendario.MiercolesFragment;
import com.santos.dev.UI.Calendario.ViernesFragment;
import com.santos.dev.Utils.AlertDialogsHelper;

import java.util.Calendar;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class HorariosFragment extends Fragment {
    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    private boolean switchSevenDays;
    private FloatingActionButton mFloatingActionButton;

    public HorariosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_horarios, container, false);
        mFloatingActionButton = view.findViewById(R.id.fabChat);
        setupFragments(view);
        //setupCustomDialog();
        if (switchSevenDays) changeFragments(true, view);


        return view;
    }

    private void setupFragments(View view) {
        adapter = new FragmentsTabAdapter(getFragmentManager());
        viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        adapter.addFragment(new LunesFragment(), "Lunes");
        adapter.addFragment(new MartesFragment(), "Martes");
        adapter.addFragment(new MiercolesFragment(), "Miercoles");
        adapter.addFragment(new JuevesFragment(), "Jueves");
        adapter.addFragment(new ViernesFragment(), "Viernes");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(day == 1 ? 6 : day - 2, true);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void changeFragments(boolean isChecked, View view) {
        if (isChecked) {
            TabLayout tabLayout = view.findViewById(R.id.tabLayout);
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            adapter.addFragment(new ConversionesFragment(), "Sabado");
            adapter.addFragment(new ConversionesFragment(), "Domingo");
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(day == 1 ? 6 : day - 2, true);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            if (adapter.getFragmentList().size() > 5) {
                adapter.removeFragment(new ConversionesFragment(), 5);
                adapter.removeFragment(new ConversionesFragment(), 5);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setupCustomDialog() {
        final View alertLayout = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        AlertDialogsHelper.getAddSubjectDialog(getActivity(), alertLayout, adapter, viewPager);
    }

    private void setupSevenDaysPref() {
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //switchSevenDays = sharedPref.getBoolean(SettingsActivity.KEY_SEVEN_DAYS_SETTING, false);
    }


    int[] colorIntArray = {R.color.black_overlay,R.color.red3,R.color.linkBlue};
    int[] iconIntArray = {R.drawable.ic_close_black_24dp, R.drawable.ic_comment_black_24dp, R.drawable.ic_conversiones};

    protected void animateFab(final int position) {
        mFloatingActionButton.clearAnimation();

        // Scale down animation
        ScaleAnimation shrink = new ScaleAnimation(1f, 0.1f, 1f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(100);     // animation duration in milliseconds
        shrink.setInterpolator(new AccelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change FAB color and icon
                mFloatingActionButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), colorIntArray[position]));
                mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), iconIntArray[position]));

                // Rotate Animation
                Animation rotate = new RotateAnimation(60.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                rotate.setDuration(150);
                rotate.setInterpolator(new DecelerateInterpolator());

                // Scale up animation
                ScaleAnimation expand = new ScaleAnimation(0.1f, 1f, 0.1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(150);     // animation duration in milliseconds
                expand.setInterpolator(new DecelerateInterpolator());

                // Add both animations to animation state
                AnimationSet s = new AnimationSet(false); //false means don't share interpolators
                s.addAnimation(rotate);
                s.addAnimation(expand);
                mFloatingActionButton.startAnimation(s);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mFloatingActionButton.startAnimation(shrink);
    }
}
