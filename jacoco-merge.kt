import java.io.File

fun main(args: Array<String>){
  val path = "c:/temp/docs"

  //walk top down
  File(path).walk().forEach{
    println("${labelForDirOrFile(it)} $it")
  }

  //walk bottom up
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
