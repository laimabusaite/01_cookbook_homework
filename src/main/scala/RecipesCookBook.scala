import scala.collection.mutable.ArrayBuffer

object RecipesCookBook extends App{

  val recipesFilename = "src/resources/cookbook.txt"

  val recipeLines = Utilities.getLinesFromFile(recipesFilename)

//  recipeLines.slice(0,20).foreach(println)

  val capsTitlesRegex = """\b[A-Z\s]+\b.*"""

  val startTitlesLineIndex = Utilities.findNeedle(recipeLines, "PLAIN CHOCOLATE")
  val endTitlesLineIndex = Utilities.findNeedle(recipeLines, "INDEX TO RECIPES")

  val capsTitlesWithIndex = recipeLines.zipWithIndex.filter(pair => pair._1.matches(capsTitlesRegex))
  capsTitlesWithIndex.foreach(println)

  val ingredientsRegex = """\s+.+\b[,.]"""

  def isRecipeTitle(lines: Array[String], titleWithIndex: (String, Int), startLineIndex:Int = 0, endLineIndex:Int=100000) : Boolean = {
//    println(titleWithIndex, lines(titleWithIndex._2))

    val conditionSpaceBefore = {
      lines(titleWithIndex._2 - 1).matches("") &&
        ( lines(titleWithIndex._2 - 2).matches("")
//          || lines(titleWithIndex._2 - 2).matches("\\.*[*]")
          )  &&
        lines(titleWithIndex._2 - 3) != ""
    }
    val conditionSpaceAfter = {
      lines(titleWithIndex._2 + 1).matches("") &&
        lines(titleWithIndex._2 + 2) != ""

    }
    val conditionLineNumbers = {
      titleWithIndex._2 >= startLineIndex &&
        titleWithIndex._2 <= endLineIndex
    }

    conditionSpaceBefore && conditionSpaceAfter && conditionLineNumbers
  }

//  println(isRecipeTitle(recipeLines, capsTitlesWithIndex(8)))

  val filterForRecipeTitles = for (title <- capsTitlesWithIndex) yield isRecipeTitle(recipeLines, title)//, startTitlesLineIndex, endTitlesLineIndex)

//  filterRecipeTitles.foreach(println)

//  println(filterRecipeTitles(2))
  println(capsTitlesWithIndex.length, filterForRecipeTitles.length)
  val recipeTitlesWithIndex = capsTitlesWithIndex.zip(filterForRecipeTitles).collect { case (title, true) => title }


  recipeTitlesWithIndex.foreach(println)
  println(recipeTitlesWithIndex.length)

  def extractIngredients(lines:Array[String], titlesWithIndex:Array[(String,Int)], endIndex:Int): Map[String, Array[String]] = {
    val recipeTitleMap = titlesWithIndex.map( title => (title._1, ArrayBuffer[String]())).toMap
    for (idx <- 0 to titlesWithIndex.length-1) {
      val currentTitleIndex = titlesWithIndex(idx)._2
      val currentTitle = titlesWithIndex(idx)._1
      val nextTitleIndex = if (idx<titlesWithIndex.length-1) titlesWithIndex(idx+1)._2 else endIndex
      println(idx, currentTitleIndex, currentTitle, nextTitleIndex)
      for (line <- lines.slice(currentTitleIndex,nextTitleIndex)){
        if (line.matches(ingredientsRegex)) {
          recipeTitleMap(currentTitle) += line
          println(line)
        }
      }
    }
    val results = for ((recipeTitle, recipeIngredients) <- recipeTitleMap) yield (recipeTitle, recipeIngredients.toArray)
    results.filter(v => v._2.length>0)
  }

  val recipeIngredients = extractIngredients(recipeLines, recipeTitlesWithIndex, endTitlesLineIndex)

  def saveRecipes(recipeMap: Map[String, Array[String]], destPath:String): Unit ={
    import java.io.{PrintWriter, File} //explicit import
    val pw = new PrintWriter(new File(destPath))
    for ((title, ingredients) <- recipeMap){
      pw.write(title)
      pw.write("\n\n")
      val txt = ingredients.mkString("\n")
      pw.write(txt)
      pw.write("\n\n")
    }
    pw.close()
  }


  saveRecipes(recipeIngredients, "src/resources/recipe_ingredients.txt")

//
//  var i = 1
//  for ((title, ingredients) <- recipeIngredients){
//    println(i, title)
//    println(ingredients.length)
//    ingredients.foreach(println)
//    i += 1
//  }
//
//
//  val testCases = Array("CHOCOLATE MOLASSES KISSES", "WALNUT CREAM-CHOCOLATES", "WELLESLEY MARSHMALLOW FUDGE")
//  println()
//  println("Test")
//  for (testCase <- testCases) {
//    println(testCase)
//    if (recipeIngredients.contains(testCase)) recipeIngredients(testCase).foreach(println) else "not found"
//  }




}
