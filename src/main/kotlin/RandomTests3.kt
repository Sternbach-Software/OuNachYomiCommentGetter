import java.io.File

fun main() {
    val b = File("C:\\Users\\shmue\\Downloads\\OuNachYomiCommentGetter\\new.txt").readLines()
    for(a in b.indices) {
        if (a != 0) {
            val toInt = b[a].toInt()
            val toInt1 = b[a - 1].toInt()
            val i = toInt - toInt1
            if (i != 1) println("b[a-1]=${b[a - 1]},b[a]=${b[a]}, i=$i")
        }
    }
//    val x = File("new.txt")
//    x.createNewFile()
//    val u = x.bufferedWriter()
//    for(a in n) u.appendLine(a.first)
//    u.close()
//    println(n.take(30))
//    println(n.maxByOrNull { it.first.toInt() })
//    println(n.minByOrNull { it.first.toInt() })
}