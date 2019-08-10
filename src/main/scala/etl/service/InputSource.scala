package etl.service

trait InputSource[Source,Content]{
     def extractContent(source:Source ): List[Content]
}
