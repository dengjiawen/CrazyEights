package logic;

import common.Console;
import ui.GameWindow;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Player {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public int playerNum;

    public Hand hand;
    String name;

    ExecutorService playerThread = Executors.newSingleThreadExecutor();

    public Player(String serverAddress, int port, String name) throws Exception {

        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        hand = new Hand();

        this.name = name;

        playerThread.submit(() -> play());
    }

    public void updateList () {
        out.println("REQ_LIST");
    }

    public void play() {

        String response = "";

        try {

            while (true) {
                try {
                    response = in.readLine();
                } catch (IOException e) {};

                Console.print(response);

                if (response.startsWith("WELCOME")) {
                    playerNum = Character.getNumericValue(response.charAt(7));
                    out.println("NAME" + name);
                    out.println("REQ_LIST");
                } else if (response.startsWith("ALIVE")) {

                } else if (response.startsWith("NUM_UPDATE")) {
                    playerNum = Character.getNumericValue(response.charAt(10));
                }

                if (response.startsWith("CONNECT")) {
                    int num = Integer.parseInt(response.substring(7,8));
                    String playerName = response.substring(8);
                    if (num != playerNum) {
                        SwingUtilities.invokeLater(() -> GameWindow.requestRef().addPlayer(playerName, num));
                    }
                } else if (response.startsWith("DISCONNECT")) {
                    int num = Integer.parseInt(response.substring(10,11));
                    SwingUtilities.invokeLater(() -> GameWindow.requestRef().removePlayer(num));
                }


                else if (response.startsWith("CARD")) {

                    byte suit = Byte.parseByte(response.substring(4,5));
                    byte rank = Byte.parseByte(response.substring(5));
                    hand.add(new Card(suit, rank));

                } else if (response.startsWith("VALID_MOVE")) {
                } else if (response.startsWith("OPPONENT_MOVED")) {
                } else if (response.startsWith("VICTORY")) {
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    break;
                } else if (response.startsWith("MESSAGE")) {
                }
            }
            out.println("QUIT");
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {}
        }
    }

}




