package assign.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class OpenStackMeetingsServiceImpl implements OpenStackMeetingsService {	
	
	public List<String> getData(String link, String value) {
		Document doc = null;
		// try to load the project page
		try {
			doc = Jsoup.connect(link + "/" + value).get();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// get the web page's elements
		Elements items = null;
		if (doc != null) {
			items = doc.select("tr td a[href]");		
		} else {
			return null;
		}
		
		// extract the elements and store them in a linked list
		List<String> itemList = new LinkedList<String>();
		ListIterator<Element> iterator = items.listIterator();		    	
		while(iterator.hasNext()) {
	    	Element e = (Element) iterator.next();
    		String s = e.html();
    		if (s.equalsIgnoreCase("Parent Directory"))
    			continue;
    		itemList.add(s);
	    }
		return itemList;
	}
	
}
