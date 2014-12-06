package com.samples;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ICounter {
	
	public int getCounter(boolean inc) throws FileNotFoundException, IOException;
	public int getCounter() throws FileNotFoundException, IOException;
	public boolean saveCounter(int count);
	public boolean incrementCounter();

}
