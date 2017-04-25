(ns koraujkor.charts
    (:require [reagent.core :as reagent]))

(def -model
{
    :credits {
        :enabled false
    }
})

(defn- chart [style model]
    (reagent/create-class {:reagent-render #(do [:div {:style style}])
                           :component-did-mount #(.highcharts (js/$ (reagent/dom-node %)) (clj->js (merge -model model)))}))

(defn- chart' [style model']
    (reagent/create-class {:reagent-render #(do [:div {:style style}])
                           :component-did-mount #(.highcharts (js/$ (reagent/dom-node %)) (clj->js (merge -model (model'))))
                           :component-did-update #(.highcharts (js/$ (reagent/dom-node %)) (clj->js (assoc-in (merge -model (model')) [:plotOptions :series :animation] false)))}))

(.setOptions js/Highcharts (clj->js
{
    :lang {
        :months [
            "január", "február", "március"
            "április", "május", "június"
            "július", "augusztus", "szeptember"
            "október", "november", "december"
        ]
        :shortMonths [
            "jan", "feb", "már"
            "ápr", "máj", "jún"
            "júl", "aug", "szept"
            "okt", "nov", "dec"
        ]
    }
}))

(defn- color [n]
    (nth (.. js/Highcharts getOptions -colors) n))
(defn- rgba [rgb a]
    (.. js/Highcharts (Color (clj->js rgb)) (setOpacity a) (get "rgba")))
(defn- brighten [rgb x]
    (.. js/Highcharts (Color (clj->js rgb)) (brighten x) (get "rgb")))
(defn- brighten' [c x]
    (.. js/Highcharts (Color (clj->js c)) (brighten x) get))

(def gradient2
    (into [] (map #(do {:radialGradient {:cx 0.5 :cy 0.3 :r 0.7} :stops [[0, %] [1, (brighten % -0.3)]]}) ; darken
        (.. js/Highcharts getOptions -colors))))

(def gradient3
    (into [] (map #(do {:radialGradient {:cx 0.4 :cy 0.3 :r 0.5} :stops [[0, %] [1, (brighten % -0.2)]]}) ; darken
        (.. js/Highcharts getOptions -colors))))

(defn- date [y m]
    (.UTC js/Date y m))


(def jegyzékek-száma-keletkezési-hely-szerint
    [{
        :name "Ausztria"
        :data [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 1 0 0 0 0 1 0 0 0 1 1 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 1 0 1 0 1 0 0 0 0 0 1 1 0 0 0 1 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 1 0 0 0 1 0 1 1 0 3 1 0 0 1 0 0 5 0 0 1 2 1 1 0 20 0 0 2 4 1 1 3 2 3 2 1 0 3 0 8 0 0 1 3 1 2 0 1 2 4 0 1 0 0 0 0 0 0 0 0 0 1 1 0 0 2 1 0 0 0 2 2 0 0 0 0 0 0 2 0 0 0 1 1 0 0 1 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0 0 0 0 0]
    }, {
        :name "Magyarország"
        :data [0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1 0 0 0 1 0 0 0 0 2 0 1 0 0 0 1 0 0 0 1 0 0 0 0 1 0 0 0 0 0 0 1 0 0 1 0 0 0 1 0 0 0 0 0 0 3 0 1 0 0 0 0 1 1 0 0 0 1 1 0 2 1 1 2 0 2 1 3 0 0 1 2 1 2 1 3 1 2 5 4 0 5 1 2 4 0 1 0 2 5 5 1 4 5 7 3 5 4 3 5 1 10 3 9 4 1 2 1 5 5 3 2 1 3 2 1 2 1 5 1 2 2 2 3 7 1 0 4 2 1 4 1 3 0 1 0 0 2 2 1 1 3 1 4 1 1 0 2 4 3 3 2 0 2 3 6 2 1 1 5 6 3 8 3 7 2 1 2 4 9 5 23 5 3 2 1 2 5 5 3 2 3 5 2 4 1 1 3 17 6 15 1 0 2 4 0 0 0 0]
    }, {
        :name "Románia"
        :data [0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 1 0 1 0 0 1 0 1 2 1 1 1 0 1 2 3 2 3 1 2 0 2 0 0 0 4 0 0 2 2 0 0 0 2 1 0 3 2 0 1 1 1 0 2 2 0 0 1 0 2 3 4 1 1 3 3 1 1 1 4 6 2 6 2 1 3 1 1 0 0 1 6 8 1 3 9 2 4 7 4 3 2 4 3 3 2 1 14 3 2 3 1 7 3 4 3 2 3 7 5 3 6 6 3 4 9 3 2 7 2 6 1 1 7 7 7 2 3 2 2 13 3 2 7 7 7 4 4 6 7 4 2 4 1 5 5 3 0 2 5 0 5 3 0 6 3 6 2 2 3 1 3 5 0 6 4 0 6 1 3 5 3 8 2 3 9 2 5 4 0 2 5 6 1 10 3 0 0 0 0]
    }, {
        :name "Szlovákia"
        :data [2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 1 0 0 1 0 1 0 0 1 2 0 1 0 3 1 0 1 2 1 0 1 1 1 1 2 0 2 3 1 1 1 1 2 1 2 1 4 1 2 1 1 4 3 0 5 6 0 8 3 3 2 3 1 3 3 2 2 6 1 2 10 5 3 8 3 4 3 1 0 5 1 2 1 9 3 2 2 5 6 7 4 5 6 8 3 2 4 7 4 3 1 3 6 7 13 4 3 3 4 3 1 7 3 4 14 4 3 2 1 5 3 3 7 2 3 2 0 5 2 5 4 12 4 8 8 3 6 16 4 4 8 8 7 6 5 5 5 5 4 7 4 7 5 1 4 6 3 8 2 1 0 2 3 4 0 1 0 2 5 2 0 4 1 0 1 1 0 1 0 1 0 1 3 0 0 0 4 1 3 2 1 0 1 0 0 0 1 0 0 0 2 0 0 0 0 1 3 2 0 0 0 1]
    }])


(defn- psv-data [psv]
    (into [] (map (fn [[a & z]] {:name a :y (reduce + (map second z)) :drilldown a}) psv)))

(defn- psv-series [psv]
    (into [] (map (fn [[a & z]] {:name a :id a :data z}) psv)))

(def psv0 [#_["Ország" ["Város" "Jegyzékek száma"]]
    ["Ausztria" ["Bécs" 8]
                ["Bécsújhely" 1]
                ["Borsmonostor" 1]
                ["Büdöskút" 1]
                ["Feketeváros" 1]
                ["Felsőlászló" 1]
                ["Fertőszéleskút" 1]
                ["Fraknó" 1]
                ["Graz" 2]
                ["Grossfeld" 1]
                ["Kabold" 2]
                ["Királyfalva" 1]
                ["Kisboldogasszony" 1]
                ["Kishöflány" 4]
                ["Kismarton" 5]
                ["Köpcsény" 1]
                ["Lajtapordány" 3]
                ["Lók" 1]
                ["Loretto" 1]
                ["Mannersdorff" 1]
                ["Marienburg" 1]
                ["Miklósfalva" 1]
                ["Missen" 1]
                ["Neithausen" 1]
                ["Németújvár" 11]
                ["Neustadt" 1]
                ["Nezsidér" 4]
                ["Northeim" 1]
                ["Nyulas" 1]
                ["Oroszvár" 2]
                ["Oszlop" 1]
                ["Pándorfalu" 1]
                ["Pordány" 1]
                ["Pottendorf" 3]
                ["Répcesarud" 1]
                ["Ruszt" 35]
                ["Somorja" 2]
                ["Sopronnyék" 2]
                ["Stolzenburg" 1]
                ["Szározvám" 2]
                ["Szentgyörgy" 1]
                ["Szentmargitbánya" 1]
                ["Tormafalu" 1]
                ["Védeny" 2]
                ["Völgyfalu" 1]
                ["Warmia" 1]
                ["Zundrava" 2]]
    ["Csehország" ["Prága" 1]]
    ["Hollandia" ["Amsterdam" 1]
                 ["Franeker" 4]
                 ["Utrecht" 1]]
    ["Horvátország" ["Csáktornya" 2]
                    ["Horvátkimle" 2]]
    ["Lengyelország" ["Boroszló" 3]]
    ["Magyarország" ["Abony" 1]
                    ["Alsónémedi" 2]
                    ["Altorja" 2]
                    ["Bag" 3]
                    ["Báji" 1]
                    ["Bercel" 1]
                    ["Berkesz" 1]
                    ["Bezenye" 1]
                    ["Bodok" 1]
                    ["Boldog" 2]
                    ["Buda" 16]
                    ["Buják" 4]
                    ["Csanak" 1]
                    ["Csongrád" 1]
                    ["Debrecen" 27]
                    ["Dömölk" 1]
                    ["Dunaharaszti" 1]
                    ["Dunakeszi" 1]
                    ["Ecseg" 6]
                    ["Ecser" 1]
                    ["Eger" 2]
                    ["Esztergom" 4]
                    ["Fegyvernek" 1]
                    ["Fertőszentmiklós" 1]
                    ["Galgamácsa" 2]
                    ["Gyöngyös" 3]
                    ["Győr" 2]
                    ["Harsány" 1]
                    ["Hatvan" 1]
                    ["Herencsény" 1]
                    ["Hévíz" 1]
                    ["Hódmezővásárhely" 1]
                    ["Hunfalva" 1]
                    ["Isaszeg" 1]
                    ["Jobbágyi" 6]
                    ["Kálló" 3]
                    ["Kecskemét" 1]
                    ["Kerepes" 1]
                    ["Keresd" 2]
                    ["Kóka" 6]
                    ["Korlát" 1]
                    ["Kosd" 1]
                    ["Körmend" 1]
                    ["Kőszeg" 27]
                    ["Lébény" 1]
                    ["Lőrinc" 1]
                    ["Mácsa" 1]
                    ["Magyarnádor" 2]
                    ["Magyaróvár" 1]
                    ["Mezőkövesd" 1]
                    ["Mosonmagyaróvár" 1]
                    ["Mosonszolnok" 1]
                    ["Nagyajta" 1]
                    ["Nagybresztovány" 1]
                    ["Nagykanizsa" 1]
                    ["Nagykáta" 2]
                    ["Nagykőrös" 1]
                    ["Nagylödő" 1]
                    ["Nézsa" 1]
                    ["Nógrád" 2]
                    ["Nógrádverőce" 1]
                    ["Noszlop" 1]
                    ["Nőtincs" 2]
                    ["Páli" 1]
                    ["Palotás" 2]
                    ["Pannonhalma" 3]
                    ["Pécel" 3]
                    ["Pécs" 2]
                    ["Pest" 5]
                    ["Rád" 1]
                    ["Regéc" 1]
                    ["Rékas" 1]
                    ["Romhány" 4]
                    ["Sári" 1]
                    ["Sárospatak" 21]
                    ["Sárvár" 2]
                    ["Sátoraljaújhely" 1]
                    ["Somogyvár" 1]
                    ["Sopron" 147]
                    ["Sűly" 1]
                    ["Sümeg" 3]
                    ["Szalánc" 1]
                    ["Szatmár" 6]
                    ["Szecső" 3]
                    ["Szente" 1]
                    ["Szentlászló" 2]
                    ["Szentlőrinckáta" 2]
                    ["Szentmártonkáta" 1]
                    ["Szentmihály" 1]
                    ["Sziget" 2]
                    ["Szőlős" 2]
                    ["Tápióbicske" 1]
                    ["Tápiószele" 1]
                    ["Tarcal" 1]
                    ["Tereske" 2]
                    ["Tiszafüred" 1]
                    ["Tiszaladány" 2]
                    ["Tóalmás" 5]
                    ["Tokaj" 1]
                    ["Tószeg" 2]
                    ["Tömörd" 1]
                    ["Turóc" 1]
                    ["Úri" 3]
                    ["Üllő" 2]
                    ["Vác" 3]
                    ["Vácszentlászló" 2]
                    ["Veresegyháza" 3]
                    ["Verseg" 1]
                    ["Veszprém" 1]
                    ["Vis" 1]
                    ["Vizkelet" 1]
                    ["Vízvár" 1]
                    ["Zsámbok" 2]]
    ["Németország" ["Drezda" 2]
                   ["Heidelberg" 2]
                   ["Tübingen" 1]
                   ["Ulm" 4]
                   ["Wittenberg" 1]]
    ["Olaszország" ["Bologna" 1]
                   ["Padova" 6]]
    ["Románia" ["Balázsfalva" 1]
               ["Bátos" 1]
               ["Belec" 2]
               ["Beszterce" 93]
               ["Borberek" 1]
               ["Brassó" 34]
               ["Brulya" 2]
               ["Csépán" 1]
               ["Csíksomlyó" 2]
               ["Dál" 1]
               ["Ebesfalva" 1]
               ["Egerbegy" 1]
               ["Erdély" 4]
               ["Fenyőfalva" 2]
               ["Fogaras" 8]
               ["Gerdály" 1]
               ["Gernyeszeg" 17]
               ["Gyergyóalfalu" 4]
               ["Gyergyószárhegy" 1]
               ["Gyulafehérvár" 17]
               ["Holcmány" 1]
               ["Kaplony" 1]
               ["Károlyvár" 1]
               ["Kispéterfalva" 1]
               ["Kolozsvár" 53]
               ["Lesses" 1]
               ["Malomárka" 1]
               ["Márkusfalva" 1]
               ["Marosvásárhely" 12]
               ["Mártonhegy" 2]
               ["Mikháza" 1]
               ["Nagybánya" 12]
               ["Nagyenyed" 12]
               ["Nagyszeben" 128]
               ["Nagyvárad" 1]
               ["Prépostfalva" 2]
               ["Radnót" 4]
               ["Rozsonda" 2]
               ["Rüsz" 1]
               ["Sajószentivány" 1]
               ["Segesvár" 50]
               ["Szászsebes" 1]
               ["Szászszentgyörgy" 1]
               ["Szászújfalu" 1]
               ["Szászváros" 1]
               ["Szászzalatna" 1]
               ["Szeben" 6]
               ["Székelyudvarhely" 10]
               ["Szentágota" 1]
               ["Tarcsafalva" 1]
               ["Torda" 4]
               ["Tövis" 1]
               ["Ujfalu" 1]
               ["Vajdahunyad" 1]
               ["Váldhíd" 1]
               ["Várad" 2]
               ["Zabola" 2]
               ["Zilah" 1]]
    ["Svájc" ["Basel" 2]]
    ["Szlovákia" ["Barskapronca" 3]
                 ["Bártfa" 8]
                 ["Beckó" 1]
                 ["Besztercebánya" 104]
                 ["Biccse" 1]
                 ["Cseklész" 1]
                 ["Csetnek" 1]
                 ["Csiffár" 2]
                 ["Divény" 2]
                 ["Eperjes" 23]
                 ["Érsekújvár" 2]
                 ["Ezeke" 1]
                 ["Farkasfalva" 1]
                 ["Felsőiszkáz" 1]
                 ["Felvidék" 2]
                 ["Fülek" 1]
                 ["Galgóc" 1]
                 ["Garamkürtös" 3]
                 ["Garammindszent" 3]
                 ["Garamszentbenedek" 17]
                 ["Garamszentkereszt" 3]
                 ["Garamszőllős" 1]
                 ["Garany" 1]
                 ["Huszt" 1]
                 ["Illava" 2]
                 ["Ipolyság" 1]
                 ["Jánosgyarmat" 1]
                 ["Jászó" 1]
                 ["Karvaly" 4]
                 ["Kassa" 83]
                 ["Kasza" 1]
                 ["Késmárk" 5]
                 ["Kiskeresnye" 1]
                 ["Kistapolcsány" 1]
                 ["Kisszeben" 1]
                 ["Komárom" 3]
                 ["Körmöcbánya" 37]
                 ["Lelesz" 4]
                 ["Léva" 2]
                 ["Losonc" 1]
                 ["Lőcse" 66]
                 ["Makovicza" 1]
                 ["Mátranovák" 1]
                 ["Modor" 1]
                 ["Mogyorómál" 2]
                 ["Nagylócsa" 1]
                 ["Nagysáros" 3]
                 ["Nagyszombat" 20]
                 ["Nagytapolcsány" 1]
                 ["Nyitra" 4]
                 ["Nyitrazávod" 2]
                 ["Pozsony" 28]
                 ["Pozsonybeszterce" 1]
                 ["Sasvár" 3]
                 ["Savnik" 1]
                 ["Selmecbánya" 85]
                 ["Sokolnitz" 1]
                 ["Szakolca" 5]
                 ["Szedikert" 1]
                 ["Szelnice" 1]
                 ["Szepeshely" 2]
                 ["Szepesvár" 1]
                 ["Szklabinya" 1]
                 ["Temetvény" 1]
                 ["Trencsén" 4]
                 ["Vágsellye" 1]
                 ["Verebély" 2]
                 ["Zólyom" 4]
                 ["Zsarnóca" 1]
                 ["Zsolna" 2]]
    ["Törökország" ["Konstantinápoly" 1]
                   ["Rodostó" 1]]
    ["Ukrajna" ["Munkács" 1]
               ["Ungvár" 7]]
])

(def psv1 [#_["Ország" ["Város" "Jegyzékek száma"]]
    ["Szlovákia" ["Besztercebánya" 104]
                 ["Selmecbánya" 85]
                 ["Kassa" 83]
                 ["Lőcse" 66]
                 ["Körmöcbánya" 37]
                 ["Pozsony" 28]
                 ["Eperjes" 23]
                 ["Nagyszombat" 20]
                 ["Garamszentbenedek" 17]
                 ["Bártfa" 8]
                 ["Szakolca" 5]
                 ["Késmárk" 5]
                 ["Zólyom" 4]
                 ["Trencsén" 4]
                 ["Nyitra" 4]
                 ["Lelesz" 4]
                 ["Karvaly" 4]
                 ["Sasvár" 3]
                 ["Nagysáros" 3]
                 ["Komárom" 3]
                 ["Garamszentkereszt" 3]
                 ["Garammindszent" 3]
                 ["Garamkürtös" 3]
                 ["Barskapronca" 3]
                 ["Zsolna" 2]
                 ["Verebély" 2]
                 ["Szepeshely" 2]
                 ["Nyitrazávod" 2]
                 ["Mogyorómál" 2]
                 ["Léva" 2]
                 ["Illava" 2]
                 ["Felvidék" 2]
                 ["Érsekújvár" 2]
                 ["Divény" 2]
                 ["Csiffár" 2]
                 ["Zsarnóca" 1]
                 ["Vágsellye" 1]
                 ["Temetvény" 1]
                 ["Szklabinya" 1]
                 ["Szepesvár" 1]
                 ["Szelnice" 1]
                 ["Szedikert" 1]
                 ["Sokolnitz" 1]
                 ["Savnik" 1]
                 ["Pozsonybeszterce" 1]
                 ["Nagytapolcsány" 1]
                 ["Nagylócsa" 1]
                 ["Modor" 1]
                 ["Mátranovák" 1]
                 ["Makovicza" 1]
                 ["Losonc" 1]
                 ["Kisszeben" 1]
                 ["Kistapolcsány" 1]
                 ["Kiskeresnye" 1]
                 ["Kasza" 1]
                 ["Jászó" 1]
                 ["Jánosgyarmat" 1]
                 ["Ipolyság" 1]
                 ["Huszt" 1]
                 ["Garany" 1]
                 ["Garamszőllős" 1]
                 ["Galgóc" 1]
                 ["Fülek" 1]
                 ["Felsőiszkáz" 1]
                 ["Farkasfalva" 1]
                 ["Ezeke" 1]
                 ["Csetnek" 1]
                 ["Cseklész" 1]
                 ["Biccse" 1]
                 ["Beckó" 1]]
    ["Románia" ["Nagyszeben" 128]
               ["Beszterce" 93]
               ["Kolozsvár" 53]
               ["Segesvár" 50]
               ["Brassó" 34]
               ["Gyulafehérvár" 17]
               ["Gernyeszeg" 17]
               ["Nagyenyed" 12]
               ["Nagybánya" 12]
               ["Marosvásárhely" 12]
               ["Székelyudvarhely" 10]
               ["Fogaras" 8]
               ["Szeben" 6]
               ["Torda" 4]
               ["Radnót" 4]
               ["Gyergyóalfalu" 4]
               ["Erdély" 4]
               ["Zabola" 2]
               ["Várad" 2]
               ["Rozsonda" 2]
               ["Prépostfalva" 2]
               ["Mártonhegy" 2]
               ["Fenyőfalva" 2]
               ["Csíksomlyó" 2]
               ["Brulya" 2]
               ["Belec" 2]
               ["Zilah" 1]
               ["Váldhíd" 1]
               ["Vajdahunyad" 1]
               ["Ujfalu" 1]
               ["Tövis" 1]
               ["Tarcsafalva" 1]
               ["Szentágota" 1]
               ["Szászzalatna" 1]
               ["Szászváros" 1]
               ["Szászújfalu" 1]
               ["Szászszentgyörgy" 1]
               ["Szászsebes" 1]
               ["Sajószentivány" 1]
               ["Rüsz" 1]
               ["Nagyvárad" 1]
               ["Mikháza" 1]
               ["Márkusfalva" 1]
               ["Malomárka" 1]
               ["Lesses" 1]
               ["Kispéterfalva" 1]
               ["Károlyvár" 1]
               ["Kaplony" 1]
               ["Holcmány" 1]
               ["Gyergyószárhegy" 1]
               ["Gerdály" 1]
               ["Egerbegy" 1]
               ["Ebesfalva" 1]
               ["Dál" 1]
               ["Csépán" 1]
               ["Borberek" 1]
               ["Bátos" 1]
               ["Balázsfalva" 1]]
    ["Magyarország" ["Sopron" 147]
                    ["Kőszeg" 27]
                    ["Debrecen" 27]
                    ["Sárospatak" 21]
                    ["Buda" 16]
                    ["Szatmár" 6]
                    ["Kóka" 6]
                    ["Jobbágyi" 6]
                    ["Ecseg" 6]
                    ["Tóalmás" 5]
                    ["Pest" 5]
                    ["Romhány" 4]
                    ["Esztergom" 4]
                    ["Buják" 4]
                    ["Veresegyháza" 3]
                    ["Vác" 3]
                    ["Úri" 3]
                    ["Szecső" 3]
                    ["Sümeg" 3]
                    ["Pécel" 3]
                    ["Pannonhalma" 3]
                    ["Kálló" 3]
                    ["Gyöngyös" 3]
                    ["Bag" 3]
                    ["Zsámbok" 2]
                    ["Vácszentlászló" 2]
                    ["Üllő" 2]
                    ["Tószeg" 2]
                    ["Tiszaladány" 2]
                    ["Tereske" 2]
                    ["Szőlős" 2]
                    ["Sziget" 2]
                    ["Szentlőrinckáta" 2]
                    ["Szentlászló" 2]
                    ["Sárvár" 2]
                    ["Pécs" 2]
                    ["Palotás" 2]
                    ["Nőtincs" 2]
                    ["Nógrád" 2]
                    ["Nagykáta" 2]
                    ["Magyarnádor" 2]
                    ["Keresd" 2]
                    ["Győr" 2]
                    ["Galgamácsa" 2]
                    ["Eger" 2]
                    ["Boldog" 2]
                    ["Altorja" 2]
                    ["Alsónémedi" 2]
                    ["Vízvár" 1]
                    ["Vizkelet" 1]
                    ["Vis" 1]
                    ["Veszprém" 1]
                    ["Verseg" 1]
                    ["Turóc" 1]
                    ["Tömörd" 1]
                    ["Tokaj" 1]
                    ["Tiszafüred" 1]
                    ["Tarcal" 1]
                    ["Tápiószele" 1]
                    ["Tápióbicske" 1]
                    ["Szentmihály" 1]
                    ["Szentmártonkáta" 1]
                    ["Szente" 1]
                    ["Szalánc" 1]
                    ["Sűly" 1]
                    ["Somogyvár" 1]
                    ["Sátoraljaújhely" 1]
                    ["Sári" 1]
                    ["Rékas" 1]
                    ["Regéc" 1]
                    ["Rád" 1]
                    ["Páli" 1]
                    ["Noszlop" 1]
                    ["Nógrádverőce" 1]
                    ["Nézsa" 1]
                    ["Nagylödő" 1]
                    ["Nagykőrös" 1]
                    ["Nagykanizsa" 1]
                    ["Nagybresztovány" 1]
                    ["Nagyajta" 1]
                    ["Mosonszolnok" 1]
                    ["Mosonmagyaróvár" 1]
                    ["Mezőkövesd" 1]
                    ["Magyaróvár" 1]
                    ["Mácsa" 1]
                    ["Lőrinc" 1]
                    ["Lébény" 1]
                    ["Körmend" 1]
                    ["Kosd" 1]
                    ["Korlát" 1]
                    ["Kerepes" 1]
                    ["Kecskemét" 1]
                    ["Isaszeg" 1]
                    ["Hunfalva" 1]
                    ["Hódmezővásárhely" 1]
                    ["Hévíz" 1]
                    ["Herencsény" 1]
                    ["Hatvan" 1]
                    ["Harsány" 1]
                    ["Fertőszentmiklós" 1]
                    ["Fegyvernek" 1]
                    ["Ecser" 1]
                    ["Dunakeszi" 1]
                    ["Dunaharaszti" 1]
                    ["Dömölk" 1]
                    ["Csongrád" 1]
                    ["Csanak" 1]
                    ["Bodok" 1]
                    ["Bezenye" 1]
                    ["Berkesz" 1]
                    ["Bercel" 1]
                    ["Báji" 1]
                    ["Abony" 1]]
    ["Ausztria" ["Ruszt" 35]
                ["Németújvár" 11]
                ["Bécs" 8]
                ["Kismarton" 5]
                ["Nezsidér" 4]
                ["Kishöflány" 4]
                ["Pottendorf" 3]
                ["Lajtapordány" 3]
                ["Zundrava" 2]
                ["Védeny" 2]
                ["Szározvám" 2]
                ["Sopronnyék" 2]
                ["Somorja" 2]
                ["Oroszvár" 2]
                ["Kabold" 2]
                ["Graz" 2]
                ["Warmia" 1]
                ["Völgyfalu" 1]
                ["Tormafalu" 1]
                ["Szentmargitbánya" 1]
                ["Szentgyörgy" 1]
                ["Stolzenburg" 1]
                ["Répcesarud" 1]
                ["Pordány" 1]
                ["Pándorfalu" 1]
                ["Oszlop" 1]
                ["Nyulas" 1]
                ["Northeim" 1]
                ["Neustadt" 1]
                ["Neithausen" 1]
                ["Missen" 1]
                ["Miklósfalva" 1]
                ["Marienburg" 1]
                ["Mannersdorff" 1]
                ["Loretto" 1]
                ["Lók" 1]
                ["Köpcsény" 1]
                ["Kisboldogasszony" 1]
                ["Királyfalva" 1]
                ["Grossfeld" 1]
                ["Fraknó" 1]
                ["Fertőszéleskút" 1]
                ["Felsőlászló" 1]
                ["Feketeváros" 1]
                ["Büdöskút" 1]
                ["Borsmonostor" 1]
                ["Bécsújhely" 1]]
    ["Németország" ["Ulm" 4]
                   ["Heidelberg" 2]
                   ["Drezda" 2]
                   ["Wittenberg" 1]
                   ["Tübingen" 1]]
    ["Ukrajna" ["Ungvár" 7]
               ["Munkács" 1]]
    ["Olaszország" ["Padova" 6]
                   ["Bologna" 1]]
    ["Hollandia" ["Franeker" 4]
                 ["Utrecht" 1]
                 ["Amsterdam" 1]]
    ["Horvátország" ["Horvátkimle" 2]
                    ["Csáktornya" 2]]
    ["Lengyelország" ["Boroszló" 3]]
    ["Svájc" ["Basel" 2]]
    ["Törökország" ["Rodostó" 1]
                   ["Konstantinápoly" 1]]
    ["Csehország" ["Prága" 1]]
])

(def psv2 [#_["Ország" ["Város" "Jegyzékek száma"]]
    ["Szlovákia" ["Besztercebánya" 104]
                 ["Selmecbánya" 85]
                 ["Kassa" 83]
                 ["Lőcse" 66]
                 ["Körmöcbánya" 37]
                 ["Pozsony" 28]
                 ["Eperjes" 23]
                 ["Nagyszombat" 20]
                 ["Garamszentbenedek" 17]
                 ["Bártfa" 8]
                 ["Szakolca" 5]
                 ["Késmárk" 5]]
    ["Románia" ["Nagyszeben" 128]
               ["Beszterce" 93]
               ["Kolozsvár" 53]
               ["Segesvár" 50]
               ["Brassó" 34]
               ["Gyulafehérvár" 17]
               ["Gernyeszeg" 17]
               ["Nagyenyed" 12]
               ["Nagybánya" 12]
               ["Marosvásárhely" 12]
               ["Székelyudvarhely" 10]
               ["Fogaras" 8]
               ["Szeben" 6]]
    ["Magyarország" ["Sopron" 147]
                    ["Kőszeg" 27]
                    ["Debrecen" 27]
                    ["Sárospatak" 21]
                    ["Buda" 16]
                    ["Szatmár" 6]
                    ["Kóka" 6]
                    ["Jobbágyi" 6]
                    ["Ecseg" 6]
                    ["Tóalmás" 5]
                    ["Pest" 5]]
    ["Ausztria" ["Ruszt" 35]
                ["Németújvár" 11]
                ["Bécs" 8]
                ["Kismarton" 5]]
    ["Ukrajna" ["Ungvár" 7]]
    ["Olaszország" ["Padova" 6]]
])

(def psv3 [#_["Ország" ["Város" "Jegyzékek száma"]]
    ["Szlovákia" ["Besztercebánya" 104]
                 ["Selmecbánya" 85]
                 ["Kassa" 83]
                 ["Lőcse" 66]
                 ["Körmöcbánya" 37]
                 ["Pozsony" 28]
                 ["Eperjes" 23]
                 ["Nagyszombat" 20]
                 ["Garamszentbenedek" 17]
                 ["Bártfa" 8]
                 ["Szakolca" 5]
                 ["Késmárk" 5]
                 ["Zólyom" 4]
                 ["Trencsén" 4]
                 ["Nyitra" 4]
                 ["Lelesz" 4]
                 ["Karvaly" 4]
                 ["Sasvár" 3]
                 ["Nagysáros" 3]
                 ["Komárom" 3]
                 ["Garamszentkereszt" 3]
                 ["Garammindszent" 3]
                 ["Garamkürtös" 3]
                 ["Barskapronca" 3]]
    ["Románia" ["Nagyszeben" 128]
               ["Beszterce" 93]
               ["Kolozsvár" 53]
               ["Segesvár" 50]
               ["Brassó" 34]
               ["Gyulafehérvár" 17]
               ["Gernyeszeg" 17]
               ["Nagyenyed" 12]
               ["Nagybánya" 12]
               ["Marosvásárhely" 12]
               ["Székelyudvarhely" 10]
               ["Fogaras" 8]
               ["Szeben" 6]
               ["Torda" 4]
               ["Radnót" 4]
               ["Gyergyóalfalu" 4]
               ["Erdély" 4]]
    ["Magyarország" ["Sopron" 147]
                    ["Kőszeg" 27]
                    ["Debrecen" 27]
                    ["Sárospatak" 21]
                    ["Buda" 16]
                    ["Szatmár" 6]
                    ["Kóka" 6]
                    ["Jobbágyi" 6]
                    ["Ecseg" 6]
                    ["Tóalmás" 5]
                    ["Pest" 5]
                    ["Romhány" 4]
                    ["Esztergom" 4]
                    ["Buják" 4]
                    ["Veresegyháza" 3]
                    ["Vác" 3]
                    ["Úri" 3]
                    ["Szecső" 3]
                    ["Sümeg" 3]
                    ["Pécel" 3]
                    ["Pannonhalma" 3]
                    ["Kálló" 3]
                    ["Gyöngyös" 3]
                    ["Bag" 3]]
    ["Ausztria" ["Ruszt" 35]
                ["Németújvár" 11]
                ["Bécs" 8]
                ["Kismarton" 5]
                ["Nezsidér" 4]
                ["Kishöflány" 4]
                ["Pottendorf" 3]
                ["Lajtapordány" 3]]
    ["Ukrajna" ["Ungvár" 7]]
    ["Olaszország" ["Padova" 6]]
    ["Németország" ["Ulm" 4]]
    ["Hollandia" ["Franeker" 4]]
    ["Lengyelország" ["Boroszló" 3]]
])

(def psv4 [#_["Nagyobb földrajzi egység" ["Település" "Jegyzékek száma"]]
    ["Felföld" ["Besztercebánya" 104]
               ["Selmecbánya" 85]
               ["Kassa" 83]
               ["Lőcse" 66]
               ["Körmöcbánya" 37]
               ["Pozsony" 28]
               ["Eperjes" 23]
               ["Sárospatak" 21]
               ["Nagyszombat" 20]
               ["Garamszentbenedek" 17]
               ["Bártfa" 8]
               ["Ungvár" 7]
               ["Késmárk" 5]
               ["Szakolca" 5]
               ["Karvaly" 4]
               ["Lelesz" 4]
               ["Nyitra" 4]
               ["Trencsén" 4]
               ["Barskapronca" 3]
               ["Garamkürtös" 3]
               ["Garammindszent" 3]
               ["Garamszentkereszt" 3]
               ["Nagysáros" 3]
               ["Sasvár" 3]
               ["Zólyom" 3]
               ["Belec" 2]
               ["Csiffár" 2]
               ["Divény" 2]
               ["Érsekújvár" 2]
               ["Felvidék" 2]
               ["Illava" 2]
               ["Kisszeben" 2]
               ["Léva" 2]
               ["Mogyorómál" 2]
               ["Nyitrazávod" 2]
               ["Szepeshely" 2]
               ["Verebély" 2]
               ["Zsolna" 2]
               ["Báji" 1]
               ["Beckó" 1]
               ["Biccse" 1]
               ["Bodok" 1]
               ["Cseklész" 1]
               ["Csetnek" 1]
               ["Ezeke" 1]
               ["Farkasfalva" 1]
               ["Fülek" 1]
               ["Galgóc" 1]
               ["Garamszőllős" 1]
               ["Garany" 1]
               ["Gömör megye" 1]
               ["Hajnik" 1]
               ["Hunfalva" 1]
               ["Ipolyság" 1]
               ["Jánosgyarmat" 1]
               ["Jászó" 1]
               ["Karancs Beriny" 1]
               ["Kasza" 1]
               ["Kiskeresnye" 1]
               ["Kistapolcsány" 1]
               ["Korlát" 1]
               ["Losonc" 1]
               ["Makovicza" 1]
               ["Márkusfalva" 1]
               ["Missen" 1]
               ["Modor" 1]
               ["Munkács" 1]
               ["Nagybresztovány" 1]
               ["Nagylócsa" 1]
               ["Nagytapolcsány" 1]
               ["Pozsonybeszterce" 1]
               ["Regéc" 1]
               ["Sátoraljaújhely" 1]
               ["Savnik" 1]
               ["Stolzenburg" 1]
               ["Szalánc" 1]
               ["Szedikert" 1]
               ["Szelnice" 1]
               ["Szentmihály" 1]
               ["Szepesvár" 1]
               ["Szklabinya" 1]
               ["Temetvény" 1]
               ["Turóc" 1]
               ["Vágsellye" 1]
               ["Zólyom vármegye" 1]
               ["Zsarnóca" 1]]
    ["Erdély" ["Nagyszeben" 133]
              ["Beszterce" 93]
              ["Kolozsvár" 53]
              ["Segesvár" 50]
              ["Brassó" 34]
              ["Gernyeszeg" 19]
              ["Gyulafehérvár" 18]
              ["Marosvásárhely" 12]
              ["Nagyenyed" 12]
              ["Székelyudvarhely" 10]
              ["Fogaras" 8]
              ["Erdély" 4]
              ["Gyergyóalfalu" 4]
              ["Radnót" 4]
              ["Torda" 4]
              ["Keresd" 3]
              ["Altorja" 2]
              ["Brulya" 2]
              ["Csíksomlyó" 2]
              ["Fenyőfalva" 2]
              ["Mártonhegy" 2]
              ["Prépostfalva" 2]
              ["Rozsonda" 2]
              ["Szászváros" 2]
              ["Zabola" 2]
              ["Balázsfalva" 1]
              ["Bátos" 1]
              ["Borberek" 1]
              ["Borsmonostor" 1]
              ["Csépán" 1]
              ["Csíkszentmihály" 1]
              ["Dál" 1]
              ["Ebesfalva" 1]
              ["Egerbegy" 1]
              ["Gerdály" 1]
              ["Grossfeld" 1]
              ["Gyergyószárhegy" 1]
              ["Holcmány" 1]
              ["Homoródszentmárton v. Ürmös" 1]
              ["Huszt" 1]
              ["Keresztényfalva" 1]
              ["Kispéterfalva" 1]
              ["Lesses" 1]
              ["Malomárka" 1]
              ["Marienburg" 1]
              ["Mátranovák" 1]
              ["Mikháza" 1]
              ["Nagyajta" 1]
              ["Netus" 1]
              ["Rüsz" 1]
              ["Sajószentivány" 1]
              ["Szászsebes" 1]
              ["Szászszentgyörgy" 1]
              ["Szászújfalu" 1]
              ["Szászzalatna" 1]
              ["Szentágota" 1]
              ["Tarcsafalva" 1]
              ["Tövis" 1]
              ["Vajdahunyad" 1]
              ["Váldhíd" 1]]
    ["Nyugat Magyarország" ["Sopron" 147]
                           ["Ruszt" 35]
                           ["Kőszeg" 27]
                           ["Németújvár" 11]
                           ["Kismarton" 5]
                           ["Kishöflány" 4]
                           ["Nezsidér" 4]
                           ["Lajtapordány" 3]
                           ["Pottendorff" 3]
                           ["Kabold" 2]
                           ["Oroszvár" 2]
                           ["Somorja" 2]
                           ["Sopronnyék" 2]
                           ["Szározvám" 2]
                           ["Védeny" 2]
                           ["Zundrava" 2]
                           ["Büdöskút" 1]
                           ["Feketeváros" 1]
                           ["Felsőlászló" 1]
                           ["Fertőszéleskút" 1]
                           ["Fraknó" 1]
                           ["Hof am Leithagebirge" 1]
                           ["Királyfalva" 1]
                           ["Kisboldogasszony" 1]
                           ["Köpcsény" 1]
                           ["Lók" 1]
                           ["Loretto" 1]
                           ["Mannersdorff" 1]
                           ["Miklósfalva" 1]
                           ["Nyulas" 1]
                           ["Oszlop" 1]
                           ["Pándorfalu" 1]
                           ["Pordány" 1]
                           ["Répcesarud" 1]
                           ["Szentgyörgy" 1]
                           ["Szentmargitbánya" 1]
                           ["Tormafalu" 1]
                           ["Ujfalu" 1]
                           ["Völgyfalu" 1]]
    ["Alföld" ["Debrecen" 27]
              ["Ecseg" 6]
              ["Jobbágyi" 6]
              ["Kóka" 6]
              ["Pest" 5]
              ["Tóalmás" 5]
              ["Buják" 4]
              ["Romhány" 4]
              ["Bag" 3]
              ["Gyöngyös" 3]
              ["Kálló" 3]
              ["Pécel" 3]
              ["Szecső" 3]
              ["Úri" 3]
              ["Vác" 3]
              ["Veresegyháza" 3]
              ["Alsónémedi" 2]
              ["Boldog" 2]
              ["Eger" 2]
              ["Galgamácsa" 2]
              ["Magyarnádor" 2]
              ["Nagykáta" 2]
              ["Nógrád" 2]
              ["Nőtincs" 2]
              ["Palotás" 2]
              ["Szentlászló" 2]
              ["Szentlőrinckáta" 2]
              ["Sziget" 2]
              ["Szőlős" 2]
              ["Tereske" 2]
              ["Tiszaladány" 2]
              ["Tószeg" 2]
              ["Üllő" 2]
              ["Vácszentlászló" 2]
              ["Abony" 1]
              ["Bercel" 1]
              ["Berkesz" 1]
              ["Csongrád" 1]
              ["Dunaharaszti" 1]
              ["Dunakeszi" 1]
              ["Ecser" 1]
              ["Fegyvernek" 1]
              ["Harsány" 1]
              ["Hatvan" 1]
              ["Herencsény" 1]
              ["Hódmezővásárhely" 1]
              ["Isaszeg" 1]
              ["Kecskemét" 1]
              ["Kerepes" 1]
              ["Kosd" 1]
              ["Lőrinc" 1]
              ["Mácsa" 1]
              ["Mezőkövesd" 1]
              ["Nagykőrös" 1]
              ["Nézsa" 1]
              ["Nógrádverőce" 1]
              ["Rád" 1]
              ["Rékas" 1]
              ["Sári" 1]
              ["Sokolnitz" 1]
              ["Sűly" 1]
              ["Szente" 1]
              ["Szentmártonkáta" 1]
              ["Tápióbicske" 1]
              ["Tápiósüly" 1]
              ["Tápiószele" 1]
              ["Tarcal" 1]
              ["Tiszafüred" 1]
              ["Tokaj" 1]
              ["váci egyházmegye" 1]
              ["Verseg" 1]
              ["Vis" 1]]
    ["Dunántúl" ["Buda" 16]
                ["Esztergom" 4]
                ["Komárom" 3]
                ["Pannonhalma" 3]
                ["Sümeg" 3]
                ["Csáktornya" 2]
                ["Győr" 2]
                ["Horvátkimle" 2]
                ["Pécs" 2]
                ["Sárvár" 2]
                ["Zsámbok" 2]
                ["Bezenye" 1]
                ["Csanak" 1]
                ["Dömölk" 1]
                ["Felsőiszkáz" 1]
                ["Fertőszentmiklós" 1]
                ["Hévíz" 1]
                ["Komárom megye" 1]
                ["Körmend" 1]
                ["Lébény" 1]
                ["Magyaróvár" 1]
                ["Mosonmagyaróvár" 1]
                ["Mosonszolnok" 1]
                ["Nagykanizsa" 1]
                ["Nagylödő" 1]
                ["Noszlop" 1]
                ["Páli" 1]
                ["Somogyvár" 1]
                ["Tömörd" 1]
                ["Veszprém" 1]
                ["Vizkelet" 1]
                ["Vízvár" 1]]
    ["Partium" ["Nagybánya" 12]
               ["Szatmár" 6]
               ["Várad" 2]
               ["Kaplony" 1]
               ["Nagyvárad" 1]
               ["Zilah" 1]]
    ["Ausztria" ["Bécs" 8]
                ["Grác" 2]
                ["Bécsújhely" 1]]
    ["Németország" ["Ulm" 4]
                   ["Drezda" 2]
                   ["Heidelberg" 2]
                   ["Northeim" 1]
                   ["Tübingen" 1]
                   ["Wittenberg" 1]]
    ["Olaszország" ["Padova" 6]
                   ["Bologna" 1]]
    ["Hollandia" ["Franeker" 4]
                 ["Amsterdam" 1]
                 ["Utrecht" 1]]
    ["Lengyelország" ["Boroszló" 3]
                     ["Warmia" 1]]
    ["Svájc" ["Bázel" 2]]
    ["Törökország" ["Konstantinápoly" 1]
                   ["Rodostó" 1]]
    ["Csehország" ["Prága" 1]]
])


#_(ns koraujkor.charts.line)

#_(def title "Highcharts - Line chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -line []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "line"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként külön-külön"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :allowDecimals false
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :line {
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.spline)

#_(def title "Highcharts - Spline chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -spline []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "spline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként külön-külön"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :spline {
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.line-stacked)

#_(def title "Highcharts - Line chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -line-stacked []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "line"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként együttesen"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :line {
            :stacking "normal"
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.spline-stacked)

#_(def title "Highcharts - Spline chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -spline-stacked []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "spline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként együttesen"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :spline {
            :stacking "normal"
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.line-percent)

#_(def title "Highcharts - Line chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -line-percent []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "line"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként arányaiban"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(str (js* "this.value") "%")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :line {
            :stacking "percent"
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.spline-percent)

#_(def title "Highcharts - Spline chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -spline-percent []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "spline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként arányaiban"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(str (js* "this.value") "%")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :spline {
            :stacking "percent"
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.area)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -area []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "area"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként külön-külön"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :allowDecimals false
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :area {
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.area-spline)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -area-spline []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként külön-külön"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :areaspline {
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.area-stacked)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -area-stacked []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "area"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként együttesen"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :area {
            :stacking "normal"
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.area-stacked-spline)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -area-stacked-spline []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként együttesen"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :areaspline {
            :stacking "normal"
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.area-percent)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -area-percent []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "area"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként arányaiban"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(str (js* "this.value") "%")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :area {
            :stacking "percent"
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.area-percent-spline)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -area-percent-spline []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként arányaiban"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma évente"
        }
        :labels {
            :formatter #(str (js* "this.value") "%")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :areaspline {
            :stacking "percent"
            :pointStart 1533
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series jegyzékek-száma-keletkezési-hely-szerint
}))


#_(ns koraujkor.charts.spline-stacked-sum10)

#_(def title "Highcharts - Spline chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -spline-stacked-sum10 []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "spline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként együttesen évtizedenként összesítve"
    }
    :xAxis {
        :allowDecimals false
        :startOnTick true
        :endOnTick false
        :showLastLabel true
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :allowDecimals false
        :floor 0
        :title {
            :text "Jegyzékek száma évtizedenként összesítve"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :spline {
            :stacking "normal"
            :pointStart 1530
            :pointInterval 10
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series [{
        :name "Ausztria"
        :data [0, 0, 1, 0, 3, 3, 2, 3, 3, 2, 2, 7, 30, 18, 17, 10, 2, 7, 4, 2, 0, 4, 0]
    }, {
        :name "Magyarország"
        :data [1, 0, 2, 4, 2, 2, 2, 6, 8, 12, 24, 27, 50, 27, 21, 23, 15, 18, 38, 61, 30, 50, 4]
    }, {
        :name "Románia"
        :data [0, 1, 0, 1, 2, 10, 13, 11, 12, 15, 27, 24, 41, 40, 42, 45, 48, 44, 29, 31, 35, 44, 3]
    }, {
        :name "Szlovákia"
        :data [2, 0, 5, 9, 11, 17, 23, 30, 45, 26, 50, 51, 46, 28, 57, 68, 46, 21, 16, 10, 9, 6, 3]
    }]
}))


#_(ns koraujkor.charts.area-spline-stacked-sum10)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -area-spline-stacked-sum10 []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként együttesen évtizedenként összesítve"
    }
    :xAxis {
        :allowDecimals false
        :startOnTick true
        :endOnTick false
        :showLastLabel true
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :allowDecimals false
        :floor 0
        :title {
            :text "Jegyzékek száma évtizedenként összesítve"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :areaspline {
            :stacking "normal"
            :pointStart 1530
            :pointInterval 10
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series [{
        :name "Ausztria"
        :data [0, 0, 1, 0, 3, 3, 2, 3, 3, 2, 2, 7, 30, 18, 17, 10, 2, 7, 4, 2, 0, 4, 0]
    }, {
        :name "Magyarország"
        :data [1, 0, 2, 4, 2, 2, 2, 6, 8, 12, 24, 27, 50, 27, 21, 23, 15, 18, 38, 61, 30, 50, 4]
    }, {
        :name "Románia"
        :data [0, 1, 0, 1, 2, 10, 13, 11, 12, 15, 27, 24, 41, 40, 42, 45, 48, 44, 29, 31, 35, 44, 3]
    }, {
        :name "Szlovákia"
        :data [2, 0, 5, 9, 11, 17, 23, 30, 45, 26, 50, 51, 46, 28, 57, 68, 46, 21, 16, 10, 9, 6, 3]
    }]
}))


#_(ns koraujkor.charts.spline-percent-sum10)

#_(def title "Highcharts - Spline chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -spline-percent-sum10 []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "spline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként arányaiban évtizedenként összesítve"
    }
    :xAxis {
        :allowDecimals false
        :startOnTick true
        :endOnTick false
        :showLastLabel true
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :floor 0
        :title {
            :text "Jegyzékek számának százalékos aránya évtizedenként összesítve"
        }
        :labels {
            :formatter #(str (js* "this.value") "%")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :spline {
            :stacking "percent"
            :pointStart 1530
            :pointInterval 10
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series [{
        :name "Ausztria"
        :data [0, 0, 1, 0, 3, 3, 2, 3, 3, 2, 2, 7, 30, 18, 17, 10, 2, 7, 4, 2, 0, 4, 0]
    }, {
        :name "Magyarország"
        :data [1, 0, 2, 4, 2, 2, 2, 6, 8, 12, 24, 27, 50, 27, 21, 23, 15, 18, 38, 61, 30, 50, 4]
    }, {
        :name "Románia"
        :data [0, 1, 0, 1, 2, 10, 13, 11, 12, 15, 27, 24, 41, 40, 42, 45, 48, 44, 29, 31, 35, 44, 3]
    }, {
        :name "Szlovákia"
        :data [2, 0, 5, 9, 11, 17, 23, 30, 45, 26, 50, 51, 46, 28, 57, 68, 46, 21, 16, 10, 9, 6, 3]
    }]
}))


#_(ns koraujkor.charts.area-spline-percent-sum10)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -area-spline-percent-sum10 []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "x"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként arányaiban évtizedenként összesítve"
    }
    :xAxis {
        :allowDecimals false
        :startOnTick true
        :endOnTick false
        :showLastLabel true
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :floor 0
        :title {
            :text "Jegyzékek számának százalékos aránya évtizedenként összesítve"
        }
        :labels {
            :formatter #(str (js* "this.value") "%")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :areaspline {
            :stacking "percent"
            :pointStart 1530
            :pointInterval 10
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series [{
        :name "Ausztria"
        :data [0, 0, 1, 0, 3, 3, 2, 3, 3, 2, 2, 7, 30, 18, 17, 10, 2, 7, 4, 2, 0, 4, 0]
    }, {
        :name "Magyarország"
        :data [1, 0, 2, 4, 2, 2, 2, 6, 8, 12, 24, 27, 50, 27, 21, 23, 15, 18, 38, 61, 30, 50, 4]
    }, {
        :name "Románia"
        :data [0, 1, 0, 1, 2, 10, 13, 11, 12, 15, 27, 24, 41, 40, 42, 45, 48, 44, 29, 31, 35, 44, 3]
    }, {
        :name "Szlovákia"
        :data [2, 0, 5, 9, 11, 17, 23, 30, 45, 26, 50, 51, 46, 28, 57, 68, 46, 21, 16, 10, 9, 6, 3]
    }]
}))


#_(ns koraujkor.charts.pie)

#_(def title "Highcharts - Pie chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :pie {
            :allowPointSelect true
            :cursor "pointer"
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :type "pie"
        :name "részesedése"
        :data [
            ["Ausztria" 120]
            ["Csehország" 1]
            ["Hollandia" 6]
            ["Horvátország" 4]
            ["Lengyelország" 3]
            {
                :name "Magyarország"
                :y 427
                :sliced true
                :selected true
            }
            ["Németország" 10]
            ["Olaszország" 7]
            ["Románia" 518]
            ["Svájc" 2]
            ["Szlovákia" 579]
            ["Törökország" 2]
            ["Ukrajna" 8]
        ]
    }]
}))


#_(ns koraujkor.charts.pie-3d)

#_(def title "Highcharts - Pie chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie-3d []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
        :options3d {
            :enabled true
            :alpha 50
        }
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :pie {
            :allowPointSelect true
            :cursor "pointer"
            :depth 60
            :innerSize 160
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :type "pie"
        :name "részesedése"
        :data [
            ["Ausztria" 120]
            ["Csehország" 1]
            ["Hollandia" 6]
            ["Horvátország" 4]
            ["Lengyelország" 3]
            {
                :name "Magyarország"
                :y 427
                :sliced true
                :selected true
            }
            ["Németország" 10]
            ["Olaszország" 7]
            ["Románia" 518]
            ["Svájc" 2]
            ["Szlovákia" 579]
            ["Törökország" 2]
            ["Ukrajna" 8]
        ]
    }]
}))


#_(ns koraujkor.charts.pie-drilldown)

#_(def title "Highcharts - Pie drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie-drilldown []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Kattints az országra: jegyzékek száma városok szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :series {
            :allowPointSelect true
            :cursor "pointer"
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :name "Országok"
        :colorByPoint true
        :data (psv-data psv0)
    }]
    :drilldown {
        :series (psv-series psv0)
    }
}))


#_(ns koraujkor.charts.pie-drilldown-ordered)

#_(def title "Highcharts - Pie drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie-drilldown-ordered []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Kattints az országra: jegyzékek száma városok szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :series {
            :allowPointSelect true
            :cursor "pointer"
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :name "Országok"
        :colorByPoint true
        :data (psv-data psv1)
    }]
    :drilldown {
        :series (psv-series psv1)
    }
}))


#_(ns koraujkor.charts.pie-drilldown-ordered-3d)

#_(def title "Highcharts - Pie drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie-drilldown-ordered-3d []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
        :options3d {
            :enabled true
            :alpha 50
        }
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Kattints az országra: jegyzékek száma városok szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :series {
            :allowPointSelect true
            :cursor "pointer"
            :depth 60
            :innerSize 160
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :name "Országok"
        :colorByPoint true
        :data (psv-data psv1)
    }]
    :drilldown {
        :series (psv-series psv1)
    }
}))


#_(ns koraujkor.charts.pie-drilldown-ordered-min5)

#_(def title "Highcharts - Pie drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie-drilldown-ordered-min5 []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
    }
    :title {
        :text "A jegyzékek száma (min. 5) keletkezési hely szerint"
    }
    :subtitle {
        :text "Kattints az országra: jegyzékek száma városok szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :series {
            :allowPointSelect true
            :cursor "pointer"
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :name "Országok"
        :colorByPoint true
        :data (psv-data psv2)
    }]
    :drilldown {
        :series (psv-series psv2)
    }
}))


#_(ns koraujkor.charts.pie-drilldown-ordered-min5-3d)

#_(def title "Highcharts - Pie drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie-drilldown-ordered-min5-3d []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
        :options3d {
            :enabled true
            :alpha 50
        }
    }
    :title {
        :text "A jegyzékek száma (min. 5) keletkezési hely szerint"
    }
    :subtitle {
        :text "Kattints az országra: jegyzékek száma városok szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :series {
            :allowPointSelect true
            :cursor "pointer"
            :depth 60
            :innerSize 160
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :name "Országok"
        :colorByPoint true
        :data (psv-data psv2)
    }]
    :drilldown {
        :series (psv-series psv2)
    }
}))


#_(ns koraujkor.charts.pie-donut)

#_(def title "Highcharts - Donut chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(def donut-categories ["Szlovákia", "Románia", "Magyarország", "Ausztria", "Ukrajna", "Olaszország"])

(def donut-data
[{
    :y 481
    :color (gradient2 0)
    :drilldown {
        :name "szlovák városok"
        :categories ["Besztercebánya", "Selmecbánya", "Kassa", "Lőcse", "Körmöcbánya", "Pozsony", "Eperjes", "Nagyszombat", "Garamszentbenedek", "Bártfa", "Szakolca", "Késmárk"]
        :data [104, 85, 83, 66, 37, 28, 23, 20, 17, 8, 5, 5]
        :color (gradient2 0)
    }
}, {
    :y 452
    :color (gradient2 1)
    :drilldown {
        :name "román városok"
        :categories ["Nagyszeben", "Beszterce", "Kolozsvár", "Segesvár", "Brassó", "Gyulafehérvár", "Gernyeszeg", "Nagyenyed", "Nagybánya", "Marosvásárhely", "Székelyudvarhely", "Fogaras", "Szeben"]
        :data [128, 93, 53, 50, 34, 17, 17, 12, 12, 12, 10, 8, 6]
        :color (gradient2 1)
    }
}, {
    :y 272
    :color (gradient2 2)
    :drilldown {
        :name "magyar városok"
        :categories ["Sopron", "Kőszeg", "Debrecen", "Sárospatak", "Buda", "Szatmár", "Kóka", "Jobbágyi", "Ecseg", "Tóalmás", "Pest"]
        :data [147, 27, 27, 21, 16, 6, 6, 6, 6, 5, 5]
        :color (gradient2 2)
    }
}, {
    :y 59
    :color (gradient2 3)
    :drilldown {
        :name "osztrák városok"
        :categories ["Ruszt", "Németújvár", "Bécs", "Kismarton"]
        :data [35, 11, 8, 5]
        :color (gradient2 3)
    }
}, {
    :y 7
    :color (gradient2 4)
    :drilldown {
        :name "ukrán város"
        :categories ["Ungvár"]
        :data [7]
        :color (gradient2 4)
    }
}, {
    :y 6
    :color (gradient2 5)
    :drilldown {
        :name "olasz város"
        :categories ["Padova"]
        :data [6]
        :color (gradient2 5)
    }
}])

(defn- donut1 [categories data]
    (into [] (map #(do {:name %1 :y (:y %2) :color (:color %2)}) categories data)))

(defn- donut2 [data]
    (into [] (mapcat #(let [c (:color %) d (:drilldown %) l (-> d :data count)]
        (for [j (range l)] {:name (nth (:categories d) j) :y (nth (:data d) j) :color (brighten' c (- 0.2 (/ (/ j l) 5)))})) data)))

(defn -pie-donut []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
    }
    :title {
        :text "A jegyzékek száma (min. 5) keletkezési hely szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :pie {
            :allowPointSelect true
            :cursor "pointer"
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
            :shadow true
            :center ["50%", "50%"]
        }
    }
    :colors gradient2
    :series [{
        :name "részesedése"
        :data (donut1 donut-categories donut-data)
        :size "60%"
        :dataLabels {
            :formatter #(when (< 5 (js* "this.y")) (js* "this.point.name"))
            :color "white"
            :distance -100
        }
    }, {
        :name "részesedése"
        :data (donut2 donut-data)
        :size "80%"
        :innerSize "60%"
        :dataLabels {
            :formatter #(when (< 1 (js* "this.y")) (str "<b>" (js* "this.point.name") ":</b> " (js* "this.y")))
        }
    }]
}))


#_(ns koraujkor.charts.column-drilldown-ordered-min5)

#_(def title "Highcharts - Column drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -column-drilldown-ordered-min5 []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "column"
    }
    :title {
        :text "A jegyzékek száma (min. 5) keletkezési hely szerint"
    }
    :subtitle {
        :text "Kattints az országra: jegyzékek száma városok szerint"
    }
    :xAxis {
        :type "category"
        :labels {
            :rotation 0
            :style {
                :fontSize "14px"
            }
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma"
        }
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.y} db</b>"
    }
    :plotOptions {
        :series {
            :allowPointSelect true
            :cursor "pointer"
        }
    }
    :colors gradient2
    :series [{
        :name "jegyzékek száma"
        :colorByPoint true
        :data (psv-data psv2)
        :dataLabels {
            :enabled true
            :y -10
            :style {
                :fontSize "14px"
            }
        }
    }]
    :drilldown {
        :series (psv-series psv2)
    }
}))


#_(ns koraujkor.charts.column-drilldown-ordered-min5-3d)

#_(def title "Highcharts - Column drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -column-drilldown-ordered-min5-3d []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "column"
        :options3d {
            :enabled true
            :alpha 20
            :beta 10
        }
    }
    :title {
        :text "A jegyzékek száma (min. 5) keletkezési hely szerint"
    }
    :subtitle {
        :text "Kattints az országra: jegyzékek száma városok szerint"
    }
    :xAxis {
        :type "category"
        :labels {
            :rotation 0
            :style {
                :fontSize "14px"
            }
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma"
        }
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.y} db</b>"
    }
    :plotOptions {
        :series {
            :allowPointSelect true
            :cursor "pointer"
            :depth 50
        }
    }
    :colors gradient2
    :series [{
        :name "jegyzékek száma"
        :colorByPoint true
        :data (psv-data psv2)
        :dataLabels {
            :enabled true
            :y -20
            :style {
                :fontSize "14px"
            }
        }
    }]
    :drilldown {
        :series (psv-series psv2)
    }
}))


#_(ns koraujkor.charts.column-drilldown-ordered-min5-3d-slider)

#_(def title "Highcharts - Column drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -column-drilldown-ordered-min5-3d-slider []
    (let [container "column-drilldown-ordered-min5-3d-slider-container"
          model {
                    :chart {
                        :renderTo container
                        :type "column"
                        :marginTop 100
                        :options3d {
                            :enabled true
                            :alpha 20
                            :beta 10
                        }
                    }
                    :title {
                        :text "A jegyzékek száma (min. 5) keletkezési hely szerint"
                    }
                    :subtitle {
                        :text "Kattints az országra: jegyzékek száma városok szerint"
                    }
                    :xAxis {
                        :type "category"
                        :labels {
                            :rotation 0
                            :style {
                                :fontSize "14px"
                            }
                        }
                    }
                    :yAxis {
                        :title {
                            :text "Jegyzékek száma"
                        }
                    }
                    :tooltip {
                        :pointFormat "{series.name}: <b>{point.y} db</b>"
                    }
                    :legend {
                        :enabled false
                    }
                    :plotOptions {
                        :series {
                            :allowPointSelect true
                            :cursor "pointer"
                            :depth 50
                        }
                    }
                    :colors gradient2
                    :series [{
                        :name "jegyzékek száma"
                        :colorByPoint true
                        :data (psv-data psv2)
                        :dataLabels {
                            :enabled true
                            :y -20
                            :style {
                                :fontSize "14px"
                            }
                        }
                    }]
                    :drilldown {
                        :series (psv-series psv2)
                    }
                }]
        (reagent/create-class {
            :reagent-render
                #(do [:div
                    [:div {:id container :style {:min-width "300px" :height "690px" :max-width "900px" :margin "0 auto"}}]
                    [:table
                        [:tr [:td "alpha"] [:td [:input#R0 {:type "range" :min -45 :max 45 :defaultValue 20}] [:span#R0-value.value]]]
                        [:tr [:td "beta"]  [:td [:input#R1 {:type "range" :min -45 :max 45 :defaultValue 10}] [:span#R1-value.value]]]]])
            :component-did-mount
                #(let [chart (new js/Highcharts.Chart (clj->js (merge -model model)))
                       options3d (.. chart -options -chart -options3d)
                       showValues (fn []
                            (.html (js/$ "#R0-value") (.-alpha options3d))
                            (.html (js/$ "#R1-value") (.-beta options3d)))]
                    (.on (js/$ "#R0") "change" (fn []
                        (set! (.-alpha options3d) (js* "this.value"))
                        (showValues)
                        (.redraw chart false)))
                    (.on (js/$ "#R1") "change" (fn []
                        (set! (.-beta options3d) (js* "this.value"))
                        (showValues)
                        (.redraw chart false)))
                    (showValues))})))


#_(ns koraujkor.charts.column-drilldown-inverted)

#_(def title "Highcharts - Column drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -column-drilldown-inverted []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "column"
        :inverted true
    }
    :title {
        :text "A jegyzékek száma (min. 3) keletkezési hely szerint"
    }
    :subtitle {
        :text "Kattints az országra: jegyzékek száma városok szerint"
    }
    :xAxis {
        :type "category"
        :labels {
            :rotation 0
            :style {
                :fontSize "14px"
            }
        }
    }
    :yAxis {
        :title {
            :text "Jegyzékek száma"
        }
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.y} db</b>"
    }
    :plotOptions {
        :series {
            :allowPointSelect true
            :cursor "pointer"
        }
    }
    :colors gradient2
    :series [{
        :name "jegyzékek száma"
        :colorByPoint true
        :data (psv-data psv3)
        :dataLabels {
            :enabled true
            :y -10
            :style {
                :fontSize "14px"
            }
        }
    }]
    :drilldown {
        :series (psv-series psv3)
    }
}))


#_(ns koraujkor.charts.column-stacked-3d)

#_(def title "Highcharts - Column chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -column-stacked-3d []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "column"
        :options3d {
            :enabled true
            :alpha 15
            :beta 15
            :depth 150
        }
        :marginTop 90
    }
    :title {
        :text "A jegyzékek száma évszázadonként"
    }
    :subtitle {
        :text "A keletkezési ország szerint rétegezve"
    }
    :legend {
        :layout "vertical"
        :align "left"
        :verticalAlign "top"
        :x 100
        :y 70
        :floating true
        :backgroundColor "white"
        :borderWidth 1
    }
    :xAxis {
        :categories ["XVI. század", "XVII. század", "XVIII. század"]
        :labels {
            :style {
                :fontSize "13px"
            }
        }
    }
    :yAxis {
        :allowDecimals false
        :title {
            :text "Jegyzékek száma"
        }
    }
    :tooltip {
        :headerFormat "<b>{point.key}</b><br/>"
        :pointFormat "{series.name}: {point.y} / {point.stackTotal}"
    }
    :plotOptions {
        :column {
            :stacking "normal"
            :depth 120
        }
    }
    :colors gradient2
    :series [{
        :name "Ausztria"
        :data [9, 96, 15]
    }, {
        :name "Csehország"
        :data [nil, 1, nil]
    }, {
        :name "Hollandia"
        :data [nil, 4, 2]
    }, {
        :name "Horvátország"
        :data [nil, 4, nil]
    }, {
        :name "Lengyelország"
        :data [nil, nil, 3]
    }, {
        :name "Magyarország"
        :data [13, 214, 200]
    }, {
        :name "Németország"
        :data [2, 6, 2]
    }, {
        :name "Olaszország"
        :data [2, 5, nil]
    }, {
        :name "Románia"
        :data [31, 308, 179]
    }, {
        :name "Svájc"
        :data [nil, 2, nil]
    }, {
        :name "Szlovákia"
        :data [75, 447, 57]
    }, {
        :name "Törökország"
        :data [nil, nil, 2]
    }, {
        :name "Ukrajna"
        :data [nil, nil, 8]
    }]
}))


#_(ns koraujkor.charts.column-stacked-3d-verso)

#_(def title "Highcharts - Column chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/exporting.js"])

;       ; Add mouse events for rotation
;       $(chart.column-stacked-3d-verso-container).bind("mousedown.hc touchstart.hc", function (e) {
;           e = chart.pointer.normalize(e)

;           var posX = e.pageX
;               posY = e.pageY
;               alpha = chart.options.chart.options3d.alpha
;               beta = chart.options.chart.options3d.beta
;               newAlpha
;               newBeta
;               sensitivity = 5 ; lower is more sensitive

;           $(document).bind({
;               "mousemove.hc touchdrag.hc": function (e) {
;                   ; Run beta
;                   newBeta = beta + (posX - e.pageX) / sensitivity
;                   newBeta = Math.min(100, Math.max(-100, newBeta))
;                   chart.options.chart.options3d.beta = newBeta

;                   ; Run alpha
;                   newAlpha = alpha + (e.pageY - posY) / sensitivity
;                   newAlpha = Math.min(100, Math.max(-100, newAlpha))
;                   chart.options.chart.options3d.alpha = newAlpha

;                   chart.redraw(false)
;               }
;               "mouseup touchend": function () {
;                   $(document).unbind(".hc")
;               }
;           })
;       })

(defn -column-stacked-3d-verso []
    (chart {:min-width "300px" :height "740px" :max-width "900px" :margin "0 auto"}
{
    :chart {
;               :renderTo "column-stacked-3d-verso-container"
        :type "column"
        :options3d {
            :enabled true
            :alpha 15
            :beta 15
            :depth 90
        }
        :marginTop 90
    }
    :title {
        :text "A jegyzékek száma keletkezési ország szerint"
    }
    :subtitle {
        :text "Évszázadonként rétegezve (forgatható grafikon)"
    }
    :legend {
        :layout "vertical"
        :align "left"
        :verticalAlign "top"
        :x 100
        :y 70
        :floating true
        :backgroundColor "white"
        :borderWidth 1
    }
    :xAxis {
        :categories ["Ausztria", "Csehország", "Hollandia", "Horvátország", "Lengyelország", "Magyarország", "Németország", "Olaszország", "Románia", "Svájc", "Szlovákia", "Törökország", "Ukrajna"]
        :labels {
            :rotation -30
            :style {
                :fontSize "13px"
            }
        }
    }
    :yAxis {
        :allowDecimals false
        :title {
            :text "Jegyzékek száma"
        }
    }
    :tooltip {
        :headerFormat "<b>{point.key}</b><br/>"
        :pointFormat "{series.name}: {point.y} / {point.stackTotal}"
    }
    :plotOptions {
        :column {
            :stacking "normal"
            :depth 60
        }
    }
    :colors gradient2
    :series [{
        :name "XVIII. század"
        :data [15, nil, 2, nil, 3, 200, 2, nil, 179, nil, 57, 2, 8]
    }, {
        :name "XVII. század"
        :data [96, 1, 4, 4, nil, 214, 6, 5, 308, 2, 447, nil, nil]
    }, {
        :name "XVI. század"
        :data [9, nil, nil, nil, nil, 13, 2, 2, 31, nil, 75, nil, nil]
    }]
}))


#_(ns koraujkor.charts.scatter)

#_(def title "Highcharts - Scatter chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -scatter []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "scatter"
        :zoomType "xy"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként külön-külön (nagyítható minden irányban)"
    }
    :xAxis {
        :allowDecimals false
        :startOnTick true
        :endOnTick true
        :showLastLabel true
    }
    :yAxis {
        :allowDecimals false
        :floor 0
        :title {
            :text "Jegyzékek száma évente"
        }
    }
    :legend {
        :layout "vertical"
        :align "left"
        :verticalAlign "top"
        :x 100
        :y 70
        :floating true
        :backgroundColor "white"
        :borderWidth 1
    }
    :plotOptions {
        :scatter {
            :marker {
                :radius 5
                :states {
                    :hover {
                        :enabled true
                        :lineColor "rgb(100,100,100)"
                    }
                }
            }
            :states {
                :hover {
                    :marker {
                        :enabled false
                    }
                }
            }
            :tooltip {
                :headerFormat "<b>{series.name}</b><br/>"
                :pointFormat "{point.x}: {point.y} db"
            }
        }
    }
    :colors gradient3
    :series [{
        :name "Ausztria"
        :data [[1552, 1], [1570, 1], [1571, 1], [1578, 1], [1583, 1], [1587, 1], [1588, 1], [1592, 1], [1599, 1], [1604, 1]
               [1606, 1], [1608, 1], [1614, 1], [1615, 1], [1619, 1], [1627, 1], [1628, 1], [1635, 1], [1639, 1], [1641, 1]
               [1642, 1], [1644, 3], [1645, 1], [1648, 1], [1651, 5], [1654, 1], [1655, 2], [1656, 1], [1657, 1], [1659, 20]
               [1662, 2], [1663, 4], [1664, 1], [1665, 1], [1666, 3], [1667, 2], [1668, 3], [1669, 2], [1670, 1], [1672, 3]
               [1674, 8], [1677, 1], [1678, 3], [1679, 1], [1680, 2], [1682, 1], [1683, 2], [1684, 4], [1686, 1], [1696, 1]
               [1697, 1], [1700, 2], [1701, 1], [1705, 2], [1706, 2], [1713, 2], [1717, 1], [1718, 1], [1721, 1], [1725, 1]
               [1741, 1], [1742, 1], [1743, 1], [1744, 1]]
    }, {
        :name "Csehország"
        :data [[1666, 1]]
    }, {
        :name "Hollandia"
        :data [[1661, 1], [1680, 2], [1694, 1], [1713, 1], [1735, 1]]
    }, {
        :name "Horvátország"
        :data [[1659, 1], [1662, 1], [1665, 1], [1671, 1]]
    }, {
        :name "Lengyelország"
        :data [[1719, 2], [1721, 1]]
    }, {
        :name "Magyarország"
        :data [[1535, 1], [1553, 1], [1556, 1], [1560, 1], [1565, 2], [1567, 1], [1571, 1], [1575, 1], [1580, 1], [1587, 1]
               [1590, 1], [1594, 1], [1601, 3], [1603, 1], [1608, 1], [1609, 1], [1613, 1], [1614, 1], [1616, 2], [1617, 1]
               [1618, 1], [1619, 2], [1621, 2], [1622, 1], [1623, 3], [1626, 1], [1627, 2], [1628, 1], [1629, 2], [1630, 1]
               [1631, 3], [1632, 1], [1633, 2], [1634, 5], [1635, 4], [1637, 5], [1638, 1], [1639, 2], [1640, 4], [1642, 1]
               [1644, 2], [1645, 5], [1646, 5], [1647, 1], [1648, 4], [1649, 5], [1650, 7], [1651, 3], [1652, 5], [1653, 4]
               [1654, 3], [1655, 5], [1656, 1], [1657, 10], [1658, 3], [1659, 9], [1660, 4], [1661, 1], [1662, 2], [1663, 1]
               [1664, 5], [1665, 5], [1666, 3], [1667, 2], [1668, 1], [1669, 3], [1670, 2], [1671, 1], [1672, 2], [1673, 1]
               [1674, 5], [1675, 1], [1676, 2], [1677, 2], [1678, 2], [1679, 3], [1680, 7], [1681, 1], [1683, 4], [1684, 2]
               [1685, 1], [1686, 4], [1687, 1], [1688, 3], [1690, 1], [1693, 2], [1694, 2], [1695, 1], [1696, 1], [1697, 3]
               [1698, 1], [1699, 4], [1700, 1], [1701, 1], [1703, 2], [1704, 4], [1705, 3], [1706, 3], [1707, 2], [1709, 2]
               [1710, 3], [1711, 6], [1712, 2], [1713, 1], [1714, 1], [1715, 5], [1716, 6], [1717, 3], [1718, 8], [1719, 3]
               [1720, 7], [1721, 2], [1722, 1], [1723, 2], [1724, 4], [1725, 9], [1726, 5], [1727, 23], [1728, 5], [1729, 3]
               [1730, 2], [1731, 1], [1732, 2], [1733, 5], [1734, 5], [1735, 3], [1736, 2], [1737, 3], [1738, 5], [1739, 2]
               [1740, 4], [1741, 1], [1742, 1], [1743, 3], [1744, 17], [1745, 6], [1746, 15], [1747, 1], [1749, 2], [1750, 4]]
    }, {
        :name "Németország"
        :data [[1565, 1], [1590, 1], [1615, 1], [1617, 1], [1661, 1], [1665, 2], [1666, 1], [1734, 1], [1736, 1]]
    }, {
        :name "Olaszország"
        :data [[1535, 1], [1577, 1], [1626, 1], [1640, 1], [1655, 1], [1667, 1], [1686, 1]]
    }, {
        :name "Románia"
        :data [[1541, 1], [1563, 1], [1575, 1], [1577, 1], [1580, 1], [1582, 1], [1583, 2], [1584, 1], [1585, 1], [1586, 1]
               [1588, 1], [1589, 2], [1590, 3], [1591, 2], [1592, 3], [1593, 1], [1594, 2], [1596, 2], [1600, 4], [1603, 2]
               [1604, 2], [1608, 2], [1609, 1], [1611, 3], [1612, 2], [1614, 1], [1615, 1], [1616, 1], [1618, 2], [1619, 2]
               [1622, 1], [1624, 2], [1625, 3], [1626, 4], [1627, 1], [1628, 1], [1629, 3], [1630, 3], [1631, 1], [1632, 1]
               [1633, 1], [1634, 4], [1635, 6], [1636, 2], [1637, 6], [1638, 2], [1639, 1], [1640, 3], [1641, 1], [1642, 1]
               [1645, 1], [1646, 6], [1647, 8], [1648, 1], [1649, 3], [1650, 9], [1651, 2], [1652, 4], [1653, 7], [1654, 4]
               [1655, 3], [1656, 2], [1657, 4], [1658, 3], [1659, 3], [1660, 2], [1661, 1], [1662, 14], [1663, 3], [1664, 2]
               [1665, 3], [1666, 1], [1667, 7], [1668, 3], [1669, 4], [1670, 3], [1671, 2], [1672, 3], [1673, 7], [1674, 5]
               [1675, 3], [1676, 6], [1677, 6], [1678, 3], [1679, 4], [1680, 9], [1681, 3], [1682, 2], [1683, 7], [1684, 2]
               [1685, 6], [1686, 1], [1687, 1], [1688, 7], [1689, 7], [1690, 7], [1691, 2], [1692, 3], [1693, 2], [1694, 2]
               [1695, 13], [1696, 3], [1697, 2], [1698, 7], [1699, 7], [1700, 7], [1701, 4], [1702, 4], [1703, 6], [1704, 7]
               [1705, 4], [1706, 2], [1707, 4], [1708, 1], [1709, 5], [1710, 5], [1711, 3], [1713, 2], [1714, 5], [1716, 5]
               [1717, 3], [1719, 6], [1720, 3], [1721, 6], [1722, 2], [1723, 2], [1724, 3], [1725, 1], [1726, 3], [1727, 5]
               [1729, 6], [1730, 4], [1732, 6], [1733, 1], [1734, 3], [1735, 5], [1736, 3], [1737, 8], [1738, 2], [1739, 3]
               [1740, 9], [1741, 2], [1742, 5], [1743, 4], [1745, 2], [1746, 5], [1747, 6], [1748, 1], [1749, 10], [1750, 3]]
    }, {
        :name "Svájc"
        :data [[1672, 1], [1674, 1]]
    }, {
        :name "Szlovákia"
        :data [[1533, 2], [1551, 1], [1552, 1], [1553, 1], [1556, 1], [1558, 1], [1561, 1], [1562, 2], [1564, 1], [1566, 3]
               [1567, 1], [1569, 1], [1570, 2], [1571, 1], [1573, 1], [1574, 1], [1575, 1], [1576, 1], [1577, 2], [1579, 2]
               [1580, 3], [1581, 1], [1582, 1], [1583, 1], [1584, 1], [1585, 2], [1586, 1], [1587, 2], [1588, 1], [1589, 4]
               [1590, 1], [1591, 2], [1592, 1], [1593, 1], [1594, 4], [1595, 3], [1597, 5], [1598, 6], [1600, 8], [1601, 3]
               [1602, 3], [1603, 2], [1604, 3], [1605, 1], [1606, 3], [1607, 3], [1608, 2], [1609, 2], [1610, 6], [1611, 1]
               [1612, 2], [1613, 10], [1614, 5], [1615, 3], [1616, 8], [1617, 3], [1618, 4], [1619, 3], [1620, 1], [1622, 5]
               [1623, 1], [1624, 2], [1625, 1], [1626, 9], [1627, 3], [1628, 2], [1629, 2], [1630, 5], [1631, 6], [1632, 7]
               [1633, 4], [1634, 5], [1635, 6], [1636, 8], [1637, 3], [1638, 2], [1639, 4], [1640, 7], [1641, 4], [1642, 3]
               [1643, 1], [1644, 3], [1645, 6], [1646, 7], [1647, 13], [1648, 4], [1649, 3], [1650, 3], [1651, 4], [1652, 3]
               [1653, 1], [1654, 7], [1655, 3], [1656, 4], [1657, 14], [1658, 4], [1659, 3], [1660, 2], [1661, 1], [1662, 5]
               [1663, 3], [1664, 3], [1665, 7], [1666, 2], [1667, 3], [1668, 2], [1670, 5], [1671, 2], [1672, 5], [1673, 4]
               [1674, 12], [1675, 4], [1676, 8], [1677, 8], [1678, 3], [1679, 6], [1680, 16], [1681, 4], [1682, 4], [1683, 8]
               [1684, 8], [1685, 7], [1686, 6], [1687, 5], [1688, 5], [1689, 5], [1690, 5], [1691, 4], [1692, 7], [1693, 4]
               [1694, 7], [1695, 5], [1696, 1], [1697, 4], [1698, 6], [1699, 3], [1700, 8], [1701, 2], [1702, 1], [1704, 2]
               [1705, 3], [1706, 4], [1708, 1], [1710, 2], [1711, 5], [1712, 2], [1714, 4], [1715, 1], [1717, 1], [1718, 1]
               [1720, 1], [1722, 1], [1724, 1], [1725, 3], [1729, 4], [1730, 1], [1731, 3], [1732, 2], [1733, 1], [1735, 1]
               [1739, 1], [1743, 2], [1748, 1], [1749, 3], [1750, 2], [1754, 1]]
    }, {
        :name "Törökország"
        :data [[1708, 1], [1736, 1]]
    }, {
        :name "Ukrajna"
        :data [[1701, 1], [1704, 1], [1707, 6]]
    }]
}))


#_(ns koraujkor.charts.scatter-3d)

#_(def title "Highcharts - Scatter chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/exporting.js"])

;       ; Add mouse events for rotation
;       $(chart.scatter-3d-container).bind("mousedown.hc touchstart.hc", function (e) {
;           e = chart.pointer.normalize(e)

;           var posX = e.pageX
;               posY = e.pageY
;               alpha = chart.options.chart.options3d.alpha
;               beta = chart.options.chart.options3d.beta
;               newAlpha
;               newBeta
;               sensitivity = 5 ; lower is more sensitive

;           $(document).bind({
;               "mousemove.hc touchdrag.hc": function (e) {
;                   ; Run beta
;                   newBeta = beta + (posX - e.pageX) / sensitivity
;                   newBeta = Math.min(100, Math.max(-100, newBeta))
;                   chart.options.chart.options3d.beta = newBeta

;                   ; Run alpha
;                   newAlpha = alpha + (e.pageY - posY) / sensitivity
;                   newAlpha = Math.min(100, Math.max(-100, newAlpha))
;                   chart.options.chart.options3d.alpha = newAlpha

;                   chart.redraw(false)
;               }
;               "mouseup touchend": function () {
;                   $(document).unbind(".hc")
;               }
;           })
;       })

(defn -scatter-3d []
    (chart {:min-width "300px" :height "730px" :max-width "900px" :margin "0 auto"}
{
    :chart {
;               :renderTo "scatter-3d-container"
        :marginTop 80
        :type "scatter"
        :options3d {
            :enabled true
            :alpha 0
            :beta 0
            :depth 300
            :viewDistance 10
            :frame {
                :bottom { :size 1 :color "rgba(0,0,0,0.02)" }
                :back { :size 1 :color "rgba(0,0,0,0.04)" }
                :side { :size 1 :color "rgba(0,0,0,0.06)" }
            }
        }
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Országonként külön-külön (forgatható grafikon)"
    }
    :xAxis {
        :allowDecimals false
        :startOnTick true
        :endOnTick true
        :showLastLabel true
    }
    :yAxis {
        :allowDecimals false
        :floor 0
        :title {
            :text "Jegyzékek száma évente"
        }
    }
    :zAxis {
        :min 0
        :max 12
    }
    :legend {
        :enabled true
        :layout "vertical"
        :align "left"
        :verticalAlign "top"
        :x 120
        :y 100
        :floating true
        :backgroundColor "white"
        :borderWidth 1
    }
    :plotOptions {
        :scatter {
            :marker {
                :symbol "circle"
                :radius 5
                :states {
                    :hover {
                        :enabled true
                        :lineColor "rgb(100,100,100)"
                    }
                }
            }
            :states {
                :hover {
                    :marker {
                        :enabled false
                    }
                }
            }
            :tooltip {
                :headerFormat "<b>{series.name}</b><br/>"
                :pointFormat "{point.x}: {point.y} db"
            }
        }
    }
    :colors gradient3
    :series [{
        :name "Ausztria"
        :data [[1552, 1, 0], [1570, 1, 0], [1571, 1, 0], [1578, 1, 0], [1583, 1, 0], [1587, 1, 0], [1588, 1, 0], [1592, 1, 0]
               [1599, 1, 0], [1604, 1, 0], [1606, 1, 0], [1608, 1, 0], [1614, 1, 0], [1615, 1, 0], [1619, 1, 0], [1627, 1, 0]
               [1628, 1, 0], [1635, 1, 0], [1639, 1, 0], [1641, 1, 0], [1642, 1, 0], [1644, 3, 0], [1645, 1, 0], [1648, 1, 0]
               [1651, 5, 0], [1654, 1, 0], [1655, 2, 0], [1656, 1, 0], [1657, 1, 0], [1659, 20, 0], [1662, 2, 0], [1663, 4, 0]
               [1664, 1, 0], [1665, 1, 0], [1666, 3, 0], [1667, 2, 0], [1668, 3, 0], [1669, 2, 0], [1670, 1, 0], [1672, 3, 0]
               [1674, 8, 0], [1677, 1, 0], [1678, 3, 0], [1679, 1, 0], [1680, 2, 0], [1682, 1, 0], [1683, 2, 0], [1684, 4, 0]
               [1686, 1, 0], [1696, 1, 0], [1697, 1, 0], [1700, 2, 0], [1701, 1, 0], [1705, 2, 0], [1706, 2, 0], [1713, 2, 0]
               [1717, 1, 0], [1718, 1, 0], [1721, 1, 0], [1725, 1, 0], [1741, 1, 0], [1742, 1, 0], [1743, 1, 0], [1744, 1, 0]]
    }, {
        :name "Csehország"
        :data [[1666, 1, 1]]
    }, {
        :name "Hollandia"
        :data [[1661, 1, 2], [1680, 2, 2], [1694, 1, 2], [1713, 1, 2], [1735, 1, 2]]
    }, {
        :name "Horvátország"
        :data [[1659, 1, 3], [1662, 1, 3], [1665, 1, 3], [1671, 1, 3]]
    }, {
        :name "Lengyelország"
        :data [[1719, 2, 4], [1721, 1, 4]]
    }, {
        :name "Magyarország"
        :data [[1535, 1, 5], [1553, 1, 5], [1556, 1, 5], [1560, 1, 5], [1565, 2, 5], [1567, 1, 5], [1571, 1, 5], [1575, 1, 5]
               [1580, 1, 5], [1587, 1, 5], [1590, 1, 5], [1594, 1, 5], [1601, 3, 5], [1603, 1, 5], [1608, 1, 5], [1609, 1, 5]
               [1613, 1, 5], [1614, 1, 5], [1616, 2, 5], [1617, 1, 5], [1618, 1, 5], [1619, 2, 5], [1621, 2, 5], [1622, 1, 5]
               [1623, 3, 5], [1626, 1, 5], [1627, 2, 5], [1628, 1, 5], [1629, 2, 5], [1630, 1, 5], [1631, 3, 5], [1632, 1, 5]
               [1633, 2, 5], [1634, 5, 5], [1635, 4, 5], [1637, 5, 5], [1638, 1, 5], [1639, 2, 5], [1640, 4, 5], [1642, 1, 5]
               [1644, 2, 5], [1645, 5, 5], [1646, 5, 5], [1647, 1, 5], [1648, 4, 5], [1649, 5, 5], [1650, 7, 5], [1651, 3, 5]
               [1652, 5, 5], [1653, 4, 5], [1654, 3, 5], [1655, 5, 5], [1656, 1, 5], [1657, 10, 5], [1658, 3, 5], [1659, 9, 5]
               [1660, 4, 5], [1661, 1, 5], [1662, 2, 5], [1663, 1, 5], [1664, 5, 5], [1665, 5, 5], [1666, 3, 5], [1667, 2, 5]
               [1668, 1, 5], [1669, 3, 5], [1670, 2, 5], [1671, 1, 5], [1672, 2, 5], [1673, 1, 5], [1674, 5, 5], [1675, 1, 5]
               [1676, 2, 5], [1677, 2, 5], [1678, 2, 5], [1679, 3, 5], [1680, 7, 5], [1681, 1, 5], [1683, 4, 5], [1684, 2, 5]
               [1685, 1, 5], [1686, 4, 5], [1687, 1, 5], [1688, 3, 5], [1690, 1, 5], [1693, 2, 5], [1694, 2, 5], [1695, 1, 5]
               [1696, 1, 5], [1697, 3, 5], [1698, 1, 5], [1699, 4, 5], [1700, 1, 5], [1701, 1, 5], [1703, 2, 5], [1704, 4, 5]
               [1705, 3, 5], [1706, 3, 5], [1707, 2, 5], [1709, 2, 5], [1710, 3, 5], [1711, 6, 5], [1712, 2, 5], [1713, 1, 5]
               [1714, 1, 5], [1715, 5, 5], [1716, 6, 5], [1717, 3, 5], [1718, 8, 5], [1719, 3, 5], [1720, 7, 5], [1721, 2, 5]
               [1722, 1, 5], [1723, 2, 5], [1724, 4, 5], [1725, 9, 5], [1726, 5, 5], [1727, 23, 5], [1728, 5, 5], [1729, 3, 5]
               [1730, 2, 5], [1731, 1, 5], [1732, 2, 5], [1733, 5, 5], [1734, 5, 5], [1735, 3, 5], [1736, 2, 5], [1737, 3, 5]
               [1738, 5, 5], [1739, 2, 5], [1740, 4, 5], [1741, 1, 5], [1742, 1, 5], [1743, 3, 5], [1744, 17, 5], [1745, 6, 5]
               [1746, 15, 5], [1747, 1, 5], [1749, 2, 5], [1750, 4, 5]]
    }, {
        :name "Németország"
        :data [[1565, 1, 6], [1590, 1, 6], [1615, 1, 6], [1617, 1, 6], [1661, 1, 6], [1665, 2, 6], [1666, 1, 6], [1734, 1, 6]
               [1736, 1, 6]]
    }, {
        :name "Olaszország"
        :data [[1535, 1, 7], [1577, 1, 7], [1626, 1, 7], [1640, 1, 7], [1655, 1, 7], [1667, 1, 7], [1686, 1, 7]]
    }, {
        :name "Románia"
        :data [[1541, 1, 8], [1563, 1, 8], [1575, 1, 8], [1577, 1, 8], [1580, 1, 8], [1582, 1, 8], [1583, 2, 8], [1584, 1, 8]
               [1585, 1, 8], [1586, 1, 8], [1588, 1, 8], [1589, 2, 8], [1590, 3, 8], [1591, 2, 8], [1592, 3, 8], [1593, 1, 8]
               [1594, 2, 8], [1596, 2, 8], [1600, 4, 8], [1603, 2, 8], [1604, 2, 8], [1608, 2, 8], [1609, 1, 8], [1611, 3, 8]
               [1612, 2, 8], [1614, 1, 8], [1615, 1, 8], [1616, 1, 8], [1618, 2, 8], [1619, 2, 8], [1622, 1, 8], [1624, 2, 8]
               [1625, 3, 8], [1626, 4, 8], [1627, 1, 8], [1628, 1, 8], [1629, 3, 8], [1630, 3, 8], [1631, 1, 8], [1632, 1, 8]
               [1633, 1, 8], [1634, 4, 8], [1635, 6, 8], [1636, 2, 8], [1637, 6, 8], [1638, 2, 8], [1639, 1, 8], [1640, 3, 8]
               [1641, 1, 8], [1642, 1, 8], [1645, 1, 8], [1646, 6, 8], [1647, 8, 8], [1648, 1, 8], [1649, 3, 8], [1650, 9, 8]
               [1651, 2, 8], [1652, 4, 8], [1653, 7, 8], [1654, 4, 8], [1655, 3, 8], [1656, 2, 8], [1657, 4, 8], [1658, 3, 8]
               [1659, 3, 8], [1660, 2, 8], [1661, 1, 8], [1662, 14, 8], [1663, 3, 8], [1664, 2, 8], [1665, 3, 8], [1666, 1, 8]
               [1667, 7, 8], [1668, 3, 8], [1669, 4, 8], [1670, 3, 8], [1671, 2, 8], [1672, 3, 8], [1673, 7, 8], [1674, 5, 8]
               [1675, 3, 8], [1676, 6, 8], [1677, 6, 8], [1678, 3, 8], [1679, 4, 8], [1680, 9, 8], [1681, 3, 8], [1682, 2, 8]
               [1683, 7, 8], [1684, 2, 8], [1685, 6, 8], [1686, 1, 8], [1687, 1, 8], [1688, 7, 8], [1689, 7, 8], [1690, 7, 8]
               [1691, 2, 8], [1692, 3, 8], [1693, 2, 8], [1694, 2, 8], [1695, 13, 8], [1696, 3, 8], [1697, 2, 8], [1698, 7, 8]
               [1699, 7, 8], [1700, 7, 8], [1701, 4, 8], [1702, 4, 8], [1703, 6, 8], [1704, 7, 8], [1705, 4, 8], [1706, 2, 8]
               [1707, 4, 8], [1708, 1, 8], [1709, 5, 8], [1710, 5, 8], [1711, 3, 8], [1713, 2, 8], [1714, 5, 8], [1716, 5, 8]
               [1717, 3, 8], [1719, 6, 8], [1720, 3, 8], [1721, 6, 8], [1722, 2, 8], [1723, 2, 8], [1724, 3, 8], [1725, 1, 8]
               [1726, 3, 8], [1727, 5, 8], [1729, 6, 8], [1730, 4, 8], [1732, 6, 8], [1733, 1, 8], [1734, 3, 8], [1735, 5, 8]
               [1736, 3, 8], [1737, 8, 8], [1738, 2, 8], [1739, 3, 8], [1740, 9, 8], [1741, 2, 8], [1742, 5, 8], [1743, 4, 8]
               [1745, 2, 8], [1746, 5, 8], [1747, 6, 8], [1748, 1, 8], [1749, 10, 8], [1750, 3, 8]]
    }, {
        :name "Svájc"
        :data [[1672, 1, 9], [1674, 1, 9]]
    }, {
        :name "Szlovákia"
        :data [[1533, 2, 10], [1551, 1, 10], [1552, 1, 10], [1553, 1, 10], [1556, 1, 10], [1558, 1, 10], [1561, 1, 10], [1562, 2, 10]
               [1564, 1, 10], [1566, 3, 10], [1567, 1, 10], [1569, 1, 10], [1570, 2, 10], [1571, 1, 10], [1573, 1, 10], [1574, 1, 10]
               [1575, 1, 10], [1576, 1, 10], [1577, 2, 10], [1579, 2, 10], [1580, 3, 10], [1581, 1, 10], [1582, 1, 10], [1583, 1, 10]
               [1584, 1, 10], [1585, 2, 10], [1586, 1, 10], [1587, 2, 10], [1588, 1, 10], [1589, 4, 10], [1590, 1, 10], [1591, 2, 10]
               [1592, 1, 10], [1593, 1, 10], [1594, 4, 10], [1595, 3, 10], [1597, 5, 10], [1598, 6, 10], [1600, 8, 10], [1601, 3, 10]
               [1602, 3, 10], [1603, 2, 10], [1604, 3, 10], [1605, 1, 10], [1606, 3, 10], [1607, 3, 10], [1608, 2, 10], [1609, 2, 10]
               [1610, 6, 10], [1611, 1, 10], [1612, 2, 10], [1613, 10, 10], [1614, 5, 10], [1615, 3, 10], [1616, 8, 10], [1617, 3, 10]
               [1618, 4, 10], [1619, 3, 10], [1620, 1, 10], [1622, 5, 10], [1623, 1, 10], [1624, 2, 10], [1625, 1, 10], [1626, 9, 10]
               [1627, 3, 10], [1628, 2, 10], [1629, 2, 10], [1630, 5, 10], [1631, 6, 10], [1632, 7, 10], [1633, 4, 10], [1634, 5, 10]
               [1635, 6, 10], [1636, 8, 10], [1637, 3, 10], [1638, 2, 10], [1639, 4, 10], [1640, 7, 10], [1641, 4, 10], [1642, 3, 10]
               [1643, 1, 10], [1644, 3, 10], [1645, 6, 10], [1646, 7, 10], [1647, 13, 10], [1648, 4, 10], [1649, 3, 10], [1650, 3, 10]
               [1651, 4, 10], [1652, 3, 10], [1653, 1, 10], [1654, 7, 10], [1655, 3, 10], [1656, 4, 10], [1657, 14, 10], [1658, 4, 10]
               [1659, 3, 10], [1660, 2, 10], [1661, 1, 10], [1662, 5, 10], [1663, 3, 10], [1664, 3, 10], [1665, 7, 10], [1666, 2, 10]
               [1667, 3, 10], [1668, 2, 10], [1670, 5, 10], [1671, 2, 10], [1672, 5, 10], [1673, 4, 10], [1674, 12, 10], [1675, 4, 10]
               [1676, 8, 10], [1677, 8, 10], [1678, 3, 10], [1679, 6, 10], [1680, 16, 10], [1681, 4, 10], [1682, 4, 10], [1683, 8, 10]
               [1684, 8, 10], [1685, 7, 10], [1686, 6, 10], [1687, 5, 10], [1688, 5, 10], [1689, 5, 10], [1690, 5, 10], [1691, 4, 10]
               [1692, 7, 10], [1693, 4, 10], [1694, 7, 10], [1695, 5, 10], [1696, 1, 10], [1697, 4, 10], [1698, 6, 10], [1699, 3, 10]
               [1700, 8, 10], [1701, 2, 10], [1702, 1, 10], [1704, 2, 10], [1705, 3, 10], [1706, 4, 10], [1708, 1, 10], [1710, 2, 10]
               [1711, 5, 10], [1712, 2, 10], [1714, 4, 10], [1715, 1, 10], [1717, 1, 10], [1718, 1, 10], [1720, 1, 10], [1722, 1, 10]
               [1724, 1, 10], [1725, 3, 10], [1729, 4, 10], [1730, 1, 10], [1731, 3, 10], [1732, 2, 10], [1733, 1, 10], [1735, 1, 10]
               [1739, 1, 10], [1743, 2, 10], [1748, 1, 10], [1749, 3, 10], [1750, 2, 10], [1754, 1, 10]]
    }, {
        :name "Törökország"
        :data [[1708, 1, 11], [1736, 1, 11]]
    }, {
        :name "Ukrajna"
        :data [[1701, 1, 12], [1704, 1, 12], [1707, 6, 12]]
    }]
}))


#_(ns koraujkor.charts.bubble)

#_(def title "Highcharts - Bubble chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-more.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -bubble []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "bubble"
        :plotBorderWidth 1
        :zoomType "xy"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "A buborék mérete arányos az országok számával"
    }
    :xAxis {
        :allowDecimals false
        :startOnTick true
        :endOnTick true
        :showLastLabel true
        :gridLineWidth 1
    }
    :yAxis {
        :allowDecimals false
        :floor 0
        :title {
            :text "Jegyzékek száma évente"
        }
        :gridLineWidth 1
    }
    :legend {
        :enabled false
    }
    :plotOptions {
        :bubble {
            :minSize "2%"
            :maxSize "8%"
            :marker {
                :states {
                    :hover {
                        :enabled true
                        :lineColor "rgb(100,100,100)"
                    }
                }
            }
            :states {
                :hover {
                    :marker {
                        :enabled false
                    }
                }
            }
            :tooltip {
                :headerFormat "<b>{point.x}</b><br/>"
                :pointFormat "{point.y} jegyzék {point.z} országból"
            }
        }
    }
    :series [{
        :data [[1533  2 1] [1535  2 2] [1541  1 1] [1551  1 1] [1552  2 2] [1553  2 2] [1556  2 2]
               [1558  1 1] [1560  1 1] [1561  1 1] [1562  2 1] [1563  1 1] [1564  1 1] [1565  3 2]
               [1566  3 1] [1567  2 2] [1569  1 1] [1570  3 2] [1571  3 3] [1573  1 1] [1574  1 1]
               [1575  3 3] [1576  1 1] [1577  4 3] [1578  1 1] [1579  2 1] [1580  5 3] [1581  1 1]
               [1582  2 2] [1583  4 3] [1584  2 2] [1585  3 2] [1586  2 2] [1587  4 3] [1588  3 3]
               [1589  6 2] [1590  6 4] [1591  4 2] [1592  5 3] [1593  2 2] [1594  7 3] [1595  3 1]
               [1596  2 1] [1597  5 1] [1598  6 1] [1599  1 1] [1600 12 2] [1601  6 2] [1602  3 1]
               [1603  5 3] [1604  6 3] [1605  1 1] [1606  4 2] [1607  3 1] [1608  6 4] [1609  4 3]
               [1610  6 1] [1611  4 2] [1612  4 2] [1613 11 2] [1614  8 4] [1615  6 4] [1616 11 3]
               [1617  5 3] [1618  7 3] [1619  8 4] [1620  1 1] [1621  2 1] [1622  7 3] [1623  4 2]
               [1624  4 2] [1625  4 2] [1626 15 4] [1627  7 4] [1628  5 4] [1629  7 3] [1630  9 3]
               [1631 10 3] [1632  9 3] [1633  7 3] [1634 14 3] [1635 17 4] [1636 10 2] [1637 14 3]
               [1638  5 3] [1639  8 4] [1640 15 4] [1641  6 3] [1642  6 4] [1643  1 1] [1644  8 3]
               [1645 13 4] [1646 18 3] [1647 22 3] [1648 10 4] [1649 11 3] [1650 19 3] [1651 14 4]
               [1652 12 3] [1653 12 3] [1654 15 4] [1655 14 5] [1656  8 4] [1657 29 4] [1658 10 3]
               [1659 36 5] [1660  8 3] [1661  5 5] [1662 24 5] [1663 11 4] [1664 11 4] [1665 19 6]
               [1666 11 6] [1667 15 5] [1668  9 4] [1669  9 3] [1670 11 4] [1671  6 4] [1672 14 5]
               [1673 12 3] [1674 31 5] [1675  8 3] [1676 16 3] [1677 17 4] [1678 11 4] [1679 14 4]
               [1680 36 5] [1681  8 3] [1682  7 3] [1683 21 4] [1684 16 4] [1685 14 3] [1686 13 5]
               [1687  7 3] [1688 15 3] [1689 12 2] [1690 13 3] [1691  6 2] [1692 10 2] [1693  8 3]
               [1694 12 4] [1695 19 3] [1696  6 4] [1697 10 4] [1698 14 3] [1699 14 3] [1700 18 4]
               [1701  9 5] [1702  5 2] [1703  8 2] [1704 14 4] [1705 12 4] [1706 11 4] [1707 12 3]
               [1708  3 3] [1709  7 2] [1710 10 3] [1711 14 3] [1712  4 2] [1713  6 4] [1714 10 3]
               [1715  6 2] [1716 11 2] [1717  8 4] [1718 10 3] [1719 11 3] [1720 11 3] [1721 10 4]
               [1722  4 3] [1723  4 2] [1724  8 3] [1725 14 4] [1726  8 2] [1727 28 2] [1728  5 1]
               [1729 13 3] [1730  7 3] [1731  4 2] [1732 10 3] [1733  7 3] [1734  9 3] [1735 10 4]
               [1736  7 4] [1737 11 2] [1738  7 2] [1739  6 3] [1740 13 2] [1741  4 3] [1742  7 3]
               [1743 10 4] [1744 18 2] [1745  8 2] [1746 20 2] [1747  7 2] [1748  2 2] [1749 15 3]
               [1750  9 3] [1754  1 1]]
        :color (color 8)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 8) 0.5)]
                ]
            }
        }
    }]
}))


#_(ns koraujkor.charts.bubble-map)

#_(def title "Highcharts - Bubble chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-more.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -bubble-map []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "bubble"
        :plotBorderWidth 1
        :zoomType "xy"
        :panning true
        :panKey "shift"
    }
    :title {
        :text "A jegyzékek száma (min. 3) keletkezési hely szerint"
    }
    :subtitle {
        :text "A buborékok városok (a térkép nagyítható, görgethető)"
    }
    :xAxis {
        :title {
            :text "\u2190 nyugat \u2013 kelet \u2192"
        }
        :labels {
            :format "{value}°"
        }
        :gridLineWidth 1
    }
    :yAxis {
        :title {
            :text "\u2190 dél \u2013 észak \u2192"
        }
        :labels {
            :format "{value}°"
        }
        :gridLineWidth 1
    }
    :legend {
        :layout "vertical"
        :align "right"
        :verticalAlign "top"
        :x -60
        :y 80
        :floating true
        :backgroundColor "white"
        :borderWidth 1
    }
    :plotOptions {
        :bubble {
            :minSize "2%"
            :maxSize "8%"
            :marker {
                :states {
                    :hover {
                        :enabled true
                        :lineColor "rgb(100,100,100)"
                    }
                }
            }
            :states {
                :hover {
                    :marker {
                        :enabled false
                    }
                }
            }
            :dataLabels {
                :enabled true
                :formatter #(if (or (< (js* "this.point.x") 15) (< 50 (js* "this.point.y")) (< 20 (js* "this.point.z"))) (js* "this.point.name") "")
            }
        }
    }
    :tooltip {
        :formatter #(str "<b>" (js* "this.point.name") "</b> városából<br/>"
                         "<b>" (js* "this.point.z") "</b> jegyzéket ismerünk")
    }
    :series [{
        :name "Ausztria"
        :data [
            { :name "Bécs" :y 48.208333 :x 16.373056 :z 8 }
            { :name "Kishöflány" :y 47.840278 :x 16.5 :z 4 }
            { :name "Kismarton" :y 47.845556 :x 16.518889 :z 5 }
            { :name "Lajtapordány" :y 47.936667 :x 16.48 :z 3 }
            { :name "Németújvár" :y 47.059167 :x 16.323056 :z 11 }
            { :name "Ruszt" :y 47.802778 :x 16.672222 :z 35 }
        ]
        :color (color 0)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 0) 0.5)]
                ]
            }
        }
    }, {
        :name "Hollandia"
        :data [
            { :name "Franeker" :y 53.1875 :x 5.54 :z 4 }
        ]
        :color (color 1)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 1) 0.5)]
                ]
            }
        }
    }, {
        :name "Lengyelország"
        :data [
            { :name "Boroszló" :y 51.11 :x 17.022222 :z 3 }
        ]
        :color (color 2)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 2) 0.5)]
                ]
            }
        }
    }, {
        :name "Magyarország"
        :data [
            { :name "Bag" :y 47.63567 :x 19.48374 :z 3 }
            { :name "Buda" :y 47.501744 :x 19.033336 :z 16 }
            { :name "Buják" :y 47.88543 :x 19.54028 :z 4 }
            { :name "Debrecen" :y 47.53 :x 21.639167 :z 27 }
            { :name "Ecseg" :y 47.89653 :x 19.60615 :z 6 }
            { :name "Esztergom" :y 47.785556 :x 18.740278 :z 4 }
            { :name "Gyöngyös" :y 47.783333 :x 19.933333 :z 3 }
            { :name "Jobbágyi" :y 47.82934 :x 19.68207 :z 6 }
            { :name "Kálló" :y 47.7513 :x 19.49391 :z 3 }
            { :name "Kóka" :y 47.4865 :x 19.57883 :z 6 }
            { :name "Kőszeg" :y 47.381911 :x 16.552211 :z 27 }
            { :name "Pannonhalma" :y 47.54875 :x 17.74839 :z 3 }
            { :name "Pécel" :y 47.4905 :x 19.3409 :z 3 }
            { :name "Pest" :y 47.498333 :x 19.040833 :z 5 }
            { :name "Romhány" :y 47.92552 :x 19.25822 :z 4 }
            { :name "Sárospatak" :y 48.31897 :x 21.56636 :z 21 }
            { :name "Sopron" :y 47.68489 :x 16.58305 :z 147 }
            { :name "Sümeg" :y 46.9775 :x 17.281944 :z 3 }
            { :name "Tóalmás" :y 47.512389 :x 19.667869 :z 5 }
            { :name "Úri" :y 47.41639 :x 19.51777 :z 3 }
            { :name "Vác" :y 47.77641 :x 19.13724 :z 3 }
            { :name "Veresegyháza" :y 47.65712 :x 19.28495 :z 3 }
        ]
        :color (color 3)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 3) 0.5)]
                ]
            }
        }
    }, {
        :name "Németország"
        :data [
            { :name "Ulm" :y 48.4 :x 9.983333 :z 4 }
        ]
        :color (color 4)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 4) 0.5)]
                ]
            }
        }
    }, {
        :name "Olaszország"
        :data [
            { :name "Padova" :y 45.416667 :x 11.866667 :z 6 }
        ]
        :color (color 5)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 5) 0.5)]
                ]
            }
        }
    }, {
        :name "Románia"
        :data [
            { :name "Beszterce" :y 47.135833 :x 24.496389 :z 93 }
            { :name "Brassó" :y 45.666667 :x 25.616667 :z 34 }
            { :name "Fogaras" :y 45.844722 :x 24.974167 :z 8 }
            { :name "Gernyeszeg" :y 46.67 :x 24.645 :z 17 }
            { :name "Gyergyóalfalu" :y 46.701111 :x 25.503889 :z 4 }
            { :name "Gyulafehérvár" :y 46.066944 :x 23.57 :z 17 }
            { :name "Kolozsvár" :y 46.78 :x 23.559444 :z 53 }
            { :name "Marosvásárhely" :y 46.543056 :x 24.558333 :z 12 }
            { :name "Nagybánya" :y 47.656944 :x 23.574167 :z 12 }
            { :name "Nagyenyed" :y 46.312222 :x 23.729167 :z 12 }
            { :name "Nagyszeben" :y 45.8 :x 24.15 :z 128 }
            { :name "Radnót" :y 46.453611 :x 24.233333 :z 4 }
            { :name "Segesvár" :y 46.216667 :x 24.8 :z 50 }
            { :name "Székelyudvarhely" :y 46.3 :x 25.3 :z 10 }
            { :name "Torda" :y 46.560833 :x 23.783611 :z 4 }
        ]
        :color (color 6)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 6) 0.5)]
                ]
            }
        }
    }, {
        :name "Szlovákia"
        :data [
            { :name "Barskapronca" :y 48.683333 :x 18.866667 :z 3 }
            { :name "Bártfa" :y 49.294444 :x 21.276389 :z 8 }
            { :name "Besztercebánya" :y 48.7325 :x 19.149167 :z 104 }
            { :name "Eperjes" :y 48.998056 :x 21.239444 :z 23 }
            { :name "Garamkürtös" :y 48.6162 :x 18.7847 :z 3 }
            { :name "Garammindszent" :y 48.575 :x 18.875 :z 3 }
            { :name "Garamszentbenedek" :y 48.345556 :x 18.558889 :z 17 }
            { :name "Garamszentkereszt" :y 48.590278 :x 18.850833 :z 3 }
            { :name "Karvaly" :y 48.633333 :x 18.933333 :z 4 }
            { :name "Kassa" :y 48.718889 :x 21.257778 :z 83 }
            { :name "Késmárk" :y 49.138333 :x 20.429167 :z 5 }
            { :name "Komárom" :y 47.757222 :x 18.129722 :z 3 }
            { :name "Körmöcbánya" :y 48.705 :x 18.915833 :z 37 }
            { :name "Lelesz" :y 48.463889 :x 22.027778 :z 4 }
            { :name "Lőcse" :y 49.025 :x 20.588889 :z 66 }
            { :name "Nagysáros" :y 49.041667 :x 21.194444 :z 3 }
            { :name "Nagyszombat" :y 48.372222 :x 17.583333 :z 20 }
            { :name "Nyitra" :y 48.306944 :x 18.086389 :z 4 }
            { :name "Pozsony" :y 48.144722 :x 17.112778 :z 28 }
            { :name "Sasvár" :y 48.633333 :x 17.141667 :z 3 }
            { :name "Selmecbánya" :y 48.458056 :x 18.896389 :z 85 }
            { :name "Szakolca" :y 48.844722 :x 17.225 :z 5 }
            { :name "Trencsén" :y 48.891944 :x 18.036667 :z 4 }
            { :name "Zólyom" :y 48.570556 :x 19.1175 :z 4 }
        ]
        :color (color 7)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 7) 0.5)]
                ]
            }
        }
    }, {
        :name "Ukrajna"
        :data [
            { :name "Ungvár" :y 48.616667 :x 22.3 :z 7 }
        ]
        :color (color 8)
        :marker {
            :fillColor {
                :radialGradient { :cx 0.4 :cy 0.3 :r 0.7 }
                :stops [
                    [0, "rgba(255,255,255,0.5)"]
                    [1, (rgba (color 8) 0.5)]
                ]
            }
        }
    }]
}))


#_(ns koraujkor.charts.heatmap)

#_(def title "Highcharts - Heatmap chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/heatmap.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -heatmap []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "heatmap"
    }
    :title {
        :text "A jegyzékek száma keletkezési hely és idő szerint"
    }
    :subtitle {
        :text "Évtizedenként összesítve"
    }
    :xAxis {
        :categories [
                              "53", "54", "55", "56", "57", "58", "59"
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69"
            "70", "71", "72", "73", "74", "75"
        ]
    }
    :yAxis {
        :title nil
        :reversed true
        :categories [
            "Bártfa"
            "Bécs"
            "Beszterce"
            "Besztercebánya"
            "Brassó"
            "Buda"
            "Debrecen"
            "Eperjes"
            "Garamszentbenedek"
            "Gernyeszeg"
            "Gyulafehérvár"
            "Kassa"
            "Kolozsvár"
            "Körmöcbánya"
            "Kőszeg"
            "Lőcse"
            "Marosvásárhely"
            "Nagybánya"
            "Nagyenyed"
            "Nagyszeben"
            "Nagyszombat"
            "Padova"
            "Pest"
            "Pozsony"
            "Ruszt"
            "Sárospatak"
            "Segesvár"
            "Selmecbánya"
            "Sopron"
            "Szakolca"
        ]
    }
    :colorAxis {
        :min 0
        :minColor "#ffffff" ; "white"
        :maxColor (color 0)
    }
    :legend {
        :align "right"
        :layout "vertical"
        :margin 10
        :verticalAlign "top"
        :y 60
        :symbolHeight 600
    }
    :tooltip {
        :formatter #(let [x (js* "this.series.xAxis.categories[this.point.x]")
                          y (js* "this.series.yAxis.categories[this.point.y]")
                          z (js* "this.point.value")]
                        (str "<b>" y "</b> városában<br/>"
                             "<b>1" x "0</b>-<b>1" x "9</b> között<br/>"
                             "<b>" z "</b> jegyzék keletkezett"))
    }
    :series [{
        :name "jegyzékek száma"
        :borderWidth 1
        :data [
            [3 0 1] [7 0 1] [11 0 2] [13 0 1] [17 0 1] [19 0 2]
            [2 1 1] [4 1 1] [5 1 2] [6 1 1] [9 1 1] [14 1 1] [19 1 1]
            [6 2 4] [7 2 4] [8 2 2] [9 2 1] [10 2 6] [11 2 7] [12 2 10] [13 2 9] [14 2 6] [15 2 10] [16 2 1] [17 2 7] [18 2 5] [19 2 9] [20 2 3] [21 2 9]
            [0 3 2] [4 3 2] [6 3 3] [7 3 2] [8 3 2] [10 3 11] [11 3 2] [12 3 1] [13 3 4] [14 3 13] [15 3 26] [16 3 24] [17 3 6] [18 3 5] [22 3 1]
            [1 4 1] [4 4 1] [5 4 4] [7 4 3] [8 4 1] [9 4 3] [10 4 2] [13 4 1] [14 4 1] [15 4 2] [16 4 7] [17 4 2] [18 4 2] [20 4 1] [21 4 2] [22 4 1]
            [2 5 1] [15 5 2] [16 5 2] [17 5 1] [18 5 1] [19 5 4] [20 5 4] [21 5 1]
            [14 6 1] [15 6 1] [16 6 1] [17 6 1] [18 6 9] [19 6 6] [20 6 6] [21 6 2]
            [2 7 3] [4 7 1] [7 7 1] [11 7 4] [12 7 2] [14 7 4] [15 7 3] [16 7 1] [17 7 1] [18 7 1] [20 7 1] [22 7 1]
            [8 8 1] [9 8 1] [10 8 7] [11 8 4] [12 8 2] [15 8 1] [16 8 1]
            [14 9 1] [15 9 1] [16 9 12] [19 9 1] [20 9 1] [21 9 1]
            [6 10 1] [11 10 1] [12 10 3] [13 10 2] [15 10 3] [16 10 3] [17 10 2] [19 10 1] [20 10 1]
            [3 11 1] [4 11 1] [5 11 2] [6 11 6] [7 11 5] [8 11 5] [9 11 4] [10 11 7] [11 11 8] [12 11 15] [13 11 10] [14 11 11] [15 11 5] [18 11 1] [20 11 1] [21 11 1]
            [5 12 1] [6 12 2] [7 12 3] [8 12 3] [9 12 3] [10 12 6] [11 12 1] [12 12 4] [13 12 1] [14 12 12] [15 12 5] [16 12 2] [17 12 5] [18 12 2] [19 12 1] [20 12 1] [21 12 1]
            [4 13 2] [5 13 2] [6 13 4] [7 13 5] [8 13 4] [10 13 1] [11 13 4] [13 13 1] [14 13 1] [15 13 7] [19 13 1] [20 13 4] [22 13 1]
            [0 14 1] [6 14 1] [7 14 1] [8 14 1] [10 14 2] [11 14 2] [12 14 7] [13 14 12]
            [10 15 4] [11 15 4] [12 15 4] [13 15 4] [14 15 2] [15 15 21] [16 15 17] [17 15 6] [18 15 4]
            [12 16 5] [13 16 1] [14 16 1] [15 16 1] [17 16 2] [20 16 1] [21 16 1]
            [9 17 1] [12 17 1] [13 17 2] [15 17 1] [16 17 2] [19 17 2] [20 17 1] [21 17 2]
            [13 18 1] [14 18 4] [15 18 3] [16 18 1] [17 18 1] [18 18 1] [20 18 1]
            [6 19 6] [7 19 1] [8 19 5] [9 19 6] [10 19 10] [11 19 2] [12 19 11] [13 19 7] [14 19 1] [15 19 5] [16 19 5] [17 19 9] [18 19 11] [19 19 9] [20 19 19] [21 19 20] [22 19 1]
            [3 20 2] [5 20 1] [6 20 2] [7 20 1] [8 20 4] [10 20 3] [13 20 1] [14 20 4] [16 20 1] [18 20 1]
            [0 21 1] [4 21 1] [9 21 1] [11 21 1] [12 21 1] [13 21 1]
            [3 22 1] [17 22 1] [20 22 1] [21 22 1] [22 22 1]
            [2 23 1] [3 23 1] [5 23 2] [6 23 1] [7 23 3] [8 23 11] [10 23 3] [12 23 2] [13 23 1] [19 23 1] [20 23 1] [21 23 1]
            [6 24 1] [8 24 2] [10 24 1] [12 24 3] [13 24 12] [14 24 2] [15 24 8] [17 24 3] [18 24 1] [19 24 1] [21 24 1]
            [5 25 1] [7 25 1] [9 25 1] [10 25 1] [12 25 1] [13 25 1] [15 25 2] [17 25 3] [18 25 1] [19 25 3] [20 25 4] [21 25 2]
            [5 26 4] [8 26 1] [9 26 1] [10 26 1] [11 26 11] [13 26 13] [14 26 10] [15 26 6] [16 26 2] [20 26 1]
            [2 27 1] [4 27 1] [5 27 5] [6 27 7] [7 27 5] [8 27 12] [9 27 10] [10 27 7] [11 27 9] [12 27 6] [13 27 3] [14 27 6] [16 27 1] [17 27 3] [18 27 3] [19 27 3] [20 27 1] [21 27 2]
            [2 28 1] [3 28 1] [4 28 1] [5 28 1] [7 28 3] [8 28 6] [9 28 9] [10 28 18] [11 28 23] [12 28 30] [13 28 12] [14 28 12] [15 28 14] [16 28 3] [17 28 5] [18 28 4] [19 28 4]
            [13 29 1] [14 29 1] [15 29 1] [19 29 1] [21 29 1]
        ]
        :dataLabels {
            :enabled true
            :color "black"
            :style {
                :textShadow "none"
                :HcTextStroke nil
            }
        }
    }]
}))


#_(ns koraujkor.charts.pyramid)

#_(def title "Highcharts - Pyramid chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/funnel.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pyramid []
    (chart {:min-width "300px" :height "700px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pyramid"
    }
    :title {
        :text "A jegyzékek száma egyre speciálisabb feltételek szerint"
    }
    :subtitle {
        :text "Magyarország / Sopron / XVII. század"
    }
    :legend {
        :enabled false
    }
    :tooltip {
        :enabled false
    }
    :plotOptions {
        :series {
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y} db"
                :color "black"
                :softConnector true
            }
        }
    }
    :colors gradient2
    :series [{
        :data [
            ["Összes" 1687]
            ["Magyarország" 427]
            ["Sopron" 147]
            ["XVII. század" 130]
        ]
    }]
}))


#_(ns koraujkor.charts.range)

#_(def title "Highcharts - Range chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-more.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defonce tulajdonosok-élete (reagent/atom
[
    ["Zay Ferenc"             1505 1570]
    ["Thurzó (II.) Szaniszló" 1531 1586]
    ["Illésházy István"       1540 1609]
    ["Báthory András"         1566 1599]
    ["Forgách Ferenc"         1566 1615]
    ["Thurzó (V.) György"     1567 1616]
    ["Esterházy Miklós"       1582 1645]
    ["Rákóczi György, I."     1593 1648]
    ["Rákóczi Pál"            1596 1636]
    ["Batthyány I. Ádám"      1609 1659]
    ["Bethlen János"          1613 1678]
    ["Thököly Zsigmond"       1618 1678]
    ["Zrínyi Miklós"          1620 1664]
    ["Zrínyi Péter"           1621 1671]
    ["Rákóczi Zsigmond, IV."  1622 1652]
    ["Nádasdy Ferenc"         1625 1671]
    ["Batthyány I. Pál"       1629 1674]
    ["Apafi Mihály, I."       1629 1690]
    ["Batthyány II. Kristóf"  1632 1685]
    ["Teleki I. Mihály"       1634 1690]
    ["Csáky István"           1635 1699]
    ["Eszterházy Pál"         1635 1713]
    ["Bethlen Miklós"         1642 1716]
    ["Bethlen Elek"           1645 1696]
    ["Rákóczi Erzsébet"       1655 1707]
    ["Thököly Imre"           1657 1705]
    ["Batthyány II. Ádám"     1662 1703]
    ["Teleki János"           1663 1679]
    ["Apafi Mihály, II."      1676 1713]
    ["Rákóczi Ferenc, II."    1676 1735]
    ["Teleki Pál"             1677 1731]
    ["Bethlen Kata"           1700 1759]
]))

(defn -range [] (let [_ @tulajdonosok-élete]
    [chart' {:min-width "300px" :height "760px" :max-width "900px" :margin "0 auto"}
(fn [] {
    :chart {
        :type "columnrange"
        :inverted true
    }
    :title {
        :text "Tulajdonosok élete"
    }
    :subtitle {
        :text "Főnemesek"
    }
    :legend {
        :enabled false
    }
    :xAxis {
        :categories (mapv first @tulajdonosok-élete)
    }
    :yAxis {
        :title {
            :text nil
        }
    }
    :plotOptions {
        :columnrange {
            :dataLabels {
                :enabled true
                :formatter #(js* "this.y") ; clean, unformatted number for year
            }
            :tooltip {
                :pointFormat "{series.name}: <b>{point.low:.0f}</b> - <b>{point.high:.0f}</b>"
            }
        }
    }
    :colors gradient2
    :series [{
        :name "élt"
        :data (mapv rest @tulajdonosok-élete)
    }]
})]))


#_(ns koraujkor.charts.range-pie)

#_(def title "Highcharts - Combination chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-more.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -range-pie []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "columnrange"
        :inverted true
    }
    :title {
        :text "Tulajdonosok élete"
    }
    :subtitle {
        :text "Főnemesek"
    }
    :legend {
        :enabled false
    }
    :xAxis {
        :type "category"
    }
    :yAxis {
        :title {
            :text nil
        }
    }
    :plotOptions {
        :columnrange {
            :dataLabels {
                :enabled true
                :formatter #(js* "this.y") ; clean, unformatted number for year
            }
        }
    }
    :colors gradient2
    :series [{
        :name "élt"
        :data [
            { :name "Zay Ferenc" :low 1505 :high 1570 }
            { :name "Thurzó (II.) Szaniszló" :low 1531 :high 1586 }
            { :name "Illésházy István" :low 1540 :high 1609 }
            { :name "Báthory András" :low 1566 :high 1599 }
            { :name "Forgách Ferenc" :low 1566 :high 1615 }
            { :name "Thurzó (V.) György" :low 1567 :high 1616 }
            { :name "Esterházy Miklós" :low 1582 :high 1645 }
            { :name "Rákóczi György, I." :low 1593 :high 1648 }
            { :name "Rákóczi Pál" :low 1596 :high 1636 }
            { :name "Batthyány I. Ádám" :low 1609 :high 1659 }
            { :name "Bethlen János" :low 1613 :high 1678 }
            { :name "Thököly Zsigmond" :low 1618 :high 1678 }
            { :name "Zrínyi Miklós" :low 1620 :high 1664 }
            { :name "Zrínyi Péter" :low 1621 :high 1671 }
            { :name "Rákóczi Zsigmond, IV." :low 1622 :high 1652 }
            { :name "Nádasdy Ferenc" :low 1625 :high 1671 }
            { :name "Batthyány I. Pál" :low 1629 :high 1674 }
            { :name "Apafi Mihály, I." :low 1629 :high 1690 }
            { :name "Batthyány II. Kristóf" :low 1632 :high 1685 }
            { :name "Teleki I. Mihály" :low 1634 :high 1690 }
            { :name "Csáky István" :low 1635 :high 1699 }
            { :name "Eszterházy Pál" :low 1635 :high 1713 }
            { :name "Bethlen Miklós" :low 1642 :high 1716 }
            { :name "Bethlen Elek" :low 1645 :high 1696 }
            { :name "Rákóczi Erzsébet" :low 1655 :high 1707 :color (gradient2 5) }
            { :name "Thököly Imre" :low 1657 :high 1705 }
            { :name "Batthyány II. Ádám" :low 1662 :high 1703 }
            { :name "Teleki János" :low 1663 :high 1679 }
            { :name "Apafi Mihály, II." :low 1676 :high 1713 }
            { :name "Rákóczi Ferenc, II." :low 1676 :high 1735 }
            { :name "Teleki Pál" :low 1677 :high 1731 }
            { :name "Bethlen Kata" :low 1700 :high 1759 :color (gradient2 5) }
        ]
        :tooltip {
            :pointFormat "{series.name}: <b>{point.low:.0f}</b> - <b>{point.high:.0f}</b>"
        }
    }, {
        :type "pie"
        :name "nemek aránya"
        :data [{
            :name "nő"
            :y 2
            :color (gradient2 5)
            :sliced true
            :selected true
        }, {
            :name "férfi"
            :y 30
            :color (gradient2 0)
        }]
        :center [610, 80]
        :size 100
        :startAngle 90
        :showInLegend false
        :tooltip {
            :headerFormat ""
            :pointFormat "<b>{point.y}</b> {point.name}: <b>{point.percentage:.2f}</b>%"
        }
        :allowPointSelect true
    }, {
        :type "pie"
        :name "felekezeti hovatartozás"
        :data [{
            :name "kálvinista"
            :y 11
            :color (gradient2 2)
        }, {
            :name "katolikus"
            :y 15
            :color (gradient2 1)
        }, {
            :name "lutheránus"
            :y 6
            :color (gradient2 3)
        }]
        :center [136, 490]
        :size 160
        :showInLegend false
        :tooltip {
            :headerFormat ""
            :pointFormat "<b>{point.y}</b> {point.name}: <b>{point.percentage:.2f}</b>%"
        }
        :allowPointSelect true
    }]
}))


#_(ns koraujkor.charts.tree)

#_(def title "Highcharts - Tree chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -tree []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "bar"
    }
    :title {
        :text "Tulajdonosok száma halálozásuk évtizede szerint"
    }
    :subtitle {
        :text "Felekezeti hovatartozásuk szerint csoportosítva"
    }
    :xAxis [{
        :categories [
                                    "54", "55", "56", "57", "58", "59"
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69"
            "70", "71", "72", "73", "74", "75", "76", "77", "78"
        ]
        :reversed true
        :labels {
            :step 1
            :format "1{value}0-"
        }
    }, {
        :categories [
                                    "54", "55", "56", "57", "58", "59"
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69"
            "70", "71", "72", "73", "74", "75", "76", "77", "78"
        ]
        :reversed true
        :opposite true
        :linkedTo 0
        :labels {
            :step 1
            :format "-1{value}9"
        }
    }]
    :yAxis {
        :title {
            :text nil
        }
        :labels {
            :formatter #(js* "Math.abs(this.value)")
        }
    }
    :plotOptions {
        :series {
            :stacking "normal"
        }
    }
    :tooltip {
        :formatter #(let [c (js* "this.point.category")]
            (str "<b>" (js* "Math.abs(this.point.y)") "</b> " (js* "this.series.name") " \u271d <b>1" c "0</b>-<b>1" c "9</b>"))
    }
    :colors gradient2
    :series [{
        :index 0 :legendIndex 0
        :name "katolikus"
        :data [-1, -3, -8, -6, -6, -3, -3, -3, nil, -6, -10, -1, -7, -10, -9, -5, -4, -6, -6, -3, nil, -3, nil, -1, nil]
        :color (gradient2 1)
    }, {
        :index 3 :legendIndex 1
        :name "lutheránus"
        :data [nil, 1, 3, 4, 17, 24, 14, 24, 18, 51, 59, 63, 66, 35, 73, 37, 33, 25, 25, 28, 29, 2, nil, nil, 1]
        :color (gradient2 3)
    }, {
        :index 2 :legendIndex 2
        :name "kálvinista"
        :data [nil, nil, nil, nil, nil, 3, 2, nil, 2, 4, 4, 4, 4, 8, 4, 4, 3, 15, 7, 7, 6, 1, 3, nil, nil]
        :color (gradient2 2)
    }, {
        :index 1 :legendIndex 3
        :name "unitárius"
        :data [nil, nil, nil, nil, nil, nil, 1, 1, 1, 2, 1, nil, 3, nil, nil, nil, nil, 1, 1, 2, nil, nil, nil, nil, nil]
        :color (gradient2 0)
    }]
}))


#_(ns koraujkor.charts.tree-pie)

#_(def title "Highcharts - Tree chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -tree-pie []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "bar"
    }
    :title {
        :text "Tulajdonosok száma halálozásuk évtizede szerint"
    }
    :subtitle {
        :text "Felekezeti hovatartozásuk szerint csoportosítva"
    }
    :xAxis [{
        :categories [
                                    "54", "55", "56", "57", "58", "59"
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69"
            "70", "71", "72", "73", "74", "75", "76", "77", "78"
        ]
        :reversed true
        :labels {
            :step 1
            :format "1{value}0-"
        }
    }, {
        :categories [
                                    "54", "55", "56", "57", "58", "59"
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69"
            "70", "71", "72", "73", "74", "75", "76", "77", "78"
        ]
        :reversed true
        :opposite true
        :linkedTo 0
        :labels {
            :step 1
            :format "-1{value}9"
        }
    }]
    :yAxis {
        :title {
            :text nil
        }
        :labels {
            :formatter #(js* "Math.abs(this.value)")
        }
    }
    :plotOptions {
        :series {
            :stacking "normal"
        }
    }
    :tooltip {
        :formatter #(let [point (js* "this.point") series (js* "this.series")]
            (if (= (.-type series) "pie")
                (str "<b>" (.-y point) "</b> " (.-name point) ": <b>" (.. point -percentage (toFixed 2)) "</b>%")
                (let [n (.abs js/Math (.-y point)) c (.-category point)]
                    (str "<b>" n "</b> " (.-name series) " \u271d <b>1" c "0</b>-<b>1" c "9</b>"))))
    }
    :colors gradient2
    :series [{
        :index 0 :legendIndex 0
        :name "katolikus"
        :data [-1, -3, -8, -6, -6, -3, -3, -3, nil, -6, -10, -1, -7, -10, -9, -5, -4, -6, -6, -3, nil, -3, nil, -1, nil]
        :color (gradient2 1)
    }, {
        :index 3 :legendIndex 1
        :name "lutheránus"
        :data [nil, 1, 3, 4, 17, 24, 14, 24, 18, 51, 59, 63, 66, 35, 73, 37, 33, 25, 25, 28, 29, 2, nil, nil, 1]
        :color (gradient2 3)
    }, {
        :index 2 :legendIndex 2
        :name "kálvinista"
        :data [nil, nil, nil, nil, nil, 3, 2, nil, 2, 4, 4, 4, 4, 8, 4, 4, 3, 15, 7, 7, 6, 1, 3, nil, nil]
        :color (gradient2 2)
    }, {
        :index 1 :legendIndex 3
        :name "unitárius"
        :data [nil, nil, nil, nil, nil, nil, 1, 1, 1, 2, 1, nil, 3, nil, nil, nil, nil, 1, 1, 2, nil, nil, nil, nil, nil]
        :color (gradient2 0)
    }, {
        :type "pie"
        :name "felekezeti hovatartozás"
        :data [{
            :name "katolikus"
            :y 104
            :color (gradient2 1)
        }, {
            :name "lutheránus"
            :y 632
            :color (gradient2 3)
        }, {
            :name "kálvinista"
            :y 81
            :color (gradient2 2)
        }, {
            :name "unitárius"
            :y 13
            :color (gradient2 0)
        }]
        :center [660, 100]
        :size 160
        :showInLegend false
        :tooltip {
            :headerFormat ""
            :pointFormat "<b>{point.y}</b> {point.name}: <b>{point.percentage:.2f}</b>%"
        }
        :allowPointSelect true
    }]
}))


#_(ns koraujkor.charts.pie-3d-ortus)

#_(def title "Highcharts - Pie chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie-3d-ortus []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
        :options3d {
            :enabled true
            :alpha 50
        }
    }
    :title {
        :text "A tulajdonosok száma társadalmi állásuk szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :pie {
            :allowPointSelect true
            :cursor "pointer"
            :depth 60
            :innerSize 160
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y} fő"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :type "pie"
        :name "részesedése"
        :data [
            [ "főnemes" 42 ]
            [ "nemes" 108 ]
            [ "paraszt" 54 ]
            [ "polgár" 1052 ]
        ]
    }]
}))


#_(ns koraujkor.charts.pie-3d-offic)

#_(def title "Highcharts - Pie chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie-3d-offic []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
        :options3d {
            :enabled true
            :alpha 50
        }
    }
    :title {
        :text "A tulajdonosok száma (min. 5) foglalkozásuk szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :pie {
            :allowPointSelect true
            :cursor "pointer"
            :depth 60
            :innerSize 160
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y} fő"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :type "pie"
        :name "részesedése"
        :data [
            [ "lelkész" 113 ]
            [ "plébános" 43 ]
            [ "főpap" 37 ]
            [ "tanár" 31 ]
            [ "kereskedő" 26 ]
            [ "kamarai alkalmazott" 23 ]
            [ "pap" 23 ]
            [ "prédikátor" 18 ]
            [ "tanácsos" 18 ]
            [ "orvos" 16 ]
            [ "szabó" 15 ]
            [ "aranyműves" 13 ]
            [ "főispán" 13 ]
            [ "iskolai rektor" 12 ]
            [ "rektor" 12 ]
            [ "szenátor" 12 ]
            [ "szűcs" 12 ]
            [ "jegyző" 11 ]
            [ "cipész" 10 ]
            [ "tímár" 10 ]
            [ "könyvkereskedő" 9 ]
            [ "könyvkötő" 8 ]
            [ "ügyvéd" 8 ]
            [ "bányász" 7 ]
            [ "bíró" 7 ]
            [ "főkapitány" 7 ]
            [ "fürdős" 7 ]
            [ "mészáros" 7 ]
            [ "szerzetes" 7 ]
            [ "tanító" 7 ]
            [ "diák" 6 ]
            [ "fazekas" 6 ]
            [ "fejedelem" 6 ]
            [ "kádár" 6 ]
            [ "költő" 6 ]
            [ "városbíró" 6 ]
            [ "borbély" 5 ]
            [ "diplomata" 5 ]
            [ "gyógyszerész" 5 ]
            [ "iskolamester" 5 ]
            [ "nyomdász" 5 ]
            [ "püspök" 5 ]
            [ "történetíró" 5 ]
        ]
    }]
}))


#_(ns koraujkor.charts.bar-offic)

#_(def title "Highcharts - Bar chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -bar-offic []
    (chart {:min-width "300px" :height "3000px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "bar"
    }
    :title {
        :text "A tulajdonosok száma foglalkozásuk szerint"
    }
    :legend {
        :enabled false
    }
    :xAxis {
        :type "category"
    }
    :yAxis {
        :title {
            :text nil
        }
    }
    :tooltip {
        :headerFormat ""
        :pointFormat "{point.name}: <b>{point.y}</b> fő"
    }
    :plotOptions {
        :bar {
            :dataLabels {
                :enabled true
            }
        }
    }
    :colors gradient2
    :series [{
        :data [
            [ "lelkész" 113 ]
            [ "plébános" 43 ]
            [ "főpap" 37 ]
            [ "tanár" 31 ]
            [ "kereskedő" 26 ]
            [ "kamarai alkalmazott" 23 ]
            [ "pap" 23 ]
            [ "prédikátor" 18 ]
            [ "tanácsos" 18 ]
            [ "orvos" 16 ]
            [ "szabó" 15 ]
            [ "aranyműves" 13 ]
            [ "főispán" 13 ]
            [ "iskolai rektor" 12 ]
            [ "rektor" 12 ]
            [ "szenátor" 12 ]
            [ "szűcs" 12 ]
            [ "jegyző" 11 ]
            [ "cipész" 10 ]
            [ "tímár" 10 ]
            [ "könyvkereskedő" 9 ]
            [ "könyvkötő" 8 ]
            [ "ügyvéd" 8 ]
            [ "bányász" 7 ]
            [ "bíró" 7 ]
            [ "főkapitány" 7 ]
            [ "fürdős" 7 ]
            [ "mészáros" 7 ]
            [ "szerzetes" 7 ]
            [ "tanító" 7 ]
            [ "diák" 6 ]
            [ "fazekas" 6 ]
            [ "fejedelem" 6 ]
            [ "kádár" 6 ]
            [ "költő" 6 ]
            [ "városbíró" 6 ]
            [ "borbély" 5 ]
            [ "diplomata" 5 ]
            [ "gyógyszerész" 5 ]
            [ "iskolamester" 5 ]
            [ "nyomdász" 5 ]
            [ "püspök" 5 ]
            [ "történetíró" 5 ]
            [ "asztalos" 4 ]
            [ "felcser" 4 ]
            [ "főbíró" 4 ]
            [ "hentes" 4 ]
            [ "kancellár" 4 ]
            [ "kapitány" 4 ]
            [ "katona" 4 ]
            [ "kovács" 4 ]
            [ "kötélverő" 4 ]
            [ "nádor" 4 ]
            [ "nyerges" 4 ]
            [ "országbíró" 4 ]
            [ "takács" 4 ]
            [ "városi tanácsos" 4 ]
            [ "csizmadia" 3 ]
            [ "kamarai írnok" 3 ]
            [ "kamarás" 3 ]
            [ "kántor" 3 ]
            [ "királyi tanácsos" 3 ]
            [ "lakatos" 3 ]
            [ "pék" 3 ]
            [ "polgármester" 3 ]
            [ "posztókészítő" 3 ]
            [ "szappanfőző" 3 ]
            [ "üveges" 3 ]
            [ "városi írnok" 3 ]
            [ "bencés szerzetes" 2 ]
            [ "deák" 2 ]
            [ "ferences atya" 2 ]
            [ "fürdőmester" 2 ]
            [ "gombkötő" 2 ]
            [ "humanista" 2 ]
            [ "írnok" 2 ]
            [ "író" 2 ]
            [ "iskolarektor" 2 ]
            [ "kamarai tisztviselő" 2 ]
            [ "kanonok" 2 ]
            [ "kereskedő felesége" 2 ]
            [ "kereskedősegéd" 2 ]
            [ "országgyűlési képviselő" 2 ]
            [ "ötvös" 2 ]
            [ "sebész" 2 ]
            [ "szappankészítő" 2 ]
            [ "szatócs" 2 ]
            [ "tanácstag" 2 ]
            [ "trombitás" 2 ]
            [ "tűkészítő" 2 ]
            [ "vagyonösszeíró" 2 ]
            [ "városi alkalmazott" 2 ]
            [ "városi jegyző" 2 ]
            [ "zenész" 2 ]
            [ "zeneszerző" 2 ]
            [ "ács" 1 ]
            [ "alispán" 1 ]
            [ "aljegyző" 1 ]
            [ "apát" 1 ]
            [ "az ispotály kurátora" 1 ]
            [ "bányafelügyelő" 1 ]
            [ "bányaügyi főfelügyelő" 1 ]
            [ "bognár" 1 ]
            [ "borbélysegéd" 1 ]
            [ "capellanus" 1 ]
            [ "collaborator" 1 ]
            [ "curator" 1 ]
            [ "császári harmincados" 1 ]
            [ "császári törvényszéki alkalmazott" 1 ]
            [ "diakónus" 1 ]
            [ "egyetemi hallgató" 1 ]
            [ "egyházfi" 1 ]
            [ "egyházi gondnok" 1 ]
            [ "egyházi író" 1 ]
            [ "egyházi művek írója" 1 ]
            [ "emlékíró" 1 ]
            [ "fejedelmi tanácsúr" 1 ]
            [ "ferences szerzetes" 1 ]
            [ "festő" 1 ]
            [ "filozófus" 1 ]
            [ "főadószedő" 1 ]
            [ "főgondnok" 1 ]
            [ "főkancellár" 1 ]
            [ "főlovászmester" 1 ]
            [ "fuvaros" 1 ]
            [ "gazdálkodó" 1 ]
            [ "gazdasági szakember" 1 ]
            [ "hadbiztos" 1 ]
            [ "hagyatéki összeíró" 1 ]
            [ "harmincados felesége" 1 ]
            [ "historikus" 1 ]
            [ "horvát bán" 1 ]
            [ "humanista író" 1 ]
            [ "iskolai gondnok" 1 ]
            [ "iskola rektor" 1 ]
            [ "ispotálytiszt" 1 ]
            [ "ítélőmester" 1 ]
            [ "kalapos" 1 ]
            [ "kalligráfus" 1 ]
            [ "kancelláriai igazgató" 1 ]
            [ "kannakészítő" 1 ]
            [ "kántor felesége" 1 ]
            [ "káptalani dékán" 1 ]
            [ "kendőkészítő" 1 ]
            [ "késműves" 1 ]
            [ "készkészítő" 1 ]
            [ "készkészítő felesége" 1 ]
            [ "kincstartó" 1 ]
            [ "királyi főpohárnokmester" 1 ]
            [ "királyi helytartó" 1 ]
            [ "királyi megbízott" 1 ]
            [ "királyi táblai ülnök" 1 ]
            [ "kolozsvári református egyház kurátora" 1 ]
            [ "kormányzósági tanácsos" 1 ]
            [ "koronaőr" 1 ]
            [ "könyvkötő segéd" 1 ]
            [ "krónikaíró" 1 ]
            [ "kulcsár" 1 ]
            [ "lánckovács" 1 ]
            [ "lantművész" 1 ]
            [ "lektor" 1 ]
            [ "lelkész felesége" 1 ]
            [ "lelki gondozó" 1 ]
            [ "madarász" 1 ]
            [ "matematikus" 1 ]
            [ "mézeskalácsos" 1 ]
            [ "mezőőr" 1 ]
            [ "nagyprépost" 1 ]
            [ "nyitrai alispán" 1 ]
            [ "ólommérő" 1 ]
            [ "órakészítő" 1 ]
            [ "orgonakészítő" 1 ]
            [ "orgonista" 1 ]
            [ "orgonista felesége" 1 ]
            [ "országgyülési követ" 1 ]
            [ "osztozkodó-bíra" 1 ]
            [ "önéletíró" 1 ]
            [ "patkolókovács" 1 ]
            [ "pedagógiai író" 1 ]
            [ "piaci jegyző" 1 ]
            [ "poéta" 1 ]
            [ "polihisztor" 1 ]
            [ "politikai író" 1 ]
            [ "politikus" 1 ]
            [ "porkoláb" 1 ]
            [ "posztónyíró" 1 ]
            [ "publicista" 1 ]
            [ "puskaportöltő" 1 ]
            [ "puskaportörő mester" 1 ]
            [ "requisitor" 1 ]
            [ "rézműves" 1 ]
            [ "segéd iskolamester" 1 ]
            [ "segédtanító" 1 ]
            [ "serfőző" 1 ]
            [ "serfőzőmester" 1 ]
            [ "szabó felesége" 1 ]
            [ "szabómester" 1 ]
            [ "szepesi alispán" 1 ]
            [ "szerzetes rend" 1 ]
            [ "szijjártó" 1 ]
            [ "szolgabíró" 1 ]
            [ "szolgáló" 1 ]
            [ "szűcsmester" 1 ]
            [ "táblai írnok" 1 ]
            [ "táblai ülnök" 1 ]
            [ "tábornagy" 1 ]
            [ "tanácsos felesége" 1 ]
            [ "tanácsúr" 1 ]
            [ "templom- és iskolaszolga" 1 ]
            [ "teológus" 1 ]
            [ "tiszttartó" 1 ]
            [ "tisztviselő" 1 ]
            [ "tudós" 1 ]
            [ "udvarbíró" 1 ]
            [ "udvari alkalmazott" 1 ]
            [ "udvari kancelláriai tanácsos" 1 ]
            [ "udvarmester" 1 ]
            [ "vámos" 1 ]
            [ "várkapitány" 1 ]
            [ "városi őrség kapitánya" 1 ]
            [ "vásározó" 1 ]
            [ "vaskereskedő" 1 ]
        ]
    }, {
        :type "pie"
        :data [
            [ "lelkész" 113 ]
            [ "plébános" 43 ]
            [ "főpap" 37 ]
            [ "tanár" 31 ]
            [ "kereskedő" 26 ]
            [ "kamarai alkalmazott" 23 ]
            [ "pap" 23 ]
            [ "prédikátor" 18 ]
            [ "tanácsos" 18 ]
            [ "orvos" 16 ]
            [ "szabó" 15 ]
            [ "aranyműves" 13 ]
            [ "főispán" 13 ]
            [ "iskolai rektor" 12 ]
            [ "rektor" 12 ]
            [ "szenátor" 12 ]
            [ "szűcs" 12 ]
            [ "jegyző" 11 ]
            [ "cipész" 10 ]
            [ "tímár" 10 ]
            [ "könyvkereskedő" 9 ]
            [ "könyvkötő" 8 ]
            [ "ügyvéd" 8 ]
        ]
        :center [402, 360]
        :size 360
        :innerSize 56
        :shadow true
        :showInLegend false
        :tooltip {
            :headerFormat ""
            :pointFormat "<b>{point.y}</b> {point.name}: <b>{point.percentage:.2f}</b>%"
        }
        :allowPointSelect true
    }]
}))


#_(ns koraujkor.charts.heatmap-typo)

#_(def title "Highcharts - Heatmap chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/heatmap.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -heatmap-typo []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "heatmap"
    }
    :title {
        :text "A jegyzékek száma forrástipológia és keletkezési idő szerint"
    }
    :subtitle {
        :text "Évtizedenként összesítve"
    }
    :xAxis {
        :categories [
                              "53", "54", "55", "56", "57", "58", "59"
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69"
            "70", "71", "72", "73", "74", "75"
        ]
    }
    :yAxis {
        :title nil
        :reversed true
        :categories [
            "I.1. Katalógus"
            "I.2.1. Hagyaték"
            "I.2.2. Árvaügy"
            "I.2.3. Végrendelet"
            "I.2.4. Vagyonelkobzás"
            "I.2.5. Peres ügy"
            "I.2.6. Canonica visitatio"
            "I.2.7. Vegyes célú"
            "I.3.1. Adományozás"
            "I.3.2. Könyvkölcsönzés"
            "I.3.3. Intézményi vásárlás"
            "I.4. Személyes irat"
            "I.4.1. Naplóbejegyzés"
            "I.4.2. Levél"
            "I.5. Egyéb"
            "I.5.1. Személyes kölcsönzés"
            "I.5.2. Adásvétel"
            "I.5.3. Köttetés"
            "I.5.4. Könyvszámla"
            "I.5.5. Könyvbejegyzés"
            "I.5.6. Nem hivatalos"
        ]
    }
    :colorAxis {
        :min 0
        :minColor "#ffffff" ; "white"
        :maxColor (color 0)
    }
    :legend {
        :align "right"
        :layout "vertical"
        :margin 10
        :verticalAlign "top"
        :y 60
        :symbolHeight 600
    }
    :tooltip {
        :formatter #(let [x (js* "this.series.xAxis.categories[this.point.x]")
                          y (js* "this.series.yAxis.categories[this.point.y]")
                          z (js* "this.point.value")]
                        (str "<i>" y "</i> kategóriában<br/>"
                             "<b>1" x "0</b>-<b>1" x "9</b> között<br/>"
                             "<b>" z "</b> jegyzék keletkezett"))
    }
    :series [{
        :name "jegyzékek száma"
        :borderWidth 1
        :data [
            [10 0 2] [11 0 1] [12 0 3] [13 0 3] [14 0 4] [15 0 8] [16 0 6] [17 0 7] [18 0 4] [19 0 1] [2 0 1] [21 0 3] [22 0 1] [3 0 1] [4 0 1] [7 0 5] [8 0 1] [9 0 2]
            [0 1 2] [10 1 73] [11 1 79] [12 1 92] [13 1 85] [14 1 67] [15 1 105] [16 1 65] [17 1 49] [18 1 41] [19 1 36] [20 1 39] [21 1 40] [22 1 6] [2 1 6] [3 1 8] [4 1 8] [5 1 28] [6 1 37] [7 1 25] [8 1 41] [9 1 36]
            [10 2 1] [11 2 1] [12 2 4] [13 2 2] [14 2 1] [15 2 1] [17 2 1] [19 2 2] [21 2 1] [9 2 1]
            [11 3 2] [13 3 1] [14 3 2] [16 3 1] [17 3 2] [3 3 3] [4 3 2] [7 3 3]
            [0 4 1] [10 4 1] [12 4 2] [13 4 1] [14 4 13] [15 4 5] [17 4 5] [18 4 3] [20 4 1] [3 4 1] [7 4 2]
            [10 5 1] [12 5 3] [13 5 1] [14 5 1] [18 5 1] [19 5 1] [7 5 1]
            [10 6 7] [11 6 10] [12 6 41] [13 6 4] [14 6 14] [15 6 4] [16 6 4] [18 6 17] [19 6 32] [20 6 4] [21 6 36] [3 6 1] [8 6 2] [9 6 9]
            [0 7 1] [10 7 12] [11 7 8] [12 7 10] [13 7 8] [14 7 15] [15 7 8] [16 7 5] [17 7 7] [18 7 4] [19 7 5] [20 7 6] [2 7 1] [21 7 4] [22 7 1] [3 7 4] [4 7 3] [6 7 1] [7 7 10] [8 7 11] [9 7 3]
            [10 8 3] [11 8 1] [12 8 4] [13 8 3] [14 8 9] [15 8 6] [16 8 5] [17 8 6] [18 8 8] [19 8 9] [20 8 4] [21 8 4] [5 8 1] [6 8 1] [7 8 2] [8 8 2]
            [10 9 2] [11 9 1] [14 9 2] [15 9 2] [17 9 6] [18 9 5] [20 9 1] [8 9 2] [9 9 1]
            [10 10 2]
            [17 11 1]
            [13 12 2] [14 12 4] [15 12 3] [16 12 4] [17 12 7] [18 12 3] [19 12 3] [20 12 1] [7 12 1]
            [10 13 2] [12 13 3] [13 13 2] [14 13 6] [15 13 8] [16 13 6] [17 13 5] [18 13 3] [19 13 1] [20 13 1] [2 13 1] [22 13 1] [4 13 1]
            [13 14 1] [19 14 1] [20 14 5] [21 14 4] [7 14 1] [8 14 1]
            [13 15 1] [16 15 8] [7 15 1]
            [10 16 1] [12 16 2] [14 16 1] [8 16 2]
            [13 17 1] [21 17 1] [4 17 1] [6 17 1]
            [13 18 3] [16 18 2] [4 18 1] [8 18 1]
            [12 19 3] [13 19 1] [14 19 4] [15 19 1] [16 19 4] [17 19 3] [20 19 1] [21 19 2] [2 19 2] [7 19 1] [8 19 5] [9 19 2]
            [11 20 4] [12 20 4] [13 20 2] [14 20 7] [15 20 3] [16 20 4] [17 20 6] [18 20 6] [19 20 1] [21 20 1] [5 20 1] [6 20 1] [8 20 2] [9 20 1]
        ]
        :dataLabels {
            :enabled true
            :color "black"
            :style {
                :textShadow "none"
                :HcTextStroke nil
            }
        }
    }]
}))


#_(ns koraujkor.charts.seneca-ortus)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -seneca-ortus []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "x"
    }
    :title {
        :text "„Seneca” előfordulása a jegyzék keletkezésének ideje szerint évtizedenként összesítve"
    }
    :subtitle {
        :text "A tulajdonos társadalmi rangja szerint csoportosítva, együttesen ábrázolva"
    }
    :xAxis {
        :allowDecimals false
        :startOnTick true
        :endOnTick false
        :showLastLabel true
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :allowDecimals false
        :floor 0
        :title {
            :text "„Seneca” előfordulása évtizedenként összesítve"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :areaspline {
            :stacking "normal"
            :pointStart 1530
            :pointInterval 10
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :colors gradient2
    :series [{
        :name "főnemes"
        :data [0, 0, 0, 0, 0, 1, 1, 0, 3, 3, 3, 0, 4, 6, 8, 0, 0, 0, 1, 3, 0, 0, 5]
    }, {
        :name "nemes"
        :data [0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 3, 3, 2, 4, 2, 0, 0, 0, 0, 0]
    }, {
        :name "paraszt"
        :data [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0]
    }, {
        :name "polgár"
        :data [0, 0, 0, 0, 0, 2, 3, 3, 4, 0, 1, 3, 1, 5, 7, 5, 7, 7, 7, 2, 3, 8, 0]
    }, {
        :name "(n.a.)"
        :data [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 9, 8, 1, 1, 9, 1, 6, 7, 1, 1]
    }, {
        :type "pie"
        :data [
            [ "főnemes" 38 ]
            [ "nemes" 18 ]
            [ "paraszt" 3 ]
            [ "polgár" 68 ]
            [ "(n.a.)" 47 ]
        ]
        :center [180, 156]
        :size 240
        :innerSize 60
        :shadow true
        :showInLegend false
        :tooltip {
            :headerFormat ""
            :pointFormat "<b>{point.y}</b> {point.name}: <b>{point.percentage:.2f}</b>%"
        }
        :allowPointSelect true
    }]
}))


#_(ns koraujkor.charts.seneca-religio)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -seneca-religio []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "x"
    }
    :title {
        :text "„Seneca” előfordulása a jegyzék keletkezésének ideje szerint évtizedenként összesítve"
    }
    :subtitle {
        :text "A tulajdonos felekezeti hovatartozása szerint csoportosítva, együttesen ábrázolva"
    }
    :xAxis {
        :allowDecimals false
        :startOnTick true
        :endOnTick false
        :showLastLabel true
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :allowDecimals false
        :floor 0
        :title {
            :text "„Seneca” előfordulása évtizedenként összesítve"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :areaspline {
            :stacking "normal"
            :pointStart 1530
            :pointInterval 10
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :colors gradient2
    :series [{
        :name "katolikus"
        :data [0, 0, 0, 0, 1, 0, 3, 0, 1, 1, 0, 0, 5, 16, 13, 2, 2, 9, 2, 7, 1, 1, 1]
        :color (gradient2 1)
    }, {
        :name "lutheránus"
        :data [0, 0, 0, 0, 0, 3, 0, 1, 5, 0, 1, 3, 2, 6, 7, 3, 8, 3, 3, 2, 1, 5, 0]
        :color (gradient2 3)
    }, {
        :name "kálvinista"
        :data [0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 0, 0, 2, 0, 0, 3, 2, 6, 3, 4, 1, 0, 2]
        :color (gradient2 2)
    }, {
        :name "unitárius"
        :data [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 6, 0, 0]
        :color (gradient2 0)
    }, {
        :name "(n.a.)"
        :data [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 0, 0, 1, 0, 1, 4, 3]
        :color (gradient2 5)
    }, {
        :type "pie"
        :data [
            { :name "katolikus" :y 65 :color (gradient2 1) }
            { :name "lutheránus" :y 53 :color (gradient2 3) }
            { :name "kálvinista" :y 27 :color (gradient2 2) }
            { :name "unitárius" :y 8 :color (gradient2 0) }
            { :name "(n.a.)" :y 19 :color (gradient2 5) }
        ]
        :center [180, 156]
        :size 240
        :innerSize 60
        :shadow true
        :showInLegend false
        :tooltip {
            :headerFormat ""
            :pointFormat "<b>{point.y}</b> {point.name}: <b>{point.percentage:.2f}</b>%"
        }
        :allowPointSelect true
    }]
}))


#_(ns koraujkor.charts.mokka-r-cent)

#_(def title "Highcharts - Pie chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -mokka-r-cent []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
        :options3d {
            :enabled true
            :alpha 50
        }
    }
    :title {
        :text "Mokka-R kötetek száma megjelenésük évszázada szerint (incl. facsimile)"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :pie {
            :allowPointSelect true
            :cursor "pointer"
            :depth 60
            :innerSize 160
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :type "pie"
        :name "részesedése"
        :data [
         ;; [ "XI. század", 4 ]
         ;; [ "XII. század", 6 ]
         ;; [ "XIII. század", 2 ]
            [ "XV. század" 1934 ]
            [ "XVI. század" 37548 ]
            [ "XVII. század" 45671 ]
            [ "XVIII. század" 92248 ]
            [ "XIX. század" 91228 ]
            [ "XX. század" 507 ]
            {
                :name "sine anno"
                :y 7239
                :sliced true
                :selected true
            }
        ]
    }]
}))


#_(ns koraujkor.charts.mokka-r-bibl)

#_(def title "Highcharts - Bar chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -mokka-r-bibl []
    (chart {:min-width "300px" :height "1800px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "bar"
    }
    :title {
        :text "Mokka-R taggyűjtemények a kötetek <i>(= rekordok)</i> száma szerint"
    }
    :legend {
        :enabled false
    }
    :xAxis {
        :type "category"
    }
    :yAxis {
        :title {
            :text nil
        }
    }
    :tooltip {
        :headerFormat ""
        :pointFormat "{point.name}: <b>{point.y}</b> kötet"
    }
    :plotOptions {
        :bar {
            :dataLabels {
                :enabled true
            }
        }
    }
    :colors gradient2
    :series [{
        :data [
            [ "Debreceni Egyetemi és Nemzeti Könyvtár" 45257 ]
            [ "Somogyi-könyvtár" 27114 ]
            [ "Esztergomi Főszékesegyházi Könyvtár" 19937 ]
            [ "FSZEK OPAC -1850" 18416 ]
            [ "SZTE Egyetemi Könyvtár" 16425 ]
            [ "ELTE Egyetemi Könyvtár" 13723 ]
            [ "Klimó-gyűjtemény" 13214 ]
            [ "OSZK Antiqua" 13046 ]
            [ "Ráday Könyvtár" 10831 ]
            [ "PTEEK OPAC -1850" 8771 ]
            [ "Régi Magyar Könyvtár III." 8457 ]
            [ "II. Rákóczi Ferenc Megyei Könyvtár" 8380 ]
            [ "Evangélikus Hittudományi Egyetem Könyvtára" 7208 ]
            [ "Magyar Országos Levéltár Könyvtára" 7068 ]
            [ "Nagykárolyi piarista könyvtár" 6698 ]
            [ "Régi Magyar Könyvtár I-II." 6623 ]
            [ "Gyulafehérvári Hittudományi Főiskola Könyvtára" 4525 ]
            [ "Debreceni Református Kollégium Könyvtára" 3428 ]
            [ "Országgyűlési Könyvtár" 3268 ]
            [ "Apponyi Hungarica" 2947 ]
            [ "Régi magyarországi nyomtatványok" 2924 ]
            [ "KSH Könyvtár" 2584 ]
            [ "Reguly Antal Műemlékkönyvtár" 2348 ]
            [ "Kaplonyi ferences könyvtár" 2071 ]
            [ "Füleki ferences könyvtár" 1928 ]
            [ "Szamosújvári örmény könyvtár" 1657 ]
            [ "Nagyváradi római katolikus egyházmegyei könyvtár" 1646 ]
            [ "Piarista Központi Könyvtár" 1601 ]
            [ "Batthyány-gyűjtemény" 1423 ]
            [ "SZIE Állatorvos-tudományi Könyvtár" 1162 ]
            [ "Kolozsvári Protestáns Teológiai Intézet" 1142 ]
            [ "BME OMIKK" 1138 ]
            [ "Helischer könyvtár" 1115 ]
            [ "FSZEK Antiqua" 991 ]
            [ "Zombori Kármelita Rendház Könyvtára" 936 ]
            [ "Kecskeméti Református Egyházközség Könyvtára" 877 ]
            [ "Nagyenyedi minorita könyvtár" 872 ]
            [ "Csurgói Csokonai Vitéz Mihály Református Gimnázium Könyvtára" 704 ]
            [ "Országos Pedagógiai Könyvtár és Múzeum" 610 ]
            [ "Országos Mezőgazdasági Könyvtár" 596 ]
            [ "A kolozsvári római katolikus Lyceum Könyvtár" 507 ]
            [ "Kalocsai Főszékesegyházi Könyvtár" 505 ]
            [ "Magyar Képzőművészeti Egyetem Könyvtára" 440 ]
            [ "Füleki Vármúzeum" 384 ]
            [ "MFGI Országos Földtani Szakkönyvtár" 300 ]
            [ "Sapientia Szerzetesi Hittudományi Főiskola Könyvtára" 283 ]
            [ "Csíkszépvízi örmény könyvtár" 113 ]
            [ "RMNy „S” (előkészületben)" 88 ]
            [ "Kolozsvári református kollégium könyvtára" 66 ]
            [ "Kolozsvári akadémiai könyvtár" 28 ]
            [ "Rimaszombat" 9 ]
         ;; [ "Kőszegi Evangélikus Egyházközség" 2 ]
         ;; [ "Esztergomi Hittudományi Főiskola Könyvtára" 1 ]
        ]
    }, {
        :type "pie"
        :data [
            {
                :sliced true
                :selected true
                :name "bibliográfia"
                :y 21039
                :color (gradient2 1)
            }
            {
                :name "határon innen"
                :y #_231651 231648
                :color (gradient2 3)
            }
            {
                :name "határon túl"
                :y 23697
                :color (gradient2 2)
            }
        ]
        :center [370, 360]
        :size 360
        :innerSize 56
        :shadow true
        :showInLegend false
        :tooltip {
            :headerFormat ""
            :pointFormat "<b>{point.y}</b> {point.name}: <b>{point.percentage:.2f}</b>%"
        }
        :allowPointSelect true
    }]
}))


#_(ns koraujkor.charts.mokka-r-datum)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -mokka-r-datum []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "x"
    }
    :title {
        :text "Mokka-R kötetek száma megjelenésük éve szerint"
    }
    :xAxis {
        :allowDecimals false
        :labels {
            :formatter #(js* "this.value") ; clean, unformatted number for year
        }
    }
    :yAxis {
        :title {
            :text "Kötetek száma évente"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " db"
    }
    :plotOptions {
        :areaspline {
            :stacking "normal"
            :pointStart 1450
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series [{
        :name "bibliográfia"
        :data [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 4, 5, 3, 4, 3, 4, 1, 2, 7, 11, 5, 5, 14, 1, 12, 9, 11, 10, 18, 7, 2, 10, 9, 10, 10, 12, 24, 11, 13, 26, 31, 18, 11, 27, 18, 21, 14, 26, 11, 22, 27, 22, 30, 40, 26, 21, 30, 32, 20, 38, 28, 31, 19, 15, 35, 34, 21, 24, 47, 21, 47, 15, 15, 22, 19, 32, 26, 31, 18, 43, 25, 26, 39, 40, 19, 20, 32, 26, 48, 45, 35, 30, 27, 55, 47, 31, 52, 36, 49, 42, 55, 63, 32, 60, 73, 65, 84, 65, 86, 54, 45, 59, 74, 45, 41, 86, 79, 73, 115, 88, 88, 64, 86, 51, 60, 55, 76, 56, 61, 89, 92, 90, 73, 142, 144, 110, 135, 115, 81, 85, 53, 54, 75, 76, 75, 73, 79, 69, 74, 105, 94, 82, 92, 81, 111, 102, 122, 177, 193, 110, 80, 30, 76, 82, 74, 76, 72, 83, 100, 82, 98, 50, 66, 89, 66, 103, 107, 111, 116, 135, 133, 139, 149, 92, 92, 110, 143, 129, 148, 141, 146, 128, 183, 153, 106, 119, 121, 184, 129, 98, 140, 163, 202, 158, 196, 149, 195, 192, 153, 180, 154, 117, 154, 166, 165, 192, 173, 160, 145, 181, 181, 309, 279, 231, 327, 226, 194, 184, 171, 154, 176, 208, 158, 157, 192, 161, 176, 164, 236, 140, 151, 174, 136, 114, 127, 147, 139, 131, 99, 113, 4, 2, 3, 3, 25, 15, 7, 1, 3, 4, 2, 2, 1, 2, 4, 3, 3, 2, 1, 4, 1, 3, 4, 3, 5, 6, 1, 4, 2, 3, 4, 3, 2, 4, 3, 1, 2, 1, 1, 2, 1, 0, 1, 2, 2, 0, 3, 4, 1, 2, 2, 2, 7, 5, 2, 1, 1, 1, 3, 5, 1, 1, 3, 4, 2, 3, 2, 1, 1, 0, 3, 4, 3, 4, 1, 3, 3, 5, 3, 1, 0, 1, 4, 1, 3, 1, 1, 2, 3, 1, 2, 1, 0, 0, 0, 1, 1, 0, 2, 2, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, #_0]
     ;; :color (color 1)
    }, {
        :name "határon innen"
        :data [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 2, 9, 16, 22, 37, 31, 25, 43, 17, 42, 47, 29, 55, 62, 61, 49, 25, 44, 61, 52, 70, 74, 57, 47, 55, 87, 65, 59, 76, 86, 91, 75, 102, 116, 154, 124, 119, 137, 149, 153, 158, 145, 176, 178, 213, 243, 219, 250, 228, 217, 284, 275, 372, 355, 313, 375, 323, 333, 276, 241, 207, 238, 291, 252, 274, 215, 226, 232, 254, 240, 281, 270, 313, 292, 266, 288, 266, 295, 286, 205, 313, 350, 439, 368, 260, 280, 305, 339, 358, 292, 394, 368, 405, 358, 417, 368, 371, 341, 430, 368, 363, 359, 410, 368, 349, 337, 375, 355, 310, 333, 390, 334, 439, 440, 353, 368, 446, 365, 347, 354, 410, 358, 414, 400, 398, 369, 411, 596, 595, 527, 591, 546, 578, 250, 249, 209, 185, 189, 220, 198, 291, 200, 299, 258, 308, 223, 197, 230, 240, 239, 319, 270, 275, 248, 167, 113, 195, 182, 161, 166, 229, 258, 298, 308, 178, 131, 189, 193, 140, 145, 174, 161, 225, 213, 179, 256, 277, 207, 180, 214, 266, 191, 374, 275, 299, 339, 233, 281, 295, 315, 276, 288, 255, 275, 257, 325, 448, 365, 267, 287, 345, 295, 299, 294, 307, 252, 267, 394, 317, 341, 328, 389, 363, 315, 348, 376, 422, 506, 528, 414, 393, 348, 389, 318, 340, 360, 395, 357, 514, 465, 497, 420, 805, 387, 427, 291, 283, 257, 261, 333, 346, 293, 364, 331, 297, 288, 317, 370, 328, 382, 336, 363, 335, 371, 385, 443, 431, 438, 421, 498, 529, 595, 522, 452, 602, 499, 533, 563, 565, 504, 561, 612, 676, 583, 746, 620, 679, 633, 664, 561, 556, 603, 924, 728, 637, 642, 664, 681, 692, 685, 710, 579, 767, 683, 856, 834, 896, 797, 798, 829, 836, 807, 985, 861, 974, 811, 890, 1096, 954, 1150, 1071, 1026, 1322, 1464, 1985, 1369, 1508, 1413, 1310, 1322, 1352, 1403, 2331, 1923, 1579, 1128, 1074, 1138, 1129, 938, 1055, 1048, 1647, 1070, 1288, 1123, 1184, 1115, 1053, 1163, 1204, 968, 935, 847, 707, 780, 861, 1037, 1198, 1472, 1207, 1285, 1407, 1096, 1287, 1112, 1135, 1559, 1432, 1506, 1766, 1824, 2197, 1756, 1711, 1877, 1685, 1799, 1901, 1894, 1671, 1767, 2196, 2132, 2230, 2782, 2835, 2794, 2498, 2539, 2302, 1421, #_7680]
     ;; :color (color 3)
    }, {
        :name "határon túl"
        :data [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 2, 0, 1, 0, 0, 1, 0, 0, 0, 5, 1, 3, 2, 1, 3, 5, 4, 3, 8, 0, 5, 2, 2, 5, 1, 13, 3, 3, 3, 6, 1, 3, 8, 14, 17, 4, 3, 4, 1, 11, 2, 3, 5, 7, 6, 5, 2, 5, 7, 4, 7, 5, 3, 1, 8, 24, 2, 5, 10, 5, 10, 6, 17, 3, 7, 11, 8, 10, 7, 7, 12, 10, 1, 2, 3, 10, 2, 7, 7, 7, 4, 9, 11, 3, 10, 2, 4, 7, 4, 20, 15, 8, 7, 24, 9, 14, 4, 23, 13, 12, 9, 10, 10, 13, 11, 14, 8, 16, 10, 24, 5, 17, 11, 23, 21, 19, 12, 13, 6, 18, 15, 24, 27, 14, 34, 29, 24, 13, 16, 31, 13, 21, 15, 21, 14, 33, 13, 25, 32, 20, 32, 10, 8, 11, 22, 12, 26, 12, 13, 21, 32, 7, 14, 10, 15, 19, 25, 21, 27, 30, 20, 31, 22, 14, 40, 38, 19, 23, 30, 29, 37, 20, 57, 39, 40, 32, 36, 30, 23, 24, 26, 18, 44, 28, 49, 36, 43, 28, 37, 39, 47, 47, 41, 57, 61, 63, 54, 63, 67, 58, 50, 53, 82, 67, 85, 71, 61, 72, 66, 220, 105, 37, 47, 26, 35, 37, 44, 58, 60, 47, 58, 45, 48, 64, 49, 42, 62, 65, 57, 60, 69, 90, 87, 83, 87, 125, 102, 112, 111, 105, 76, 103, 100, 102, 138, 103, 85, 119, 114, 130, 129, 109, 156, 121, 130, 143, 157, 101, 150, 215, 178, 191, 152, 152, 197, 181, 181, 216, 137, 178, 240, 204, 239, 232, 204, 220, 251, 196, 218, 228, 224, 191, 137, 228, 172, 150, 220, 131, 166, 200, 170, 158, 127, 206, 243, 202, 208, 177, 197, 236, 169, 167, 172, 200, 120, 113, 105, 161, 129, 214, 113, 152, 153, 159, 125, 137, 156, 118, 80, 74, 77, 63, 78, 76, 66, 54, 111, 122, 78, 141, 95, 117, 100, 98, 124, 113, 145, 136, 126, 118, 138, 170, 129, 129, 113, 83, 61, 87, 90, 111, 69, 60, 86, 69, 81, 69, 49, 32, 25, #_44]
     ;; :color (color 2)
    }, {
        :type "pie"
        :data [
            {
                :sliced true
                :selected true
                :name "bibliográfia"
                :y 20311
             ;; :color (color 1)
            }
            {
                :name "határon innen"
                :y 215897
             ;; :color (color 3)
            }
            {
                :name "határon túl"
                :y 22768
             ;; :color (color 2)
            }
        ]
        :center [200, 200]
        :size 240
        :innerSize 48
        :shadow true
        :showInLegend false
        :tooltip {
            :headerFormat ""
            :pointFormat "<b>{point.y}</b> {point.name}: <b>{point.percentage:.2f}</b>%"
        }
        :allowPointSelect true
    }]
}))


#_(ns koraujkor.charts.mokka-r-innen)

#_(def title "Highcharts - Heatmap chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/heatmap.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -mokka-r-innen []
    (chart {:min-width "300px" :height "810px" :max-width "1300px" :margin "0 auto"}
{
    :chart {
        :type "heatmap"
    }
    :title {
        :text "Mokka-R kötetek száma gyűjtemény és megjelenési idő szerint"
    }
    :subtitle {
        :text "Évtizedenként összesítve (határon innen)"
    }
    :xAxis {
        :categories [
                                          "45", "46", "47", "48", "49"
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69"
            "70", "71", "72", "73", "74", "75", "76", "77", "78", "79"
            "80", "81", "82", "83", "84"
        ]
    }
    :yAxis {
        :title nil
        :reversed true
        :categories [
            "Batthyány-gyűjtemény"
            "BME OMIKK"
            "Csurgói Csokonai Vitéz Mihály ..." ; "Református Gimnázium Könyvtára"
            "Debreceni Egyetemi és Nemzeti Könyvtár"
            "Debreceni Református Kollégium Könyvtára"
            "ELTE Egyetemi Könyvtár"
            "Esztergomi Főszékesegyházi Könyvtár"
            "Evangélikus Hittudományi Egyetem Könyvtára"
            "FSZEK Antiqua"
            "FSZEK OPAC -1850"
            "II. Rákóczi Ferenc Megyei Könyvtár"
            "Kalocsai Főszékesegyházi Könyvtár"
            "Kecskeméti Református Egyházközség Könyvtára"
            "Klimó-gyűjtemény"
            "KSH Könyvtár"
            "Magyar Képzőművészeti Egyetem Könyvtára"
            "Magyar Országos Levéltár Könyvtára"
            "MFGI Országos Földtani Szakkönyvtár"
            "Országos Mezőgazdasági Könyvtár"
            "Országos Pedagógiai Könyvtár és Múzeum"
            "Országgyűlési Könyvtár"
            "OSZK Antiqua"
            "Piarista Központi Könyvtár"
            "PTEEK OPAC -1850"
            "Ráday Könyvtár"
            "Reguly Antal Műemlékkönyvtár"
            "Sapientia Szerzetesi Hittudományi Főiskola ..." ; "Könyvtára"
            "Somogyi-könyvtár"
            "SZIE Állatorvos-tudományi Könyvtár"
            "SZTE Egyetemi Könyvtár"
        ]
    }
    :colorAxis {
        :min 0
        :minColor "#ffffff" ; "white"
        :maxColor (color 0)
    }
    :legend {
        :align "right"
        :layout "vertical"
        :margin 10
        :verticalAlign "top"
        :y 60
        :symbolHeight 600
    }
    :tooltip {
        :formatter #(let [x (js* "this.series.xAxis.categories[this.point.x]")
                          y (js* "this.series.yAxis.categories[this.point.y]")
                          z (js* "this.point.value")]
                        (str "<i>" y "</i><br/>"
                             "<b>1" x "0</b>-<b>1" x "9</b> közötti időből<br/>"
                             "<b>" z "</b> kötet származik"))
    }
    :series [{
        :name "kötetek száma"
        :borderWidth 1
        :data [
            [7, 0, 1]
            [9, 0, 3]
            [11, 0, 3]
            [12, 0, 3]
            [13, 0, 2]
            [14, 0, 1]
            [15, 0, 4]
            [16, 0, 4]
            [18, 0, 7]
            [19, 0, 2]
            [20, 0, 16]
            [21, 0, 19]
            [22, 0, 12]
            [23, 0, 26]
            [24, 0, 27]
            [25, 0, 28]
            [26, 0, 41]
            [27, 0, 83]
            [28, 0, 117]
            [29, 0, 138]
            [30, 0, 148]
            [31, 0, 95]
            [32, 0, 52]
            [33, 0, 5]
            [35, 0, 6]
            [36, 0, 1]
            [37, 0, 5]
            [38, 0, 6]
            [39, 0, 1]

            [3, 1, 1]
            [4, 1, 3]
            [8, 1, 2]
            [9, 1, 3]
            [10, 1, 6]
            [11, 1, 3]
            [12, 1, 2]
            [13, 1, 9]
            [14, 1, 12]
            [15, 1, 16]
            [16, 1, 10]
            [17, 1, 9]
            [18, 1, 9]
            [19, 1, 9]
            [20, 1, 11]
            [21, 1, 20]
            [22, 1, 18]
            [23, 1, 40]
            [24, 1, 36]
            [25, 1, 12]
            [26, 1, 30]
            [27, 1, 38]
            [28, 1, 33]
            [29, 1, 64]
            [30, 1, 68]
            [31, 1, 94]
            [32, 1, 137]
            [33, 1, 127]
            [34, 1, 181]
            [35, 1, 19]
            [36, 1, 13]
            [37, 1, 20]
            [38, 1, 31]
            [39, 1, 40]

            [8, 2, 3]
            [9, 2, 1]
            [10, 2, 2]
            [11, 2, 11]
            [12, 2, 5]
            [13, 2, 2]
            [14, 2, 7]
            [15, 2, 10]
            [16, 2, 26]
            [17, 2, 9]
            [18, 2, 13]
            [19, 2, 18]
            [20, 2, 41]
            [21, 2, 39]
            [22, 2, 19]
            [23, 2, 38]
            [24, 2, 23]
            [25, 2, 14]
            [26, 2, 4]
            [27, 2, 16]
            [28, 2, 8]
            [29, 2, 12]
            [30, 2, 28]
            [31, 2, 24]
            [32, 2, 22]
            [33, 2, 21]
            [34, 2, 66]
            [35, 2, 43]
            [36, 2, 39]
            [37, 2, 20]
            [38, 2, 55]
            [39, 2, 52]

            [1, 3, 4]
            [2, 3, 33]
            [3, 3, 64]
            [4, 3, 78]
            [5, 3, 88]
            [6, 3, 128]
            [7, 3, 160]
            [8, 3, 130]
            [9, 3, 189]
            [10, 3, 270]
            [11, 3, 328]
            [12, 3, 328]
            [13, 3, 357]
            [14, 3, 370]
            [15, 3, 694]
            [16, 3, 694]
            [17, 3, 582]
            [18, 3, 587]
            [19, 3, 699]
            [20, 3, 1037]
            [21, 3, 920]
            [22, 3, 959]
            [23, 3, 1104]
            [24, 3, 1063]
            [25, 3, 1041]
            [26, 3, 1089]
            [27, 3, 1333]
            [28, 3, 1387]
            [29, 3, 1358]
            [30, 3, 1368]
            [31, 3, 1528]
            [32, 3, 1863]
            [33, 3, 2810]
            [34, 3, 2580]
            [35, 3, 2352]
            [36, 3, 1699]
            [37, 3, 2585]
            [38, 3, 2863]
            [39, 3, 3799]

            [1, 4, 4]
            [2, 4, 62]
            [3, 4, 100]
            [4, 4, 134]
            [5, 4, 44]
            [6, 4, 10]
            [7, 4, 4]
            [8, 4, 4]
            [9, 4, 10]
            [10, 4, 36]
            [11, 4, 74]
            [12, 4, 70]
            [13, 4, 76]
            [14, 4, 72]
            [15, 4, 98]
            [16, 4, 146]
            [17, 4, 162]
            [18, 4, 142]
            [19, 4, 174]
            [20, 4, 270]
            [21, 4, 256]
            [22, 4, 328]
            [23, 4, 306]
            [24, 4, 320]
            [25, 4, 334]
            [26, 4, 56]
            [27, 4, 6]
            [28, 4, 36]
            [29, 4, 18]
            [30, 4, 26]
            [31, 4, 16]
            [32, 4, 6]

            [5, 5, 359]
            [6, 5, 692]
            [7, 5, 499]
            [8, 5, 683]
            [9, 5, 863]
            [10, 5, 929]
            [11, 5, 1069]
            [12, 5, 1042]
            [13, 5, 1196]
            [14, 5, 1509]
            [15, 5, 669]
            [16, 5, 619]
            [17, 5, 307]
            [18, 5, 294]
            [19, 5, 308]
            [20, 5, 371]
            [21, 5, 443]
            [22, 5, 455]
            [23, 5, 484]
            [24, 5, 482]
            [25, 5, 97]
            [26, 5, 25]
            [27, 5, 13]
            [28, 5, 19]
            [29, 5, 1]
            [33, 5, 1]
            [34, 5, 1]

            [0, 6, 1]
            [1, 6, 2]
            [2, 6, 50]
            [3, 6, 92]
            [4, 6, 125]
            [5, 6, 129]
            [6, 6, 191]
            [7, 6, 118]
            [8, 6, 60]
            [9, 6, 84]
            [10, 6, 109]
            [11, 6, 80]
            [12, 6, 72]
            [13, 6, 89]
            [14, 6, 66]
            [15, 6, 128]
            [16, 6, 183]
            [17, 6, 132]
            [18, 6, 177]
            [19, 6, 154]
            [20, 6, 165]
            [21, 6, 213]
            [22, 6, 207]
            [23, 6, 230]
            [24, 6, 240]
            [25, 6, 274]
            [26, 6, 296]
            [27, 6, 389]
            [28, 6, 554]
            [29, 6, 656]
            [30, 6, 736]
            [31, 6, 941]
            [32, 6, 1153]
            [33, 6, 2609]
            [34, 6, 1888]
            [35, 6, 1387]
            [36, 6, 1264]
            [37, 6, 1448]
            [38, 6, 1309]
            [39, 6, 1388]

            [2, 7, 6]
            [3, 7, 44]
            [4, 7, 32]
            [5, 7, 54]
            [6, 7, 84]
            [7, 7, 378]
            [8, 7, 162]
            [9, 7, 114]
            [10, 7, 196]
            [11, 7, 198]
            [12, 7, 152]
            [13, 7, 156]
            [14, 7, 216]
            [15, 7, 76]
            [16, 7, 66]
            [17, 7, 54]
            [18, 7, 74]
            [19, 7, 34]
            [20, 7, 94]
            [21, 7, 80]
            [22, 7, 90]
            [23, 7, 76]
            [24, 7, 56]
            [25, 7, 56]
            [26, 7, 52]
            [27, 7, 74]
            [28, 7, 120]
            [29, 7, 128]
            [30, 7, 218]
            [31, 7, 262]
            [32, 7, 446]
            [33, 7, 502]
            [34, 7, 686]
            [35, 7, 494]
            [36, 7, 320]
            [37, 7, 562]
            [38, 7, 498]
            [39, 7, 164]

            [2, 8, 8]
            [3, 8, 14]
            [4, 8, 31]
            [5, 8, 42]
            [6, 8, 86]
            [7, 8, 149]
            [8, 8, 97]
            [9, 8, 96]
            [10, 8, 72]
            [11, 8, 93]
            [12, 8, 75]
            [13, 8, 70]
            [14, 8, 141]
            [15, 8, 14]

            [2, 9, 8]
            [3, 9, 19]
            [4, 9, 29]
            [5, 9, 40]
            [6, 9, 67]
            [7, 9, 122]
            [8, 9, 87]
            [9, 9, 94]
            [10, 9, 76]
            [11, 9, 85]
            [12, 9, 74]
            [13, 9, 68]
            [14, 9, 137]
            [15, 9, 103]
            [16, 9, 65]
            [17, 9, 98]
            [18, 9, 52]
            [19, 9, 39]
            [20, 9, 67]
            [21, 9, 149]
            [22, 9, 91]
            [23, 9, 475]
            [24, 9, 156]
            [25, 9, 199]
            [26, 9, 172]
            [27, 9, 158]
            [28, 9, 227]
            [29, 9, 295]
            [30, 9, 374]
            [31, 9, 444]
            [32, 9, 678]
            [33, 9, 1204]
            [34, 9, 1661]
            [35, 9, 1275]
            [36, 9, 1107]
            [37, 9, 1585]
            [38, 9, 2499]
            [39, 9, 3697]

            [5, 10, 1]
            [6, 10, 1]
            [7, 10, 1]
            [8, 10, 2]
            [9, 10, 4]
            [10, 10, 10]
            [11, 10, 12]
            [12, 10, 13]
            [13, 10, 16]
            [14, 10, 25]
            [15, 10, 24]
            [16, 10, 40]
            [17, 10, 32]
            [18, 10, 24]
            [19, 10, 33]
            [20, 10, 51]
            [21, 10, 57]
            [22, 10, 34]
            [23, 10, 43]
            [24, 10, 69]
            [25, 10, 51]
            [26, 10, 83]
            [27, 10, 125]
            [28, 10, 193]
            [29, 10, 267]
            [30, 10, 304]
            [31, 10, 397]
            [32, 10, 533]
            [33, 10, 654]
            [34, 10, 869]
            [35, 10, 934]
            [36, 10, 654]
            [37, 10, 767]
            [38, 10, 927]
            [39, 10, 1038]

            [1, 11, 1]
            [2, 11, 123]
            [3, 11, 165]
            [4, 11, 186]
            [5, 11, 15]
            [6, 11, 8]
            [7, 11, 2]
            [8, 11, 1]
            [11, 11, 1]

            [7, 12, 2]
            [8, 12, 3]
            [9, 12, 5]
            [10, 12, 2]
            [11, 12, 6]
            [12, 12, 7]
            [13, 12, 2]
            [14, 12, 5]
            [15, 12, 8]
            [16, 12, 8]
            [17, 12, 2]
            [18, 12, 5]
            [19, 12, 3]
            [20, 12, 9]
            [21, 12, 12]
            [22, 12, 14]
            [23, 12, 18]
            [24, 12, 16]
            [25, 12, 15]
            [26, 12, 2]
            [27, 12, 8]
            [28, 12, 11]
            [29, 12, 16]
            [30, 12, 13]
            [31, 12, 22]
            [32, 12, 21]
            [33, 12, 19]
            [34, 12, 26]
            [35, 12, 27]
            [36, 12, 19]
            [37, 12, 15]
            [38, 12, 32]
            [39, 12, 51]

            [1, 13, 2]
            [3, 13, 7]
            [4, 13, 11]
            [5, 13, 20]
            [6, 13, 22]
            [7, 13, 41]
            [8, 13, 60]
            [9, 13, 57]
            [10, 13, 70]
            [11, 13, 58]
            [12, 13, 82]
            [13, 13, 70]
            [14, 13, 73]
            [15, 13, 111]
            [16, 13, 118]
            [17, 13, 132]
            [18, 13, 118]
            [19, 13, 183]
            [20, 13, 139]
            [21, 13, 152]
            [22, 13, 184]
            [23, 13, 230]
            [24, 13, 280]
            [25, 13, 344]
            [26, 13, 411]
            [27, 13, 608]
            [28, 13, 604]
            [29, 13, 801]
            [30, 13, 820]
            [31, 13, 888]
            [32, 13, 848]
            [33, 13, 639]
            [34, 13, 459]
            [35, 13, 542]
            [36, 13, 604]
            [37, 13, 823]
            [38, 13, 599]
            [39, 13, 254]

            [10, 14, 1]
            [12, 14, 1]
            [18, 14, 1]
            [20, 14, 1]
            [21, 14, 2]
            [23, 14, 1]
            [24, 14, 3]
            [25, 14, 3]
            [26, 14, 2]
            [27, 14, 5]
            [28, 14, 12]
            [29, 14, 4]
            [30, 14, 35]
            [31, 14, 40]
            [32, 14, 59]
            [33, 14, 176]
            [34, 14, 143]
            [35, 14, 178]
            [36, 14, 148]
            [37, 14, 226]
            [38, 14, 350]
            [39, 14, 416]

            [11, 15, 1]
            [19, 15, 2]
            [24, 15, 2]
            [27, 15, 2]
            [29, 15, 2]
            [30, 15, 3]
            [31, 15, 4]
            [32, 15, 6]
            [33, 15, 16]
            [34, 15, 16]
            [35, 15, 13]
            [36, 15, 20]
            [37, 15, 21]
            [38, 15, 77]
            [39, 15, 72]

            [3, 16, 1]
            [8, 16, 1]
            [10, 16, 6]
            [11, 16, 1]
            [12, 16, 1]
            [13, 16, 1]
            [14, 16, 1]
            [17, 16, 3]
            [18, 16, 4]
            [19, 16, 3]
            [20, 16, 12]
            [21, 16, 7]
            [22, 16, 16]
            [23, 16, 35]
            [24, 16, 59]
            [25, 16, 71]
            [26, 16, 56]
            [27, 16, 62]
            [28, 16, 112]
            [29, 16, 123]
            [30, 16, 135]
            [31, 16, 182]
            [32, 16, 216]
            [33, 16, 380]
            [34, 16, 417]
            [35, 16, 480]
            [36, 16, 608]
            [37, 16, 846]
            [38, 16, 1432]
            [39, 16, 1499]

            [10, 17, 1]
            [11, 17, 6]
            [18, 17, 4]
            [19, 17, 7]
            [21, 17, 1]
            [22, 17, 2]
            [23, 17, 3]
            [24, 17, 4]
            [25, 17, 8]
            [26, 17, 5]
            [27, 17, 6]
            [28, 17, 13]
            [29, 17, 16]
            [30, 17, 19]
            [31, 17, 30]
            [32, 17, 42]
            [33, 17, 60]
            [34, 17, 42]
            [35, 17, 9]
            [36, 17, 2]
            [37, 17, 5]
            [38, 17, 2]

            [9, 18, 1]
            [12, 18, 3]
            [15, 18, 1]
            [16, 18, 2]
            [17, 18, 1]
            [21, 18, 4]
            [24, 18, 2]
            [25, 18, 1]
            [26, 18, 1]
            [27, 18, 2]
            [28, 18, 3]
            [29, 18, 1]
            [30, 18, 7]
            [31, 18, 16]
            [32, 18, 21]
            [33, 18, 32]
            [34, 18, 51]
            [35, 18, 41]
            [36, 18, 63]
            [37, 18, 88]
            [38, 18, 109]
            [39, 18, 119]

            [15, 19, 1]
            [18, 19, 1]
            [23, 19, 1]
            [25, 19, 1]
            [26, 19, 2]
            [27, 19, 3]
            [28, 19, 6]
            [29, 19, 7]
            [30, 19, 9]
            [31, 19, 11]
            [32, 19, 31]
            [33, 19, 18]
            [34, 19, 46]
            [35, 19, 27]
            [36, 19, 57]
            [37, 19, 112]
            [38, 19, 80]
            [39, 19, 176]

            [7, 20, 1]
            [9, 20, 2]
            [10, 20, 2]
            [11, 20, 7]
            [13, 20, 2]
            [14, 20, 1]
            [15, 20, 4]
            [16, 20, 5]
            [17, 20, 2]
            [18, 20, 4]
            [19, 20, 3]
            [20, 20, 7]
            [21, 20, 10]
            [22, 20, 15]
            [23, 20, 21]
            [24, 20, 9]
            [25, 20, 26]
            [26, 20, 18]
            [27, 20, 19]
            [28, 20, 36]
            [29, 20, 54]
            [30, 20, 59]
            [31, 20, 71]
            [32, 20, 99]
            [33, 20, 197]
            [34, 20, 244]
            [35, 20, 266]
            [36, 20, 196]
            [37, 20, 306]
            [38, 20, 543]
            [39, 20, 755]

            [4, 21, 2]
            [5, 21, 511]
            [6, 21, 925]
            [7, 21, 1467]
            [8, 21, 1129]
            [9, 21, 1179]
            [10, 21, 1368]
            [11, 21, 1523]
            [12, 21, 1385]
            [13, 21, 1480]
            [14, 21, 1848]
            [15, 21, 191]
            [16, 21, 7]
            [24, 21, 1]

            [2, 22, 8]
            [3, 22, 22]
            [4, 22, 16]
            [5, 22, 7]
            [6, 22, 3]
            [7, 22, 2]
            [8, 22, 3]
            [10, 22, 5]
            [12, 22, 7]
            [13, 22, 6]
            [14, 22, 37]
            [15, 22, 6]
            [16, 22, 16]
            [17, 22, 1]
            [18, 22, 10]
            [19, 22, 7]
            [20, 22, 5]
            [21, 22, 23]
            [22, 22, 15]
            [23, 22, 17]
            [24, 22, 16]
            [25, 22, 5]
            [26, 22, 11]
            [27, 22, 10]
            [28, 22, 24]
            [29, 22, 52]
            [30, 22, 64]
            [31, 22, 54]
            [32, 22, 113]
            [33, 22, 106]
            [34, 22, 128]
            [35, 22, 110]
            [36, 22, 106]
            [37, 22, 122]
            [38, 22, 153]
            [39, 22, 157]

            [4, 23, 1]
            [6, 23, 3]
            [8, 23, 6]
            [9, 23, 7]
            [10, 23, 9]
            [11, 23, 15]
            [12, 23, 13]
            [13, 23, 10]
            [14, 23, 15]
            [15, 23, 16]
            [16, 23, 20]
            [17, 23, 15]
            [18, 23, 17]
            [19, 23, 13]
            [20, 23, 30]
            [21, 23, 38]
            [22, 23, 21]
            [23, 23, 43]
            [24, 23, 57]
            [25, 23, 48]
            [26, 23, 85]
            [27, 23, 107]
            [28, 23, 137]
            [29, 23, 196]
            [30, 23, 245]
            [31, 23, 344]
            [32, 23, 397]
            [33, 23, 604]
            [34, 23, 639]
            [35, 23, 589]
            [36, 23, 529]
            [37, 23, 691]
            [38, 23, 1072]
            [39, 23, 1709]

            [1, 24, 1]
            [2, 24, 8]
            [3, 24, 10]
            [4, 24, 22]
            [5, 24, 19]
            [6, 24, 39]
            [7, 24, 45]
            [8, 24, 55]
            [9, 24, 89]
            [10, 24, 107]
            [11, 24, 84]
            [12, 24, 105]
            [13, 24, 117]
            [14, 24, 140]
            [15, 24, 159]
            [16, 24, 224]
            [17, 24, 214]
            [18, 24, 198]
            [19, 24, 248]
            [20, 24, 276]
            [21, 24, 266]
            [22, 24, 295]
            [23, 24, 412]
            [24, 24, 455]
            [25, 24, 484]
            [26, 24, 392]
            [27, 24, 499]
            [28, 24, 568]
            [29, 24, 570]
            [30, 24, 589]
            [31, 24, 724]
            [32, 24, 900]
            [33, 24, 670]
            [34, 24, 177]
            [35, 24, 52]
            [36, 24, 42]
            [37, 24, 18]
            [38, 24, 10]
            [39, 24, 6]

            [3, 25, 3]
            [4, 25, 1]
            [5, 25, 4]
            [6, 25, 3]
            [7, 25, 4]
            [8, 25, 9]
            [9, 25, 15]
            [10, 25, 20]
            [11, 25, 12]
            [12, 25, 14]
            [13, 25, 9]
            [14, 25, 6]
            [15, 25, 23]
            [16, 25, 33]
            [17, 25, 10]
            [18, 25, 19]
            [19, 25, 16]
            [20, 25, 39]
            [21, 25, 46]
            [22, 25, 33]
            [23, 25, 34]
            [24, 25, 32]
            [25, 25, 43]
            [26, 25, 47]
            [27, 25, 65]
            [28, 25, 116]
            [29, 25, 125]
            [30, 25, 141]
            [31, 25, 172]
            [32, 25, 218]
            [33, 25, 242]
            [34, 25, 262]
            [35, 25, 148]
            [36, 25, 206]
            [37, 25, 83]
            [38, 25, 70]
            [39, 25, 24]

            [5, 26, 1]
            [8, 26, 3]
            [9, 26, 9]
            [10, 26, 13]
            [11, 26, 4]
            [12, 26, 8]
            [13, 26, 4]
            [15, 26, 4]
            [16, 26, 6]
            [17, 26, 1]
            [19, 26, 5]
            [20, 26, 3]
            [21, 26, 5]
            [22, 26, 3]
            [23, 26, 4]
            [24, 26, 2]
            [25, 26, 4]
            [26, 26, 3]
            [27, 26, 3]
            [28, 26, 4]
            [29, 26, 7]
            [30, 26, 16]
            [31, 26, 27]
            [32, 26, 11]
            [33, 26, 11]
            [34, 26, 10]
            [35, 26, 4]
            [36, 26, 4]
            [37, 26, 15]
            [38, 26, 12]
            [39, 26, 34]

            [2, 27, 2]
            [3, 27, 6]
            [4, 27, 19]
            [5, 27, 10]
            [6, 27, 15]
            [7, 27, 23]
            [8, 27, 22]
            [9, 27, 34]
            [10, 27, 45]
            [11, 27, 55]
            [12, 27, 52]
            [13, 27, 64]
            [14, 27, 93]
            [15, 27, 81]
            [16, 27, 117]
            [17, 27, 131]
            [18, 27, 82]
            [19, 27, 71]
            [20, 27, 131]
            [21, 27, 183]
            [22, 27, 193]
            [23, 27, 185]
            [24, 27, 325]
            [25, 27, 268]
            [26, 27, 327]
            [27, 27, 582]
            [28, 27, 741]
            [29, 27, 947]
            [30, 27, 960]
            [31, 27, 1077]
            [32, 27, 1187]
            [33, 27, 2354]
            [34, 27, 1630]
            [35, 27, 1420]
            [36, 27, 1488]
            [37, 27, 2295]
            [38, 27, 3399]
            [39, 27, 5743]

            [11, 28, 1]
            [12, 28, 4]
            [13, 28, 3]
            [14, 28, 4]
            [15, 28, 3]
            [16, 28, 2]
            [17, 28, 3]
            [18, 28, 1]
            [19, 28, 1]
            [20, 28, 4]
            [21, 28, 3]
            [22, 28, 8]
            [23, 28, 2]
            [24, 28, 1]
            [25, 28, 6]
            [26, 28, 14]
            [27, 28, 8]
            [28, 28, 9]
            [29, 28, 11]
            [30, 28, 14]
            [31, 28, 34]
            [32, 28, 88]
            [33, 28, 116]
            [34, 28, 107]
            [35, 28, 75]
            [36, 28, 70]
            [37, 28, 113]
            [38, 28, 184]
            [39, 28, 245]

            [2, 29, 1]
            [3, 29, 5]
            [4, 29, 8]
            [5, 29, 13]
            [6, 29, 6]
            [7, 29, 14]
            [8, 29, 13]
            [9, 29, 15]
            [10, 29, 48]
            [11, 29, 50]
            [12, 29, 43]
            [13, 29, 71]
            [14, 29, 68]
            [15, 29, 125]
            [16, 29, 172]
            [17, 29, 94]
            [18, 29, 74]
            [19, 29, 176]
            [20, 29, 196]
            [21, 29, 171]
            [22, 29, 176]
            [23, 29, 185]
            [24, 29, 319]
            [25, 29, 250]
            [26, 29, 152]
            [27, 29, 222]
            [28, 29, 323]
            [29, 29, 452]
            [30, 29, 543]
            [31, 29, 606]
            [32, 29, 671]
            [33, 29, 875]
            [34, 29, 1014]
            [35, 29, 1324]
            [36, 29, 1070]
            [37, 29, 1353]
            [38, 29, 1946]
            [39, 29, 2290]
        ]
        :dataLabels {
            :enabled true
            :color "black"
            :style {
                :fontSize "9px"
                :textShadow "none"
                :HcTextStroke nil
            }
        }
    }]
}))


#_(ns koraujkor.charts.mokka-r-onnan)

#_(def title "Highcharts - Heatmap chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/heatmap.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -mokka-r-onnan []
    (chart {:min-width "300px" :height "600px" :max-width "1200px" :margin "0 auto"}
{
    :chart {
        :type "heatmap"
        :marginTop 80
    }
    :title {
        :text "Mokka-R kötetek száma gyűjtemény és megjelenési idő szerint"
    }
    :subtitle {
        :text "Évtizedenként összesítve (határon túl)"
    }
    :xAxis {
        :categories [
                                          "45", "46", "47", "48", "49"
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69"
            "70", "71", "72", "73", "74", "75", "76", "77", "78", "79"
            "80", "81", "82", "83", "84"
        ]
    }
    :yAxis {
        :title nil
        :reversed true
        :categories [
            "A kolozsvári római katolikus Lyceum ..." ; "Könyvtár"
            "Csíkszépvízi örmény könyvtár"
            "Füleki ferences könyvtár"
            "Füleki Vármúzeum"
            "Gyulafehérvári Hittudományi Főiskola ..." ; "Könyvtára"
            "Helischer könyvtár"
            "Kaplonyi ferences könyvtár"
            "Kolozsvári akadémiai könyvtár"
            "Kolozsvári Protestáns Teológiai Intézet"
            "Kolozsvári református kollégium könyvtára"
            "Nagyenyedi minorita könyvtár"
            "Nagykárolyi piarista könyvtár"
            "Nagyváradi római katolikus egyházmegyei ..." ; "könyvtár"
            "Rimaszombat"
            "Szamosújvári örmény könyvtár"
            "Zombori Kármelita Rendház Könyvtára"
        ]
    }
    :colorAxis {
        :min 0
        :minColor "#ffffff" ; "white"
        :maxColor (color 0)
    }
    :legend {
        :align "right"
        :layout "vertical"
        :margin 10
        :verticalAlign "top"
        :y 60
        :symbolHeight 480
    }
    :tooltip {
        :formatter #(let [x (js* "this.series.xAxis.categories[this.point.x]")
                          y (js* "this.series.yAxis.categories[this.point.y]")
                          z (js* "this.point.value")]
                        (str "<i>" y "</i><br/>"
                             "<b>1" x "0</b>-<b>1" x "9</b> közötti időből<br/>"
                             "<b>" z "</b> kötet származik"))
    }
    :series [{
        :name "kötetek száma"
        :borderWidth 1
        :data [
            [5, 0, 1]
            [6, 0, 4]
            [10, 0, 3]
            [12, 0, 5]
            [13, 0, 14]
            [14, 0, 8]
            [15, 0, 6]
            [16, 0, 15]
            [17, 0, 31]
            [18, 0, 18]
            [19, 0, 40]
            [20, 0, 34]
            [21, 0, 39]
            [22, 0, 32]
            [23, 0, 49]
            [24, 0, 97]
            [25, 0, 102]
            [26, 0, 6]
            [27, 0, 2]
            [30, 0, 1]

            [10, 1, 1]
            [22, 1, 2]
            [23, 1, 2]
            [24, 1, 1]
            [25, 1, 3]
            [26, 1, 1]
            [27, 1, 1]
            [28, 1, 3]
            [29, 1, 12]
            [30, 1, 11]
            [31, 1, 20]
            [32, 1, 19]
            [33, 1, 6]
            [34, 1, 8]
            [35, 1, 16]
            [36, 1, 2]
            [37, 1, 2]
            [38, 1, 1]
            [39, 1, 2]

            [4, 2, 2]
            [5, 2, 3]
            [6, 2, 2]
            [7, 2, 5]
            [8, 2, 6]
            [10, 2, 2]
            [11, 2, 1]
            [12, 2, 4]
            [13, 2, 4]
            [14, 2, 4]
            [15, 2, 10]
            [16, 2, 16]
            [17, 2, 15]
            [18, 2, 10]
            [19, 2, 7]
            [20, 2, 17]
            [21, 2, 20]
            [22, 2, 9]
            [23, 2, 25]
            [24, 2, 40]
            [25, 2, 68]
            [26, 2, 52]
            [27, 2, 95]
            [28, 2, 82]
            [29, 2, 124]
            [30, 2, 178]
            [31, 2, 169]
            [32, 2, 143]
            [33, 2, 116]
            [34, 2, 150]
            [35, 2, 178]
            [36, 2, 80]
            [37, 2, 80]
            [38, 2, 120]
            [39, 2, 39]

            [14, 3, 1]
            [15, 3, 6]
            [16, 3, 1]
            [17, 3, 5]
            [18, 3, 2]
            [19, 3, 5]
            [20, 3, 7]
            [21, 3, 6]
            [22, 3, 5]
            [23, 3, 5]
            [24, 3, 11]
            [25, 3, 7]
            [26, 3, 19]
            [27, 3, 21]
            [28, 3, 16]
            [29, 3, 31]
            [30, 3, 48]
            [31, 3, 71]
            [32, 3, 25]
            [33, 3, 28]
            [34, 3, 6]
            [35, 3, 28]
            [36, 3, 7]
            [37, 3, 7]
            [38, 3, 11]
            [39, 3, 5]

            [5, 4, 1]
            [6, 4, 12]
            [7, 4, 3]
            [8, 4, 10]
            [9, 4, 11]
            [10, 4, 18]
            [11, 4, 13]
            [12, 4, 11]
            [13, 4, 31]
            [14, 4, 19]
            [15, 4, 24]
            [16, 4, 48]
            [17, 4, 49]
            [18, 4, 24]
            [19, 4, 23]
            [20, 4, 63]
            [21, 4, 62]
            [22, 4, 57]
            [23, 4, 92]
            [24, 4, 112]
            [25, 4, 122]
            [26, 4, 181]
            [27, 4, 239]
            [28, 4, 293]
            [29, 4, 273]
            [30, 4, 386]
            [31, 4, 544]
            [32, 4, 408]
            [33, 4, 389]
            [34, 4, 267]
            [35, 4, 118]
            [36, 4, 107]
            [37, 4, 167]
            [38, 4, 51]
            [39, 4, 82]

            [13, 5, 1]
            [14, 5, 4]
            [16, 5, 1]
            [18, 5, 3]
            [19, 5, 2]
            [20, 5, 1]
            [21, 5, 2]
            [22, 5, 6]
            [23, 5, 4]
            [24, 5, 7]
            [25, 5, 12]
            [26, 5, 12]
            [27, 5, 15]
            [28, 5, 19]
            [29, 5, 26]
            [30, 5, 36]
            [31, 5, 56]
            [32, 5, 48]
            [33, 5, 149]
            [34, 5, 126]
            [35, 5, 134]
            [36, 5, 131]
            [37, 5, 90]
            [38, 5, 130]
            [39, 5, 98]

            [5, 6, 2]
            [6, 6, 1]
            [8, 6, 3]
            [9, 6, 1]
            [10, 6, 1]
            [11, 6, 3]
            [12, 6, 1]
            [13, 6, 8]
            [14, 6, 3]
            [15, 6, 10]
            [16, 6, 3]
            [17, 6, 7]
            [18, 6, 4]
            [19, 6, 3]
            [20, 6, 5]
            [21, 6, 12]
            [22, 6, 13]
            [23, 6, 29]
            [24, 6, 41]
            [25, 6, 100]
            [26, 6, 54]
            [27, 6, 49]
            [28, 6, 77]
            [29, 6, 133]
            [30, 6, 156]
            [31, 6, 221]
            [32, 6, 204]
            [33, 6, 229]
            [34, 6, 121]
            [35, 6, 89]
            [36, 6, 42]
            [37, 6, 186]
            [38, 6, 104]
            [39, 6, 78]

            [12, 7, 1]
            [14, 7, 1]
            [16, 7, 2]
            [17, 7, 1]
            [19, 7, 1]
            [20, 7, 2]
            [21, 7, 1]
            [22, 7, 1]
            [23, 7, 6]
            [24, 7, 9]
            [25, 7, 2]
            [26, 7, 1]

            [11, 8, 1]
            [15, 8, 3]
            [22, 8, 1]
            [23, 8, 3]
            [24, 8, 2]
            [25, 8, 21]
            [26, 8, 14]
            [27, 8, 33]
            [28, 8, 57]
            [29, 8, 93]
            [30, 8, 84]
            [31, 8, 118]
            [32, 8, 141]
            [33, 8, 135]
            [34, 8, 244]
            [35, 8, 58]
            [36, 8, 33]
            [37, 8, 36]
            [38, 8, 20]
            [39, 8, 29]

            [10, 9, 2]
            [11, 9, 1]
            [13, 9, 1]
            [14, 9, 4]
            [17, 9, 1]
            [18, 9, 1]
            [19, 9, 7]
            [20, 9, 15]
            [21, 9, 7]
            [22, 9, 2]
            [23, 9, 9]
            [24, 9, 9]
            [25, 9, 6]
            [26, 9, 1]

            [8, 10, 1]
            [12, 10, 2]
            [13, 10, 5]
            [14, 10, 4]
            [15, 10, 2]
            [16, 10, 5]
            [17, 10, 8]
            [18, 10, 11]
            [19, 10, 5]
            [20, 10, 5]
            [21, 10, 5]
            [22, 10, 8]
            [23, 10, 16]
            [24, 10, 25]
            [25, 10, 17]
            [26, 10, 28]
            [27, 10, 61]
            [28, 10, 80]
            [29, 10, 84]
            [30, 10, 91]
            [31, 10, 85]
            [32, 10, 62]
            [33, 10, 39]
            [34, 10, 36]
            [35, 10, 13]
            [36, 10, 10]
            [37, 10, 14]
            [38, 10, 10]
            [39, 10, 14]

            [6, 11, 1]
            [8, 11, 5]
            [9, 11, 5]
            [10, 11, 14]
            [11, 11, 14]
            [12, 11, 6]
            [13, 11, 18]
            [14, 11, 19]
            [15, 11, 26]
            [16, 11, 20]
            [17, 11, 16]
            [18, 11, 16]
            [19, 11, 18]
            [20, 11, 23]
            [21, 11, 20]
            [22, 11, 31]
            [23, 11, 69]
            [24, 11, 83]
            [25, 11, 86]
            [26, 11, 108]
            [27, 11, 267]
            [28, 11, 237]
            [29, 11, 336]
            [30, 11, 448]
            [31, 11, 555]
            [32, 11, 591]
            [33, 11, 615]
            [34, 11, 501]
            [35, 11, 640]
            [36, 11, 337]
            [37, 11, 507]
            [38, 11, 575]
            [39, 11, 166]

            [2, 12, 1]
            [3, 12, 2]
            [4, 12, 3]
            [5, 12, 28]
            [6, 12, 17]
            [7, 12, 53]
            [8, 12, 27]
            [9, 12, 48]
            [10, 12, 42]
            [11, 12, 27]
            [12, 12, 30]
            [13, 12, 50]
            [14, 12, 41]
            [15, 12, 61]
            [16, 12, 95]
            [17, 12, 80]
            [18, 12, 73]
            [19, 12, 68]
            [20, 12, 84]
            [21, 12, 149]
            [22, 12, 142]
            [23, 12, 194]
            [24, 12, 196]
            [25, 12, 69]
            [26, 12, 6]

            [14, 13, 1]
            [15, 13, 1]
            [19, 13, 1]
            [21, 13, 1]
            [22, 13, 1]
            [25, 13, 2]
            [28, 13, 1]
            [31, 13, 1]

            [8, 14, 1]
            [9, 14, 4]
            [10, 14, 1]
            [11, 14, 1]
            [12, 14, 1]
            [13, 14, 5]
            [14, 14, 1]
            [15, 14, 1]
            [16, 14, 5]
            [17, 14, 2]
            [18, 14, 2]
            [19, 14, 10]
            [20, 14, 7]
            [21, 14, 12]
            [22, 14, 13]
            [23, 14, 20]
            [24, 14, 19]
            [25, 14, 37]
            [26, 14, 29]
            [27, 14, 91]
            [28, 14, 139]
            [29, 14, 156]
            [30, 14, 280]
            [31, 14, 258]
            [32, 14, 132]
            [33, 14, 125]
            [34, 14, 71]
            [35, 14, 88]
            [36, 14, 20]
            [37, 14, 64]
            [38, 14, 30]
            [39, 14, 28]

            [14, 15, 3]
            [15, 15, 1]
            [16, 15, 3]
            [17, 15, 3]
            [18, 15, 2]
            [19, 15, 1]
            [20, 15, 4]
            [21, 15, 7]
            [22, 15, 10]
            [23, 15, 16]
            [24, 15, 13]
            [25, 15, 15]
            [26, 15, 25]
            [27, 15, 52]
            [28, 15, 41]
            [29, 15, 58]
            [30, 15, 81]
            [31, 15, 84]
            [32, 15, 74]
            [33, 15, 57]
            [34, 15, 42]
            [35, 15, 45]
            [36, 15, 30]
            [37, 15, 42]
            [38, 15, 66]
            [39, 15, 110]
        ]
        :dataLabels {
            :enabled true
            :color "black"
            :style {
                :textShadow "none"
                :HcTextStroke nil
            }
        }
    }]
}))


#_(ns koraujkor.charts.mokka-r-app)

#_(def title "Highcharts - Heatmap chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/heatmap.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -mokka-r-app []
    (chart {:min-width "300px" :height "600px" :max-width "1200px" :margin "0 auto"}
{
    :chart {
        :type "heatmap"
        :marginTop 80
    }
    :title {
        :text "Mokka-R kötetek száma gyűjtemény és megjelenési idő szerint"
    }
    :subtitle {
        :text "Évtizedenként összesítve (Apponyi, RMK, RMNy)"
    }
    :xAxis {
        :categories [
                                          "45", "46", "47", "48", "49"
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69"
            "70", "71", "72", "73", "74", "75", "76", "77", "78", "79"
            "80", "81", "82", "83", "84"
        ]
    }
    :yAxis {
        :title nil
        :reversed true
        :categories [
            "Apponyi Hungarica"
            "Régi Magyar Könyvtár I-II."
            "Régi Magyar Könyvtár III."
            "Régi magyarországi nyomtatványok"
            "RMNy „S” (előkészületben)"
        ]
    }
    :colorAxis {
        :min 0
        :minColor "#ffffff" ; "white"
        :maxColor (color 0)
    }
    :legend {
        :align "right"
        :layout "vertical"
        :margin 10
        :verticalAlign "top"
        :y 60
        :symbolHeight 480
    }
    :tooltip {
        :formatter #(let [x (js* "this.series.xAxis.categories[this.point.x]")
                          y (js* "this.series.yAxis.categories[this.point.y]")
                          z (js* "this.point.value")]
                        (str "<i>" y "</i><br/>"
                             "<b>1" x "0</b>-<b>1" x "9</b> közötti időből<br/>"
                             "<b>" z "</b> kötet származik"))
    }
    :series [{
        :name "kötetek száma"
        :borderWidth 1
        :data [
            [2, 0, 12]
            [3, 0, 16]
            [4, 0, 37]
            [5, 0, 57]
            [6, 0, 106]
            [7, 0, 115]
            [8, 0, 105]
            [9, 0, 76]
            [10, 0, 86]
            [11, 0, 133]
            [12, 0, 70]
            [13, 0, 88]
            [14, 0, 205]
            [15, 0, 90]
            [16, 0, 86]
            [17, 0, 100]
            [18, 0, 23]
            [19, 0, 36]
            [20, 0, 30]
            [21, 0, 153]
            [22, 0, 67]
            [23, 0, 690]
            [24, 0, 114]
            [25, 0, 80]
            [26, 0, 69]
            [27, 0, 26]
            [28, 0, 32]
            [29, 0, 25]
            [30, 0, 16]
            [31, 0, 24]
            [32, 0, 24]
            [33, 0, 26]
            [34, 0, 16]
            [35, 0, 9]
            [36, 0, 5]
            [37, 0, 2]
            [38, 0, 3]
            [39, 0, 1]

            [2, 1, 3]
            [3, 1, 1]
            [4, 1, 1]
            [5, 1, 4]
            [7, 1, 4]
            [8, 1, 28]
            [9, 1, 29]
            [10, 1, 63]
            [11, 1, 87]
            [12, 1, 143]
            [13, 1, 157]
            [14, 1, 210]
            [15, 1, 91]
            [16, 1, 178]
            [17, 1, 196]
            [18, 1, 282]
            [19, 1, 373]
            [20, 1, 523]
            [21, 1, 808]
            [22, 1, 742]
            [23, 1, 696]
            [24, 1, 840]
            [25, 1, 713]
            [26, 1, 70]
            [34, 1, 1]

            [2, 2, 7]
            [3, 2, 64]
            [4, 2, 75]
            [5, 2, 143]
            [6, 2, 154]
            [7, 2, 138]
            [8, 2, 107]
            [9, 2, 131]
            [10, 2, 165]
            [11, 2, 231]
            [12, 2, 220]
            [13, 2, 261]
            [14, 2, 366]
            [15, 2, 392]
            [16, 2, 539]
            [17, 2, 303]
            [18, 2, 177]
            [19, 2, 303]
            [20, 2, 530]
            [21, 2, 661]
            [22, 2, 805]
            [23, 2, 871]
            [24, 2, 763]
            [25, 2, 702]
            [26, 2, 133]
            [32, 2, 1]
            [33, 2, 1]

            [2, 3, 7]
            [3, 3, 4]
            [5, 3, 1]
            [6, 3, 1]
            [7, 3, 5]
            [8, 3, 32]
            [9, 3, 48]
            [10, 3, 87]
            [11, 3, 128]
            [12, 3, 192]
            [13, 3, 211]
            [14, 3, 246]
            [15, 3, 146]
            [16, 3, 237]
            [17, 3, 277]
            [18, 3, 390]
            [19, 3, 526]
            [20, 3, 346]

            [7, 4, 3]
            [8, 4, 3]
            [9, 4, 4]
            [10, 4, 5]
            [11, 4, 9]
            [12, 4, 17]
            [13, 4, 22]
            [14, 4, 24]
            [15, 4, 1]
        ]
        :dataLabels {
            :enabled true
            :color "black"
            :style {
                :textShadow "none"
                :HcTextStroke nil
            }
        }
    }]
}))


#_(ns koraujkor.charts.humanus-099b)

#_(def title "Highcharts - Pie chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -humanus-099b []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
        :options3d {
            :enabled true
            :alpha 50
        }
    }
    :title {
        :text "HUMANUS feldolgozó intézmények részesedése"
    }
    :subtitle {
        :text "<b>099$b</b> almező alapján (<i>OSZK/NEKTÁR</i> nélkül)"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :pie {
            :allowPointSelect true
            :cursor "pointer"
            :depth 60
            :innerSize 160
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :type "pie"
        :name "részesedése"
        :data [
         ;; [ "OSZK/NEKTÁR", 9277 ]
            {
                :name "OSZK/HBO"
                :y 3904
                :sliced true
                :selected true
            }
            [ "IKER" 795 ]
            [ "KSZK" 648 ]
            [ "feldolgozó intézményre vár" 502 ]
            [ "MATARKA" 296 ]
            [ "OPKM" 95 ]
            [ "OIK" 46 ]
            [ "A magyar irodalomtörténet bibliográfiája" 38 ]
            [ "SZTE KK" 35 ]
            [ "MTA" 18 ]
            [ "EOK/MEB" 17 ]
            [ "ELTE/AAIK" 11 ]
            [ "PTE BTKK" 10 ]
            [ "EPA" 9 ]
            [ "Illyés Gyula Megyei Könyvtár" 4 ]
            [ "Bod Péter Megyei Könyvtár (Románia)" 3 ]
            [ "BI" 2 ]
        ]
    }]
}))


#_(ns koraujkor.charts.humanus-099a)

#_(def title "Highcharts - Pie chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -humanus-099a []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
        :options3d {
            :enabled true
            :alpha 50
        }
    }
    :title {
        :text "HUMANUS feldolgozottsági szint részesedése"
    }
    :subtitle {
        :text "<b>099$a</b> almező alapján"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :pie {
            :allowPointSelect true
            :cursor "pointer"
            :depth 60
            :innerSize 160
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :type "pie"
        :name "részesedése"
        :data [
            [ "teljes" 3629 ]
            [ "válogató" 1910 ]
            [ "szórványos" 623 ]
            [ "válogató módon, ... részben feldolgozva" 15 ]
            [ "válogató, hiányosan" 9 ]
            [ "válogató, hiánytalanul" 8 ]
            [ "anyagának egy része érhető el digitalizált formában" 7 ]
            [ "teljes anyaga elérhető digitalizált formában" 5 ]
            [ "hiányosan, de részegységenként teljesen" 5 ]
            [ "digitálisan" 3 ]
            [ "teljes, hiányosan" 2 ]
            {
                :name "Humanus mutatványszám"
                :y 1
                :sliced true
                :selected true
            }
        ]
    }]
}))


#_(ns koraujkor.charts.humanus-creat8-modif5)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -humanus-creat8-modif5 []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "xy"
        :panning true
        :panKey "shift"
    }
    :title {
        :text "HUMANUS rekordok létrehozása és utolsó módosítása (2000-től)"
    }
    :subtitle {
        :text "A rekordok bejegyzései (<b>008</b>[0-3] és <b>005</b>[2-5]) alapján együttesen"
    }
    :xAxis {
        :type "datetime"
        :minRange (* 7 24 60 60 1000) ; seven days
        :dateTimeLabelFormats {
            :day "%b. %e."
        }
    }
    :yAxis {
        :allowDecimals false
        :title {
            :text "Létrehozások és módosítások havonta"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " tétel"
        :dateTimeLabelFormats {
            :month "%Y. %B"
        }
    }
    :plotOptions {
        :areaspline {
            :stacking "normal"
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series [{
        :name "létrehozások"
        :data [
            [ (date 2000 0) 388 ]
            [ (date 2000 1) 228 ]
            [ (date 2000 2) 181 ]
            [ (date 2000 3) 188 ]
            [ (date 2000 4) 162 ]
            [ (date 2000 5) 96 ]
            [ (date 2000 6) 109 ]
            [ (date 2000 7) 189 ]
            [ (date 2000 8) 146 ]
            [ (date 2000 9) 224 ]
            [ (date 2000 10) 100 ]
            [ (date 2000 11) 11 ]

            [ (date 2001 0) 87 ]
            [ (date 2001 1) 256 ]
            [ (date 2001 2) 264 ]
            [ (date 2001 3) 215 ]
            [ (date 2001 4) 131 ]
            [ (date 2001 5) 135 ]
            [ (date 2001 6) 168 ]
            [ (date 2001 7) 375 ]
            [ (date 2001 8) 166 ]
            [ (date 2001 9) 280 ]
            [ (date 2001 10) 177 ]
            [ (date 2001 11) 132 ]

            [ (date 2002 0) 440 ]
            [ (date 2002 1) 353 ]
            [ (date 2002 2) 372 ]
            [ (date 2002 3) 434 ]
            [ (date 2002 4) 713 ]
            [ (date 2002 5) 728 ]
            [ (date 2002 6) 529 ]
            [ (date 2002 7) 346 ]
            [ (date 2002 8) 486 ]
            [ (date 2002 9) 604 ]
            [ (date 2002 10) 528 ]
            [ (date 2002 11) 219 ]

            [ (date 2003 0) 752 ]
            [ (date 2003 1) 467 ]
            [ (date 2003 2) 408 ]
            [ (date 2003 3) 741 ]
            [ (date 2003 4) 504 ]
            [ (date 2003 5) 461 ]
            [ (date 2003 6) 463 ]
            [ (date 2003 7) 374 ]
            [ (date 2003 8) 492 ]
            [ (date 2003 9) 1177 ]
            [ (date 2003 10) 1431 ]
            [ (date 2003 11) 960 ]

            [ (date 2004 0) 1581 ]
            [ (date 2004 1) 1121 ]
            [ (date 2004 2) 507 ]
            [ (date 2004 3) 527 ]
            [ (date 2004 4) 713 ]
            [ (date 2004 5) 1172 ]
            [ (date 2004 6) 1190 ]
            [ (date 2004 7) 414 ]
            [ (date 2004 8) 1303 ]
            [ (date 2004 9) 1968 ]
            [ (date 2004 10) 4140 ]
            [ (date 2004 11) 267 ]

            [ (date 2005 0) 336 ]
            [ (date 2005 1) 359 ]
            [ (date 2005 2) 453 ]
            [ (date 2005 3) 826 ]
            [ (date 2005 4) 503 ]
            [ (date 2005 5) 403 ]
            [ (date 2005 6) 420 ]
            [ (date 2005 7) 366 ]
            [ (date 2005 8) 509 ]
            [ (date 2005 9) 738 ]
            [ (date 2005 10) 538 ]
            [ (date 2005 11) 230 ]

            [ (date 2006 0) 456 ]
            [ (date 2006 1) 421 ]
            [ (date 2006 2) 683 ]
            [ (date 2006 3) 645 ]
            [ (date 2006 4) 813 ]
            [ (date 2006 5) 615 ]
            [ (date 2006 6) 433 ]
            [ (date 2006 7) 564 ]
            [ (date 2006 8) 555 ]
            [ (date 2006 9) 497 ]
            [ (date 2006 10) 320 ]
            [ (date 2006 11) 281 ]

            [ (date 2007 0) 305 ]
            [ (date 2007 1) 307 ]
            [ (date 2007 2) 342 ]
            [ (date 2007 3) 1366 ]
            [ (date 2007 4) 1195 ]
            [ (date 2007 5) 1153 ]
            [ (date 2007 6) 471 ]
            [ (date 2007 7) 513 ]
            [ (date 2007 8) 1091 ]
            [ (date 2007 9) 1235 ]
            [ (date 2007 10) 3362 ]
            [ (date 2007 11) 1889 ]

            [ (date 2008 0) 2523 ]
            [ (date 2008 1) 2446 ]
            [ (date 2008 2) 2002 ]
            [ (date 2008 3) 3094 ]
            [ (date 2008 4) 116278 ]
            [ (date 2008 5) 3599 ]
            [ (date 2008 6) 2318 ]
            [ (date 2008 7) 4175 ]
            [ (date 2008 8) 2743 ]
            [ (date 2008 9) 2090 ]
            [ (date 2008 10) 1903 ]
            [ (date 2008 11) 1354 ]

            [ (date 2009 0) 2195 ]
            [ (date 2009 1) 3874 ]
            [ (date 2009 2) 3645 ]
            [ (date 2009 3) 2723 ]
            [ (date 2009 4) 3145 ]
            [ (date 2009 5) 2980 ]
            [ (date 2009 6) 3145 ]
            [ (date 2009 7) 1458 ]
            [ (date 2009 8) 4044 ]
            [ (date 2009 9) 3223 ]
            [ (date 2009 10) 4023 ]
            [ (date 2009 11) 2512 ]

            [ (date 2010 0) 5582 ]
            [ (date 2010 1) 3390 ]
            [ (date 2010 2) 3711 ]
            [ (date 2010 3) 16974 ]
            [ (date 2010 4) 9301 ]
            [ (date 2010 5) 2929 ]
            [ (date 2010 6) 3564 ]
            [ (date 2010 7) 1107 ]
            [ (date 2010 8) 3076 ]
            [ (date 2010 9) 3473 ]
            [ (date 2010 10) 2499 ]
            [ (date 2010 11) 2178 ]

            [ (date 2011 0) 4296 ]
            [ (date 2011 1) 2866 ]
            [ (date 2011 2) 2960 ]
            [ (date 2011 3) 2712 ]
            [ (date 2011 4) 4264 ]
            [ (date 2011 5) 2457 ]
            [ (date 2011 6) 4675 ]
            [ (date 2011 7) 1520 ]
            [ (date 2011 8) 3534 ]
            [ (date 2011 9) 3254 ]
            [ (date 2011 10) 3883 ]
            [ (date 2011 11) 2768 ]

            [ (date 2012 0) 4102 ]
            [ (date 2012 1) 3777 ]
            [ (date 2012 2) 2614 ]
            [ (date 2012 3) 2348 ]
            [ (date 2012 4) 2562 ]
            [ (date 2012 5) 2252 ]
            [ (date 2012 6) 1842 ]
            [ (date 2012 7) 1382 ]
            [ (date 2012 8) 1674 ]
            [ (date 2012 9) 2644 ]
            [ (date 2012 10) 2620 ]
            [ (date 2012 11) 1146 ]

            [ (date 2013 0) 4171 ]
            [ (date 2013 1) 2119 ]
            [ (date 2013 2) 2135 ]
            [ (date 2013 3) 1865 ]
            [ (date 2013 4) 1537 ]
            [ (date 2013 5) 1554 ]
            [ (date 2013 6) 1651 ]
            [ (date 2013 7) 504 ]
            [ (date 2013 8) 2238 ]
            [ (date 2013 9) 1271 ]
            [ (date 2013 10) 1833 ]
            [ (date 2013 11) 766 ]

            [ (date 2014 0) 1300 ]
            [ (date 2014 1) 1590 ]
            [ (date 2014 2) 1131 ]
            [ (date 2014 3) 821 ]
            [ (date 2014 4) 843 ]
            [ (date 2014 5) 1800 ]
            [ (date 2014 6) 1018 ]
            [ (date 2014 7) 82 ]
        ]
    }, {
        :name "módosítások"
        :data [
            [ (date 2000 0) 0 ]
            [ (date 2000 1) 0 ]
            [ (date 2000 2) 0 ]
            [ (date 2000 3) 0 ]
            [ (date 2000 4) 0 ]
            [ (date 2000 5) 1 ]
            [ (date 2000 6) 0 ]
            [ (date 2000 7) 51734 ]
            [ (date 2000 8) 0 ]
            [ (date 2000 9) 0 ]
            [ (date 2000 10) 13523 ]
            [ (date 2000 11) 2 ]

            [ (date 2001 0) 6 ]
            [ (date 2001 1) 178 ]
            [ (date 2001 2) 185 ]
            [ (date 2001 3) 157 ]
            [ (date 2001 4) 111 ]
            [ (date 2001 5) 38 ]
            [ (date 2001 6) 54 ]
            [ (date 2001 7) 183 ]
            [ (date 2001 8) 323 ]
            [ (date 2001 9) 372 ]
            [ (date 2001 10) 88 ]
            [ (date 2001 11) 42 ]

            [ (date 2002 0) 338 ]
            [ (date 2002 1) 298 ]
            [ (date 2002 2) 201 ]
            [ (date 2002 3) 361 ]
            [ (date 2002 4) 705 ]
            [ (date 2002 5) 448 ]
            [ (date 2002 6) 301 ]
            [ (date 2002 7) 250 ]
            [ (date 2002 8) 367 ]
            [ (date 2002 9) 380 ]
            [ (date 2002 10) 525 ]
            [ (date 2002 11) 295 ]

            [ (date 2003 0) 727 ]
            [ (date 2003 1) 356 ]
            [ (date 2003 2) 468 ]
            [ (date 2003 3) 663 ]
            [ (date 2003 4) 403 ]
            [ (date 2003 5) 318 ]
            [ (date 2003 6) 335 ]
            [ (date 2003 7) 387 ]
            [ (date 2003 8) 471 ]
            [ (date 2003 9) 1130 ]
            [ (date 2003 10) 1142 ]
            [ (date 2003 11) 657 ]

            [ (date 2004 0) 1631 ]
            [ (date 2004 1) 1263 ]
            [ (date 2004 2) 570 ]
            [ (date 2004 3) 501 ]
            [ (date 2004 4) 631 ]
            [ (date 2004 5) 847 ]
            [ (date 2004 6) 2535 ]
            [ (date 2004 7) 297 ]
            [ (date 2004 8) 770 ]
            [ (date 2004 9) 1431 ]
            [ (date 2004 10) 2935 ]
            [ (date 2004 11) 273 ]

            [ (date 2005 0) 177 ]
            [ (date 2005 1) 307 ]
            [ (date 2005 2) 572 ]
            [ (date 2005 3) 635 ]
            [ (date 2005 4) 567 ]
            [ (date 2005 5) 523 ]
            [ (date 2005 6) 389 ]
            [ (date 2005 7) 260 ]
            [ (date 2005 8) 452 ]
            [ (date 2005 9) 673 ]
            [ (date 2005 10) 457 ]
            [ (date 2005 11) 239 ]

            [ (date 2006 0) 324 ]
            [ (date 2006 1) 649 ]
            [ (date 2006 2) 609 ]
            [ (date 2006 3) 530 ]
            [ (date 2006 4) 697 ]
            [ (date 2006 5) 608 ]
            [ (date 2006 6) 336 ]
            [ (date 2006 7) 482 ]
            [ (date 2006 8) 488 ]
            [ (date 2006 9) 415 ]
            [ (date 2006 10) 409 ]
            [ (date 2006 11) 176 ]

            [ (date 2007 0) 368 ]
            [ (date 2007 1) 422 ]
            [ (date 2007 2) 778 ]
            [ (date 2007 3) 1455 ]
            [ (date 2007 4) 678 ]
            [ (date 2007 5) 624 ]
            [ (date 2007 6) 363 ]
            [ (date 2007 7) 325 ]
            [ (date 2007 8) 490 ]
            [ (date 2007 9) 809 ]
            [ (date 2007 10) 3383 ]
            [ (date 2007 11) 1423 ]

            [ (date 2008 0) 1587 ]
            [ (date 2008 1) 1690 ]
            [ (date 2008 2) 1259 ]
            [ (date 2008 3) 3317 ]
            [ (date 2008 4) 2463 ]
            [ (date 2008 5) 3185 ]
            [ (date 2008 6) 3233 ]
            [ (date 2008 7) 4037 ]
            [ (date 2008 8) 2637 ]
            [ (date 2008 9) 2097 ]
            [ (date 2008 10) 2677 ]
            [ (date 2008 11) 2533 ]

            [ (date 2009 0) 2153 ]
            [ (date 2009 1) 3022 ]
            [ (date 2009 2) 3078 ]
            [ (date 2009 3) 4828 ]
            [ (date 2009 4) 2850 ]
            [ (date 2009 5) 105764 ]
            [ (date 2009 6) 2967 ]
            [ (date 2009 7) 1456 ]
            [ (date 2009 8) 3939 ]
            [ (date 2009 9) 3184 ]
            [ (date 2009 10) 3417 ]
            [ (date 2009 11) 2546 ]

            [ (date 2010 0) 4598 ]
            [ (date 2010 1) 2889 ]
            [ (date 2010 2) 4005 ]
            [ (date 2010 3) 3784 ]
            [ (date 2010 4) 4252 ]
            [ (date 2010 5) 3833 ]
            [ (date 2010 6) 3082 ]
            [ (date 2010 7) 1475 ]
            [ (date 2010 8) 7358 ]
            [ (date 2010 9) 4873 ]
            [ (date 2010 10) 4057 ]
            [ (date 2010 11) 3284 ]

            [ (date 2011 0) 4418 ]
            [ (date 2011 1) 4867 ]
            [ (date 2011 2) 4901 ]
            [ (date 2011 3) 4248 ]
            [ (date 2011 4) 5425 ]
            [ (date 2011 5) 7537 ]
            [ (date 2011 6) 5343 ]
            [ (date 2011 7) 3290 ]
            [ (date 2011 8) 6366 ]
            [ (date 2011 9) 5563 ]
            [ (date 2011 10) 6374 ]
            [ (date 2011 11) 3237 ]

            [ (date 2012 0) 5202 ]
            [ (date 2012 1) 4818 ]
            [ (date 2012 2) 3123 ]
            [ (date 2012 3) 2572 ]
            [ (date 2012 4) 3349 ]
            [ (date 2012 5) 2772 ]
            [ (date 2012 6) 1837 ]
            [ (date 2012 7) 3766 ]
            [ (date 2012 8) 2751 ]
            [ (date 2012 9) 3856 ]
            [ (date 2012 10) 3045 ]
            [ (date 2012 11) 1608 ]

            [ (date 2013 0) 2146 ]
            [ (date 2013 1) 3468 ]
            [ (date 2013 2) 2868 ]
            [ (date 2013 3) 2253 ]
            [ (date 2013 4) 1952 ]
            [ (date 2013 5) 2238 ]
            [ (date 2013 6) 2536 ]
            [ (date 2013 7) 596 ]
            [ (date 2013 8) 2691 ]
            [ (date 2013 9) 2277 ]
            [ (date 2013 10) 2628 ]
            [ (date 2013 11) 1117 ]

            [ (date 2014 0) 1751 ]
            [ (date 2014 1) 2163 ]
            [ (date 2014 2) 1463 ]
            [ (date 2014 3) 1444 ]
            [ (date 2014 4) 1750 ]
            [ (date 2014 5) 3741 ]
            [ (date 2014 6) 2489 ]
            [ (date 2014 7) 348 ]
        ]
    }]
}))


#_(ns koraujkor.charts.humanus-creat-modif)

#_(def title "Highcharts - Area chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -humanus-creat-modif []
    (chart {:min-width "300px" :height "750px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "areaspline"
        :zoomType "xy"
        :panning true
        :panKey "shift"
    }
    :title {
        :text "HUMANUS rekordok létrehozása és módosítása (2009-től)"
    }
    :subtitle {
        :text "A szerkesztési napló alapján együttesen"
    }
    :xAxis {
        :type "datetime"
        :minRange (* 7 24 60 60 1000) ; seven days
        :dateTimeLabelFormats {
            :day "%b. %e."
        }
    }
    :yAxis {
        :allowDecimals false
        :title {
            :text "Létrehozások és módosítások havonta"
        }
        :labels {
            :formatter #(js* "this.value")
        }
    }
    :tooltip {
        :shared true
        :valueSuffix " tétel"
        :dateTimeLabelFormats {
            :month "%Y. %B"
        }
    }
    :plotOptions {
        :areaspline {
            :stacking "normal"
            :marker {
                :enabled false
                :symbol "circle"
                :radius 2
                :states {
                    :hover {
                        :enabled true
                    }
                }
            }
        }
    }
    :series [{
        :name "létrehozások"
        :data [
            [ (date 2009 0) 1737 ]
            [ (date 2009 1) 2793 ]
            [ (date 2009 2) 2715 ]
            [ (date 2009 3) 1961 ]
            [ (date 2009 4) 1956 ]
            [ (date 2009 5) 1914 ]
            [ (date 2009 6) 2034 ]
            [ (date 2009 7) 908 ]
            [ (date 2009 8) 2691 ]
            [ (date 2009 9) 2770 ]
            [ (date 2009 10) 3110 ]
            [ (date 2009 11) 2240 ]

            [ (date 2010 0) 3677 ]
            [ (date 2010 1) 2581 ]
            [ (date 2010 2) 2570 ]
            [ (date 2010 3) 2205 ]
            [ (date 2010 4) 2525 ]
            [ (date 2010 5) 2106 ]
            [ (date 2010 6) 1932 ]
            [ (date 2010 7) 831 ]
            [ (date 2010 8) 2614 ]
            [ (date 2010 9) 2146 ]
            [ (date 2010 10) 2006 ]
            [ (date 2010 11) 1764 ]

            [ (date 2011 0) 2617 ]
            [ (date 2011 1) 2233 ]
            [ (date 2011 2) 2162 ]
            [ (date 2011 3) 1864 ]
            [ (date 2011 4) 2226 ]
            [ (date 2011 5) 1617 ]
            [ (date 2011 6) 1521 ]
            [ (date 2011 7) 744 ]
            [ (date 2011 8) 2369 ]
            [ (date 2011 9) 2313 ]
            [ (date 2011 10) 2514 ]
            [ (date 2011 11) 1614 ]

            [ (date 2012 0) 2869 ]
            [ (date 2012 1) 2775 ]
            [ (date 2012 2) 1597 ]
            [ (date 2012 3) 1110 ]
            [ (date 2012 4) 1403 ]
            [ (date 2012 5) 1559 ]
            [ (date 2012 6) 929 ]
            [ (date 2012 7) 442 ]
            [ (date 2012 8) 1084 ]
            [ (date 2012 9) 1656 ]
            [ (date 2012 10) 1651 ]
            [ (date 2012 11) 707 ]

            [ (date 2013 0) 1141 ]
            [ (date 2013 1) 1228 ]
            [ (date 2013 2) 1296 ]
            [ (date 2013 3) 903 ]
            [ (date 2013 4) 876 ]
            [ (date 2013 5) 946 ]
            [ (date 2013 6) 1036 ]
            [ (date 2013 7) 316 ]
            [ (date 2013 8) 868 ]
            [ (date 2013 9) 676 ]
            [ (date 2013 10) 973 ]
            [ (date 2013 11) 366 ]

            [ (date 2014 0) 592 ]
            [ (date 2014 1) 735 ]
            [ (date 2014 2) 554 ]
            [ (date 2014 3) 467 ]
            [ (date 2014 4) 396 ]
            [ (date 2014 5) 925 ]
        ]
    }, {
        :name "módosítások"
        :data [
            [ (date 2009 0) 1191 ]
            [ (date 2009 1) 1444 ]
            [ (date 2009 2) 2331 ]
            [ (date 2009 3) 2360 ]
            [ (date 2009 4) 2821 ]
            [ (date 2009 5) 3215 ]
            [ (date 2009 6) 2465 ]
            [ (date 2009 7) 1401 ]
            [ (date 2009 8) 3162 ]
            [ (date 2009 9) 2466 ]
            [ (date 2009 10) 2389 ]
            [ (date 2009 11) 2265 ]

            [ (date 2010 0) 3464 ]
            [ (date 2010 1) 2847 ]
            [ (date 2010 2) 4866 ]
            [ (date 2010 3) 4348 ]
            [ (date 2010 4) 4932 ]
            [ (date 2010 5) 4490 ]
            [ (date 2010 6) 2992 ]
            [ (date 2010 7) 1825 ]
            [ (date 2010 8) 4633 ]
            [ (date 2010 9) 5336 ]
            [ (date 2010 10) 5343 ]
            [ (date 2010 11) 3137 ]

            [ (date 2011 0) 4913 ]
            [ (date 2011 1) 5397 ]
            [ (date 2011 2) 6292 ]
            [ (date 2011 3) 5145 ]
            [ (date 2011 4) 5896 ]
            [ (date 2011 5) 7524 ]
            [ (date 2011 6) 4169 ]
            [ (date 2011 7) 3116 ]
            [ (date 2011 8) 5578 ]
            [ (date 2011 9) 5012 ]
            [ (date 2011 10) 5633 ]
            [ (date 2011 11) 2467 ]

            [ (date 2012 0) 3094 ]
            [ (date 2012 1) 2615 ]
            [ (date 2012 2) 2765 ]
            [ (date 2012 3) 2295 ]
            [ (date 2012 4) 3218 ]
            [ (date 2012 5) 2130 ]
            [ (date 2012 6) 1736 ]
            [ (date 2012 7) 3524 ]
            [ (date 2012 8) 2604 ]
            [ (date 2012 9) 2461 ]
            [ (date 2012 10) 2122 ]
            [ (date 2012 11) 1162 ]

            [ (date 2013 0) 1759 ]
            [ (date 2013 1) 2372 ]
            [ (date 2013 2) 1889 ]
            [ (date 2013 3) 1802 ]
            [ (date 2013 4) 1565 ]
            [ (date 2013 5) 1486 ]
            [ (date 2013 6) 1918 ]
            [ (date 2013 7) 477 ]
            [ (date 2013 8) 2248 ]
            [ (date 2013 9) 1499 ]
            [ (date 2013 10) 2245 ]
            [ (date 2013 11) 991 ]

            [ (date 2014 0) 1283 ]
            [ (date 2014 1) 1645 ]
            [ (date 2014 2) 1017 ]
            [ (date 2014 3) 1063 ]
            [ (date 2014 4) 969 ]
            [ (date 2014 5) 2448 ]
        ]
    }]
}))


#_(ns koraujkor.charts.pie-pie)

#_(def title "Highcharts - Pie drilldown chart")

#_(def require ["http://code.jquery.com/jquery-1.11.1.min.js"
              "http://code.highcharts.com/highcharts.js"
              "http://code.highcharts.com/highcharts-3d.js"
              "http://code.highcharts.com/modules/drilldown.js"
              "http://code.highcharts.com/modules/exporting.js"])

(defn -pie-pie []
    (chart {:min-width "300px" :height "770px" :max-width "900px" :margin "0 auto"}
{
    :chart {
        :type "pie"
        :options3d {
            :enabled true
            :alpha 50
        }
    }
    :title {
        :text "A jegyzékek száma keletkezési hely szerint"
    }
    :subtitle {
        :text "Kattints a földrajzi egységre: jegyzékek száma települések szerint"
    }
    :tooltip {
        :pointFormat "{series.name}: <b>{point.percentage:.2f}%</b>"
    }
    :plotOptions {
        :series {
            :allowPointSelect true
            :cursor "pointer"
            :depth 60
            :innerSize 160
            :dataLabels {
                :enabled true
                :format "<b>{point.name}</b>: {point.y}"
                :connectorColor "silver"
            }
        }
    }
    :colors gradient2
    :series [{
        :name "részesedése"
        :colorByPoint true
        :data (psv-data psv4)
    }]
    :drilldown {
        :series (psv-series psv4)
    }
}))


