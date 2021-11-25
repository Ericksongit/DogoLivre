package com.example.dogolivre.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dogolivre.R;
import com.example.dogolivre.helper.ConfiguracaoFirebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("DogFlix");
        setSupportActionBar(toolbar);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);
        NavController navCo = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navCo);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.feedFragment, R.id.pesquisaFragment, R.id.postagemFragment, R.id.perfilFragment).build();

        NavigationUI.setupActionBarWithNavController(this, navCo, appBarConfiguration);

        //configuraBottomNavigationView();

//        getSupportFragmentManager().beginTransaction().replace(R.id.viewPage, new FeedFragment()).commit();
//        bottomNavigation.setSelectedItemId(R.id.ic_home);
//
//        bottomNavigation.setOnItemSelectedListener(item -> {
//            Fragment fragment = null;
//
//            switch (item.getItemId()) {
//                case R.id.ic_home:
//                    fragment = new FeedFragment();
//                    break;
//            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.viewPage, fragment).commit();
//            return true;
//
//        });



    }
//    private void configuraBottomNavigationView() {
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
//    }

//    private void habilitarNavegação (BottomNavigationView vi) {
//        vi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                switch (item.getItemId()) {
//                    case R.id.ic_home:
//                        fragmentTransaction.replace(R.id.viewPage, new Fragment()).commit();
//                        return true;
//                }
//
//                return false;
//            }
//        });
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sair:
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}