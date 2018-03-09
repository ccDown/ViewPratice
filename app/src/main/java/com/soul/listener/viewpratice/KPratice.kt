package com.soul.listener.viewpratice

/**
 * @description
 * @author kuan
 * Created on 2018/1/25.
 */


fun printPdf():()-> Unit {
    var a = 1
    return {
        print("现在的数字是   ${a++}")
    }
}

fun main(args: Array<String>) {

    var printpdf = printPdf()
    printpdf()
    printpdf()

    val sum: Int.(other: Int) -> Int = { this + it }
    println(1.sum(2))

    args.forEach(::println)

    args.filter(String::isNotEmpty)

    args.forEach (PdfPrint()::println)

    val list = listOf(1,3,4,5,10,8,2)

    list.map {
        it*2+3
    }

    val newList = list.map { Int::toDouble }
    newList.forEach (::println)

    val list2 = listOf(
            1..20,
            2..5,
            100..322
    )

    val flatMap = list2.flatMap { it.map { "No $it" } }

    val numList = list2.flatMap { it }
    numList.reduce { acc, i -> acc + i }

    numList.fold(StringBuilder()){acc, i -> acc.append(i).append(",") }

    println((0..6).joinToString { "," })

    //过滤掉单数
    println((0..6).filter { it % 2 == 1 })

    //过滤奇数位置的数据
    println((0..6).filterIndexed{index, i -> index % 2 == 1 })

}

val fun1 = { string: String, invoke: String ->
    println("开始  ${string}   ${invoke}")
}


class Hello {
    fun world() {
        println("Hello Worle")
    }
}

class PdfPrint{
    fun println(any:Any){
        kotlin.io.println(any)
    }
}
