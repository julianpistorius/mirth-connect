package com.webreach.mirth.model.converters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectStringSerializer implements Serializer<Object> {
	public String serialize(Object source) {
		String data = null;
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		
		try {
			ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);
			objectOutStream.writeObject(source);
			objectOutStream.flush();
			data = byteOutStream.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				byteOutStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	public Object deserialize(String source) {
		Object data = null;
		ByteArrayInputStream byteInStream = new ByteArrayInputStream(source.getBytes());

		try {
			ObjectInputStream objectInStream = new ObjectInputStream(byteInStream);
			data = objectInStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				byteInStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}
}
