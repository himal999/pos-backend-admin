/**
 * User: Himal_J
 * Date: 2/25/2025
 * Time: 3:40 PM
 * <p>
 */

package com.dtech.admin.dto.response;


import lombok.Data;

@Data
public class DocumentDownloadResponseDTO {
    private String type;
    private String file;
    private String fileName;
    private String fileExtensiones;
}
