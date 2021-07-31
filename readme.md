Serwis konta

Serwis udostępnia API do zakładania konta wraz z subkontami walutowymi.
Serwis umożliwia wymianę środków pomiędzy subkontami walutowymi, z uwzględnieniem 
kursu wymiany dla zakupu i sprzedaży

 - założenie konta użytkownika
 endpoint: http://localhost:8089/accounts - metoda POST

 - pobranie informacji o koncie
 endpoint: http://localhost:8089/accounts/{pesel} - metoda GET

 - konwersję walut pomiędzy istniejącymi subkontami
 endpoint: http://localhost:8089/accounts/exchange - metoda POST

 - dodanie nowego subkonta walutowego (waluta musi istnieć w tabelach NBP)
 endpoint: http://localhost:8089/accounts/subaccounts - metoda POST

Dokumentacja endpointów dla projektu: http://localhost:8089/swagger-ui.html

Panel administracyjny bazy: http://localhost:8089/h2-console
 - użytkownik: root
 - hasło: qaz123

Przydatne podczas testów i oceny:
 - generatory numerów pesel: https://pesel.cstudios.pl/o-generatorze/generator-on-line

