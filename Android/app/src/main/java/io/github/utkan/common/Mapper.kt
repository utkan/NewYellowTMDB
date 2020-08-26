package io.github.utkan.common

interface Mapper<IN, OUT> {
    fun map(input: IN): OUT
}
