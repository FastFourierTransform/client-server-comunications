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

import com.pt.exceptions.ClientAlreadyUsePort;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public interface IClient {
    
    /**
     * Send Message and wait for response
     * 
     * @param host
     * @param port
     * @param message bytes of sending message
     * @return response
     */
    byte[] sendRequest(String host,int port,byte[] message);
    
    /**
     * Send Message asynchronous
     * New thread should send and wait for the response
     * 
     * @param host
     * @param port
     * @param message
     * @param callback - implementation of callback function
     * @return 
     */
    byte[] sendRequestAssync(String host,int port,byte[] message,IResponseCallback callback);
    
    /**
     * Send messege and dont wait for response
     * Note dont exist any kinda of feedback
     * 
     * @param host
     * @param port
     * @param message 
     */
    void sendRequestWithoutResponse(String host,int port,byte[] message);
    
    /**
     * Start new thread that will receive messages send by the server,
     * All message will be handle with the defaultHandler if not override
     * 
     * @param host
     * @param port
     * @param defaultHandler
     * @return 
     * @throws com.pt.exceptions.ClientAlreadyUsePort 
     */
    IConnection startConnection(String host,int port,IResponseCallback defaultHandler) throws ClientAlreadyUsePort;
    
    /**
     * End the connection
     * 
     * @param port 
     */
    void closeConnection(int port);
    
    /**
     * close all connections
     * 
     */
    void shutdown();
}
