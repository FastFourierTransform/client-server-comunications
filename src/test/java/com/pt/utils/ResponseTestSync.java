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

import com.pt.implementation.client.HandlerSyncResponse;
import com.pt.interfaces.client.IResponseCallback;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ResponseTestSync implements IResponseCallback{

    public String message;
    private final Semaphore sem;
    private final int size;
    
    public ResponseTestSync(Semaphore sem,int sizeExpectedResponse){
        this.sem = sem;
        this.size = sizeExpectedResponse;
    }

    @Override
    public void handlerResponse(InputStream inStream) {
        try {
            //message receive
            byte[] array = Utils.readUntilMaxSize(new DataInputStream(inStream), size);
                    
            this.message = new String(array);
            System.out.println("Receive from server " + this.message);
            sem.release();
        } catch (IOException ex) {
            Logger.getLogger(HandlerSyncResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
