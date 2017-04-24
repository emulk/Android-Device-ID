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
        WifiInfo connectionInfo = findIpAdress();
        String simId = findSimId();
        String simOperator = findSimOperator();
        String bluetoothName = findBluetoothName();
        String bluetoothAddress = findBluetoothAddress();
        String hardwareSerial = findHardwareSerial();
        String deviceName = findDeviceName();
        String cpuNumber = findCpuNumber();
        String osVersion = findOsVersion();
        String sdkVersion = findSdkVersion();
        String totRam = findTotRam();
        String fingerprint = findFingerprint();


        if (cellNum1 != null && cellNum1.length() != 0) {
            obj = new IdObject("Sim Number ",
                    cellNum1);
            results.add(obj);
        }

        if (email != null && email.length() != 0) {
            obj = new IdObject("Email ",
                    email);
            obj.setDescription("The email associated with this device.");
            results.add(obj);
        }

        if (gsfValue != null && gsfValue.length() != 0) {
            obj = new IdObject("Google Service Framework ",
                    gsfValue);
            obj.setDescription("A 64-bit number that is randomly generated on the device first boot, GSF should remain constant for the lifetime of the device." +
                    "The Value change if a factory reset is performed.");
            results.add(obj);
        }

        if (android_id != null && android_id.length() != 0) {
            obj = new IdObject("Android Device ID ",
                    android_id);
            obj.setDescription("A 64-bit unique number that is randomly generated on the device first boot, and identify the device. " +
                    "The Value change if a factory reset is performed.");
            results.add(obj);
        }


        if (imei != null && imei.length() != 0) {
            obj = new IdObject("IMEI ",
                    imei);
            obj.setDescription("International Mobile Equipment Identy is a 15 or 17 digit code that uniquely identifies mobile phone sets." +
                    "The IMEI code can enable a GSM or UMTS network to prevent a misplaced or stolen phone from initiling calls.");
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
                obj.setDescription("Is a 32-bit number that identifies each sender or receiver of information that is sent in packets" +
                        "across the Internet. When you request an HTML page or send e-mail, the Internet Protocol part of TCP/IP includes tour IP address" +
                        "in the message.");
                results.add(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (connectionInfo != null && connectionInfo.getSSID() != null && connectionInfo.getSSID().length() != 0) {
            obj = new IdObject("SSID ",
                    connectionInfo.getSSID().toString());
            obj.setDescription("Is a sequence of characters that uniquely names a wireless local area network (WLAN)." +
                    "An SSID is sometimes referred to as a 'Network name'. This name allows stations to connect to the desired network" +
                    " when multiple independent networks operate in the same pysical area.");
            results.add(obj);
        }

        if (connectionInfo != null && connectionInfo.getMacAddress() != null && connectionInfo.getMacAddress().length() != 0) {
            obj = new IdObject("Mac Address ",
                    connectionInfo.getMacAddress().toString());
            obj.setDescription("In a local area network (LAN), the Media Access Control address is the device unique hardware number. " +
                    "A correspondence table relates your IP address to your device's physical address on the LAN");
            results.add(obj);
        }

        if (connectionInfo != null && connectionInfo.getBSSID() != null && connectionInfo.getBSSID().length() != 0) {
            obj = new IdObject("BSSID ",
                    connectionInfo.getBSSID().toString());
            obj.setDescription("Is the MAC address of the wireless access point generated ny combining the 24 bit Organization Unique " +
                    "Identifier and the manufacturer's assigned 24-bit identifier for the radio chipset in the WAP.");
            results.add(obj);
        }

        if (simOperator != null && simOperator.length() != 0) {
            obj = new IdObject("SIM Operator Codes",
                    simOperator.toString());
            obj.setDescription("Is a unique number assigned to every telecommunications operator in all countries of the world. " +
                    "Operator code consists of two parts: Mobile Network Code(MNC) and Mobile Country Code(MCC).");
            results.add(obj);
        }

        if (simId != null && simId.length() != 0) {
            obj = new IdObject("SIM Serial Number",
                    simId.toString());
            obj.setDescription("sometimes called the ICC-ID (Integrated Circuit Card ID), is for international identification." +
                    " The SNN typically has 19 digits and contains specific details about your operator, your location, and when it was made.");
            results.add(obj);
        }


        if (bluetoothName != null && bluetoothName.length() != 0) {
            obj = new IdObject("Bluetooth Name ",
                    bluetoothName.toString());
            obj.setDescription("Device bluetooth name.");
            results.add(obj);
        }

        if (bluetoothAddress != null && bluetoothAddress.length() != 0) {
            obj = new IdObject("Bluetooth Address ",
                    bluetoothAddress.toString());
            obj.setDescription("The Media Access Control address is the bluetooth unique hardware number.");

            results.add(obj);
        }


        /*************************** OPERATING SYSTEM DETAILS ********************/

        if (deviceName != null && deviceName.length() != 0) {
            obj = new IdObject("Manufacturer and Model ",
                    deviceName);
            obj.setDescription("The manufacturer of the product/hardware, and the device name.");
            results.add(obj);
        }

        if (osVersion != null && osVersion.length() != 0) {
            obj = new IdObject("Android Version ",
                    osVersion);
            obj.setDescription("The current version of Android.");
            results.add(obj);
        }

        if (sdkVersion != null && sdkVersion.length() != 0) {
            obj = new IdObject("API Level ",
                    sdkVersion);
            obj.setDescription("Numeric value of the android version. ");
            results.add(obj);
        }

        if (cpuNumber != null && cpuNumber.length() != 0) {
            obj = new IdObject("CPU Cores ",
                    cpuNumber);
            obj.setDescription("The CPU number on this device.");
            results.add(obj);
        }

        if (hardwareSerial != null && hardwareSerial.length() != 0) {
            obj = new IdObject("Hardware Serial ",
                    hardwareSerial.toString());
            obj.setDescription("A hardware serial alphanumeric number");
            results.add(obj);
        }

        if (fingerprint != null && fingerprint.length() != 0) {
            obj = new IdObject("Device Fingerprint",
                    fingerprint.toString());
            obj.setDescription("Is information collected about the current build, extracted from system properties.");
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

    public String findCpuNumber() {
        String name = "";
        try {
            name = Runtime.getRuntime().availableProcessors() + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public String findOsVersion() {
        String name = "";
        try {
            name = Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public String findSdkVersion() {
        String name = "";
        try {
            name = Build.VERSION.SDK_INT + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public String findTotRam() {
        String name = "";
        try {
            name = Runtime.getRuntime().totalMemory() + "";

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
            String message = "https://play.google.com/store/apps/details?id=" + appPackageName;
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
