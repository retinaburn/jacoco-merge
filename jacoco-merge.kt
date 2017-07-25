import java.io.File
/**
** jacoco-merge
**
** Simple tool to generate an html file with links
** to sub-projects that contain jacoco results with
** a summary of the results for each entry
**
** java -jar jacoco-jar /path/to/project/folders
**
*/

//regex to pull out the table footer from jacoco results
val footerRegex: Regex = Regex(
  """.*<tfoot>(.*)</tfoot>.*"""
  )
//regex to pull each field from the jacoco results footer
val resultRegex: Regex = Regex(
  """([\w, %]+)</td>"""
  )

fun main(args: Array<String>){
  val path = if (args.size > 0) args[0] else "c:/temp/docs"

  val newIndexFilePath = path+"/index.html"

  //get list of packages that contain the jacoco index files
  val jacocoPackages = mutableListOf<File>()
  File(path).listFiles().forEach{
    if (it.isDirectory &&
      it.resolve(File("target/site/jacoco/index.html")).exists()){
        jacocoPackages.add(it)
    }
  }

  /*
  * For each package with jacoco
    - open the index.html file and pull out the results in the table footer
    - generate the entry for the html file using the jacoco results
  */
  println("Found jacoco index files in:")
  var fileLinks = ""
  jacocoPackages.forEach{
    print("${it.name}")

    val html = it.resolve(File("target/site/jacoco/index.html")).readText()
    val footerResults = getJacocoResultsFromFooter(html);
    println("  ${footerResults.get(2)} ${footerResults.get(4)}")
    val relativePath = "${it.name}/target/site/jacoco/index.html"
    fileLinks += generateEntryForHtmlFile(relativePath, it, footerResults)
  }

  generateHtmlFile(newIndexFilePath, fileLinks)?.let{
    println("Wrote html index file to $it")
  }

}

fun generateEntryForHtmlFile(path: String, file: File, footerResults: List<String>): String{
  val entry = """<a href="$path">${file.name}</a><br/>
  <table>
  <tr><td>Missed Instructions</td><td>Cov.</td><td>Missed Branches</td><td>Cov.</td></tr>
  <tr><td>${footerResults.get(1)}</td>
  <td>${footerResults.get(2)}</td>
  <td>${footerResults.get(3)}</td>
  <td>${footerResults.get(4)}</td></tr>
  </table><br/>
  """
  return entry
}

fun generateHtmlFile(indexPath: String, fileLinks: String): String?{
  val htmlText = """
<html>
  ${fileLinks}
</html>
"""

  //println(htmlText)
  File(indexPath).writeText(htmlText)
  return indexPath
}

fun getJacocoResultsFromFooter(html: String): List<String> {
  var results = ArrayList<String>()
  val matches = footerRegex.matchEntire(html)

  if (matches != null &&
     matches.groups.size == 2 &&
     matches.groups.get(1)?.value != null){
       //print("Match 1: ${matches.groups.get(1)}")
    val footer =  matches.groups.get(1)?.value;
    //println("Footer: $footer")
    footer?.let{
      val fields = resultRegex.findAll(footer)
      //add each parsed field to the list
      fields.forEach{
        it.groups.get(1)?.value?.let{
          results.add(it)
        }
      }
    }

  }
  return results;

}
