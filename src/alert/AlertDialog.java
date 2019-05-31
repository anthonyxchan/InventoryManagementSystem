/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alert;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author antho
 */
public class AlertDialog {
    public static void display(String title, String message){
        // Empty window
        Stage window = new Stage();
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(150);
        
        Label label = new Label();
        label.setText(message);
        Button okay = new Button("OK");
        okay.setOnAction((e -> window.close()));
        
        VBox layout = new VBox(5);
        layout.getChildren().addAll(label, okay);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
        
        
    }
}
