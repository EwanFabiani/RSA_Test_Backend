package dev.ewanfabiani.api.controllers;

import com.google.gson.Gson;
import dev.ewanfabiani.api.SecurityService;
import dev.ewanfabiani.api.data.EncryptedMessage;
import dev.ewanfabiani.api.data.FullEncryptedMessage;
import dev.ewanfabiani.api.models.ChatModel;
import dev.ewanfabiani.database.tables.MessageTable;
import dev.ewanfabiani.exceptions.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody EncryptedMessage encryptedMessage) {
        try {
            SecurityService securityService = new SecurityService();
            boolean isMessageValid = securityService.verifyMessage(encryptedMessage.getData(), encryptedMessage.getSignature(), encryptedMessage.getSender());
            if (!isMessageValid) {
                System.out.println("Invalid signature!");
                return new ResponseEntity<>("Invalid signature", HttpStatus.BAD_REQUEST);
            }else {
                MessageTable messageTable = new MessageTable();
                System.out.println("Message from " + encryptedMessage.getSender() + " to " + encryptedMessage.getReceiver() + " is valid");
                System.out.println("Message: " + encryptedMessage.getData());
                messageTable.storeMessage(encryptedMessage.getData(), encryptedMessage.getSender(), encryptedMessage.getReceiver(), encryptedMessage.getSignature());
                System.out.println("Stored message from " + encryptedMessage.getSender() + " to " + encryptedMessage.getReceiver());
                return new ResponseEntity<>("Message stored", HttpStatus.OK);
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    @PostMapping("/get_chat")
    public ResponseEntity<String> getNewChat(@RequestBody ChatModel chatModel) {
        System.out.println("Getting chat between " + chatModel.getSender() + " and " + chatModel.getRecipient());
        System.out.println("Challenge: " + chatModel.getChallengeSolve());
        try {
            SecurityService securityService = new SecurityService();
            System.out.println("Verifying challenge for " + chatModel.getRecipient());
            if (!securityService.verifyChallenge(chatModel.getRecipient(), chatModel.getChallengeSolve())) {
                return new ResponseEntity<>("Invalid challenge", HttpStatus.BAD_REQUEST);
            }
            MessageTable messageTable = new MessageTable();
            ArrayList<FullEncryptedMessage> messages = messageTable.getUndeliveredMessages(chatModel.getSender(), chatModel.getRecipient());
            messageTable.markMessagesAsDelivered(chatModel.getSender(), chatModel.getRecipient());
            System.out.println("Got chat between " + chatModel.getSender() + " and " + chatModel.getRecipient());
            String json = new Gson().toJson(messages);
            System.out.println(json);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

    @PostMapping("/get_all")
    public ResponseEntity<String> getAllUnread(@RequestBody ChatModel chatModel) {
        System.out.println("Getting all unread messages for " + chatModel.getUser());
        System.out.println("Challenge: " + chatModel.getChallengeSolve());
        try {
            SecurityService securityService = new SecurityService();
            System.out.println("Verifying challenge for " + chatModel.getUser());
            if (!securityService.verifyChallenge(chatModel.getUser(), chatModel.getChallengeSolve())) {
                return new ResponseEntity<>("Invalid challenge", HttpStatus.BAD_REQUEST);
            }
            MessageTable messageTable = new MessageTable();
            ArrayList<FullEncryptedMessage> messages = messageTable.getAllUndeliveredMessages(chatModel.getUser());
            messageTable.markAllMessagesAsDelivered(chatModel.getUser());
            System.out.println("Got all unread messages for " + chatModel.getUser());
            String json = new Gson().toJson(messages);
            System.out.println(json);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
