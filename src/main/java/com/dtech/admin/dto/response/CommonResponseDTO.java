/**
 * User: Himal_J
 * Date: 5/5/2025
 * Time: 7:00 AM
 * <p>
 */

package com.dtech.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class CommonResponseDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}
