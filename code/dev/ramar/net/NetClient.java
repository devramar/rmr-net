package dev.ramar.net;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.Duration;

import java.util.function.Function;


import dev.ramar.json.JArray;
import dev.ramar.json.JToken;
import dev.ramar.json.JObject;
import dev.ramar.json.jit.JITJArray;
import dev.ramar.json.jit.JITJObject;

import dev.ramar.structs.Either;

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

    public ProtoHandle handle()
    {
        return new ProtoHandle();
    }

    public ProtoHandle handle(HttpRequest req)
    {
        return new ProtoHandle(req);
    }


    /// Used to configure a request before selecting the response handler.
    ///
    /// HttpRequest is immutable, so ProtoHandle owns a HttpRequest.Builder while request modifiers
    /// are being chained. The request is built only when build()/string()/bytes()/jobject()/etc. is
    /// called.
    public class ProtoHandle
    {
        private HttpRequest req = null;
        private HttpRequest.Builder builder = null;
        private Function<HttpRequest, HttpRequest> requestTransform = Function.identity();

        public ProtoHandle(HttpRequest req)
        {
            this.req = req;
        }

        public ProtoHandle()
        { }

        /// Replace the request represented by this handle.
        public ProtoHandle request(HttpRequest req)
        {
            this.req = req;
            this.builder = null;
            return this;
        }

        /// Set/replace the request URI.
        public ProtoHandle url(String url)
        {
            return this.url(URI.create(url));
        }

        /// Set/replace the request URI.
        public ProtoHandle url(URI uri)
        {
            this.ensureBuilder().uri(uri);
            return this;
        }

        /// Append a header value.
        public ProtoHandle header(String name, String value)
        {
            this.ensureBuilder().header(name, value);
            return this;
        }

        /// Replace all existing values for a header.
        public ProtoHandle setHeader(String name, String value)
        {
            this.ensureBuilder().setHeader(name, value);
            return this;
        }

        public ProtoHandle timeout(Duration timeout)
        {
            this.ensureBuilder().timeout(timeout);
            return this;
        }

        public ProtoHandle expectContinue(boolean enabled)
        {
            this.ensureBuilder().expectContinue(enabled);
            return this;
        }

        public ProtoHandle version(HttpClient.Version version)
        {
            this.ensureBuilder().version(version);
            return this;
        }

        public ProtoHandle GET()
        {
            this.ensureBuilder().GET();
            return this;
        }

        public ProtoHandle DELETE()
        {
            this.ensureBuilder().DELETE();
            return this;
        }

        public ProtoHandle POST(HttpRequest.BodyPublisher body)
        {
            this.ensureBuilder().POST(body);
            return this;
        }

        public ProtoHandle POST(String body)
        {
            return this.POST(HttpRequest.BodyPublishers.ofString(body));
        }

        public ProtoHandle PUT(HttpRequest.BodyPublisher body)
        {
            this.ensureBuilder().PUT(body);
            return this;
        }

        public ProtoHandle PUT(String body)
        {
            return this.PUT(HttpRequest.BodyPublishers.ofString(body));
        }

        public ProtoHandle method(String method, HttpRequest.BodyPublisher body)
        {
            this.ensureBuilder().method(method, body);
            return this;
        }

        /// Convenience helper for JSON POST requests.
        public ProtoHandle json(String json)
        {
            return this
                .setHeader("Content-Type", "application/json")
                .POST(json);
        }

        /// Apply a final request transform after all fluent modifiers have been resolved.
        ///
        /// This is primarily useful for authentication layers such as DPoP, where the signature
        /// must be generated from the final HTTP method + final URI.
        public ProtoHandle mapRequest(Function<HttpRequest, HttpRequest> transform)
        {
            this.requestTransform = this.requestTransform.andThen(transform);
            return this;
        }

        /// Build the final request, including any mapRequest transforms.
        public HttpRequest build()
        {
            HttpRequest baseRequest;

            if (this.builder != null)
                baseRequest = this.builder.build();
            else if (this.req != null)
                baseRequest = this.req;
            else
                throw new IllegalStateException("no HttpRequest or URL configured");

            return this.requestTransform.apply(baseRequest);
        }

        public Handle<String, String> string()
        {
            return new Handle<String, String>(this.build(), HttpResponse.BodyHandlers.ofString())
                .convert(s -> s)
            ;
        }

        public Handle<byte[], byte[]> bytes()
        {
            return new Handle<byte[], byte[]>(this.build(), HttpResponse.BodyHandlers.ofByteArray())
                .convert(b -> b)
            ;
        }

        public Handle<String, JObject> jobject()
        {
            return new Handle<String, JObject>(this.build(), HttpResponse.BodyHandlers.ofString())
                .convert(JITJObject::FromString)
            ;
        }

        public Handle<String, JArray> jarray()
        {
            return new Handle<String, JArray>(this.build(), HttpResponse.BodyHandlers.ofString())
                .convert(JITJArray::FromString)
            ;
        }

        public Handle<String, JToken> jtoken()
        {
            return new Handle<String, JToken>(this.build(), HttpResponse.BodyHandlers.ofString())
                .convert(JToken::JITFromString)
            ;
        }

        private HttpRequest.Builder ensureBuilder()
        {
            if (this.builder != null)
                return this.builder;

            if (this.req == null)
            {
                this.builder = HttpRequest.newBuilder();
                return this.builder;
            }

            this.builder = copyRequest(this.req);
            this.req = null;
            return this.builder;
        }
    }

    /// Create a mutable builder containing the important properties of an immutable HttpRequest.
    /// Useful to request-transform/authentication layers which need to add headers to a finished
    /// request without discarding its method/body/timeout/version.
    public static HttpRequest.Builder copyRequest(HttpRequest req)
    {
        HttpRequest.Builder builder = HttpRequest.newBuilder(req.uri());

        req.timeout().ifPresent(builder::timeout);
        req.version().ifPresent(builder::version);
        builder.expectContinue(req.expectContinue());

        req.headers().map().forEach((name, values) ->
            values.forEach(value -> builder.header(name, value))
        );

        builder.method(
            req.method(),
            req.bodyPublisher().orElse(HttpRequest.BodyPublishers.noBody())
        );

        return builder;
    }

    public class Handle<T, R>
    {
        public HttpRequest req;
        public HttpResponse.BodyHandler<T> handler;

        private HttpResponse<T> res;
        public HttpResponse<T> response()
        { return this.res; }

        public Function<T, R> bodyConverter;

        public Handle(HttpRequest req, HttpResponse.BodyHandler<T> handler)
        {
            this.req = req;
            this.handler = handler;
        }

        public Handle(HttpResponse.BodyHandler<T> handler)
        {
            this.handler = handler;
        }

        public Handle<T, R> convert(Function<T, R> converter)
        {
            this.bodyConverter = converter;
            return this;
        }

        public Handle<T, R> request(HttpRequest req)
        {
            this.req = req;
            return this;
        }

        /* result processing
        --===------------------
        */

        public Either<HttpException, R> quiet()
        {
            return Either.TryChecked(this::loud);
        }

        public R loud() throws HttpException
        {
            if( this.bodyConverter == null )
                throw new IllegalStateException("no bodyConverter setup to convert result to a value");
            try
            {
                this.res = NetUtil.HandleRequest(NetClient.this.client, this.req, this.handler);
                if( this.bodyConverter == null )
                    throw new IllegalStateException("no bodyConverter setup to convert result to a value");

                try
                {
                    return this.bodyConverter.apply(this.res.body());
                }
                catch(Exception ex)
                {
                    throw new IllegalArgumentException("result conversion failed.", ex);
                }
            }
            catch(HttpException ex)
            {
                this.res = (HttpResponse<T>)ex.response();
                throw new HttpException("failed to handle.", ex);
            }
        }
    }

    public <T> Either<HttpException, HttpResponse<T>> tryHandle(HttpRequest req, HttpResponse.BodyHandler<T> handler)
    {
        return Either.TryChecked(() -> this.handle(req, handler)); }

    public <T> HttpResponse<T> handle(HttpRequest req, HttpResponse.BodyHandler<T> handler) throws HttpException
    {
        return NetUtil.HandleRequest(this.client, req, handler);
    }

    public Either<HttpException, String> tryHandleString(HttpRequest req)
    {
        return Either.TryChecked(() -> this.handleString(req));
    }

    public String handleString(HttpRequest req) throws HttpException
    {
        return NetUtil.HandleRequest_String(this.client, req);
    }

    public Either<HttpException, byte[]> tryHandleBytes(HttpRequest req)
    {
        return Either.TryChecked(() -> this.handleBytes(req));
    }

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
