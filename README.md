====       Tema 2 APD      ====
==== David Capragiu, 334CA ====

Flow-ul temei:

    1. Am folosit ExecutorService pentru a fixa un număr maxim de thread-uri
după cum a fost specificat în enunț (Executors.newFixedThreadPool), acest număr de thread-uri
este preluat ca argument la rulea programului
    Jumătate din thread-urile alocate pentru executarea programului vor lansa task-uri de procesare
a comenzilor întrucât nu se vor putea rula cele de căutare a produselor datorită cozii blocante a
executor service-ului.
    Această împărțire am gândit-o astfel deoarece dacă de exemplu rulez programul cu 8 thread-uri,
4 se vor ocupa de comenzi iar cele 4 thread-uri care se ocupă de comenzi vor putea lansa 4 task-uri
pentru produse, care mai departe vor putea lansa alte 4 astfel de task-uri și tot așa.

    2. Se creează workerii care se ocupă de preluarea comenzilor din fișierul "orders.txt",
specific vorbind, vom instanția câte un obiect de tipul OrderTask, unde se va pasa ca referință
un scanner comun tuturor workerilor pentru a putea avea fișierul "orders.txt" deschis o singură dată,
astfel fiecare thread putând să citească pe rând într-un bloc synchronized o linie din fișier pentru a afla
id-ul comenzii de care va trebui să ne ocupăm.

    3. Fiecare worker care citește din fișierul "orders.txt" va da submit în executor service cu noi task-uri,
aceste fiind legate de găsirea produselor din comandă (ProductTask), aceste task-uri noi citind din fișierul
cu stocul de produse. La găsirea unui produs dintr-o comandă se va scrie produsul curent în fișierul de output cu produse
la care va fi atașat statusul shipped, iar OrderTask-urile vor scrie comenzile cu statusul shipped.


    P.S: Pentru a putea determina momentul în care putem închide ExecutorService am folosit un Phaser
care are un comportament similar cu cel al barierei doar că este mult mai maleabil datorită faptului
că putem să nu știm inițial de câte "await-uri" avem nevoie.
    Am dat register fiecărui task executabil în executor service, iar la finalizarea acestuia am apelat
arrive pentru fiecare task

    Pentru a nu mai pasa referințe către FileWritere, Phaser și ExecutorService ca parametri am decis să
folosesc un singleton (Controller.java), mi s-a părut mult mai simplu și mai ușor de urmărit codul astfel.