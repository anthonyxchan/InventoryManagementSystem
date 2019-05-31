/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author antho
 */
public class LoginPanelController implements Initializable {
    
    @FXML private Label loginStatus;    
    @FXML private TextField txtUsername;            
    @FXML private PasswordField txtPassword;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        if (txtUsername.getText().equals("user") && txtPassword.getText().equals("user123")){
            loginStatus.setText("Login Success");
            Parent home_page = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage app = (Stage)((Node) event.getSource()).getScene().getWindow();
            app.setScene(new Scene(home_page,1000, 600));
            app.show();
        
        }
        else{
            loginStatus.setText("Login Fail");
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
