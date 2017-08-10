package androiddeveloper.eder.padilla.printsample;

/**
 * Created by ederpadilla on 09/08/17.
 */

import android.os.Looper;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.BluetoothConnectionInsecure;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

public class SecondPrintSample {

       // SecondPrintSample example = new SecondPrintSample();
       // String theBtMacAddress = "AC:3F:A4:1B:20:A6";
       // example.sendZplOverBluetooth(theBtMacAddress);
       // example.sendCpclOverBluetooth(theBtMacAddress);

    void sendCpclOverBluetooth(final String theBtMacAddress) {

        new Thread(new Runnable() {
            public void run() {
                try {

                    // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                    Connection thePrinterConn = new BluetoothConnectionInsecure(theBtMacAddress);

                    // Initialize
                    Looper.prepare();

                    // Open the connection - physical connection is established here.
                    thePrinterConn.open();

                    // This example prints "This is a CPCL test." near the top of the label.
                    String cpclData = "! 0 200 200 210 1\r\n"
                            + "TEXT 4 0 30 40 This is a CPCL test.\r\n"
                            + "FORM\r\n"
                            + "PRINT\r\n";

                    // Send the data to printer as a byte array.
                    thePrinterConn.write(cpclData.getBytes());
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

    public void teest(final String theBtMacAddress, final String image){
        new Thread(new Runnable() {
            public void run() {
                try {
                    // Instantiate connection for given Bluetooth&reg; MAC Address.
                    BluetoothConnection thePrinterConn = new BluetoothConnection(theBtMacAddress);
                    String secondZpl = "^XA ^JMA,12^FS^ ^FX Top section with company logo, name and address.^CF0,50 ^FO30,50^FDDRIVE^FS ^CF0,20 ^FO30,100^FDExpo Santa fe^FS ^FO30,135^FDShelbyville TN 38102^FS ^FO30,170^FDUnited States (USA)^FS ^FO20,230^GB500,1,3^FS ^FX Second section with recipient address and permit information.^CFA,20 ^FO20,280^FDPlacas: 345AAA^FS ^FO20,320^FDMarca: Audi^FS ^FO20,360^FDModelo: A3^FS ^FO20,400^FDEntrada: 10-08-2017 10:23am^FS ^FX Third section with qr code. ^FO20,420^BQN,5,10^FD///userid/aaaaaaaaaa/valetid/4^FS^XZ ^FO20,450,0^IMR:http://app.driveapp.mx/drive/valet/images/image_1_55_2017_08_08_18_10_33.png^FS ^FO0,0^GFA,^XZ";
                    // Initialize

                    String imageZpl ="^XA^FX Top section with company logo, name and address.^CF0,60 ^FO30,50^FDDRIVE^FS ^CF0,30 ^FO30,100^FDExpo Santa fe^FS ^FO30,135^FDShelbyville TN 38102^FS ^FO30,170^FDUnited States (USA)^FS ^FO20,230^GB450,1,3^FS ^FX Second section with recipient address and permit information.^CFA,25 ^FO20,280^FDPlacas: 345AAA^FS ^FO20,320^FDMarca: Audi^FS ^FO20,360^FDModelo: A3^FS ^FO20,400^FDEntrada: 10-08-2017 10:23am^FS ^CFA,10 ^FO20,460^GB450,1,3^FS ^FX Third section with qr code. ^FO20,470^BQN,5,10^FD///userid/aaaaaaaaaa/valetid/4^FS^FO50,50^GFB,32,32,2, "+image+"^FS^XZ";
                    Looper.prepare();
                    thePrinterConn.open();

                    // Open the connection - physical connection is established here.
                    ZebraPrinter zPrinterIns = ZebraPrinterFactory.getInstance(thePrinterConn);
                    zPrinterIns.sendCommand("! U1 setvar \"device.languages\" \"zpl\"\r\n");
                    zPrinterIns.sendCommand("~jc^xa^jus^xz");
                    Thread.sleep(500);

                    // Send the data to printer as a byte array.
                    zPrinterIns.sendCommand(imageZpl);

                    // Make sure the data got to the printer before closing the connection
                    Thread.sleep(500);

                    // Close the connection to release resources.
                    thePrinterConn.close();

                    Looper.myLooper().quit();
                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private boolean isPrinterReady(Connection thePrinterConn) {
        boolean isOK = false;
        try {
            thePrinterConn.open();
            // Creates a ZebraPrinter object to use Zebra specific functionality like getCurrentStatus()
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(thePrinterConn);

            PrinterStatus printerStatus = printer.getCurrentStatus();
            if (printerStatus.isReadyToPrint) {
                isOK = true;
            } else if (printerStatus.isPaused) {
                System.out.println("Cannot Print because the printer is paused.");
            } else if (printerStatus.isHeadOpen) {
                System.out.println("Cannot Print because the printer media door is open.");
            } else if (printerStatus.isPaperOut) {
                System.out.println("Cannot Print because the paper is out.");
            } else {
                System.out.println("Cannot Print.");
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (ZebraPrinterLanguageUnknownException e) {
            e.printStackTrace();
        }
        return isOK;
    }

}
