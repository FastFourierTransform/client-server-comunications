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

/**
 *
 * @author Tiago Alexandre Melo Almeida
 * 
 * Represent a connection with the server
 * Features:
 *  send message and wait for response sync
 *  send message and wait for response assync, i.e receive in Receiver thread
 *  send message assync and receive assync, i.e send message with help of thread pool, and receive in Receiver Thread 
 */
public interface IConnection {
    
    
    /**
     * Send Message without using another thread
     * Receiver thread should wait for the response
     * 
     * @param outStream outputStream here the user should write the message to send
     * @param callback
     * @throws java.lang.Exception 
     */
    void sendRequestWaitAssync(Message message,IResponseCallback callback) throws Exception;
    
    /**
     * Send Message asynchronous
     * Receive Message asynchronous
     * New thread should send and the receiver thread should wait for the response
     * 
     * @param outStream outputStream here the user should write the message to send
     * @param callback - implementation of callback function that handler the receive message
     * @throws java.lang.Exception 
     */
    void sendRequestAssync(Message message,IResponseCallback callback) throws Exception;

    /**
     * Send messege and dont wait for response
     * Note dont exist any kinda of feedback
     * 
     * @param outStream outputStream here the user should write the message to send
     * @throws java.lang.Exception 
     */
    void sendRequestWithoutResponse(Message message) throws Exception;
    //TODO sendRequestWithoutResponse Assync
    
    /**
     * End this connection
     */
    void close();
}
