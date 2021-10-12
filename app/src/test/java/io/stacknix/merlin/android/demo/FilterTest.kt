package io.stacknix.merlin.android.demo

import io.stacknix.merlin.db.MerlinObject
import io.stacknix.merlin.db.MerlinQuery
import io.stacknix.merlin.db.queries.Filter
import io.stacknix.merlin.db.queries.SQLBuilder
import org.junit.Before
import org.junit.Test

class FilterTest {

    private val AGE = "age"
    private val NAME = "name"
    private val JAMES = "JAMES"
    private val JERRY = "JERRY"
    private val EMAIL = "EMAIL"
    private val TEST_EMAIL = "test@merlin.com"

    lateinit var filter: Filter

    @Before
    fun setup() {
        filter = MerlinQuery<MerlinObject>(null);
    }

    @Test
    fun `empty expression should - success`() {
        run(true)
    }

    @Test
    fun `start with NOT should - fail`() {
         filter.not()
         run(false)
    }

    @Test
    fun `start with NOT followed by other expression should - fail`() {
        filter.not().equal(NAME, JAMES)
        run(true)
    }

    @Test
    fun `raw - success`() {
        filter.equal(NAME, JAMES).and().not().notEqual(NAME, JAMES)
        run(true)
    }

    @Test
    fun `raw group - success`() {
        filter.equal(NAME, JAMES)
            .or()
            .`in`("id", arrayOf(6L, 8L))
            .notEqual(NAME, JERRY)
            .beginGroup()
            .isNull(AGE)
            .and()
            .notEqual("age", 45)
            .notEqual(EMAIL, TEST_EMAIL)
            .endGroup()
        run(true)
    }

    private fun run(shouldSuccess: Boolean){
        try {
            filter.build()
            val sb = SQLBuilder(filter)
            println(sb.sql)
            assert(shouldSuccess)
        } catch (e: RuntimeException) {
            e.printStackTrace()
            assert(!shouldSuccess)
        }
    }
}