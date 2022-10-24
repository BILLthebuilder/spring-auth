package com.unknown.controller;

import com.unknown.dto.CreateUserRequest;
import com.unknown.dto.GenericResponse;
import com.unknown.dto.LoginUserRequest;
import com.unknown.dto.UpdateUserRequest;
import com.unknown.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<GenericResponse> signup(@RequestBody @Valid CreateUserRequest request){
        GenericResponse response;
        try {
            userService.create(request);
            response = new GenericResponse("User signed up successfully","SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception ex){
            response = new GenericResponse("Sign up failed","FAILED");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("login")
    public ResponseEntity<GenericResponse> login(@RequestBody @Valid LoginUserRequest request){
        GenericResponse response;
        try {
            var token = userService.login(request);
            response = new GenericResponse("User logged in successfully", "SUCCESS");
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(response);
        }catch (Exception ex){
            response = new GenericResponse("Login failed", "FAILED");
            return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<GenericResponse> update(@RequestBody @Valid UpdateUserRequest request,@PathVariable long id){
        GenericResponse response;
        try{
           userService.update(id,request);
           response = new GenericResponse("User updated successfully","SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception ex){
            response = new GenericResponse("update failed","FAILED");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable long id){
        GenericResponse response;
        try{
            userService.delete(id);
            response = new GenericResponse("User deleted successfully","SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception ex){
            response = new GenericResponse("deleting failed","FAILED");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
