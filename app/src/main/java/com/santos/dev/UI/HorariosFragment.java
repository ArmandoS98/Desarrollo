package com.santos.dev.UI;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.santos.dev.Adapters.FragmentsTabAdapter;
import com.santos.dev.MainActivity;
import com.santos.dev.R;
import com.santos.dev.UI.Calendario.JuevesFragment;
import com.santos.dev.UI.Calendario.LunesFragment;
import com.santos.dev.UI.Calendario.MartesFragment;
import com.santos.dev.UI.Calendario.MiercolesFragment;
import com.santos.dev.UI.Calendario.ViernesFragment;
import com.santos.dev.Utils.AlertDialogsHelper;
import com.santos.dev.Utils.SQLiteFukes.DatabaseHelper;
import com.santos.firebasecomponents.Models.Semana;
import com.santos.firebasecomponents.Models.Week;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class HorariosFragment extends Fragment {
    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    private boolean switchSevenDays;
    private DatabaseHelper databaseHelper;
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
        //setupCustomDialog(view);
        if (switchSevenDays) changeFragments(true, view);

        databaseHelper = new DatabaseHelper(getContext());
        /*AddData("1", "Dibujo Tecnico", "Clase con el inge", "5:30 a 7:30");
        AddData("2", "Dibujo Tecnico", "Clase con el inge", "5:30 a 7:30");
        AddData("3", "Dibujo Tecnico", "Clase con el inge", "5:30 a 7:30");
        AddData("4", "Dibujo Tecnico", "Clase con el inge", "5:30 a 7:30");
        AddData("5", "Dibujo Tecnico", "Clase con el inge", "5:30 a 7:30");*/

        return view;
    }

    public void AddData(Semana semana) {
        boolean insertData = databaseHelper.addData(semana);
        if (insertData)
            Toast.makeText(getContext(), "Informacion Guardada", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                animateFab(tab.getPosition());
                mFloatingActionButton.setOnClickListener(v -> guardarCursoHorario(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        tabLayout.setupWithViewPager(viewPager);
    }

    private void guardarCursoHorario(int position) {
        final View alertLayout = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(R.string.add_subject);
        alert.setCancelable(false);
        Button cancel = alertLayout.findViewById(R.id.cancel);
        Button submit = alertLayout.findViewById(R.id.save);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();

        final HashMap<Integer, EditText> editTextHashs = new HashMap<>();
        final EditText subject = alertLayout.findViewById(R.id.subject_dialog);
        editTextHashs.put(R.string.subject, subject);
        final EditText teacher = alertLayout.findViewById(R.id.teacher_dialog);
        editTextHashs.put(R.string.teacher, teacher);
        final EditText room = alertLayout.findViewById(R.id.room_dialog);
        editTextHashs.put(R.string.room, room);
        final TextView from_time = alertLayout.findViewById(R.id.from_time);
        final TextView to_time = alertLayout.findViewById(R.id.to_time);
        //final Button select_color = alertLayout.findViewById(R.id.select_color);
        final Week week = new Week();
        Semana semana = new Semana();

        from_time.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    (view, hourOfDay, minute) -> {
                        from_time.setText(String.format("%02d:%02d", hourOfDay, minute));
                        semana.setHora_de(String.format("%02d:%02d", hourOfDay, minute));
                    }, mHour, mMinute, true);
            timePickerDialog.setTitle(R.string.choose_time);
            timePickerDialog.show();
        });

        to_time.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    (view, hourOfDay, minute1) -> {
                        to_time.setText(String.format("%02d:%02d", hourOfDay, minute1));
                        semana.setHora_a(String.format("%02d:%02d", hourOfDay, minute1));
                    }, hour, minute, true);
            timePickerDialog.setTitle(R.string.choose_time);
            timePickerDialog.show();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        submit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(subject.getText()) || TextUtils.isEmpty(teacher.getText()) || TextUtils.isEmpty(room.getText())) {
                for (Map.Entry<Integer, EditText> entry : editTextHashs.entrySet()) {
                    if (TextUtils.isEmpty(entry.getValue().getText())) {
                        entry.getValue().setError(getActivity().getResources().getString(entry.getKey()) + " " + getActivity().getResources().getString(R.string.field_error));
                        entry.getValue().requestFocus();
                    }
                }
            } else if (!from_time.getText().toString().matches(".*\\d+.*") || !to_time.getText().toString().matches(".*\\d+.*")) {
                Snackbar.make(alertLayout, R.string.time_error, Snackbar.LENGTH_LONG).show();
            } else {
                //DbHelper dbHelper = new DbHelper(activity);
                //Matcher fragment = Pattern.compile("(.*Fragment)").matcher(adapter.getItem(viewPager.getCurrentItem()).toString());
                //ColorDrawable buttonColor = (ColorDrawable) select_color.getBackground();
                semana.setCurso(subject.getText().toString());
                semana.setDia(String.valueOf(position));
                semana.setCatedratico(teacher.getText().toString());
                semana.setSalon(room.getText().toString());
                // week.setColor(buttonColor.getColor());
                //databaseHelper.addData(semana);
                AddData(semana);
                adapter.notifyDataSetChanged();

                subject.getText().clear();
                teacher.getText().clear();
                room.getText().clear();
                from_time.setText(R.string.select_time);
                to_time.setText(R.string.select_time);
                //select_color.setBackgroundColor(Color.WHITE);
                subject.requestFocus();
                dialog.dismiss();
            }
        });
        dialog.show();
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

    /*private void setupCustomDialog(View view) {
        final View alertLayout = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        AlertDialogsHelper.getAddSubjectDialog((Activity) view.getContext(), alertLayout, adapter, viewPager);
    }*/

    private void setupSevenDaysPref() {
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //switchSevenDays = sharedPref.getBoolean(SettingsActivity.KEY_SEVEN_DAYS_SETTING, false);
    }


    int[] colorIntArray = {R.color.linkBlue, R.color.red3, R.color.linkBlue, R.color.red3, R.color.linkBlue};
    int[] iconIntArray = {R.drawable.ic_add_black_24dp, R.drawable.ic_add_black_24dp, R.drawable.ic_add_black_24dp, R.drawable.ic_add_black_24dp, R.drawable.ic_add_black_24dp};

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
