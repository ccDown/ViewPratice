package com.soul.listener.viewpratice

/**
 * @description
 * @author kuan
 * Created on 2018/1/25.
 */

fun main(args: Array<String>) {
    val sum: Int.(other: Int) -> Int = { this + it }
    println(1.sum(2))
}

val fun1 = {string: String, invoke: String ->
    println("开始  ${string}   ${invoke}")
}
