package com.codehaus.mojo.email;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SplitUtilsTest
{
    private static File basedir;

    @BeforeClass
    public static void beforeClass()
    {
        basedir = new File( System.getProperty( "basedir", "." ) );
    }

    @Test
    public void testSingleColumnList()
        throws IOException
    {
        File testFile = new File( basedir, "src/test/data/single-column-email-list.txt" );

        List<String> tokens = SplitUtils.splits( null, testFile );
        Assert.assertEquals( 3, tokens.size() );
        Assert.assertEquals( "user1@a.com", tokens.get( 0 ) );
        Assert.assertEquals( "user2@a.com", tokens.get( 1 ) );
        Assert.assertEquals( "user3@a.com", tokens.get( 2 ) );

    }
}
