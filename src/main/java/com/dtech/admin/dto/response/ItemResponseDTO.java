package com.dtech.admin.dto.response;

import com.dtech.admin.dto.SimpleBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDTO extends CommonResponseDTO {
    private Long id;
    private String code;
    private String description;
    private String status;
    private String statusDescription;
    private SimpleBaseDTO category;
    private SimpleBaseDTO brand;
    private String unit;
    private String unitDescription;
}
