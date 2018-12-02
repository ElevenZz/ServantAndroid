package servant.servantandroid.view;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.TextView;

import servant.servantandroid.R;
import servant.servantandroid.internal.ModuleTree.Module;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.viewmodel.MainViewModel;

public class MainActivity
    extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private MainViewModel m_viewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        // bind the header to the current item
        m_viewModel.GetSelectedInstance().observe(this, (@Nullable ServantInstance instance) -> {
            // lets not have nullptr's in out app
            ((TextView)header.findViewById(R.id.textSelectedInstance)).setText(
                instance == null? "N/A" : instance.toString()
            );
        });

        m_viewModel.GetSelectedModule().observe(this, (@Nullable Module module) -> {
            // lets not have nullptr's in out app
            ((TextView)header.findViewById(R.id.textSelectedInstance)).setText(
                module == null? "N/A" : module.toString()
            );
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ViewStub stub = findViewById(R.id.layout_stub);
            stub.setLayoutResource(R.layout.content_settings);
            //View inflated = stub.inflate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_server_01) {
            // Handle the camera action
        } else if (id == R.id.nav_server_02) {

        } else if (id == R.id.nav_server_03) {

        } else if (id == R.id.nav_server_04) {

        } else if (id == R.id.nav_serveradd) {


        } else if (id == R.id.nav_module_01) {

        } else if (id == R.id.nav_module_02) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
