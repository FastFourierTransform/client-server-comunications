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

import com.pt.implementation.server.ThreadConnectionServerTCP;
import com.pt.implementation.server.Server;
import com.pt.exceptions.ServerAlreadyUsePort;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ServerMethodTest {
    
    public ServerMethodTest() {
    }
    
    private Server server;
    
    @Before
    public void setUp() {
        server = new Server(100,100);
    }
    
    @After
    public void tearDown() {
        server.shutdown();
        Thread[] threadsRunning = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsRunning);

    }

    @Test
    public void testStartAndStopListningConnections() throws Exception {
        //arrange,act
        server.startListningConnections(4554
                , (inMessage, outMessage) -> {
                } );
        
        //assert
        Thread[] threadsRunning = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsRunning);
        
        assertTrue(Arrays.asList(threadsRunning).stream()
                .anyMatch(x -> x.getName().contains("ConnectionServer")));
        
        //not the best pratic have two propose in the same test
        server.stopListningConnections(4554);
        
        //assert
        threadsRunning = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsRunning);
        
    }

    @Test
    public void testStopUnstartConnection(){
        //arrange
        Thread[] threadsRunning = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsRunning);
        System.out.println("Antes");
        Arrays.asList(threadsRunning).stream()
                .filter(t -> !t.isInterrupted())
                .forEach(System.out::println);
        
        //act
        server.stopListningConnections(4000);
        
        //assert
        System.out.println("Depois");
        threadsRunning = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsRunning);
        Arrays.asList(threadsRunning).stream()
                .filter(t -> !t.isInterrupted())
                .forEach(System.out::println);
    }
    
    @Test(expected = ServerAlreadyUsePort.class)
    public void testStartInAlredyUsagePortConnection()throws Exception {
        //arrange
        server.startListningConnections(4001
                ,(inMessage, outMessage) -> {
                } );
        //act
        server.startListningConnections(4001
                , (inMessage, outMessage) -> {
                });
    }
    
    @Test
    public void testStartAndStopListningConnections_NonDefaultImplementation() throws Exception {
        //arrange,act
        server.startListningConnections(new ThreadConnectionServerTCP(
                8000,
                (inMessage, outMessage) -> {
                },
                //dumb thread pool only for test propose
                Executors.newCachedThreadPool()));
        
        //assert
        Thread[] threadsRunning = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsRunning);
        
        assertTrue(Arrays.asList(threadsRunning).stream()
                .anyMatch(x -> x.getName().contains("ConnectionServer")));
        
        //not the best pratic have two propose in the same test
        server.stopListningConnections(8000);
        
        //assert
        threadsRunning = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsRunning);
        
    }

    
    @Test(expected = ServerAlreadyUsePort.class)
    public void testStartInAlredyUsagePortConnection_NonDefaultImplementation()throws Exception {
        //arrange
        server.startListningConnections(new ThreadConnectionServerTCP(
                8001,
                (inMessage, outMessage) -> {
                },
                //dumb thread pool only for test propose
                Executors.newCachedThreadPool()));
        //act
        server.startListningConnections(new ThreadConnectionServerTCP(
                8001,
                (inMessage, outMessage) -> {
                },
                //dumb thread pool only for test propose
                Executors.newCachedThreadPool()) );
    }
    
    @Test
    public void testMultiplesListningConnections() throws Exception {
        //arrange
        List<Integer> ports = Arrays.asList(4555, 4556, 4557, 4558);
        //act
        server.startListningConnections(ports.get(0)
                , (inMessage, outMessage) -> {
                });
        server.startListningConnections(ports.get(1)
                , (inMessage, outMessage) -> {
                } );
        server.startListningConnections(ports.get(2)
                , (inMessage, outMessage) -> {
                } );
        server.startListningConnections(ports.get(3)
                , (inMessage, outMessage) -> {
                } );
        
        //assert
        Thread[] threadsRunning = new Thread[Thread.activeCount()];
        Thread.enumerate(threadsRunning);
        assertTrue(Arrays.asList(threadsRunning).stream()
                .anyMatch(x -> x.getName().contains("ConnectionServer") 
                        //verify if thread name contains any of the server ports
                        && ports.stream().anyMatch(p -> x.getName().contains(p.toString()))));
    }

}
