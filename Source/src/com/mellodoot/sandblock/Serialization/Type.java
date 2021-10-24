package com.mellodoot.sandblock.Serialization;

public class Type {
	
	public static final byte UNKNOWN = 0;
	public static final byte BYTE    = 1;
	public static final byte SHORT   = 2;
	public static final byte CHAR    = 3;
	public static final byte INTEGER = 4;
	public static final byte LONG    = 5;
	public static final byte FLOAT   = 6;
	public static final byte DOUBLE  = 7;
	public static final byte BOOLEAN = 8;
	
	public static int getSize(byte type) {
		switch (type) {
			case UNKNOWN: assert(false);
			case BYTE:    return 1;
			case SHORT:   return 2;
			case CHAR:    return 2;
			case INTEGER: return 4;
			case LONG:    return 8;
			case FLOAT:   return 4;
			case DOUBLE:  return 8;
			case BOOLEAN: return 1;
		}
		assert(false);
		return 0;
	}
	
	public static String getType(byte type) {
		switch (type) {
			case BYTE:    return "BYTE";
			case SHORT:   return "SHORT";
			case CHAR:    return "CHAR";
			case INTEGER: return "INTEGER";
			case LONG:    return "LONG";
			case FLOAT:   return "FLOAT";
			case DOUBLE:  return "DOUBLE";
			case BOOLEAN: return "BOOLEAN";
		}
		return "UNKNOWN";
	}
	
}
