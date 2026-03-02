package dev.ramar.net;

import dev.ramar.json.JITJObject;
import dev.ramar.json.JObject;

import java.net.http.HttpResponse;

import dev.ramar.json.*;

public class HttpException extends NetException
{
    private static String MessageOf(HttpResponse<String> response)
    {
        JObject obj = JITJObject.FromString(response.body());
        if( obj == null || obj.isEmpty() )
            return "";

        String error = obj.get("error").tryString();
        if( error != null )
            return error;

        return "";
    }

    public String message()
    {
        return new JObject()
            .with("error", this.getMessage())
            .toString()
        ;
    }

    public HttpException()
    { 
        super(); 
        this.code = -1;
    }

    public HttpException(Exception inner)
    { 
        super(inner);
        if( inner instanceof HttpException )
            this.code = ((HttpException)inner).code;
        else
            this.code = -1;
    }

    public HttpException(String msg, Exception inner)
    { 
        super(msg, inner); 
        if( inner instanceof HttpException )
            this.code = ((HttpException)inner).code;
        else
            this.code = -1;
    }

    public HttpException(int code, String msg)
    {
        super(msg, null);
        this.code = code;
    }

    public HttpException(int code, String msg, Exception inner)
    {
        super(msg, null);
        this.code = code;
    }

    public HttpException(int code)
    {
        this.code = code;
    }

    public HttpException(HttpResponse<String> response)
    {
        super(MessageOf(response), null);
        this.code = response.statusCode();
    }

    public final int code;
    public int getStatusCode() 
    { return this.code; }

    public String errorResponse()
    {
        return NetResponses.Exception(this).toString();
    }
}