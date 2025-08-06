/**
 * User: Himal_J
 * Date: 2/28/2025
 * Time: 12:37 PM
 * <p>
 */

package com.dtech.admin.mapper.entityToDto;


import com.dtech.admin.dto.response.DocumentDownloadResponseDTO;
import com.dtech.admin.dto.response.UserCommonResponseDTO;
import com.dtech.admin.dto.response.UserDetailsResponseDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.WebUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

@Log4j2
@RequiredArgsConstructor
public class SystemUserMapper {
   private final static ModelMapper modelMapper = new ModelMapper();
    public static UserDetailsResponseDTO mapUserDetailsForLogin(WebUser webUser) {
        try {
            log.info("user profile mapper start for login");

            UserDetailsResponseDTO userDetailsResponseDTO = modelMapper.map(webUser, UserDetailsResponseDTO.class);
            userDetailsResponseDTO.setStatusDescription(Status.valueOf(userDetailsResponseDTO.getStatus()).getDescription());

            if (webUser.getProfileImg() != null) {
                log.info("user get profile img");
                DocumentDownloadResponseDTO documentDownloadResponseDTO =
                        modelMapper.map(webUser.getProfileImg(), DocumentDownloadResponseDTO.class);
                log.info("user get profile img downloaded");
            }

            log.info("Success profile mapper for login {} ", userDetailsResponseDTO);
            return userDetailsResponseDTO;

        } catch (Exception e) {
            log.error("Error mapping user details for login", e);
            throw e;
        }
    }

    public static UserCommonResponseDTO mapUserDetails(WebUser webUser) {
        try {

            log.info("user profile mapper start");

            UserCommonResponseDTO commonResponseDTO = modelMapper.map(webUser, UserCommonResponseDTO.class);
            commonResponseDTO.setStatusDescription(Status.valueOf(commonResponseDTO.getStatus()).getDescription());
            commonResponseDTO.setLoginStatus(Status.valueOf(commonResponseDTO.getLoginStatus()).getDescription());
            commonResponseDTO.setNewUsername(commonResponseDTO.getUsername());

            log.info("Success profile mapper {} ", commonResponseDTO);
            return commonResponseDTO;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

}
