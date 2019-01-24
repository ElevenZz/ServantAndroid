package servant.servantandroid.view;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.OnItemClickListener;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

/**
 * main and only activity
 * implements add server listener to handle positive callbacks by the add server dialog
 * implements on refresh listener to allow "swipe up to refresh" on the selected module
 */
public class StartActivity
    extends AppCompatActivity
    implements AddServerFragment.AddServerListener, SwipeRefreshLayout.OnRefreshListener {

    /**
     * adapter for the drawer content
     */
    private InstancesListAdapter m_instances;

    /**
     * databinding for this activity
     */
    private ActivityStartBinding m_binding;

    /**
     * an observable holder getting updated when the user clicks on a module
     * the observer in the main activity updates the content view with the module categories
     */
    private MutableLiveData<ModuleAdapter> m_selectedModule;

    /**
     * called when this activity is about to be created for displaying
     * @param savedInstanceState if this activity is restored from memory
     *                           ex. due to the user reentering the app
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup mutable live data for the selected module
        // acts as a UI bridge between the drawer and the main view
        m_selectedModule = new MutableLiveData<>();
        // using databinding for UI access instead of the clunky findViewById
        m_binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

        Toolbar toolbar = m_binding.appbar.toolbar;
        setSupportActionBar(toolbar);
        m_binding.appbar.content.swipeLayout.setOnRefreshListener(this);

        // override static logger singleton instance with a UI logger
        // which displays a dialog in case of an error
        Logger.setLogger(new UILogger(getSupportFragmentManager()));
        // init database. lazy initialization would be preferable
        // but i need the activity context
        DatabaseService.initialize(getApplicationContext());
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

        // the stuff in the drawer
        final RecyclerView instancesView  = m_binding.drawerContent.instancesList;
        // the stuff in the main view
        final RecyclerView categoriesView = m_binding.appbar.content.categoriesList;

        // setup groupie adapters for both views
        GroupAdapter instancesAdapter  = new GroupAdapter();
        GroupAdapter categoriesAdapter = new GroupAdapter();

        // a listener for all UI adapters.
        // executes click method if adapter implements ClickableItem interface
        OnItemClickListener listener = (item, view) -> {
            if(item instanceof ClickableItem) { ((ClickableItem)item).onClick(view); }
        };

        instancesAdapter.setOnItemClickListener(listener);
        categoriesAdapter.setOnItemClickListener(listener);

        instancesAdapter.add(m_instances);

        instancesView.setAdapter(instancesAdapter);
        instancesView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: display currently selected module in toolbar
        m_selectedModule.observe(this, (module) ->
            runOnUiThread(() -> categoriesAdapter.update(module.getChilds()))
        );

        categoriesView.setAdapter(categoriesAdapter);
        categoriesView.setLayoutManager(new LinearLayoutManager(this));

        // naive swipe to delete implementation
        new ItemTouchHelper(m_instances.getTouchCallback()).attachToRecyclerView(instancesView);
    }

    @Override public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
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

    /**
     * listener callback from the add server dialog
     * called when the user click the dialog's ok button
     * @param dialog the dialog this was called from
     */
    @Override public void onAddServerClicked(DialogFragment dialog) {
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

    /**
     * gets called when android decides it wants to pause to activity
     * saves all instances to the local cache
     */
    @Override protected void onPause() {
        super.onPause();
        m_instances.saveInstances();
    }

    /**
     * overrides OnRefreshListener, gets called when the user swipes upwards
     * calls update on the selected module if it isn't null, starts the refresh animation
     * and stops it again from the update callback
     */
    @Override public void onRefresh() {
        if(m_selectedModule.getValue() != null)
            m_selectedModule.getValue().update(() -> {
                m_binding.appbar.content.swipeLayout.setRefreshing(false);
                m_instances.saveInstances();
            });
    }
}
