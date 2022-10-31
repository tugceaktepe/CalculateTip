package com.example.calculatetip

import com.example.tipcalculator.calculateTip
import junit.framework.Assert.assertEquals
import org.junit.Test

class TipCalculatorTests {

    //Unit testler JVM uzerinde yani bilgisayarinizda kosar.
    //Test metodlari öncesinde @Test anotasyonu kullanılır. Bu anotasyon Junit kütüphanesinden gelir.
    //Test metotları normal app metodları gibi ekstra logic barındırmaz.
    //Metot isimleri bu test sonucunda ne beklendiğini acıklar sekilde acık ve net olmalıdır.
    //genellikle testler assertion diye tabir ettiğimiz metotlar ile biter.
    //Assertion actual outputu expected deger ile kiyaslamamazi saglar.
    //Pek cok programlama dilinde unit test yazarken assertion kullanilmaktadir.
    //Google'in support ettigi Truth kutuphanesi assertionlar icin kullanılabilir.

    @Test
    fun calculate_20_percent_tip_no_roundup(){
        //Given
        val expectedTip = "$1.00"
        val amount = 10.00
        val tipPercent = 20.00

        //when
        val actualTip = calculateTip(amount = amount, tipPercent = tipPercent, false)

        //then
        assertEquals(expectedTip, actualTip)
    }
}