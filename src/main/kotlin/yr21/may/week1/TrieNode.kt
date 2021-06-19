package yr21.may.week1

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class TrieNode {
    companion object {
        private const val ROOT_CHAR = '_'

        // Remap the ordinal values to array indexing values 0..25 for US Alphabet
        @JvmStatic
        private val letterToOrdinal =
            (CharRange('a', 'z') + ROOT_CHAR).withIndex().associate { (index, value) -> value to index }

        @JvmStatic
        private val ordinalToLetter = letterToOrdinal.entries.associate { (k, v) -> v to k }

        @JvmStatic
        private fun Char.toAlphaNum() = letterToOrdinal[this]

        @JvmStatic
        private fun Int.toAlphaChar() = ordinalToLetter[this] ?: ROOT_CHAR

        fun of(vararg words: String): TrieNode {
            tailrec fun loop(words: Array<out String>, trieNode: TrieNode): TrieNode =
                if (words.isEmpty()) trieNode
                else loop(words.sliceArray(1 until words.size), (trieNode + words.first()))

            return if (words.isEmpty()) TrieNode() else loop(words, TrieNode())
        }
    }

    private val children = arrayOfNulls<TrieNode>(letterToOrdinal.size)
    private var endOfWord = false

    operator fun plus(word: String): TrieNode {
        this += word
        return this
    }

    operator fun plusAssign(word: String) {
        if (word == "") return
        val nextLetter = word.drop(1)
        val nextNode = this.add(word.first(), nextLetter == "")
        nextNode += nextLetter
    }

    private fun add(value: Char, endOfWord: Boolean): TrieNode {
        logger.debug { "attempting to add $value and is ${if (endOfWord) "" else "not"} end of word" }
        val index = value.toAlphaNum() ?: throw IllegalArgumentException("Value: $value not supported")
        var nodeForValue = this[value]

        if (nodeForValue == null) {
            nodeForValue = TrieNode()
            this[index] = nodeForValue
        }
        if (endOfWord) this.endOfWord = endOfWord
        return nodeForValue
    }

    operator fun minus(word: String): TrieNode {
        val endOfWordNode = findEndOfWord(word) ?: return this
        endOfWordNode.endOfWord = false
        // TODO: what about case where path to this node is not a word?  Do we need to clean up anything?
        return this
    }

    operator fun minusAssign(word: String) {
        this - word
    }

    private fun findEndOfWord(word: String): TrieNode? {
        logger.debug { "searching for '$word'" }
        if (word == "") return null
        val nextNode = this[word[0]] ?: return null
        val nextWord = word.drop(1)
        logger.debug { "found '${word[0]}' (end of a word=${endOfWord}) next is '$nextWord'" }
        return if (nextWord == "" && this.endOfWord) this else nextNode.findEndOfWord(nextWord)
    }

    operator fun contains(word: String) = findEndOfWord(word) != null

    operator fun set(index: Int, value: TrieNode) {
        children[index] = value
    }

    operator fun get(value: Char) =
        children[value.toAlphaNum() ?: throw IllegalArgumentException("Value: $value not supported")]

    private fun toDotGraph(fromIndex: Int, level: Int): String {
        val valuesAt = children.mapIndexed { index, trieNode -> trieNode?.let { index } }.filterNotNull()
        if (valuesAt.isEmpty()) return "\n"
        logger.debug { "discovered values at: $valuesAt" }
        val output = valuesAt.joinToString(postfix = "\n", separator = "\n")
        {
            """
            |${fromIndex.toAlphaChar()}$level [label="${fromIndex.toAlphaChar()}"];
            |${it.toAlphaChar()}${level + 1} [label="${it.toAlphaChar()}"];
            |${fromIndex.toAlphaChar()}$level -> ${it.toAlphaChar()}${level + 1};
            """.trimMargin()
        }

        val nextValues = valuesAt.associateWith { i -> children[i] }
        return output + nextValues.entries.joinToString(separator = "\n") { (i, n) ->
            n!!.toDotGraph(i, level + 1)
        }
    }

    fun toDotGraph(): String =
        "digraph {\n ${toDotGraph(ROOT_CHAR.toAlphaNum()!!, 0)} }"


    override fun toString(): String {
        val content = children.mapIndexed { i, v -> v?.let { i.toAlphaChar() } ?: '_' }.joinToString()
        return "TrieNode(children=$content)"
    }
}