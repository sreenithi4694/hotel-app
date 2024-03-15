package com.webapp.HotelWebAppBE.Models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Customer_Order")
public class Order {

    public String Id;
    public String Cust_id;
    public List<Product> order_Details;
    public String order_id;
}
