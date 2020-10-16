package com.cctv.controller.light;

import com.cctv.controller.light.manager.UE4Controller;
import com.cctv.controller.light.manager.UIController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.cctv.controller.light")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        final UE4Controller ue4Controller = new UE4Controller();
        final UIController uiController = new UIController(ue4Controller);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ue4Controller.startServer(9527);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    uiController.startServer(9528);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
