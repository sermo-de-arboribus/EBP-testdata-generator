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

public class DropboxUploaderThread extends Thread
{
	private java.io.File fileToUpload;
	private Title title;
	private static final String APP_KEY = "4iwecg3jddfa352";
	private static final String APP_SECRET = "6kqbbwd54m0jsjn";
	
	public DropboxUploaderThread(java.io.File file, Title title)
	{
		fileToUpload = file;
		this.title = title;
		ConfigurationRegistry registry = ConfigurationRegistry.getRegistry();
		Properties properties = System.getProperties();
		properties.setProperty("javax.net.ssl.trustStore", FilenameUtils.normalize(registry.getString("ssl.pathToCacerts")));
		properties.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		if(registry.getBooleanValue("net.useProxy"))
		{
			properties.setProperty("http.proxyHost", registry.getString("net.proxy.host"));
			properties.setProperty("http.proxyPort", registry.getString("net.proxy.port"));
			properties.setProperty("https.proxyHost", registry.getString("net.proxy.host"));
			properties.setProperty("https.proxyPort", registry.getString("net.proxy.port"));
		}
	}
	
	public void run()
	{
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
        
        if(metadata == null)
        {
        	Utilities.showWarnPane("Cover file upload for title \"" + title.getName()  + "\" to Dropbox has failed.\nYou must upload the file yourself and set the MediaFileLink in the ONIX manually");
        }
        else
        {
            System.out.print(metadata.toStringMultiline());
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
        	String url = shareableUrl.replace("?dl=0", "?dl=1");
        	title.setMediaFileUrl(url);
        }
        
        /* This would be code for using user authentication / user's own Dropbox account.
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
		String authorizeUrl = webAuth.start();
        System.out.println("1. Go to " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first).");
        System.out.println("3. Copy the authorization code.");
        System.out.print("Enter the authorization code here: ");      
        String code = null;

		try
		{
			code = new BufferedReader(new InputStreamReader(System.in)).readLine();
		}
		catch (IOException e)
		{
			System.out.println("Error reading your user input");
			e.printStackTrace();
		}
        if (code == null)
        {
            System.exit(1); 
            return;
        }
        code = code.trim();

        DbxAuthFinish authFinish;
        try
        {
            authFinish = webAuth.finish("ch5YeQ5Sn0AAAAAAAAADEIsTX_J03cOV88I5ha-17ME9b_hVBAy3dCsfo11Qjxqe");
        }
        catch (DbxException ex)
        {
            System.err.println("Error in DbxWebAuth.start: " + ex.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("Authorization complete.");
        System.out.println("- User ID: " + authFinish.userId);
        System.out.println("- Access Token: " + authFinish.accessToken);

        // Save auth information to output file.
        DbxAuthInfo authInfo = new DbxAuthInfo(authFinish.accessToken, appInfo.host);
        java.io.File pathToConfigDir = Utilities.getConfigDir();
        java.io.File pathToDropboxAuthFile = new java.io.File(FilenameUtils.concat(pathToConfigDir.getPath(), "dbxWbAuth.txt"));
        try
        {
            DbxAuthInfo.Writer.writeToFile(authInfo, pathToDropboxAuthFile);
            System.out.println("Saved authorization information to \"" + pathToDropboxAuthFile.getPath() + "\".");
        }
        catch (IOException ex)
        {
            System.err.println("Error saving to <auth-file-out>: " + ex.getMessage());
            System.err.println("Dumping to stderr instead:");
            System.exit(1); 
            return;
        }*/
	}
}
