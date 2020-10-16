package com.cctv.controller.light.manager;

import com.cctv.controller.light.utils.StreamProcessor;

import java.io.IOException;
import java.net.Socket;

public class UE4Client implements Runnable{

    private String id;

    private String name;

    private Socket client;

    private StreamProcessor sp;

    private Long lastAccessTime;

    private boolean isContinue = true;

    public UE4Client(Socket client,String id) throws IOException {
        this.id = id;
        this.client = client;
        this.sp = new StreamProcessor(client.getInputStream(),client.getOutputStream());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    @Override
    public void run() {
        try {
            int nameLength = this.sp.readInt32();
            this.name = this.sp.readString(nameLength, "UTF-8");
            this.refurbish();
            System.out.println("接入UE4:" + name);
            while(this.isContinue){
                short st = 0;
                try {
                    st = this.sp.readInt16();
                    System.out.println(st);
                } catch (IOException e) {
                    break;
                }
                if(st == 1){//表示心跳
                    System.out.println("接收心跳");
                    this.lastAccessTime = System.currentTimeMillis();
                } else if(st == 0){ //表示结束
                    System.out.println("客户端主动关闭");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            this.destory();
        }
    }

    private void refurbish(){
        this.lastAccessTime = System.currentTimeMillis();
        if(!UE4Controller.container.contains(this.id)){
            UE4Controller.container.put(this.id,this);
        }
    }

    public void send(byte...value) throws IOException {
        this.sp.writeBytes(value);
    }

    public void send(int value) throws IOException {
        this.sp.writeInt32(value);
    }

    private void destory(){
        System.out.println("对端主动关闭:" + this.name);
        if(this.client != null){
            try {
                this.sp.close();
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        UE4Controller.container.remove(this.id);
    }

    public void close(){
        System.out.println("xhh");
        this.isContinue = false;
        if(this.client != null){
            Socket client = this.client;
            this.client = null;
            try {
                this.sp.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            UE4Controller.container.remove(this.id);
        }
    }
}