package pl.rzagorski.networkstatsmanager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int READ_PHONE_STATE_REQUEST = 37;

    EditText packageNameEd;
    TextView TrafficStatsAllUid;
    TextView TrafficStatsAllRx;
    TextView TrafficStatsAllTx;
    TextView TrafficStatsPackageUid;
    TextView TrafficStatsPackageRx;
    TextView TrafficStatsPackageTx;

    TextView NetworkStatsManagerAllUid;
    TextView NetworkStatsManagerAllRx;
    TextView NetworkStatsManagerAllTx;
    TextView NetworkStatsManagerPackageUid;
    TextView NetworkStatsManagerPackageRx;
    TextView NetworkStatsManagerPackageTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onResume() {
        super.onResume();
        if (!hasPermissionToReadPhoneStats()) {
            requestPhoneStateStats();
            return;
        }
        initTextViews();
        fillData(packageNameEd.getText().toString());
    }

    private void requestPermissions() {
        if (!hasPermissionToReadNetworkHistory()) {
            requestReadNetworkHistoryAccess();
            return;
        }
        if (!hasPermissionToReadPhoneStats()) {
            requestPhoneStateStats();
            return;
        }
    }

    private void initTextViews() {
        packageNameEd = (EditText) findViewById(R.id.package_name_edit_text);
        packageNameEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (PackageManagerHelper.isPackage(MainActivity.this, s)) {
                    fillData(s.toString());
                }
            }
        });
        TrafficStatsAllUid = (TextView) findViewById(R.id.traffic_stats_all_uid);
        TrafficStatsAllRx = (TextView) findViewById(R.id.traffic_stats_all_rx);
        TrafficStatsAllTx = (TextView) findViewById(R.id.textView3);
        TrafficStatsPackageUid = (TextView) findViewById(R.id.textView14);
        TrafficStatsPackageRx = (TextView) findViewById(R.id.textView16);
        TrafficStatsPackageTx = (TextView) findViewById(R.id.textView18);
        NetworkStatsManagerAllUid = (TextView) findViewById(R.id.network_stats_manager_all_uid);
        NetworkStatsManagerAllRx = (TextView) findViewById(R.id.network_stats_manager_all_rx);
        NetworkStatsManagerAllTx = (TextView) findViewById(R.id.textView6);
        NetworkStatsManagerPackageUid = (TextView) findViewById(R.id.textView7);
        NetworkStatsManagerPackageRx = (TextView) findViewById(R.id.textView10);
        NetworkStatsManagerPackageTx = (TextView) findViewById(R.id.textView19);
    }

    private void fillData(String packageName) {
        int uid = PackageManagerHelper.getPackageUid(this, packageName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager, uid);
            fillNetworkStatsAll(networkStatsHelper);
            fillNetworkStatsPackage(uid, networkStatsHelper);
        }
        fillTrafficStatsAll();
        fillTrafficStatsPackage(uid);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void fillNetworkStatsAll(NetworkStatsHelper networkStatsHelper) {
        NetworkStatsManagerAllUid.setText("-1");
        long mobileWifiRx = networkStatsHelper.getAllRxBytesMobile(this) + networkStatsHelper.getAllRxBytesWifi();
        NetworkStatsManagerAllRx.setText(mobileWifiRx + " Bps");
        long mobileWifiTx = networkStatsHelper.getAllRxBytesMobile(this) + networkStatsHelper.getAllRxBytesWifi();
        NetworkStatsManagerAllTx.setText(mobileWifiTx + " Bps");
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void fillNetworkStatsPackage(int uid, NetworkStatsHelper networkStatsHelper) {
        NetworkStatsManagerPackageUid.setText(packageNameEd.getText().toString() + " :" + uid);
        long mobileWifiRx = networkStatsHelper.getPackageRxBytesMobile(this) + networkStatsHelper.getPackageRxBytesWifi();
        NetworkStatsManagerPackageRx.setText(mobileWifiRx + " Bps");
        long mobileWifiTx = networkStatsHelper.getPackageRxBytesMobile(this) + networkStatsHelper.getPackageRxBytesWifi();
        NetworkStatsManagerPackageTx.setText(mobileWifiTx + " Bps");
    }

    private void fillTrafficStatsAll() {
        TrafficStatsAllUid.setText("-1");
        TrafficStatsAllRx.setText(TrafficStatsHelper.getAllRxBytes() + " Bps");
        TrafficStatsAllTx.setText(TrafficStatsHelper.getAllTxBytes() + " Bps");
    }

    private void fillTrafficStatsPackage(int uid) {
        TrafficStatsPackageUid.setText(packageNameEd.getText().toString() + " :" + uid);
        TrafficStatsPackageRx.setText(TrafficStatsHelper.getPackageRxBytes(uid) + " Bps");
        TrafficStatsPackageTx.setText(TrafficStatsHelper.getPackageTxBytes(uid) + " Bps");
    }

    private boolean hasPermissionToReadPhoneStats() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestPhoneStateStats() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_REQUEST);
    }

    private boolean hasPermissionToReadNetworkHistory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        requestReadNetworkHistoryAccess();
        return false;
    }

    private void requestReadNetworkHistoryAccess() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fillData(packageNameEd.getText().toString());
        }
    }
}
