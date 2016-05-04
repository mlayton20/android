package com.laytonlabs.android.taptheblue;

/**
 * Created by matthewlayton on 24/04/2016.
 */
public class Constants {
    public static final String INTENT_RESART_VIA = "RESART_VIA";
    public static final String INTENT_RESART_VIA_BACK_BTN = "Back Button";
    public static final String INTENT_RESART_VIA_RESART_BTN = "Restart Button";
    public static final String SKU_REMOVE_ADS = "removeads_001";
    public static boolean purchaseRemoveAds = false;

    public static boolean hasPurchasedRemoveAds() {
        return purchaseRemoveAds;
    }

    public static void setPurchaseRemoveAds(boolean purchaseRemoveAds) {
        Constants.purchaseRemoveAds = purchaseRemoveAds;
    }
}
