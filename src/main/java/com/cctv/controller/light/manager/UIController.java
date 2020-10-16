package com.cctv.controller.light.manager;

import com.cctv.controller.light.utils.StreamProcessor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UIController {

    private ServerSocket server;

    private boolean isContinue = true;

    private UE4Controller ue4Controller;

    public UIController(UE4Controller controller){
        this.ue4Controller = controller;
    }

    private ExecutorService pools = Executors.newFixedThreadPool(1);

    public void startServer(int port) throws IOException {
        if(this.server != null){
            return;
        }
        this.server = new ServerSocket();
        this.server.bind(new InetSocketAddress(port));
        while(this.isContinue){
            Socket accept = null;
            try {
                accept = this.server.accept();
                pools.submit(new UIClient(accept,this.ue4Controller));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
