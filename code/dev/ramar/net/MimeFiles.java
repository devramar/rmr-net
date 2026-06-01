package dev.ramar.net;

import java.util.Locale;
import java.util.Map;

/// converts MIME strings into file extensions
public class MimeFiles
{

    private static final Map<String, String> MIME_EXTENSIONS = Map.ofEntries(
        Map.entry("image/png", "png"),
        Map.entry("image/jpeg", "jpg"),
        Map.entry("image/jpg", "jpg"),
        Map.entry("image/gif", "gif"),
        Map.entry("image/webp", "webp"),
        Map.entry("image/svg+xml", "svg"),
        Map.entry("image/bmp", "bmp"),
        Map.entry("image/tiff", "tiff"),

        Map.entry("application/json", "json"),
        Map.entry("application/xml", "xml"),
        Map.entry("text/xml", "xml"),
        Map.entry("text/plain", "txt"),
        Map.entry("text/html", "html"),
        Map.entry("text/css", "css"),
        Map.entry("text/javascript", "js"),
        Map.entry("application/javascript", "js"),

        Map.entry("application/pdf", "pdf"),
        Map.entry("application/zip", "zip"),
        Map.entry("application/gzip", "gz"),
        Map.entry("application/octet-stream", "bin")
    );


    public static String ExtensionOf(String mimeType)
    {
        if(mimeType == null)
            return "bin";

        String cleanMime = mimeType
            .trim()
            .toLowerCase(Locale.ROOT)
        ;

        int paramIndex = cleanMime.indexOf(';');

        if(paramIndex >= 0)
            cleanMime = cleanMime.substring(0, paramIndex).trim();

        return MIME_EXTENSIONS.getOrDefault(cleanMime, "bin");
    }

    public static String ExtensionWithDotOf(String mimeType)
    {
        return "." + ExtensionOf(mimeType);
    }

}