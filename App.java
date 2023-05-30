import database.Datasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.MedicineSupply;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/scene/panels/LoginScene.fxml"));
        Parent root = loader.load();

        Image icon = new Image(getClass().getResourceAsStream("ui/imgs/main-icon.png"));
        primaryStage.getIcons().add(icon);
        
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
        Datasource.getInstance().saveMedicineSupply(MedicineSupply.getInstance());
        Datasource.getInstance().close();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
