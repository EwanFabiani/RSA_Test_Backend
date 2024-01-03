package dev.ewanfabiani.api.data;

public class FullEncryptedMessage {

    private String message;
    private String signature;
    private String sender;
    private String receiver;
    private long timestamp;

    public FullEncryptedMessage(String message, String signature, String sender, String receiver, long timestamp) {
        this.message = message;
        this.signature = signature;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    public String getData() {
        return message;
    }

    public String getSignature() {
        return signature;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
