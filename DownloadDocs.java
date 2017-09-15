import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;


public class DownloadDocs 
{
	public void downloadingDocs(String FileName, String str)throws IOException
	{
		try
		{
			System.out.println(FileName);
			File file = new File(FileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(str);
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			System.out.println("Error is " + e.getMessage());
	        e.printStackTrace();
		}
	}

}
