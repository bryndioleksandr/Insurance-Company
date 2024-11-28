package org.company.insurance.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(
            summary = "Get user by ID",
            description = "Fetches user details by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User details fetched successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Create new user",
            description = "Creates a new user in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid user data")
            }
    )
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
    @Operation(
            summary = "Update user details",
            description = "Updates the details of an existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User details updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid user data"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public UserDto updateUserDetails(@Valid @RequestBody  UserDto userDto) {
        return userService.updateUserDetails(userDto);
    }


    @PutMapping("/assign-agent/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Assign agent to user",
            description = "Assigns an agent to a user, with an optional hire date and position",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Agent assigned successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public UserDto assignAgentToUser(
            @PathVariable("username") String username,
            @RequestParam(name = "hireDate", required = false) LocalDate hireDate,
            @RequestParam(name = "position") String position) {
        return userService.assignAgentToUser(username, hireDate, position);
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Delete user by ID",
            description = "Deletes a user from the system by their ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Get all users",
            description = "Fetches all users with pagination support",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of users fetched successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "204", description = "No users found")
            }
    )
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        Page<UserDto> userDtos = userService.getAllUsers(pageable);
        if (userDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Get sorted users",
            description = "Fetches users sorted by the specified field and order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sorted list of users fetched successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "204", description = "No users found")
            }
    )
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
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Get filtered users",
            description = "Fetches users based on provided filters (e.g., ID, birth date, phone number, etc.)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list of users fetched successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "No users found matching the filters")
            }
    )
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
