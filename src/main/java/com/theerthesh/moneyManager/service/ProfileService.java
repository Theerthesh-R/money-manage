package com.theerthesh.moneyManager.service;

import com.theerthesh.moneyManager.dto.AuthDTO;
import com.theerthesh.moneyManager.dto.ProfileDto;
import com.theerthesh.moneyManager.entity.ProfileEntity;
import com.theerthesh.moneyManager.repository.ProfileRepository;

import com.theerthesh.moneyManager.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Value("${app.activation.url}")
    private String activationVariable;

public ProfileDto registerProfile(ProfileDto profileDto){
   ProfileEntity newProfile=toentity(profileDto);
   newProfile.setActivationToken(UUID.randomUUID().toString());
    newProfile=profileRepository.save(newProfile);
    String activattionLink= " "+activationVariable+"/activate?token=" + newProfile.getActivationToken();
    String subject="Activate your Money Manger account";
    String body ="Click on the following link to activate your account"+activattionLink;
    emailService.sendMail(newProfile.getEmail(),subject,body);
return todto(newProfile);

}

public ProfileEntity toentity(ProfileDto profileDto){
    return ProfileEntity.builder()
            .id(profileDto.getId())
            .email(profileDto.getEmail())
            .fullName(profileDto.getFullName())
            .password(passwordEncoder.encode(profileDto.getPassword()))
            .createdAt(profileDto.getCreatedAt())
            .profileImageUrl(profileDto.getProfileImageUrl())
            .updatedAt(profileDto.getUpdatedAt())

            .build();
}
    ProfileDto todto(ProfileEntity profileEntity){
    return ProfileDto.builder()
            .id(profileEntity.getId())
            .email(profileEntity.getEmail())
            .fullName(profileEntity.getFullName())
            .createdAt(profileEntity.getCreatedAt())
            .updatedAt(profileEntity.getUpdatedAt())
            .profileImageUrl(profileEntity.getProfileImageUrl())
            .build();
}
public boolean acivateProfile(String activationToken){

    return  profileRepository.findByActivationToken(activationToken)
            .map(profile->{
        profile.setIsActive(true);
        profileRepository.save(profile);
        return  true;
    }).orElse(false);
}
public boolean isAccountActive(String email){
    return  profileRepository.findByEmail(email)
            .map(ProfileEntity::getIsActive)
            .orElse(false);
}
public ProfileEntity getCurrentProfile(){
   Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
   return profileRepository.findByEmail(authentication.getName())
           .orElseThrow(()->new UsernameNotFoundException("profile not found with the email" + authentication.getName()) );
}

public ProfileDto getPublicProfile(String email){
    ProfileEntity currentUser;
    if(email==null){
        currentUser=getCurrentProfile();
    }
    else{
        currentUser=profileRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("not found by this email id" + email));
    }
    return ProfileDto.builder()
            .id(currentUser.getId())
            .fullName(currentUser.getFullName())
            .profileImageUrl(currentUser.getProfileImageUrl())
            .email(currentUser.getEmail())
            .createdAt(currentUser.getCreatedAt())
            .updatedAt(currentUser.getUpdatedAt())
            .build();
}

    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
    try{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getPassword()));
        String token = jwtUtil.generateToken(authDTO.getEmail());
        return Map.of("token",token,
                "use",getPublicProfile(authDTO.getEmail()));

    } catch (Exception e) {
        throw new RuntimeException("Invalid Email or Password");
    }
    }
}
