/*
Copyright 2017 Erik Erlandson

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package coulomb.infra

import shapeless._
import shapeless.syntax.singleton._
import singleton.ops._

private [coulomb] trait XIntValue[I] {
  def value: Int
}
private [coulomb] object XIntValue {
  implicit def evidence[I](implicit i: singleton.ops.Id[I]): XIntValue[I] =
    new XIntValue[I] {
      val value = i.value.asInstanceOf[Int]
    }
}

private [coulomb] trait XIntAdd[L, R] {
  type Out
}
private [coulomb] object XIntAdd {
  type Aux[L, R, O] = XIntAdd[L, R] { type Out = O }
  implicit def witness[L, R](implicit op: +[L, R]): Aux[L, R, op.Out] = new XIntAdd[L, R] { type Out = op.Out }
}

private [coulomb] trait XIntSub[L, R] {
  type Out
}
private [coulomb] object XIntSub {
  type Aux[L, R, O] = XIntSub[L, R] { type Out = O }
  implicit def witness[L, R](implicit op: -[L, R]): Aux[L, R, op.Out] = new XIntSub[L, R] { type Out = op.Out }
}

private [coulomb] trait XIntMul[L, R] {
  type Out
}
private [coulomb] object XIntMul {
  type Aux[L, R, O] = XIntMul[L, R] { type Out = O }
  implicit def witness[L, R](implicit op: *[L, R]): Aux[L, R, op.Out] = new XIntMul[L, R] { type Out = op.Out }
}

private [coulomb] trait XIntNeg[N] {
  type Out
}
private [coulomb] object XIntNeg {
  type Aux[N, O] = XIntNeg[N] { type Out = O }
  implicit def witness[N](implicit op: Negate[N]): Aux[N, op.Out] = new XIntNeg[N] { type Out = op.Out }
}

private [coulomb] trait XIntLT[L, R] {
  type Out
}
private [coulomb] object XIntLT {
  type Aux[L, R, O] = XIntLT[L, R] { type Out = O }
  implicit def witness[L, R](implicit op: <[L, R]): Aux[L, R, op.Out] = new XIntLT[L, R] { type Out = op.Out }
}

private [coulomb] trait XIntGT[L, R] {
  type Out
}
private [coulomb] object XIntGT {
  type Aux[L, R, O] = XIntGT[L, R] { type Out = O }
  implicit def witness[L, R](implicit op: >[L, R]): Aux[L, R, op.Out] = new XIntGT[L, R] { type Out = op.Out }
}

private [coulomb] trait XIntEQ[L, R] {
  type Out
}
private [coulomb] object XIntEQ {
  type Aux[L, R, O] = XIntEQ[L, R] { type Out = O }
  implicit def witness[L, R](implicit op: ==[L, R]): Aux[L, R, op.Out] = new XIntEQ[L, R] { type Out = op.Out }
}

private [coulomb] trait XIntNE[L, R] {
  type Out
}
private [coulomb] object XIntNE {
  type Aux[L, R, O] = XIntNE[L, R] { type Out = O }
  implicit def witness[L, R](implicit op: !=[L, R]): Aux[L, R, op.Out] = new XIntNE[L, R] { type Out = op.Out }
}

private [coulomb] trait XIntNon01[N] {
  type Out
}
private [coulomb] object XIntNon01 {
  type Aux[N, O] = XIntNon01[N] { type Out = O }
  implicit def evidence[N, R0, R1](implicit ne0: XIntNE.Aux[N, 0, R0], ne1: XIntNE.Aux[N, 1, R1], a01: &&[R0, R1]): Aux[N, a01.Out] =
    new XIntNon01[N] { type Out = a01.Out }
}
