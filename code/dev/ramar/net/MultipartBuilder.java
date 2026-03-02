package dev.ramar.net;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/// GPT-MADE
public class MultipartBuilder
{
    private static class Part
    {
        final String name;
        final String filename;
        final String contentType;
        final byte[] data;

        Part(String name, String filename, String contentType, byte[] data)
        {
            this.name = name;
            this.filename = filename;
            this.contentType = contentType;
            this.data = data;
        }
    }

    private final String boundary = "----" + UUID.randomUUID();
    private final List<Part> parts = new ArrayList<>();

    public MultipartBuilder json(String name, String json)
    {
        parts.add(new Part(name, null, "application/json",
            json.getBytes(StandardCharsets.UTF_8)));
        return this;
    }

    public MultipartBuilder string(String name, String value)
    {
        parts.add(new Part(name, null, "text/plain; charset=UTF-8",
            value.getBytes(StandardCharsets.UTF_8)));
        return this;
    }

    public MultipartBuilder bytes(String name, String filename, String contentType, byte[] data)
    {
        parts.add(new Part(name, filename, contentType, data));
        return this;
    }

    public MultipartBuilder file(String name, Path file, String contentType)
    {
        try
        {
            byte[] data = Files.readAllBytes(file);
            parts.add(new Part(name, file.getFileName().toString(), contentType, data));
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Failed to read file: " + file, ex);
        }
        return this;
    }

    public HttpRequest build(URI uri)
    {
        List<byte[]> segments = new ArrayList<>();
        for (Part part : parts)
        {
            StringBuilder header = new StringBuilder();
            header.append("--").append(boundary).append("\r\n");
            header.append("Content-Disposition: form-data; name=\"").append(part.name).append("\"");
            if (part.filename != null)
                header.append("; filename=\"").append(part.filename).append("\"");
            header.append("\r\n");
            if (part.contentType != null)
                header.append("Content-Type: ").append(part.contentType).append("\r\n");
            header.append("\r\n");

            segments.add(header.toString().getBytes(StandardCharsets.UTF_8));
            segments.add(part.data);
            segments.add("\r\n".getBytes(StandardCharsets.UTF_8));
        }
        String closing = "--" + boundary + "--\r\n";
        segments.add(closing.getBytes(StandardCharsets.UTF_8));

        byte[] body = concat(segments);

        return HttpRequest.newBuilder()
            .uri(uri)
            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
            .build();
    }

    private static byte[] concat(List<byte[]> arrays)
    {
        int len = arrays.stream().mapToInt(a -> a.length).sum();
        byte[] out = new byte[len];
        int pos = 0;
        for (byte[] arr : arrays)
        {
            System.arraycopy(arr, 0, out, pos, arr.length);
            pos += arr.length;
        }
        return out;
    }
}