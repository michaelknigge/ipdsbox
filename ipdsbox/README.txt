
generell:

- alle Klassen verwenden nur noch den IpdsInputStream
-- dieser bietet readXxxxxx Methoden f�r alle Datentypen
--- FLAG
--- UBIN
--- BIN
--- etc.
-- Zum Lesen den DataInputStream verwenden: https://docs.oracle.com/javase/7/docs/api/java/io/DataInputStream.html



weiter:

- ggf. neue Klasse IpdsCommand
- statische create Methode, die IpdsCommand aus PagePrinterRequest baut 
-- Alternativ eine IpdsCommandBuilder Klasse (ggf. nicht statisch, ggf. mit "Executor-Map wie bei AFP")
- neues Interface IpdsCommandHandler (hat nur handle Methode)
- neue Methode ipdsCommandHandler im PagePrinterDaemonBuilder



weiter:
IpdsPrinter implementiert sp�ter beide Interfaces, da ggf. aufgrund eines PagePrinterRequest
der IpdsPrinter resettet werden muss o. �. (und muss dann zur�ck in HomeState).
