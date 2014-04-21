paradoxReader
=============

Paradox DB File Reader

Using the paradoxReader
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

