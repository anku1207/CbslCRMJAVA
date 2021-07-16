package com.cbslprojects.crm.CRM.Activitys;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Async.GetVersionCode;
import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Interfaces.GetLocation;
import com.cbslprojects.crm.CRM.Interfaces.LogoutAndShowSearcViewListener;
import com.cbslprojects.crm.CRM.Interfaces.RefreshListListener;
import com.cbslprojects.crm.CRM.Location.LocationTrack;
import com.cbslprojects.crm.CRM.Login.LoginActivity;
import com.cbslprojects.crm.CRM.Model.CommenResponse;
import com.cbslprojects.crm.CRM.dialog.WhatsNewDialogFragment;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.RMMS.fragments.RMMSFragment;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.NotificationScheduler;
import com.cbslprojects.crm.CRM.Util.Utility;
import com.cbslprojects.crm.CRM.broadcast.AlarmReceiver;
import com.cbslprojects.crm.CRM.fragments.HomeFragment;
import com.cbslprojects.crm.CRM.fragments.RegisterComplaintFragment;
import com.cbslprojects.crm.CRM.fragments.ResolveComplaintFragment;
import com.cbslprojects.crm.CRM.fragments.ScheduleMachineFragment;
import com.cbslprojects.crm.CRM.service.LocationUpdateService;
import com.cbslprojects.crm.CRM.service.NotificationService;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RefreshListListener, LogoutAndShowSearcViewListener {

    private Toolbar searchtollbar;
    private Menu search_menu;
    private MenuItem item_search;
    private Fragment f = null;
    private int fragment_id;
    private SearchView searchView;
    private MenuItem search;
    private String uid;
    private String refnumber;
    private String latitute;
    private String longitude;
    private LocationTrack locationTrack;
    private ProgressDialog progressDialog;
    private EditText txtSearch;
    private LinearLayout ll_attendens;
    private TextView tv_check;
    private boolean isLogout = false;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        navigationView = findViewById(R.id.nav_view);
        ll_attendens = findViewById(R.id.ll_attendens);
        tv_check = findViewById(R.id.tv_check);

        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        View v = navigationView.getHeaderView(0);
        CircleImageView profile_pic = v.findViewById(R.id.profile_pic);
        TextView mUser = v.findViewById(R.id.name_textview);
        TextView mEmail = v.findViewById(R.id.email_textView);

        String username = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_USER_NAME, null);
        String emailid = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_EMAIL_ID, null);
        String image_url = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_IMAGE_PATH, null);

        if (image_url != null) {
            Utility.showImage(this, image_url.replace("~", Utility.domain_name), profile_pic);
        }

        uid = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_USER_ID, null);
        refnumber = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_REFERENCE_NO, null);


        mUser.setText(username);
        mEmail.setText(emailid);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSearchtollbar();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        if (refnumber != null)
            eveningAlert();
        else
            morningAlert();

        Intent myIntent = new Intent(this, AlarmReceiver.class);

        boolean isWorking = (PendingIntent.getBroadcast(this, 100, myIntent
                , PendingIntent.FLAG_NO_CREATE) != null);
        if (isWorking) {
            Log.d("alarm", "is working");
        } else {
            Log.d("alarm", "is not working");
            NotificationScheduler.setReminder(this, AlarmReceiver.class, 9, 30);
        }

        refnumber = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_REFERENCE_NO, null);

        if (refnumber != null) {
            tv_check.setText(getResources().getString(R.string.check_out));
        } else {
            tv_check.setText(getResources().getString(R.string.check_in));
        }

        ll_attendens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_check.getText().toString().trim().equals(getResources().getString(R.string.check_in))) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                } else {
                    isLogout = false;
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(R.drawable.cbm_logo)
                            .setTitle("CBSL GROUP CRM")
                            .setMessage("Are you sure you want to Checkout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkPermission();
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        boolean showtargetview = MyPrefences.getInstance(this).getBoolean(Constaints.SHOW_TARGET_VIEW, true);

        if (showtargetview) {
            showTargetView();
        }

       String versioncode= MyPrefences.getInstance(this).getString(Constaints.APP_UPDATE,"0");
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            if(!version.equals(versioncode)){
                new WhatsNewDialogFragment( ).show(getSupportFragmentManager(),"WhatsNewDialogFragment");
                MyPrefences.getInstance(this).setString(Constaints.APP_UPDATE,version);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        if (intent != null) {
            boolean status = intent.getBooleanExtra(Constaints.STATUS, false);
            if (status) {
                callNewActivty(R.id.nav_resolve_complaint);
            } else {
                callNewActivty(R.id.nav_home);
            }
        }

    }

    private void showTargetView() {
        new MaterialTapTargetSequence()
                .addPrompt(new MaterialTapTargetPrompt.Builder(MainActivity.this)
                        .setTarget(findViewById(R.id.ll_attendens))
                        .setPrimaryText("Attendance")
                        .setSecondaryText("Check In and Check Out")
                        .setAnimationInterpolator(new LinearOutSlowInInterpolator())
                        .setFocalPadding(R.dimen.dp))
                // .setIcon(R.drawable.ic))
                .show();

        MyPrefences.getInstance(this).setBoolean(Constaints.SHOW_TARGET_VIEW, false);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            CloseApp();

        }
    }

    private void CloseApp() {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("CBSL GROUP CRM");
        ad.setMessage("Are you sure you want to close the app?");
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    private void morningAlert() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

            Date startime = simpleDateFormat.parse("06:00 AM");
            Date endtime = simpleDateFormat.parse("11:00 AM");

            Date current_time = simpleDateFormat.parse(Utility.getTime());

            //    Date current_time = simpleDateFormat.parse("06:30 AM");

            if (current_time.after(startime) && current_time.before(endtime)) {
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("CBSL GROUP CRM");
                ad.setMessage("Do you want to Check In?");
                ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, MapsActivity.class));

                    }
                });
                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad.show();

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void eveningAlert() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

            Date startime = simpleDateFormat.parse("06:00 PM");
            Date endtime = simpleDateFormat.parse("11:00 PM");

            Date current_time = simpleDateFormat.parse(Utility.getTime());

            // Date current_time = simpleDateFormat.parse("06:30 PM");

            if (current_time.after(startime) && current_time.before(endtime)) {
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("CBSL GROUP CRM");
                ad.setMessage("Do you want to Check Out?");
                ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //checkPermission();


                    }
                });
                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad.show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);

        search = menu.findItem(R.id.action_search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the MainActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//            alertDialog();
//        } else
        if (id == R.id.action_search) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                circleReveal(true);
            else
                searchtollbar.setVisibility(View.VISIBLE);

            item_search.expandActionView();
            // Toast.makeText(this, "s:"+s, Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //if (Utility.isNetworkAvailable(MainActivity.this)) {
        callNewActivty(id);
//        } else {
//            Toast.makeText(MainActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
//        }

        if (id == R.id.nav_logout) {
            // manoj shakya 16-07-21
            isLogout = true;

            alertDialog();
            Intent service = new Intent(MainActivity.this, NotificationService.class);
            MainActivity.this.startService(service);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this, SettingActivity.class));
        } else if (id == R.id.nav_update) {

            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Update App");
            ad.setMessage("Can you update App?");
            ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GetVersionCode getVersionCode = new GetVersionCode(MainActivity.this);
                    getVersionCode.execute();
                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad.show();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void alertDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.cbm_logo)
                .setTitle("CBSL GROUP CRM")
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkPermission();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void callNewActivty(int id) {
        fragment_id = id;
        if (id == R.id.nav_home) {
            ll_attendens.setVisibility(View.VISIBLE);
            f = new HomeFragment();
            if (search != null)
                search.setVisible(false);

        } else if (id == R.id.nav_resolve_complaint) {
            if (search != null)
                search.setVisible(true);
            ll_attendens.setVisibility(View.INVISIBLE);
            f = new ResolveComplaintFragment();

        } else if (id == R.id.nav_register_complaint) {
            search.setVisible(true);
            ll_attendens.setVisibility(View.INVISIBLE);
            //check what fragment load ...........
            Bundle b = new Bundle();
            b.putBoolean(Constaints.WHOS_FRAGMENT_LOAD, true);
           // b.putInt(Constaints.WHOS_FRAGMENT_LOAD, 1);
            f = new RegisterComplaintFragment();
            f.setArguments(b);

        } else if (id == R.id.nav_file_upload) {
            fragment_id = R.id.nav_register_complaint;
            search.setVisible(true);
            ll_attendens.setVisibility(View.INVISIBLE);
            //check what fragment load ..........
            Bundle b = new Bundle();
            b.putBoolean(Constaints.WHOS_FRAGMENT_LOAD, false);
           // b.putInt(Constaints.WHOS_FRAGMENT_LOAD, 1);
            f = new RegisterComplaintFragment();
            f.setArguments(b);

        }
//        else if (id == R.id.consumable_order) {
//            search.setVisible(true);
//            ll_attendens.setVisibility(View.INVISIBLE);
//            //check what fragment load ...........
//            Bundle b = new Bundle();
//            b.putInt(Constaints.WHOS_FRAGMENT_LOAD, 2);
//            f = new RegisterComplaintFragment();
//            f.setArguments(b);
//
//        }
//        else if (id == R.id.consumable_track) {
//
//        }
        else if (id == R.id.nav_schedule_machine_list) {
            search.setVisible(true);
            f = new ScheduleMachineFragment();
            ll_attendens.setVisibility(View.INVISIBLE);

        }
//        else if (id == R.id.nav_rmms) {
//            search.setVisible(true);
//            ll_attendens.setVisibility(View.INVISIBLE);
//            f = new RMMSFragment();
            // startActivity(new Intent(MainActivity.this, RMMSActivity.class));
      //  }
        if (f != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_contain, f);
            fragmentTransaction.commit();
        }
    }

    private void setSearchtollbar() {
        searchtollbar = findViewById(R.id.searchtoolbar);
        if (searchtollbar != null) {
            searchtollbar.inflateMenu(R.menu.menu_search);
            search_menu = searchtollbar.getMenu();

            searchtollbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(false);
                    else
                        searchtollbar.setVisibility(View.GONE);
                }
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(false);
                    } else
                        searchtollbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });

            initSearchView();


        } else
            Log.d("toolbar", "setSearchtollbar: NULL");
    }

    private void initSearchView() {
        searchView = (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        // Enable/Disable Submit button in the keyboard

        searchView.setSubmitButtonEnabled(false);

        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close_black_24dp);


        // set hint and the text colors


        txtSearch = searchView.findViewById(R.id.search_src_text);
        txtSearch.setHint("Search Bank name,Branch name,Sold Id...");
        txtSearch.setTextSize(16);
        txtSearch.setHintTextColor(Color.DKGRAY);

        txtSearch.setInputType(InputType.TYPE_CLASS_TEXT);
        //  InputFilter[] FilterArray = new InputFilter[1];
        // FilterArray[0] = new InputFilter.LengthFilter(6);
        //  txtSearch.setFilters(FilterArray);

        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));


        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.toString().length() == 0)
                    callSearchOnline("");
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String s = txtSearch.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    callSearchOnline(s);
                    return true;
                }
                return false;
            }
        });

        // set the cursor

        AutoCompleteTextView searchTextView = searchView.findViewById(R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }
        });

    }

    private void callSearch(String query) {
        switch (fragment_id) {
            case R.id.nav_register_complaint:
                ((RegisterComplaintFragment) f).SearchSoldId(query);
                break;
            case R.id.nav_resolve_complaint:
                ((ResolveComplaintFragment) f).SearchSoldId(query);
                break;
            case R.id.nav_schedule_machine_list:
                ((ScheduleMachineFragment) f).SearchSoldId(query);
                break;
//            case R.id.nav_rmms:
//                ((RMMSFragment) f).SearchSoldId(query);
//                break;
        }
    }

    private void callSearchOnline(String query) {
        switch (fragment_id) {
            case R.id.nav_register_complaint:
                ((RegisterComplaintFragment) f).SearchSoldIdOnline(query);
                break;
            case R.id.nav_resolve_complaint:
                ((ResolveComplaintFragment) f).SearchSoldIdOnline(query);
                break;
            case R.id.nav_schedule_machine_list:
                ((ScheduleMachineFragment) f).SearchSoldIdOnline(query);
                break;
//            case R.id.nav_rmms:
//                ((RMMSFragment) f).SearchSoldIdOnline(query);
//                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void circleReveal(final boolean isShow) {
        final View myView = findViewById(R.id.searchtoolbar);

        int width = myView.getWidth();

        width -= (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if (isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();

    }

    private void checkPermission() {
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        ) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 103);
            return;
        }

        getCurrentLocation();

    }

    private void getCurrentLocation() {
        progressDialog.show();
        locationTrack = new LocationTrack();
        locationTrack.init(MainActivity.this);
        locationTrack.getCurrentLocation(MainActivity.this, new GetLocation() {
            @Override
            public void getLocation(Location location) {
                if (location != null) {
                    DecimalFormat df2 = new DecimalFormat("00.000000");
                    latitute = String.valueOf(df2.format(location.getLatitude()));
                    longitude = String.valueOf(df2.format(location.getLongitude()));

                    checkOut();

                } else {
                    Toast.makeText(MainActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        locationTrack.startLocationUpdates(MainActivity.this);
    }

    private void checkOut() {
        if (uid != null && latitute != null && longitude != null) {
            if (Utility.isNetworkAvailable(MainActivity.this)) {
                //  if (Utility.isGPSEnabled(MainActivity.this)) {
                if (refnumber != null)
                    setCheckOutOnServer(uid, latitute, longitude, refnumber);
                else {
                    callLogout();
                }
//                } else {
//                    Utility.showSettingsAlert(MainActivity.this);
//                }
            } else {
                Toast.makeText(MainActivity.this, "Please check internet connection"
                        , Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Location not detected",
                    Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    private void setCheckOutOnServer(String user_id, String lat, String lon, String ReferenceNo) {

        String Device_id = Utility.getDeviceId(this);

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<CommenResponse> listCall = apiInterface.sendCheckOut(user_id, lat, lon, ReferenceNo, Device_id);

        listCall.enqueue(new Callback<CommenResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommenResponse> call, @NonNull retrofit2.Response<CommenResponse> response) {
                try {
                    CommenResponse commenResponse = response.body();

                    if (commenResponse == null) {
                        Toast.makeText(MainActivity.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (commenResponse.getErrorMessage().equals("Success") && commenResponse.getErrorCode().equals("000")) {
                        Log.i("rerereere", commenResponse.toString());


                        if (isLogout)
                            callLogout();
                        else {
                            tv_check.setText(getResources().getString(R.string.check_in));

                            MyPrefences.getInstance(MainActivity.this).setString(MyPrefences.PREFRENCE_REFERENCE_NO, null);

                            Intent serviceIntent = new Intent(MainActivity.this, LocationUpdateService.class);
                            MainActivity.this.stopService(serviceIntent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error :" + commenResponse.getErrorMessage()
                                , Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommenResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                // progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callLogout() {


        MyPrefences.getInstance(MainActivity.this).setString(MyPrefences.PREFRENCE_REFERENCE_NO, null);

        MyPrefences.getInstance(MainActivity.this).clearAllData();

        Intent serviceIntent = new Intent(MainActivity.this, LocationUpdateService.class);
        MainActivity.this.stopService(serviceIntent);

        ComponentName receiver = new ComponentName(this, AlarmReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        NotificationScheduler.cancelReminder(MainActivity.this, AlarmReceiver.class);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 103:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    getCurrentLocation();

                } else {
                    Utility.showToast(this, "Oops you just denied the permission");
                    finish();
                }
                break;
//            default:
//                Utility.showToast(this, "Oops you just denied the permission");
//                finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationTrack != null) {
            locationTrack.stopLocationUpdates();
        }

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationTrack != null) {
            locationTrack = null;
        }
    }

    @Override
    public void refreshList() {
        if (item_search != null)
            item_search.collapseActionView();

    }

    @Override
    public void logout() {
        isLogout = true;
        alertDialog();
    }

    @Override
    public void showSearchView(int fragmentId, Fragment fragment) {
        search.setVisible(true);
        ll_attendens.setVisibility(View.GONE);
        fragment_id = fragmentId;
        f = fragment;
        navigationView.setCheckedItem(fragmentId);
    }
}
