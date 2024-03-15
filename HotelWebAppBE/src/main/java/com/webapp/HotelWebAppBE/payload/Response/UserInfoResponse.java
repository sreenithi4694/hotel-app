package com.webapp.HotelWebAppBE.payload.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private String id;
    private String username;
    private String email;
    private List<String> roles;
    private String token;
}
