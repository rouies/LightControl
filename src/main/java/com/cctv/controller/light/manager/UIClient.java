package com.cctv.controller.light.manager;

import com.cctv.controller.light.utils.StreamProcessor;

import java.io.IOException;
import java.net.Socket;

public class UIClient implements Runnable{

    private StreamProcessor sp;

    private Socket client;

    private UE4Controller ue4;

    private boolean isContinue = true;

    public UIClient(Socket client,UE4Controller ue4) throws IOException {
        this.client = client;
        this.ue4 = ue4;
        this.sp = new StreamProcessor(client.getInputStream(),client.getOutputStream());
    }

    private void destory(){
        if(this.client != null){
            try {
                this.sp.close();
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        this.isContinue = false;
        if(this.client != null){
            try {
                this.sp.close();
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.client = null;
        }
    }

    @Override
    public void run() {
        System.out.println("接入UI");
        while(this.isContinue){
            try {
                int opt = this.sp.readByte();
                if(opt == 0){
                    //虚拟
                    int id = this.sp.readInt32();
                    int value = this.sp.readInt32();
                    this.ue4.sendData(String.valueOf(id),value);
                } else if(opt == 1){
                    //实体
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        this.destory();
    }
}
