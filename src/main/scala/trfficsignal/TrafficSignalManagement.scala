package trfficsignal

import scalaz.State
import scalaz.syntax.applicative._
trait TrafficSignalManagement {

//  def direction(incomingSignal:Direction,outgoingSignal:Direction):Route
//
//  def currentState:State[CurrentStatus,Unit]

  def signal(incomingSignal:Direction,outgoingSignal:Direction)(currentStatus: CurrentStatus):Signal = ???

}
