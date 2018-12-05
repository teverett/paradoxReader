[![Travis](https://travis-ci.org/teverett/paradoxReader.svg?branch=master)](https://travis-ci.org/teverett/paradoxReader)
[![Coverity Scan](https://img.shields.io/coverity/scan/3997.svg)](https://scan.coverity.com/projects/teverett-paradoxreader)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0879c97ba4134bacace2e0260d8547da)](https://www.codacy.com/app/teverett/paradoxReader?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=teverett/paradoxReader&amp;utm_campaign=Badge_Grade)
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

   void start();
}
```

The `record` method will be called once per record in the table file.


