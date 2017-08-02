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

import com.pt.exceptions.ClientAlreadyUsePort;
import com.pt.exceptions.ServerAlreadyUsePort;
import com.pt.implementation.server.ThreadConnectionServerTCP;
import com.pt.implementation.server.ThreadNamedFactory;
import com.pt.interfaces.client.Connection;
import com.pt.interfaces.client.IClient;
import com.pt.interfaces.client.IConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.pt.interfaces.client.IResponseCallback;
import com.pt.interfaces.client.Message;
import com.pt.interfaces.client.Request;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class Client implements IClient{

    private final Map<Integer,Connection> openConnections;
    private final ExecutorService requestThreadPool;
    private final Request request;
    
    public Client(Request request){
        this.request = request;
        openConnections = new HashMap<>();
        requestThreadPool = Executors.newCachedThreadPool(new ThreadNamedFactory("ThreadClientAssyncSender - %d"));
        this.request.setPoolThread(requestThreadPool);
    }
    
    public Client(){
        openConnections = new HashMap<>();
        requestThreadPool = Executors.newCachedThreadPool(new ThreadNamedFactory("ThreadClientAssyncSender - %d"));
        this.request = new RequestTCP();//TODO should be UDP
        this.request.setPoolThread(requestThreadPool);
    }
    
    

    @Override
    public IConnection startConnection(String host, int port, IResponseCallback defaultHandler) throws ClientAlreadyUsePort{
        if (!openConnections.containsKey(port)){
            openConnections.put(port, new ConnectionTCP(host, port, defaultHandler,requestThreadPool));
            openConnections.get(port).connect();
            return openConnections.get(port);
        }
        //else
        throw new ClientAlreadyUsePort(port);
        
    }
    
    public void startConnection(Connection connectionImplementation) throws ClientAlreadyUsePort {
        
        if (!openConnections.containsKey(connectionImplementation.getPort())){
            openConnections.put(connectionImplementation.getPort(), connectionImplementation);
            openConnections.get(connectionImplementation.getPort()).connect();
        }
        //else
        throw new ClientAlreadyUsePort(connectionImplementation.getPort());
    }

    @Override
    public void closeConnection(int port) {
        if (openConnections.containsKey(port)){
            openConnections.get(port).close();
        }
    }

    @Override
    public void shutdown() {
        openConnections.forEach((k,v)->v.close());
    }

    @Override
    public InputStream sendRequest(String host, int port, Message message) throws Exception {
        return request.sendRequest(host, port, message);
    }

    @Override
    public void sendRequestWaitAssync(String host, int port, Message message, IResponseCallback callback) throws Exception {
        request.sendRequestWaitAssync(host, port, message, callback);
    }

    @Override
    public void sendRequestAssync(String host, int port, Message message, IResponseCallback callback) throws Exception {
        request.sendRequestAssync(host, port, message, callback);
    }

    @Override
    public void sendRequestWithoutResponse(String host, int port, Message message) throws Exception {
        request.sendRequestWithoutResponse(host, port, message);
    }
    
}
