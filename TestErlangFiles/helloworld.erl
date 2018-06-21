main() ->
      test(),
      test(),
      a = 1+2,
      io:fwrite(a).

test() ->
    io:fwrite(3+1).

test2() ->
    io:fwrite("test").
