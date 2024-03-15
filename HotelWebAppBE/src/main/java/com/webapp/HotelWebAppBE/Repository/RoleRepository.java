package com.webapp.HotelWebAppBE.Repository;

import java.util.Optional;

import com.webapp.HotelWebAppBE.Models.Role;
import com.webapp.HotelWebAppBE.Models.ERole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
