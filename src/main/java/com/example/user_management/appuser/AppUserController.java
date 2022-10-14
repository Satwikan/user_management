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

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @GetMapping(path = "{userId}")
    public Optional<AppUser> getUser(@PathVariable("id") long aid) {
        return appUserRepository.findById(aid);
    }
    @PostMapping(consumes = {"application/json"})
    public AppUser addUser(@RequestBody AppUser alien) {
        appUserRepository.save(alien);
        return alien;
    }
    @PutMapping(consumes = {"application/json"})
    public AppUser updateAlien(@RequestBody AppUser alien) {
        appUserRepository.save(alien);
        return alien;
    }
    @DeleteMapping("{id}")
    public AppUser deleteAlien(@PathVariable long id) {
        AppUser a = appUserRepository.findById(id).orElse(new AppUser());
        appUserRepository.delete(a);
        return a;
    }
}
