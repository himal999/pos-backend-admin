package com.dtech.admin.dto.response;

import lombok.Data;

@Data
public class ApplicationUserResponseDTO {
    private String username;
    private UserPersonalDetailsResponseDTO userPersonalDetails;
}
