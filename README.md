![CI](https://github.com/teverett/paradoxReader/workflows/CI/badge.svg)
[![DepShield Badge](https://depshield.sonatype.org/badges/teverett/paradoxReader/depshield.svg)](https://depshield.github.io)

paradoxReader
=============

[Paradox](https://en.wikipedia.org/wiki/Paradox_(database)) Database File Reader

Maven Coordinates
-------------

```
<groupId>com.khubla.pdxreader</groupId>
<artifactId>pdxreader</artifactId>
<version>1.5</version>
<packaging>jar</packaging>
```

Using the paradoxReader from the command line
-------------

The command-line interface produces CSV from .DB files.  An example invocation of the command-line interface which produces CSV from "CONTACTS.DB" is:

`java -jar target/paradoxReader-1.0-jar-with-dependencies.jar --file=src/test/resources/CONTACTS.DB`

Using the paradoxReader in code
--------------

To use the paradoxReader in code, supply an InputStream to a .DB file, and an implementation of PDXTableListener to the class DBTableFile.  An example from the unit tests:

```java
final InputStream inputStream = TestDBFile.class.getResourceAsStream(filename);
final DBTableFile pdxFile = new DBTableFile();
final PDXTableListener pdxTableListener = new MyPDXTableListener();
pdxFile.read(inputStream, pdxTableListener);
```

The interface PDXTableListener looks like this:

```java
public interface PDXTableListener {
   void finish();

   void header(DBTableHeader pdxTableHeader);

   void record(List<DBTableValue> values);

   void start(String filename);
}
```

The `record` method will be called once per record in the table file.

SQL Output
--------------

Versions 1.6+ contain the class `PDXTableReaderSQLListenerImpl`, a class which produces `CREATE` and `INSERT` SQL.
 


