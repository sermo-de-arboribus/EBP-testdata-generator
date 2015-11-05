package testdatagen.gui.listeners;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class MenuAboutInfoListener implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		JFrame infoWindow = new JFrame("How to use the Testdata Generator");
		infoWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		infoWindow.setLocation(20, 20);
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
			"    <title>About the Testdata Generator</title>\n" +
			"  </head>\n" +
			"  <body>\n" +
			"    <h1>About the Testdata Generator</h1>\n" +
			"    <p>This is an alpha version of the Testdata Generator for the E-Book Plant.</p>\n" +
			"    <p>If you have questions, ideas, suggestions, bug reports, please contact" +
			"       Kai.Weber@knv.de</p>" +
			"  </body>\n" +
			"</html>";
}