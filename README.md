# CS5011 Practical 3

## Report

The report can be found in `doc/report.pdf`.

## Running the project

Running the project (without tests etc.):
```bash
todo
```

To run the unit tests and the evaluation, please refer to
the commands provided in the Makefile (see below).

## Project structure

```
+ doc:          Report, test coverage, etc.
+ evaluation:   Evaluation scripts
+ lib:          Dependencies (for unit testing and evaluation)
+ src:          Source code
+ test:         Unit tests
```

## Makefile

The following commands are available from the root directory of the project
and can be called using the make command.

Make sure to compile (`make compile`) the project before using any of the other commands.

```
compile      Compile all java files
clean        Remove java class files
test         Run JUnit tests
stacscheck   Run stacscheck checks (only on school server)
evaluation   Run the evaluation (run experiments, create plots)
help         Print available commands
```
