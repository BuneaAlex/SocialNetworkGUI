package com.example.socialnetworkgui;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.validators.MessageValidator;
import com.example.socialnetworkgui.repository.db.MessageDBRepo;
import com.example.socialnetworkgui.service.UserService;
import com.example.socialnetworkgui.ui.Console;
import com.example.socialnetworkgui.utils.Constants;

import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) throws Exception {


        UserService service = UserService.getInstance();

        Console console = new Console(service);

        //console.run();




    }
}