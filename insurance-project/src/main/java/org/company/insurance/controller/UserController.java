package org.company.insurance.controller;


import lombok.AllArgsConstructor;
import org.company.insurance.dto.UserCreationDto;
import org.company.insurance.dto.UserDto;
import org.company.insurance.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreationDto userCreationDto) {
        return ResponseEntity.ok(userService.createUser(userCreationDto));
    }

//    @PutMapping
//    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
//        return ResponseEntity.ok(userService.updateUser(userDto));
//    }
//
//    @PutMapping
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<UserDto> updateUserDetails(@RequestBody UserDto userDto) {
//        return ResponseEntity.ok(userService.updateUserDetails(userDto));
//    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    public UserDto updateUserDetails(@RequestBody  UserDto userDto) {
        return userService.updateUserDetails(userDto);
    }


    @PutMapping("/assign-agent/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto assignAgentToUser(
            @PathVariable("username") String username,
            @RequestParam(name = "hireDate", required = false) LocalDate hireDate,
            @RequestParam(name = "position") String position) {
        return userService.assignAgentToUser(username, hireDate, position);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        Page<UserDto> userDtos = userService.getAllUsers(pageable);
        if (userDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedUsers(
            @RequestParam String sortBy,
            @RequestParam String order,
            @PageableDefault Pageable pageable) {
        Page<UserDto> userDtos = userService.getSortedUsers(sortBy, order, pageable);
        if (userDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredUsers(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "birthDate", required = false) LocalDate birthDate,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "surname", required = false) String surname,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "role", required = false) String role,
            @PageableDefault Pageable pageable) {
        Page<UserDto> userDtos = userService.getFilteredUsers(id, birthDate, phoneNumber, firstName, surname, email, role, pageable);

        if (userDtos.isEmpty()) {
            return new ResponseEntity<>("No users found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }
}
