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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class UtilsTest {
    
    public UtilsTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testIntToBytes() {
        //arrange
        int integer = 654132;
        
        //act
        byte[] result = Utils.intToBytes(integer);
        
        //slower version 
        byte[] expected = ByteBuffer.allocate(4).putInt(integer).array();
        
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBytesToInt() {
        //arrange
        int expected = 654132;
        //slower version 
        byte[] correct = ByteBuffer.allocate(4).putInt(expected).array();
        
        //act
        int result = Utils.bytesToInt(correct);
        
        //assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConcatenateByteArrays() {
    }

    @Test
    public void testReadUntilMaxSize() throws Exception {
    }
    
    @Test
    public void testInputStreamToByteArray() throws IOException{
        byte[] expected = "Tiago".getBytes();
        ByteArrayInputStream bis = new ByteArrayInputStream(expected);
        
        byte[] result = Utils.InputStreamToByteArray(bis);
        
        Assert.assertArrayEquals(expected, result);
    }
    
}
