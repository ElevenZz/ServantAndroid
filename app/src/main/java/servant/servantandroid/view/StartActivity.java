package servant.servantandroid.view;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.xwray.groupie.GroupAdapter;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import servant.servantandroid.R;
import servant.servantandroid.internal.Logger;
import servant.servantandroid.viewmodel.InstanceAdapter;
import servant.servantandroid.viewmodel.InstancesListAdapter;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.net.MalformedURLException;

public class StartActivity
    extends AppCompatActivity
    implements
        NavigationView.OnNavigationItemSelectedListener,
        AddServerFragment.AddServerListener {

    private InstancesListAdapter m_instances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Logger.setLogger(new UILogger(getSupportFragmentManager()));
        DatabaseService.initialize(getApplicationContext());
        m_instances = new InstancesListAdapter(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View v) ->
            new AddServerFragment().show(
                getSupportFragmentManager(),
                getString(R.string.addserver_title)
            )
        );

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final RecyclerView recyclerView = findViewById(R.id.instances_list);
        GroupAdapter adapter = new GroupAdapter();
        adapter.setOnItemClickListener((item, view) -> {
            ((InstanceAdapter)item).update();
        });

        adapter.add(m_instances);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(m_instances.getTouchCallback()).attachToRecyclerView(recyclerView);
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
        getMenuInflater().inflate(R.menu.start, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAddServerClicked(DialogFragment dialog) {
        EditText remoteHost = dialog.getDialog().findViewById(R.id.remote_host);
        EditText servername = dialog.getDialog().findViewById(R.id.servername);
        try {
            m_instances.addInstance(
                remoteHost.getText().toString(),
                servername.getText().toString()
            );
        } catch (MalformedURLException e) {
            ErrorFragment.showErrorFragment(
                getSupportFragmentManager(),
                "the host you entered is in an incorrect format. example host: https://kek.com",
                e.toString()
            );
        } catch (IllegalArgumentException e) {
            ErrorFragment.showErrorFragment(
                getSupportFragmentManager(),
                "you entered an illegal instance name: " + servername.getText().toString(),
                e.toString()
            );
        }
    }
}
