package etl.service

trait OutPutSource[Content]{
 def load(source:String,content: Content):Unit
}
