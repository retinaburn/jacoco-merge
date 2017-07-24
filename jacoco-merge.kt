import java.io.File

fun main(args: Array<String>){
  val path = if (args.size > 0) args[0] else "c:/temp/docs"

  val newIndexFilePath = path+"/index.html"

  //get list of Files that contain the jacoco index files
  val jacocoPackages = File(path).listFiles().filter{
    it.isDirectory &&
    it.resolve(File("target/site/jacoco/index.html")).exists()
  }

  println("Found jacoco index files in:")
  var fileLinks = ""
  jacocoPackages.forEach{
    println("$it")
    val relativePath = "${it.name}/target/site/jacoco/index.html"
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

  println(htmlText)
  File(newIndexFilePath).writeText(htmlText)
  println("Wrote html index file to $newIndexFilePath")

}
