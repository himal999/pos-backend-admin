/**
 * User: Himal_J
 * Date: 4/27/2025
 * Time: 7:30 PM
 * <p>
 */

package com.dtech.admin.dto.response;

import lombok.Data;

@Data
public class AuthorizationTaskResponseDTO {
    private boolean add;
    private boolean update;
    private boolean view;
    private boolean search;
    private boolean delete;
    private boolean userRolePrivilegeAssign;
    private boolean passwordReset;
    private boolean fileUpload;
    private boolean financialApp;
    private boolean grnItem;
    private boolean grnCategory;
    private boolean grnSupplier;
}
