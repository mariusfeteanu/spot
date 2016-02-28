package com.spotai

import com.spotai.pattern.Pattern
import com.spotai.template.Template

/*
Represents an AIML category.
*/
case class Category(stimulus:Pattern, response:Template, that:Option[Pattern], topic:Option[Pattern])
