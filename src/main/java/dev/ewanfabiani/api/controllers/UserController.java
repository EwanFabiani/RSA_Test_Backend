package dev.ewanfabiani.api.controllers;

import com.google.gson.Gson;
import dev.ewanfabiani.api.data.User;
import dev.ewanfabiani.api.models.UserModel;
import dev.ewanfabiani.database.tables.UserTable;
import dev.ewanfabiani.exceptions.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody UserModel userCreationModel) {
        try {
            //VERIFY THE KEY && MAX USERNAME LENGTH
            User newUser = userCreationModel.toUser();
            UserTable userTable = new UserTable();
            userTable.createUser(newUser.getUsername(), newUser.getModulus(), newUser.getExponent());
            System.out.println("Created user: " + newUser.getUsername());
            System.out.println("Modulus: " + newUser.getModulus());
            System.out.println("Exponent: " + newUser.getExponent());
            return new ResponseEntity<>("Account created", HttpStatus.OK);
        } catch (DatabaseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/get_user")
    public ResponseEntity<String> getUser(@RequestBody UserModel userModel) {
        try {
            UserTable userTable = new UserTable();
            User user = userTable.getUser(userModel.getUsername());
            String json = new Gson().toJson(user);
            System.out.println("Got user: " + user.getUsername());
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (DatabaseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
