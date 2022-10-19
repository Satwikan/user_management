package com.example.user_management.appuser;

import com.example.user_management.registration.token.ConfirmationToken;
import com.example.user_management.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public UserDetails loadUserById(long id) throws UsernameNotFoundException {
        return appUserRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if (userExists) {
            throw new IllegalStateException("email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public AppUser updateUser(long userId, AppUser updatedAppUser) {
        try {
            System.out.println("id is "+userId);
            AppUser appUser = appUserRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + userId));
            if (updatedAppUser.getFirstName()!= null)
                appUser.setFirstName(updatedAppUser.getFirstName());
            if (updatedAppUser.getLastName()!= null)
                appUser.setLastName(updatedAppUser.getLastName());
            if (updatedAppUser.getEmail()!= null)
                appUser.setEmail(updatedAppUser.getEmail());
            if (updatedAppUser.getPassword()!= null) {
                String encodedPassword = bCryptPasswordEncoder.encode(updatedAppUser.getPassword());
                appUser.setPassword(encodedPassword);
            }
            appUserRepository.save(appUser);
            return ResponseEntity.ok(appUser).getBody();
        }
        catch (
                ResourceNotFoundException e) {
            return null;
        }
    }
}
