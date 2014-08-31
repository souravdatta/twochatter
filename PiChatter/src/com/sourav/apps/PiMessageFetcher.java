/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sourav.apps;

import java.awt.TrayIcon;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.restlet.resource.ClientResource;

/**
 *
 * @author sourdatt
 */
public class PiMessageFetcher implements Runnable {
    private Thread runner;
    private PiChatterMenu menuref;
    private UserBean bean;
    private volatile boolean running = false;
    private ClientResource client;
    
    public PiMessageFetcher(PiChatterMenu ic, UserBean bn) {
        menuref = ic;
        bean = bn;
        client = new ClientResource("<APP_URL>/api/ping/" + bean.getName());
        runner = new Thread(this);
    }
    
    public void start() {
        running = true;
        runner.start();
    }
    
    public void stop() {
        running = false;
        try {
            runner.join();
        } catch (InterruptedException iox) {}
        runner = null;
    }

    @Override
    public void run() {
        while (running) {
            try {
                String rawJSON = client.get().getText();
                JSONArray messages = (JSONArray)JSONValue.parse(rawJSON);
                PiChatterWindow window = PiChatterWindow.getWindow();
                
                for (int i = 0; i < messages.size(); i++) {
                    JSONObject message = (JSONObject)messages.get(i);
                    String from = (String)message.get("from");
                    String time = (String)message.get("time");
                    String body = (String)message.get("data");
                    body = PiCodec.decode(body);
                    
                    String str = String.format(" %s: %s\t (sent at: %s)", from, body, time);
                    window.appendChat(str);
                }
                
                if (messages.size() > 0) {
                    menuref.setNotification();
                }
                
            } catch (IOException iox) {
                iox.printStackTrace();
            }
            try {
                Thread.sleep(600);
            } catch (InterruptedException iox) {
                iox.printStackTrace();
            }
        }
    }
    
}
