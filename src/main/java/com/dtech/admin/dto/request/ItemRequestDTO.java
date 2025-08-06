package com.dtech.admin.dto.request;



import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemRequestDTO extends ChannelRequestDTO {
    private Long id;
    private String code;
    private String description;
    private String status;
    private String category;
    private String brand;
    private String unit;
}
