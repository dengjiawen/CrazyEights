package logic;

import common.Console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Player {

    private static int PORT = 9050;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private int playerNum;

    public Hand hand;

    public Player(String serverAddress) throws Exception {

        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        hand = new Hand();
    }

    public void play() throws Exception {

        String response;

        try {
            response = in.readLine();
            Console.print(response);
            if (response.startsWith("WELCOME")) {
                playerNum = Character.getNumericValue(response.charAt(8));
            }

            while (true) {
                response = in.readLine();
                System.out.println(response);

                if (response.startsWith("CONNECT")) {

                } else if (response.startsWith("ADD_CARD")) {

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




