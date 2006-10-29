package games.stendhal.client.update;

import java.net.URL;
import java.security.AccessControlException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * a progress bar for the download progress
 */
public class UpdateProgressBar extends JFrame implements HttpClient.ProgressListener {
	private int max = 100;
	private int sizeOfLastFiles = 0;
	private JPanel contentPane = null;
	private JProgressBar progressBar = null;

	/**
	 * Creates update progress bar
	 *
	 * @param max max file size
	 */
	public UpdateProgressBar(int max) {
		super("Downloading...");
		this.max = max;
		try {
			URL url = this.getClass().getClassLoader().getResource("data/gui/StendhalIcon.png");
			setIconImage(new ImageIcon(url).getImage());
		} catch (Exception e) {
			// in case that resource is not avainable
		}

		initializeComponents();

		this.pack();
		try {
			this.setAlwaysOnTop(true);
		} catch (AccessControlException e) {
			// ignore it
		}
	}

	private void initializeComponents() {
		contentPane = (JPanel) this.getContentPane();

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		contentPane.add(new JLabel("Downloading..."));
		contentPane.add(Box.createVerticalStrut(5));

		progressBar = new JProgressBar(0, max);
		progressBar.setStringPainted(false);
		progressBar.setValue(0);
		contentPane.add(progressBar);
	}

	public void onDownloading(int downloadedBytes) {
		progressBar.setValue(sizeOfLastFiles + downloadedBytes);
	}

	public void onDownloadCompleted(int byteCounter) {
		sizeOfLastFiles = sizeOfLastFiles + byteCounter;
		progressBar.setValue(sizeOfLastFiles);
	}

}
