package xyxgame.playtest;

import android.os.Bundle;


import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

import xyxgame.playtest.Adapter.MPAdapter;


public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {


    Button bt;
    RecyclerView recyclerView;

    BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        bt = findViewById(R.id.bt);
        recyclerView = findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billingClient.isReady()) {

                    SkuDetailsParams params = SkuDetailsParams.newBuilder().
                            setSkusList(Arrays.asList("test", "test2", "test3")).setType(BillingClient.SkuType.INAPP).build();
                    billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                            if (responseCode==BillingClient.BillingResponse.OK){
                                loadRecyclerView(skuDetailsList);
                            }else {Toast.makeText(MainActivity.this, "SkuDetailsResponse Fail"+responseCode, Toast.LENGTH_LONG).show();}
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "xxxNO Ready", Toast.LENGTH_LONG).show();
                }
            }
        });
        setupBilling();

    }

    private void loadRecyclerView(List<SkuDetails> skuDetailsList) {
        MPAdapter adapter=new MPAdapter(this,skuDetailsList,billingClient);
        recyclerView.setAdapter(adapter);
    }

    private void setupBilling() {
        billingClient = BillingClient.newBuilder(this).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MainActivity.this, "NO=" + responseCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(MainActivity.this, "Billing Disconnected()", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        Toast.makeText(MainActivity.this, "Click to Tay-BUY ", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


}
