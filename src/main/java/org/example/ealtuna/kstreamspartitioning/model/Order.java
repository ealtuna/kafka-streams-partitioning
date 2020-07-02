package org.example.ealtuna.kstreamspartitioning.model;

public class Order {
    private String id;
    private String productId;
    private int amount;

    public Order() {
    }

    public Order(String id, String productId, int amount) {
        this.id = id;
        this.productId = productId;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
