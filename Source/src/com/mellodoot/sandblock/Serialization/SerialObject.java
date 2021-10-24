package com.mellodoot.sandblock.Serialization;

import static com.mellodoot.sandblock.Serialization.SerializationUtils.*;

import java.util.ArrayList;
import java.util.List;

public class SerialObject extends SerialBase {
	
	public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
	private short fieldCount;
	private short stringCount;
	private short arrayCount;
	public List<SerialField>  fields =  new ArrayList<SerialField>();
	public List<SerialString> strings = new ArrayList<SerialString>();
	public List<SerialArray>  arrays =  new ArrayList<SerialArray>();
	
	private SerialObject() {
	}
	
	public SerialObject(String name) {
		size += 1 + 2 + 2 + 2;
		setName(name);		
	}
	
	public void addField(SerialField field) {
		fields.add(field);
		size += field.getSize();
		
		fieldCount = (short)fields.size();
	}
	
	public void addString(SerialString string) {
		strings.add(string);
		size += string.getSize();
		
		stringCount = (short)strings.size();
	}
	
	public void addArray(SerialArray array) {
		arrays.add(array);
		size += array.getSize();
		
		arrayCount = (short)arrays.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public SerialField findField(String name) {
		for (SerialField field : fields) {
			if (field.getName().equals(name))
				return field;
		}
		return null;
	}
	
	public SerialString findString(String name) {
		for (SerialString string : strings) {
			if (string.getName().equals(name))
				return string;
		}
		return null;
	}
	
	public SerialArray findArray(String name) {
		for (SerialArray array : arrays) {
			if (array.getName().equals(name))
				return array;
		}
		return null;
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, fieldCount);
		for (SerialField field : fields) 
			pointer = field.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, stringCount);
		for (SerialString string : strings) 
			pointer = string.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, arrayCount);
		for (SerialArray array : arrays) 
			pointer = array.getBytes(dest, pointer);
		
		return pointer;
	}
	
	public static SerialObject Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		SerialObject result = new SerialObject();
		result.nameLength = readShort(data, pointer);
		pointer += Type.SHORT;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += Type.INTEGER;
		
		result.fieldCount = readShort(data, pointer);
		pointer += Type.SHORT;
		for (int i = 0; i < result.fieldCount; i++) {
			SerialField field = SerialField.Deserialize(data, pointer);
			result.fields.add(field);
			pointer += field.getSize();
		}
		
		result.stringCount = readShort(data, pointer);
		pointer += Type.SHORT;
		for (int i = 0; i < result.stringCount; i++) {
			SerialString string = SerialString.Deserialize(data, pointer);
			result.strings.add(string);
			pointer += string.getSize();
		}
		
		result.arrayCount = readShort(data, pointer);
		pointer += Type.SHORT;
		for (int i = 0; i < result.arrayCount; i++) {
			SerialArray array = SerialArray.Deserialize(data, pointer);
			result.arrays.add(array);
			pointer += array.getSize();
		}
		
		return result;
	}
	
}
