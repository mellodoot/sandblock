package net.heyitsultra.UltraNet.serialization;

import static net.heyitsultra.UltraNet.serialization.SerializationUtils.*;

import java.util.ArrayList;
import java.util.List;

public class UNObject extends UNBase {
	
	public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
	private short fieldCount;
	private short stringCount;
	private short arrayCount;
	public List<UNField>  fields =  new ArrayList<UNField>();
	public List<UNString> strings = new ArrayList<UNString>();
	public List<UNArray>  arrays =  new ArrayList<UNArray>();
	
	private UNObject() {
	}
	
	public UNObject(String name) {
		size += 1 + 2 + 2 + 2;
		setName(name);		
	}
	
	public void addField(UNField field) {
		fields.add(field);
		size += field.getSize();
		
		fieldCount = (short)fields.size();
	}
	
	public void addString(UNString string) {
		strings.add(string);
		size += string.getSize();
		
		stringCount = (short)strings.size();
	}
	
	public void addArray(UNArray array) {
		arrays.add(array);
		size += array.getSize();
		
		arrayCount = (short)arrays.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public UNField findField(String name) {
		for (UNField field : fields) {
			if (field.getName().equals(name))
				return field;
		}
		return null;
	}
	
	public UNString findString(String name) {
		for (UNString string : strings) {
			if (string.getName().equals(name))
				return string;
		}
		return null;
	}
	
	public UNArray findArray(String name) {
		for (UNArray array : arrays) {
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
		for (UNField field : fields) 
			pointer = field.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, stringCount);
		for (UNString string : strings) 
			pointer = string.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, arrayCount);
		for (UNArray array : arrays) 
			pointer = array.getBytes(dest, pointer);
		
		return pointer;
	}
	
	public static UNObject Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		UNObject result = new UNObject();
		result.nameLength = readShort(data, pointer);
		pointer += Type.SHORT;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += Type.INTEGER;
		
		result.fieldCount = readShort(data, pointer);
		pointer += Type.SHORT;
		for (int i = 0; i < result.fieldCount; i++) {
			UNField field = UNField.Deserialize(data, pointer);
			result.fields.add(field);
			pointer += field.getSize();
		}
		
		result.stringCount = readShort(data, pointer);
		pointer += Type.SHORT;
		for (int i = 0; i < result.stringCount; i++) {
			UNString string = UNString.Deserialize(data, pointer);
			result.strings.add(string);
			pointer += string.getSize();
		}
		
		result.arrayCount = readShort(data, pointer);
		pointer += Type.SHORT;
		for (int i = 0; i < result.arrayCount; i++) {
			UNArray array = UNArray.Deserialize(data, pointer);
			result.arrays.add(array);
			pointer += array.getSize();
		}
		
		return result;
	}
	
}
