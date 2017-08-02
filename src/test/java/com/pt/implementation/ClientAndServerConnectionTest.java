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
import com.pt.implementation.infrastruct.ThreadServerConnection;
import com.pt.interfaces.client.IConnection;
import com.pt.utils.ResponseTestSync;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */

public class ClientAndServerConnectionTest {
    
    private Client client;
    private static ThreadServerConnection server;
    
    @BeforeClass
    public static void start(){
        // this server handler replay the request this the prefix "ola "
        server = new ThreadServerConnection(5647,(inMessage, outMessage) -> {
            byte[] buffer = new byte[30];//Will work for test purpose, but user should implement his protocol
            
            int read= inMessage.read(buffer);
            
            byte[] r = new byte[read];
            System.arraycopy(buffer, 0, r, 0, read);
            
            String receiveMessage = new String(r);
            System.out.println("Server receive " + receiveMessage + " size " + read);
            String send = "Ola " + receiveMessage;
            outMessage.write(send.getBytes());
            System.out.println("Response " + send);
            
        });
        server.start();
    }
    
    @AfterClass
    public static void finish(){
        server.shutdown();
    }
    
    
    @Before
    public void setUp(){
        client = new Client();
        
        
        
    }
    
    @After
    public void tearDown(){
        client.shutdown();
    }
    
    
    @Test
    public void ClientSendSingleRequestWaitAssync() throws ClientAlreadyUsePort, Exception{
        //arrange,
        IConnection connection = client.startConnection("localhost", 5647, (message) -> {});//default handler will not be used in this test
        String expected = "Ola Tiago";
        
        Semaphore sem = new Semaphore(0);
        ResponseTestSync handler = new ResponseTestSync(sem,5);
        //act
        connection.sendRequestWaitAssync((outStream) -> {outStream.write("Tiago".getBytes());},
                handler);//message "Tiago"
  
        sem.acquire();//block waiting for the response to be able to assert the response 
        
        //assert
        Assert.assertEquals(expected,handler.message);
    }
    
    @Test
    public void ClientSendMultiRequestWaitAssync() throws ClientAlreadyUsePort, Exception{
        //arrange,
        IConnection connection = client.startConnection("localhost", 5647, (message) -> {});//default handler will not be used in this test
        
        
        String expected = "Ola Tiago";
    
        for (int i=0;i<=9;i++){
            Semaphore sem = new Semaphore(0);
            ResponseTestSync handler = new ResponseTestSync(sem, 7);
            String message = "Tiago "+i;
            //act
            connection.sendRequestWaitAssync((outStream) -> {outStream.write(message.getBytes());},
                    handler);//message "Tiago"
            
            sem.acquire();//block waiting for all the response to be able to assert the response 
            Assert.assertEquals(expected+" "+i,handler.message);
        }
        
    }
    
    @Test
    public void ClientSendRequestAssync() throws ClientAlreadyUsePort, Exception{
        //arrange,
        IConnection connection = client.startConnection("localhost", 5647, (message) -> {});//default handler will not be used in this test
        String expected = "Ola Tiago";
        
        Semaphore sem = new Semaphore(0);
        ResponseTestSync handler = new ResponseTestSync(sem,5);
        //act
        connection.sendRequestAssync((outStream) -> {outStream.write("Tiago".getBytes());},
                handler);//message "Tiago"
  
        sem.acquire();//block waiting for the response to be able to assert the response 
        
        //assert
        Assert.assertEquals(expected,handler.message);
    }

    @Test
    public void ClientSendMultiRequestAssync() throws ClientAlreadyUsePort, Exception{
        //arrange,
        IConnection connection = client.startConnection("localhost", 5647, (message) -> {});//default handler will not be used in this test
        
        
        String expected = "Ola Tiago";
    
        for (int i=0;i<=9;i++){
            Semaphore sem = new Semaphore(0);
            ResponseTestSync handler = new ResponseTestSync(sem, 7);
            String message = "Tiago "+i;
            //act
            connection.sendRequestAssync((outStream) -> {outStream.write(message.getBytes());},
                    handler);//message "Tiago"
            
            sem.acquire();//block waiting for all the response to be able to assert the response 
            Assert.assertEquals(expected+" "+i,handler.message);
        }
        
    }
}
