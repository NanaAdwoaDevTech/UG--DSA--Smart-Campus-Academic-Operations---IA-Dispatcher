# 🏛️ UG Smart Academic & IA Alert Dispatcher
### DCIT 204/308: Joint DSA Semester Project
**Department of Computer Science, University of Ghana**
**Project Leader:** Nana Adwoa Aforo Osei

---

## ⚠️ Read This Before You Touch Any Code

This README is the single source of truth for how this project is organized, who is responsible for what, and what is required before your code is allowed into `main`. It assumes you've already read the full assignment brief and that you know how to use Git and GitHub (clone, branch, commit, push, open a PR). If any of that is unfamiliar, sort that out before starting — this document will not walk you through Git basics.

Every rule below exists for a reason: **this project is deliberately built so that no two members' code can be identical, and so that no one can submit fully generic AI-generated code without modification.** Read Step 4 carefully — it explains exactly how that is checked.

---

## 📌 STEP 1: Find Your Assigned Task & ID Parameter

Locate your name in the table below. This tells you three things: **which file(s) you own**, **what you're building**, and **the specific numeric parameter you must derive from your own Student ID and hard-code into your solution**.

Do not use someone else's parameter. Do not skip deriving it yourself, even if the final number is written out for you below — you are expected to be able to explain, on request, exactly how that number was calculated from your ID.

| # | Name | Student ID | Assigned Task / File(s) | ID Parameter Derivation Rule |
|---|------|-----------|--------------------------|-------------------------------|
| 1 | Nana Adwoa Aforo Osei | 22028283 | Group Leader / PM | Oversees overall architecture, interfaces, and integration. |
| 2 | Benedict Frimpong | 22390234 | `data/locations.csv` & `data/roads.csv` | Initial batch load size = Sum of ID digits (2+2+3+9+0+2+3+4) = **25** records |
| 3 | Glorious James Okyere | 22031299 | `database/schema.sql` & `docs/M1_Context.docx` | Initial resource capacity seed = Sum of last 3 ID digits (2+9+9) = **20** |
| 4 | Donkoh Kwasi Kyei | 22302834 | `src/structures/CustomArrayList.java` | Initial array capacity = Sum of ID digits (2+2+3+0+2+8+3+4) = **24** |
| 5 | Hammond Emmanuel Adukwei | 22400734 | `src/structures/CustomLinkedList.java` | Custom iterator step size = Last digit (4) + 1 = **5** |
| 6 | Adjei Asaph Adjetey | 22242385 | `src/structures/CustomStack.java` | Undo/Audit log stack depth = Last 2 digits = **85** |
| 7 | Godlove Agyei Sarfo | 22263864 | `src/structures/CustomQueue.java` | Circular queue max size = Last 3 digits (864) % 100 = **64** |
| 8 | Hellena Osei Tuah Addo | 22414374 | `src/structures/CustomDeque.java` | Priority jump threshold = Last 2 digits = **74** |
| 9 | Jeff Akubea | 22411677 | `src/structures/CustomHashTable.java` | Initial array size = First prime > last 3 digits (677) = **683** |
| 10 | David Edu Turkson | 22012947 | `src/structures/CustomBST.java` | Max tree depth check threshold = Sum of ID digits = **27** |
| 11 | Timothy Donkor Kyebambo | 22370734 | `src/algorithms/CustomSearch.java` | Target binary search seed = Last 4 digits = **0734** |
| 12 | Anefo Israel | 22299195 | `src/algorithms/SimpleSorts.java` | Insertion sort threshold = Last 2 digits (95) % 20 = **15** |
| 13 | Sarpong Malvin Sarfo | 22300217 | `src/algorithms/AdvancedSorts.java` | Quicksort pivot offset = Last digit = **7** |
| 14 | Daniella Kalevor | 22405426 | `src/algorithms/CampusGraph.java` | Dijkstra traffic penalty multiplier = Last 2 digits (26) / 10 = **2.6** |

**If your name is not on this list, or you believe you've been assigned the wrong row, contact the Group Leader immediately — do not just pick a task yourself.**

---

## 📌 STEP 2: What You Need to Do

You own your module **end-to-end**. That means you are responsible for all of the following, not just "the code":

### 1. Core Source File (`.java`)
Write your assigned custom data structure or algorithm inside the exact folder specified in the table above (e.g. `/src/structures/`, `/src/algorithms/`, `/data/`, `/database/`).

### 2. Built-in Java Collection Ban
You **CANNOT** use any of the following inside your core logic:
- `java.util.ArrayList`
- `java.util.LinkedList`
- `java.util.HashMap`
- `java.util.PriorityQueue`
- `java.util.Vector`
- `java.util.Stack`

Everything must be built from scratch using **primitive arrays or node pointers only**. If your file imports `java.util.*` for your core structure, it will be rejected — no exceptions.

### 3. Executable Test Class
Every file you submit must contain a working `main()` method (or a proper unit test runner) that demonstrates your structure handling all three of the following:
- **Normal inputs** — standard additions, insertions, lookups, etc.
- **Boundary cases** — resizing events, single-element structures, structure at capacity.
- **Invalid / empty inputs** — e.g. popping an empty stack, searching an empty tree, dequeuing from an empty queue.

A file with no test evidence is treated as incomplete, even if the core logic looks fine.

### 4. Report & Trace Documentation
Produce the trace table or structural diagram for your component, for inclusion in the final technical report. Don't leave this for the last minute — it's part of your deliverable, not an optional extra.

---

## 📌 STEP 3: Mandatory Pre-Pull Request Checklist

**Before you open a Pull Request against `main`, you must personally verify your own code works 100%.** Do not submit half-written, unverified, or broken code and expect the Group Leader to debug it for you. PRs that fail this checklist will be rejected and sent back — this wastes everyone's time, so check it yourself first.

Go through every box below before you click "Create Pull Request":

- [ ] **Compiles without errors** — your code compiles locally with zero syntax errors or warnings.
- [ ] **Tests pass locally** — you have actually run your file's `main()` method (or test runner) yourself and confirmed every output is correct.
- [ ] **No built-in collections used** — your structure is implemented with raw arrays or nodes only, no `java.util.*` imports for core logic.
- [ ] **Proper file location** — your file sits strictly inside its designated directory as listed in Step 1's table.
- [ ] **Clean code formatting** — clear, consistent variable naming and standard indentation throughout.

> **PR Policy:** Any Pull Request that fails local compilation, uses a banned Java collection class, or shows no evidence of local test execution will be **rejected immediately** and returned to you for rework. This applies with no exceptions, regardless of deadline pressure.

---

## 📌 STEP 4: How Your Work Will Be Verified

Every component will be checked for **ID Verification** — proof that you personally derived your parameter and integrated it correctly. This is the main mechanism the team uses to confirm work is genuinely your own and not copy-pasted or lifted wholesale from a generic AI output.

### Required Header + Declared Variable

At the top of your `.java` file, include a standard header comment. Inside your class, explicitly declare your ID-derived variable using the exact value from Step 1. For example:

```java
/*
 * Name: [Your Full Name]
 * Student ID: [Your Student ID]
 * Assigned Component: [Your Data Structure / Algorithm]
 * ID Derivation Rule: [Explain the math formula used]
 */

public class CustomHashTable {
    // Verification Variable derived from Student ID: 22411677 -> 683
    private static final int INITIAL_CAPACITY = 683;

    // ... your custom implementation ...
}
```

If your declared value doesn't match the formula for your actual Student ID, or if the value is missing entirely, your PR will be flagged and rejected regardless of whether the rest of the code works.

---

## 🤖 A Note on Using AI Tools

You are welcome to use AI tools (Claude, ChatGPT, Copilot, etc.) to help you learn concepts, debug errors, or get unstuck — that's a normal part of how people write code now, and no one is asking you to pretend otherwise.

**But keep this in mind:** the ID-derivation requirement in Steps 1 and 4 exists specifically because generic AI-generated solutions do not know your Student ID and will not naturally produce your specific parameter, your specific comment header, or code that matches your teammates' individually-assigned values. If you paste in a fully AI-generated file without adapting it to your actual assignment:

- It will very likely **fail Step 4 verification** outright.
- Even if it happens to pass, unedited AI output tends to look structurally identical across different people's files, which is exactly the pattern this checklist is designed to catch.
- It will not include working local test evidence unless you actually run it yourself — which you must do regardless.

**Use AI as a helper, not as a substitute for understanding and adapting your own code.** Whatever you submit needs to be something you can explain, defend, and personally verify works — because that's what Step 3's checklist and Step 4's ID check are both there to confirm.

---

*Questions about your assignment, file location, or a checklist item? Ask the Group Leader before opening a PR — not after it gets rejected.*
