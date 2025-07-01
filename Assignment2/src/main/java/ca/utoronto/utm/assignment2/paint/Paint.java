package ca.utoronto.utm.assignment2.paint;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Paint extends Application {

        public static void main(String[] args) {
                launch(args);
        }

        @Override
        public void start(Stage stage) throws Exception {

                FXMLLoader fxmlLoader = new FXMLLoader(
                                getClass().getResource("/ca/utoronto/utm/assignment2/paint-view.fxml"));

                Parent root = fxmlLoader.load();
                root.requestFocus();
                Scene scene = new Scene(root);
                stage.setTitle("Paint");
                stage.setScene(scene);
                stage.show();
        }
}
