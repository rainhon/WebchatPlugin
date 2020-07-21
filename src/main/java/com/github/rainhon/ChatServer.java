package com.github.rainhon;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ChatServer extends WebSocketServer {

    private final JavaPlugin plugin;

    public ChatServer( int port , JavaPlugin plugin ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
        this.plugin = plugin;
    }

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("from", "服务器");
        messageJson.addProperty("content", "您已连接到服务器聊天系统");
        conn.send(messageJson.toString()); //This method sends a message to the new client
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {

    }

    @Override
    public void onMessage( WebSocket conn, String message ) {
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("from", "WEB_");
        messageJson.addProperty("content", message);
        broadcast(messageJson.toString());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
           Bukkit.broadcastMessage(ChatColor.YELLOW + "[云玩家]:" + ChatColor.WHITE + message);
        });
    }
//    @Override
//    public void onMessage( WebSocket conn, ByteBuffer message ) {
//        broadcast( message.array() );
//        System.out.println( conn + ": " + message );
//    }

    @Override
    public void onError( WebSocket conn, Exception ex ) {
        ex.printStackTrace();
        if( conn != null ) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(100);
    }

}