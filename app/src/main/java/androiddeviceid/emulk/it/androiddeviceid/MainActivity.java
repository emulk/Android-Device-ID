package androiddeviceid.emulk.it.androiddeviceid;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final Uri gsf;

    static {
        gsf = Uri.parse("content://com.google.android.gsf.gservices");
    }

    public static String appPackageName;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "MainAcitivty";

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        appPackageName = getApplicationContext().getPackageName();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<IdObject> getDataSet() {
        ArrayList results = new ArrayList<IdObject>();
        IdObject obj;

        String cellNum1 = findCellNum1();
        String email = findEmail();
        String gsfValue = findGsf();
        String android_id = findAndroidId();
        String imei = findImei();
        String deviceName = findDeviceName();
        WifiInfo connectionInfo = findIpAdress();
        String simId = findSimId();
        String simOperator = findSimOperator();
        String bluetoothName = findBluetoothName();
        String bluetoothAddress = findBluetoothAddress();
        String hardwareSerial = findHardwareSerial();
        String fingerprint = findFingerprint();


        if (cellNum1 != null && cellNum1.length() != 0) {
            obj = new IdObject("Sim Number ",
                    cellNum1);
            results.add(obj);
        }

        if (email != null && email.length() != 0) {
            obj = new IdObject("Email ",
                    email);
            results.add(obj);
        }

        if (gsfValue != null && gsfValue.length() != 0) {
            obj = new IdObject("Google Service Framework ",
                    gsfValue);
            results.add(obj);
        }

        if (android_id != null && android_id.length() != 0) {
            obj = new IdObject("Android Device ID ",
                    android_id);
            results.add(obj);
        }


        if (imei != null && imei.length() != 0) {
            obj = new IdObject("IMEI ",
                    imei);
            results.add(obj);
        }

        if (deviceName != null && deviceName.length() != 0) {
            obj = new IdObject("Manufacturer and Model ",
                    deviceName);
            results.add(obj);
        }

        if (connectionInfo != null && connectionInfo.getIpAddress() != 0) {
            try {
                int ipAddress = connectionInfo.getIpAddress();

                byte[] myIPAddress = BigInteger.valueOf(ipAddress).toByteArray();

                int i = 0;
                int j = myIPAddress.length - 1;
                byte tmp;
                while (j > i) {
                    tmp = myIPAddress[j];
                    myIPAddress[j] = myIPAddress[i];
                    myIPAddress[i] = tmp;
                    j--;
                    i++;
                }
                InetAddress myInetIP = InetAddress.getByAddress(myIPAddress);
                String myIP = myInetIP.getHostAddress();
                obj = new IdObject("Ip Address ",
                        myIP);
                results.add(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (connectionInfo != null && connectionInfo.getSSID() != null && connectionInfo.getSSID().length() != 0) {
            obj = new IdObject("SSID ",
                    connectionInfo.getSSID().toString());
            results.add(obj);
        }

        if (connectionInfo != null && connectionInfo.getMacAddress() != null && connectionInfo.getMacAddress().length() != 0) {
            obj = new IdObject("Mac Address ",
                    connectionInfo.getMacAddress().toString());
            results.add(obj);
        }

        if (connectionInfo != null && connectionInfo.getBSSID() != null && connectionInfo.getBSSID().length() != 0) {
            obj = new IdObject("BSSID ",
                    connectionInfo.getBSSID().toString());
            results.add(obj);
        }

        if (simOperator != null && simOperator.length() != 0) {
            obj = new IdObject("SIM Operator",
                    simOperator.toString());
            results.add(obj);
        }

        if (simId != null && simId.length() != 0) {
            obj = new IdObject("SIM Card Serial ",
                    simId.toString());
            results.add(obj);
        }


        if (bluetoothName != null && bluetoothName.length() != 0) {
            obj = new IdObject("Bluetooth Name ",
                    bluetoothName.toString());
            results.add(obj);
        }

        if (bluetoothAddress != null && bluetoothAddress.length() != 0) {
            obj = new IdObject("Bluetooth Address ",
                    bluetoothAddress.toString());
            results.add(obj);
        }

        if (hardwareSerial != null && hardwareSerial.length() != 0) {
            obj = new IdObject("Hardware Serial ",
                    hardwareSerial.toString());
            results.add(obj);
        }

        if (fingerprint != null && fingerprint.length() != 0) {
            obj = new IdObject("Device Fingerprint",
                    fingerprint.toString());
            results.add(obj);
        }


        return results;
    }

    public String findCellNum1() {
        String cellNum = "";
        try {
            TelephonyManager phoneManager = (TelephonyManager)
                    getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            cellNum = phoneManager.getLine1Number().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cellNum;

    }

    public String findEmail() {
        String email = "";
        try {
            Account[] accountList = AccountManager.get(this).getAccountsByType("com.google");
            email = accountList[0].name.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;

    }

    public String findGsf() {
        String name = "";
        try {
            Cursor query = getApplicationContext().getContentResolver().query(gsf, null, null, new String[]{"android_id"}, null);
            if (query != null) {
                query.moveToFirst();
                name = Long.toHexString(Long.parseLong(query.getString(1))).toString().toUpperCase().trim();
            }
            query.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;

    }


    /**
     * @return
     */
    public String findAndroidId() {
        String android_id = "";
        try {
            android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return android_id;
    }

    /**
     * @return
     */
    public String findImei() {
        String imei = "";
        try {
            TelephonyManager
                    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    public String findDeviceName() {
        String name = "";
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;

            name = manufacturer + " " + model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public WifiInfo findIpAdress() {
        WifiInfo connectionInfo = null;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            connectionInfo = wm.getConnectionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectionInfo;
    }

    public String findSimId() {
        String name = "";
        try {
            TelephonyManager
                    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            name = telephonyManager.getSimSerialNumber().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public String findSimOperator() {
        String name = "";
        try {
            TelephonyManager
                    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            name = telephonyManager.getSimOperator().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public String findBluetoothName() {
        String name = "";
        try {
            name = BluetoothAdapter.getDefaultAdapter().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public String findBluetoothAddress() {
        String name = "";
        try {
            name = BluetoothAdapter.getDefaultAdapter().getAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public String findHardwareSerial() {
        String name = "";
        try {
            name = Build.SERIAL.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }


    public String findFingerprint() {
        String name = "";
        try {
            name = Build.FINGERPRINT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_refresh) {
            Intent refresh = new Intent(MainActivity.this, MainActivity.class);
            startActivity(refresh);
            MainActivity.this.finish();
        } else if (id == R.id.nav_share) {
            String message = "http://play.google.com/store/apps/developer?id=" + appPackageName;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Share"));


        } else if (id == R.id.nav_send) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

}
