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

import com.pt.interfaces.client.Connection;

import com.pt.interfaces.client.IResponseCallback;
import com.pt.utils.Utils;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ConnectionTCP extends Connection {

    private Socket sClient;

    public ConnectionTCP(String hostName, int port, IResponseCallback defaultHandler) {
        super(hostName, port, new ConnectionReceiverTCP(defaultHandler));
        
    }

    @Override
    public void connect() {
        try {
            sClient = new Socket(hostName, port);
            //Not best way, TODO fix
            ((ConnectionReceiverTCP) receiverThread).setSocket(sClient);
            receiverThread.start();
        } catch (IOException ex) {
            //handler error
            Logger.getLogger(ConnectionTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte[] sendRequest(byte[] message) throws IOException, InterruptedException{

        int messageID = messageIDgenerator.getAndIncrement();
        byte[] idAndSize = Utils.concatenateByteArrays(Utils.intToBytes(messageID),Utils.intToBytes(message.length));
        sClient.getOutputStream().write(Utils.concatenateByteArrays(idAndSize, message));

        //wait sync response
        Semaphore wait = new Semaphore(0);//initialy 0 permits
        HandlerSyncResponse response = new HandlerSyncResponse(wait);
        receiverThread.addCustomHanlder(messageID, response);
        wait.acquire();//block until receive the answer
        return response.getReceiveMessage();

    }

    @Override
    public byte[] sendRequestAndWaitAssync(byte[] message, IResponseCallback callback) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] sendRequestAssync(byte[] message, IResponseCallback callback) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendRequestWithoutResponse(byte[] message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        receiverThread.interrupt();
        try {
            sClient.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
