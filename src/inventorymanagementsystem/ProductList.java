/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

/**
 *this class is used to generate product that will used in ProductControllerClass
 * @author antho
 */



public class ProductList {
    private String itemNumber;
    private String productDescription;
    private String vendor;
    private String cost;
    private String inventory;
    private String totalValue;

  
    public ProductList(String item_number, String product_description, String vendor, String cost, String inventory, String totalValue) {
        this.itemNumber = item_number;
        this.productDescription = product_description;
        this.vendor = vendor;
        this.cost = cost;
        this.inventory = inventory;
        this.totalValue = totalValue;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }


    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

}
