package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.business.impl.UserManager;
import org.example.configuration.security.isauthenticated.IsAuthenticated;
import org.example.domain.CreateUserRequest;
import org.example.domain.CreateUserResponse;
import org.example.domain.GetAllUsersResponse;
import org.example.domain.UpdateUserRequest;
import org.example.domain.classes.UserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class UserController {
    private final UserManager userManager;

    @CrossOrigin(origins = "https://localhost:8080.com")
    @GetMapping(path = "/users")
    public String homeInit(Model model){return "home";}

    @IsAuthenticated
    @RolesAllowed({"ROLE_ADMIN","ROLE_CUSTOMER","ROLE_REALTOR"})
    @GetMapping("{id}")
    public ResponseEntity<UserInfo> getUser(@PathVariable(value = "id") final long id) {
        final Optional<UserInfo> userInfoOptional = userManager.getUser(id);
        return userInfoOptional.map(userInfo -> ResponseEntity.ok().body(userInfo)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getAllUsers( ) {
        return ResponseEntity.ok(userManager.getAllUsers());
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_ADMIN"})
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userManager.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/customer")
    public ResponseEntity<CreateUserResponse> createCustomer(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = userManager.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/realtor")
    public ResponseEntity<CreateUserResponse> createRealtor(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = userManager.createRealtor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RolesAllowed({"ROLE_REALTOR", "ROLE_CUSTOMER"})
    @PutMapping("{id}")
    public ResponseEntity<UserInfo> updateUser(@PathVariable("id") long id, @RequestBody @Valid UpdateUserRequest request) {
        request.setId(id);
        userManager.updateUser(request);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @PutMapping("deactivate/{id}")
    public ResponseEntity<UserInfo> deactivateUser(@PathVariable("id") long id) {
        userManager.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

}
