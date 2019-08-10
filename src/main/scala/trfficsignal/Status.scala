package trfficsignal

trait Status {
  def next: Status = this match {
    case `1` => `2`
    case `2` => `3`
    case `3` => `4`
    case `4` => `5`
    case `5` => `6`
    case `6` => `1`

  }
}

case object `1` extends Status

case object `2` extends Status

case object `3` extends Status

case object `4` extends Status

case object `5` extends Status

case object `6` extends Status


