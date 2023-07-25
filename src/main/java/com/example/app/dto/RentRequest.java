package com.example.app.dto;

import com.example.app.model.Material;

public class RentRequest {

    private Material material;
    private Long quantity;

    public RentRequest(Material material, Long quantity) {
        this.material = material;
        this.quantity = quantity;
    }

    public RentRequest() {
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "RentRequest{" +
                "material=" + material +
                ", quantity=" + quantity +
                '}';
    }
}
