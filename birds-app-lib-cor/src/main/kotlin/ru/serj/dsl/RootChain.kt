package ru.serj.dsl

fun <T> rootChain(block: ChainDsl<T>.() -> Unit): ChainDsl<T> {
    val rootChainDsl = ChainDsl<T>()
    block(rootChainDsl)
    return rootChainDsl
}
