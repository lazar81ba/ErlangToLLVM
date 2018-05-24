Kompilacja kodu z C do LLVM IL (potrzebny clang)
-----------------
~~~~
clang -S -emit-llvm -O3 program.c
~~~~

Sprawdzenie, czy program działa:
~~~~
lli program.ll
~~~~

Kompilacja do kodu natywnego:
~~~~
llc -filetype=obj program.ll
clang program.o -o program
~~~~

Odpalanie skompilowanego programu
~~~~
$ ./program
~~~~