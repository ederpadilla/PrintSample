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

package androiddeveloper.eder.padilla.printsample.zebrademos.statuschannel;

import java.util.HashSet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.zebra.sdk.comm.ConnectionChannel;
import com.zebra.sdk.printer.discovery.BluetoothDiscoverer;
import com.zebra.sdk.printer.discovery.ServiceDiscoveryHandler;

import androiddeveloper.eder.padilla.printsample.zebrademos.ConnectionScreen;
import androiddeveloper.eder.padilla.printsample.zebrademos.util.SettingsHelper;
import androiddeveloper.eder.padilla.printsample.zebrademos.util.UIHelper;

/**
 */
public class StatusChannelDemo extends ConnectionScreen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testButton.setText("Get Printer Status");
    }

    @Override
    public void performTest() {
        Intent intent;
        intent = new Intent(this, StatusChannelScreen.class);
        intent.putExtra("bluetooth selected", isBluetoothSelected());
        intent.putExtra("mac address", getMacAddressFieldText());
        intent.putExtra("tcp address", getTcpAddress());
        intent.putExtra("tcp port", getTcpPortNumber());
        startActivity(intent);
    }

    @Override
    protected boolean shouldAllowPortNumberEditing() {
        return false;
    }

    protected int desiredVisibilityForSecondTestButton() {
        return View.VISIBLE;
    }

    @Override
    public void performSecondTest() {
        String macAddress = getMacAddressFieldText();
        final HashSet<ConnectionChannel> channels = new HashSet<ConnectionChannel>();
        final Context context = this;
        SettingsHelper.saveBluetoothAddress(context, macAddress);
        try {
            BluetoothDiscoverer.findServices(this, macAddress, new ServiceDiscoveryHandler() {

                public void discoveryFinished() {
                    String availableChannelString = "";
                    for (ConnectionChannel c : channels) {
                        if (c != null) {
                            availableChannelString += c.toString() + "\n";
                        }
                    }
                    Toast.makeText(context, "Available channels:\n" + availableChannelString, Toast.LENGTH_LONG).show();
                }

                public void foundService(ConnectionChannel channel) {
                    channels.add(channel);

                }
            });

        } catch (IllegalArgumentException e) {
            UIHelper helper = new UIHelper(this);
            helper.showErrorDialogOnGuiThread(e.getMessage());

        }
    }
}
