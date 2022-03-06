package io.stacknix.merlin.db.networking;

public class JsonRPCException extends Exception{

    public int code;
    public String message;

    public JsonRPCException(int code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }
}
