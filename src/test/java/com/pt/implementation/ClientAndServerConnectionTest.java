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
package com.pt.implementation;

import com.pt.exceptions.ClientAlreadyUsePort;
import com.pt.implementation.client.Client;
import com.pt.implementation.infrastruct.ThreadServer;
import com.pt.interfaces.client.IConnection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ClientAndServerConnectionTest {
    
    private Client client;
    private ThreadServer server;
    
    @Before
    public void setUp(){
        client = new Client();
        
        // this server handler replay the request this the prefix "ola "
        server = new ThreadServer(5647,(message) -> {
            String receiveMessage = new String(message);
            System.out.println("Server receive " + receiveMessage);
            
            return ("Ola " + receiveMessage).getBytes(); //To change body of generated lambdas, choose Tools | Templates.
        });
        server.start();
        
        
    }
    
    @After
    public void tearDown(){
        client.shutdown();
        server.shutdown();
    }
    
    @Test
    public void ClientSendRequestSync() throws ClientAlreadyUsePort, Exception{
        //arrange,
        IConnection connection = client.startConnection("localhost", 5647, (message) -> {});//default handler will not be used in this test
        byte[] expected = "Ola Tiago".getBytes();
        //act
        byte[] response = connection.sendRequest("Tiago".getBytes());//message "Tiago"
        //assert
        Assert.assertArrayEquals(expected, response);
    }
   
}
