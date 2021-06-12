package yr21.may.week1

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class TrieNode {
    private companion object {
        // Remap the ordinal values to array indexing values 0..25 for US Alphabet
        @JvmStatic
        private val letterToOrdinal = CharRange('a', 'z').withIndex().associate { (index, value) -> value to index }

        @JvmStatic
        private val ordinalToLetter = letterToOrdinal.entries.associate { (k, v) -> v to k }

        private fun Char.toAlphaNum() = letterToOrdinal[this]
        private fun Int.toAlphaChar() = ordinalToLetter[this] ?: '_'
    }

    private val children = arrayOfNulls<TrieNode>(letterToOrdinal.size)
    private var endOfWord = false

    fun addWord(word: String) {
        if (word == "") return
        val nextLetter = word.drop(1)
        val nextNode = this.add(word.first(), nextLetter == "")
        return nextNode.addWord(nextLetter)
    }

    fun deleteWord(word: String): Result<String> {
        val endOfWordNode = findEndOfWord(word) ?: return Result.failure(IllegalArgumentException("$word not found"))
        endOfWordNode.endOfWord = false
        // TODO: what about case where path to this node is not a word?  Do we need to clean up anything?
        return Result.success(word)
    }

    private fun add(value: Char, endOfWord: Boolean): TrieNode {
        logger.debug {
            "attempting to add $value and is ${if (endOfWord) "" else "not"} end of word"
        }
        val index = value.toAlphaNum() ?: throw IllegalArgumentException("Value: $value not supported")
        var result = children[index]
        if (result == null) {
            result = TrieNode()
            children[index] = result
        }
        if (endOfWord) this.endOfWord = endOfWord
        return result
    }

    private fun findEndOfWord(word: String): TrieNode? {
        logger.debug { "searching for '$word'" }
        if (word == "") return null
        val nextNode = this[word[0]] ?: return null
        val nextWord = word.drop(1)
        logger.debug { "found '${word[0]}' (end of a word=${endOfWord}) next is '$nextWord'" }
        return if (nextWord == "" && this.endOfWord) this else nextNode.findEndOfWord(nextWord)
    }

    fun contains(word: String): Boolean {
        return findEndOfWord(word) != null
    }

    operator fun get(value: Char) =
        children[value.toAlphaNum() ?: throw IllegalArgumentException("Value: $value not supported")]

    override fun toString(): String {
        fun nextLevel(node: TrieNode): String {
            val content = node.children.mapIndexed { i, v -> v?.let { i.toAlphaChar() } ?: '_' }.joinToString()
            val iResult = "TrieNode(children=${content}\n"
            return iResult + node.children.filterNotNull().joinToString("\n") { nextLevel(it) }
        }
        return nextLevel(this)
    }
}

fun main() {
    val root = TrieNode()
    root.addWord("alpha")
    println(root)
    println("Searching for alpha: ${root.contains("alpha")}")
    println("Searching for alpo: ${root.contains("alpo")}")
    println("Searching for alp: ${root.contains("alp")}")

    println(CharRange('a', 'z') + ' ')
}