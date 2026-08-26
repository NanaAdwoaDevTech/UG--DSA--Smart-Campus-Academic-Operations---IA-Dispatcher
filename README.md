# Ghana Smart Service Operations Optimizer
DCIT 204/308 Joint DSA Semester Project

## ID-derived parameters
- Index number: 22012947 → digit sum = 27
- MAX_DEPTH_THRESHOLD (CustomBST_22012947) = 27

## Structure
- `src/structures/CustomBST_22012947.java` — custom non-self-balancing BST (Section 6), authored by index number 22012947
- `src/structures/MyList_22012947.java` — custom resizable array, replaces `java.util.ArrayList`/`List`

Files are suffixed with the author's index number per team convention, so individual contributions stay distinct in the shared repository.

## Compliance notes
- No built-in Java collection classes are used anywhere in the custom structures (`ArrayList`, `LinkedList`, `HashMap`, `PriorityQueue`, `Vector` are all avoided). `MyList_22012947<T>` is a from-scratch dynamic array backed by a plain `Object[]`.
- `CustomBST_22012947.java` includes a runnable `main()` method that tests normal, empty-tree, duplicate-insert, and skewed-tree cases — compiled and verified locally before submission.

## Running locally
```
javac -encoding UTF-8 -d out src/structures/MyList_22012947.java src/structures/CustomBST_22012947.java
java -cp out src.structures.CustomBST_22012947
```
