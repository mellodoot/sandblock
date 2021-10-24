package com.mellodoot.sandblock.Serialization;

import static com.mellodoot.sandblock.Serialization.SerializationUtils.*;

public class SerialField extends SerialBase {
	
	public static final byte CONTAINER_TYPE = ContainerType.FIELD;
	public byte type;
	public byte[] data;
	
	public SerialField() {
	}
	
	public byte getByte() {
		return readByte(data, 0);
	}
	
	public short getShort() {
		return readShort(data, 0);
	}
	
	public char getChar() {
		return readChar(data, 0);
	}
	
	public int getInt() {
		return readInt(data, 0);
	}
	
	public long getLong() {
		return readLong(data, 0);
	}
	
	public double getDouble() {
		return readDouble(data, 0);
	}
	
	public float getFloat() {
		return readFloat(data, 0);
	}
	
	public boolean getBoolean() {
		return readBoolean(data, 0);
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, data);
		return pointer;
	}
	
	public int getSize() {
		assert(data.length == Type.getSize(type));
		return 1 + 2 + name.length + 1 + data.length;
	}
	
	public static SerialField Byte(String name, byte value) {
		SerialField field = new SerialField();
		field.setName(name);
		field.type = Type.BYTE;
		field.data = new byte[Type.getSize(Type.BYTE)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static SerialField Integer(String name, int value) {
		SerialField field = new SerialField();
		field.setName(name);
		field.type = Type.INTEGER;
		field.data = new byte[Type.getSize(Type.INTEGER)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static SerialField Short(String name, short value) {
		SerialField field = new SerialField();
		field.setName(name);
		field.type = Type.SHORT;
		field.data = new byte[Type.getSize(Type.SHORT)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static SerialField Char(String name, char value) {
		SerialField field = new SerialField();
		field.setName(name);
		field.type = Type.CHAR;
		field.data = new byte[Type.getSize(Type.CHAR)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static SerialField Long(String name, long value) {
		SerialField field = new SerialField();
		field.setName(name);
		field.type = Type.LONG;
		field.data = new byte[Type.getSize(Type.LONG)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static SerialField Float(String name, float value) {
		SerialField field = new SerialField();
		field.setName(name);
		field.type = Type.FLOAT;
		field.data = new byte[Type.getSize(Type.FLOAT)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static SerialField Double(String name, double value) {
		SerialField field = new SerialField();
		field.setName(name);
		field.type = Type.DOUBLE;
		field.data = new byte[Type.getSize(Type.DOUBLE)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static SerialField Boolean(String name, boolean value) {
		SerialField field = new SerialField();
		field.setName(name);
		field.type = Type.BOOLEAN;
		field.data = new byte[Type.getSize(Type.BOOLEAN)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static SerialField Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		SerialField result = new SerialField();
		result.nameLength = readShort(data, pointer);
		pointer += Type.SHORT;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.type = data[pointer++];
		
		result.data = new byte[Type.getSize(result.type)];
		readBytes(data, pointer, result.data);
		pointer += Type.getSize(result.type);
		
		return result;
	}
	
	public String toString() {
		switch (type) {
			case Type.BYTE:    return "" + getByte();
			case Type.SHORT:   return "" + getShort();
			case Type.CHAR:    return "" + getChar();
			case Type.INTEGER: return "" + getInt();
			case Type.LONG:    return "" + getLong();
			case Type.FLOAT:   return "" + getFloat();
			case Type.DOUBLE:  return "" + getDouble();
			case Type.BOOLEAN: return "" + getBoolean();
		}
		return "";
	}
	
}
