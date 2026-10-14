package dev.ramar.net;


import dev.ramar.json.JArray;
import dev.ramar.json.JToken;
import dev.ramar.json.JObject;

public class NetResponses
{


    public static JObject Success(Object result)
    {
        return JObject.Create()
            .with("result", result)
            .asObject()
        ;
    }

    public static JObject Error(String message)
    {
        return JObject.Create()
            .with("error", message)
            .asObject()
        ;
    }


    public static JObject Exception(Exception ex)
    {
        return JObject.Create()
            .with("error", ex.getMessage())
            .with("trace", JArray.From(ExceptionUtil.TraceLinesAsStream(ex)))
            .asObject()
        ;
    }



    public static JObject Page(int total, int offset, JArray items)
    {
        return JObject.Create()
            .with("total", total)
            .with("offset", offset)
            .with("items", items)
            .asObject()
        ;
    }
}