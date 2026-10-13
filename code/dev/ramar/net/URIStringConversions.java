package dev.ramar.net;

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.util.Objects;
import java.util.Collection;
import java.util.function.Function;

import java.util.stream.Stream;
import java.util.stream.Collectors;

public final class URIStringConversions
{

    private URIStringConversions()
    {

    }



    public static <T> String From(Collection<T> items)
    {
        return From(items, Objects::toString);
    }



    public static <T> String From(Collection<T> items, Function<T, String> mapper)
    {
        if(items == null)
            return "";

        return From(items.stream(), mapper);
    }



    public static <T> String From(Stream<T> items)
    {
        return From(items, Objects::toString);
    }



    public static <T> String From(Stream<T> items, Function<T, String> mapper)
    {
        if(items == null)
            return "";

        if(mapper == null)
            mapper = Objects::toString;

        Function<T, String> finalMapper = mapper;

        return items
            .filter(Objects::nonNull)
            .map(finalMapper)
            .filter(Objects::nonNull)
            .map(URIStringConversions::Encode)
            .collect(Collectors.joining(","))
        ;
    }



    public static String Encode(String value)
    {
        if(value == null)
            return "";

        return URLEncoder
            .encode(value, StandardCharsets.UTF_8)
            .replace("+", "%20")
        ;
    }

}