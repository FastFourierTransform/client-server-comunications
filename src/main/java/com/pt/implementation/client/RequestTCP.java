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

import com.pt.implementation.server.ThreadRequestHandlerTCP;
import com.pt.interfaces.client.IResponseCallback;
import com.pt.interfaces.client.Message;
import com.pt.interfaces.client.Request;
import com.pt.utils.Utils;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.Socket;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class RequestTCP extends Request{

    @Override
    public InputStream sendRequest(String host, int port, Message message) throws Exception {
        Socket channel = new Socket(host, port);
        
        int messageID = messageIDgenerator.getAndIncrement();
        
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();
        
        out.write(Utils.intToBytes(messageID));
        message.send(out);
        out.flush();//send
        int incommingMsgID = new DataInputStream(in).readInt();
        
        if (messageID!=incommingMsgID)
            throw new ProtocolException("Message ID's dont match");
        
        return in;
    }

    @Override
    public void sendRequestWaitAssync(String host, int port, Message message, IResponseCallback callback) throws Exception {
        Socket channel = new Socket(host, port);
        
        int messageID = messageIDgenerator.getAndIncrement();
        
        OutputStream out = channel.getOutputStream();
        
        out.write(Utils.intToBytes(messageID));
        message.send(out);
        out.flush();//send
        
        //Other thread should wait for the response
        poolThread.execute(new ThreadResponseCallbackTCP(callback, channel.getInputStream(), messageID));
    }

    @Override
    public void sendRequestAssync(String host, int port, Message message, IResponseCallback callback) throws Exception {
        Socket channel = new Socket(host, port);
        poolThread.execute(new ThreadHandlerResquestTCP(callback, channel.getInputStream(), channel.getOutputStream(), message, messageIDgenerator));
    }

    @Override
    public void sendRequestWithoutResponse(String host, int port, Message message) throws Exception {
        Socket channel = new Socket(host, port);
        
        int messageID = messageIDgenerator.getAndIncrement();
        
        OutputStream out = channel.getOutputStream();
        
        out.write(Utils.intToBytes(messageID));
        message.send(out);
        out.flush();//send
    }
    
}
