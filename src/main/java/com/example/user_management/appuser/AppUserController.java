package com.example.user_management.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/user")
public class AppUserController {
    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @GetMapping(path = "{userId}")
    public Optional<AppUser> getUser(@PathVariable("id") long aid) {
        return appUserRepository.findById(aid);
    }
    @PostMapping(consumes = {"application/json"})
    public AppUser addUser(@RequestBody AppUser appUser) {
        appUserRepository.save(appUser);
        return appUser;
    }
    @PutMapping(path = "{userId}", consumes = {"application/json"})
    public AppUser updateAlien(@PathVariable long userId,@RequestBody AppUser updatedAppUser) {
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
            if (updatedAppUser.getPassword()!= null)
                appUser.setPassword(updatedAppUser.getPassword());
            appUserRepository.save(appUser);
            return ResponseEntity.ok(appUser).getBody();
        }
        catch (
                ResourceNotFoundException e) {
            return null;
        }
    }
    @DeleteMapping("{id}")
    public AppUser deleteAlien(@PathVariable long id) {
        AppUser a = appUserRepository.findById(id).orElse(new AppUser());
        appUserRepository.delete(a);
        return a;
    }
}
