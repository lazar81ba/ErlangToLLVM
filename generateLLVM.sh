#!/bin/bash

cd bin && clang -S -emit-llvm -O3 test.c
lli test.ll
