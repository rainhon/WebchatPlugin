package com.github.rainhon.webchat;

import com.github.rainhon.ChatServer;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.UnknownHostException;

public class WebChat extends JavaPlugin {
    private ChatServer chatServer;

    @Override
    public void onEnable() {
        super.onEnable();

        Thread t = new Thread(()->{
            try{
                chatServer = new ChatServer(7000, this);
                chatServer.start();
            }catch (UnknownHostException e){
                Bukkit.getLogger().info(e.getLocalizedMessage());
            }
        });
        t.setDaemon(true);
        t.start();

        this.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncPlayerChatEvent event){
                JsonObject messageJson = new JsonObject();
                messageJson.addProperty("from", event.getPlayer().getPlayerListName());
                messageJson.addProperty("content", event.getMessage());
                chatServer.broadcast(messageJson.toString());
            }
        }, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        try{
            chatServer.stop();
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }

    }
}
