package com.theerthesh.moneyManager.service;

import com.theerthesh.moneyManager.entity.ProfileEntity;
import com.theerthesh.moneyManager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {
   private  final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       ProfileEntity existingEntity =profileRepository.findByEmail(email)
               .orElseThrow( () ->new UsernameNotFoundException("profilr not found wit the email "+email));


        return User.builder()
                .username(existingEntity.getEmail())
                .password(existingEntity.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
