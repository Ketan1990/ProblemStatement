package etl2.service

case class Component[I,O](operation: I => Either[String,O] )


object Component {

  def compose[OuterInput, InnerInput, Output](
                                               outer: Component[OuterInput, InnerInput],
                                               inner: Component[InnerInput, Output]
                                             ): Component[OuterInput, Output] =
    Component[OuterInput, Output](operation = input => outer.operation(input).flatMap(inner.operation))


  implicit class Chain[OuterInput, InnerInput, Output](outer: Component[OuterInput, InnerInput]){
    def -> (inner: Component[InnerInput, Output]): Component[OuterInput, Output] = Component.compose(outer,inner)
  }


}
