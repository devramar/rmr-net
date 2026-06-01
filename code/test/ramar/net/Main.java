package test.ramar.net;

import dev.ramar.test.TestUtil;

public class Main
{

    public static void main(String[] args)
    {
        TestUtil.Test(
            AddressScopeTests.class,
            MimeFilesTests.class
        );
    }

}