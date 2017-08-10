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

package androiddeveloper.eder.padilla.printsample.zebrademos;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androiddeveloper.eder.padilla.printsample.R;
import androiddeveloper.eder.padilla.printsample.zebrademos.connectionbuilder.ConnectionBuilderDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.connectivity.ConnectivityDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.discovery.DiscoveryDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.imageprint.ImagePrintDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.listformats.ListFormatsDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.magcard.MagCardDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.receipt.ReceiptDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.sendfile.SendFileDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.sigcapture.SigCaptureDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.smartcard.SmartCardDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.status.PrintStatusDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.statuschannel.StatusChannelDemo;
import androiddeveloper.eder.padilla.printsample.zebrademos.storedformat.StoredFormatDemo;


public class LoadDevDemo extends ListActivity {

    private static final int CONNECT_ID = 0;
    private static final int DISCO_ID = 1;
    private static final int IMGPRNT_ID = 2;
    private static final int LSTFORMATS_ID = 3;
    private static final int MAGCARD_ID = 4;
    private static final int PRNTSTATUS_ID = 5;
    private static final int SMRTCARD_ID = 6;
    private static final int SIGCAP_ID = 7;
    private static final int SNDFILE_ID = 8;
    private static final int STRDFRMT_ID = 9;
    private static final int STATUSCHANNEL_ID = 10;
    private static final int CONNECTIONBUILDER_ID = 11;
    private static final int RECEIPT_ID = 12;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent;
        switch (position) {
        case CONNECT_ID:
            intent = new Intent(this, ConnectivityDemo.class);
            break;
        case DISCO_ID:
            intent = new Intent(this, DiscoveryDemo.class);
            break;
        case IMGPRNT_ID:
            intent = new Intent(this, ImagePrintDemo.class);
            break;
        case LSTFORMATS_ID:
            intent = new Intent(this, ListFormatsDemo.class);
            break;
        case MAGCARD_ID:
            intent = new Intent(this, MagCardDemo.class);
            break;
        case PRNTSTATUS_ID:
            intent = new Intent(this, PrintStatusDemo.class);
            break;
        case SMRTCARD_ID:
            intent = new Intent(this, SmartCardDemo.class);
            break;
        case SIGCAP_ID:
            intent = new Intent(this, SigCaptureDemo.class);
            break;
        case SNDFILE_ID:
            intent = new Intent(this, SendFileDemo.class);
            break;
        case STRDFRMT_ID:
            intent = new Intent(this, StoredFormatDemo.class);
            break;
        case STATUSCHANNEL_ID:
            intent = new Intent(this, StatusChannelDemo.class);
            break;
        case CONNECTIONBUILDER_ID:
            intent = new Intent(this, ConnectionBuilderDemo.class);
            break;
        case RECEIPT_ID:
            intent = new Intent(this, ReceiptDemo.class);
            break;
        default:
            return;// not possible
        }
        startActivity(intent);
    }

}
