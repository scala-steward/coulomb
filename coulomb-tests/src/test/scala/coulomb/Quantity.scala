package coulomb

import org.scalatest._
import org.scalactic._
import org.scalatest.matchers.{Matcher, MatchResult}
import TripleEquals._

import spire.math._

import coulomb.si._
import coulomb.siprefix._
import coulomb.mks._
import coulomb.accepted._
import coulomb.time._
import coulomb.info._
import coulomb.binprefix._
import coulomb.us._
import coulomb.temp._

import org.scalatest.QMatchers._

class QuantitySpec extends FlatSpec with Matchers {
  it should "allocate a Quantity" in {
    val q = new Quantity[Double, Meter](1.0)
    q shouldBeQ[Double, Meter](1.0)
  }
/*

  it should "define the Standard International Base Units" in {
    val m = Meter(1D)
    val s = Second(1f)
    val kg = Kilogram(1)
    val a = Ampere(1L)
    val mol = Mole(BigInt(1))
    val c = Candela(BigDecimal(1))
    val k = Kelvin(Rational(1))

    m.qtup should beQ[Double, Meter](1)
    s.qtup should beQ[Float, Second](1)
    kg.qtup should beQXI[Int, Kilogram](1)
    a.qtup should beQ[Long, Ampere](1)
    mol.qtup should beQXI[BigInt, Mole](1)
    c.qtup should beQ[BigDecimal, Candela](1)
    k.qtup should beQ[Rational, Kelvin](1)
  }

  it should "enforce unit convertability at compile time" in {
    "1D.withUnit[Meter].toUnit[Foot]" should compile
    "1D.withUnit[Meter].toUnit[Second]" shouldNot compile

    "1D.withUnit[Acre %* Foot].toUnit[Mega %* Liter]" should compile
    "1D.withUnit[Acre %* Foot].toUnit[Mega %* Hectare]" shouldNot compile

    "1D.withUnit[Mole %/ Liter].toUnit[(Kilo %* Mole) %/ (Meter %^ _3)]" should compile
    "1D.withUnit[Mole %/ Liter].toUnit[(Kilo %* Mole) %/ (Meter %^ _4)]" shouldNot compile
  }

  it should "implement toUnit over supported numeric types" in {
    val meterToFoot = 3.2808399

    // integral types
    100.toByte.withUnit[Meter].toUnit[Foot].qtup should beQ[Byte, Foot](meterToFoot, 100)
    100.toShort.withUnit[Meter].toUnit[Foot].qtup should beQ[Short, Foot](meterToFoot, 100)
    100.withUnit[Meter].toUnit[Foot].qtup should beQ[Int, Foot](meterToFoot, 100)
    100L.withUnit[Meter].toUnit[Foot].qtup should beQ[Long, Foot](meterToFoot, 100)
    BigInt(100).withUnit[Meter].toUnit[Foot].qtup should beQ[BigInt, Foot](meterToFoot, 100)

    // non-integral types
    1f.withUnit[Meter].toUnit[Foot].qtup should beQ[Float, Foot](meterToFoot)
    1D.withUnit[Meter].toUnit[Foot].qtup should beQ[Double, Foot](meterToFoot)
    BigDecimal(1D).withUnit[Meter].toUnit[Foot].qtup should beQ[BigDecimal, Foot](meterToFoot)
    Rational(1).withUnit[Meter].toUnit[Foot].qtup should beQ[Rational, Foot](meterToFoot)
    Algebraic(1).withUnit[Meter].toUnit[Foot].qtup should beQ[Algebraic, Foot](meterToFoot)
    Real(1).withUnit[Meter].toUnit[Foot].qtup should beQ[Real, Foot](meterToFoot)
    Number(1).withUnit[Meter].toUnit[Foot].qtup should beQ[Number, Foot](meterToFoot)
  }

  it should "implement toUnit optimization cases" in {
    // numerator only conversion
    Yard(2).toUnit[Foot].qtup should beQXI[Int, Foot](6)

    // identity
    Meter(2).toUnit[Meter].qtup should beQXI[Int, Meter](2)
    Meter(2.0).toUnit[Meter].qtup should beQ[Double, Meter](2)
  }

  it should "implement toRef over supported numeric types" in {
    37.withUnit[Second].toRep[Byte].qtup should beQXI[Byte, Second](37)
    37.withUnit[Second].toRep[Short].qtup should beQXI[Short, Second](37)
    37.withUnit[Second].toRep[Int].qtup should beQXI[Int, Second](37)
    37.withUnit[Second].toRep[Long].qtup should beQXI[Long, Second](37)
    37.withUnit[Second].toRep[BigInt].qtup should beQXI[BigInt, Second](37)

    37.withUnit[Second].toRep[Float].qtup should beQ[Float, Second](37.0)
    37.withUnit[Second].toRep[Double].qtup should beQ[Double, Second](37.0)
    37.withUnit[Second].toRep[BigDecimal].qtup should beQ[BigDecimal, Second](37.0)
    37.withUnit[Second].toRep[Rational].qtup should beQ[Rational, Second](37.0)
    37.withUnit[Second].toRep[Algebraic].qtup should beQ[Algebraic, Second](37.0)
    37.withUnit[Second].toRep[Real].qtup should beQ[Real, Second](37.0)
    37.withUnit[Second].toRep[Number].qtup should beQ[Number, Second](37.0)
  }

  it should "implement unary -" in {
    (-Kilogram(42.0)).qtup should beQ[Double, Kilogram](-42.0)
    (-(1.withUnit[Kilogram %/ Mole])).qtup should beQXI[Int, Kilogram %/ Mole](-1)
  }

  it should "implement +" in {
    val literToCup = 4.22675283773 // Rational(2000000000 / 473176473)

    // Full rational numerator multiplication overflows smaller integers.
    // todo: investigate heuristics on smaller rationals
    // see: https://github.com/erikerlandson/coulomb/issues/15
    (Cup(100L) + Liter(100L)).qtup should beQ[Long, Cup](1.0 + literToCup, 100)
    (Cup(BigInt(100)) + Liter(BigInt(100))).qtup should beQ[BigInt, Cup](1.0 + literToCup, 100)

    (Cup(1f) + Liter(1f)).qtup should beQ[Float, Cup](1.0 + literToCup)
    (Cup(1D) + Liter(1D)).qtup should beQ[Double, Cup](1.0 + literToCup)
    (Cup(BigDecimal(1)) + Liter(BigDecimal(1))).qtup should beQ[BigDecimal, Cup](1.0 + literToCup)
    (Cup(Rational(1)) + Liter(Rational(1))).qtup should beQ[Rational, Cup](1.0 + literToCup)
    (Cup(Algebraic(1)) + Liter(Algebraic(1))).qtup should beQ[Algebraic, Cup](1.0 + literToCup)
    (Cup(Real(1)) + Liter(Real(1))).qtup should beQ[Real, Cup](1.0 + literToCup)
    (Cup(Number(1)) + Liter(Number(1))).qtup should beQ[Number, Cup](1.0 + literToCup)
  }

  it should "implement + optimization cases" in {
    // numerator only conversion
    (Cup(1) + Quart(1)).qtup should beQXI[Int, Cup](5)

    // identity
    (Cup(1) + Cup(1)).qtup should beQXI[Int, Cup](2)
    (Cup(1.0) + Cup(1.0)).qtup should beQ[Double, Cup](2.0)
  }

  it should "implement -" in {
    val inchToCentimeter = 2.54 // Rational(127 / 50)

    (Centimeter(100.toByte) - Inch(10.toByte)).qtup should beQ[Byte, Centimeter](10.0 - inchToCentimeter, 10)
    (Centimeter(1000.toShort) - Inch(100.toShort)).qtup should beQ[Short, Centimeter](
      10.0 - inchToCentimeter, 100)
    (Centimeter(1000) - Inch(100)).qtup should beQ[Int, Centimeter](10.0 - inchToCentimeter, 100)
    (Centimeter(1000L) - Inch(100L)).qtup should beQ[Long, Centimeter](10.0 - inchToCentimeter, 100)
    (Centimeter(BigInt(1000)) - Inch(BigInt(100))).qtup should beQ[BigInt, Centimeter](
      10.0 - inchToCentimeter, 100)

    (Centimeter(10f) - Inch(1f)).qtup should beQ[Float, Centimeter](10.0 - inchToCentimeter)
    (Centimeter(10D) - Inch(1D)).qtup should beQ[Double, Centimeter](10.0 - inchToCentimeter)
    (Centimeter(BigDecimal(10)) - Inch(BigDecimal(1))).qtup should beQ[BigDecimal, Centimeter](
      10.0 - inchToCentimeter)
    (Centimeter(Rational(10)) - Inch(Rational(1))).qtup should beQ[Rational, Centimeter](
      10.0 - inchToCentimeter)
    (Centimeter(Algebraic(10)) - Inch(Algebraic(1))).qtup should beQ[Algebraic, Centimeter](
      10.0 - inchToCentimeter)
    (Centimeter(Real(10)) - Inch(Real(1))).qtup should beQ[Real, Centimeter](10.0 - inchToCentimeter)
    (Centimeter(Number(10)) - Inch(Number(1))).qtup should beQ[Number, Centimeter](10.0 - inchToCentimeter)
  }

  it should "implement - optimization cases" in {
    // numerator only conversion
    (Inch(13) - Foot(1)).qtup should beQXI[Int, Inch](1)

    // identity
    (Inch(2) - Inch(1)).qtup should beQXI[Int, Inch](1)
    (Inch(2.0) - Inch(1.0)).qtup should beQ[Double, Inch](1)
  }

  it should "implement *" in {
    (Acre(2.toByte) * Foot(3.toByte)).qtup should beQXI[Byte, Acre %* Foot](6)
    (Acre(2.toShort) * Foot(3.toShort)).qtup should beQXI[Short, Acre %* Foot](6)
    (Acre(2) * Foot(3)).qtup should beQXI[Int, Acre %* Foot](6)
    (Acre(2L) * Foot(3L)).qtup should beQXI[Long, Acre %* Foot](6)
    (Acre(BigInt(2)) * Foot(BigInt(3))).qtup should beQXI[BigInt, Acre %* Foot](6)

    (Acre(2f) * Foot(3f)).qtup should beQ[Float, Acre %* Foot](6)
    (Acre(2D) * Foot(3D)).qtup should beQ[Double, Acre %* Foot](6)
    (Acre(BigDecimal(2)) * Foot(BigDecimal(3))).qtup should beQ[BigDecimal, Acre %* Foot](6)
    (Acre(Rational(2)) * Foot(Rational(3))).qtup should beQ[Rational, Acre %* Foot](6)
    (Acre(Algebraic(2)) * Foot(Algebraic(3))).qtup should beQ[Algebraic, Acre %* Foot](6)
    (Acre(Real(2)) * Foot(Real(3))).qtup should beQ[Real, Acre %* Foot](6)
    (Acre(Number(2)) * Foot(Number(3))).qtup should beQ[Number, Acre %* Foot](6)
  }

  it should "implement * miscellaneous" in {
    (2.withUnit[Meter %/ Second] * Second(3)).qtup should beQXI[Int, Meter](6)
    (2D.withUnit[Mole %/ Liter] * 2D.withUnit[Liter %/ Second] * 2D.withUnit[Second])
      .qtup should beQ[Double, Mole](8)
  }

  it should "implement /" in {
    (Meter(10.toByte) / Second(3.toByte)).qtup should beQXI[Byte, Meter %/ Second](3)
    (Meter(10.toShort) / Second(3.toShort)).qtup should beQXI[Short, Meter %/ Second](3)
    (Meter(10) / Second(3)).qtup should beQXI[Int, Meter %/ Second](3)
    (Meter(10L) / Second(3L)).qtup should beQXI[Long, Meter %/ Second](3)
    (Meter(BigInt(10)) / Second(BigInt(3))).qtup should beQXI[BigInt, Meter %/ Second](3)

    (Meter(10f) / Second(3f)).qtup should beQ[Float, Meter %/ Second](3.33333)
    (Meter(10D) / Second(3D)).qtup should beQ[Double, Meter %/ Second](3.33333)
    (Meter(BigDecimal(10)) / Second(BigDecimal(3))).qtup should beQ[BigDecimal, Meter %/ Second](3.33333)
    (Meter(Rational(10)) / Second(Rational(3))).qtup should beQ[Rational, Meter %/ Second](3.33333)
    (Meter(Algebraic(10)) / Second(Algebraic(3))).qtup should beQ[Algebraic, Meter %/ Second](3.33333)
    (Meter(Real(10)) / Second(Real(3))).qtup should beQ[Real, Meter %/ Second](3.33333)
    (Meter(Number(10)) / Second(Number(3))).qtup should beQ[Number, Meter %/ Second](3.33333)
  }

  it should "implement pow" in {
    Meter(3.toByte).pow[_2].qtup should beQXI[Byte, Meter %^ _2](9)
    Meter(3.toShort).pow[_2].qtup should beQXI[Short, Meter %^ _2](9)
    Meter(3).pow[_2].qtup should beQXI[Int, Meter %^ _2](9)
    Meter(3L).pow[_2].qtup should beQXI[Long, Meter %^ _2](9)
    Meter(BigInt(3)).pow[_2].qtup should beQXI[BigInt, Meter %^ _2](9)

    Meter(3f).pow[_2].qtup should beQ[Float, Meter %^ _2](9)
    Meter(3D).pow[_2].qtup should beQ[Double, Meter %^ _2](9)
    Meter(BigDecimal(3)).pow[_2].qtup should beQ[BigDecimal, Meter %^ _2](9)
    Meter(Rational(3)).pow[_2].qtup should beQ[Rational, Meter %^ _2](9)
    Meter(Algebraic(3)).pow[_2].qtup should beQ[Algebraic, Meter %^ _2](9)
    Meter(Real(3)).pow[_2].qtup should beQ[Real, Meter %^ _2](9)
    Meter(Number(3)).pow[_2].qtup should beQ[Number, Meter %^ _2](9)
  }

  it should "implement pow miscellaneous" in {
    5D.withUnit[Meter %/ Second].pow[_0].qtup should beQ[Double, Unitless](1)
    Meter(7).pow[_1].qtup should beQXI[Int, Meter](7)
    Second(Rational(1, 11)).pow[_neg1].qtup should beQ[Rational, Second %^ _neg1](11)
  }

  it should "implement <" in {
    (Meter(1) < Meter(2)) should be (true)
    (Meter(1) < Meter(1)) should be (false)
    (Meter(2) < Meter(1)) should be (false)

    (Meter(1D) < Meter(2D)) should be (true)
    (Meter(1D) < Meter(1D)) should be (false)
    (Meter(2D) < Meter(1D)) should be (false)

    (Yard(1) < Foot(6)) should be (true)
    (Yard(1) < Foot(4)) should be (false)
    (Yard(1) < Foot(3)) should be (false)
    (Yard(1) < Foot(2)) should be (false)

    (Yard(1f) < Foot(4f)) should be (true)
    (Yard(1f) < Foot(3f)) should be (false)
    (Yard(1f) < Foot(2f)) should be (false)
  }

  it should "implement >" in {
    (Meter(1) > Meter(2)) should be (false)
    (Meter(1) > Meter(1)) should be (false)
    (Meter(2) > Meter(1)) should be (true)

    (Meter(1D) > Meter(2D)) should be (false)
    (Meter(1D) > Meter(1D)) should be (false)
    (Meter(2D) > Meter(1D)) should be (true)

    (Yard(1) > Foot(6)) should be (false)
    (Yard(1) > Foot(4)) should be (false)
    (Yard(1) > Foot(3)) should be (false)
    (Yard(1) > Foot(2)) should be (true)

    (Yard(1f) > Foot(4f)) should be (false)
    (Yard(1f) > Foot(3f)) should be (false)
    (Yard(1f) > Foot(2f)) should be (true)
  }

  it should "implement <=" in {
    (Meter(1) <= Meter(2)) should be (true)
    (Meter(1) <= Meter(1)) should be (true)
    (Meter(2) <= Meter(1)) should be (false)

    (Meter(1D) <= Meter(2D)) should be (true)
    (Meter(1D) <= Meter(1D)) should be (true)
    (Meter(2D) <= Meter(1D)) should be (false)

    (Yard(1) <= Foot(6)) should be (true)
    (Yard(1) <= Foot(4)) should be (true)
    (Yard(1) <= Foot(3)) should be (true)
    (Yard(1) <= Foot(2)) should be (false)

    (Yard(1f) <= Foot(4f)) should be (true)
    (Yard(1f) <= Foot(3f)) should be (true)
    (Yard(1f) <= Foot(2f)) should be (false)
  }

  it should "implement >=" in {
    (Meter(1) >= Meter(2)) should be (false)
    (Meter(1) >= Meter(1)) should be (true)
    (Meter(2) >= Meter(1)) should be (true)

    (Meter(1D) >= Meter(2D)) should be (false)
    (Meter(1D) >= Meter(1D)) should be (true)
    (Meter(2D) >= Meter(1D)) should be (true)

    (Yard(1) >= Foot(6)) should be (false)
    (Yard(1) >= Foot(4)) should be (true)
    (Yard(1) >= Foot(3)) should be (true)
    (Yard(1) >= Foot(2)) should be (true)

    (Yard(1f) >= Foot(4f)) should be (false)
    (Yard(1f) >= Foot(3f)) should be (true)
    (Yard(1f) >= Foot(2f)) should be (true)
  }

  it should "implement ===" in {
    (Meter(1) === Meter(2)) should be (false)
    (Meter(1) === Meter(1)) should be (true)
    (Meter(2) === Meter(1)) should be (false)

    (Meter(1D) === Meter(2D)) should be (false)
    (Meter(1D) === Meter(1D)) should be (true)
    (Meter(2D) === Meter(1D)) should be (false)

    (Yard(1) === Foot(6)) should be (false)
    (Yard(1) === Foot(4)) should be (true)
    (Yard(1) === Foot(3)) should be (true)
    (Yard(1) === Foot(2)) should be (false)

    (Yard(1f) === Foot(4f)) should be (false)
    (Yard(1f) === Foot(3f)) should be (true)
    (Yard(1f) === Foot(2f)) should be (false)
  }

  it should "implement =!=" in {
    (Meter(1) =!= Meter(2)) should be (true)
    (Meter(1) =!= Meter(1)) should be (false)
    (Meter(2) =!= Meter(1)) should be (true)

    (Meter(1D) =!= Meter(2D)) should be (true)
    (Meter(1D) =!= Meter(1D)) should be (false)
    (Meter(2D) =!= Meter(1D)) should be (true)

    (Yard(1) =!= Foot(6)) should be (true)
    (Yard(1) =!= Foot(4)) should be (false)
    (Yard(1) =!= Foot(3)) should be (false)
    (Yard(1) =!= Foot(2)) should be (true)

    (Yard(1f) =!= Foot(4f)) should be (true)
    (Yard(1f) =!= Foot(3f)) should be (false)
    (Yard(1f) =!= Foot(2f)) should be (true)
  }

  it should "implement toStr" in {
    Meter(1).toStr should be ("1 m")
    1.withUnit[Kilo %* Meter].toStr should be ("1 km")
    (Meter(1.5) / Second(1.0)).toStr should be ("1.5 m / s")
    Second(1.0).pow[_neg1].toStr should be ("1.0 s^-1")
    1.withUnit[(Acre %* Foot) %/ (Meter %* Second)].toStr should be ("1 (acre ft) / (m s)")
    1.withUnit[Meter %/ (Second %^ _2)].toStr should be ("1 m / (s^2)")
  }

  it should "implement toStrFull" in {
    Meter(1).toStrFull should be ("1 meter")
    1.withUnit[Kilo %* Meter].toStrFull should be ("1 kilo-meter")
    (Meter(1.5) / Second(1.0)).toStrFull should be ("1.5 meter / second")
    Second(1.0).pow[_neg1].toStrFull should be ("1.0 second ^ -1")
    1.withUnit[(Acre %* Foot) %/ (Meter %* Second)].toStrFull should be ("1 (acre * foot) / (meter * second)")
    1.withUnit[Meter %/ (Second %^ _2)].toStrFull should be ("1 meter / (second ^ 2)")
  }

  it should "implement unitStr" in {
    Meter(1).unitStr should be ("m")
    1.withUnit[Kilo %* Meter].unitStr should be ("km")
    (Meter(1.5) / Second(1.0)).unitStr should be ("m / s")
    Second(1.0).pow[_neg1].unitStr should be ("s^-1")
    1.withUnit[(Acre %* Foot) %/ (Meter %* Second)].unitStr should be ("(acre ft) / (m s)")
    1.withUnit[Meter %/ (Second %^ _2)].unitStr should be ("m / (s^2)")
  }

  it should "implement unitStrFull" in {
    Meter(1).unitStrFull should be ("meter")
    1.withUnit[Kilo %* Meter].unitStrFull should be ("kilo-meter")
    (Meter(1.5) / Second(1.0)).unitStrFull should be ("meter / second")
    Second(1.0).pow[_neg1].unitStrFull should be ("second ^ -1")
    1.withUnit[(Acre %* Foot) %/ (Meter %* Second)].unitStrFull should be ("(acre * foot) / (meter * second)")
    1.withUnit[Meter %/ (Second %^ _2)].unitStrFull should be ("meter / (second ^ 2)")
  }

  it should "implement converter companion method" in {
    val f1 = Quantity.converter[Double, Kilo %* Meter, Mile]
    f1(1D.withUnit[Kilo %* Meter]).qtup should beQ[Double, Mile](0.62137)
    f1(Mile(1.0)).qtup should beQ[Double, Mile](1.0)

    val f2 = Quantity.converter[Algebraic, Meter %/ (Second %^ _2), Foot %/ (Second %^ _2)]
    f2(Algebraic(9.801).withUnit[Meter %/ (Second %^ _2)])
      .qtup should beQ[Algebraic, Foot %/ (Second %^ _2)](32.1555)
    f2(Algebraic(1).withUnit[Foot %/ (Second %^ _2)])
      .qtup should beQ[Algebraic, Foot %/ (Second %^ _2)](1.0)

    "Quantity.converter[Algebraic, Meter %/ (Second %^ _2), Mole %/ (Second %^ _2)]" shouldNot compile
    "Quantity.converter[Algebraic, Meter %/ (Second %^ _2), Foot %/ (Second %^ _3)]" shouldNot compile
  }

  it should "implement coefficient companion method" in {
    Quantity.coefficient[Yard, Meter] should be (Rational(9144, 10000))
    Quantity.coefficient[Mile %/ Hour, Meter %/ Second] should be (Rational(1397, 3125))
    Quantity.coefficient[(Kilo %* Meter) %/ (Second %^ _2), Mile %/ (Minute %^ _2)] should be (
      Rational(3125000, 1397))

    "Quantity.coefficient[Mile %/ Hour, Meter %/ Kilogram]" shouldNot compile
    "Quantity.coefficient[(Kilo %* Meter) %/ (Second %^ _2), Mile %/ (Ampere %^ _2)]" shouldNot compile
  }

  it should "implement unitStr companion method" in {
    Quantity.unitStr[Meter] should be ("m")
    Quantity.unitStr[Kilo %* Meter] should be ("km")
    Quantity.unitStr[Meter %/ Second] should be ("m / s")
  }

  it should "implement unitStrFull companion method" in {
    Quantity.unitStrFull[Meter] should be ("meter")
    Quantity.unitStrFull[Kilo %* Meter] should be ("kilo-meter")
    Quantity.unitStrFull[Meter %/ Second] should be ("meter / second")
  }

  it should "implement implicit conversion between convertable units" in {
    (1D.withUnit[Yard] :Quantity[Double, Foot]).qtup should beQ[Double, Foot](3)

    val q: Quantity[Double, Mile %/ Hour] = 1D.withUnit[Kilo %* Meter %/ Second]
    q.qtup should beQ[Double, Mile %/ Hour](2236.936292)

    def f(a: Double WithUnit (Meter %/ (Second %^ _2))) = a
    f(32D.withUnit[Foot %/ (Second %^ _2)]).qtup should beQ[Double, Meter %/ (Second %^ _2)](9.7536)
  }
*/
}
