# NET

Ramar's Networking package

This package has various utilities to get you making net requests easier. the main thing to check out is `NetQuerier`.
it splits making requests into two simple parts:
 - querier.post/patch/etc(String suburl) 
   - this method returns a Builder setup with that request, with the url combined with the querier's base.
 - `.handle` suite of methods, basically NetUtil wrappers, but the idea is to have methods that, if a 200 is returned, attempts to parse the body in the desired form.

if you combine your implementing class with with `dev.ramar.utils.ExecutorCaller`, then you can expand your utility to what i use:
 - ExecutorCaller has `.sync()` `.async()` `.block()` methods to easily decide where to process a method.
EG:
```java
MyExecutorQuerier eq = new MyExecutorQuerier();

///          | process on this thread
///          |       | response should be a JArray
///          v       v                v do a GET request to ~/users
JArray arr = eq.sync(eq.handle_JArray(eq.get("/users")));

///           v process on another thread, but wait
JObject dat = eq.block()

///                              v process on another thread w/ callback
CompletableFuture<JObject> dat = eq.async(eq.asJArray(eq.get("/users/123")));
```


## Building / JRunner

This project is a JRunner project. if you wanna build it, you can't right now! gotta manually do it ;)