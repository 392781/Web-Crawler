package crawler;

import java.io.IOException;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Spider {
	private static HashSet<String> visited = new HashSet<String>();
	private static Queue<String> links = new LinkedList<String>();
	
	public static void crawl(String URL) throws IOException {
		links.add(URL);
		visited.add(URL);
		
		while (!links.isEmpty()) {
			String current = links.poll();
			
			try {
				Connection connection = Jsoup.connect(current);
				System.out.println("[ Connected to: " + current + " ]");
				try {
					Document htmlDocument = connection.get();
					Elements linksOnPage = htmlDocument.select("a[href]");
					for (Element link : linksOnPage) {
						if (!visited.contains(link.absUrl("href"))) {
							links.add(link.absUrl("href"));
							visited.add(link.absUrl("href"));
						}
					}
				} catch (UnsupportedMimeTypeException | HttpStatusException e) {
					continue;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("[ Unable to connect to" + current + " ]");
				continue;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		crawl("http://www.reddit.com/");
	}
}
