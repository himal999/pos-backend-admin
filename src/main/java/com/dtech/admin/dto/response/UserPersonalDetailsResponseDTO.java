package com.dtech.admin.dto.response;

import lombok.Data;

@Data
public class UserPersonalDetailsResponseDTO {
    private String initials;
    private String firstName;
    private String lastName;
    private String nic;
}
