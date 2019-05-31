/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

import alert.AlertDialog;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antho
 */
public class SalesController implements Initializable {

    @FXML private Button deleteOrder;
    @FXML private Button addNewOrder;
    
    @FXML private TextField searchOrder_textfield;
    @FXML private JFXTextField order_number_textfield;
    @FXML private JFXTextField customer_textfield;
    @FXML private JFXTextField item_number_textfield;
    @FXML private DatePicker order_date_textfield;
    @FXML private JFXTextField price_textfield;
    @FXML private JFXTextField quantity_textfield;

    @FXML private TableView<OrderList> tableOrder;
    @FXML private TableColumn<?, ?> orderNumberColumn;
    @FXML private TableColumn<?, ?> itemNumberColumn;
    @FXML private TableColumn<?, ?> customerColumn;
    @FXML private TableColumn<?, ?> orderDateColumn;
    @FXML private TableColumn<?, ?> priceColumn;
    @FXML private TableColumn<?, ?> quantityColumn;
    @FXML private TableColumn<?, ?> orderTotalColumn;

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<OrderList> data;
 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = DBConnection.InventoryManagementDatabase();
        data = FXCollections.observableArrayList();
        setCellTable();
        loadDataFromDatabase();
    }    

    @FXML
    private void deleteOrder(ActionEvent event) {
        String sql = "delete from orderList where order_number = ?";
        try {
            pst = con.prepareStatement(sql);
                    // getText from JFXtextfield
            pst.setString(1, order_number_textfield.getText());
  
            int i  = pst.executeUpdate();

            if (i == 1){
                AlertDialog.display("info", "Data Deleted");
                loadDataFromDatabase();
                clearTextField();
            }  
        } catch (SQLException ex) {
            AlertDialog.display("info", "Wrong data inserted. Please try again");       

            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    
    @FXML
    private void searchProduct(KeyEvent event) {

        searchOrder_textfield.setOnKeyReleased(e ->{
            if(searchOrder_textfield.getText().equals("")){
                loadDataFromDatabase();
            }
            else{
                data.clear();
                String sql = "Select * From OrderList where order_number LIKE '%"+searchOrder_textfield.getText()+"%'" + 
                        "Union all Select * From OrderList where item_number LIKE '%"+searchOrder_textfield.getText()+"%'" + 
                        "Union all Select * From OrderList where customer LIKE '%"+searchOrder_textfield.getText()+"%'" + 
                        "Union all Select * From OrderList where order_date LIKE '%"+searchOrder_textfield.getText()+"%'" + 
                        "Union all Select * From OrderList where price LIKE '%"+searchOrder_textfield.getText()+"%'" +
                        "Union all Select * From OrderList where quantity LIKE '%"+searchOrder_textfield.getText()+"%'" + 
                        "Union all Select * From OrderList where order_total LIKE '%"+searchOrder_textfield.getText()+"%'" ;
                        
                try {
                    pst = con.prepareStatement(sql);
                    // insert the item_number we want to search into sql statement
                    rs = pst.executeQuery();
                    while(rs.next()){
                        data.add(new OrderList(""+rs.getInt(1), ""+rs.getInt(2), 
                        rs.getString(3), ""+rs.getDate(4), ""+rs.getDouble(5), 
                        ""+rs.getDouble(6), ""+rs.getDouble(7)));
                    }
                    tableOrder.setItems(data);

                } catch (SQLException ex) {
                    Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
    }
    
    

    @FXML
    private void addNewOrder(ActionEvent event)throws IOException, SQLException {
        String sql = "INSERT INTO OrderList(order_number, item_number, "
                + "customer, order_date, price, quantity, order_total)\n" +
                       "VALUES (?,?,?,?,?,?,?)";
        
        Integer order_number = Integer.parseInt(order_number_textfield.getText());
        Integer item_number = Integer.parseInt(item_number_textfield.getText()); 
        String customer = customer_textfield.getText();
        Double price = Double.valueOf(price_textfield.getText());
        Integer quantity = Integer.parseInt(quantity_textfield.getText());

                // insert value into sql command
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, order_number);
            pst.setInt(2, item_number);
            pst.setString(3, customer);
            pst.setDate(4, java.sql.Date.valueOf(order_date_textfield.getValue()));
            pst.setDouble(5, price);
            pst.setDouble(6, quantity);
            pst.setDouble(7, price*quantity);

            int i = pst.executeUpdate();
            // if executioin is successful
            if (i == 1){
                AlertDialog.display("info", "Data Inserted");
                // set tableProduct with new value
                setCellTable();
                loadDataFromDatabase();
                clearTextField();
            }

        } catch (SQLException ex) {
            AlertDialog.display("info", "Wrong data inserted. Please try again");       
    //        Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            pst.close();
        }
    }
    
    private void setCellTable(){

        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        itemNumberColumn.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderTotalColumn.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

    }

    private void loadDataFromDatabase(){
        data.clear();
        try {
            // find all the record in Product
            pst = con.prepareStatement("Select* from OrderList");
            rs = pst.executeQuery();
            while(rs.next()){

                data.add(new OrderList(""+rs.getInt(1), ""+rs.getInt(2), 
                        rs.getString(3), ""+rs.getDate(4), ""+rs.getDouble(5), 
                        ""+rs.getDouble(6), ""+rs.getDouble(7)));
            }  
            tableOrder.setItems(data);
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

       
    private void clearTextField(){
        order_number_textfield.clear();
        customer_textfield.clear();
        item_number_textfield.clear();
        price_textfield.clear();
        quantity_textfield.clear();
    }

    
     @FXML
    public void goToDashboard(ActionEvent event) throws IOException{
         loadWindow("Dashboard.fxml",event);
    }
    
    @FXML
    public void goToProduct(ActionEvent event) throws IOException{
        loadWindow("Product.fxml",event);

    }



    @FXML
    public void goToSales(ActionEvent event) throws IOException{
        loadWindow("Sales.fxml",event);
    }
    
    void loadWindow(String loc, ActionEvent event)throws IOException{
        Parent home_page = FXMLLoader.load(getClass().getResource(loc));
        Scene reportScene = new Scene(home_page, 1000, 600);
        Stage app = (Stage)((Node) event.getSource()).getScene().getWindow();
        app.setScene(reportScene);
        app.show(); 
    }
    
    
}
