package testdatagen;

import com.dropbox.core.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;

import testdatagen.utilities.Utilities;

public class DropboxUploaderThread extends Thread
{
	private java.io.File fileToUpload;
	private static final String APP_KEY = "4iwecg3jddfa352";
	private static final String APP_SECRET = "6kqbbwd54m0jsjn";
	
	public DropboxUploaderThread(java.io.File file)
	{
		fileToUpload = file;
	}
	
	public void run()
	{
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		DbxRequestConfig config = new DbxRequestConfig("EBP-Testdata-Generator", Locale.getDefault().toString());
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
		
		String authorizeUrl = webAuth.start();
        System.out.println("1. Go to " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first).");
        System.out.println("3. Copy the authorization code.");
        System.out.print("Enter the authorization code here: ");
        
        String code = null;;
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
            authFinish = webAuth.finish(code);
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
        }
	}
}