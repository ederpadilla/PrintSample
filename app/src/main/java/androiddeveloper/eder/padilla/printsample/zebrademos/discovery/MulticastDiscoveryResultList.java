/***********************************************
 * CONFIDENTIAL AND PROPRIETARY 
 * 
 * The source code and other information contained herein is the confidential and the exclusive property of
 * ZIH Corp. and is subject to the terms and conditions in your end user license agreement.
 * This source code, and any other information contained herein, shall not be copied, reproduced, published, 
 * displayed or distributed, in whole or in part, in any medium, by any means, for any purpose except as
 * expressly permitted under such license agreement.
 * 
 * Copyright ZIH Corp. 2012
 * 
 * ALL RIGHTS RESERVED
 ***********************************************/

package androiddeveloper.eder.padilla.printsample.zebrademos.discovery;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;

import com.zebra.sdk.printer.discovery.DiscoveryException;
import com.zebra.sdk.printer.discovery.NetworkDiscoverer;

import androiddeveloper.eder.padilla.printsample.zebrademos.util.UIHelper;

public class MulticastDiscoveryResultList extends DiscoveryResultList {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle aBundle = getIntent().getExtras();

        if (aBundle != null) {
            int multicastHops = aBundle.getInt(MulticastDiscoveryParameters.MULTICAST_HOPS);
            try {
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                MulticastLock lock = wifi.createMulticastLock("wifi_multicast_lock");
                lock.setReferenceCounted(true);
                lock.acquire();
                NetworkDiscoverer.multicast(this, multicastHops);
                lock.release();
            } catch (DiscoveryException e) {
                new UIHelper(this).showErrorDialog(e.getMessage());
            }
        }
    }

}
