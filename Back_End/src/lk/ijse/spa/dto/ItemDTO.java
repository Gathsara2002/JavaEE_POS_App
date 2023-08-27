/**
 * @author : Gathsara
 * created : 8/10/2023 -- 6:29 PM
 **/

package lk.ijse.spa.dto;

public class ItemDTO {
    String code;
    String name;
    double price;
    double qty;

    public ItemDTO(String code, String name, double price, double qty) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public ItemDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}

