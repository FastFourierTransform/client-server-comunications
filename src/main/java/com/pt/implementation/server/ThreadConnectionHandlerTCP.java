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

import com.pt.interfaces.server.ThreadConnectionHandler;
import com.pt.interfaces.server.IHandler;
import com.pt.utils.Utils;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ThreadConnectionHandlerTCP extends ThreadConnectionHandler{
 
    
    //represente a channel of communication with the client
    private final Socket cSocket;
    private OutputStream cOut;
    private InputStream cIn;
    
    public ThreadConnectionHandlerTCP(IHandler mHandler,Socket cSocket)
    {
        super(mHandler);
        this.cSocket = cSocket;
    }

    @Override
    public void run() {
        
        try {
            cOut = cSocket.getOutputStream();
            cIn = cSocket.getInputStream();
            while (!cSocket.isClosed()){
                DataInputStream input = new DataInputStream(cIn);

                int messageId = input.readInt();
                System.out.println("Server read int " + messageId);
                //keep message protocol 
                cOut.write(Utils.intToBytes(messageId));
                
                messageHandler.handleMessage(cIn, cOut);
                
                cOut.flush();
                System.out.println("Server send response");
                
                
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadConnectionHandlerTCP.class.getName()).log(Level.WARNING, "Error receiving or sending from socket\nProbably client end connection", ex);
        } catch (Exception ex) {
            Logger.getLogger(ThreadConnectionHandlerTCP.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
