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

package androiddeveloper.eder.padilla.printsample.zebrademos.imageprint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import androiddeveloper.eder.padilla.printsample.R;
import androiddeveloper.eder.padilla.printsample.zebrademos.util.SettingsHelper;
import androiddeveloper.eder.padilla.printsample.zebrademos.util.UIHelper;

public class ImagePrintDemo extends Activity {

    private RadioButton btRadioButton;
    private EditText macAddressEditText;
    private EditText ipAddressEditText;
    private EditText portNumberEditText;
    private EditText printStoragePath;
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    private static final String tcpAddressKey = "ZEBRA_DEMO_TCP_ADDRESS";
    private static final String tcpPortKey = "ZEBRA_DEMO_TCP_PORT";
    private static final String PREFS_NAME = "OurSavedAddress";
    private UIHelper helper = new UIHelper(this);
    private static int TAKE_PICTURE = 1;
    private static int PICTURE_FROM_GALLERY = 2;
    private static File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_print_demo);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        ipAddressEditText = (EditText) this.findViewById(R.id.ipAddressInput);
        String ip = settings.getString(tcpAddressKey, "");
        ipAddressEditText.setText(ip);

        portNumberEditText = (EditText) this.findViewById(R.id.portInput);
        String port = settings.getString(tcpPortKey, "");
        portNumberEditText.setText(port);

        macAddressEditText = (EditText) this.findViewById(R.id.macInput);
        String mac = settings.getString(bluetoothAddressKey, "");
        macAddressEditText.setText(mac);

        printStoragePath = (EditText) findViewById(R.id.printerStorePath);

        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    printStoragePath.setVisibility(View.VISIBLE);
                } else {
                    printStoragePath.setVisibility(View.INVISIBLE);
                }
            }
        });

        btRadioButton = (RadioButton) this.findViewById(R.id.bluetoothRadio);

        Button cameraButton = (Button) this.findViewById(R.id.testButton);
        cameraButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                getPhotoFromCamera();
            }
        });

        Button galleryButton = (Button) this.findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                getPhotosFromGallery();
            }
        });

        RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bluetoothRadio) {
                    toggleEditField(macAddressEditText, true);
                    toggleEditField(portNumberEditText, false);
                    toggleEditField(ipAddressEditText, false);
                } else {
                    toggleEditField(portNumberEditText, true);
                    toggleEditField(ipAddressEditText, true);
                    toggleEditField(macAddressEditText, false);
                }
            }
        });
    }

    private void getPhotosFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_FROM_GALLERY);
    }

    private void toggleEditField(EditText editText, boolean set) {
        /*
         * Note: Disabled EditText fields may still get focus by some other means, and allow text input.
         *       See http://code.google.com/p/android/issues/detail?id=2771
         */
        editText.setEnabled(set);
        editText.setFocusable(set);
        editText.setFocusableInTouchMode(set);
    }

    private boolean isBluetoothSelected() {
        return btRadioButton.isChecked();
    }

    private String getMacAddressFieldText() {
        return macAddressEditText.getText().toString();
    }

    private String getTcpAddress() {
        return ipAddressEditText.getText().toString();
    }

    private String getTcpPortNumber() {
        return portNumberEditText.getText().toString();
    }

    private void getPhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), "tempPic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, TAKE_PICTURE);
    }
    Bitmap test1,test2;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PICTURE) {
               // printPhotoFromExternal(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
            if (requestCode == PICTURE_FROM_GALLERY) {
                Uri imgPath = data.getData();
                Bitmap myBitmap = null;
                try {
                    myBitmap = Media.getBitmap(getContentResolver(), imgPath);
                } catch (FileNotFoundException e) {
                    helper.showErrorDialog(e.getMessage());
                } catch (IOException e) {
                    helper.showErrorDialog(e.getMessage());
                }
                //Bitmap bitmap,bitmap1,bitmap2,bitmap3;
                //ImageView imageView = new ImageView(getApplicationContext());
                //Glide.with(this).load("http://app.driveapp.mx/drive/valet/images/image_1_55_2017_08_08_18_10_33.png").into(imageView);
                //bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                //printPhotoFromExternal(bitmap);
                //ImageView imageView2 = new ImageView(getApplicationContext());
                //Glide.with(this).load("http://app.driveapp.mx/drive/valet/images/image_2_55_2017_08_08_18_10_33.png").into(imageView2);
                //bitmap1 = ((BitmapDrawable) imageView2.getDrawable()).getBitmap();
                //printPhotoFromExternal(bitmap1);
                //ImageView imageView3 = new ImageView(getApplicationContext());
                //Glide.with(this).load("http://app.driveapp.mx/drive/valet/images/image_3_55_2017_08_08_18_10_33.png").into(imageView3);
                //bitmap2 = ((BitmapDrawable) imageView3.getDrawable()).getBitmap();
                //printPhotoFromExternal(bitmap2);
                //ImageView imageView4 = new ImageView(getApplicationContext());
                //Glide.with(this).load("http://app.driveapp.mx/drive/valet/images/image_4_55_2017_08_08_18_10_33.png").into(imageView4);
                //bitmap3 = ((BitmapDrawable) imageView4.getDrawable()).getBitmap();
                //printPhotoFromExternal(bitmap3);

                //printPhotoFromExternal(myBitmap);

                Glide.with(getApplicationContext())
                        .load("http://app.driveapp.mx/drive/valet/images/image_1_55_2017_08_08_18_10_33.jpg")
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(200, 200) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                // Do something with bitmap here.
                                test1 = bitmap;
                                if (test1==null||test2==null){

                                }else{
                                printPhotoFromExternal(test1,test2);}
                            }
                        });
              Glide.with(getApplicationContext())
                      .load("http://app.driveapp.mx/drive/valet/images/image_1_55_2017_08_08_18_10_33.jpg")
                      .asBitmap()
                      .into(new SimpleTarget<Bitmap>(200, 200) {
                          @Override
                          public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                              // Do something with bitmap here.
                              test2 = bitmap;
                              if (test1==null||test2==null){

                              }else{
                                  printPhotoFromExternal(test1,test2);}
                          }
                      });
             //Glide.with(getApplicationContext())
             //        .load("http://app.driveapp.mx/drive/valet/images/image_3_55_2017_08_08_18_10_33.png")
             //        .asBitmap()
             //        .into(new SimpleTarget<Bitmap>(200, 200) {
             //            @Override
             //            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
             //                // Do something with bitmap here.
             //                printPhotoFromExternal(bitmap);
             //            }
             //        });
             //Glide.with(getApplicationContext())
             //        .load("http://app.driveapp.mx/drive/valet/images/image_4_55_2017_08_08_18_10_33.png")
             //        .asBitmap()
             //        .into(new SimpleTarget<Bitmap>(200, 200) {
             //            @Override
             //            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
             //                // Do something with bitmap here.
             //                printPhotoFromExternal(bitmap);
             //            }
             //        });
            }
        }

    }

    private void printPhotoFromExternal(final Bitmap bitmap,final Bitmap bitmap2) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    getAndSaveSettings();

                    Looper.prepare();
                    helper.showLoadingDialog("Sending image to printer");
                    Connection connection = getZebraPrinterConn();
                    connection.open();
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);

                    if (((CheckBox) findViewById(R.id.checkBox)).isChecked()) {
                        printer.storeImage(printStoragePath.getText().toString(), new ZebraImageAndroid(bitmap), 550, 412);
                    } else {
                        printer.printImage(new ZebraImageAndroid(bitmap), 20, 500, 200, 200, false);
                        printer.printImage(new ZebraImageAndroid(bitmap2), 20, 500, 200, 200, false);

                    }
                    connection.close();

                    if (file != null) {
                        file.delete();
                        file = null;
                    }
                } catch (ConnectionException e) {
                    helper.showErrorDialogOnGuiThread(e.getMessage());
                } catch (ZebraPrinterLanguageUnknownException e) {
                    helper.showErrorDialogOnGuiThread(e.getMessage());
                } catch (ZebraIllegalArgumentException e) {
                    helper.showErrorDialogOnGuiThread(e.getMessage());
                } finally {
                    bitmap.recycle();
                    helper.dismissLoadingDialog();
                    Looper.myLooper().quit();
                }
            }
        }).start();

    }

    private Connection getZebraPrinterConn() {
        int portNumber;
        try {
            portNumber = Integer.parseInt(getTcpPortNumber());
        } catch (NumberFormatException e) {
            portNumber = 0;
        }
        return isBluetoothSelected() ? new BluetoothConnection(getMacAddressFieldText()) : new TcpConnection(getTcpAddress(), portNumber);
    }

    private void getAndSaveSettings() {
        SettingsHelper.saveBluetoothAddress(ImagePrintDemo.this, getMacAddressFieldText());
        SettingsHelper.saveIp(ImagePrintDemo.this, getTcpAddress());
        SettingsHelper.savePort(ImagePrintDemo.this, getTcpPortNumber());
    }

}
