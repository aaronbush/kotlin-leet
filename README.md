# TrieNode

An experiment in two areas:

- learning Kotlin
- learning Trie data structure

## Example Use

Creating a trie and adding a word and checking for existence.

```kotlin
val myTrie = TrieNode()
myTrie + "hello"
if ("hello" in myTrie) println("world")
```

Adding multiple words to a trie.

```kotlin
val myTrie = TrieNode()
myTrie + "hello" + "world"
if ("hello" in myTrie && "world" in myTrie) println("hello world")
```

Removing words from a trie.

```kotlin
val myTrie = TrieNode()
myTrie + "hello"
myTrie - "hello"
if ("hello" !in myTrie) println("goodbye")
```

## Additional Information

More examples in the unit test. Also tracking issues/features to add in TODO.md

# Background

Started with exploring leetcode via Kotlin. The first problem that was presented I attempted in a brute-force manner and
then found two issues:

1. was too slow - figured as much
1. the version of Kotlin available on leetcode at the time is 1.3 (I'm writing in 1.5.x) and was difficult to keep code
   compatible.

Regardless, the problem was about efficient searching for words via prefix/suffix patterns. It seems that a Trie
structure might help here.

I had found an article (misplaced it now) prior to this which hinted that engineers should try to develop a few common
solutions to improve/round-out their skills. One was a text editor and mentioned Trie and Rope data structures. So, I
figured let's try the Trie first.

## Some links

- TODO: find that missing article 😀
- https://en.wikipedia.org/wiki/Trie
- https://medium.com/basecs/trying-to-understand-tries-3ec6bede0014
