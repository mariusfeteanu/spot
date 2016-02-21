/*
Represents an AIML category.
*/
case class Category(stimulus:Pattern, response:Template, that:Option[Pattern], topic:Option[Pattern])
