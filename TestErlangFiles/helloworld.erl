-module(helloworld).
-export([hello_world_test/0]).

hello_world_test() -> io:fwrite("hello, world\n").