import java.net.URL
import kotlinx.coroutines.*
import kotlin.system.measureNanoTime

fun main() {
    val y = measureNanoTime{
        val scope = newFixedThreadPoolContext(20, "MyOwnThread")
        //3000-5000
        val a = (2000..3200).map {
            GlobalScope.async(scope) {
                val b = URL("https://www.outorah.org/p/$it").readText()
                if (b.lastIndexOf("Nach Yomi",b.indexOf("</title>"))==-1) println("Not nach yomi: $it")
                println("Parsed: $it/3000")
            }
        }
        println("Start 1")
        runBlocking {
            a.forEach { it.await() }
        }
        scope.close()
    }
    println("Time: ${y/1_000_000_000}")
}