package dev.ramar.net;

import java.util.Arrays;
import java.util.stream.Stream;

import java.util.*;
import java.util.stream.*;

public class ExceptionUtil
{

    public static Stream<String> TraceLinesAsStream(Exception ex)
    { 
        return Arrays
            .stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
        ;
    }

    public static String[] TraceLinesAsArray(Exception ex)
    {
        return Arrays
            .stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .toArray(String[]::new)
        ;
    }

}