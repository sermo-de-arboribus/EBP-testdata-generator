package testdatagen.gui.listeners;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * Listener for displaying the "About" information when clicking the corresponding menu item
 */
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
			"    <h1>About the Testdata Generator 1.1</h1>\n" +
			"    <p>This is version 1.1 of the Testdata Generator for e-books and metadata.</p>\n" +
			"    <p>If you have questions, ideas, suggestions, bug reports, please contact" +
			"       Kai.Weber@kno-va.de</p>" +
			"    <p>Please let me know, what would be your most urgent or most interesting configuration" +
			"       options that should be implemented as soon as possible.</p>" +
			"    <p>The current roadmap for future releases is:" + 
			"    <ul><li>Implement further ONIX 3 elements</li>"
			+ "      <li>Adjust publisher selection to make it configurable per scenario<li>"
			+ "      <li>Enable a distinction between titles with publication date in the future and those in the past</li>"
			+ "      <li>Add price effective elements</li></ul></p>" +
			"  </body>\n" +
			"</html>";
}