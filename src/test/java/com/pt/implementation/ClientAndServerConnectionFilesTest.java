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
import com.pt.implementation.infrastruct.ThreadServerConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Test capability send files
 * 
 * @author Tiago Alexandre Melo Almeida
 */
public class ClientAndServerConnectionFilesTest {
    private Client client;
    private static ThreadServerConnection server;
    
    @BeforeClass
    public static void start(){
        // this server handler response with "file receive"
        //server = new ThreadServer(5647,(message) -> ("Ola file receive").getBytes());
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
    /*
    @Test
    public void ClientMultipleSendRequestAssync() throws ClientAlreadyUsePort, Exception{
        //arrange,
        IConnection connection = client.startConnection("localhost", 5647, (message) -> {});//default handler will not be used in this test

        File fileToSend = new File("clientFS/ProjetoFinal.pdf");
        FileInputStream inputFile = new FileInputStream(fileToSend);

        Semaphore sem = new Semaphore(0);
        ResponseTestSync handler = new ResponseTestSync(sem);
        
        connection.sendRequestAssync(inputFile,(int) fileToSend.length() ,handler);
        
        sem.acquire();
        
        System.out.println("Receiver " + new String(handler.response));
    }
*/
}
