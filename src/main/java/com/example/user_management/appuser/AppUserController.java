package com.example.user_management.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/user")
public class AppUserController {
    @Autowired
    private AppUserRepository appUserRepository;

    private AppUserService appUserService;

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
    public AppUser updateUser(@PathVariable long userId, @RequestBody AppUser updatedAppUser) {
        return appUserService.updateUser(userId,updatedAppUser);
    }
    @DeleteMapping("{id}")
    public AppUser deleteUser(@PathVariable long id) {
        AppUser a = appUserRepository.findById(id).orElse(new AppUser());
        appUserRepository.delete(a);
        return a;
    }
}
