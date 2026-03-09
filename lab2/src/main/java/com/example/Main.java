package com.example;

import io.jooby.Jooby;



public class Main {
    public static void main(String[] args) {
        Jooby.runApp(args, JoobyApp::new);
    }
}