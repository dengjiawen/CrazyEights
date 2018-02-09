package logic;

import common.Console;
import common.Constants;
import logic.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Server {

    public static boolean allReady = false;
    public static Crazy8s.Client[] players;
    public static ServerSocket listener;

    static ExecutorService serverThread = Executors.newSingleThreadExecutor();

    public static void init (int portNumber) throws Exception {
        listener = new ServerSocket(portNumber);
        serverThread.submit(() -> initConnection(portNumber));
    }

    public static void initConnection (int portNumber) {
        Console.print("Crazy8s Server is Running");

        int maxPlayer = Constants.element("MaxPlayer");

        try {
            while (true) {
                Crazy8s game = new Crazy8s();

                players = new Crazy8s.Client[maxPlayer];

                Socket playerSocket = null;
                try {
                    playerSocket = listener.accept();
                } catch (IOException e) {}

                players[1] = game.new Client(playerSocket, 0 + 1);
                players[1].start();

                game.currentPlayer = 0;

                while (!allReady) {
                    boolean accepted = false;
                    playerSocket = null;

                    try {
                        playerSocket = listener.accept();
                        accepted = true;
                    } catch (Exception e) {
                        accepted = false;
                    }

                    if (accepted) {
                        players[game.numPlayer + 1] = game.new Client(playerSocket, 0 + 1);
                        players[game.numPlayer + 1].start();
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




