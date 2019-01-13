package servant.servantandroid.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.xwray.groupie.GroupAdapter;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.EditText;

import java.net.MalformedURLException;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import servant.servantandroid.R;
import servant.servantandroid.viewmodel.InstancesListAdapter;
import servant.servantandroid.viewmodel.MainViewModel;
import servant.servantandroid.databinding.ActivityMainBinding;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

public class MainActivity
    extends AppCompatActivity
    implements
        NavigationView.OnNavigationItemSelectedListener,
        AddServerFragment.AddServerListener {

    private MainViewModel m_viewModel;
    private InstancesListAdapter m_instances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseService.initialize(getApplicationContext());
        m_viewModel = new MainViewModel(
            getPreferences(Context.MODE_PRIVATE),
            getSupportFragmentManager()
        );

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        findViewById(R.id.add_btn).setOnClickListener((View v) ->
            new AddServerFragment().show(
                getSupportFragmentManager(),
                getString(R.string.addserver_title)
            )
        );

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //m_instances = new InstancesListAdapter(this);

        final RecyclerView recyclerView = binding.drawerContent.instancesList;
        GroupAdapter adapter = new GroupAdapter();
        adapter.add(m_instances);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.add_btn).setOnClickListener((View v) ->
            new AddServerFragment().show(
                getSupportFragmentManager(),
                getString(R.string.addserver_title)
            )
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DatabaseService.getInstance().close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
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
        // this is such a fucking stupid anti OOP design pattern
        // maybe ill fix this sometimes, im not in the mental condition to do so rn tho
        // sorry bout that
        switch (item.getGroupId()) {
            case R.id.server_group:
                m_viewModel.selectInstance(item.getTitle().toString());
                break;

            case R.id.modules_group:
                m_viewModel.selectModule(item.getTitle().toString());
                ((DrawerLayout)findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                break;
        }

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
