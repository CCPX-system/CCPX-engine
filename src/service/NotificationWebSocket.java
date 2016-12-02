package service;


import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket/notification") // URI
public class NotificationWebSocket {

    private static final String GUEST_PREFIX = "Guest";
    // AtomicInteger getAndIncrement()
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    // CopyOnWriteArraySet toArray()
    private static final Set<NotificationWebSocket> connections =
            new CopyOnWriteArraySet<NotificationWebSocket>();

    private final String nickname;
    // Session
    private Session session;

    public NotificationWebSocket() {
    	String threadName = "Thread-Name:"+Thread.currentThread().getName();
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
        System.out.println(threadName+", "+nickname);
    }


    @OnOpen
    public void start(Session session) {
    	System.out.println("클라이언트 접속됨 "+session);
        this.session = session;
        connections.add(this);
        //String message = String.format("* %s %s", nickname, "has joined.");
        //broadcast(message);
    }


    @OnClose
    public void end() {
        connections.remove(this);
        //String message = String.format("* %s %s", nickname, "has disconnected.");
        //broadcast(message);
    }


    @OnMessage
    public void incoming(String message) {
    	System.out.print(message);
    	
    	/*
    	String threadName = "Thread-Name:"+Thread.currentThread().getName();
    	System.out.println(threadName+", "+nickname);
        if(message==null || message.trim().equals("")) return;
        String filteredMessage = String.format("%s: %s", nickname, message);
        
        System.out.print(filteredMessage);
        */
        broadcast(message);
    }

    
    @OnError
    public void onError(Throwable t) throws Throwable {
        System.err.println("Chat Error: " + t.toString());
    }


    public static void broadcast(String msg) {
    	System.out.print(msg);
    	
        for (NotificationWebSocket client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                System.err.println("Chat Error: Failed to send message to client:"+e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }

                String message = String.format("* %s %s",
                        client.nickname, "has been disconnected.");
                broadcast(message);
            }
        }
    }
}