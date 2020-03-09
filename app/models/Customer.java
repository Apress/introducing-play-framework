package models;

import controllers.Order;

import java.util.List;

public class Customer {

    private long id;
    private String name;
    private boolean loyaltyMember;
    private boolean isActive;
    private List<Order> orders;

    public List<Order> getOrders() {
        return null;
    }
    public boolean deactivate() {
        //Logic and validation to deactivate a customer
        return false;
    }
}
