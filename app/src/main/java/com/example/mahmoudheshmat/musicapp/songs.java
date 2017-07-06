package com.example.mahmoudheshmat.musicapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.backup.BackupManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;


public class songs extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;

    // Tabbed Layout
    private TabLayout tabLayout;
    private ViewPager viewPager;

    Context context;

    //Bottom playing song
    ImageView song_image;
    ImageView play_image;
    TextView song_name;
    TextView artist_name;
    CardView cardView;

    ArrayList<songs_item> items;

    //Broad BroadcastManager
    LocalBroadcastManager bManager;
    IntentFilter intentFilter;

    //Get all songs
    SharedPreferences sharedPreferences ;
    String sort_type ;
    String sort_by ;

    //Drawer
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    //Service get mediaSessionId
    playingsongService mService;
    Boolean isconnect;


    public static Boolean isPlaying = false;
    public static Boolean isAlbum = false;
    static int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.songs));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;

        //Tabbed Layout
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Bottom playing song
        song_image = (ImageView) findViewById(R.id.song_image);
        play_image = (ImageView) findViewById(R.id.playPause);
        song_name = (TextView) findViewById(R.id.song_name);
        artist_name = (TextView) findViewById(R.id.artist_name);
        cardView = (CardView) findViewById(R.id.card_view);
        cardView.setOnClickListener(this);
        play_image.setOnClickListener(this);

        //BroadCastManager
        bManager = LocalBroadcastManager.getInstance(context);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.BOTTOM);
        bManager.registerReceiver(Receiver, intentFilter);

        //Get lists of songs and store it in items
        sharedPreferences = getSharedPreferences(Constant.SORT_SONG, Context.MODE_PRIVATE);
        sort_type = sharedPreferences.getString(Constant.SORT_TYPE, Constant.ASC_SORT);
        sort_by = sharedPreferences.getString(Constant.SORT_BY, Constant.MEDIASTORE_TITLE);
        items = new ArrayList<>();
        getSongs get = new getSongs();
        items = get.getAllSongs(context, sort_type, sort_by);


        //Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.drawer_open, R.string.drawer_close );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationItemSelected(item.getItemId());
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(context, playingsongService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    //Connect to bind service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playingsongService.localBinder local = (playingsongService.localBinder) service;
            mService = local.getService();
            isconnect = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isconnect = false;
        }
    };

    // Broadcast receiver from service to change song data
    public BroadcastReceiver Receiver = new  BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadCast(intent);
        }
    };

    public void onBroadCast(Intent intent){
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();

        if (action.equals(Constant.BOTTOM)){

            String Data_type = bundle.getString("type");

            if(Data_type.equals("songData")){

                if(isAlbum){
                    items = getSongs.songs_data_list;
                }
                isAlbum = false;
                isPlaying = true;
                currentSongIndex = bundle.getInt("currentSongIndex");
                Picasso.with(context)
                        .load(items.get(currentSongIndex).getSong_image())
                        .placeholder(R.drawable.placeholder)
                        .into(song_image);
                song_name.setText(items.get(currentSongIndex).getSong_name());
                artist_name.setText(items.get(currentSongIndex).getArtist_name());
                play_image.setImageResource(R.drawable.pause_music);

            } else if(Data_type.equals(Constant.PLAY_ACTION)){
                play_image.setImageResource(R.drawable.play_music);
            }else if(Data_type.equals(Constant.PAUSE_ACTION)){
                play_image.setImageResource(R.drawable.pause_music);
            } else if (Data_type.equals("album")){
                if(!isAlbum){
                    items = getSongs.AlbumSongs_data_list;
                }
                isAlbum = true;
                currentSongIndex = bundle.getInt("currentSongIndex");
                Picasso.with(context)
                        .load(items.get(currentSongIndex).getSong_image())
                        .placeholder(R.drawable.placeholder)
                        .into(song_image);
                song_name.setText(items.get(currentSongIndex).getSong_name());
                artist_name.setText(items.get(currentSongIndex).getArtist_name());
                play_image.setImageResource(R.drawable.pause_music);

            }
        }
    }

    public void NavigationItemSelected(int selectedItem){
        switch (selectedItem){
            case R.id.theme:
                changeBackground();
                break;

            case R.id.songs:
                NavigationUtils.navigateSongs(context);
                break;

            case R.id.playlist:
                NavigationUtils.navigatoPlaylists(context);
                break;

            case R.id.favourite:
                favouritesFragment favourites = new favouritesFragment();
                addCenterFragments(favourites, Constant.FAVOURITES);
                break;

            case R.id.equalizer:
                Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mService.getAudioSessionId());
                startActivityForResult(effects, 0);
                break;

            case R.id.notes:
                NavigationUtils.navigatoNotes(context);
                //drawerLayout.closeDrawers();
                break;

            case R.id.setting:

                break;

            case R.id.about:

                break;

            case R.id.logout:

                break;

            case R.id.folder:
                fileManagerFragment files = new fileManagerFragment();
                addCenterFragments(files, Constant.FILES);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.song, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search:
                NavigationUtils.navigateToSearch(this);
                return true;

            case R.id.action_shuffle:
                shuffleAll();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //make shuffle all from menu bar
    private void shuffleAll(){
        Intent intent = new Intent(context, playingsongActivity.class);
        intent.putExtra("type", "shuffle_all");
        startActivity(intent);
    }

    // Tabbed Host and view Pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongsFragment(), getString(R.string.songs));
        adapter.addFragment(new AlbumsFragment(), getString(R.string.album));
        adapter.addFragment(new ArtistsFragment(), getString(R.string.artist));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v == play_image){
            play_pause();
            NavigationUtils.playPause_playingsongService(context);
        }else if(v == cardView){
            if(isPlaying) {
                NavigationUtils.play_playingsongActivityFromAdapter(context, currentSongIndex);
            }
        }
    }

    //View pager of tabbed layout
    class ViewPagerAdapter extends FragmentPagerAdapter {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //PlayPause image button
    private void play_pause(){
        if(isPlaying){
            isPlaying = false;
            play_image.setImageResource(R.drawable.play_music);
        }else{
            isPlaying = true;
            play_image.setImageResource(R.drawable.pause_music);
        }
    }

    private void addCenterFragments(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    //change color theme background
    public void changeBackground(){
        final ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                toolbar.setBackgroundColor(color);
            }

            @Override
            public void onCancel(){
                colorPicker.dismissDialog();
            }
        });
    }

}
