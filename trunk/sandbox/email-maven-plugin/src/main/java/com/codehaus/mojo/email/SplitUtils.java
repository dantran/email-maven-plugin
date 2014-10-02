package com.codehaus.mojo.email;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

public class SplitUtils
{
    private static final String SEPARATORS = " ,;\r\n";

    private SplitUtils()
    {
    }

    public static List<String> splits( final String strParam, final File fileParam )
        throws IOException
    {
        ArrayList<String> list = new ArrayList<String>();

        if ( !StringUtils.isBlank( strParam ) )
        {
            String[] tokens = StringUtils.split( strParam, SEPARATORS );
            for ( String token : tokens )
            {
                list.add( token );
            }
        }

        if ( fileParam != null )
        {
            String values = FileUtils.fileRead( fileParam );
            String[] tokens = StringUtils.split( values, SEPARATORS );
            for ( String token : tokens )
            {
                list.add( token );
            }
        }

        return list;

    }

}
