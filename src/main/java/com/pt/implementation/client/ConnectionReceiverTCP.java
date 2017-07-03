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
package com.pt.implementation.client;

import com.pt.interfaces.client.ConnectionReceiver;
import com.pt.interfaces.client.IResponseCallback;
import com.pt.utils.Pointer;
import com.pt.utils.Utils;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ConnectionReceiverTCP extends ConnectionReceiver {
    
    private Socket inSocket;
    
    public ConnectionReceiverTCP(IResponseCallback handler) {
        super(handler);
    }

    public void setSocket(Socket socket){
        this.inSocket = socket;
    }
    
    @Override
    public boolean condition() {
        return !inSocket.isClosed();
    }

    @Override
    public int waitResponse(Pointer<byte[]> pointer) {
        try {
            DataInputStream input = new DataInputStream(inSocket.getInputStream());
            int messageId = input.readInt();
            int messageSize = input.readInt();
            pointer.value = Utils.readUntilMaxSize(input,messageSize);
            return messageId;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionReceiverTCP.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return -1;
    }

    
}
