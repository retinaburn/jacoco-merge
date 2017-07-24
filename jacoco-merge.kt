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

    /*matches?.groups?.forEach{
      print("Matches: ${it}")
    }*/

    println()
    fileLinks += """<a href="$relativePath">${it.name}</a><br/>
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

  //println(htmlText)
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
    println("Footer: $footer")
    if (footer != null) {
      val fields = resultRegex.findAll(footer)
      print("Results: ")
      fields.forEach{
        //print("${it.groups.get(1)?.value} ")
        if (it.groups.get(1)?.value != null){
          val x = it.groups.get(1)
          if (x != null){
            val y = x.value
            results.add(y)
          }
        }
      }
      println("$results")
    }

  }
  return results;

}
