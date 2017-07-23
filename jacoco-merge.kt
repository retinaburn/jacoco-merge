import java.io.File

fun main(args: Array<String>){
  val path = "c:/temp/docs"

  //print traversal - walk top down
  //printWalkTopDown(path)

  //printWalkBottomUp - walk bottom up
  //printWalkBottomUp(path)

  val jacocoFiles = File(path).walk().filter{
    it.extension == "exec"
  }
  jacocoFiles.forEach{
    println(it)
  }

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
