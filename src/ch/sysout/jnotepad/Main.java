package ch.sysout.jnotepad;

import java.io.FileOutputStream;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class Main {
	private static String FILE = "c:/temp/FirstPdf.pdf";

	public Main() {
		//		File tmpFolder = LoadLibs.extractTessResources("win32-x86-64");
		//		System.setProperty("java.library.path", tmpFolder.getPath());
		//		System.out.println(System.getProperty("java.library.path"));
		//
		//		Tesseract tesseract = new Tesseract();
		//		//		tesseract.setLanguage("deu");
		//		tesseract.setOcrEngineMode(1);
		//
		//		try {
		//			URL systemResource = ClassLoader.getSystemResource("data");
		//			if (systemResource != null) {
		//				Path dataDirectory = Paths.get(systemResource.toURI());
		//				tesseract.setDatapath(dataDirectory.toString());
		//				try {
		//					BufferedImage image = ImageIO.read(Main.class.getResourceAsStream("/ocrexample.jpg"));
		//					try {
		//						String result = tesseract.doOCR(image);
		//						System.out.println(result);
		//					} catch (TesseractException e) {
		//						e.printStackTrace();
		//					}
		//				} catch (IOException e) {
		//					e.printStackTrace();
		//				}
		//			}
		//		} catch (URISyntaxException e1) {
		//			e1.printStackTrace();
		//		}

		MainFrame frame = new MainFrame();
		frame.setSize(640, 460);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			FirstPdf.addMetaData(document);
			FirstPdf.addTitlePage(document);
			FirstPdf.addContent(document);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			//			UIManager.setLookAndFeel(new FlatDarkLaf());
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
		new Main();
	}
}
