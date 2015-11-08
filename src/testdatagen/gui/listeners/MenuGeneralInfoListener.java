package testdatagen.gui.listeners;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class MenuGeneralInfoListener implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		JFrame infoWindow = new JFrame("How to use the Testdata Generator");
		infoWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		infoWindow.setLocation(50, 50);
		infoWindow.setSize(800, 600);
		
		JEditorPane textArea = new JEditorPane();
		textArea.setEditable(false);
		textArea.setContentType("text/html");
		textArea.setText(HTML_CONTENT);

		JScrollPane editorScrollPane = new JScrollPane(textArea);
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editorScrollPane.setPreferredSize(new Dimension(250, 145));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
		
		infoWindow.add(editorScrollPane);

		infoWindow.setVisible(true);
	}

	private static final String HTML_CONTENT =
			"<html>\n" +
			"  <head>\n" +
			"    <title>How to use the Testdata Generator</title>\n" +
			"  </head>\n" +
			"  <body>\n" +
			"    <h1>How to use the Testdata Generator</h1>\n" +
			"    <p>In the Testdata Generator for the E-Book Plant you can maintain several test scenarios. " +
			"       A scenario defines one to many products that belong to the given scenario. " +
			"       A product is defined in terms of the products that are used in the E-Book Plant. " +
			"       Many aspects of a product will be filled in randomly during the product asset" +
			"       generation. Other aspects are configurable. Just have a look around and try it out by yourself.</p>\n" +
			"    <h2>First steps</h2>" +
			"    <p>Start a new scenario. Add a few titles to the scenario. Save the scenario to disc. " +
			"       Generate the files based on the scenario.</p>\n" +
			"    <p>When the program starts, it opens with three demo scenarios. You can also start with " +
			"       generating the scenario files of these demo scenarios.</p>\n" +
			"    <h2>A note on uploads</h2>\n" +
			"    <p>The Testdata Generator can upload media files (e.g. book covers) to the Dropbox and " +
			"       insert the URL to download those uploaded cover files into the ONIX files' &lt;MediaFile&gt;" +
			"       or &lt;SupportingResource&gt; elements. This certainly only works if the firewall doesn't " +
			"       block HTTPS-traffic with the Dropbox API. If the Dropbox service is not reachable, you " +
			"       will be notified to take care of uploading the file and inserting the download link into " +
			"       the ONIX file by yourself.</p>\n" +
			"    <h2>Creating unique ISBNs</h2>\n" +
			"    <p>At the moment the mechanism of creating unique ISBNs is not very elaborate. On first run " +
			"       the Testdata Generator will create a configuration directory where it stores the most" +
			"       recently created ISBN. Every further product added to a scenario will get the most recent" +
			"       ISBNumber plus one. If the Testdata Generator is used in a team, it is recommended to store " +
			"       the Testdata Generator on a network share and let all users start Testdata Generator from the same " +
			"       .jar file, as this will ensure \"globally\" unique ISBNs.</p>" +
			"    <h2>Configuration options</h2>" +
			"    <p>When you add a title to a scenario, you're given several configuration options. They are meant" +
			"       for your convenience, but even if you do adjust any settings on the title configuration screen," +
			"       the Testdata Generator will output valid files for you. The Testdata Generator has built-in defaults" +
			"       for all required ONIX values, which it will use. Many ONIX options (marked with \"additional ...\")" +
			"       will keep the default ONIX elements and just add further elements to the output ONIX file.</p>" +
			"  </body>\n" +
			"</html>";
}