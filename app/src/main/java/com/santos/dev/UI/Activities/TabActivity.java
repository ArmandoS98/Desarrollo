package com.santos.dev.UI.Activities;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.santos.dev.Interfaz.IMainMaestro;
import com.santos.dev.Models.Cursos;
import com.santos.dev.Models.Notas;
import com.santos.dev.R;
import com.santos.dev.UI.FormulasFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.santos.dev.MainActivity.KEY_NOTAS;

public class TabActivity extends AppCompatActivity implements IMainMaestro {

    private static final String TAG = "TabActivity";
    //Widgets
    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Cursos cursos = null;
        Intent i = getIntent();
        cursos = i.getParcelableExtra(KEY_NOTAS);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(cursos.getNombre_curso());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (cursos != null) {

            //Donde se carggaran las tabs
            viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
            setupViewPager(viewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    viewPager.setCurrentItem(tab.getPosition());
                    Log.d(TAG, "onTabSelected: pos: " + tab.getPosition());

                    switch (tab.getPosition()) {
                        case 0:
                            //Toast.makeText(GradoDetalleActivity.this, "Hola", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //Toast.makeText(MangaDetalleActivity.this, "Hola No.2", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            //Toast.makeText(MangaDetalleActivity.this, "Hola No.3", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            //Toast.makeText(MangaDetalleActivity.this, "Hola No.3", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //bundle
        /*Bundle bundle = new Bundle();
        bundle.putStringArrayList("DATE", materias);*/

        /*FormulasFragment tabTareasFragment = new FormulasFragment();

        tabTareasFragment.setArguments(bundle);*/
        //adapter.addFrag(tabTareasFragment, "Notas");
        adapter.addFrag(new FormulasFragment(), "Notas");
        adapter.addFrag(new FormulasFragment(), "Compatidos");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onNotaSeleccionada(Notas notas) {
        Log.d(TAG, "onNotaSeleccionada: Nota" + notas);
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra(KEY_NOTAS, notas);
        SimpleDateFormat spf = new SimpleDateFormat("dd MMM, yyyy, HH:mm aa");
        String date = spf.format(notas.getTimestamp());
        intent.putExtra("date", date);

        startActivity(intent);
    }

    @Override
    public void onNotaUpdate(Notas notas) {

    }

    @Override
    public void onCursotoNotaa(Cursos cursos) {

    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nueva_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Hla que hace", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(getApplicationContext(), NuevoCursooActivity.class));
            //showTheNewDialog(R.style.DialogScale);
            //mAdaptadorMaestrosCompleto.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
