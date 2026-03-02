package dev.ramar.net;

import java.io.IOException;

public class NetException extends IOException
{
    public NetException()
    { super(); }

    public NetException(Exception inner)
    { super(inner); }

    public NetException(String msg, Exception inner)
    { super(msg, inner); }

}