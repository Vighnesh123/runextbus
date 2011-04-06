package org.runextbus.com;


import java.util.List;

public interface DataInterface {
	
	public long insert(String name);
	public void deleteAll();
	public List<String> selectAll();
	
}
