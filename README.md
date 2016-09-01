paradoxReader
=============

[Paradox](https://en.wikipedia.org/wiki/Paradox_(database)) Database File Reader

Maven Coordinates
-------------

```
<groupId>com.khubla.pdxreader</groupId>
<artifactId>pdxreader</artifactId>
<version>1.1-SNAPSHOT</version>
<packaging>jar</packaging>
```

Using the paradoxReader from the command line
-------------

The command-line interface produces CSV from .DB files.  An example invocation of the command-line interface which produces CSV from "CONTACTS.DB" is:

`java -jar target/paradoxReader-1.0-jar-with-dependencies.jar --file=src/test/resources/CONTACTS.DB`

Using the paradoxReader in code
--------------

To use the paradoxReader in code, supply an InputStream to a .DB file, and an implementation of PDXReaderListener to the class DBTableFile.  An example from the unit tests:

```java
final InputStream inputStream = TestDBFile.class.getResourceAsStream(filename);
final DBTableFile pdxFile = new DBTableFile();
final PDXReaderListener pdxReaderListener = new PDXReaderCSVListenerImpl();
pdxFile.read(inputStream, pdxReaderListener);
```

The interface PDXReaderListener looks like this:

```java
public interface PDXReaderListener {
   void finish();

   void header(DBTableHeader pdxTableHeader);

   void record(List<DBTableValue> values);

   void start();
}
```

The `record` method will be called once per record in the table file.

Travis Status
---------

<a href="https://travis-ci.org/teverett/"paradoxReader><img src="https://api.travis-ci.org/teverett/paradoxReader.png"></a>

