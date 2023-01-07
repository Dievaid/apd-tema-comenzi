====       Tema 2 APD      ====
==== David Capragiu, 334CA ====

Flow-ul temei:

    1. Am folosit ExecutorService pentru a fixa un număr maxim de thread-uri
după cum a fost specificat în enunț (Executors.newFixedThreadPool), acest număr de thread-uri este preluat ca argument la rulea programului
    Vor fi P thread-uri care se ocupă de setarea statusului de shipped în comandă și P thread-uri care fac același lucru
pentru produse

    2. Se creează workerii care se ocupă de preluarea comenzilor din fișierul "orders.txt",
specific vorbind, vom instanția câte un obiect de tipul OrderTask, unde se va pasa ca referință
un scanner comun tuturor workerilor pentru a putea avea fișierul "orders.txt" deschis o singură dată,
astfel fiecare thread putând să citească pe rând într-un bloc synchronized o linie din fișier pentru a afla
id-ul comenzii de care va trebui să ne ocupăm.

    3. Fiecare worker care citește din fișierul "orders.txt" va da submit într-un executor service cu noi task-uri,
aceste fiind legate de găsirea produselor din comandă (ProductTask), aceste task-uri noi citind din fișierul
cu stocul de produse. La găsirea unui produs dintr-o comandă se va scrie produsul curent în fișierul de output cu produse
la care va fi atașat statusul shipped, iar OrderTask-urile vor scrie comenzile cu statusul shipped.

    4. Fiecare comandă care este procesată va aștepta terminararea procesului de căutare pentru a afla dacă
este comanda întreg livrată (Mecanism implementat cu un CyclicBarrier)

    P.S: Pentru a putea determina momentul în care putem închide ExecutorService am folosit un Phaser
care are un comportament similar cu cel al barierei doar că este mult mai maleabil datorită faptului
că putem să nu știm inițial de câte "await-uri" avem nevoie.
    Am dat register fiecărui task executabil în executor service, iar la finalizarea acestora am apelat
arrive pentru fiecare task pentru a înregistra terminarea task-ului
    Practic, dacă încep să număr startul fiecărui task, va trebui să număr și câte dintre task-uri au ajuns
la final

    Pentru a nu mai pasa referințe către FileWritere, Phaser și ExecutorService ca parametri am decis să
folosesc un singleton (Controller.java), mi s-a părut mult mai simplu și mai ușor de urmărit codul astfel.