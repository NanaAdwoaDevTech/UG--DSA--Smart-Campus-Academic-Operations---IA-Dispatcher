# Ghana Smart Service Operations Optimizer
DCIT 204/308 Joint DSA Semester Project

## Author
- Name: David Edu Turkson
- Student ID: 22012947
- Assigned Component: CustomBST.java (src/structures/CustomBST.java)

## ID-derived parameters
- Index number: 22012947 → digit sum = 27
- MAX_DEPTH_THRESHOLD (CustomBST) = 27

## Structure
- `src/structures/CustomBST.java` — custom non-self-balancing BST (Section 6)
- `src/structures/MyList.java` — custom resizable array, replaces `java.util.ArrayList`/`List`

## Compliance notes
- No built-in Java collection classes are used anywhere in the custom structures (`ArrayList`, `LinkedList`, `HashMap`, `PriorityQueue`, `Vector` are all avoided). `MyList<T>` is a from-scratch dynamic array backed by a plain `Object[]`.
- `CustomBST.java` includes a runnable `main()` method that tests normal, empty-tree, duplicate-insert, and skewed-tree cases — compiled and verified locally before submission.

## Running locally
```
javac -encoding UTF-8 -d out src/structures/MyList.java src/structures/CustomBST.java
java -cp out structures.CustomBST
```
