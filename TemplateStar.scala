/*
The <star/> element of a template, will be replaced by the words that matched * (WildStar) if any.
*/
class TemplateStar extends TemplateElement

object TemplateStar {
  def apply() = {
    new TemplateStar()
  }
}
