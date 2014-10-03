package com.codehaus.mojo.email;

/*
 * The MIT License
 *
 * Copyright (c) 2004, The Codehaus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;

/**
 * Provides functionality for sending email.
 */
@Mojo( name = "send", requiresProject = false, threadSafe = true, aggregator = true )
public class SendMailMojo
    extends AbstractMojo
{

    /**
     * Smtp server host
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "host", required = true, defaultValue = "smtp.gmail.com" )
    private String host;

    /**
     * Smtp server port
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "port", defaultValue = "465" )
    private int port;

    /**
     * Smtp user credential
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "user" )
    private String user;

    /**
     * Smpt password credential
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "password" )
    private String password;

    /**
     * When <code>true</code>, enable SSL connection to SMTP server.
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "ssl", defaultValue = "true" )
    private boolean ssl = true;

    /**
     * Content encoding
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "charset", defaultValue = "utf-8" )
    private String charset;

    /**
     * When <code>true</code>, enable SSL connection to SMTP server.
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "startTLS", defaultValue = "true" )
    private boolean startTLS = true;

    /**
     * Subject
     */
    @Parameter( property = "subject", required = true )
    private String subject;

    /**
     * Sender
     */
    @Parameter( property = "from", required = true )
    private String from;

    /**
     * Comma/space separated 'to' list
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "to", required = true )
    private String to;

    /**
     * Comma/space separated to list in a file
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "toFile" )
    private File toFile;

    /**
     * Comma/space separated 'cc' list
     */
    @Parameter( property = "cc", defaultValue = "" )
    private String cc;

    /**
     * Comma/space separated 'cc' list in a file
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "ccFile" )
    private File ccFile;

    /**
     * Comma/space separated 'bcc' list
     */
    @Parameter( property = "bcc", defaultValue = "" )
    private String bcc;

    /**
     * Comma/space separated 'bcc' list in a file
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "bccFile" )
    private File bccFile;

    /**
     * Maximum number to bcc recipient per send
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "maxBccCountPerSend", defaultValue = "99" )
    private int maxBccCountPerSend;

    /**
     * Simple message to send out
     */
    @Parameter( property = "message", defaultValue = "" )
    private String message = "";

    /**
     * Message content from a file
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "messageFile" )
    private File messageFile;

    /**
     * Comma separated files to attach to the mail
     */
    @Parameter( property = "attachments" )
    private String attachments;

    /**
     * Current user system settings for use in Maven.
     *
     * @since 1.0 beta 1
     */
    @Parameter( defaultValue = "${settings}", readonly = true )
    private Settings settings;

    /**
     * When <code>true</code>, skip the execution.
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "skip", defaultValue = "false" )
    private boolean skip = false;

    /**
     * Send using HTML content.
     *
     * @since 1.0 beta 1
     */
    @Parameter( property = "html", defaultValue = "false" )
    private boolean html = false;

    /**
     * MNG-4384
     *
     * @since 1.0 beta 1
     */
    @Component( hint = "mng-4384" )
    private SecDispatcher securityDispatcher;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            if ( this.messageFile != null )
            {
                this.message = FileUtils.fileRead( this.messageFile, this.charset );
            }

            Server server = getServerSettings( host + ":" + port );

            List<String> bccList = SplitUtils.splits( bcc, bccFile );

            while ( true )
            {
                List<String> bccTokens = new ArrayList<String>();
                for ( int i = 0; i < this.maxBccCountPerSend; ++i )
                {
                    if ( bccList.size() != 0 )
                    {
                        bccTokens.add( bccList.get( 0 ) );
                        bccList.remove( 0 );
                    }
                }

                //very hacky here where we repeat the 'to', 'cc' list per bcc block
                send( server, bccTokens );
                if ( bccList.size() == 0 )
                {
                    break;
                }
            }


        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Unable to send out email", e );
        }
    }

    private void send( Server server, List<String> bccList )
        throws EmailException, IOException
    {
        MultiPartEmail email = new MultiPartEmail();
        if ( html )
        {
            email = new HtmlEmail();
        }

        email.setCharset( this.charset );

        email.setHostName( this.host );
        email.setSmtpPort( this.port );
        email.setAuthenticator( new DefaultAuthenticator( server.getUsername(), server.getPassword() ) );
        email.setSSLOnConnect( this.ssl );
        email.setStartTLSEnabled( this.startTLS );

        if ( !StringUtils.isBlank( this.from ) )
        {
            email.setFrom( this.from );
        }

        this.configureToList( email, SplitUtils.splits( to, toFile ) );
        this.configureCcList( email, SplitUtils.splits( cc, ccFile ) );
        this.configureBccList( email, bccList );

        this.configureAttachments( email );

        email.setSubject( this.subject );
        email.setMsg( this.message );

        email.send();
    }

    private Server getServerSettings( String serverId )
        throws SecDispatcherException
    {
        Server server = this.settings.getServer( serverId );

        if ( server != null )
        {
            if ( server.getPassword() != null )
            {
                server.setPassword( securityDispatcher.decrypt( server.getPassword() ) );
            }
        }
        else
        {
            server = new Server();
        }

        if ( StringUtils.isBlank( server.getUsername() ) )
        {
            server.setUsername( this.user );
        }

        if ( StringUtils.isBlank( server.getPassword() ) )
        {
            server.setPassword( this.password );
        }
        return server;
    }

    private void configureToList( Email email, List<String> tokens )
        throws EmailException, IOException
    {
        for ( String token : tokens )
        {
            email.addTo( token );
        }
    }

    private void configureCcList( Email email, List<String> tokens )
        throws EmailException, IOException
    {
        for ( String token : tokens )
        {
            email.addCc( token );
        }
    }

    private void configureBccList( Email email, List<String> tokens )
        throws EmailException, IOException
    {
        for ( String token : tokens )
        {
            email.addBcc( token );
        }
    }

    private void configureAttachments( MultiPartEmail email )
        throws EmailException, IOException
    {
        List<String> tokens = SplitUtils.splits( this.attachments, null );
        for ( String token : tokens )
        {
            EmailAttachment attachment = new EmailAttachment();
            File file = new File( token );
            attachment.setPath( file.getCanonicalPath() );
            attachment.setDisposition( EmailAttachment.ATTACHMENT );
            attachment.setDescription( file.getName() );
            attachment.setName( file.getName() );

            email.attach( attachment );
        }
    }

}