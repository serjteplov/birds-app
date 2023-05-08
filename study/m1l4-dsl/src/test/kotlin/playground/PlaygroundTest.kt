package playground

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith

// Реализуйте dsl для составления sql запроса, чтобы все тесты стали зелеными.
class SqlDslUnitTest {
    private fun checkSQL(expected: String, sql: SqlSelectBuilder) {
        assertEquals(expected, sql.build())
    }

    @Test
    fun `simple select all from table`() {
        val expected = "select * from table"

        val real = query {
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `select certain columns from table 1`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    /**
     * __eq__ is "equals" function. Must be one of char:
     *  - for strings - "="
     *  - for numbers - "="
     *  - for null - "is"
     */
       @Test
       fun `select with complex where condition with one condition`() {
           val expected = "select * from table where col_a = 'id'"
           val real = query {
               from("table")
               where { 
                   "col_a" eq "id" 
               }
           }
           checkSQL(expected, real)
       }


    /**
     * __nonEq__ is "non equals" function. Must be one of chars:
     *  - for strings - "!="
     *  - for numbers - "!="
     *  - for null - "!is"
     */
    @Test
    fun `select with complex where condition with two conditions`() {
        val expected = "select * from table where col_a !is null"
        val real = query {
            from("table")
            where { "col_a" nonEq null }
        }
        checkSQL(expected, real)
    }


    @Test
    fun `select with complex where condition with number`() {
        val expected = "select * from table where (col_b = 5 and col_a = 3)"
        val real = query {
            from("table")
            where {
                "col_b" eq 5
                "col_a" eq 3
            }
        }
        checkSQL(expected, real)
    }


    @Test
    fun `when 'or' conditions are specified then they are respected`() {
        val expected = "select * from table where (col_a = 4 or col_b = 6)"
        val real = query {
            from("table")
            where {
                or {
                    "col_a" eq 4
                    "col_b" eq 6
                }
            }
        }
        checkSQL(expected, real)
    }

    @Test
    fun `test or and`() {
        val expected = "select * from table where (col_c = 'abc' and (col_a = 4 or col_b = 6))"

        val real = query {
            from("table")
            where {
                "col_c" eq "abc"
                or {
                    "col_a" eq 4
                    "col_b" eq 6
                }
            }
        }
        checkSQL(expected, real)
    }

    @Test
    fun `test or and2`() {
        val expected = "select * from table where (col_a = 4 or (col_f = 'fuu' or col_g = 'baar'))"
        val real = query {
            from("table")
            where {
                or {
                    "col_a" eq 4
                    or {
                        "col_f" eq "fuu"
                        "col_g" eq "baar"
                    }
                }
            }
        }
        checkSQL(expected, real)
    }

    @Test
    fun `test or and3`() {
        val expected = "select * from table where (col_a = 4 and col_b = 6 and col_d = 'abc' and col_e is null)"
        val real = query {
            from("table")
            where {
                "col_a" eq 4
                "col_b" eq 6
                "col_d" eq "abc"
                "col_e" eq null
            }
        }
        checkSQL(expected, real)
    }
}