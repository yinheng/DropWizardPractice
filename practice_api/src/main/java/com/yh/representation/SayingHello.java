package com.yh.representation;


public class SayingHello {
    private long id;
    private String content;

    public SayingHello() {}

    public SayingHello(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
