package com.laytonlabs.android.taptheblue;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.laytonlabs.android.taptheblue.util.IabHelper;
import com.laytonlabs.android.taptheblue.util.IabResult;
import com.laytonlabs.android.taptheblue.util.Inventory;

/**
 * Created by matthewlayton on 29/04/2016.
 */
public class TapBlueApp extends Application {

    static final String TAG = "TapBlueApp";
    private Tracker mTracker;
    IabHelper mHelper;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();

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

                //Check app purchases the user has already.
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // handle error here
                return;
            }

            Log.d(TAG, "Got remove Ads already? " + inventory.hasPurchase(Constants.SKU_REMOVE_ADS));

            if (inventory.hasPurchase(Constants.SKU_REMOVE_ADS)) {
                Constants.setPurchaseRemoveAds(true);
            }
        }
    };
}
