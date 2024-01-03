package dev.ewanfabiani.api.data;

public class EncryptedMessage {

    private String data;
    private String signature;
    private String sender;
    private String receiver;

    public String getData() {
        return data;
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
}
