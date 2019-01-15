package servant.servantandroid.view;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.xwray.groupie.GroupAdapter;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import servant.servantandroid.R;
import servant.servantandroid.databinding.ActivityStartBinding;
import servant.servantandroid.internal.Logger;
import servant.servantandroid.viewmodel.InstancesListAdapter;
import servant.servantandroid.viewmodel.ModuleAdapter;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.net.MalformedURLException;

public class StartActivity
    extends AppCompatActivity implements AddServerFragment.AddServerListener {

    private InstancesListAdapter m_instances;
    private ActivityStartBinding m_binding;
    private MutableLiveData<ModuleAdapter> m_selectedModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        m_selectedModule = new MutableLiveData<>();
        m_binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

        Toolbar toolbar = m_binding.appbar.toolbar;
        setSupportActionBar(toolbar);

        Logger.setLogger(new UILogger(getSupportFragmentManager()));
        Context ctx = getApplicationContext();
        DatabaseService.initialize(ctx);
        m_instances = new InstancesListAdapter(this, m_selectedModule);

        FloatingActionButton fab = m_binding.appbar.fab;
        fab.setOnClickListener((v) ->
            new AddServerFragment().show(
                getSupportFragmentManager(),
                getString(R.string.addserver_title)
            )
        );

        DrawerLayout drawer = m_binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final RecyclerView instancesView  = m_binding.drawerContent.instancesList;

        GroupAdapter adapter = new GroupAdapter();
        adapter.setOnItemClickListener((item, view) -> {
            if(item instanceof ClickableItem) { ((ClickableItem)item).onClick(view); }
        });

        adapter.add(m_instances);
        instancesView.setAdapter(adapter);
        instancesView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(m_instances.getTouchCallback()).attachToRecyclerView(instancesView);
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
