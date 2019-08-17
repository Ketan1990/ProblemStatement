package trfficsignal

import scalaz.State

case class CurrentStatus(state: Status = `1`)

object CurrentStatus {
  def status = State[CurrentStatus, Status] {
    oldStatus => (CurrentStatus(oldStatus.state.next), oldStatus.state)
  }
}


object Apps extends App {
  val result: State[CurrentStatus, Status] = CurrentStatus.status
  val a = for {
    b1 <- result
    b2 <- CurrentStatus.status
    b3 <- CurrentStatus.status
    b4 <- CurrentStatus.status
    b5 <- CurrentStatus.status
  } yield b5

  print(a.run(CurrentStatus()))
}