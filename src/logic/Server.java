/**
 * Copyright 2018 (C) Jiawen Deng. All rights reserved.
 *
 * This document is the property of Jiawen Deng.
 * It is considered confidential and proprietary.
 *
 * This document may not be reproduced or transmitted in any form,
 * in whole or in part, without the express written permission of
 * Jiawen Deng.
 *
 *-----------------------------------------------------------------------------
 * Server.java
 *-----------------------------------------------------------------------------
 * This class initializes the server.
 *-----------------------------------------------------------------------------
 */

package logic;

import common.Console;
import common.Constants;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static boolean all_ready = false;       // boolean of whether all players are ready
    private static ServerSocket listener;           // listener to listen for incoming connections
    private static Crazy8s game;                    // Crazy8s game
    private static ExecutorService server_thread;    // thread for the server listener

    /**
     * Initializes server
     * @param portNumber    the port to start the server on
     */
    public static void init (int portNumber) {

        try {
            listener = new ServerSocket(portNumber);
        } catch (IOException e) {
            Console.printErrorMessage("Listening server cannot be started.", Server.class.getName());
        }

        server_thread = Executors.newSingleThreadExecutor();
        server_thread.submit(() -> initConnection(portNumber));
    }

    /**
     * Stop listening for new players
     */
    public static void stopListening () {
        try {
            listener.close();
            server_thread.shutdownNow();
            server_thread = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * initialize connection
     * @param portNumber    port number
     */
    public static void initConnection (int portNumber) {
        Console.print("Crazy8s Server is Running");

        int maxPlayer = Constants.getInt("MaxPlayer");  // max players

        // create new game, and accept new players as long as max player had not been
        // met, and connection is firmly established.

        try {
            while (true) {
                game = new Crazy8s();

                Socket playerSocket = null;
                try {
                    playerSocket = listener.accept();
                } catch (IOException e) {}

                game.addClientThread(playerSocket);

                while (!all_ready) {
                    boolean accepted = false;
                    playerSocket = null;

                    try {
                        playerSocket = listener.accept();
                        accepted = true;
                    } catch (IOException e) {
                        accepted = false;
                    }

                    if (accepted && game.getNumClient() < maxPlayer) {
                        game.addClientThread(playerSocket);
                    }
                }
            }
        } finally {
            try {
                listener.close();
            } catch (IOException e) {}
        }
    }

}




