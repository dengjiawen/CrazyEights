package logic;

import common.Console;
import common.Constants;
import logic.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Server {

    private static boolean all_ready = false;
    private static ServerSocket listener;

    private static Crazy8s game;

    private static ExecutorService serverThread = Executors.newSingleThreadExecutor();

    public static void init (int portNumber) throws Exception {
        listener = new ServerSocket(portNumber);
        serverThread.submit(() -> initConnection(portNumber));
    }

    public static void stopListening () {
        try {
            listener.close();
        } catch (IOException error) {
            // tell someone that cares
        }
    }

    public static void initConnection (int portNumber) {
        Console.print("Crazy8s Server is Running");

        int maxPlayer = Constants.element("MaxPlayer");

        try {
            while (true) {
                game = new Crazy8s();

                Socket playerSocket = null;
                try {
                    playerSocket = listener.accept();
                } catch (IOException e) {

                }

                game.clients.add(game.new Client(playerSocket));
                game.clients.get(0).start();

                while (!all_ready) {
                    boolean accepted = false;
                    playerSocket = null;

                    try {
                        playerSocket = listener.accept();
                        accepted = true;
                    } catch (Exception e) {
                        accepted = false;
                    }

                    if (accepted && game.clients.size() < maxPlayer) {
                        Crazy8s.Client newClient = game.new Client(playerSocket);
                        game.clients.add(newClient);
                        newClient.start();
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




