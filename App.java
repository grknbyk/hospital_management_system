import database.Datasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/scene/LoginScene.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }

    @Override
    public void init() throws Exception {
        super.init();
        if (!Datasource.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to database");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        Datasource.getInstance().close();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
