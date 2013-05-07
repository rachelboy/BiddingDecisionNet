package edu.olin.rboy.bridge;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Serial<T extends Serializable> {
	
	public void save(FileOutputStream f_out, T obj) {
		try {
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
			obj_out.writeObject (obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public T load(FileInputStream f_in) {
		ObjectInputStream o_in;
		try {
			o_in = new ObjectInputStream(f_in);
			return (T) o_in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
