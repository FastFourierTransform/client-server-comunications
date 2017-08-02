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

import com.pt.implementation.client.Client;
import com.pt.implementation.infrastruct.ThreadServerRequest;
import com.pt.utils.ResponseTestSync;
import com.pt.utils.ServerHandler;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ClientAndServerRequestTest {
    private Client client;
    private static ThreadServerRequest server;
    
    @BeforeClass
    public static void start(){
        // this server handler replay the request this the prefix "ola "
        server = new ThreadServerRequest(7647,(inMessage, outMessage) -> {
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
    //@Ignore
    public void sendSingleMessageSync() throws Exception{
        //arrange
        String message = "Tiago Almeida";
        byte[] array = new byte[17];
        //act
        InputStream in = client.sendRequest("localhost", 7647, (outStream) -> {
            outStream.write(message.getBytes());
        });
        
        int readed = in.read(array);
        String response = new String(array);
        
        //assert
        Assert.assertEquals("Ola "+message, response);
    }
    
    @Test
    //@Ignore
    public void sendMultipleMessageSync() throws Exception{
        //arrange
        
        byte[] array = new byte[19];
        
        for (int i = 0;i<=9;i++){
            String message = "Tiago Almeida "+i;
            //act
            InputStream in = client.sendRequest("localhost", 7647, (outStream) -> {
                outStream.write(message.getBytes());
            });

            int readed = in.read(array);
            String response = new String(array);

            //assert
            Assert.assertEquals("Ola "+message, response);
        }
    }
    
    @Test
    //@Ignore
    public void sendSingleMessageWaitAssync() throws Exception{
        //arrange
        String message = "Tiago Almeida";
        
        Semaphore sem = new Semaphore(0);
        ResponseTestSync response = new ResponseTestSync(sem, 17);
        //act
        client.sendRequestWaitAssync("localhost", 7647,(outStream) -> {
            outStream.write(message.getBytes());
        }  ,response );
        
        sem.acquire();//block until receive
        //assert
        Assert.assertEquals("Ola "+message, response.message);
    }
    
    @Test
    //@Ignore
    public void sendMultipleMessageWaitAssync() throws Exception{
        Semaphore sem = new Semaphore(0);
        
        Supplier<ResponseTestSync> r = () -> new ResponseTestSync(sem, 19);
        List<ResponseTestSync> responses = Stream
                .generate(r)
                .limit(10)
                .collect(Collectors.toList());
        
        String message = "Tiago Almeida";
        //arrange
        for (int i = 0;i<=9;i++){
            String msgToSend = message+" "+i;

            //act
            client.sendRequestWaitAssync("localhost", 7647,(outStream) -> {
                outStream.write(msgToSend.getBytes());
            }  ,responses.get(i) );

        }
        sem.acquire(10);//block until all response came in
        
        //assert
        for (int i = 0;i<=9;i++){
            Assert.assertEquals(("Ola "+message+" "+i),responses.get(i).message);
        }
    }
    
    @Test
    //@Ignore
    public void sendSingleMessageAssync() throws Exception{
        //arrange
        String message = "Tiago Almeida";
        
        Semaphore sem = new Semaphore(0);
        ResponseTestSync response = new ResponseTestSync(sem, 17);
        //act
        client.sendRequestAssync("localhost", 7647,(outStream) -> {
            outStream.write(message.getBytes());
        }  ,response );
        
        sem.acquire();//block until receive
        //assert
        Assert.assertEquals("Ola "+message, response.message);
    }
    
    @Test
    public void sendMultipleMessageAssync() throws Exception{
        Semaphore sem = new Semaphore(0);
        
        Supplier<ResponseTestSync> r = () -> new ResponseTestSync(sem, 19);
        List<ResponseTestSync> responses = Stream
                .generate(r)
                .limit(10)
                .collect(Collectors.toList());
        
        String message = "Tiago Almeida";
        //arrange
        for (int i = 0;i<=9;i++){
            String msgToSend = message+" "+i;

            //act
            client.sendRequestAssync("localhost", 7647,(outStream) -> {
                outStream.write(msgToSend.getBytes());
            }  ,responses.get(i) );

        }
        sem.acquire(10);//block until all response came in
        
        //assert
        for (int i = 0;i<=9;i++){
            Assert.assertEquals(("Ola "+message+" "+i),responses.get(i).message);
        }
    }
    
    @Test
    public void sendSingleMessageWithoutResponse() throws Exception{
        //arrange
        Semaphore semServer = new Semaphore(0);
        ServerHandler handler = new ServerHandler(semServer);
        
        ThreadServerRequest serverTest = new ThreadServerRequest(7777,handler);
        serverTest.start();
        
        String message = "Tiago Almeida";
        //act
        client.sendRequest("localhost", 7777, (outStream) -> {
            outStream.write(message.getBytes());
        });
        
        semServer.acquire();
        
        Assert.assertEquals(message,handler.receiveMessage);
        
        
    }
}
