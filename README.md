
# Noustea
Tento repozitář slouží jako úložiště kódu pro aplikaci a backend k modelu, který má demonstrovat projekt **NOUSTEA**. 

[Složka s aplikací](/app/src/main)
[Soubor s backendem](BACKEND.py)

## Aplikace
Aplikace je napsána v programovacím jazyce [Kotlin](https://kotlinlang.org/). Ten byl vyvinut českou společností [JetBrains](https://www.jetbrains.com/) jako modernější verze programovacího jazyka Java, a v roce 2019 bylo na konferenci Google I/O [oznámeno, že se stává oficiálním vývojovým jazykem pro Android](https://android-developers.googleblog.com/2017/05/google-io-2017-empowering-developers-to.html). Aplikace tedy podporuje pouze operační systém Android a byla naprogramována v programu [Android Studio](https://developer.android.com/studio). Není přizpůsobena na jiná zařízení než se kterými jsme pracovali (Raspberry Pi Pico W a Samsung Galaxy Tab 4). Nachází se v ní pohyblivý model celé lodi, který je rozdělený na jednotlivé animace, takže se dá u jednotlivých animací změnit rychlost, nebo je úplně zastavit. Dále se zde dá najít například ovládání motorů na modelu, které dokáží pohybovat s solárními panely, radiátory nebo gravitačním habitatem (ring). Má také samostatnou záložku na moduly, kde je vidět ve kterých místech jsou moduly připojeny. U každého se také dá ovládat barva LED žárovek a k odlišení i napsat název, popisek a hmotnost modulu. 

![Ukázka menu](https://i.imgur.com/hKCBdIJ.png)

*Menu aplikace*

![Ukázka obrazovky pro moduly](https://i.imgur.com/JAkXEam.png)
*Obrazovka pro postranní moduly, připojené moduly se dají odlišit od odpojených*

![Ukázka obrazovky pro dialogy modulů](https://i.imgur.com/7EfJSMF.png)*Dialog pro každý modul kde se dá přímo v aplikaci změnit název, popis a hmotnost. Také se dá vybírat z osmi různých barev (RGB + CMY + bílá + vypnuto)*

![Ukázka obrazovky pro ovládání solárních panelů](https://i.imgur.com/aOGMTPN.png)
*Dialog pro nastavování rychlosti pro servo motory, které otáčí na modelu solárními panely*

## Backend
Jako ovládací jednotku pro náš model byl vybrán čip [Raspberry Pi Pico W](https://rpishop.cz/jako-picorp2040/5073-raspberry-pi-pico-w-5056561803173.html) pro svoji integrovanou funkci připojení se k Wi-Fi sítím. Programuje se v programovacím jazyce [MicroPython](https://micropython.org/), který je velmi podobný znám jazyku Python. Pro nahrávání kódu na čip byl použit program [Thonny](https://thonny.org/), který zajišťuje sériovou komunikaci mezi počítačem a čipem. 

Na tabletu se nejdříve spustí hotspot s předem nastaveným jménem a heslem. Pico se na něj zkusí připojit a jakmile získá svou IP adresu, začne na ní hostovat svoji webovou stránku. Pokyny se pak posílají jako adresa na této stránce (například `/set_servo=`). Aplikace si nejdříve najde v listu připojených klientů pomocí známé mac adresy čipu ip adresu stránky, a pak na ní posílá dotazy. 	

Problém toho, jaký modul je připojen do jakého portu, byl vyřešen velice elegantně. V každém modulu se nachází obvod s různými odpory. Čip má v sobě porty pro vstup, ale jenom tři, takže jsme byli nuceni koupit [konvertor](https://www.adafruit.com/product/1083), který s ním komunikuje přes [I²C protokol](https://cs.wikipedia.org/wiki/I%C2%B2C). Motory se ovládají přes technologii jménem [PWM (Pulzně šířková modulace)](https://cs.wikipedia.org/wiki/Pulzn%C4%9B_%C5%A1%C3%AD%C5%99kov%C3%A1_modulace), kterou všechny výstupní porty podporují. Ta neposílá proud do portu konstatně, ale pouze určitý zlomek času (tzv. duty time). DC motor na pohyb gravitačního habitatu funguje od cca 50%-100% duty time. 360° servo motory na otáčení radiátorů a solárních panelů fungují trochu jinak než běžně používané 180°. Zatímco do 180° se posílá duty time 0%-7% pro nastavení resp. úhlů 0° - 180°, 360° serva při cca 3.5% stojí, a když jdeme od tohoto středu dále na obě strany, tak postupně zrychlují zrychlují po směru hodinových ručiček, nebo proti němu.
Nastavování LED žárovek v modulech a portech pro ně probíhá tak, že z aplikace se pošle číslo 0-7. To slouží jako pozice prvku v poli, které obsahuje bitové instrukce pro žárovky (například 101 - červená a modrá se rozsvítí, zelená zhasne). Tyto instrukce se nastaví do tří různých pinů, které potom ovlivňují barvu RGB LED žárovky. Normálně se dají opět posílat pomocí PWM hodnoty 0-255, ale protože v čipu je k některým PWM obvodům připojeno více pinů, museli jsme se uchýlit k řešení posílat tam plných 3.3V, nebo 0V.
