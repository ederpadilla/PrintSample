package androiddeveloper.eder.padilla.printsample;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.BluetoothConnectionInsecure;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveryException;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;
import com.zebra.sdk.printer.discovery.NetworkDiscoverer;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androiddeveloper.eder.padilla.printsample.zebraprintinglibrary.PrintLibrary;
import androiddeveloper.eder.padilla.printsample.zebraprintinglibrary.entities.Printer;
import androiddeveloper.eder.padilla.printsample.zebraprintinglibrary.entities.PrinterFinder;
import androiddeveloper.eder.padilla.printsample.zebraprintinglibrary.interfaces.PrinterResponse;
import androiddeveloper.eder.padilla.printsample.zebraprintinglibrary.utils.GlobalesCustom;


public class MainActivity extends ListActivity {

    private Button btn_connect;
    private Button btn_print;
    private ImageButton btn_refresh;
    private EditText etx_macAddress;
    private Context mContext;
    private String bluetoothAddress;
    private String TAG = "App";
    private PrinterResponse printerResponse;
    private Printer currentPrinter;
    private boolean searchFinished;
    private ArrayList<String> discoveredPrinters = null;
    private ArrayAdapter<String> mArrayAdapter;
    private Bitmap image = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        etx_macAddress = (EditText) this.findViewById(R.id.etx_macAddress);
        etx_macAddress.setText(PrintLibrary.getPrinterZebraSingleton().getMacAddress());
        btn_print = (Button) this.findViewById(R.id.btn_print);
        btn_connect = (Button) this.findViewById(R.id.btn_connect);
        btn_refresh = (ImageButton)findViewById(R.id.btn_refresh);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        SecondPrintSample secondPrintSample = new SecondPrintSample();
        String theBtMacAddress = "AC:3F:A4:1B:20:A6";
        secondPrintSample.teest(theBtMacAddress);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void defineEvents() {
        btn_connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        Looper.prepare();
                        enableTestButton(false);
                        doConnection();
                        Looper.loop();
                        Looper.myLooper().quit();


                    }
                }).start();
            }
        });
        btn_print.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        SecondPrintSample secondPrintSample = new SecondPrintSample();
                        String theBtMacAddress = "AC:3F:A4:1B:20:A6";
                        secondPrintSample.teest(theBtMacAddress);
                        //Looper.prepare();
                        //SecondPrintSample secondPrintSample = new SecondPrintSample();
                        //String theBtMacAddress = "AC:3F:A4:1B:20:A6";
                        //secondPrintSample.sendZplTwoOverBluetooth(theBtMacAddress);
                        ////secondPrintSample.sendCpclOverBluetooth(theBtMacAddress);
                        //enableTestButton(false);
                        ////String zpl ="^XA^FX Top section with company logo, name and address.^CF0,60^FO30,50^FDDRIVE^FS^CF0,40^FO30,100^FDExpo Santa fe^FS^FO30,135^FDShelbyville TN 38102^FS^FO30,170^FDUnited States (USA)^FS^FO20,230^GB450,1,3^FS^FX Second section with recipient address and permit information.^CFA,30^FO20,280^FDPlacas: 345AAA^FS^FO20,320^FDMarca: Audi^FS^FO20,360^FDModelo: A3^FS^FO20,400^FDHora de entrada: 10:23am^FS^CFA,15^FO20,460^GB450,1,3^FS^FX Third section with barcode.^BY5,2,270^FO20,480^BC^FD1234567890^FS^XZ";
                        ////printText(zpl);
                        ////printZpl();
                        ////secondTesT();
                        ////printImage();
                        //Looper.loop();
                        //Looper.myLooper().quit();
                    }
                }).start();
            }
        });

        btn_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressBarIndeterminateVisibility(true);
                discoverDevices();
                findPrinters();
            }
        });

        etx_macAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PrintLibrary.getPrinterZebraSingleton().setMacAddress(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                PrintLibrary.getPrinterZebraSingleton().setMacAddress(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                PrintLibrary.getPrinterZebraSingleton().setMacAddress(s.toString());

            }
        });

        printerResponse = new PrinterResponse(){

            @Override
            public void processFoundPrinter(final String texto) {
                if (!discoveredPrinters.contains(texto)) {
                    discoveredPrinters.add(texto);
                    mArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void processFinished() {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {
                    public void run() {
                        searchFinished = true;
                        Toast.makeText(MainActivity.this, "Discovered devices" + " " + discoveredPrinters.size() ,
                                Toast.LENGTH_SHORT).show();
                        setProgressBarIndeterminateVisibility(false);
                        btn_refresh.setEnabled(true);
                    }
                });
            }

            @Override
            public void processError(String message) {
                // TODO Auto-generated method stub
                Log.e(TAG, message);
            }};

    }

    private void secondTesT(){

        new Thread(new Runnable() {
            public void run() {
                try {
                    // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                    Connection thePrinterConn = new BluetoothConnectionInsecure(etx_macAddress.getText().toString());

                    // Initialize
                    Looper.prepare();

                    // Open the connection - physical connection is established here.
                    thePrinterConn.open();

                    // This example prints "This is a ZPL test." near the top of the label.
                    String zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";
                    final String zpl ="^XA^FX Top section with company logo, name and address.^CF0,60^FO30,50^FDDRIVE^FS^CF0,40^FO30,100^FDExpo Santa fe^FS^FO30,135^FDShelbyville TN 38102^FS^FO30,170^FDUnited States (USA)^FS^FO20,230^GB450,1,3^FS^FX Second section with recipient address and permit information.^CFA,30^FO20,280^FDPlacas: 345AAA^FS^FO20,320^FDMarca: Audi^FS^FO20,360^FDModelo: A3^FS^FO20,400^FDHora de entrada: 10:23am^FS^CFA,15^FO20,460^GB450,1,3^FS^FX Third section with barcode.^BY5,2,270^FO20,480^BC^FD1234567890^FS^XZ";

                    // Send the data to printer as a byte array.
                    thePrinterConn.write(zpl.getBytes());


                    // Make sure the data got to the printer before closing the connection
                    Thread.sleep(500);

                    // Close the insecure connection to release resources.
                    thePrinterConn.close();

                    Looper.myLooper().quit();
                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void printZpl() {
        Connection thePrinterConn = new BluetoothConnectionInsecure(etx_macAddress.getText().toString());
        try {
            thePrinterConn.open();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        try {
            ZebraPrinter activePrinter = ZebraPrinterFactory.getInstance(thePrinterConn);
            StringBuilder zplString = new StringBuilder();
            zplString.append("^XA");
            zplString.append("^FX Top section with company logo, name and address.");
            zplString.append("F^CF0,60");
            zplString.append("^FO30,50^FDDRIVE^FS");
            zplString.append("^CF0,40");
            zplString.append("^FO30,100^FDExpo Santa fe^FS");
            zplString.append("^FO30,135^FDShelbyville TN 38102^FS");
            zplString.append("^FO30,170^FDUnited States (USA)^FS");
            zplString.append("^FO20,230^GB450,1,3^FS");
            zplString.append("^FX Second section with recipient address and permit information.");
            zplString.append("^CFA,30");
            zplString.append("^FO20,280^FDPlacas: 345AAA^FS");
            zplString.append("^FO20,320^FDMarca: Audi^FS");
            zplString.append("^FO20,360^FDModelo: A3^FS");
            zplString.append("^FO20,400^FDHora de entrada: 10:23am^FS");
            zplString.append("^CFA,15");
            zplString.append("^FO20,460^GB450,1,3^FS");
            zplString.append("^FX Third section with barcode.");
            zplString.append("^BY5,2,270");
            zplString.append("^FO20,480^BC^FD1234567890^FS");
            zplString.append("^XZ");
            activePrinter.sendCommand(zplString.toString());
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (ZebraPrinterLanguageUnknownException e) {
            e.printStackTrace();
        }
    }

    private void findPrinters() {
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        btn_refresh.setEnabled(false);
                    }
                });

                Looper.prepare();
                try {
                    searchFinished = false;
                    PrinterFinder pf = new PrinterFinder(mContext,  printerResponse, PrintLibrary.getPrinterZebraSingleton().getMacAddress());
                }
                catch (Exception e){
                    Log.e(TAG, e.getMessage());
                }
                finally {
                    Looper.myLooper().quit();
                }
            }
        }).start();
    }

    public void connect() {
        bluetoothAddress = getMacAddressFieldText();
        PrintLibrary.getPrinterZebraSingleton().setMacAddress(getMacAddressFieldText());

        try {
            PrintLibrary.getGlobals().setZebraPrinterConnection(PrintLibrary.getPrinterZebraSingleton().doConnection());

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            disconnect();
        }

        if (PrintLibrary.getPrinterZebraSingleton().isConnected()) {
            currentPrinter = GlobalesCustom.getCurrentPrinter();
            currentPrinter.setMacAddress(bluetoothAddress);

            try {
                setConnectionButtonText();
                enableTestButton(true);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                disconnect();
            }
        }

    }

    public void disconnect() {

        currentPrinter = null;

        try {
            PrintLibrary.getPrinterZebraSingleton().disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void setupListAdapter() {
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, discoveredPrinters);
        setListAdapter(mArrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        etx_macAddress.setText(l.getItemAtPosition(position).toString().substring(0, 17).trim());

        new Thread(new Runnable() {
            public void run() {
                enableTestButton(false);
                Looper.prepare();
                enableTestButton(true);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();

    }

    private void setConnectionButtonText(){
        runOnUiThread(new Runnable() {
            public void run() {
                if (PrintLibrary.getPrinterZebraSingleton().isConnected()) {
                    btn_connect.setText(getString(R.string.cfgDisconnect, PrintLibrary.getPrinterZebraSingleton().getFriendlyName() ));
                } else {
                    btn_connect.setText(getString(R.string.cfgConnect));
                }
            }
        });

    }

    private void enableTestButton(final boolean enabled) {
        runOnUiThread(new Runnable() {
            public void run() {
                btn_connect.setEnabled(enabled);
                btn_print.setEnabled(enabled);
                btn_refresh.setEnabled(enabled);
            }
        });
    }

    private String getMacAddressFieldText() {
        return etx_macAddress.getText().toString();
    }


    private void doConnection() {

        if (!PrintLibrary.getPrinterZebraSingleton().isConnected())
            connect();
        else
            disconnect();

        enableTestButton(true);
        setConnectionButtonText();
    }
    public void discoverDevices(){
        Log.e("si entra a discover","devices");
        progressBar.setVisibility(View.VISIBLE);
        DiscoveryHandler discoveryHandler = new DiscoveryHandler() {
            List<DiscoveredPrinter> printers = new ArrayList<DiscoveredPrinter>();

            public void foundPrinter(DiscoveredPrinter printer) {
                printers.add(printer);
            }

            public void discoveryFinished() {
                progressBar.setVisibility(View.GONE);
                for (DiscoveredPrinter printer : printers) {
                    Log.e("printer",printer.toString());
                }
                Log.e("Discovered ",  printers.size() + " printers.");
            }

            public void discoveryError(String message) {
                progressBar.setVisibility(View.GONE);
                Log.e("An error occurred", "during discovery" + message);
            }
        };
        try {
            progressBar.setVisibility(View.GONE);
            Log.e("Starting"," printer discovery.");
            NetworkDiscoverer.findPrinters(discoveryHandler);
        } catch (DiscoveryException e) {
            e.printStackTrace();
        }
    }

    private void printText(String texto){
        try {
            if (currentPrinter != null){
                currentPrinter.setText(texto);

                texto = PrintLibrary.getPrinterZebraService().setTextToPrinter(currentPrinter);
                PrintLibrary.getPrinterZebraService().printText(texto);
                //Connection connection = new BluetoothConnection(bluetoothAddress);
                //Map<Integer, String> vars = new HashMap<Integer, String>();
                //vars.put(12, "Dongfeng Visteon Automotive Systems Co. recorte, Ltd."); // Customer Name
                //vars.put(11, "Número de orden"); // Invoice Number
                //vars.put(13, "Nombre del proveedor"); // Vendor Name
                //printer.printStoredFormat("E:FORMAT3.ZPL", vars);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (ZebraPrinterConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void printImage(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        image = null;

        int initImage = centerImage();
        try {
            image = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.image,options);
            String str = PrintLibrary.getPrinterZebraService().setImageToPrinter();
            PrintLibrary.getPrinterZebraService().sendText(str);
            initImage = centerImage();
            PrintLibrary.getPrinterZebraSingleton().printImage(initImage, image, 200, 100);

        } catch (ZebraPrinterConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private int centerImage(){
        int position = 0;

        if (currentPrinter != null){
            currentPrinter.setInches(3);
            position = (currentPrinter.getWidth() - 200) / 2;
        }
        return position;
    }

    public class FormatUtilSample {
        // public static void main(String[] args) {
        //     try {
        //         new FormatUtilExample().example1();
        //         new FormatUtilExample().example2();
        //         new FormatUtilExample().example3();
        //     } catch (ConnectionException e) {
        //         e.printStackTrace();
        //     }
        // }

        // Print a stored format with the given variables. This ZPL will store a format on a printer, for use with example1.
        // ^XA
        // ^DFE:FORMAT1.ZPL
        // ^FS
        // ^FT26,243^A0N,56,55^FH\^FN12"First Name"^FS
        // ^FT26,296^A0N,56,55^FH\^FN11"Last Name"^FS
        // ^FT258,73^A0N,39,38^FH\^FDVisitor^FS
        // ^BY2,4^FT403,376^B7N,4,0,2,2,N^FH^FDSerial Number^FS
        // ^FO5,17^GB601,379,8^FS
        // ^XZ

        private void example1() throws ConnectionException {
            Connection connection = new TcpConnection("192.168.1.32", TcpConnection.DEFAULT_ZPL_TCP_PORT);
            try {
                connection.open();
                ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                Map<Integer, String> vars = new HashMap<Integer, String>();
                vars.put(12, "John");
                vars.put(11, "Smith");
                printer.printStoredFormat("E:FORMAT1.ZPL", vars);
            } catch (ConnectionException e) {
                e.printStackTrace();
            } catch (ZebraPrinterLanguageUnknownException e) {
                e.printStackTrace();
            } finally {
                connection.close();
            }
        }

        // Print a stored format with the given variables. This ZPL will store a format on the Link-OS&#0153; printer, for
        // use with
        // example2.
        // ^XA
        // ^DFE:FORMAT2.ZPL
        // ^FS
        // ^FT26,243^A0N,56,55^FH\^FN12"First Name"^FS
        // ^FT26,296^A0N,56,55^FH\^FN11"Last Name"^FS
        // ^FT258,73^A0N,39,38^FH\^FDVisitor^FS
        // ^FO100,100^XG^FN13,1,1^FS
        // ^FO5,17^GB601,379,8^FS
        // ^XZ

        private void example2() throws ConnectionException {
            Connection connection = new TcpConnection("192.168.1.32", TcpConnection.DEFAULT_ZPL_TCP_PORT);
            try {
                connection.open();
                Map<Integer, String> vars = new HashMap<Integer, String>();
                vars.put(12, "John");
                vars.put(11, "Smith");
                vars.put(13, "R:PIC.GRF");
                ZebraPrinter genericPrinter = ZebraPrinterFactory.getInstance(connection);
                ZebraPrinterLinkOs linkOsPrinter = ZebraPrinterFactory.createLinkOsPrinter(genericPrinter);
                if (linkOsPrinter != null) {
                    linkOsPrinter.printStoredFormatWithVarGraphics("E:FORMAT2.ZPL", vars);
                }
            } catch (ConnectionException e) {
                e.printStackTrace();
            } catch (ZebraPrinterLanguageUnknownException e) {
                e.printStackTrace();
            } finally {
                connection.close();
            }
        }

        // Print a stored format with the given variables. This ZPL will store a format on a printer,
        // for use with example3.
        // This example also requires the ANMDS.TTF font to have been download to the printer prior to using this code.
        //^XA
        //^DFE:FORMAT3.ZPL
        //^FS
        //^FT26,223^FH^A@N,56,55,E:ANMDS.TTF^CI28^FH\^FN12"Customer Name"^FS
        //^FT26,316^FH\^A@N,56,55,E:ANMDS.TTF^FH\^FN11"Invoice Number"^FS
        //^FT348,73^FH^A@N,39,38,E:ANMDS.TTF^FH\^FN13"Vendor Name^FS
        //^BY2,4^FT643,376^B7N,4,0,2,2,N^FH\^FDSerial Number^FS
        //^FO5,17^GB863,379,8^FS
        //^XZ

        private void example3() throws ConnectionException {
            Connection connection = new TcpConnection("192.168.1.32", TcpConnection.DEFAULT_ZPL_TCP_PORT);
            try {
                connection.open();
                ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                Map<Integer, String> vars = new HashMap<Integer, String>();
                vars.put(12, "Dongfeng Visteon Automotive Systems Co. recorte, Ltd."); // Customer Name
                vars.put(11, "Número de orden"); // Invoice Number
                vars.put(13, "Nombre del proveedor"); // Vendor Name
                printer.printStoredFormat("E:FORMAT3.ZPL", vars);
            } catch (ConnectionException e) {
                e.printStackTrace();
            } catch (ZebraPrinterLanguageUnknownException e) {
                e.printStackTrace();
            } finally {
                connection.close();
            }
        }
    }
}