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

import com.pt.interfaces.server.IHandler;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ServerHandler implements IHandler {
    
    public String receiveMessage;
    
    private Semaphore sem;
    
    public ServerHandler(Semaphore sem){
        this.sem = sem;
    }
    
    @Override
    public void handleMessage(InputStream inMessage, OutputStream outMessage) throws Exception {
       
        byte[] buffer = new byte[30];//Will work for test purpose, but user should implement his protocol

        int read= inMessage.read(buffer);

        byte[] r = new byte[read];
        System.arraycopy(buffer, 0, r, 0, read);

        receiveMessage = new String(r);
        System.out.println("Server receive " + receiveMessage + " size " + read);
        String send = "Ola " + receiveMessage;
        outMessage.write(send.getBytes());

        sem.release();
    }
    
}
