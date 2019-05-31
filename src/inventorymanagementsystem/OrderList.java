/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

/**
 *this class is used to generate orders that will used in SalesControllerClass
 * @author antho
 */

public class OrderList {
    private String orderNumber;
    private String itemNumber;
    private String customer;
    private String orderDate;
    private String price;
    private String quantity;
    private String orderTotal;

    public OrderList(String orderNumber, String itemNumber, String customer, String orderDate, String price, String quantity, String orderTotal) {
        this.orderNumber = orderNumber;
        this.itemNumber = itemNumber;
        this.customer = customer;
        this.orderDate = orderDate;
        this.price = price;
        this.quantity = quantity;
        this.orderTotal = orderTotal;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }
    
}
