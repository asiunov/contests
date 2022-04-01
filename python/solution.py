from __future__ import annotations

import os
import sys
import time
from contextlib import ContextDecorator, contextmanager
from typing import List

IS_DEBUG = bool(os.environ.get("LOCAL_RUN"))

RUN_N_TESTS_IN_PROD = True


def solve(r: Reader) -> None:
  print(r.next_int())


def main(r: Reader) -> None:
  with timer("main"):
    t = r.next_int() if RUN_N_TESTS_IN_PROD or IS_DEBUG else 1
    for i in range(t):
      solve(r)


class Reader(object):
  def __init__(self, input_file):
    self.__input_file = input_file
    self.__last_line = None

  def next_line(self) -> str:
    self.__last_line = None
    return self.__input_file.readline().split()

  def next_token(self) -> str:
    while not self.__last_line:
      self.__last_line = self.__input_file.readline().split()[::-1]
    return self.__last_line.pop()

  def next_int(self):
    return int(self.next_token())

  def next_int_array(self, n: int) -> List[int]:
    return [int(self.next_token()) for _ in range(n)]


if IS_DEBUG:
  def log(*args, **kwargs):
    print(*args, **kwargs, flush=True)


  class timer(ContextDecorator):
    def __init__(self, name: str):
      self.__name = name

    def __enter__(self):
      self.__start_time = time.time()
      return self

    def __exit__(self, exc_type, exc, exc_tb):
      log(f"Timer[{self.__name}]: {time.time() - self.__start_time:.6f} seconds")
else:
  def log(*args, **kwargs):
    pass


  @contextmanager
  def timer(name):
    yield None

if __name__ == '__main__':
  input_file = open('../input.txt') if IS_DEBUG else sys.stdin
  try:
    main(Reader(input_file))
  finally:
    input_file.close()
