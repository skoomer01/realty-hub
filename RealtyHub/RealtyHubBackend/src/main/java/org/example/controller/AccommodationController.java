package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.business.impl.AccommodationManager;
import org.example.configuration.security.isauthenticated.IsAuthenticated;
import org.example.domain.*;
import org.example.domain.classes.Accommodation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/accommodations")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class AccommodationController {
    private final AccommodationManager accommodationManager;

    @GetMapping
    public ResponseEntity<GetAllAccommodationsResponse> getAllAccommodations() {
        return ResponseEntity.ok(accommodationManager.getAllAccommodations());
    }

    @GetMapping("/owned")
    public ResponseEntity<GetAllAccommodationsResponse> getAllOwnedAccommodations() {
        return ResponseEntity.ok(accommodationManager.getAllOwnedAccommodations());
    }
    @GetMapping("/active")
    public ResponseEntity<GetAllAccommodationsResponse> getAllActiveAccommodations() {
        return ResponseEntity.ok(accommodationManager.getAllActiveAccommodations());
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_REALTOR"})
    @PostMapping("/create")
    public ResponseEntity<CreateAccommodationResponse> createAccommodation(@RequestBody @Valid CreateAccommodationRequest request) {
        CreateAccommodationResponse response = accommodationManager.createAccommodation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_CUSTOMER"})
    @PostMapping("/purchase/{id}")
    public void purchaseAccommodation(@PathVariable(value = "id") final long id) {
        accommodationManager.purchaseAccommodation(id);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Accommodation> getAccommodation(@PathVariable(value = "id") final long id) {
        final Optional<Accommodation> accommodationOptional = accommodationManager.getAccommodation(id);
        return accommodationOptional.map(accommodation -> ResponseEntity.ok().body(accommodation)).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @IsAuthenticated
    @RolesAllowed({"ROLE_ADMIN","ROLE_REALTOR"})
    @DeleteMapping("/{accommodationId}")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable int accommodationId) {
        accommodationManager.deleteAccommodation(accommodationId);
        return ResponseEntity.noContent().build();
    }
    @IsAuthenticated
    @RolesAllowed({"ROLE_REALTOR"})
    @PutMapping("/update")
    public ResponseEntity<Void> updateAccommodation(@RequestBody @Valid UpdateAccommodationRequest request) {
        accommodationManager.updateAccommodation(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activeCount")
    public int getActiveAccommodationsCount() {
        return accommodationManager.countActiveAccommodations();
    }

    @GetMapping("/count")
    public int getAccommodationsCount() {
        return accommodationManager.countAccommodations();
    }
}
