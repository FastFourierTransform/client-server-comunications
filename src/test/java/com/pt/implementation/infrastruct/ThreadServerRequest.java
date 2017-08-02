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
package com.pt.implementation.infrastruct;

import com.pt.exceptions.ServerAlreadyUsePort;
import com.pt.implementation.server.Server;
import com.pt.interfaces.server.IHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ThreadServerRequest extends Thread{
    private final IHandler serverHandler;
    private final Server server;
    private final int port;
    
    public ThreadServerRequest( int port,IHandler sHandler){
        this.serverHandler = sHandler;
        this.server = new Server(1,50);
        this.port = port;
    }
    
    @Override
    public void run() {
        
        try {
            server.startListningRequests(port, serverHandler);
            System.out.println("Server strat");
        } catch (ServerAlreadyUsePort ex) {
            Logger.getLogger(ThreadServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void shutdown(){
        server.shutdown();
        this.interrupt();
    }
}
