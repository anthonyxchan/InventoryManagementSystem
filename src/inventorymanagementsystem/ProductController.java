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
public class ProductController implements Initializable {

    @FXML private Button addNewItems;
    @FXML private Button deleteProduct;

    @FXML private TableView<ProductList> tableProduct;
    @FXML private TableColumn<?, ?> itemNumberColumn;
    @FXML private TableColumn<?, ?> descriptionColumn;
    @FXML private TableColumn<?, ?> vendorColumn;
    @FXML private TableColumn<?, ?> costColumn;
    @FXML private TableColumn<?, ?> inventoryColumn;
    @FXML private TableColumn<?, ?> totalValueColumn;

    @FXML private JFXTextField item_number_textfield;
    @FXML private JFXTextField desciption_textfield;
    @FXML private JFXTextField vendor_textfield;
    @FXML private JFXTextField cost_textfield;
    @FXML private JFXTextField inventory_textfield;
    @FXML private TextField searchProduct_textfield;
    
    
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<ProductList> data;
 

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


    // add new item record to database when AddNewItem button is pressed
    @FXML
    private void loadAddNewItem(ActionEvent event)throws IOException, SQLException{
        // a sql insert value command
        String sql = "INSERT INTO Product(item_number, product_description, vendor, cost, inventory, total_value)\n" +
                       "VALUES (?,?,?,?,?,?)";
        
        // getText from JFXtextfield
        Integer item_number = Integer.parseInt(item_number_textfield.getText());
        String description = desciption_textfield.getText();
        String vendor = vendor_textfield.getText();
        Double cost = Double.valueOf(cost_textfield.getText());
        Double inventory = Double.valueOf(inventory_textfield.getText());
        
        // insert value into sql command
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, item_number);
            pst.setString(2, description);
            pst.setString(3, vendor);
            pst.setDouble(4, cost);
            pst.setDouble(5, inventory);
            pst.setDouble(6, cost* inventory);
            
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
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            pst.close();
        }
    }
    
    private void setCellTable(){
        itemNumberColumn.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        vendorColumn.setCellValueFactory(new PropertyValueFactory<>("vendor"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        inventoryColumn.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
    }
    
    private void loadDataFromDatabase() {
        // adding data.clear() because we want to make sure tableview has nothing 
        // otherwise data shown on table will be duplicated
        data.clear();
        try {
            // find all the record in Product
            pst = con.prepareStatement("Select* from Product");
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new ProductList(""+rs.getInt(1), rs.getString(2), rs.getString(3), ""+rs.getDouble(4), ""+rs.getDouble(5), ""+rs.getDouble(6)));
            }  
            tableProduct.setItems(data);
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void clearTextField(){
        desciption_textfield.clear();
        item_number_textfield.clear();
        vendor_textfield.clear();
        cost_textfield.clear();
        inventory_textfield.clear();
    }

    @FXML
    private void handleDeleteProduct(ActionEvent event) {
        String sql = "delete from Product where item_number = ?";
        try {
            pst = con.prepareStatement(sql);
                    // getText from JFXtextfield
            pst.setString(1, item_number_textfield.getText());
  
            int i  = pst.executeUpdate();

            if (i == 1){
                AlertDialog.display("info", "Data Deleted");
                loadDataFromDatabase();
                clearTextField();
            }  
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void searchProduct(KeyEvent event) {

        searchProduct_textfield.setOnKeyReleased(e ->{
            if(searchProduct_textfield.getText().equals("")){
                loadDataFromDatabase();
            }
            else{
                data.clear();
                String sql = "Select * From Product where item_number LIKE '%"+searchProduct_textfield.getText()+"%'" + 
                        "Union all Select * From Product where product_description LIKE '%"+searchProduct_textfield.getText()+"%'" + 
                        "Union all Select * From Product where vendor LIKE '%"+searchProduct_textfield.getText()+"%'" + 
                        "Union all Select * From Product where cost LIKE '%"+searchProduct_textfield.getText()+"%'" + 
                        "Union Select * From Product where inventory LIKE '%"+searchProduct_textfield.getText()+"%'" ;
                try {
                    pst = con.prepareStatement(sql);
                    // insert the item_number we want to search into sql statement
                    rs = pst.executeQuery();
                    while(rs.next()){
                        data.add(new ProductList(""+rs.getInt(1), rs.getString(2), rs.getString(3), ""+rs.getDouble(4), ""+rs.getDouble(5), ""+rs.getDouble(6)));
                    }
                    tableProduct.setItems(data);

                } catch (SQLException ex) {
                    Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
    }
    
    
    // code below this part manage the change of scene
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

