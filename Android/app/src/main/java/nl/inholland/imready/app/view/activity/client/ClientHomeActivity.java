package nl.inholland.imready.app.view.activity.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.zbra.androidlinq.Stream;
import nl.inholland.imready.R;
import nl.inholland.imready.app.logic.PreferenceConstants;
import nl.inholland.imready.app.view.ParcelableConstants;
import nl.inholland.imready.app.view.activity.shared.MessagesActivity;
import nl.inholland.imready.app.view.adapter.PersonalBlockAdapter;
import nl.inholland.imready.app.view.fragment.WelcomeDialogFragment;
import nl.inholland.imready.app.view.listener.LoadMoreListener;
import nl.inholland.imready.app.view.listener.OnLoadedListener;
import nl.inholland.imready.model.blocks.PersonalActivity;
import nl.inholland.imready.model.blocks.PersonalBlock;
import nl.inholland.imready.model.blocks.PersonalComponent;
import nl.inholland.imready.model.enums.BlockPartStatus;
import nl.inholland.imready.model.enums.BlockType;

import static br.com.zbra.androidlinq.Linq.stream;

public class ClientHomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnLoadedListener<PersonalBlock> {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private BaseAdapter gridAdapter;
    private LoadMoreListener loadMoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        initGridView();
        initToolbarAndDrawer();
        loadMoreListener.loadMore();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_home, menu);
        TextView userText = findViewById(R.id.username);

        SharedPreferences settings = getSharedPreferences(PreferenceConstants.FILE, MODE_PRIVATE);
        String username = settings.getString(PreferenceConstants.USER_NAME, getString(R.string.default_username));
        userText.setText(username);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.messages:
                gotoMessages();
                return true;
            case R.id.notifications:
                gotoNotifications();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_messages:
                gotoMessages();
                break;
            case R.id.drawer_family:
                gotoFamily();
                break;
            case R.id.drawer_info:
                gotoInfo();
                break;
        }
    }

    private void initGridView() {
        GridView gridView = findViewById(R.id.blocks);
        List<OnLoadedListener<PersonalBlock>> listeners = new ArrayList<>();
        listeners.add(this);
        gridAdapter = new PersonalBlockAdapter(this, listeners);
        loadMoreListener = (LoadMoreListener) gridAdapter;
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);
    }

    private void initToolbarAndDrawer() {
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer
        drawer = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawerOpen, R.string.drawerClosed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        Button messagesButton = drawer.findViewById(R.id.drawer_messages);
        messagesButton.setOnClickListener(this);

        Button familyButton = drawer.findViewById(R.id.drawer_family);
        familyButton.setOnClickListener(this);

        Button infoButton = drawer.findViewById(R.id.drawer_info);
        infoButton.setOnClickListener(this);

        Button logoutButton = drawer.findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);
    }

    private void gotoMessages() {
        Intent intent = new Intent(this, MessagesActivity.class);
        startActivity(intent);
    }

    private void gotoNotifications() {
        Toast.makeText(this, "Soon!", Toast.LENGTH_SHORT).show();
    }

    private void gotoFamily() {
        Toast.makeText(this, "Soon!", Toast.LENGTH_SHORT).show();
    }

    private void gotoInfo() {
        Toast.makeText(this, "Soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PersonalBlock block = (PersonalBlock) adapterView.getItemAtPosition(position);
        if (block.getBlock().getType() == BlockType.ADD) {
            Intent intent = new Intent(this, ClientFutureplanEditActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ClientBlockDetailsActivity.class);
            intent.putExtra(ParcelableConstants.BLOCK, block);
            startActivity(intent);
        }

    }

    @Override
    public void onLoaded(List<PersonalBlock> body) {
        if (body != null) {
            Stream<PersonalComponent> components = stream(body).selectMany(PersonalBlock::getComponents);
            Stream<PersonalActivity> activities = components.selectMany(PersonalComponent::getActivities);
            List<PersonalActivity> todoSoon = activities.where(activity -> activity.getStatus() == BlockPartStatus.ONGOING).toList();

            if (!todoSoon.isEmpty()) {
                WelcomeDialogFragment dialogWelcome = new WelcomeDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ParcelableConstants.TODO_ACTIVITIES, (ArrayList<? extends Parcelable>) todoSoon);
                dialogWelcome.setArguments(bundle);
                dialogWelcome.show(getSupportFragmentManager(), "welcome");
            }
        }
    }
}
