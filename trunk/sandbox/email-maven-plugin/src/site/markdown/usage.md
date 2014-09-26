<!--
~~ The MIT License
~~
~~ Copyright (c) 2014, The Codehaus
~~
~~ Permission is hereby granted, free of charge, to any person obtaining a copy of
~~ this software and associated documentation files (the "Software"), to deal in
~~ the Software without restriction, including without limitation the rights to
~~ use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
~~ of the Software, and to permit persons to whom the Software is furnished to do
~~ so, subject to the following conditions:
~~
~~ The above copyright notice and this permission notice shall be included in all
~~ copies or substantial portions of the Software.
~~
~~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
~~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
~~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
~~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
~~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
~~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
~~ SOFTWARE.

-->



#Usage

  Brief examples on how to use the Email Maven Plugin goals.

## Send a simple message

In this example, you will need to have a account at gmail. Note by default, this plugin uses Google's mail server

    mvn email:send -Duser=your.gmail.username -Dpassword=your.gmail.password -Dto=your.friend@somewhere.com -Dfrom=your.email@somewhre.com -Dsubject=a.subject -Dmessage=a.message


Note:

  * 'to' settings can be a list of comma/space/semi-colon email list

  * Add '-Dcc=xxx' -Dbcc=xxx' add needed


## Hide credentials

It is not desirable to expose username and password on command line, please follow [Maven password encryption](http://maven.apache.org/guides/mini/guide-encryption.html)
to store your credentials at your Maven's local settings.xml ( ~/.m2/settings.xml)

Here is a typical example of your settings.xml's server block

    <?xml version="1.0" encoding="UTF-8"?>

    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

      [...]

      <servers>
        [...]
        <server>
          <id>smtp.gmail.com:465</id>
          <username>your.gmail.account.without@xxx</username>
          <password>{iq5l/zJzMRgHx03wPxUKfm39CoLqYsz//dNTqB9ouVQ=}</password>
        </server>
      </servers>

      [...]

    </settings>

All following examples assume your have credential stored at your local setting.xml


## Send a simple message with contents taken from an external file


    mvn email:send -Dto=xxx -Dfrom=xxx -Dsubject=xxx -DmessageFile=path.to.your.text.file


## Send a simple message with attachments


    mvn email:send -Dto=xxx -Dfrom=xxx -Dsubject=xxx -Dmessage=xxx -Dattachments=path.to.multiple.files.separeted.by.space.comma.or.semicolon


## Send a html content


    mvn email:send -Dto=xxx -Dfrom=xxx -Dsubject=xxx -DmessageFile=path.to.your.html.file -Dhtml=true


## Send a html content to the mass users using bcc


    mvn email:send -Dto=xxx -Dfrom=xxx -Dsubject=xxx -DmessageFile=path.to.your.html.file -Dhtml=true -DbccFile=path.to.file.content.user.email.list

