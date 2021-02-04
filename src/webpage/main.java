package webpage;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.io.OutputStream;
//import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class main 
{
	public static void main(String args[]) throws Exception
	{
		// HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 80), 0);
		HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
		server.createContext("/", new Page("main.txt"));
		server.createContext("/art", new Page("art.txt"));
		server.createContext("/certifications", new Page("certifications.txt"));
		server.createContext("/style.css", new NoTopPage("style.css"));
        server.setExecutor(null);
		server.start();
	}
	public static String getTop()
	{
		return getTextFromFile("top.txt");
	}
	public static String getTextFromFile(String fileName)
	{
		String a = "file read error :(";
		try
		{
			//BufferedReader r = new BufferedReader(new FileReader(fileName));
			//System.out.println("/"+fileName);
			//System.out.println("/webpage/"+fileName);
			InputStream is = main.class.getResourceAsStream("/webpage/"+fileName);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader r = new BufferedReader(isr);
			String line = r.readLine();
			a = "";
			while(line != null)
			{
				a = a + line;
				line = r.readLine();
			}
			//r.close();
		}
		catch(Exception e){e.printStackTrace();}
		return a;
	}
}

class Page implements HttpHandler 
{
	String name;
	public Page(String name)
	{
		this.name = name;
	}
    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = main.getTop() + main.getTextFromFile(name);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
class NoTopPage implements HttpHandler 
{
	String name;
	public NoTopPage(String name)
	{
		this.name = name;
	}
    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = main.getTextFromFile(name);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}