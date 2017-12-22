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

package coulomb.define

import scala.reflect.runtime.universe._

import spire.math._

import coulomb.si._

class DerivedTemp[U](coef: Rational, val off: Rational, name: String, abbv: String) extends DerivedUnit[U, Kelvin](coef, name, abbv) {
  override def toString = s"DerivedTemp($coef, $off, $name, $abbv)"
}

object DerivedTemp {
  def apply[U](coef: Rational = Rational(1), off: Rational = Rational(0), name: String = "", abbv: String = "")(implicit
    ut: TypeTag[U]): DerivedTemp[U] = new DerivedTemp[U](coef, off, name, abbv)

  // A slight hack that is used by TempConverter to simplify its rules
  implicit def evidencek2k: DerivedTemp[Kelvin] = DerivedTemp[Kelvin](name = "Kelvin", abbv = "K")
}
