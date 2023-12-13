package ch.sysout.jnotepad;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentListener;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JScrollPane sp;
	private JTextArea txtMain;

	public MainPanel() {
		super(new BorderLayout());
		initComponents();
		createUI();
	}

	private void initComponents() {
		txtMain = new JTextArea();
		txtMain.setLineWrap(true);
		txtMain.setWrapStyleWord(true);
	}

	private void createUI() {
		sp = new JScrollPane(txtMain);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setBorder(BorderFactory.createEmptyBorder());
		add(sp);
	}

	public void addDocumentListener(DocumentListener l) {
		txtMain.getDocument().addDocumentListener(l);
	}

	public void setLineWrap(boolean lineWrap) {
		txtMain.setLineWrap(lineWrap);
		sp.setHorizontalScrollBarPolicy(lineWrap ? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED : JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	}

	public void setWordWrap(boolean wordWrap) {
		txtMain.setWrapStyleWord(wordWrap);
	}

	public boolean isDocumentEmpty() {
		return txtMain.getText().isEmpty();
	}

	public void createNewDocument() {
		clearText();
	}

	public JTextArea getTxtMain() {
		return txtMain;
	}

	public void clearText() {
		txtMain.setText("");
	}

	public void openFile(Path fileToLoad) {
		try (BufferedReader br = Files.newBufferedReader(fileToLoad, StandardCharsets.UTF_8)) {
			String line = "";
			while ((line = br.readLine()) != null) {
				if  (!txtMain.getText().isEmpty()) {
					txtMain.append("\n");
				}
				txtMain.append(line);
			}
		} catch (IOException ex) {
			System.out.format("I/O error: %s%n", ex);
		}
	}
}