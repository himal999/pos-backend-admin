/**
 * User: Himal_J
 * Date: 4/29/2025
 * Time: 11:44 AM
 * <p>
 */

package com.dtech.admin.dto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonResponseDTO extends com.dtech.admin.dto.response.CommonResponseDTO {
    private Long id;
    private String code;
    private String description;
    private String status;
    private String statusDescription;
}
