package dev.ramar.net;


import dev.ramar.json.JObject;
import dev.ramar.json.JArray;
import dev.ramar.json.JToken;

public class NetResponses
{


    public static JObject Success(Object result)
    {
        return new JObject()
            .with("result", result)
            .asObject()
        ;
    }

    public static JObject Error(String message)
    {
        return new JObject()
            .with("error", message)
            .asObject()
        ;
    }


    public static JObject Exception(Exception ex)
    {
        return new JObject()
            .with("error", ex.getMessage())
            .with("trace", JArray.From(ExceptionUtil.TraceLinesAsStream(ex)))
            .asObject()
        ;
    }



    public static JObject Page(int total, int offset, JArray items)
    {
        return new JObject()
            .with("total", total)
            .with("offset", offset)
            .with("items", items)
            .asObject()
        ;
    }
}