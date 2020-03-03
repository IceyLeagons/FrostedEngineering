package net.iceyleagons.frostedengineering.storage.mongo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public abstract class MongoClientAbstract {
	
	public MongoClient client;
	public String database;
	
	public MongoClientAbstract(String host, String port, String user, String password, String database) {
		try {
			String encodedpwd = URLEncoder.encode(password, "UTF-8");
			String encodeduser = URLEncoder.encode(user, "UTF-8");
			client = new MongoClient(new MongoClientURI("mongodb://"+encodeduser+":"+encodedpwd+"@"+host+":"+port+"/"+database));
		} catch (UnknownHostException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
