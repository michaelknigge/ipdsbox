
generell:

- alle Klassen verwenden nur noch den IpdsInputStream
-- dieser bietet readXxxxxx Methoden für alle Datentypen
--- FLAG
--- UBIN
--- BIN
--- etc.
-- Zum Lesen den DataInputStream verwenden: https://docs.oracle.com/javase/7/docs/api/java/io/DataInputStream.html
    oder besser https://www.cs.rit.edu/~ark/pj/doc/edu/rit/io/DataInputStream.html (da gibt es auch readUnsignedLong etc.)



weiter:

- ggf. neue Klasse IpdsCommand
- statische create Methode, die IpdsCommand aus PagePrinterRequest baut 
-- Alternativ eine IpdsCommandBuilder Klasse (ggf. nicht statisch, ggf. mit "Executor-Map wie bei AFP")
- neues Interface IpdsCommandHandler (hat nur handle Methode)
- neue Methode ipdsCommandHandler im PagePrinterDaemonBuilder



weiter:
IpdsPrinter implementiert später beide Interfaces, da ggf. aufgrund eines PagePrinterRequest
der IpdsPrinter resettet werden muss o. ä. (und muss dann zurück in HomeState).



Struktur:

mk.ipdsbox.ipds

mk.ipdsbox.ipds.printer
--> IpdsPrinter, States, ...

mk.ipdsbox.ipds.exceptions
--> Exceptions - aber nur wenn es mehrere gibt, sonst in common

mk.ipdsbox.ipds.common
--> Allgemeines Zeuchs - gilt es aber so weit es geht zu vermeiden....

mk.ipdsbox.ipds.util
--> Byte und Bit-Parserei

mk.ipdsbox.ipds.commandset.devicecontrol
--> Device Control Set

mk.ipdsbox.ipds.commandset.text
--> Text Control Set

mk.ipdsbox.ipds.commandset.imimage
--> IM-Image Control Set

mk.ipdsbox.ipds.commandset.ioimage
--> IO-Image Control Set

mk.ipdsbox.ipds.commandset.graphics
--> Graphics Control Set

mk.ipdsbox.ipds.commandset.barcode
--> Bar Code Control Set

mk.ipdsbox.ipds.commandset.objectcontainer
--> Object Container Control Set

mk.ipdsbox.ipds.commandset.overlay
--> Overlay Control Set

mk.ipdsbox.ipds.commandset.pagesegment
--> Page Segment Control Set

mk.ipdsbox.ipds.commandset.loadedfont
--> Loaded Font Control Set

mk.ipdsbox.ipds.triplets
--> Triplets, ggf. aber auch in Common packen

