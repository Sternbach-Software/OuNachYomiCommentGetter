import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.URL

data class NachShiur(val shiurTitle: String, val descriptionTitle: String, val description: String)

val bufferedWriter = File("Descriptions.txt").apply { createNewFile() }.bufferedWriter()
fun main() {
    val listOfIDs = File("C:\\Users\\shmue\\OneDrive\\IdeaProjects\\OuNachYomiCommentGetterGithub\\new.txt").readLines()
    val listOfBooks = listOf(
        "Joshua",
        "Judges",
        "I Samuel",
        "II Samuel",
        "I Kings",
        "II Kings",
        "Isaiah",
        "Jeremiah",
        "Ezekiel",
        "Hosea",
        "Joel",
        "Amos",
        "Obadiah",
        "Jonah",
        "Micah",
        "Nahum",
        "Habakkuk",
        "Zephaniah",
        "Haggai",
        "Zechariah",
        "Malachi",
        "Psalms",
        "Proverbs",
        "Job",
        "Song of Songs",
        "Ruth",
        "Lamentations",
        "Ecclesiastes",
        "Esther",
        "Daniel",
        "Ezra",
        "Nehemiah",
        "I Chronicles",
        "II Chronicles"
    )
    val listOfShiurim = mutableListOf<NachShiur>()
    val numberOfShiurim = listOfIDs.size
    listOfShiurim.fillListWithDescriptions(listOfIDs, numberOfShiurim)
    listOfShiurim.sortWith(
        compareBy<NachShiur> {
            /*Judges - Chapter 14 - Nach Yomi - OU Torah*/
            /*I Samuel - Chapter 4 - Nach Yomi - OU Torah*/
            val shiurTitle = it.shiurTitle
            val seferName = shiurTitle.substring(0, shiurTitle.indexOf(" -"))
            listOfBooks.indexOf(seferName)
        }.thenBy {
            "\\d+".toRegex().find(it.shiurTitle)?.value?.toInt() ?: 0
        }
    )
    for (shiur in listOfShiurim) writeShiurToFile(shiur)
    bufferedWriter.close()
}

fun MutableList<NachShiur>.fillListWithDescriptions(
    listOfIDs: List<String>,
    numberOfShiurim: Int
) {
    var counter = 0

    val scope = newFixedThreadPoolContext(20, "MyOwnThread")
//3000-5000
    val a = listOfIDs.map { id ->
        GlobalScope.async(scope) {
            counter++
            println("Number of shiurim processed: $counter/$numberOfShiurim")
            val pageRawHTML = URL("https://www.outorah.org/p/$id").readText()
            //File("C:\\Users\\shmue\\Downloads\\OuNachYomiCommentGetter\\src\\main\\kotlin\\3669.html").readText()
            val shiurTitle = pageRawHTML.substringBetween("<title>", "</title>")
            println("Shiur title: \"$shiurTitle\"")

            if (shiurTitle.contains("intro", ignoreCase = true)) {
                val string1 = "data-v-431a452e>"
                val indexOfTitle = pageRawHTML.ordinalIndexOf(string1, 35, false) + string1.length
                val startIndexOfDescription = pageRawHTML.ordinalIndexOf(string1, 36, false) + string1.length
                val endIndexOfDescription = pageRawHTML.ordinalIndexOf(string1, 37, false) + string1.length
                val descriptionTitle = pageRawHTML.substring(indexOfTitle, pageRawHTML.indexOf("</h4>", indexOfTitle))
                var description = pageRawHTML.substring(startIndexOfDescription, endIndexOfDescription)
                description = description.replace("(<p>)|(</p>)|(<div>)|(</div>)".toRegex(), "")
                println("Description is: \"$description\"")
                add(NachShiur(shiurTitle, descriptionTitle, description))
            } else if (shiurTitle.contains("\\d".toRegex())) {
                val mainString = "data-v-89f05832>"
                val string1 = "<div $mainString"
                val string2 = mainString
                val startIndex = pageRawHTML.indexOf(string1)
                val endIndex = pageRawHTML.lastIndexOf(string1)
                val indexOfTitle = pageRawHTML.ordinalIndexOf(string2, 7, false) + string2.length
                val title = pageRawHTML.substring(indexOfTitle, pageRawHTML.indexOf("</h4>", indexOfTitle))
                println("Title is: \"$title\"")
                println("Start index: $startIndex, End index: $endIndex")
                var description = pageRawHTML.substring(startIndex + string1.length, endIndex)
                description = description.replace("(<p>)|(</p>)|(<div>)|(</div>)".toRegex(), "")
                println("Description is: \"$description\"")
                add(NachShiur(shiurTitle, title, description))
            } else {

            }
        }
    }
    runBlocking {
        a.forEach { it.await() }
    }
    scope.close()
}

fun writeShiurToFile(shiur: NachShiur) {
    bufferedWriter.appendLine("Shiur:          ${shiur.shiurTitle}")
    bufferedWriter.appendLine("Title:          ${shiur.descriptionTitle}")
    bufferedWriter.appendLine("Description:    ${shiur.description}")
}

fun String.substringBetween(string1: String, string2: String): String {
    val index1 = this.indexOf(string1)
    return this.substring(
        index1 + string1.length,
        this.indexOf(string2, index1)
    )
}

fun getStringFromInternet() = "TEST"
fun String.ordinalIndexOf(searchStr: String?, ordinal: Int, startingFromTheEnd: Boolean): Int {
    if (searchStr == null || ordinal <= 0) {
        return -1
    }
    if (searchStr.isEmpty()) {
        return if (startingFromTheEnd) this.length else 0
    }
    var found = 0
    // set the initial index beyond the end of the string
    // this is to allow for the initial index decrement/increment
    var index = if (startingFromTheEnd) this.length else -1
    do {
        index = if (startingFromTheEnd) {
            this.lastIndexOf(searchStr, index - 1) // step backwards thru string
        } else {
            this.indexOf(searchStr, index + 1) // step forwards through string
        }
        if (index < 0) {
            return index
        }
        found++
    } while (found < ordinal)
    return index
}