package com.webapp.HotelWebAppBE.Repository;

import com.webapp.HotelWebAppBE.Models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, Integer>{
    Optional<Customer> findByUsername(String username);

    Boolean existsByUsername(String usernmae);

    Boolean existsByEmail(String email);
}

