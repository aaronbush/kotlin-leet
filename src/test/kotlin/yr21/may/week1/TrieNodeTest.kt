package yr21.may.week1

import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.io.File

class TrieNodeTest {

    @Test
    fun `root node has no words`() {
        val root = TrieNode()
        root.contains("") shouldBe false
        ("someRandomWord" in root) shouldBe false
    }

    @Test
    fun `can add a word and find it`() {
        val root = TrieNode()
        root += "humanity"
        root.contains("humanity") shouldBe true
    }

    @Test
    fun `add many using arithmetic operators`() {
        val root = TrieNode()
        root + "humanity" + "java" + "bean"
        root.contains("humanity") shouldBe true
        root.contains("java") shouldBe true
        root.contains("bean") shouldBe true
    }

    @Test
    fun `add many using _of_ helper`() {
        val root = TrieNode.of("humanity", "java", "bean")
        root.contains("humanity") shouldBe true
        root.contains("java") shouldBe true
        root.contains("bean") shouldBe true
    }

    @Test
    fun `remove many using arithmetic operators`() {
        val root = TrieNode()
        root + "humanity" + "java" + "bean"
        root - "humanity" - "java"
        ("humanity" in root) shouldBe false
        ("java" in root) shouldBe false
        ("bean" in root) shouldBe true
    }

    @Test
    fun `remove using minus assign`() {
        val root = TrieNode()
        root + "humanity"
        root -= "humanity"
        ("humanity" in root) shouldBe false
    }

    @Test
    fun `can add a word and will not find a substring of that word`() {
        val root = TrieNode()
        root += "humanity"
        root.contains("human") shouldBe false
        ("human" !in root) shouldBe true
    }

    @Test
    fun `can add multiple words and will find each`() {
        val root = TrieNode()
        root += "class"
        root += "classes"
        root += "classy"

        root.contains("class") shouldBe true
        ("class" in root) shouldBe true
        root.contains("classes") shouldBe true
        root.contains("classy") shouldBe true
    }

    @Test
    fun `deleting a word that does exist is not found after`() {
        val root = TrieNode()
        root += "java"
        root - "java"
        ("java" in root) shouldBe false
    }

    @Test
    fun `deleting a word that does not exist has no side effect`() {
        val root = TrieNode()
        root += "humanity"
        root - "kotlin"
        ("humanity" in root) shouldBe true
        ("java" in root) shouldBe false
    }

    @Test
    fun `deleting same word twice will have no other side effects`() {
        val root = TrieNode()
        root += "java"
        root - "java"
        ("java" in root) shouldBe false
        root - "java"
        ("java" in root) shouldBe false
    }

    @Test
    fun `punctuation is mapped`() {
        val root = TrieNode.of(".", " ", "#")
        ("." in root) shouldBe true
        (" " in root) shouldBe true
        ("#" in root) shouldBe true
    }

    @Test
    fun `get operator works`() {
        val word = "apple"
        val chars = word.toCharArray()
        val root = TrieNode.of(word)
        var node: TrieNode? = root
        chars.forEach {
            node = node!![it]
            node shouldNotBe null
        }
    }

    @Test
    fun `removing prefix word doesnt impact longer overlapping word`() {
        val root = TrieNode.of("alligator", "all")
        root - "all"
        ("alligator" in root) shouldBe true
        ("all" in root) shouldBe false
    }

    //    @Test
    fun a() {
        val root = TrieNode()
        root += "alpha"
        root += "at"
        root += "alps"
        root += "bee"
        root += "been"
        root += "bean"
        root += "beast"
        root += "bernie"
        root['a']?.get('l')?.get('p')?.get('s')
        println(root.toDotGraph())
    }

    @Test
    fun `get paths`() {
        val root = TrieNode.of("all", "alpha", "alps", "almost", "alan", "alligator")
        val alNode = root['a']?.get('l')!!
        val suffixes = alNode.pathsBeyond()
        suffixes.shouldContainAll("l", "pha", "ps", "most", "an", "ligator")
    }

    //    @Test
    fun load() {
        val f = File("src/test/resources/words_alpha.txt")
        val root = TrieNode()
        f.forEachLine { root + it }
        var checks = 0
        f.forEachLine { word ->
            (word in root) shouldBe true
            checks++
        }
        println("performed $checks checks")
    }

}