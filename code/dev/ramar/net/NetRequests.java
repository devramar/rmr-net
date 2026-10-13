package dev.ramar.net;

import java.io.InputStream;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.time.Duration;

import java.util.Map;
import java.util.Base64;
import java.util.Objects;


import dev.ramar.json.JArray;
import dev.ramar.json.JToken;
import dev.ramar.json.JObject;

public final class NetRequests
{

    /* Constants
    --===----------
      common values used when building requests.
    */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_USER_AGENT = "User-Agent";

    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    public static final String CONTENT_TYPE_TEXT = "text/plain; charset=utf-8";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded; charset=utf-8";

    public static final String ACCEPT_JSON = "application/json";



    /* Constructor
    --===------------
      static utility class.
    */
    private NetRequests()
    {
        throw new UnsupportedOperationException("NetRequests is a static utility class.");
    }



    /* Builder
    --===--------
      base request builder helpers.

      Use this when the lazy request methods are too simple and you need
      to manually add headers, auth, timeout, or a custom body setup.
    */
    public static HttpRequest.Builder Builder(String url)
    {
        return Builder(URI.create(url));
    }

    public static HttpRequest.Builder Builder(URI uri)
    {
        Objects.requireNonNull(uri, "uri");

        return HttpRequest.newBuilder()
            .uri(uri)
        ;
    }



    /* LazyGET
    --===--------
      easy GET requests.
    */
    public static HttpRequest LazyGET(String url)
    {
        return LazyGET(URI.create(url));
    }

    public static HttpRequest LazyGET(URI uri)
    {
        return Builder(uri)
            .GET()
            .build()
        ;
    }



    /* LazyHEAD
    --===---------
      easy HEAD requests.
    */
    public static HttpRequest LazyHEAD(String url)
    {
        return LazyHEAD(URI.create(url));
    }

    public static HttpRequest LazyHEAD(URI uri)
    {
        return Builder(uri)
            .method("HEAD", HttpRequest.BodyPublishers.noBody())
            .build()
        ;
    }



    /* LazyOPTIONS
    --===------------
      easy OPTIONS requests.
    */
    public static HttpRequest LazyOPTIONS(String url)
    {
        return LazyOPTIONS(URI.create(url));
    }

    public static HttpRequest LazyOPTIONS(URI uri)
    {
        return Builder(uri)
            .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
            .build()
        ;
    }



    /* LazyPOST
    --===---------
      easy POST requests.

      Supported body inputs:
      - no body
      - String
      - byte[]
      - InputStream
    */
    public static HttpRequest LazyPOST(String url)
    {
        return LazyPOST(URI.create(url));
    }

    public static HttpRequest LazyPOST(URI uri)
    {
        return Builder(uri)
            .POST(Body())
            .build()
        ;
    }

    public static HttpRequest LazyPOST(String url, String payload)
    {
        return LazyPOST(URI.create(url), payload);
    }

    public static HttpRequest LazyPOST(URI uri, String payload)
    {
        return Builder(uri)
            .POST(Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyPOST(String url, byte[] payload)
    {
        return LazyPOST(URI.create(url), payload);
    }

    public static HttpRequest LazyPOST(URI uri, byte[] payload)
    {
        return Builder(uri)
            .POST(Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyPOST(String url, InputStream payload)
    {
        return LazyPOST(URI.create(url), payload);
    }

    public static HttpRequest LazyPOST(URI uri, InputStream payload)
    {
        return Builder(uri)
            .POST(Body(payload))
            .build()
        ;
    }



    /* LazyPUT
    --===--------
      easy PUT requests.

      Supported body inputs:
      - no body
      - String
      - byte[]
      - InputStream
    */
    public static HttpRequest LazyPUT(String url)
    {
        return LazyPUT(URI.create(url));
    }

    public static HttpRequest LazyPUT(URI uri)
    {
        return Builder(uri)
            .PUT(Body())
            .build()
        ;
    }

    public static HttpRequest LazyPUT(String url, String payload)
    {
        return LazyPUT(URI.create(url), payload);
    }

    public static HttpRequest LazyPUT(URI uri, String payload)
    {
        return Builder(uri)
            .PUT(Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyPUT(String url, byte[] payload)
    {
        return LazyPUT(URI.create(url), payload);
    }

    public static HttpRequest LazyPUT(URI uri, byte[] payload)
    {
        return Builder(uri)
            .PUT(Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyPUT(String url, InputStream payload)
    {
        return LazyPUT(URI.create(url), payload);
    }

    public static HttpRequest LazyPUT(URI uri, InputStream payload)
    {
        return Builder(uri)
            .PUT(Body(payload))
            .build()
        ;
    }



    /* LazyPATCH
    --===----------
      easy PATCH requests.

      Java's HttpRequest builder does not have a direct PATCH method,
      so this uses the generic method builder internally.

      Supported body inputs:
      - no body
      - String
      - byte[]
      - InputStream
    */
    public static HttpRequest LazyPATCH(String url)
    {
        return LazyPATCH(URI.create(url));
    }

    public static HttpRequest LazyPATCH(URI uri)
    {
        return Builder(uri)
            .method("PATCH", Body())
            .build()
        ;
    }

    public static HttpRequest LazyPATCH(String url, String payload)
    {
        return LazyPATCH(URI.create(url), payload);
    }

    public static HttpRequest LazyPATCH(URI uri, String payload)
    {
        return Builder(uri)
            .method("PATCH", Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyPATCH(String url, byte[] payload)
    {
        return LazyPATCH(URI.create(url), payload);
    }

    public static HttpRequest LazyPATCH(URI uri, byte[] payload)
    {
        return Builder(uri)
            .method("PATCH", Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyPATCH(String url, InputStream payload)
    {
        return LazyPATCH(URI.create(url), payload);
    }

    public static HttpRequest LazyPATCH(URI uri, InputStream payload)
    {
        return Builder(uri)
            .method("PATCH", Body(payload))
            .build()
        ;
    }



    /* LazyDELETE
    --===-----------
      easy DELETE requests.

      A DELETE can be sent with or without a body depending on the API.

      Supported body inputs:
      - no body
      - String
      - byte[]
      - InputStream
    */
    public static HttpRequest LazyDELETE(String url)
    {
        return LazyDELETE(URI.create(url));
    }

    public static HttpRequest LazyDELETE(URI uri)
    {
        return Builder(uri)
            .DELETE()
            .build()
        ;
    }

    public static HttpRequest LazyDELETE(String url, String payload)
    {
        return LazyDELETE(URI.create(url), payload);
    }

    public static HttpRequest LazyDELETE(URI uri, String payload)
    {
        return Builder(uri)
            .method("DELETE", Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyDELETE(String url, byte[] payload)
    {
        return LazyDELETE(URI.create(url), payload);
    }

    public static HttpRequest LazyDELETE(URI uri, byte[] payload)
    {
        return Builder(uri)
            .method("DELETE", Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyDELETE(String url, InputStream payload)
    {
        return LazyDELETE(URI.create(url), payload);
    }

    public static HttpRequest LazyDELETE(URI uri, InputStream payload)
    {
        return Builder(uri)
            .method("DELETE", Body(payload))
            .build()
        ;
    }



    /* LazyREQUEST
    --===------------
      generic request fallback.

      This is for methods that are not directly covered by this class.
      Examples could include TRACE, CONNECT, PROPFIND, or API-specific methods.

      Supported body inputs:
      - no body
      - String
      - byte[]
      - InputStream
    */
    public static HttpRequest LazyREQUEST(String method, String url)
    {
        return LazyREQUEST(method, URI.create(url));
    }

    public static HttpRequest LazyREQUEST(String method, URI uri)
    {
        return Builder(uri)
            .method(NormalizeMethod(method), Body())
            .build()
        ;
    }

    public static HttpRequest LazyREQUEST(String method, String url, String payload)
    {
        return LazyREQUEST(method, URI.create(url), payload);
    }

    public static HttpRequest LazyREQUEST(String method, URI uri, String payload)
    {
        return Builder(uri)
            .method(NormalizeMethod(method), Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyREQUEST(String method, String url, byte[] payload)
    {
        return LazyREQUEST(method, URI.create(url), payload);
    }

    public static HttpRequest LazyREQUEST(String method, URI uri, byte[] payload)
    {
        return Builder(uri)
            .method(NormalizeMethod(method), Body(payload))
            .build()
        ;
    }

    public static HttpRequest LazyREQUEST(String method, String url, InputStream payload)
    {
        return LazyREQUEST(method, URI.create(url), payload);
    }

    public static HttpRequest LazyREQUEST(String method, URI uri, InputStream payload)
    {
        return Builder(uri)
            .method(NormalizeMethod(method), Body(payload))
            .build()
        ;
    }



    /* Headers
    --===---------
      helper methods for adding common headers to request builders.

      These return the same builder so they can be chained into normal
      HttpRequest builder usage.
    */
    public static HttpRequest.Builder Header(HttpRequest.Builder builder, String name, String value)
    {
        Objects.requireNonNull(builder, "builder");
        Objects.requireNonNull(name, "name");

        if( value == null )
            return builder;

        return builder.header(name, value);
    }

    public static HttpRequest.Builder Headers(HttpRequest.Builder builder, Map<String, String> headers)
    {
        Objects.requireNonNull(builder, "builder");

        if( headers == null )
            return builder;

        for( Map.Entry<String, String> entry : headers.entrySet() )
            Header(builder, entry.getKey(), entry.getValue());

        return builder;
    }

    public static HttpRequest.Builder Headers(HttpRequest.Builder builder, String... headers)
    {
        Objects.requireNonNull(builder, "builder");

        if( headers == null )
            return builder;

        if( headers.length % 2 != 0 )
            throw new IllegalArgumentException("headers must contain an even number of values.");

        for( int i = 0; i < headers.length; i += 2 )
            Header(builder, headers[i], headers[i + 1]);

        return builder;
    }

    public static HttpRequest.Builder ContentType(HttpRequest.Builder builder, String contentType)
    {
        return Header(builder, HEADER_CONTENT_TYPE, contentType);
    }

    public static HttpRequest.Builder Accept(HttpRequest.Builder builder, String accept)
    {
        return Header(builder, HEADER_ACCEPT, accept);
    }

    public static HttpRequest.Builder AcceptJSON(HttpRequest.Builder builder)
    {
        return Accept(builder, ACCEPT_JSON);
    }

    public static HttpRequest.Builder UserAgent(HttpRequest.Builder builder, String userAgent)
    {
        return Header(builder, HEADER_USER_AGENT, userAgent);
    }



    /* Auth
    --===------
      helper methods for common Authorization headers.
    */
    public static HttpRequest.Builder BearerAuth(HttpRequest.Builder builder, String token)
    {
        if( token == null || token.isBlank() )
            return builder;

        return Header(builder, HEADER_AUTHORIZATION, "Bearer " + token);
    }

    public static HttpRequest.Builder BasicAuth(HttpRequest.Builder builder, String username, String password)
    {
        Objects.requireNonNull(username, "username");

        if( password == null )
            password = "";

        String token = username + ":" + password;
        String encoded = Base64.getEncoder().encodeToString(token.getBytes(DEFAULT_CHARSET));

        return Header(builder, HEADER_AUTHORIZATION, "Basic " + encoded);
    }



    /* Timeout
    --===--------
      helper methods for request timeouts.
    */
    public static HttpRequest.Builder Timeout(HttpRequest.Builder builder, Duration timeout)
    {
        Objects.requireNonNull(builder, "builder");

        if( timeout == null )
            return builder;

        return builder.timeout(timeout);
    }

    public static HttpRequest.Builder TimeoutSeconds(HttpRequest.Builder builder, long seconds)
    {
        return Timeout(builder, Duration.ofSeconds(seconds));
    }



    /* Body
    --===-----
      request body helper methods.

      A null String or byte[] body is treated as no body.
      An InputStream body must be non-null.
    */
    public static HttpRequest.BodyPublisher Body()
    {
        return HttpRequest.BodyPublishers.noBody();
    }

    public static HttpRequest.BodyPublisher Body(String payload)
    {
        if( payload == null )
            return Body();

        return HttpRequest.BodyPublishers.ofString(payload, DEFAULT_CHARSET);
    }

    public static HttpRequest.BodyPublisher Body(byte[] payload)
    {
        if( payload == null )
            return Body();

        return HttpRequest.BodyPublishers.ofByteArray(payload);
    }

    public static HttpRequest.BodyPublisher Body(InputStream payload)
    {
        Objects.requireNonNull(payload, "payload");

        return HttpRequest.BodyPublishers.ofInputStream(() -> payload);
    }



    /* JSON
    --===-----
      helpers for dev.ramar.json values.

      These only convert your JSON types into strings.
      They do not introduce any external JSON libraries.
    */

    public static HttpRequest.BodyPublisher Body(JToken payload)
    {
        if( payload == null )
            return Body();

        return HttpRequest.BodyPublishers.ofString(payload.toShortString(), DEFAULT_CHARSET);
    }


    public static HttpRequest.Builder JSON(HttpRequest.Builder builder)
    {
        return AcceptJSON(ContentType(builder, CONTENT_TYPE_JSON));
    }



    /* Query
    --===-------
      helpers for adding query parameters to a URI.
    */

    public static URI WithQuery(String url, Map<String, ?> query)
    {
        return WithQuery(URI.create(url), query);
    }

    public static URI WithQuery(URI uri, Map<String, ?> query)
    {
        Objects.requireNonNull(uri, "uri");

        String encoded = EncodeForm(query);

        if( encoded.isBlank() )
            return uri;

        String raw = uri.toString();
        int fragmentIndex = raw.indexOf('#');

        String base = fragmentIndex >= 0
            ? raw.substring(0, fragmentIndex)
            : raw
        ;

        String fragment = fragmentIndex >= 0
            ? raw.substring(fragmentIndex)
            : ""
        ;

        String separator;

        if( base.contains("?") )
            separator = base.endsWith("?") || base.endsWith("&") ? "" : "&";
        else
            separator = "?";

        return URI.create(base + separator + encoded + fragment);
    }



    /* Form
    --===------
      application/x-www-form-urlencoded helpers.

      This is useful for query strings and form payloads.
      Use LazyPOST(uri, EncodeForm(form)) for a simple form body.
    */
    public static String EncodeForm(Map<String, ?> values)
    {
        if( values == null || values.isEmpty() )
            return "";

        StringBuilder builder = new StringBuilder();

        for( Map.Entry<String, ?> entry : values.entrySet() )
        {
            if( entry.getKey() == null )
                continue;

            if( !builder.isEmpty() )
                builder.append('&');

            builder.append(Encode(entry.getKey()));
            builder.append('=');

            Object value = entry.getValue();

            if( value != null )
                builder.append(Encode(String.valueOf(value)));
        }

        return builder.toString();
    }

    public static String Encode(String value)
    {
        if( value == null )
            return "";

        return URLEncoder.encode(value, DEFAULT_CHARSET);
    }



    /* Internal
    --===---------
      small internal validation helpers.
    */
    private static String NormalizeMethod(String method)
    {
        Objects.requireNonNull(method, "method");

        String normalized = method.trim().toUpperCase();

        if( normalized.isBlank() )
            throw new IllegalArgumentException("method cannot be blank.");

        return normalized;
    }

}