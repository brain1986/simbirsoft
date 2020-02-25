package ru.iprustam.trainee.simbirchat.handlers.handleresults;

public class HandleDetails {
    private String text;

    public HandleDetails(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}