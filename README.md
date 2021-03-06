# Kakuro
Application for playing and solving kakuro (A* algorithm).

# Requirements
Use `Java JDK 8`

# Autotest
Use `./autotest.sh` to run solve on examples and check if result is the same as expected.

Examples are stored in `examples/`: script runs all with `.in` and check the result with 
expected from `examples/outputs/`.

# Input
File format is:
```
<width> <height>
<R/C> <x> <y> <sum> <number of fields>
```

Example file:
```
5 5
C 2 0 17 4
C 3 0 27 4
C 4 0 16 2
R 1 1 23 3
R 1 2 24 3
C 1 2 11 2
R 0 3 18 3
R 0 4 6 3
```

For board:
```
# # 17\ 27\ 16\ 
# \23 _ _ _ 
# 11\24 _ _ _ 
\18 _ _ _ # 
\6 _ _ _ #
```

You can run app with specified input and output file by passing them as arguments. 