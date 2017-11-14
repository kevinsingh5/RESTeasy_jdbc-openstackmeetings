package assign.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import assign.domain.NewProject;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;


public class OpenStackMeetingsServiceImpl implements OpenStackMeetingsService {	
	
	String dbURL = "";
	String dbUsername = "";
	String dbPassword = "";
	DataSource ds;
	
	public OpenStackMeetingsServiceImpl(String dburl, String username, String password) {
		this.dbURL = dburl;
		this.dbUsername = username;
		this.dbPassword = password;
		
		ds = setupDataSource();
	}
	
	// sets up the required fields for DataSource
	private DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername(this.dbUsername);
        ds.setPassword(this.dbPassword);
        ds.setUrl(this.dbURL);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        return ds;
	}

	public NewProject addProject (NewProject proj) throws Exception {
		// establish connection to the database specified by DataSource ds
		Connection conn = ds.getConnection();
		
		String insert = "INSERT INTO projects(name, description) VALUES(?, ?)";
		PreparedStatement stmt = conn.prepareStatement(insert,
                Statement.RETURN_GENERATED_KEYS); // gets keys back
		
		stmt.setString(1, proj.getName());
		stmt.setString(2, proj.getDescription());
		
		int affectedRows = stmt.executeUpdate(); // executeUpdate() doesn't return any values

        if (affectedRows == 0) {
            throw new SQLException("Creating project failed, no rows affected.");
        }
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
        	proj.setProjectID(generatedKeys.getInt(1));
        }
        else {
            throw new SQLException("Creating project failed, no ID obtained.");
        }
        
        // Close the connection
        conn.close();
        
		return proj;
	}

//	public List<String> getData(String link, String value) {
//		Document doc = null;
//		// try to load the project page
//		try {
//			doc = Jsoup.connect(link + "/" + value).get();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//		// get the web page's elements
//		Elements items = null;
//		if (doc != null) {
//			items = doc.select("tr td a[href]");		
//		} else {
//			return null;
//		}
//		
//		// extract the elements and store them in a linked list
//		List<String> itemList = new LinkedList<String>();
//		ListIterator<Element> iterator = items.listIterator();		    	
//		while(iterator.hasNext()) {
//	    	Element e = (Element) iterator.next();
//    		String s = e.html();
//    		if (s.equalsIgnoreCase("Parent Directory"))
//    			continue;
//    		itemList.add(s);
//	    }
//		return itemList;
//	}
	
}
