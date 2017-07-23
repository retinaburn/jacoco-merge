import java.io.File

fun main(args: Array<String>){
  val path = if (args.size > 0) args[0] else "c:/temp/docs"

  val pathCount = File(path).toPath().getNameCount()
  val newIndexFilePath = path+"/index.html"

  //print traversal - walk top down
  //printWalkTopDown(path)

  //printWalkBottomUp - walk bottom up
  //printWalkBottomUp(path)

  val jacocoFiles = File(path).walk().filter{
    !it.isDirectory && //we only want files
    it.parentFile.name == "jacoco" && //whose parent dir is jacoco
    it.name == "index.html"
  }

  println("Target path: $path")
  var fileLinks = ""
  jacocoFiles.forEach{
    println("$it = ${it.relativeTo(File(path))}")
    val relativePath = it.relativeTo(File(path))
    val packageName = it.toPath().getName(pathCount)
    fileLinks += """<a href="$relativePath">$packageName</a><br/>
    """
  }

  val htmlText = """
    <html>
      ${fileLinks}
    </html>
    """

  File(newIndexFilePath).writeText(htmlText)

}

fun printWalkTopDown(path: String){
  File(path).walk().forEach{
    println("${labelForDirOrFile(it)} $it")
  }
}

fun printWalkBottomUp(path: String){
  File(path).walkBottomUp().forEach{
    println("${labelForDirOrFile(it)} $it")
  }
}

fun labelForDirOrFile(f: File): String =
  if (f.isDirectory()){
    "D"
  } else {
    "F"
  }
