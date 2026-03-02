package dev.ramar.net;

import java.net.URI;

import dev.ramar.json.JITJArray;
import dev.ramar.json.JITJObject;
import dev.ramar.json.JToken;
import dev.ramar.json.JValue;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface NetQuerier
{

	public HttpClient client();

    public String baseURL();

    public default HttpRequest.Builder baseBuilder()
    {
        return HttpRequest.newBuilder();
    }

    /* Request Building
    --===-----------------
    */

    public default HttpRequest.Builder request(String suburl)
    {
        String url = "";

        String base = this.baseURL();
        // ensure we add https://base/url and not https://base/url/
        if( !base.endsWith("/") )
            url += base;
        else
            url += base.substring(0, base.length() - 1);

        // ensure we add /suburl/ and not suburl/
        if( !suburl.startsWith("/") )
            suburl = "/" + suburl;

        url += suburl;

        return this.baseBuilder()
            .uri(URI.create(url))
            // .header("x-api-key", this.apiKey)
            // .header("Authorization", "Bearer " + this.getAuthToken())
        ;
    }

    public default HttpRequest.Builder patch(String suburl, String body)
    {
        return this.request(suburl)
            .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
        ;
    }

    public default HttpRequest.Builder post(String suburl, String body)
    {
        return this.request(suburl)
            .POST(HttpRequest.BodyPublishers.ofString(body))
        ;
    }

    public default HttpRequest.Builder post(String suburl, byte[] body)
    {
        return this.request(suburl)
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
        ;
    }

    public default HttpRequest.Builder get(String suburl)
    {
        return this.request(suburl)
            .GET()
        ;
    }

    public default HttpRequest.Builder put(String suburl, String body)
    {
        return this.request(suburl)
            .PUT(HttpRequest.BodyPublishers.ofString(body))
        ;
    }

    /* Request Processing
    --===-------------------
    */

    /// core request

    public default <T> HttpResponse<T> handle(HttpRequest req, HttpResponse.BodyHandler<T> handler) throws HttpException
    { return NetUtil.HandleRequest(this.client(), req, handler); }

    /// BASE types

    public default String handle_String(HttpRequest req) throws HttpException
    { return NetUtil.HandleRequest_String(this.client(), req); }

    public default String bluntHandle_String(HttpRequest req)
    { 
        try
        {
            return NetUtil.HandleRequest_String(this.client(), req);
        }
        catch(HttpException ex)
        {
            System.out.println("HttpException @ bluntHandle_String: " + ex.code);
            return "";
        }
    }

    public default byte[] handle_Bytes(HttpRequest req) throws HttpException
    { return NetUtil.HandleRequest_Bytes(this.client(), req); }

    public default byte[] bluntHandle_Bytes(HttpRequest req)
    { 
        try
        {
            return NetUtil.HandleRequest_Bytes(this.client(), req);
        }
        catch(HttpException ex)
        {
            System.out.println("HttpException @ bluntHandle_Bytes: " + ex.code);
            return null;
        }
    }

    /// JSON types

    public default JToken handle_JToken(HttpRequest req) throws HttpException
    { return NetUtil.HandleRequest_JToken(this.client(), req); }

    public default JToken bluntHandle_JToken(HttpRequest req)
    { 
        try
        {
            return NetUtil.HandleRequest_JToken(this.client(), req);
        }
        catch(HttpException ex)
        {
            System.out.println("HttpException @ bluntHandle_JToken: " + ex.code);
            return JValue.Nothing();
        }
    }

    public default JITJObject handle_JObject(HttpRequest req) throws HttpException
    { return NetUtil.HandleRequest_JObject(this.client(), req); }

    public default JITJObject bluntHandle_JObject(HttpRequest req)
    { 
        try
        {
            return NetUtil.HandleRequest_JObject(this.client(), req);
        }
        catch(HttpException ex)
        {
            System.out.println("HttpException @ bluntHandle_JObject: " + ex.code);
            return null;
        }
    }

    public default JITJArray handle_JArray(HttpRequest req) throws HttpException
    { return NetUtil.HandleRequest_JArray(this.client(), req); }

    public default JITJArray bluntHandle_JArray(HttpRequest req)
    { 
        try
        {
            return NetUtil.HandleRequest_JArray(this.client(), req);
        }
        catch(HttpException ex)
        {
            System.out.println("HttpException @ bluntHandle_JArray: " + ex.code);
            return null;
        }
    }

}