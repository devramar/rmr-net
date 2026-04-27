package dev.ramar.net;

import dev.ramar.structs.Either;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import dev.ramar.json.*;


public class NetClient
{

    private final HttpClient client;

    public NetClient()
    {
        this(HttpClient.newHttpClient());
    }

    public NetClient(HttpClient client)
    {
        this.client = client;
    }



    public <T> Either<HttpException, HttpResponse<T>> tryHandle(HttpRequest req, HttpResponse.BodyHandler<T> handler)
    { return Either.TryChecked(() -> this.handle(req, handler)); }

    public <T> HttpResponse<T> handle(HttpRequest req, HttpResponse.BodyHandler<T> handler) throws HttpException
    {
        return NetUtil.HandleRequest(this.client, req, handler);
    }


    public Either<HttpException, String> tryHandleString(HttpRequest req)
    { return Either.TryChecked(() -> this.handleString(req)); }

    public String handleString(HttpRequest req) throws HttpException
    {
        return NetUtil.HandleRequest_String(this.client, req);
    }



    public Either<HttpException, byte[]> tryHandleBytes(HttpRequest req)
    { return Either.TryChecked(() -> this.handleBytes(req)); }

    public byte[] handleBytes(HttpRequest req) throws HttpException
    {
        return NetUtil.HandleRequest_Bytes(this.client, req);
    }


    public Either<HttpException, JToken> tryHandleJSON(HttpRequest req)
    { return Either.TryChecked(() -> this.handleJSON(req)); }

    public JToken handleJSON(HttpRequest req) throws HttpException
    {
        return NetUtil.HandleRequest_JToken(this.client, req);
    }

    public Either<HttpException, JITJObject> tryHandleJObject(HttpRequest req)
    { return Either.TryChecked(() -> this.handleJObject(req)); }

    public JITJObject handleJObject(HttpRequest req) throws HttpException
    {
        return NetUtil.HandleRequest_JObject(this.client, req);
    }

    public Either<HttpException, JITJArray> tryHandleJArray(HttpRequest req)
    { return Either.TryChecked(() -> this.handleJArray(req)); }

    public JITJArray handleJArray(HttpRequest req) throws HttpException
    {
        return NetUtil.HandleRequest_JArray(this.client, req);
    }
}