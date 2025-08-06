/**
 * User: Himal_J
 * Date: 5/2/2025
 * Time: 2:45 PM
 * <p>
 */

package com.dtech.admin.dto.response;

import com.dtech.admin.dto.SimpleBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserCommonResponseDTO extends CommonResponseDTO {
    private Long id;
    private String username;
    private String newUsername; // for system user page
    private String email;
    private String mobile;
    private String status;
    private String statusDescription;
    private String loginStatus;
    private String loginStatusDescription;
    private SimpleBaseDTO userRole;
    private String firstName;
    private String lastName;
    private String nic;
}
