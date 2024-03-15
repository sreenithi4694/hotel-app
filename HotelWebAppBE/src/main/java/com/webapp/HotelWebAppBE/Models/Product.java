package com.webapp.HotelWebAppBE.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Product_Data")
public class Product {
    public String Id;
    public String product_name;
    public String product_description;
    public String product_price;

}
