package top.mekal.volcano.sender.impl;

import top.mekal.volcano.sender.ISender;

/**
 * Created by mekal on 28/12/2016.
 */
public class ConsoleSender implements ISender {
    @Override
    public void send(String message) {
        System.out.println(message);
    }

    @Override
    public void close() {
        System.out.println("Console sender closed.");
    }
}
