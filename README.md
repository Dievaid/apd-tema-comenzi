====       Tema 2 APD      ====
==== David Capragiu, 334CA ====

Flow-ul temei:

    1. Am folosit ExecutorService pentru a fixa un număr maxim de thread-uri
după cum a fost specificat în enunț (Executors.newFixedThreadPool), acest număr de thread-uri
este preluat ca argument la rulea programului

    2. Se creează workerii care se ocupă de preluarea comenzilor din fișierul "orders.txt",
specific vorbind, vom instanția câte un obiect de tipul OrderTask, unde se va pasa ca referință
un scanner comun tuturor workerilor pentru a putea avea fișierul "orders.txt" deschis o singură dată,
astfel fiecare thread putând să citească pe rând într-un bloc synchronized o linie din fișier pentru a afla
id-ul comenzii de care va trebui să ne ocupăm.

    3. Fiecare worker care citește din fișierul "orders.txt" va da submit în executor service cu noi task-uri,
aceste fiind legate de găsirea produselor din comandă (ProductTask), aceste task-uri noi citind din fișierul
cu stocul de produse. La găsirea unui produs dintr-o comandă se va da submit în executor service cu un task de
scrierea în fișierul cu produse livrate (ProductWriter), iar în momentul în care pentru id-ul unei comenzi atingem numărul
de produse pe care îl are vom lansa un task de scriere în fișierul de comenzi livrate (OrderWriter)


    P.S: Pentru a putea determina momentul în care putem închide ExecutorService am folosit un Phaser
care are un comportament similar cu cel al barierei doar că este mult mai maleabil datorită faptului
că putem să nu știm inițial de câte "await-uri" avem nevoie.

    Pentru a nu mai pasa referințe către FileWritere, Phaser și ExecutorService ca parametri am decis să
folosesc un singleton (Controller.java), mi s-a părut mult mai simplu și mai ușor de urmărit codul astfel.