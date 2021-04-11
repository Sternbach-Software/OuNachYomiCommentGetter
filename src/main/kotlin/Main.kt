import java.io.File
import java.net.URL

fun main() {
    val mode = 0
    val listOfIDs = File("C:\\Users\\shmue\\Downloads\\OuNachYomiCommentGetter\\new.txt").readLines()
    val listOfBooks = listOf("Joshua","Judges","I Samuel","II Samuel","I Kings","II Kings","Isaiah","Jeremiah","Ezekiel","Hosea","Joel","Amos","Obadiah","Jonah","Micah","Nahum","Habakkuk","Zephaniah","Haggai","Zechariah","Malachi","Psalms","Proverbs","Job","Song of Songs","Ruth","Lamentations","Ecclesiastes","Esther","Daniel","Ezra","Nehemiah","I Chronicles","II Chronicles")
    if(mode==0) {
        val pageRawHTML =
            URL("https://www.outorah.org/p/3669").readText()//File("C:\\Users\\shmue\\Downloads\\OuNachYomiCommentGetter\\src\\main\\kotlin\\3669.html").readText()
        val string1 = "<div data-v-89f05832>"
        val string2 = "data-v-89f05832>"
        val startIndex = pageRawHTML.indexOf(string1)
        val endIndex = pageRawHTML.lastIndexOf(string1)
        val indexOfTitle = pageRawHTML.ordinalIndexOf(string2, 7, false) + string2.length
        val title = pageRawHTML.substring(indexOfTitle, pageRawHTML.indexOf("</h4>", indexOfTitle))
        println("Title is: \"$title\"")
        var description = pageRawHTML.substring(startIndex + string1.length, endIndex)
        description = description.replace("(<p>)|(</p>)|(<div>)|(</div>)".toRegex(), "")
        println("Description is: \"$description\"")
        //data-v-89f05832>Whatever Happened to Doeg?</h4>
    }
    else{

    }
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