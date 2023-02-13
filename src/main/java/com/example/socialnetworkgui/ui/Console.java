package com.example.socialnetworkgui.ui;


import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.service.UserService;

import java.util.Scanner;

public class Console {

    //constants
    private static final int EXIT = 0;
    private static final int ADD_USER = 1;
    private static final int REMOVE_USER = 2;
    private static final int PRINT_USERS = 3;
    private static final int ADD_FRIEND = 4;
    private static final int REMOVE_FRIEND = 5;

    private static final int COMMUNITIES_NUMBER = 6;

    private static final int MOST_SOCIABLE_COMMUNITY = 7;

    private static final int PRINT_FRIENDSHIPS = 8;
    private static final int UPDATE_USER = 9;
    private static final int UPDATE_FRIENDSHIPS = 10;

    private UserService serviceUsers;

    public Console(UserService serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    /**
     * Adding a user to the service
     */
    private void addUser()
    {
        Scanner in = new Scanner(System.in);
        in.useDelimiter("\r?\n");
        System.out.print("first name:");
        String first_name = in.next();
        System.out.print("last name:");
        String last_name = in.next();
        System.out.print("password:");
        String password = in.next();

        User user = new User(first_name,last_name, password);
        serviceUsers.save(user);
    }

    /**
     * Deleting a user to the service
     */

    private void removeUser()
    {
        Scanner in = new Scanner(System.in);
        in.useDelimiter("\r?\n");
        System.out.print("ID:");
        Integer ID = in.nextInt();

        serviceUsers.delete(ID);
    }

    private void updateUser()
    {
        Scanner in = new Scanner(System.in);
        in.useDelimiter("\r?\n");
        System.out.print("ID:");
        Integer ID = in.nextInt();
        System.out.print("new first name:");
        String first_name = in.next();
        System.out.print("new last name:");
        String last_name = in.next();
        System.out.print("password:");
        String password = in.next();

        User user = new User(first_name,last_name, password);
        user.setId(ID);

        serviceUsers.update(user);
    }

    /**
     * Printing all users.txt
     */
    private void printUsers() {

        for(User u : serviceUsers.findAll())
        {
            System.out.println(u);
        }
    }

    /**
     * Adding a friend to users.txt
     */

    private void addFriend() {
        Scanner in = new Scanner(System.in);
        in.useDelimiter("\r?\n");
        System.out.print("ID user:");
        Integer ID_user = in.nextInt();
        System.out.print("ID new friend:");
        Integer ID_friend = in.nextInt();

        serviceUsers.addFriend(ID_user,ID_friend);
    }

    /**
     * Removing friend from user
     */
    private void removeFriend() {
        Scanner in = new Scanner(System.in);
        in.useDelimiter("\r?\n");
        System.out.print("ID user:");
        Integer ID_user = in.nextInt();
        System.out.print("ID friend removed:");
        Integer ID_friend = in.nextInt();

        serviceUsers.removeFriend(ID_user,ID_friend);
    }

    private void updateFriendship() {
        Scanner in = new Scanner(System.in);
        in.useDelimiter("\r?\n");
        System.out.print("ID first user:");
        Integer ID_user1 = in.nextInt();
        System.out.print("ID second user:");
        Integer ID_user2 = in.nextInt();

        serviceUsers.updateFriendship(ID_user1,ID_user2);
    }

    private void numberOfCommunities()
    {
        System.out.println("Number of communities:" + serviceUsers.numberOfCommunities());
    }

    private void mostSociableCommunity()
    {
        System.out.println("Most sociable community:" + serviceUsers.mostSociableCommunity());
    }

    private void printFriendships()
    {
        serviceUsers.getFriendships().forEach(System.out::println);
    }

    private void commands()
    {
        System.out.println("");
        System.out.println("exit:" + EXIT);
        System.out.println("add user:" + ADD_USER);
        System.out.println("remove user:" + REMOVE_USER);
        System.out.println("print users:" + PRINT_USERS);
        System.out.println("add friend:" + ADD_FRIEND);
        System.out.println("remove friend:" + REMOVE_FRIEND);
        System.out.println("number of communities:" + COMMUNITIES_NUMBER);
        System.out.println("most sociable community:" + MOST_SOCIABLE_COMMUNITY);
        System.out.println("print friendships:" + PRINT_FRIENDSHIPS);
        System.out.println("update user:" + UPDATE_USER);
        System.out.println("update friendships:" + UPDATE_FRIENDSHIPS);

        System.out.println("");
    }

    /**
     * Running the console menu
     */

    public void run()
    {
        while(true)
        {
            commands();

            Scanner in = new Scanner(System.in);

            System.out.print("cmd>>>>");
            int cmd = in.nextInt();

            if(cmd == EXIT)
                break;

            switch(cmd) {
                case ADD_USER:
                    try
                    {
                        addUser();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case REMOVE_USER:
                    try
                    {
                        removeUser();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case PRINT_USERS:
                    printUsers();
                    break;

                case ADD_FRIEND:
                    try
                    {
                        addFriend();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                case REMOVE_FRIEND:
                    try
                    {
                        removeFriend();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;

                case COMMUNITIES_NUMBER:
                    try
                    {
                        numberOfCommunities();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;

                case MOST_SOCIABLE_COMMUNITY:
                    try
                    {
                        mostSociableCommunity();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;

                case PRINT_FRIENDSHIPS:
                    printFriendships();
                    break;

                case UPDATE_USER:
                    try
                    {
                        updateUser();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;


                case UPDATE_FRIENDSHIPS:
                    try
                    {
                        updateFriendship();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;

                default:
                    System.out.println("Comanda incorecta");
            }

        }
    }
}
