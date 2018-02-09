import common.Console;
import ui.GameWindow;

/**
 * Created by freddeng on 2018-02-07.
 */
public class Launcher {

    static GameWindow window;

    public static void main (String[] args) throws Exception {
        Console.print("Welcome.");
        Console.print("This is a test message.");

        window = new GameWindow();
        GameWindow.setRef(window);
    }

}
