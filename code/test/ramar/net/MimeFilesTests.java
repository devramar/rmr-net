package test.ramar.net;

import dev.ramar.net.MimeFiles;
import dev.ramar.test.TestExecutor;
import dev.ramar.test.TestUtil;

public class MimeFilesTests
{

    public static final TestExecutor EXECUTOR = TestUtil.Build(
        "test.ramar.net",
        MimeFilesTests::NullMimeReturnsBin,
        MimeFilesTests::UnknownMimeReturnsBin,
        MimeFilesTests::ImageMimeTypesResolve,
        MimeFilesTests::TextMimeTypesResolve,
        MimeFilesTests::ApplicationMimeTypesResolve,
        MimeFilesTests::MimeTypeIsCaseInsensitive,
        MimeFilesTests::MimeTypeTrimsWhitespace,
        MimeFilesTests::MimeTypeIgnoresParameters,
        MimeFilesTests::ExtensionWithDotAddsDot
    );



    public static void NullMimeReturnsBin()
    {
        assert MimeFiles.ExtensionOf(null).equals("bin");
    }



    public static void UnknownMimeReturnsBin()
    {
        assert MimeFiles.ExtensionOf("application/x-unknown").equals("bin");
    }



    public static void ImageMimeTypesResolve()
    {
        assert MimeFiles.ExtensionOf("image/png").equals("png");
        assert MimeFiles.ExtensionOf("image/jpeg").equals("jpg");
        assert MimeFiles.ExtensionOf("image/jpg").equals("jpg");
        assert MimeFiles.ExtensionOf("image/gif").equals("gif");
        assert MimeFiles.ExtensionOf("image/webp").equals("webp");
        assert MimeFiles.ExtensionOf("image/svg+xml").equals("svg");
        assert MimeFiles.ExtensionOf("image/bmp").equals("bmp");
        assert MimeFiles.ExtensionOf("image/tiff").equals("tiff");
    }



    public static void TextMimeTypesResolve()
    {
        assert MimeFiles.ExtensionOf("text/plain").equals("txt");
        assert MimeFiles.ExtensionOf("text/html").equals("html");
        assert MimeFiles.ExtensionOf("text/css").equals("css");
        assert MimeFiles.ExtensionOf("text/javascript").equals("js");
        assert MimeFiles.ExtensionOf("text/xml").equals("xml");
    }



    public static void ApplicationMimeTypesResolve()
    {
        assert MimeFiles.ExtensionOf("application/json").equals("json");
        assert MimeFiles.ExtensionOf("application/xml").equals("xml");
        assert MimeFiles.ExtensionOf("application/javascript").equals("js");
        assert MimeFiles.ExtensionOf("application/pdf").equals("pdf");
        assert MimeFiles.ExtensionOf("application/zip").equals("zip");
        assert MimeFiles.ExtensionOf("application/gzip").equals("gz");
        assert MimeFiles.ExtensionOf("application/octet-stream").equals("bin");
    }



    public static void MimeTypeIsCaseInsensitive()
    {
        assert MimeFiles.ExtensionOf("IMAGE/PNG").equals("png");
        assert MimeFiles.ExtensionOf("Application/JSON").equals("json");
        assert MimeFiles.ExtensionOf("Text/HTML").equals("html");
    }



    public static void MimeTypeTrimsWhitespace()
    {
        assert MimeFiles.ExtensionOf(" image/png ").equals("png");
        assert MimeFiles.ExtensionOf("\tapplication/json\n").equals("json");
    }



    public static void MimeTypeIgnoresParameters()
    {
        assert MimeFiles.ExtensionOf("text/plain; charset=utf-8").equals("txt");
        assert MimeFiles.ExtensionOf("application/json; charset=utf-8").equals("json");
        assert MimeFiles.ExtensionOf("image/svg+xml; charset=utf-8").equals("svg");
    }



    public static void ExtensionWithDotAddsDot()
    {
        assert MimeFiles.ExtensionWithDotOf("image/png").equals(".png");
        assert MimeFiles.ExtensionWithDotOf("application/json").equals(".json");
        assert MimeFiles.ExtensionWithDotOf("unknown/unknown").equals(".bin");
    }

}