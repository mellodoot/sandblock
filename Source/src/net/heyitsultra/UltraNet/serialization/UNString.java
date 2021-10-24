package net.heyitsultra.UltraNet.serialization;

import static net.heyitsultra.UltraNet.serialization.SerializationUtils.*;

public class UNString extends UNBase {
	
	public static final byte CONTAINER_TYPE = ContainerType.STRING;
	public int count;
	public char[] characters;
	
	public UNString() {
		size += 1 + 4;
	}

	public String getString() {
		return new String(characters);
	}
	
	private void updateSize() {
		size += getDataSize();
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		pointer = writeBytes(dest, pointer, count);
		pointer = writeBytes(dest, pointer, characters);
		return pointer;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getDataSize() {
		return characters.length * Type.getSize(Type.CHAR);
	}
	
	public static UNString Create(String name, String data) {
		UNString string = new UNString();
		string.setName(name);
		string.count = data.length();
		string.characters = data.toCharArray();
		string.updateSize();
		return string;
	}
	
	public static UNString Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		UNString result = new UNString();
		result.nameLength = readShort(data, pointer);
		pointer += Type.SHORT;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		result.count = readInt(data, pointer);
		pointer += 4;
		
		result.characters = new char[result.count];
		readChars(data, pointer, result.characters);
		
		pointer += result.count * Type.getSize(Type.CHAR);
		
		return result;
	}
	
}