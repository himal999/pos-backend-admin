package com.dtech.admin.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class UserCompanyDetailsRequestDTO {
    private String companyTypeCode;
    private String staffCategoryCode;
    private String staffTypeCode;
    private String designation;
    private Date permanentDate;
    private Date terminateDate;
    private String insurancePolicyCode;
    private String facility;
}
