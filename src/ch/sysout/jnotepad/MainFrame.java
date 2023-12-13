package ch.sysout.jnotepad;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JMenuBar mnb;
	private JMenu mnuFile;
	private JMenu mnuEdit;
	private JMenu mnuFormat;
	private JMenu mnuView;
	private JCheckBoxMenuItem itmEmbeddedMenuBar;
	private JMenu mnuHelp;
	private JMenuItem itmSave;
	private JMenuItem itmSaveAs;
	private JMenu mnuExportAs;
	private JMenuItem itmExportToJPG;
	private JMenuItem itmExportToPNG;
	private JMenuItem itmExportToPDF;
	private JCheckBoxMenuItem itmLineWrap;
	private JCheckBoxMenuItem itmWordWrap;
	private JMenuItem itmFont;

	private MainPanel pnlMain;

	private JPanel pnlSaveOptions;

	private JLabel txtStatus;

	private JMenuItem itmNew;
	private JMenuItem itmNewWindow;
	private JMenuItem itmOpen;
	private JMenuItem itmExit;

	private JTabbedPane tpMain;

	protected int lastSelectedTabIndex;

	private JFileChooser fc;

	public MainFrame() {
		super("Untitled - JNotePad");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getRootPane().putClientProperty("JRootPane.menuBarEmbedded", false);
		URL imageResource = getClass().getClassLoader().getResource("images/memo.png");
		if (imageResource != null) {
			setIconImage(new ImageIcon(imageResource).getImage());
		}
		initComponents();
		addListeners();
		createUI();
		pack();
	}

	private void initComponents() {
		mnb = new JMenuBar();
		mnuFile = new JMenu("File");
		itmNew = new JMenuItem("New");
		itmNewWindow = new JMenuItem("New Window");
		itmOpen = new JMenuItem("Open...");
		itmSave = new JMenuItem("Save");
		itmSaveAs = new JMenuItem("Save as...");
		mnuExportAs = new JMenu("Export as");
		itmExportToJPG = new JMenuItem("JPG");
		itmExportToPNG = new JMenuItem("PNG");
		itmExportToPDF = new JMenuItem("PDF");
		itmExit = new JMenuItem("Exit");

		mnuEdit = new JMenu("Edit");
		mnuFormat = new JMenu("Format");
		mnuView = new JMenu("View");
		itmEmbeddedMenuBar = new JCheckBoxMenuItem("Embedded menu bar");
		Object embeddedMenuBar = getRootPane().getClientProperty("JRootPane.menuBarEmbedded");
		System.out.println(embeddedMenuBar);
		itmEmbeddedMenuBar.setSelected(embeddedMenuBar == null ? false : embeddedMenuBar.toString().toLowerCase().equals("true"));
		mnuHelp = new JMenu("Help");
		itmLineWrap = new JCheckBoxMenuItem("Line Wrap");
		itmWordWrap = new JCheckBoxMenuItem("Word Wrap");
		itmFont = new JMenuItem("Font...");

		tpMain = new JTabbedPane();
		tpMain.putClientProperty("JTabbedPane.tabClosable", true);
		tpMain.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		pnlMain = new MainPanel();

		pnlSaveOptions = new JPanel(new BorderLayout());
	}

	private void addListeners() {
		addKeyListener(new KeyListener() {

			private boolean control;

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					control = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (control) {
					if (e.getKeyCode() == KeyEvent.VK_T) {
						newWindowOrTab();
					}
					if (e.getKeyCode() == KeyEvent.VK_W) {
						tpMain.remove(tpMain.getTabCount()-1);
						tpMain.setSelectedIndex(lastSelectedTabIndex);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					control = true;
				}
			}
		});
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		itmNew.addActionListener(this);
		itmNewWindow.addActionListener(this);
		itmOpen.addActionListener(this);
		itmEmbeddedMenuBar.addActionListener(this);
		itmLineWrap.addActionListener(this);
		itmWordWrap.addActionListener(this);
		itmExit.addActionListener(this);

		pnlMain.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					doPop(e);
					pnlMain.repaint();
				}
			}

			private void doPop(MouseEvent e) {
				PopUpDemo menu = new PopUpDemo();
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		pnlMain.addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				textChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				textChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textChanged();
			}

			private void textChanged() {
				if (!getTitle().startsWith("*")) {
					setTitle("*" + getTitle());
				} else {
					if (pnlMain.isDocumentEmpty()) {
						setTitle(getTitle().replace("*", ""));
					}
				}
			}
		});
	}

	protected void newWindowOrTab() {
		lastSelectedTabIndex = tpMain.getSelectedIndex();
		tpMain.addTab("new " + (tpMain.getTabCount()+1), new JPanel());
		tpMain.setSelectedIndex(tpMain.getTabCount()-1);
	}

	private void createUI() {
		mnb.add(mnuFile);
		mnuFile.add(itmNew);
		mnuFile.add(itmNewWindow);
		mnuFile.add(itmOpen);
		mnuFile.add(itmSave);
		mnuFile.add(itmSaveAs);
		mnuFile.add(new JSeparator());
		mnuFile.add(mnuExportAs);
		mnuFile.add(new JSeparator());
		mnuFile.add(itmExit);

		mnb.add(mnuEdit);
		mnb.add(mnuFormat);
		mnb.add(mnuView);
		mnuView.add(itmEmbeddedMenuBar);
		mnb.add(mnuHelp);
		mnuExportAs.add(itmExportToJPG);
		mnuExportAs.add(itmExportToPNG);
		mnuExportAs.add(itmExportToPDF);
		mnuFormat.add(itmLineWrap);
		mnuFormat.add(itmWordWrap);
		mnuFormat.add(new JSeparator());
		mnuFormat.add(itmFont);
		setJMenuBar(mnb);
		//		add(pnlImageOptions, BorderLayout.NORTH);
		tpMain.addTab("new 1", pnlMain);
		add(tpMain);
		txtStatus = new JLabel("bla");
		pnlSaveOptions.add(txtStatus);
		add(pnlSaveOptions, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(itmNew)) {
			int confirm = JOptionPane.YES_OPTION;
			if (!pnlMain.isDocumentEmpty()) {
				confirm = JOptionPane.showConfirmDialog(this, "Do you really want to discard all unsaved changes and create a new document?", "Creating new document", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			}
			if (confirm == JOptionPane.YES_OPTION) {
				pnlMain.createNewDocument();
			}
		} else if (source.equals(itmNewWindow)) {
			newWindowOrTab();
		} else if (source.equals(itmOpen)) {
			openFile();
		} else if (source.equals(itmLineWrap)) {
			pnlMain.setLineWrap(itmLineWrap.isSelected());
			itmWordWrap.setEnabled(itmLineWrap.isSelected());
		} else if (source.equals(itmWordWrap)) {
			pnlMain.setWordWrap(itmWordWrap.isSelected());
		} else if (source.equals(itmEmbeddedMenuBar)){
			getRootPane().putClientProperty("JRootPane.menuBarEmbedded", itmEmbeddedMenuBar.isSelected());
		} else if (source.equals(itmExit)) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		pnlMain.repaint();
	}

	private void openFile() {
		if (fc == null) {
			fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
		}
		//		fc.setCurrentDirectory("");
		int request = fc.showOpenDialog(this);
		if (request == JFileChooser.APPROVE_OPTION) {
			File fileToLoad = fc.getSelectedFile();
			pnlMain.clearText();
			pnlMain.openFile(fileToLoad.toPath());
		}
	}

	class PopUpDemo extends JPopupMenu {
		private JMenuItem itmCopy;
		private JMenuItem itmOpenInExplorer;

		public PopUpDemo() {
			itmCopy = new JMenuItem("Copy (Ctrl+C)");
			itmOpenInExplorer = new JMenuItem("Open File Path");
			add(itmCopy);
			add(itmOpenInExplorer);

			itmCopy.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					//					TransferableImage trans = new TransferableImage(currentImage);
					//					clipboard.setContents(trans, null);
					//					txtLog.setText("copied image");
				}
			});

			itmOpenInExplorer.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//					try {
					//						Runtime.getRuntime().exec("explorer.exe /select," + currentFilePath);
					//					} catch (IOException e1) {
					//						e1.printStackTrace();
					//					}
				}
			});
		}
	}

	private class TransferableImage implements Transferable {
		private Image image;

		public TransferableImage(Image image) {
			this.image = image;
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (flavor.equals(DataFlavor.imageFlavor) && image != null) {
				return image;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] flavors = new DataFlavor[1];
			flavors[0] = DataFlavor.imageFlavor;
			return flavors;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			DataFlavor[] flavors = getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavor.equals(flavors[i])) {
					return true;
				}
			}
			return false;
		}
	}
}