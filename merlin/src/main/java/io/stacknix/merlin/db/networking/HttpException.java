package io.stacknix.merlin.db.networking;

public class HttpException extends Exception{

    public int code;
    public String text;

    public HttpException(int code, String text){
        super(text);
        this.code = code;
        this.text = text;
    }

}
