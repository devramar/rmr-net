package test.ramar.net;

import dev.ramar.net.AddressScope;
import dev.ramar.test.TestExecutor;
import dev.ramar.test.TestUtil;

import java.net.URI;
import java.net.http.HttpRequest;

public class AddressScopeTests
{

    public static final TestExecutor EXECUTOR = TestUtil.Build(
        "test.ramar.net",
        AddressScopeTests::BaseAddsTrailingSlash,
        AddressScopeTests::BaseKeepsTrailingSlash,
        AddressScopeTests::UriReturnsBaseForEmptyPath,
        AddressScopeTests::UriJoinsPathSegments,
        AddressScopeTests::UriIgnoresNullAndBlankSegments,
        AddressScopeTests::UriTrimsSlashesFromSegments,
        AddressScopeTests::UriEncodesSpaces,
        AddressScopeTests::UriEncodesSegmentSlashes,
        AddressScopeTests::SuburlBuildsRequestForPath,
        AddressScopeTests::GetBuildsGetRequest,
        AddressScopeTests::PostBuildsPostRequest,
        AddressScopeTests::PatchBuildsPatchRequest,
        AddressScopeTests::PutBuildsPutRequest,
        AddressScopeTests::DeleteBuildsDeleteRequest,
        AddressScopeTests::MethodBuildsCustomRequest,
        AddressScopeTests::PostJSONAddsContentType,
        AddressScopeTests::PatchJSONAddsContentType
    );



    public static void BaseAddsTrailingSlash()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        assert scope.base().equals(URI.create("https://example.com/api/"));
    }



    public static void BaseKeepsTrailingSlash()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api/"));

        assert scope.base().equals(URI.create("https://example.com/api/"));
    }



    public static void UriReturnsBaseForEmptyPath()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        assert scope.uri().equals(URI.create("https://example.com/api/"));
    }



    public static void UriJoinsPathSegments()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        assert scope.uri("users", 12, "posts").equals(URI.create("https://example.com/api/users/12/posts"));
    }



    public static void UriIgnoresNullAndBlankSegments()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        assert scope.uri("users", null, "", "   ", 12).equals(URI.create("https://example.com/api/users/12"));
    }



    public static void UriTrimsSlashesFromSegments()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        assert scope.uri("/users/", "/12/").equals(URI.create("https://example.com/api/users/12"));
    }



    public static void UriEncodesSpaces()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        assert scope.uri("user files", "profile image").equals(URI.create("https://example.com/api/user%20files/profile%20image"));
    }



    public static void UriEncodesSegmentSlashes()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        assert scope.uri("users/12").equals(URI.create("https://example.com/api/users%2F12"));
    }



    public static void SuburlBuildsRequestForPath()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        HttpRequest request = scope
            .suburl("users", 12)
            .build()
        ;

        assert request.uri().equals(URI.create("https://example.com/api/users/12"));
    }



    public static void GetBuildsGetRequest()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        HttpRequest request = scope
            .get("users")
            .build()
        ;

        assert request.method().equals("GET");
        assert request.uri().equals(URI.create("https://example.com/api/users"));
    }



    public static void PostBuildsPostRequest()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        HttpRequest request = scope
            .post("users")
            .build()
        ;

        assert request.method().equals("POST");
        assert request.uri().equals(URI.create("https://example.com/api/users"));
    }



    public static void PatchBuildsPatchRequest()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        HttpRequest request = scope
            .patch("{}", "users", 12)
            .build()
        ;

        assert request.method().equals("PATCH");
        assert request.uri().equals(URI.create("https://example.com/api/users/12"));
    }



    public static void PutBuildsPutRequest()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        HttpRequest request = scope
            .put(HttpRequest.BodyPublishers.ofString("{}"), "users", 12)
            .build()
        ;

        assert request.method().equals("PUT");
        assert request.uri().equals(URI.create("https://example.com/api/users/12"));
    }



    public static void DeleteBuildsDeleteRequest()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        HttpRequest request = scope
            .delete("users", 12)
            .build()
        ;

        assert request.method().equals("DELETE");
        assert request.uri().equals(URI.create("https://example.com/api/users/12"));
    }



    public static void MethodBuildsCustomRequest()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        HttpRequest request = scope
            .method("OPTIONS", HttpRequest.BodyPublishers.noBody(), "users")
            .build()
        ;

        assert request.method().equals("OPTIONS");
        assert request.uri().equals(URI.create("https://example.com/api/users"));
    }



    public static void PostJSONAddsContentType()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        HttpRequest request = scope
            .postJSON("{\"ok\":true}", "users")
            .build()
        ;

        assert request.method().equals("POST");
        assert request.uri().equals(URI.create("https://example.com/api/users"));
        assert request.headers().firstValue("Content-Type").orElse("").equals("application/json");
    }



    public static void PatchJSONAddsContentType()
    {
        AddressScope scope = new AddressScope(URI.create("https://example.com/api"));

        HttpRequest request = scope
            .patchJSON("{\"ok\":true}", "users", 12)
            .build()
        ;

        assert request.method().equals("PATCH");
        assert request.uri().equals(URI.create("https://example.com/api/users/12"));
        assert request.headers().firstValue("Content-Type").orElse("").equals("application/json");
    }

}