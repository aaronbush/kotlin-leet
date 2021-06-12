package yr21.may.week1

import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.result.shouldNotBeSuccess
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TrieNodeTest {

    @Test
    fun `root node has no words`() {
        val root = TrieNode()
        root.contains("") shouldBe false
        root.contains("someRandomWord") shouldBe false
    }

    @Test
    fun `can add a word and find it`() {
        val root = TrieNode()
        root.addWord("humanity")
        root.contains("humanity") shouldBe true
    }

    @Test
    fun `can add a word and will not find a substring of that word`() {
        val root = TrieNode()
        root.addWord("humanity")
        root.contains("human") shouldBe false
    }

    @Test
    fun `can add multiple words and will find each`() {
        val root = TrieNode()
        root.addWord("class")
        root.addWord("classes")
        root.addWord("classy")
        root.contains("class") shouldBe true
        root.contains("classes") shouldBe true
        root.contains("classy") shouldBe true
    }

    @Test
    fun `deleting a word that does exist succeeds`() {
        val root = TrieNode()
        root.addWord("java")
        root.deleteWord("java") shouldBeSuccess "java"
    }

    @Test
    fun `deleting a word that does exist is not found after`() {
        val root = TrieNode()
        root.addWord("java")
        root.deleteWord("java")
        root.contains("java") shouldBe false
    }

    @Test
    fun `deleting a word that does not exist fails`() {
        val root = TrieNode()
        root.addWord("humanity")
        root.deleteWord("kotlin") shouldNotBeSuccess IllegalArgumentException()
    }
}