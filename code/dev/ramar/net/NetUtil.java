package dev.ramar.net;

import dev.ramar.json.JArray;
import dev.ramar.json.JITJArray;
import dev.ramar.json.JITJObject;
import dev.ramar.json.JObject;
import dev.ramar.json.JToken;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.IOException;
import java.io.UncheckedIOException;

import dev.ramar.json.*;

public class NetUtil
{

    public static MultipartBuilder MultipartRequest()
    {
        return new MultipartBuilder();
    }

   

    public static JToken ExceptionResponse(Exception ex)
    {
        return new JObject()
            .with("error", ex.getMessage())
            .with("trace", JArray.From(ExceptionUtil.TraceLinesAsStream(ex)))
        ;
    }

    public static <T> HttpResponse<T> HandleRequest(HttpClient client, HttpRequest req, HttpResponse.BodyHandler<T> handler) throws HttpException
    {
        try
        {
            HttpResponse<T> response = client.send(req, handler);
            if( response.statusCode() != 200 ) 
                throw new HttpException(response);

            return response;
        }
        catch(InterruptedException ex)
        {
            throw new RuntimeException("interrupted: " + ex.getMessage(), ex);
        }
        catch(HttpException ex)
        {
            throw new HttpException("failed to .HandleRequest: " + ex.code, ex);
        }
        catch(IOException ex)
        {
            throw new UncheckedIOException("IOException on .HandleRequest: " + ex.getMessage(), ex);
        }
    }

    public static String HandleRequest_String(HttpClient client, HttpRequest req) throws HttpException
    {
        HttpResponse<String> res = HandleRequest(client, req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }

    public static byte[] HandleRequest_Bytes(HttpClient client, HttpRequest req) throws HttpException
    {
        HttpResponse<byte[]> res = HandleRequest(client, req, HttpResponse.BodyHandlers.ofByteArray());
        return res.body();
    }

    public static JToken HandleRequest_JToken(HttpClient client, HttpRequest req) throws HttpException
    {
        HttpResponse<String> res = HandleRequest(client, req, HttpResponse.BodyHandlers.ofString());

        return JToken.JITFromString(res.body());
    }

    public static JITJObject HandleRequest_JObject(HttpClient client, HttpRequest req) throws HttpException
    {
        HttpResponse<String> res = HandleRequest(client, req, HttpResponse.BodyHandlers.ofString());

        return JITJObject.FromString(res.body());
    }

    public static JITJArray HandleRequest_JArray(HttpClient client, HttpRequest req) throws HttpException
    {
        HttpResponse<String> res = HandleRequest(client, req, HttpResponse.BodyHandlers.ofString());

        return JITJArray.FromString(res.body());
    }

}