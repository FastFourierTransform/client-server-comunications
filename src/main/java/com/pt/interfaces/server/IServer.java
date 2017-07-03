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
package com.pt.interfaces.server;

import com.pt.exceptions.ServerAlreadyUsePort;
import java.io.IOException;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public interface IServer {
    
    /**
     * Launch server-thread to listning for client connection
     * This method should be used to handle the connection and session with the clients 
     * 
     * @param port
     * @param messageHandler 
     * @throws com.pt.exceptions.ServerAlreadyUsePort 
     */
    void startListningConnections(int port,IHandler messageHandler) throws ServerAlreadyUsePort;
    
    /**
     * If the server as some thread listning requests in the port. Stop it
     * 
     * @param port 
     */
    void stopListningConnections(int port);
    
    /**
     * Launch server-thread to listning for client simples requests
     * 
     * @param port
     * @param messageHandler
     * @throws com.pt.exceptions.ServerAlreadyUsePort 
     */
    void startListningRequests(int port,IHandler messageHandler) throws ServerAlreadyUsePort;
    
    /**
     * If the server as some thread listning requests in the port. Stop it
     * 
     * @param port 
     */
    void stopListningRequests(int port);
    
    /**
     * Stop all ThreadConnectionServer that are running
     */
    void stopAllConnectionsListning();
    
    /**
     * Stop all ThreadRequestServer that are running
     */
    void stopAllRequestListning();
    
    /**
     * Stop all Thread that are running
     */
    void shutdown();
}
