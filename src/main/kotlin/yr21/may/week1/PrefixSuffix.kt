package yr21.may.week1

fun main() {
    var obj = WordFilter(arrayOf("apple", "banana"))
    println(obj)
    val index = obj.f("ba", "a")
    println(index)
}

class WordFilter(private val words: Array<String>) {
    var wordIndex: List<List<String>> = words.map { word ->
        word.reversed()
            .scan("#$word") { acc, c -> c + acc }
    }.map { it.drop(1) }

    fun f(prefix: String, suffix: String): Int {
        val indexes = mutableSetOf<Int>()

        val matches = fun(index: Int, list: List<String>) {
            if (list.any { it.startsWith("$suffix#$prefix") }) {
                indexes.add(index)
            }
        }
        wordIndex.forEachIndexed(matches)
        return indexes.maxOrNull() ?: -1
    }

    override fun toString(): String {
        return """|WordFilter(words=${words.contentToString()},
                  |           wordIndex=$wordIndex)""".trimMargin()
    }

}

/*

 */
/**
 * Your WordFilter object will be instantiated and called as such:
 * var obj = WordFilter(words)
 * var param_1 = obj.f(prefix,suffix)
 */