package com.esdo.bepilot.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private String id;
    private String avatar;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
}
