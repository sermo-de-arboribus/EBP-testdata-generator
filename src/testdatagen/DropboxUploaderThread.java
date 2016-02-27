package testdatagen;

import com.dropbox.core.*;
import com.dropbox.core.util.IOUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import testdatagen.config.ConfigurationRegistry;
import testdatagen.model.Title;
import testdatagen.utilities.Utilities;

/**
 * This class allows uploads of graphic media files to a Dropbox online storage account.
 * After successful upload a sharing link (URL) will be generated and inserted into the 
 * product's metadata.
 * The Dropbox account that will be used and its credentials are hard-coded.
 * As the upload is an I/O task that depends on bandwidth and network latency, it is 
 * designed to run in parallel to the main thread on a separate thread.
 */
public class DropboxUploaderThread extends Thread
{
	private java.io.File fileToUpload;
	private Title title;
	private static final String APP_KEY = "4iwecg3jddfa352";
	private static final String APP_SECRET = "6kqbbwd54m0jsjn";
	
	/**
	 * Constructor.
	 * @param file The file to be uploaded.
	 * @param title The product object that this file belongs to.
	 */
	public DropboxUploaderThread(java.io.File file, Title title)
	{
		fileToUpload = file;
		this.title = title;
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		Properties properties = System.getProperties();
		// configure SSL certificate of Dropbox
		properties.setProperty("javax.net.ssl.trustStore", FilenameUtils.normalize(registry.getString("ssl.pathToCacerts")));
		properties.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		// check if use of an HTTP proxy is required
		if(registry.getBooleanValue("net.useProxy"))
		{
			properties.setProperty("http.proxyHost", registry.getString("net.proxy.host"));
			properties.setProperty("http.proxyPort", registry.getString("net.proxy.port"));
			properties.setProperty("https.proxyHost", registry.getString("net.proxy.host"));
			properties.setProperty("https.proxyPort", registry.getString("net.proxy.port"));
		}
	}

	/**
	 * Worker thread, does the main work of this class (see description above)
	 */
	public void run()
	{
		// set up account credentials and target folder
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		DbxRequestConfig config = new DbxRequestConfig("EBP-Testdata-Generator", Locale.getDefault().toString());
		DbxClient dbxClient = new DbxClient(config, "ch5YeQ5Sn0AAAAAAAAADEIsTX_J03cOV88I5ha-17ME9b_hVBAy3dCsfo11Qjxqe", appInfo.host);
		
		String dropboxPath = "/" + FilenameUtils.getName(fileToUpload.getPath());
		String shareableUrl = null;
        String pathError = DbxPath.findError(dropboxPath);
        if (pathError != null)
        {
            System.err.println("Invalid <dropbox-path>: " + pathError);
        }
        
        // Upload the file
		DbxEntry.File metadata = null;
        try
        {
            InputStream in = new FileInputStream(fileToUpload);
            try
            {
                metadata = dbxClient.uploadFile(dropboxPath, DbxWriteMode.add(), -1, in);
            }
            catch (DbxException ex)
            {
                System.out.println("Error uploading to Dropbox: " + ex.getMessage());
            }
            finally
            {
                IOUtil.closeInput(in);
            }
        }
        catch (IOException ex)
        {
            System.out.println("Error reading from file \"" + fileToUpload + "\": " + ex.getMessage());
        }
        
        // verify upload and create a shareable URL
        if(metadata == null)
        {
        	Utilities.showWarnPane("Cover file upload for title \"" + title.getName()  + "\" to Dropbox has failed.\nYou must upload the file yourself and set the MediaFileLink in the ONIX manually");
        }
        else
        {
            try
            {
                shareableUrl = dbxClient.createShareableUrl(dropboxPath);
            }
            catch (DbxException ex)
            {
            	Utilities.showWarnPane("Uploaded file for title \"" + title.getName()  + "\" to Dropbox, but cannot create a shareable link");
            }
        }
        
        if(shareableUrl != null)
        {
        	// Make the file directly downloadable, without redirecting to the Dropbox download web page.
        	String url = shareableUrl.replace("?dl=0", "?dl=1");
        	title.setMediaFileUrl(url);
        }
	}
}