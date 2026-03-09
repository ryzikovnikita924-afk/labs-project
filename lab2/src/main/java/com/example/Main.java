package com.example;

import io.jooby.Jooby;

import static io.jooby.Jooby.runApp;


public class Main {
    public static void main(String[] args) {
        Jooby.runApp(args, JoobyApp::new);
    }
}