package dev.ewanfabiani.api.controllers;

import dev.ewanfabiani.api.SecurityService;
import dev.ewanfabiani.api.models.ChallengeSolveModel;
import dev.ewanfabiani.database.tables.AuthTable;
import dev.ewanfabiani.exceptions.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @GetMapping("/challenge/{username}")
    public ResponseEntity<String> getChallenge(@PathVariable String username) {
        System.out.println("Creating challenge for: " + username);
        try {
            SecurityService securityService = new SecurityService();
            String challenge = securityService.generateChallenge();
            AuthTable authTable = new AuthTable();
            try {
                authTable.deleteToken(username);
            } catch (DatabaseException ignored) {}
            authTable.createToken(username, challenge);
            System.out.println("Created challenge: " + challenge);
            System.out.println("Created token for: " + username);
            return new ResponseEntity<>(challenge, HttpStatus.OK);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
