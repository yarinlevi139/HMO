package com.example.myapplication.classes;

public class Message {
    private String id; // Add this line
    private String sender;
    private String receiver;
    private String senderEmail;
    private String receiverEmail;
    private String messageText;

    public Message() {
        // Default constructor required for Firestore
    }

    public Message(String sender, String receiver, String senderEmail, String receiverEmail, String messageText, String id) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.messageText = messageText;
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
