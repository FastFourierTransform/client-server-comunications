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
import com.pt.interfaces.client.Message;
import com.pt.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ConnectionTCP extends Connection {

    private Socket sClient;
    private OutputStream sOut;
    
    
    public ConnectionTCP(String hostName, int port, IResponseCallback defaultHandler,ExecutorService pool) {
        super(hostName, port, new ConnectionReceiverTCP(defaultHandler),pool);
    }

    @Override
    public void connect() {
        try {
            sClient = new Socket(hostName, port);
            //Not best way, TODO fix
            ((ConnectionReceiverTCP) receiverThread).setSocket(sClient);
            receiverThread.start();
            sOut = sClient.getOutputStream();
        } catch (IOException ex) {
            //handler error
            Logger.getLogger(ConnectionTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendRequestWaitAssync(Message message, IResponseCallback callback) throws IOException, InterruptedException {
        
        int messageID = messageIDgenerator.getAndIncrement();
    
        //prepare handler
        receiverThread.addCustomHanlder(messageID, callback);
        
        //send request
        sendMessage(messageID, message);
    }

    @Override
    public void sendRequestAssync(Message message, IResponseCallback callback) {
 
        int messageID = messageIDgenerator.getAndIncrement();
    
        //prepare handler
        receiverThread.addCustomHanlder(messageID, callback);
        
        //send request in other thread
        requestThreadPool.execute(() -> {
            try {
                System.out.println("client send message");
                sendMessage(messageID, message);
                System.out.println("client end send message");
            } catch (IOException ex) {
                Logger.getLogger(ConnectionTCP.class.getName()).log(Level.SEVERE, "Error in client thread send assync", ex);
            }
        });
    }

    @Override
    public void close() {
        receiverThread.interrupt();
        try {
            sClient.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionTCP.class.getName()).log(Level.SEVERE, "Socket alredy close", ex);
        }
    }

    private void sendMessage(int messageID, Message message) throws IOException {
        
        try {
            System.out.println("send id " + messageID);
            sOut.write(Utils.intToBytes(messageID));
            //write user message to outputStream
            message.send(sOut);

            sOut.flush();
        } catch (Exception ex) {
            Logger.getLogger(ConnectionTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendMessage(int messageID, int messageSize ,InputStream inStream) throws IOException {
        
        byte[] idAndSize = Utils.concatenateByteArrays(Utils.intToBytes(messageID),Utils.intToBytes(messageSize));
        OutputStream outStream = sClient.getOutputStream();
        outStream.write(idAndSize);
        int count;
        int totalBytes=0;
        byte[] buffer = new byte[1024*1024*10]; // 10MB
        while ((count = inStream.read(buffer)) > 0){
            totalBytes+=count;
            outStream.write(buffer, 0, count);
        }
        System.out.println("COPY ALL???? " + totalBytes + " - " + messageSize);
        outStream.flush();
    }

    @Override
    public void sendRequestWithoutResponse(Message message) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
