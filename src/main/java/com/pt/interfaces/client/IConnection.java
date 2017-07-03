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

/**
 *
 * @author Tiago Alexandre Melo Almeida
 * 
 * Represent a connection with the server
 */
public interface IConnection {
    
    /**
     * Send Message and wait for response
     * No threads usage
     * 
     * @param message bytes of sending message
     * @return response
     * @throws java.lang.Exception
     */
    byte[] sendRequest(byte[] message) throws Exception;
    
    /**
     * Send Message without using another thread
     * Receiver thread should wait for the response
     * 
     * @param message
     * @param callback
     * @return 
     */
    byte[] sendRequestAndWaitAssync(byte[] message,IResponseCallback callback);
    
    /**
     * Send Message asynchronous
     * New thread should send and receiver thread should wait for the response
     * 
     * @param message
     * @param callback - implementation of callback function that handler the receive message
     * @return 
     */
    byte[] sendRequestAssync(byte[] message,IResponseCallback callback);
    
    /**
     * Send messege and dont wait for response
     * Note dont exist any kinda of feedback
     * 
     * @param message 
     */
    void sendRequestWithoutResponse(byte[] message);
    //TODO sendRequestWithoutResponse Assync
    
    /**
     * End this connection
     */
    void close();
}
