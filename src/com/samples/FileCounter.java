package com.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileCounter implements ICounter {
	
	private final String COUNTER_FILE_NAME = "filecounter.txt";
	
	@Override
	public int getCounter(boolean inc) throws FileNotFoundException, IOException {
		int count = getCounter();
		if(inc)
			incrementCounter();
		
		return count;
	}
	
	@Override
	public int getCounter() throws FileNotFoundException, IOException {
		File f = new File(COUNTER_FILE_NAME);
		if(!f.exists()) {
			saveCounter(0);
		}
		
		InputStream fiStm = new FileInputStream(COUNTER_FILE_NAME);
		ObjectInputStream oiStm = new ObjectInputStream(fiStm);
		int count = oiStm.readInt();
		oiStm.close();
		return count;
	}
	
	@Override
	public boolean saveCounter(int count) {
		ObjectOutputStream oStm = null;
		try {
			oStm = new ObjectOutputStream(new FileOutputStream(COUNTER_FILE_NAME));
			oStm.writeInt(count);
			oStm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public boolean incrementCounter() {
		try {
			int count = getCounter();
			return saveCounter(count+1);
		} catch (IOException iox) {
			return false;
		}
	}
}
