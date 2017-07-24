import java.io.File
val footerRegex: Regex = Regex(
  """.*<tfoot>(.*)</tfoot>.*"""
  )
val resultRegex: Regex = Regex(
  """([\w, %]+)</td>"""
  )

fun main(args: Array<String>){
  val path = if (args.size > 0) args[0] else "c:/temp/docs"

  val newIndexFilePath = path+"/index.html"


  //get list of Files that contain the jacoco index files
  val jacocoPackages = mutableListOf<File>()
  File(path).listFiles().forEach{
    if (it.isDirectory){
      if (it.resolve(File("target/site/jacoco/index.html")).exists()){
        jacocoPackages.add(it)
      }
    }
  }

  println("Found jacoco index files in:")
  var fileLinks = ""
  jacocoPackages.forEach{
    println("${it.name}")
    val relativePath = "${it.name}/target/site/jacoco/index.html"

    val f = it.resolve(File("target/site/jacoco/index.html"))
    val html = f.readText()

    println("Matches for ${f}")
    val footerResults = getFooterResults(html);
    print("Results: ")
    footerResults.forEach{
      print("'$it' - ")
    }

    /*matches?.groups?.forEach{
      print("Matches: ${it}")
    }*/
    /*<tr><td>Missed Instructions</td><td>Cov.</td><td>Missed Branches</td><td>Cov.</td><td>Missed</td><td>Cxty</td><td>Missed</td><td>Lines</td><td>Missed</td><td>Methods</td><td>Missed</td><td>Classes</td></tr>
    <tr><td>${footerResults.get(1)}</td><td>${footerResults.get(2)}</td><td>${footerResults.get(3)}</td><td>${footerResults.get(4)}</td><td>${footerResults.get(5)}</td><td>${footerResults.get(6)}</td><td>${footerResults.get(7)}</td><td>${footerResults.get(8)}</td><td>${footerResults.get(9)}</td><td>${footerResults.get(10)}</td><td>${footerResults.get(11)}</td><td>${footerResults.get(12)}</td></tr>*/

    println()
    fileLinks += """<a href="$relativePath">${it.name}</a><br/>
    <table>
    <tr><td>Missed Instructions</td><td>Cov.</td><td>Missed Branches</td><td>Cov.</td></tr>
    <tr><td>${footerResults.get(1)}</td><td>${footerResults.get(2)}</td><td>${footerResults.get(3)}</td><td>${footerResults.get(4)}</td></tr>
    </table>
    """
  }


  generateHtmlFile(newIndexFilePath, fileLinks)
}

fun generateHtmlFile(indexPath: String, fileLinks: String){
  val htmlText = """
<html>
  ${fileLinks}
</html>
"""

  println(htmlText)
  File(indexPath).writeText(htmlText)
  println("Wrote html index file to $indexPath")

}

fun getFooterResults(html: String): List<String> {
  var results = ArrayList<String>()
  val matches = footerRegex.matchEntire(html)

  if (matches != null &&
     matches.groups.size == 2 &&
     matches.groups.get(1)?.value != null){
       //print("Match 1: ${matches.groups.get(1)}")
    val footer =  matches.groups.get(1)?.value;
    //println("Footer: $footer")
    if (footer != null) {
      val fields = resultRegex.findAll(footer)
      //add each parsed field to the list
      fields.forEach{
        it.groups.get(1)?.value?.let{
          results.add(it)
        }
      }
      //println("$results")
    }

  }
  return results;

}
