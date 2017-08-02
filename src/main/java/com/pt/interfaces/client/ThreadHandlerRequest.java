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
package com.pt.interfaces.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This thread is used to send message and wait for the response
 * 
 * @author Tiago Alexandre Melo Almeida
 */
public abstract class ThreadHandlerRequest implements Runnable{
    
    protected AtomicInteger messageIDgenerator ;
    
    protected IResponseCallback callback;
    
    //inputStream that contain the incoming message
    protected InputStream in;
    
    //outputStream for sending the message
    protected OutputStream out;
    
    protected Message message;
    
    public ThreadHandlerRequest(IResponseCallback callback,InputStream in, OutputStream out, Message message, AtomicInteger messageIDgenerator){
        this.callback = callback;
        this.in = in;
        this.out = out;
        this.message = message;
        this.messageIDgenerator = messageIDgenerator;
    }

}
