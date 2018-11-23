package name.nepavel.foodlist

import name.nepavel.foodlist.processor.Processor
import java.io.Console

fun main(args: Array<String>) {
    val console : Console? = System.console()

    val processor = Processor()

    //TODO("Make usage of console")
//    when (console){
//        null -> {
//            println("Running from an IDE...")
//        }
//        else -> {
//            while (true){
//                //Read a line from the keyboard
//                val line = console.readLine("What does Bob say? ")
//                if (line == "q"){
//                    return
//                }
//                console.printf("Bob says: %s\n", line)
//            }
//        }
//    }

    loop@ do {
        val s = readLine()
        when (s) {
            "start" -> processor.start()
            "" -> return
            else -> continue@loop
        }
    } while (true)
}
