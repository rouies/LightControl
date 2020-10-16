package com.cctv.controller.light.manager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UE4Controller {

    private class ClearController implements Runnable{

        private int timeout;

        public ClearController(int timeout){
            this.timeout = timeout;
        }

        @Override
        public void run() {
            int timeout = this.timeout;
            long currentTimeMillis = System.currentTimeMillis();
            for(Map.Entry<String,UE4Client> item : container.entrySet()){
                UE4Client client = item.getValue();
                Long lastAccessTime = client.getLastAccessTime();
                System.out.println(currentTimeMillis - lastAccessTime);
                if(currentTimeMillis - lastAccessTime > timeout){
                    //未检测到心跳
                    client.close();
                    System.out.println("心跳检测失败");
                }
            }
        }
    }

    static ConcurrentHashMap<String,UE4Client> container = new ConcurrentHashMap<String,UE4Client>();

    private int cid = 0;

    private ServerSocket server;

    private boolean isContinue = true;

    public UE4Controller(){
    }

    public UE4Controller(int timeout,int delay){
        //启动计时器检查
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new ClearController(timeout),5000,delay, TimeUnit.MILLISECONDS);
    }

    private ExecutorService pools = Executors.newFixedThreadPool(4);

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
                pools.submit(new UE4Client(accept,String.valueOf(this.cid++)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendData(String name,byte...data) throws IOException {
        if(container.containsKey(name)){
            container.get(name).send(data);
        }
    }

    public void sendData(String name,int data) throws IOException {
        if(container.containsKey(name)){
            container.get(name).send(data);
        }
    }
}
