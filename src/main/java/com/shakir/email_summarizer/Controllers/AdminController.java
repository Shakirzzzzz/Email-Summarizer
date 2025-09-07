package com.shakir.email_summarizer.Controllers;

import com.shakir.email_summarizer.Response.UserResponse;
import com.shakir.email_summarizer.Service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Admin Actions Endpoints", description = "Operations that current logged in Admin can Perform")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Fetch All users", description = "Retrieve all the Users in the Database")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public List<UserResponse> getAllUsers(){
        return adminService.getAllUsers();
    }

    @Operation(summary = "Promote user to admin", description = "Promote a users role to admin")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/role")
    public UserResponse promoteToAdmin(@PathVariable @Min(1) long id){
        return adminService.promoteToAdmin(id);
    }

    @Operation(summary = "Delete user",description = "Delete a non Admin User from the database")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Min(1) long userId){
        adminService.deleteNonAdminUser(userId);
    }



}
