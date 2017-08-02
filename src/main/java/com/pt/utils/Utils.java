/*
MIT License

Copyright (c) 2017 Tiago Almeida

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.pt.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO using java reflection to aggregate intToByte and longToByte and otherwise
 * 
 * @author Tiago Alexandre Melo Almeida
 */
public class Utils {

    public static byte[] intToBytes(int integer) {
        byte[] result = new byte[Integer.BYTES];
        for (int i = Integer.BYTES - 1; i >= 0; i--) {
            result[i] = (byte) (integer & 0xFF);
            integer >>= Byte.SIZE;
        }
        return result;
    }

    public static int bytesToInt(byte[] b) {
        int result = 0;
        for (int i = 0; i < Integer.BYTES; i++) {
            result <<= Byte.SIZE;
            result |= (b[i] & 0xFF);
        }
        return result;
    }
    
    public static byte[] longToBytes(long integer) {
        byte[] result = new byte[Long.BYTES];
        for (int i = Long.BYTES - 1; i >= 0; i--) {
            result[i] = (byte) (integer & 0xFF);
            integer >>= Byte.SIZE;
        }
        return result;
    }

    public static int bytesToLong(byte[] b) {
        int result = 0;
        for (int i = 0; i < Long.BYTES; i++) {
            result <<= Byte.SIZE;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    //more effecient?
    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    
    public static byte[] concatenateByteArrays(byte[] a, byte type) {
        byte[] b = new byte[1];
        b[0]=type;
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    
    public static byte[] concatenateIdTypeAndSize(int id, byte type,int size) {
        byte[] result = concatenateByteArrays(intToBytes(id), type);
        return concatenateByteArrays(result, intToBytes(size));
    }
    
    public static byte[] concatenateIdTypeAndSize(int id, byte type,long size) {
        byte[] result = concatenateByteArrays(intToBytes(id), type);
        return concatenateByteArrays(result, longToBytes(size));
    }

    public static byte[] readUntilMaxSize(DataInputStream input,int bytesToRead) throws IOException {
        
        byte[] buffer = new byte[8192];
        
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        while (output.size() < bytesToRead) {
            bytesRead = input.read(buffer);
            output.write(buffer, 0, bytesRead);
        }
        
        output.flush();
        return output.toByteArray();
    }
    
    public static byte[] InputStreamToByteArray(InputStream inStream) throws IOException{
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
    }
}
