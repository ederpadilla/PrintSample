package androiddeveloper.eder.padilla.printsample.zebraprintinglibrary;


import androiddeveloper.eder.padilla.printsample.zebraprintinglibrary.printer.PrinterZebraService;
import androiddeveloper.eder.padilla.printsample.zebraprintinglibrary.printer.PrinterZebraSingleton;
import androiddeveloper.eder.padilla.printsample.zebraprintinglibrary.utils.GlobalesCustom;

public class PrintLibrary {
	private static PrintLibrary INSTANCE;
//	public static Printer actualPrinter = new Printer();
	
	private PrintLibrary(){
	}
	
	public static PrintLibrary getInstance(){
		if (INSTANCE == null)
			INSTANCE = new PrintLibrary();
		
		return INSTANCE;
	}
	
	public static PrinterZebraService getPrinterZebraService(){
		return PrinterZebraService.getInstance();
	}
	
	public static PrinterZebraSingleton getPrinterZebraSingleton(){
		return PrinterZebraSingleton.getInstance();
	}
	
	public static GlobalesCustom getGlobals(){
		return GlobalesCustom.getInstance();
	}

}
