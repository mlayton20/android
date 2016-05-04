package com.laytonlabs.android.taptheblue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.laytonlabs.android.taptheblue.util.IabHelper;
import com.laytonlabs.android.taptheblue.util.IabResult;
import com.laytonlabs.android.taptheblue.util.Inventory;
import com.laytonlabs.android.taptheblue.util.Purchase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewlayton on 01/05/2016.
 */
public class ShopActivity extends Activity {

    static final String TAG = "ShopActivity";

    private Tracker mTracker;

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    String developerPurchasePayload = "";

    private Button mButtonRemoveAds;
    private Button mBackButton;

    IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Obtain the shared Tracker instance.
        mTracker = ((TapBlueApp)getApplication()).getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.AppViewBuilder().build());

        mBackButton = (Button)findViewById(R.id.shop_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionEvent("Shop - Back");
                Intent i = new Intent(ShopActivity.this, SplashScreenActivity.class);
                startActivity(i);
            }
        });

        developerPurchasePayload = getString(R.string.google_public_key_1);

        String base64EncodedPublicKey = getString(R.string.google_public_key_1) +
                getString(R.string.google_public_key_2) +
                getString(R.string.google_public_key_3);

        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    return;
                }
                // Hooray, IAB is fully set up!

                //Check their current purchases.
                List<String> additionalSkuList = new ArrayList<String>();
                additionalSkuList.add(Constants.SKU_REMOVE_ADS);
                try {
                    mHelper.queryInventoryAsync(true, additionalSkuList, null,
                            mQueryFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        });

        mButtonRemoveAds = (Button)findViewById(R.id.shop_product_remove_ads_button);
        mButtonRemoveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    purchaseRemoveAds();
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void purchaseRemoveAds() throws IabHelper.IabAsyncInProgressException {
        mHelper.launchPurchaseFlow(this, Constants.SKU_REMOVE_ADS, RC_REQUEST,
                mPurchaseFinishedListener, developerPurchasePayload);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.disposeWhenFinished();
        mHelper = null;
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);
                return;
            }
            else if (purchase.getSku().equals(Constants.SKU_REMOVE_ADS)) {
                //Store the remove ads value in app to show that the person has bought the adfree version.
                Constants.setPurchaseRemoveAds(true);
                mButtonRemoveAds.setEnabled(false);
                mButtonRemoveAds.setText(R.string.shop_product_paid);
            }
        }
    };

    IabHelper.QueryInventoryFinishedListener
            mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {
            if (result.isFailure()) {
                // handle error
                return;
            }

            String removeAdsPrice = inventory.getSkuDetails(Constants.SKU_REMOVE_ADS).getPrice();

            // update the price.
            mButtonRemoveAds.setText(removeAdsPrice);

            //Disable the buy button if its already bought.
            if (inventory.hasPurchase(Constants.SKU_REMOVE_ADS)) {
                mButtonRemoveAds.setEnabled(false);
                mButtonRemoveAds.setText(R.string.shop_product_paid);
                //TODO - Make the app a greyed out button
            }
        }
    };

    private void sendActionEvent(String action) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction(action)
                .build());
    }

}
