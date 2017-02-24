
Program PXFMT;

(*
    This program will list the field values in the given table.

      Usage:  PXFMT <tablename.db>

    The file name's extension is mandatory.  It also works with index
    files, and displays the index record's three additional fields.

    It will stop if there are any errors.
 *)

uses  Dos, Crt, Objects;

const
    { Paradox codes for field types }
    pxfAlpha        = $01;
    pxfDate         = $02;
    pxfShort        = $03;
    pxfLong         = $04;
    pxfCurrency     = $05;
    pxfNumber       = $06;
    pxfLogical      = $09;
    pxfMemoBLOb     = $0C;
    pxfBLOb         = $0D;
    pxfFmtMemoBLOb  = $0E;
    pxfOLE          = $0F;
    pxfGraphic      = $10;
    pxfTime         = $14;
    pxfTimestamp    = $15;
    pxfAutoInc      = $16;
    pxfBCD          = $17;
    pxfBytes        = $18;


type
    { field information record used in TPxHeader below }
    PFldInfoRec         = ^TFldInfoRec;
    TFldInfoRec         =  RECORD
        fType   : byte;
        fSize   : byte;
    end;


    PPxHeader           = ^TPxHeader;
    TPxHeader           =  RECORD
        recordSize              :  word;
        headerSize              :  word;
        fileType                :  byte;
        maxTableSize            :  byte;
        numRecords              :  longint;
        nextBlock               :  word;
        fileBlocks              :  word;
        firstBlock              :  word;
        lastBlock               :  word;
        unknown12x13            :  word;
        modifiedFlags1          :  byte;
        indexFieldNumber	:  byte;
        primaryIndexWorkspace   :  pointer;
        unknownPtr1A            :  pointer;
        unknown1Ex20            :  array[$001E..$0020] of byte;
        numFields               :  integer;
        primaryKeyFields        :  integer;
        encryption1             :  longint;
        sortOrder               :  byte;
        modifiedFlags2          :  byte;
        unknown2Bx2C            :  array[$002B..$002C] of byte;
        changeCount1            :  byte;
        changeCount2            :  byte;
        unknown2F               :  byte;
        tableNamePtrPtr         : ^pchar;
        fldInfoPtr              :  PFldInfoRec;
        writeProtected          :  byte;
        fileVersionID           :  byte;
        maxBlocks               :  word;
        unknown3C               :  byte;
        auxPasswords            :  byte;
        unknown3Ex3F            :  array[$003E..$003F] of byte;
        cryptInfoStartPtr       :  pointer;
        cryptInfoEndPtr         :  pointer;
        unknown48               :  byte;
        autoIncVal              :  longint;
        unknown4Dx4E            :  array[$004D..$004E] of byte;
        indexUpdateRequired     :  byte;
        unknown50x54            :  array[$0050..$0054] of byte;
        refIntegrity            :  byte;
        unknown56x57            :  array[$0056..$0057] of byte;
        case INTEGER of
          3:   (fieldInfo35     :  array[1..255] of TFldInfoRec);
          4:   (fileVerID2      :  integer;
                fileVerID3      :  integer;
                encryption2     :  longint;
                fileUpdateTime  :  longint;  { 4.0 only }
                hiFieldID       :  word;
                hiFieldIDinfo   :  word;
                sometimesNumFields:integer;
                dosCodePage     :  word;
                unknown6Cx6F    :  array[$006C..$006F] of byte;
                changeCount4    :  integer;
                unknown72x77    :  array[$0072..$0077] of byte;
                fieldInfo       :  array[1..255] of TFldInfoRec);

      { This is only the first part of the file header.  The last field
        is described as an array of 255 elements, but its size is really
        determined by the number of fields in the table.  The actual
        table header has more information that follows. }

    end;


    PDataBlock  = ^TDataBlock;
    TDataBlock  =  RECORD
        nextBlock     : word;
        blockNumber   : word;
        addDataSize   : integer;
        fileData      : array[0..$0FF9] of byte;
        { fileData size varies according to maxTableSize }
    end;




procedure ConvertPxField(var N;  F: PFldInfoRec);
{ This will convert both ways, but blanks will be turned to zeroes. }
{ Warning:  Not all field types are converted. }
type TNRec= array[0..16] of byte;
var  i    : integer;
     size : integer;
     NRec : TNRec;

    function ItsBlank : boolean;
    var  i : integer;
    begin
      ItsBlank := TRUE;
      For i := 0 to pred(size) do If TNRec(N)[i] <> 0 then ItsBlank := FALSE;
    end;

begin
  If F^.fType = pxfBCD then { BCD field size value not used for field size }
    size := 17
   else
    size := F^.fSize;
  If (F^.fType in [pxfDate..pxfNumber, pxfTime..pxfAutoInc]) and
     not ItsBlank  { leave blank fields as all zeroes }
   then
    begin
    TNRec(N)[0] := TNRec(N)[0] xor $80;
    For i := 0 to pred(size) do
      NRec[pred(size-i)] := TNRec(N)[i];
    Move(NRec, N, size);
    end;
end;


procedure ConvertPxRecord(Hdr: PPxHeader; P: pointer);
const  IndexF : TFldInfoRec = (fType: pxfShort;  fSize: sizeof(INTEGER));
var  i    : integer;
     F    : PFldInfoRec;
begin
  F := Hdr^.fldInfoPtr;  { begin with the first field identifier }
  For i := 1 to Hdr^.numFields do
    begin
    ConvertPxField(P^, F);
    If F^.fType = pxfBCD then { BCD field size value not used for field size }
      Inc(ptrrec(P).ofs, 17)
     else
      Inc(ptrrec(P).ofs, F^.fSize);
    Inc(ptrrec(F).ofs, sizeof(F^));
    end;
  If Hdr^.fileType = 1 then  { convert primary index information }
    begin
    For i := 1 to 3 do
      begin
      ConvertPxField(P^, @IndexF);
      Inc(ptrrec(P).ofs, 2);
      end;
    end;
end;


procedure WritePxField(var N;  F: PFldInfoRec);
{ not all field types are supported here }
var  i    : integer;
     A    : string;
begin
  Case F^.fType of
    pxfAlpha, pxfMemoBLOb:
      begin
      Move(N, A[1], F^.fSize);
      A[0] := char(F^.fSize);
      For i := length(A) downto 1 do
        If (A[i] = #0) then A[0] := char(pred(i));
      write('"', A, '"');
      end;
    pxfShort:             write(integer(N));
    pxfLong, pxfAutoInc:  write(longint(N));
    pxfCurrency:          write('$', double(N):1:2);
    pxfNumber:            write(double(N):1:3);

    { the rest of the field types are not translated }
    pxfDate:              write('<Date:',longint(N),'>');
    pxfLogical:           write('<Logical:',byte(N),'>');
    pxfBLOb:              write('<BLOb>');
    pxfFmtMemoBLOb:       write('<FormattedBLOb>');
    pxfOLE:               write('<OLE>');
    pxfGraphic:           write('<Graphic>');
    pxfTime:              write('<Time:',longint(N),'>');
    pxfTimestamp:         write('<TimeStamp:',longint(N),'>');
    pxfBCD:               write('<BCD>');
    pxfBytes:             write('<Bytes>');
   else                   write('<unknown>');
    end;
end;


procedure WritePxRecord(Hdr: PPxHeader; P: pointer);
const  IndexF : TFldInfoRec = (fType: pxfShort;  fSize: sizeof(INTEGER));
var  i    : integer;
     F    : PFldInfoRec;
begin
  F := Hdr^.fldInfoPtr;  { begin with the first field identifier }
  For i := 1 to Hdr^.numFields do
    begin
    If i > 1 then write(', ');
    WritePxField(P^, F);
    If F^.fType = pxfBCD then { BCD field size value not used for field size }
      Inc(ptrrec(P).ofs, 17)
     else
      Inc(ptrrec(P).ofs, F^.fSize);
    Inc(ptrrec(F).ofs, sizeof(F^));
    end;
  If Hdr^.fileType = 1 then  { display primary index information }
    begin
    For i := 1 to 3 do
      begin
      If i = 1 then write(';  index fields: ') else write(', ');
      WritePxField(P^, @IndexF);
      Inc(ptrrec(P).ofs, 2);
      end;
    end;
  writeln;
end;


procedure ReadBlock(var S: TStream; Hdr: PPxHeader; var AData );
begin
  S.Read(AData, Hdr^.maxTableSize * $0400)
end;


procedure SeekBlock(var S: TStream; Hdr: PPxHeader; ABlock: word);
var  L   : longint;
begin
  L := ABlock;
  L := (L * Hdr^.maxTableSize * $0400) + Hdr^.headerSize;
  S.Seek(L);
end;


procedure ReadAllRecords(var S: TStream);
var  i      : integer;
     num,z  : word;
     Block  : PDataBlock;
     F      : TFldInfoRec;
     Hdr    : PPxHeader;

    procedure writeAmt(L: longint; S: string);
    begin
      write(L, S);
      If L <> 1 then write('s');
      writeln;
    end;

    function  FileFormatIsOK : boolean;
    begin
      FileFormatIsOK := (Hdr^.maxTableSize >= 1) and (Hdr^.maxTableSize <= 4)
    end;

    function  FileIsEncrypted : boolean;
    begin
      If (Hdr^.fileVersionID <= 4) or not (Hdr^.fileType in [0,2,3,5]) then
        FileIsEncrypted := (Hdr^.encryption1 <> 0)
       else
        FileIsEncrypted := (Hdr^.encryption2 <> 0)
    end;

begin
  New(Hdr);
  S.Seek(0);
  S.Read(Hdr^, sizeof(Hdr^));
  If (S.Status = stOK) and FileFormatIsOK then
    begin

    { assign the header's fldInfoPtr field }
    If (Hdr^.fileVersionID <= 4) or not (Hdr^.fileType in [0,2,3,5]) then
      Hdr^.fldInfoPtr := addr(Hdr^.fieldInfo35)
     else
      Hdr^.fldInfoPtr := addr(Hdr^.fieldInfo);

    writeAmt(Hdr^.numRecords, ' record');
    writeAmt(Hdr^.fileBlocks, ' block');

    If FileIsEncrypted then
      writeln('This file is encrypted.')
     else
      begin
      New(Block);
      num := 0;
      While (S.Status = stOK) and (num < Hdr^.fileBlocks) do
        begin
        SeekBlock(S, Hdr, num);
        ReadBlock(S, Hdr, Block^);
        If (S.Status = stOK) and (Block^.addDataSize >= 0) then
          begin
          z := 0;
          For i := 0 to (Block^.addDataSize div Hdr^.recordSize) do
            begin
            ConvertPxRecord(Hdr, addr(Block^.fileData[z]));
            WritePxRecord(Hdr, addr(Block^.fileData[z]));
            Inc(z, Hdr^.recordSize);
            end;
          end;
        Inc(num);
        end;

      Dispose(Block);
      end;

    end;
end;


var  Stream   : TBufStream;

Begin
  Assign(Output, '');
  Rewrite(Output);
  Stream.Init(paramstr(1), stOpenRead, 4096);
  ReadAllRecords(Stream);
  If Stream.Status <> stOK then
    writeln(^M^J'Error:  Status=', Stream.Status, ';  Error=', Stream.ErrorInfo);
  Stream.Done;
End.
