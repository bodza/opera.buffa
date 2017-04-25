(ns koraujkor.index
    (:require [reagent.core :as reagent]
              [koraujkor.charts :as charts]))

(enable-console-print!)


(defn- toggle [chart]
    (let [showing (reagent/atom false)]
        (fn [] [:div {:style {:margin-bottom "1em" :padding-right "2em"}}
            [:button {:on-click #(do (.preventDefault %) (swap! showing not) nil)} (if @showing "hide" "show")]
            (when @showing [chart])])))


(defn title []
    [:h1 "Koraújkor, Mokka-R, Humanus teszt grafikonok"])

(defn intro [css]
    (let [highcharts {:href "http://highcharts.com/"}
          highcharts-demo {:href "http://highcharts.com/demo"}]

    [:div css

    [:p "A grafikonok alapja minden esetben valódi friss adatsor, amit egyenként manuálisan normalizáltam,
    majd az ábrázoláshoz szükséges adatszerkezetre alakítottam: ez utóbbi az egyes weblapok szövegében
    tetten érhető és értelmezhető. (pl. Firefox esetén ctrl-u megmutatja: a grafikonok nem pixeles
    képek, hanem vektoros ábrák (SVG), így tetszőlegesen skálázhatóak, akár nyomtatáshoz is jó minőségben,
    ill. egyes esetekben interaktívak; több látványos lehetőséget is kipróbáltam, persze nem kell
    túlcsicsázni, csak amennyire épp szükséges.)"]

    [:p "Nagyjából egy tucat grafikon-típus bizonyult hasznosnak, amelyek szorosan kötődnek az aktuális
    adatsor jellegéhez: nem lehet, azaz nem érdemes minden lehetséges statisztikát az összes lehetséges
    formában ábrázolni, a grafikonok automatikus generálásakor szükség lesz némi gépi bölcsességre,
    hogy a megfelelő reprezentációt tálalja az adatok mennyisége, változatossága, eloszlása, stb.
    alapján. Persze több helyes megoldás is születhet."]

    [:p "Augusztus 16-tól 23-ig egy hétre volt szükségem, hogy kitanuljam a " [:a highcharts "highcharts"]
    " nevű grafikon-rajzoló eszköznek a használatát, mind a " [:a highcharts-demo "72 demo"] " példát
    plusz a térképeket (ez utóbbiak egyelőre haszontalannak bizonyultak), közben elkészítettem
    az alábbi 56 vázlatot. Ezt a jószágot korábban nem ismertem, de nagyon nem bántam meg: valóban igen
    kellemes keretrendszer."]

    [:p "Most már szinte bármilyen [kellően józan] lekérdezéshez 1/2-3 óra alatt el tudom készíteni a
    grafikus ábrázolását; ahhoz azonban, hogy mindezt beépítsem az élő adatbázisba, jó eséllyel
    min. 2 hétre lenne szükség, persze utána nagyon jó világ lesz, de előtte javaslom, hogy a
    szeptemberi előadásodhoz használj fel az alábbi készletből, vagy még inkább: fogalmazz meg
    újabb pontos lekérdezéseket és én nagy örömmel legyártom a grafikont, amit így már hamar be
    tudnál építeni a prezentációba én pedig nyugodtabban tudnék haladni az adatbázis kialakításával."]

    [:p "Ha a címeket, címkéket, felhőcskéket (tooltip), egyéb magyarázó szövegeket németül szeretnéd
    olvasni, ahhoz majd segítséget kérek, mert tapló módon csak angolul boldogulok, ill. valamennyi
    német, latin és ómagiar ragadt rám a koraújkorból, de az ehhez már szerintem kevés lesz."]

    [:p "Első adatsorom rém egyszerűnek tűnt: a jegyzékek keletkezési helye és ideje éves bontásban
    persze a kéziratok számossága végett. Itt mindjárt jött két normalizálási feladat: a dátumok
    esetén el kell jutnunk egyetlen évszámhoz: de mi van, ha több a dátum, vagy kérdőjeles, vagy
    intervallum, vagy század második fele, vagy egyáltalán nincs; most a teszt kedvéért igyekeztem
    a legegyszerűbb módon megoldani, így néhány jegyzék ki is maradt a szórásból, mindegy, viszont
    adatbázisból generálva a normalizálási módszereket formálisan rögzíteni kellene majd."]

    [:p "A másik normalizálás a keletkezés településének nagyobb földrajzi egysége: egyelőre nem
    áll rendelkezésre a Felvidék, Partium, Erdély, et al. kategorizálás, bár feleségem megpróbálta
    besorolni a városokat, de nem szakértő, így most a teszt idejére inkább maradtam a wikipédia
    jelenlegi országok szerinti besorolásánál. Légyszi az alábbi grafikonoknál nézd el ezt a
    szépséghibát, majd küldök besorolni-való városlistát és akkor újragyártjuk a grafikonokat,
    de most direkt nem ilyen adatszerkezeti kérdésekkel kedtem a beszámolómat, hogy könnyebb
    legyen örülni az eredményeknek, noha a neheze még előttünk áll."]

    [:p "Tehát ilyen adatsorral indultam el:"]

    [:pre "Abony|Magyarország|1744
Alsónémedi|Magyarország|1744
Alsónémedi|Magyarország|1744
Altorja|Magyarország|1697
Altorja|Magyarország|1707
Amsterdam|Hollandia|1680
Bag|Magyarország|1716
Bag|Magyarország|1746
Bag|Magyarország|1746
Báji|Magyarország|1738
Balázsfalva|Románia|1735
Barskapronca|Szlovákia|1626
Barskapronca|Szlovákia|1647
...
Zabola|Románia|1680
Zilah|Románia|1689
Zólyom|Szlovákia|1589
Zólyom|Szlovákia|1589
Zólyom|Szlovákia|1622
Zólyom|Szlovákia|1647
Zundrava|Ausztria|1659
Zundrava|Ausztria|1659
Zsámbok|Magyarország|1725
Zsámbok|Magyarország|1727
Zsarnóca|Szlovákia|1674
Zsolna|Szlovákia|1706
Zsolna|Szlovákia|1712"]]))

(defonce anno (reagent/atom false))

(defn charts [css]
    [:ol css

    [:li (when @anno [:a {:href "anno/line.html"} "line.html"])
    [:p "Triviális ábrázolás: vonalas, évenként önállóan, országonként csoportosítva, külön-külön
    alapszintről mérve: egymást takarják: csúnya. A szinezés alapértelmezés szerinti, persze
    tetszőlegesen lehet rajta változtatni."]
    [toggle charts/-line]]

    [:li (when @anno [:a {:href "anno/spline.html"} "spline.html"])
    [:p "Előbbi azért csúnyácska, mert a magassági adatok, azaz a jegyzékek száma az egymást követő
    évekkel nem arányosan változik, hanem tekintélyes ingadozással: kvázi össze-vissza. Első
    körben ezen úgy lehetne segíteni, ha a mezei vonal helyett görbület-illesztést (lánykori
    neve: spline) használunk. Sajnos, sokkal nem lett jobb."]
    [toggle charts/-spline]]

    [:li (when @anno [:a {:href "anno/line-stacked.html"} "line-stacked.html"])
    [:p "Tegyük egy kicsit érthetőbbé a képet: a magassági adatok rétegződjenek egymásra, ezáltal
    a jegyzékek száma összeadódik. Hmm.. még mindig száraz és nehezen átlátható, de az abszolút
    magasság már érzékelhetően jellemzi a jegyzékek számát összesen."]
    [toggle charts/-line-stacked]]

    [:li (when @anno [:a {:href "anno/spline-stacked.html"} "spline-stacked.html"])
    [:p "Finomítsunk az előbbin spline használatával. Annyira össze-vissza adatsor, hogy ez sem
    segít sokat. Persze jobb lenne a helyzetünk, ha egy nagyságrenddel több jegyzékünk lenne,
    majd talán a többi adattár feldolgozása után.."]
    [toggle charts/-spline-stacked]]

    [:li (when @anno [:a {:href "anno/line-percent.html"} "line-percent.html"])
    [:p "Kipróbáltam még az előbbi egymásra rétegződő ábrázolást ezúttal százalékos arányban
    szétterítve a függőleges tengely mentén. Csúcs lett, de nem biztos, hogy mindenkinek."]
    [toggle charts/-line-percent]]

    [:li (when @anno [:a {:href "anno/spline-percent.html"} "spline-percent.html"])
    [:p "Ugyanez spline finomítással. Detto."]
    [toggle charts/-spline-percent]]

    [:li (when @anno [:a {:href "anno/area.html"} "area.html"])
    [:p "Nézzük meg, mit segít, ha egyszerű vonalak helyett kitöltött felületekkel ábrázoljuk
    ugyanazt az adatsort, amit az előbb vizsgáltunk. Alig lett jobb."]
    [toggle charts/-area]]

    [:li (when @anno [:a {:href "anno/area-spline.html"} "area-spline.html"])
    [:p "Hátha a spline segít.. nem sokat."]
    [toggle charts/-area-spline]]

    [:li (when @anno [:a {:href "anno/area-stacked.html"} "area-stacked.html"])
    [:p "Rétegezzük egymásra. Hmm.. nem igazán."]
    [toggle charts/-area-stacked]]

    [:li (when @anno [:a {:href "anno/area-stacked-spline.html"} "area-stacked-spline.html"])
    [:p "Spline? Talán még ez a legkevésbbé gagyi?"]
    [toggle charts/-area-stacked-spline]]

    [:li (when @anno [:a {:href "anno/area-percent.html"} "area-percent.html"])
    [:p "A szimmetria kedvéért: százalékos arány. Valamit ez is megmutat."]
    [toggle charts/-area-percent]]

    [:li (when @anno [:a {:href "anno/area-percent-spline.html"} "area-percent-spline.html"])
    [:p "Százalékos spline. Biztos akad, akinek kedvence.."]
    [toggle charts/-area-percent-spline]]

    [:li (when @anno [:a {:href "anno/spline-stacked-sum10.html"} "spline-stacked-sum10.html"])
    [:p "Most, hogy láttuk, mit lehet elérni éves szinten rétegzéssel, vagy anélkül, százalékosan,
    vagy összegszerűen... mi lenne, ha azzal tompítanánk az adatsor irregularitását, hogy
    évtizedenként összevonjuk az adatpontokat? Persze lehetne negyedszázadonként is, vagy
    sűrűbben, mindegy, most könnyű volt az utolsó számjegyet lehagyni az eredményhez: wow!"]
    [toggle charts/-spline-stacked-sum10]]

    [:li (when @anno [:a {:href "anno/area-spline-stacked-sum10.html"} "area-spline-stacked-sum10.html"])
    [:p "Ugyanez terület-kitöltéssel: wow+wow! Ja, e két utóbbinak eleve emeli a szépségét,
    ha görbét (spline) illesztünk a már így is csak közelítő adatpontokra."]
    [toggle charts/-area-spline-stacked-sum10]]

    [:li (when @anno [:a {:href "anno/spline-percent-sum10.html"} "spline-percent-sum10.html"])
    [:p "Most ugyanez vonalasan, százalákosan: nekem tecccik."]
    [toggle charts/-spline-percent-sum10]]

    [:li (when @anno [:a {:href "anno/area-spline-percent-sum10.html"} "area-spline-percent-sum10.html"])
    [:p "Területi kitöltéssel: még inkább teccik."]
    [toggle charts/-area-spline-percent-sum10]]

    [:li (when @anno [:a {:href "anno/pie.html"} "pie.html"])
    [:p "És most következzen valami egészen más: pie chart -- magyarul nem tudom mi ez: körgrafikon?
    A lényeg, hogy most elbúcsúzunk egy rövid időre az időtengelytől és az ábrázolható kategóriák
    száma korlátozott, de igen látványos. A cikk(ely)ek kattinthatóak."]
    [toggle charts/-pie]]

    [:li (when @anno [:a {:href "anno/pie-3d.html"} "pie-3d.html"])
    [:p "És mi lenne, ha beforgatnánk térbe? Na, ettől már bizsereg."]
    [toggle charts/-pie-3d]]

    [:li (when @anno [:a {:href "anno/pie-drilldown.html"} "pie-drilldown.html"])
    [:p "Akkor most tegyünk rá egy lapáttal: az országokban laknak városok: kattints egy cikk(ely)re."]
    [toggle charts/-pie-drilldown]]

    [:li (when @anno [:a {:href "anno/pie-drilldown-ordered.html"} "pie-drilldown-ordered.html"])
    [:p "Kicsit kusza, ha sok a város. Mi lenne, ha nem ABC szerint, hanem az adott helyen
    keletkezett jegyzékek száma szerint rendeznénk az óra járásával megegyezően.. talán jobb lett."]
    [toggle charts/-pie-drilldown-ordered]]

    [:li (when @anno [:a {:href "anno/pie-drilldown-ordered-3d.html"} "pie-drilldown-ordered-3d.html"])
    [:p "Forgassuk be térbe és lyukasszuk ki a közepét: wowwww!"]
    [toggle charts/-pie-drilldown-ordered-3d]]

    [:li (when @anno [:a {:href "anno/pie-drilldown-ordered-min5.html"} "pie-drilldown-ordered-min5.html"])
    [:p "Csökkentsük a számosságot: azok a városok számítanak, ahol min. 5 jegyzék keletkezett."]
    [toggle charts/-pie-drilldown-ordered-min5]]

    [:li (when @anno [:a {:href "anno/pie-drilldown-ordered-min5-3d.html"} "pie-drilldown-ordered-min5-3d.html"])
    [:p "Mindez térben."]
    [toggle charts/-pie-drilldown-ordered-min5-3d]]

    [:li (when @anno [:a {:href "anno/pie-donut.html"} "pie-donut.html"])
    [:p "A végére édesség: fánk! Kicsit egymásra másznak címkék, nem jöttem rá hamar, majd egyszer."]
    [toggle charts/-pie-donut]]

    [:li (when @anno [:a {:href "anno/column-drilldown-ordered-min5.html"} "column-drilldown-ordered-min5.html"])
    [:p "Még mindig ugyanannál az adatsornál tartunk. Most jöjjenek a mezei oszlopok, persze
    mind kattintható, csak kicsit csökkenteni kellett a számosságukat."]
    [toggle charts/-column-drilldown-ordered-min5]]

    [:li (when @anno [:a {:href "anno/column-drilldown-ordered-min5-3d.html"} "column-drilldown-ordered-min5-3d.html"])
    [:p "Forgassuk térbe! Még mindig kattinthatóan."]
    [toggle charts/-column-drilldown-ordered-min5-3d]]

    [:li (when @anno [:a {:href "anno/column-drilldown-ordered-min5-3d-slider.html"} "column-drilldown-ordered-min5-3d-slider.html"])
    [:p "Most aztán forgathatod Te is! Még mindig kattinthatóan."]
    [toggle charts/-column-drilldown-ordered-min5-3d-slider]]

    [:li (when @anno [:a {:href "anno/column-drilldown-inverted.html"} "column-drilldown-inverted.html"])
    [:p "Most kicsit másképp forgatva: álló oszlopok helyett kidőlt oszlopok. Függőlegesen így talán
    sokkal több fér el, sőt."]
    [toggle charts/-column-drilldown-inverted]]

    [:li (when @anno [:a {:href "anno/column-stacked-3d.html"} "column-stacked-3d.html"])
    [:p "Most rétegezzük az oszlopokat. Térben."]
    [toggle charts/-column-stacked-3d]]

    [:li (when @anno [:a {:href "anno/column-stacked-3d-verso.html"} "column-stacked-3d-verso.html"])
    [:p "Mint előbb és ha megfogod a bal egérgombbal, még forgathatod is! Yuck!"]
    [toggle charts/-column-stacked-3d-verso]]

    [:li (when @anno [:a {:href "anno/scatter.html"} "scatter.html"])
    [:p "Még mindig ugyanannál az adatsornál tartunk. Pedig következzen valami egészen más:
    pöttyök szétszórva: megfigyelendő a kvantálási jelenség a függőleges tengely (jegyzékek
    száma) szűk értékkészlete okán."]
    [toggle charts/-scatter]]

    [:li (when @anno [:a {:href "anno/scatter-3d.html"} "scatter-3d.html"])
    [:p "Mindez térben és ha megfogod: forgatható, valamint az ország nevére kattintva
    ki/bekapcsolható az értékkészlete. Van benne egy kis halszem optika torzítás,
    majd kiveszem, ha rájövök."]
    [toggle charts/-scatter-3d]]

    [:li (when @anno [:a {:href "anno/bubble.html"} "bubble.html"])
    [:p "Non-plus-ultra: buborékok: kellett egy harmadik numerikus érték: erőltetett, de
    működik: a buborék mérete arányos az adott időben jegyzéket megjelentető országok
    számával."]
    [toggle charts/-bubble]]

    [:li (when @anno [:a {:href "anno/bubble-map.html"} "bubble-map.html"])
    [:p "Plus-ultra: a buborékok városok, méretük arányos az adott időben keletkezett
    jegyzékek számával ÉS a helyzetük GPS koordináták alapján hozzávetőleges
    földrajzi pozíciójukat jelzi: naív térkép ÉS nagyítható mindkét tengely mentén,
    valamint a SHIFT lenyomásával vízszintesen görgethető a nagyítás."]
    [toggle charts/-bubble-map]]

    [:li (when @anno [:a {:href "anno/heatmap.html"} "heatmap.html"])
    [:p "Ultra-plus-ultra: hőtérkép (heatmap) azaz hol-mikor keletkezett a legtöbb
    jegyzék. Persze ebből lehetne buborékos is, de nem akartam túlzásba vinni."]
    [toggle charts/-heatmap]]

    [:li (when @anno [:a {:href "anno/pyramid.html"} "pyramid.html"])
    [:p "Hab a tortán: egyre speciálisabb feltételek szerint: piramis."]
    [toggle charts/-pyramid]]

    [:li (when @anno [:a {:href "anno/range.html"} "range.html"])
    [:p "Eddig tartott az első adatsor. Pont. Most pedig nézzük a főnemesek életét:
    fekvő oszlop-intervallum."]
    [toggle charts/-range]]

    [:li (when @anno [:a {:href "anno/range-pie.html"} "range-pie.html"])
    [:p "Mindez kombinálva a nemük és felekezeti hovatartozásuk szerint kiegészítő
    körgrafikonokkal. Innen is látszik, ha a protestánsok nem osztódtak volna,
    lenyomták volna a katolikusokat; így fan ez nálunk (is)."]
    [toggle charts/-range-pie]]

    [:li (when @anno [:a {:href "anno/tree.html"} "tree.html"])
    [:p "Nem csak népességfa lehet ilyen szerkezetű, hanem felekezeti-fa is."]
    [toggle charts/-tree]]

    [:li (when @anno [:a {:href "anno/tree-pie.html"} "tree-pie.html"])
    [:p "Kiegészítő körgrafikonnal, mert kínálkozott egy kis szabad terület.
    Ebből is látszik, ha a köznép számít, a lutheránusok győztek."]
    [toggle charts/-tree-pie]]

    [:li (when @anno [:a {:href "anno/pie-3d-ortus.html"} "pie-3d-ortus.html"])
    [:p "Innentől a ténylegesen feltett kérdéseidre válaszolok esetleges szűkítések
    nélkül, mert grafikonok ezen teszt állapotában az nem-oszt-szoroz, csak csökkenti
    az adatsor méretét. Tehát: a társdadalmi rang aránya körgrafikonon."]
    [toggle charts/-pie-3d-ortus]]

    [:li (when @anno [:a {:href "anno/pie-3d-offic.html"} "pie-3d-offic.html"])
    [:p "Ugyanez a foglalkozások szerint."]
    [toggle charts/-pie-3d-offic]]

    [:li (when @anno [:a {:href "anno/bar-offic.html"} "bar-offic.html"])
    [:p "Mivel igen tekintélyes a foglalkozások értékkészlete, inkább a fekvő
    oszlopok segitenek, kiemelve körgrafikonon a legnépszerűbb szakmákat."]
    [toggle charts/-bar-offic]]

    [:li (when @anno [:a {:href "anno/heatmap-typo.html"} "heatmap-typo.html"])
    [:p "Forrástipológia-hőtérkép. Győzött a hagyatéki összeírás."]
    [toggle charts/-heatmap-typo]]

    [:li (when @anno [:a {:href "anno/seneca-ortus.html"} "seneca-ortus.html"])
    [:p "Leginkább polgárok olvasták Seneca műveit, főleg a 17. század 3. negyedében,
    vagy csak ebbe az irányba torzít a jegyzékek jelen összeállítása."]
    [toggle charts/-seneca-ortus]]

    [:li (when @anno [:a {:href "anno/seneca-religio.html"} "seneca-religio.html"])
    [:p "A legtöbben katolikusok olvasták Seneca műveit, főleg a 17. század 3. negyedében,
    vagy csak ebbe az irányba torzít a jegyzékek jelen összeállítása."]
    [toggle charts/-seneca-religio]]

    [:li (when @anno [:a {:href "anno/mokka-r-cent.html"} "mokka-r-cent.html"])
    [:p "Most pedig következzen valami egészen más: a Mokka-R helyben, tehát közvetlenül
    hozzáférhető másolatban tárolt rekordjai alapján: a kötetek keletkezési ideje
    évszázadok szerint, az egyszerűség kedvéért beleértve a facsimile kiadásokat is."]
    [toggle charts/-mokka-r-cent]]

    [:li (when @anno [:a {:href "anno/mokka-r-bibl.html"} "mokka-r-bibl.html"])
    [:p "Mokka-R gyűjtemények mérete kötetek (~ rekordok) száma szerint. Lehet, hogy
    nem jól gondolom, de " [:i "bibliográfia"] " címkével külön számoltam az " [:i "Apponyi"] ",
    " [:i "RMK"] " és " [:i "RMNy"] " állományokat."]
    [toggle charts/-mokka-r-bibl]]

    [:li (when @anno [:a {:href "anno/mokka-r-datum.html"} "mokka-r-datum.html"])
    [:p "Mokka-R kötetek száma megjelenési év szerint. Itt nem kellett évtizedre
    összesíteni és jót tett neki a spline, mert ez egy olyan adatsor. Figyelemre
    méltó az évszázadonként emelkedő fűrészfog. Nem tudom mi az oka, de ez valami
    artifact."]
    [toggle charts/-mokka-r-datum]]

    [:li (when @anno [:a {:href "anno/mokka-r-innen.html"} "mokka-r-innen.html"])
    [:p "Hőtérkép határon innen."]
    [toggle charts/-mokka-r-innen]]

    [:li (when @anno [:a {:href "anno/mokka-r-onnan.html"} "mokka-r-onnan.html"])
    [:p "Hőtérkép határon túl. Bocsi, ha esetleg a Helischer gyűjteményt rossz
    helyre soroltam, most a teszt idejére így sikerült."]
    [toggle charts/-mokka-r-onnan]]

    [:li (when @anno [:a {:href "anno/mokka-r-app.html"} "mokka-r-app.html"])
    [:p "MNB és Mokka-R metszetének hőtérképe. Ha ez ugyan az MNB; nem vagyok kellően képzett."]
    [toggle charts/-mokka-r-app]]

    [:li (when @anno [:a {:href "anno/humanus-099b.html"} "humanus-099b.html"])
    [:p "Eddig volt Eruditio és Mokka-R. Mivel lenne Humanus et al. felé kapcsolat és
    mivel jelen projekten küzdve a nyáron elhanyagoltam Kincsőt, talán engesztelésül is
    készítettem négy pillanatképet a Humanus jelenéből és múltjából. Elsőként
    a feldolgozó intézmények aránya."]
    [toggle charts/-humanus-099b]]

    [:li (when @anno [:a {:href "anno/humanus-099a.html"} "humanus-099a.html"])
    [:p "Majd a feldolgozás jellege/foka."]
    [toggle charts/-humanus-099a]]

    [:li (when @anno [:a {:href "anno/humanus-creat8-modif5.html"} "humanus-creat8-modif5.html"])
    [:p "Humanus rekordok adatai alapján 2000 óta (átemeléssel, ill. konverzióval
    számos tétel került a rendszerbe annak hivatalos indulása előtti időkből)
    a létrehozások és módosítások száma."]
    [toggle charts/-humanus-creat8-modif5]]

    [:li (when @anno [:a {:href "anno/humanus-creat-modif.html"} "humanus-creat-modif.html"])
    [:p "Tényleges létrehozások és módosítások száma szerkesztői napló alapján."]
    [toggle charts/-humanus-creat-modif]]

    [:li (when @anno [:a {:href "anno/pie-pie.html"} "pie-pie.html"])
    [:p "Bonus."]
    [toggle charts/-pie-pie]]])

(defn koraujkor []
    (let [left {:style {:text-align "left"}}
          justify {:style {:text-align "justify"}}]
    [:div
        [title]
        [intro justify]
        [charts left]

        [:p "Egyelőre ennyi."]

        [:div justify
        [:p "Apropo: minden grafikon jobb felső sarkában három vízszintes vonal: egy menü,
        ahonnan kinyomtathatod, ill. lementheted a képet, ha ez segít valamiben."]]]))

(reagent/render-component [koraujkor] (. js/document (getElementById "koraujkor")))
