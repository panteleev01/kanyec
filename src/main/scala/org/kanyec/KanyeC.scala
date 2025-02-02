package org.kanyec

import java.io.FileOutputStream
import org.kanyec.ast.RootNode

object KanyeC {
  def main(args: Array[String]): Unit =  {
    if (args.length < 1) {
      println("Usage: KanyeC [-run|-declaim] [FileToSourceCode]")
      return
    }
    val filename = getFilNameFromArgs(args)
    val sourceCode = scala.io.Source.fromFile(filename).mkString
    val a = new KanyeGenerator()
    val classFilename = 
      if (filename.contains('.')) 
        filename.replaceAll("\\.[^.]*$", "")
      else 
        filename
    
    val (bytecode, root) = a.generate(sourceCode, classFilename)

    val out = new FileOutputStream(s"$classFilename.class")
    out.write(bytecode)
    out.close()

    processOption(getCommandFromArgs(args), classFilename, root)
  }
  
  def getFilNameFromArgs(args: Array[String]):String = args.length match {
    case 1 => args(0)
    case 2 => args(1)
    case _ => throw new RuntimeException("WHAT THE FUCK DID I DO WRONG!")
  }

  def getCommandFromArgs(args:Array[String]):String = args.length match {
    case 2 => args(0)
    case 1 => ""
    case _ => throw new RuntimeException("WHAT THE FUCK DID I DO WRONG!")
  }

  def processOption(command:String, argFunc: => String, root: RootNode): Unit = command match {
    case "-run" => Executor.execute(argFunc)
    case "-declaim" => Declaimer.declaim(root, argFunc)
    case _ => ???
  }

}
