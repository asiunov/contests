from __future__ import annotations

import sys
import time
from contextlib import contextmanager
import datetime
from typing import List


def solve() -> None:
  fprintln(next_int())


def global_init() -> None:
  pass


RUN_N_TESTS_IN_PROD = True
PRINT_CASE_NUMBER = False
ASSERT_IN_PROD = False

LOG_TO_FILE = False
READ_FROM_CONSOLE_IN_DEBUG = False
WRITE_TO_CONSOLE_IN_DEBUG = True
TEST_TIMER = False

IS_DEBUG = "DEBUG_MODE" in sys.argv
__output_file = None
__input_file = None
__input_last_line = None


def run() -> None:
  global __input_file, __input_last_line, __output_file
  __output_file = sys.stdout if not IS_DEBUG or WRITE_TO_CONSOLE_IN_DEBUG else open("../output.txt", "w")
  try:
    __input_file = sys.stdin if not IS_DEBUG or READ_FROM_CONSOLE_IN_DEBUG else open("../input.txt")
    try:
      with timer("total"):
        global_init()
        t = next_int() if RUN_N_TESTS_IN_PROD or IS_DEBUG else 1
        for i in range(t):
          if PRINT_CASE_NUMBER:
            fprint(f"Case #{i + 1}: ")
          if TEST_TIMER:
            with timer(f"test #{i + 1}"):
              solve()
          else:
            solve()
          if IS_DEBUG:
            __output_file.flush()
    finally:
      __input_last_line = None
      __input_file.close()
      __input_file = None
  finally:
    __output_file.flush()
    __output_file.close()


def fprint(*objects):
  print(*objects, end="", file=__output_file)


def fprintln(*objects):
  print(*objects, file=__output_file)


def next_line() -> str:
  global __input_last_line
  __input_last_line = None
  return __input_file.readline()


def next_token() -> str:
  global __input_last_line
  while not __input_last_line:
    __input_last_line = __input_file.readline().split()[::-1]
  return __input_last_line.pop()


def next_int():
  return int(next_token())


def next_float():
  return float(next_token())


def next_int_array(n: int) -> List[int]:
  return [int(next_token()) for _ in range(n)]


if IS_DEBUG or ASSERT_IN_PROD:
  def assert_predicate(p: bool, message: str = ""):
    if not p:
      raise AssertionError(message)


  def assert_not_equal(unexpected, actual):
    if unexpected == actual:
      raise AssertionError(f"assert_not_equal: {unexpected} == {actual}")


  def assert_equal(expected, actual):
    if expected != actual:
      raise AssertionError(f"assert_equal: {expected} != {actual}")
else:
  def assert_predicate(p: bool, message: str = ""):
    pass


  def assert_not_equal(unexpected, actual):
    pass


  def assert_equal(expected, actual):
    pass

if IS_DEBUG:
  __log_file = open(f"../logs/py_solution_{int(time.time() * 1000)}.log", "w") if LOG_TO_FILE else sys.stdout


  def log(*args, **kwargs):
    print(datetime.datetime.now(), "-", *args, **kwargs, flush=True, file=__log_file)


  @contextmanager
  def timer(label: str):
    start_time = time.time()
    try:
      yield
    finally:
      log(f"Timer[{label}]: {time.time() - start_time:.6f}s")
else:
  def log(*args, **kwargs):
    pass


  @contextmanager
  def timer(label: str):
    yield

if __name__ == "__main__":
  run()
