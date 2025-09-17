package edu.lk.ijse;

import edu.lk.ijse.db.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        String plainPassword = "123";
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        System.out.println("Hashed password for '123': " + hashedPassword);


        boolean isMatch = BCrypt.checkpw("123", hashedPassword);
        System.out.println("Password verification result: " + isMatch);


        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginView/LoginController.fxml"));
        primaryStage.setTitle("Elite Driving School - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            HibernateUtil.shutdown();
        });
    }
}
