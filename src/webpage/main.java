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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class main 
{
	public static void main(String args[]) throws Exception
	{
		HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
		server.createContext("/", new Page("main.txt", "HomePage", "style.css"));
		server.createContext("/art", new Page("art.txt", "Art", "style.css"));
		server.createContext("/certifications", new Page("certifications.txt", "Certification", "style.css"));
		server.createContext("/style.css", new NoTopPage("style.css"));
		//server.createContext("/trees.jpg", new ImageResponse("trees.jpg"));
        server.setExecutor(null);
		server.start();
	}
	public static String getTop()
	{
		return getTextFromFile("top.txt");
	}
	public static String getHead(String title, String css)
	{
		return  "<head>"
				+ "<title>"
				+ title
				+ "</title>"
				+ "<style>"
				+ getTextFromFile(css)
				+ "</style>"
				+ "</head>";
	}
	public static String getTextFromFile(String fileName)
	{
		String a = "file read error :(";
		try
		{
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
	String title;
	String css;
	public Page(String name, String title, String css)
	{
		this.name = name;
		this.title = title;
		this.css = css;
	}
    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = main.getHead(title, css) + main.getTop() + main.getTextFromFile(name);
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
class ImageResponse implements HttpHandler 
{
	String name;
	public ImageResponse(String name)
	{
		this.name = name;
	}
    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = main.getTextFromFile(name);
        System.out.println(response);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}