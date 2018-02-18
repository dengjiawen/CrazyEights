import common.Console;
import ui.LoadFrame;
import ui.GameWindow;
import ui.Resources;
import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by freddeng on 2018-02-07.
 */
public class Launcher {

    //Constant String used for console output.
    private static String class_name = "Launcher";
    private static ExecutorService resource_loader = Executors.newSingleThreadExecutor();
    private static boolean resource_loaded = false;

    static GameWindow window;
    static LoadFrame load;

    public static void main (String[] args) throws Exception {

        initiateLoading();
        loadResources();

        while (!resource_loaded) {
            Thread.sleep(500);
        }

        initiateWindow();

    }

    public static void initiateLoading () {
        SwingUtilities.invokeLater(() -> load = new LoadFrame());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {}
    }

    public static void initiateWindow () throws Exception {
        window = new GameWindow();
        SwingUtilities.invokeLater(() -> {
            GameWindow.setRef(window);
            load.setVisible(false);
            load.dispose();
            load = null;
            System.gc();
        });
    }

    public static void loadResources () {
        resource_loader.submit(() -> {
            Resources.loadCards();
            Resources.loadCoreAssets();
            Resources.loadButtons();
            Resources.loadMiscAssets();
            resource_loaded = true;
        });
    }

}
