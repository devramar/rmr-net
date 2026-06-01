package dev.ramar.net;

import dev.ramar.json.JToken;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

public class AddressScope
{

    public AddressScope(URI base)
    {
        this.base = EnsureTrailingSlash(base);
    }

    private final URI base;


    public URI base()
    {
        return this.base;
    }



    public URI uri(Object... subpath)
    {
        String path = JoinPath(subpath);

        if(path.isBlank())
            return this.base;

        return this.base.resolve(path);
    }



    public HttpRequest.Builder suburl(Object... subpath)
    {
        return HttpRequest
            .newBuilder(this.uri(subpath))
        ;
    }



    public HttpRequest.Builder get(Object... subpath)
    {
        return this.suburl(subpath)
            .GET()
        ;
    }



    public HttpRequest.Builder post(Object... subpath)
    {
        return this.suburl(subpath)
            .POST(HttpRequest.BodyPublishers.noBody())
        ;
    }



    public HttpRequest.Builder post(HttpRequest.BodyPublisher body, Object... subpath)
    {
        return this.suburl(subpath)
            .POST(body)
        ;
    }


    public HttpRequest.Builder patch(HttpRequest.BodyPublisher body, Object... subpath)
    {
        return this.suburl(subpath)
            .method("PATCH", body)
        ;
    }



    public HttpRequest.Builder patch(String body, Object... subpath)
    {
        return this.patch(HttpRequest.BodyPublishers.ofString(body), subpath);
    }


    public HttpRequest.Builder put(HttpRequest.BodyPublisher body, Object... subpath)
    {
        return this.suburl(subpath)
            .PUT(body)
        ;
    }



    public HttpRequest.Builder delete(Object... subpath)
    {
        return this.suburl(subpath)
            .DELETE()
        ;
    }



    public HttpRequest.Builder method(String method, HttpRequest.BodyPublisher body, Object... subpath)
    {
        return this.suburl(subpath)
            .method(method, body)
        ;
    }



    public HttpRequest.Builder postJSON(JToken json, Object... subpath)
    {
        String jsonString = "";
        if( json != null )
            jsonString = json.toString();

        return this.postJSON(jsonString, subpath);
    }

    public HttpRequest.Builder postJSON(String json, Object... subpath)
    {
        return this.post(HttpRequest.BodyPublishers.ofString(json), subpath)
            .header("Content-Type", "application/json")
        ;
    }



    public HttpRequest.Builder patchJSON(JToken json, Object... subpath)
    {
        String jsonString = "";
        if( json != null )
            jsonString = json.toString();

        return this.patchJSON(jsonString, subpath);
    }

    public HttpRequest.Builder patchJSON(String json, Object... subpath)
    {
        return this.patch(HttpRequest.BodyPublishers.ofString(json), subpath)
            .header("Content-Type", "application/json")
        ;
    }



    /* Static Helpers
    --===---------------
    */


    private static URI EnsureTrailingSlash(URI uri)
    {
        String value = uri.toString();

        if(value.endsWith("/"))
            return uri;

        return URI.create(value + "/");
    }



    private static String JoinPath(Object... subpath)
    {
        if(subpath == null || subpath.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();

        for(Object part : subpath)
        {
            if(part == null)
                continue;

            String value = part.toString();

            if(value.isBlank())
                continue;

            value = TrimSlashes(value);

            if(value.isBlank())
                continue;

            if(!sb.isEmpty())
                sb.append("/");

            sb.append(EncodePathSegment(value));
        }

        return sb.toString();
    }



    private static String TrimSlashes(String value)
    {
        int start = 0;
        int end = value.length();

        while(start < end && value.charAt(start) == '/')
            start++;

        while(end > start && value.charAt(end - 1) == '/')
            end--;

        return value.substring(start, end);
    }



    private static String EncodePathSegment(String value)
    {
        return URLEncoder
            .encode(value, StandardCharsets.UTF_8)
            .replace("+", "%20")
        ;
    }
}