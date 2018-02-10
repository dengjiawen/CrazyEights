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

    public static boolean allReady = false;
    public static ArrayList<Crazy8s.Client> players;
    public static ServerSocket listener;

    public static Crazy8s game;

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
                game = new Crazy8s();

                players = new ArrayList<>();

                Socket playerSocket = null;
                try {
                    playerSocket = listener.accept();
                } catch (IOException e) {}

                players.add(game.new Client(playerSocket));
                Console.print(players.get(0).toString());
                players.get(0).start();

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

                    if (accepted && players.size() < maxPlayer) {
                        Crazy8s.Client newClient = game.new Client(playerSocket);
                        players.add(newClient);
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




