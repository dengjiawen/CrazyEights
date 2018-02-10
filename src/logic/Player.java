package logic;

import common.Console;
import ui.GameWindow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Player {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private int playerNum;

    public Hand hand;
    String name;

    public Player(String serverAddress, int port, String name) throws Exception {

        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        hand = new Hand();

        this.name = name;
    }

    public void play() throws Exception {

        String response;

        try {
            response = in.readLine();
            Console.print(response);
            if (response.startsWith("WELCOME")) {
                playerNum = Character.getNumericValue(response.charAt(8));
                out.println("NAME" + name);
            }

            while (true) {
                response = in.readLine();
                System.out.println(response);

                if (response.startsWith("CONNECT")) {
                    int playerNum = Integer.parseInt(response.substring(7,8));
                    String playerName = response.substring(8);
                    GameWindow.requestRef().addPlayer(playerName, playerNum);
                } else if (response.startsWith("CARD")) {

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
            socket.close();
        }
    }

}




