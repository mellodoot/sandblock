package net.heyitsultra.UltraNet.serialization;

import static net.heyitsultra.UltraNet.serialization.SerializationUtils.*;

import java.io.*;
/*
import java.net.*;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.nio.file.WatchEvent.*;
*/
import java.util.*;
import java.util.regex.*;

public class UNDatabase extends UNBase {
	
	public static final byte[] HEADER = "UNDB".getBytes();
	public static final short VERSION = 0x0001;
	public static final byte CONTAINER_TYPE = ContainerType.DATABASE;
	private short objectCount;
	public List<UNObject> objects = new ArrayList<UNObject>();
	
	public UNDatabase() {
	}
	
	public UNDatabase(String name) {
		setName(name);
		size += HEADER.length + 2 + 1 + 2;
	}
	
	public void addObject(UNObject object) {
		objects.add(object);
		size += object.getSize();
		
		objectCount = (short)objects.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, HEADER);
		pointer = writeBytes(dest, pointer, VERSION);
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, objectCount);
		for (UNObject object : objects) 
			pointer = object.getBytes(dest, pointer);
		
		return pointer;
	}
	
	public static UNDatabase deserialize(byte[] data) {
		int pointer = 0;
		
		String header = readString(data, pointer, HEADER.length);
		assert(header.equals(new String(HEADER)));
		pointer += HEADER.length;		
		
		if (readShort(data, pointer) != VERSION) {
			System.err.println("Invalid UNDB version!");
			return null;
		}
		pointer += Type.SHORT;
		
		byte containerType = readByte(data, pointer);
		assert(containerType == CONTAINER_TYPE);
		pointer += Type.BYTE;
		
		UNDatabase result = new UNDatabase();
		result.nameLength = readShort(data, pointer);
		pointer += Type.SHORT;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += Type.INTEGER;
		
		result.objectCount = readShort(data, pointer);
		pointer += Type.SHORT;
		
		for(int i = 0; i < result.objectCount; i++) {
			UNObject object = UNObject.Deserialize(data, pointer);
			result.objects.add(object);
			pointer += object.getSize();
		}
		
		return result;
	}
	
	public UNObject findObject(String name) {
		for (UNObject object : objects) {
			if (object.getName().equals(name))
				return object;
		}
		return null;
	}
	
	public static UNDatabase deserializeFromFile(String path) {
		if (path.contains("/")) {
			path.replaceAll("/", Matcher.quoteReplacement("\\"));
		}
		File file = new File(path);
		byte[] buffer = null;
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
			buffer = new byte[in.available()];
			in.read(buffer);
			in.close();
			return deserialize(buffer);
		} catch (IOException e) {
			return null;
		}
	}
	
	public void serializeToFile(String path) {
		if (path.contains("/"))
			path.replaceAll("/", Matcher.quoteReplacement("\\"));
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
				if (file.isFile())
					System.out.println("File [" + path + "] created.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		byte[] data = new byte[getSize()];
		getBytes(data, 0);
		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(data);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
