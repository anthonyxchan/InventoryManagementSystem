/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antho
 */
public class DashboardController implements Initializable {

    @FXML private Pane Summary;
    @FXML private PieChart pieChart = new PieChart() ;
    @FXML private PieChart pieChart1 = new PieChart();
    @FXML private Label inventory_value_label;
    @FXML private Label sales_order_count_label;
    @FXML private Label sales_value_label;
    @FXML private Label total_inventory_label;
    
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private ObservableList<PieChart.Data> piechartDatas;
    private ObservableList<PieChart.Data> piechartDatas1;

    

 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = DBConnection.InventoryManagementDatabase();
        setInvetoryLabel();
        setInventoryValueLabel();
        setSalesOrderCountLabel();
        setTotalSalesLabel();
        
        // laod data and generate first piechart
        loadDataFromDatabase();
        pieChart.setData(piechartDatas); 
        pieChart.setTitle("Total Inventory Value");

        // laod data and generate second piechart
        loadData();
        pieChart1.setData(piechartDatas1);
        pieChart1.setTitle("Total Physical Inventory");
        
    }

    private void setInventoryValueLabel(){
        try {
            // find all the record in Product
            String sql = "Select SUM(total_value) from Product";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                // put product description and product value into piechart
                inventory_value_label.setText("$" + rs.getString(1));
            }
        }
         catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setInvetoryLabel(){
        try {
            // find all the record in Product
            String sql = "Select SUM(inventory) from Product";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                // put product description and product value into piechart
                total_inventory_label.setText(rs.getString(1));
            }
        }
         catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setTotalSalesLabel(){
        try {
            // find all the record in Product
            String sql = "Select SUM(order_total) from OrderList";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                // put product description and product value into piechart
                if(rs.getDouble(1)==0){
                    sales_value_label.setText("$" + "0");
                }
                else{
                    sales_value_label.setText("$" + rs.getString(1));
                }
                
            }
        }
         catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setSalesOrderCountLabel(){
        try {
            // find all the record in Product
            String sql = "SELECT COUNT(order_number) from OrderList;";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                // put product description and product value into piechart
                sales_order_count_label.setText(rs.getString(1));
            }
        }
         catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
    // load data from piechart 1: prodcut_description and total value
    private void loadDataFromDatabase() {
        // otherwise data shown on table will be duplicated
        piechartDatas = FXCollections.observableArrayList();
        try {
            // find all the record in Product
            String sql = "Select * from Product";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                // put product description and product value into piechart
                piechartDatas.add(new PieChart.Data(rs.getString(2),+rs.getDouble(6)));
            }
        
        }
        
         catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // load data from piechart 1: prodcut_description and inventory
    private void loadData() {
        // adding data.clear() because we want to make sure tableview has nothing 
        // otherwise data shown on table will be duplicated
        piechartDatas1 = FXCollections.observableArrayList();
        try {
            // find all the record in Product
            String sql = "Select * from Product";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                // put product description and product value into piechart
                piechartDatas1.add(new PieChart.Data(rs.getString(2),+rs.getDouble(5)));
            }
        }
         catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    // nav bar function
    void loadWindow(String loc, ActionEvent event)throws IOException{
        Parent home_page = FXMLLoader.load(getClass().getResource(loc));
        Scene reportScene = new Scene(home_page, 1000, 600);
        Stage app = (Stage)((Node) event.getSource()).getScene().getWindow();
        app.setScene(reportScene);
        app.show(); 
    }
    
}
