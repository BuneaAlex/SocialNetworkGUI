package com.example.socialnetworkgui;


import com.example.socialnetworkgui.service.UserService;
import com.example.socialnetworkgui.ui.Console;


public class Main {
    public static void main(String[] args) throws Exception {


        UserService service = UserService.getInstance();

        Console console = new Console(service);

        //console.run();




    }
}