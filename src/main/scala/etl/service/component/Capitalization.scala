package etl.service.component

import etl.model.{Content, Pairs, Sentence}

object Capitalization {
  def apply(content: Content) = content match {
    case Sentence(m,t) => Some(Sentence(m,t.toUpperCase))
    case p : Pairs => None
  }
}
