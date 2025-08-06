/**
 * User: Himal_J
 * Date: 4/30/2025
 * Time: 10:31 AM
 * <p>
 */

package com.dtech.admin.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SystemUserRequestDTO extends ChannelRequestDTO {
    private Long id;
    private String newUsername;
    private String userRole;
    private String nic;
    private String email;
    private String mobile;
    private String firstName;
    private String lastName;
    private String status;
}
