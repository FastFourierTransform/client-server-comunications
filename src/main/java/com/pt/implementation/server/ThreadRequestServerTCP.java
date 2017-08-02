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
package com.pt.implementation.server;

import com.pt.interfaces.server.IHandler;
import com.pt.interfaces.server.ThreadRequestServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ThreadRequestServerTCP extends ThreadRequestServer{
    
    public static final String TCP_SERVER = "TCP__REQUEST__SERVER";
    
    private ServerSocket serverSocket;
    
    public ThreadRequestServerTCP(int port,IHandler messageHandler,ExecutorService pool) {
        super(port,messageHandler,pool,"RequestServerTCP on port "+port);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(TCP_SERVER).log(Level.SEVERE, "Port already in use by other program, can´t launch this thread", ex);
            return;
        }
        
        while (true)
        {
            try {
                Socket tempConnection = serverSocket.accept();

                pool.execute(new ThreadRequestHandlerTCP(messageHandler,tempConnection));
                
            } catch (IOException ex) {
                Logger.getLogger(ThreadConnectionServerTCP.class.getName()).log(Level.SEVERE, "Unable to accept connection error ", ex);
            }
        }
    }
    
}
