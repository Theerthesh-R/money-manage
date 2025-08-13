package com.theerthesh.moneyManager.contrpller;

import com.theerthesh.moneyManager.dto.AuthDTO;
import com.theerthesh.moneyManager.dto.ProfileDto;
import com.theerthesh.moneyManager.entity.ProfileEntity;
import com.theerthesh.moneyManager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ProfileController {
private final ProfileService profilrService;
    @PostMapping("/register")
    public ResponseEntity<ProfileDto> addregister(@RequestBody ProfileDto profileDto){
    return ResponseEntity.status(HttpStatus.CREATED).body(profilrService.registerProfile(profileDto));
    }


    @GetMapping("/activate")
    public ResponseEntity<String>activavteProfile(String token){
        boolean isActivated=profilrService.acivateProfile(token);
        if(isActivated){
            return  ResponseEntity.ok("Profile activatetd successfully");

        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation Token Not found or already used");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO){
        try {
            if(!profilrService.isAccountActive(authDTO.getEmail())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","the email is not acivated please activate"));
            }
            else{
                 Map<String,Object> response=profilrService.authenticateAndGenerateToken(authDTO);
                 return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()));
        }
    }


}
