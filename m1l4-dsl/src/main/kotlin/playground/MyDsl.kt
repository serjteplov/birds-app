package playground

data class SqlQuery (
    var select: String,
    var column: String,
    var from: String,
    var where: String,
)

data class Condition(
    var left: String,
    var mid: String,
    var right: String
) {
    override fun toString(): String {
        return if (mid == " or ") {
            "($left$mid$right)"
        } else {
            "$left$mid$right"
        }
    }
}

class SqlSelectBuilder {
    var sql: SqlQuery = SqlQuery("select", " *", "", "")
    fun build(): String {
        if (sql.from.isEmpty()) throw RuntimeException()
        return "${sql.select}${sql.column}${sql.from}${sql.where}"
    }
}

class ConditionComposite {
    var conds = mutableListOf<Condition>()
    fun addCondition(cond: Condition) {
        conds.add(cond)
    }
    fun createOrCondition(): Condition {
        return Condition(conds[0].toString(), " or ", conds[1].toString())
    }
    fun build(): String {
        return if (conds.size == 1) {
            conds[0].toString()
        } else {
            conds.joinToString(
                separator = " and ",
                prefix = "(",
                postfix = ")"
            ) { it.toString() }
        }
    }

    infix fun String.eq(any: Any?) {
        val cond = when (any) {
            is String -> Condition(this, " = ", "'$any'")
            is Number -> Condition(this, " = ", "$any")
            else -> Condition(this, " is ", "null")
        }
        addCondition(cond)
    }

    infix fun String.nonEq(any: Any?) {
        val cond =  when (any) {
            is String -> Condition(this, " != ", "'$any'")
            is Number -> Condition(this, " != ", "$any")
            else -> Condition(this, " !is ", "null")
        }
        addCondition(cond)
    }
}

fun ConditionComposite.or(block: ConditionComposite.() -> Unit) {
    val conditionComposite = ConditionComposite()
    block(conditionComposite)
    this.addCondition(conditionComposite.createOrCondition())
}

fun SqlSelectBuilder.where(block: ConditionComposite.() -> Unit) {
    val conditionComposite = ConditionComposite()
    block(conditionComposite)
    this.sql.where = " where ${conditionComposite.build()}"
}









fun query(block: SqlSelectBuilder.() -> Unit): SqlSelectBuilder {
    val builder = SqlSelectBuilder()
    block(builder)
    return builder
}

fun SqlSelectBuilder.from(table: String) {
    this.sql.from = " from $table"
}

fun SqlSelectBuilder.select(vararg column: String) {
    val joined = column.joinToString(separator = ", ")
    this.sql.column = " $joined"
}