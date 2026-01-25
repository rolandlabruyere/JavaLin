-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server versie:                8.0.41 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Versie:              12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Databasestructuur van trafoBuildDatabase wordt geschreven
CREATE DATABASE IF NOT EXISTS `trafoBuildDatabase` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `trafoBuildDatabase`;

-- Structuur van  tabel trafoBuildDatabase.tb100_htmlpaginas wordt geschreven
CREATE TABLE IF NOT EXISTS `tb100_htmlpaginas` (
  `Id` varchar(50) NOT NULL,
  `TextPlaceHolder` varchar(25) DEFAULT NULL,
  `PricePlaceholder` varchar(25) DEFAULT NULL,
  `ImagePlaceHolderBig` varchar(25) DEFAULT NULL,
  `ImagePlaceHolderThumbNailBar` varchar(25) DEFAULT NULL,
  `InlineHtml` longtext,
  PRIMARY KEY (`Id`),
  KEY `tb100_htmlpages_idx` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumpen data van tabel trafoBuildDatabase.tb100_htmlpaginas: ~16 rows (ongeveer)
INSERT INTO `tb100_htmlpaginas` (`Id`, `TextPlaceHolder`, `PricePlaceholder`, `ImagePlaceHolderBig`, `ImagePlaceHolderThumbNailBar`, `InlineHtml`) VALUES
	('cartFlexBox', '$orderLineDetail', NULL, '$orderLineImage', NULL, '<div class="cartOrderLineDiv">\n     <div class="cartPictureDiv">\n	<img class="cartThumbNail" src="$orderLineImage"/>\n     </div>\n     <div class="cartOrderDetailDiv">\n	$orderLineDetail\n    </div>\n</div>\n'),
	('cartFlexBoxContainer', '$cartFlexBoxContainer', NULL, NULL, NULL, '<link rel="stylesheet" type="text/css" href="css/cartFlexBox.css"/>\r\n      <div class="cartContainer">\r\n           $cartFlexBoxContainer\r\n      </div>\r\n</div>'),
	('cartOrderLine', '$orderDescription', '$orderNumOfItems', '$orderLineAmount', '$orderLineSubtotal', '<table>\r\n	<tr>\r\n		<th class="thText">artikel omschrijving</th>\r\n		<th class="thNumber">aantal</th>\r\n		<th class="thAmount">prijs</th>\r\n		<th class="thAmount">orderbedrag</th>\r\n	</tr>\r\n	<tr>\r\n		<td class="tdText">$orderDescription</td>\r\n		<td class="tdNumber" onclick="alert(\'wijzigen aantal\')">$orderNumOfItems</td>\r\n		<td class="tdAmount">$orderLineAmount</td>\r\n		<td class="tdAmount">$orderLineSubtotal</td>\r\n	</tr>\r\n</table>'),
	('DetailPage', '$textArea', '$pricing', '$zoomImage', '$thumbNailBar', '<link rel="stylesheet" type="text/css" href="css/detailpage.css" />\n<div class="wrapper" id="wrapper">\n	<div id="imageDiv" class="imageDiv">\n		<div id="imageCont">$zoomImage</div>\n		<div class="thumbNailBar">\n			$thumbNailBar\n		</div>\n	</div>\n	<div id="textDiv" class="textDiv">\n		<div id="textHeaderDiv" class="textHeaderDiv">Header tekst</div>\n		<div id="retailPrice" class="retailPrice">$pricing</div>\n		<div id="textCostsDiv" class="textCostsDiv">\n		  <p>Prijzen zijn inclusief BTW, exclusief verzendkosten\n		  <br>\n			Gratis verzending voor binnenlandse bestellingen van meer dan € 50\n		  </p>		\n<!--		</div>	\n		<div class="number">-->\n			<span onclick="minusClick();" class="minus">-</span>\n			<input onkeyup="checkValue();" id="itemCount" type="text" value="1"/>\n			<span onclick="plusClick();" class="plus">+</span>\n			<button id="addCart" class="addCart" onclick="placeItemsInCartClick(id);">in winkelwagen</button>\n		  <p>\n			Standaard verzending 2 - 5 werkdagen.\n		  </p>		\n		</div>\n		<div class="textAreaDiv" id="textAreaDiv">$textArea</div>\n    </div>\n</div>\n'),
	('indexPageCandleHolders', NULL, NULL, NULL, NULL, '<link rel="stylesheet" type="text/css" href="css/flexbox.css"/>\r\n<div class="wrapper" >\r\n  <h1 class="h1Title">Overzicht kandelaars en standaards</h1>\r\n    <div class="container">\r\n        <div  class="pictureDiv" id="indexPageCandleHolders" onclick = "onReverseStringClick(id);">\r\n            <img class="thumbNail" src="images/indexpages/page3/kandelaarHexagon.jpg"/>\r\n            <br>\r\n            <text class="titles" >kandelaars en standaards (paraffine)</text>\r\n        </div>\r\n        <div  class="pictureDiv" id="indexPageCandleHoldersConcrete" onclick = "onReverseStringClick(id);">\r\n            <img class="thumbNail" src="images/indexpages/page3/kandelaarBetonGroot.jpg"/>\r\n            <br>\r\n            <text class="titles">kandelaars en standaards (beton)</text>\r\n        </div>\r\n        <div  class="pictureDiv" id="indexPageCandleHoldersEpoxy" onclick = "onReverseStringClick(id);">\r\n            <img class="thumbNail" src="images/indexpages/page3/kandelaarGeplooidEpoxy.jpg"/>\r\n            <br>\r\n            <text class="titles" >kandelaars en standaards (epoxy)</text> \r\n        </div>\r\n    </div>\r\n</div>\r\n'),
	('indexPageDinerCandle', NULL, NULL, NULL, NULL, '<div  id="200" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/gedraaidekaarsOmbre.jpg"/>\r\n	<br>\r\n		<text class="titles">gedraaide kaars ombré</text>\r\n	</br>\r\n</div>\r\n<div id="201" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/gedraaideKaarsEffen.jpg"/>\r\n	<br>\r\n		<text class="titles">gedraaide kaars effen</text>\r\n	</br>\r\n</div>\r\n<div id="202" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/gedraaideKaarsLuxe.jpg"/>\r\n	<br>\r\n		<text class="titles">gedraaide kaars luxe</text>\r\n	</br>\r\n</div>\r\n<div id="203" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/confettiKaarsOmbre.jpg"/>\r\n	<br>\r\n		<text class="titles">confetti kaars ombré</text>\r\n	</br>\r\n</div>\r\n<div id="204" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/confettiKaarsEffen.jpg"/>\r\n	<br>\r\n		<text class="titles">confetti kaars effen</text>\r\n	</br>\r\n</div>\r\n<div id="205" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/slankeRibbelKaars.jpg"/>\r\n	<br>\r\n		<text class="titles">slanke ribbelkaars</text>\r\n	</br>\r\n</div>\r\n<div id="206" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/gedraaideKaarsBreed.jpg"/>\r\n	<br>\r\n		<text class="titles">gedraaide kaars<br>breed</text>\r\n	</br>\r\n</div>\r\n<div id="207" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/gedraaideKaarsSmal.jpg"/>\r\n	<br>\r\n		<text class="titles">gedraaide kaars<br>smal</text>\r\n	</br>\r\n</div>\r\n<div id="208" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/gedraaideKaarsFijneSpiraal.jpg"/>\r\n	<br>\r\n		<text class="titles">gedraaide kaars<br>fijne spiraal</text>\r\n	</br>\r\n</div>\r\n<div id="209" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/potloodKaarsEffenOmbre.jpg"/>\r\n	<br>\r\n		<text class="titles">potloodkaars<br>effen en ombré</text>\r\n	</br>\r\n</div>\r\n<div id="210" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/setKaarsenOmbre.jpg"/>\r\n	<br>\r\n		<text class="titles">set kaarsen<br>ombré</text>\r\n	</br>\r\n</div>\r\n<div id="211" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/kaarsGevlekt.jpg"/>\r\n	<br>\r\n		<text class="titles">kaars<br>gevlekt</text>\r\n	</br>\r\n</div>\r\n<div id="212" onclick="getPage(id);">\r\n	<img class="thumbNail" src="images/indexpages/page2/kandelaar.jpg"/>\r\n	<br>\r\n		<text class="titles">kandelaar</text>\r\n	</br>\r\n</div>\r\n'),
	('indexPageKaars', NULL, NULL, NULL, NULL, '<div  class="pictureDiv" id="indexPageDinerCandle" onclick="getPage(id);">\r\n    <img class="thumbNail" src="images/Dinerkaarsen en of met Kandelaars/Gedraaide kaarsen 20 cm/20230424_175020.jpg"/>\r\n    <br>\r\n    <text class="titles" >dinerkaarsen</text>\r\n</div>\r\n<div  class="pictureDiv" id="indexPageCandleHolders" onclick="onReverseStringClick(id);">\r\n    <img class="thumbNail" src="images/Kerstkaarsen/ElandKaars2D/20211222_102913.jpg"/>\r\n    <br>\r\n    <text class="titles">feestdagen</text>\r\n</div>\r\n<div  class="pictureDiv" id="indexPageWindLights" onclick="onReverseStringClick(id);">\r\n    <img class="thumbNail" src="images/indexpages/page1/windlicht.jpg"/>\r\n    <br>\r\n    <text class="titles" >windlicht</text> \r\n</div>\r\n<div  class="pictureDiv">\r\n    <img class="thumbNail" src="images/indexpages/page1/bolkaars.jpg"/>\r\n    <br>\r\n    <text class="titles" >geometrie</text>\r\n</div>\r\n<div  class="pictureDiv">\r\n    <img class="thumbNail" src="images/indexpages/page1/decoratievekaarsen.jpg"/>\r\n    <br>\r\n    <text class="titles" >decoratief</text>\r\n</div>\r\n<div  class="pictureDiv">\r\n    <img class="thumbNail" src="images/indexpages/page1/maletorso.jpg"/>\r\n    <br>\r\n    <text class="titles" >sculptuur</text>\r\n</div>\r\n<div  class="pictureDiv">\r\n    <img class="thumbNail" src="images/indexpages/page1/lotusbloem.jpg"/>\r\n    <br>\r\n    <text class="titles" >flora</text>\r\n</div>\r\n<div  class="pictureDiv">\r\n<img class="thumbNail" src="images/indexpages/page1/lion.jpg"/>\r\n    <br>\r\n    <text class="titles" >fauna</text>\r\n</div>\r\n<div  class="pictureDiv">\r\n    <img class="thumbNail" src="images/indexpages/page1/themakaars.jpg"/>\r\n    <br>\r\n    <text class="titles" >thema</text>\r\n</div>\r\n<div  class="pictureDiv">\r\n    <img class="thumbNail" src="images/Klassieke Kaarsen/Borstbeeld David groot/20230728_groter.jpg"/>\r\n    <br>\r\n    <text class="titles" >klassiek</text>\r\n</div>\r\n<div  class="pictureDiv">\r\n    <img class="thumbNail" src="images/Tuinkaarsen/Betonnen pot gevuld met kaars/20210817_171420.jpg"/>\r\n    <br>\r\n    <text class="titles" >tuin</text>\r\n</div>\r\n<div  class="pictureDiv">\r\n    <img class="thumbNail" src="images/Boeddha kaarsen/Boeddha met vier gezichten met platte bovenkant/20210818_1152182.jpg"/>\r\n    <br>\r\n    <text class="titles" >zen</text>\r\n</div>\r\n'),
	('indexPageWindLights', NULL, NULL, NULL, NULL, '<link rel="stylesheet" type="text/css" href="css/flexbox.css"/>\r\n<div class="wrapper">\r\n<div class="thumbNail">\r\n  <h1 class="h1Title">Windlichten</h1>\r\n</div>\r\n  <div class="container">\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichtbol20cm.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht bol<br>maat 20cm</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichtbolhortensia.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht bol<br>hortensia</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichtbolblad.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht bol<br>blad</text> \r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichttijm.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht<br>tijm</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichtfolie.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht<br>folie</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichtdiamant.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht diamant<br>hortensia</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichtharmonica.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht<br>harmonica</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n    <img class="thumbNail" src="images/indexpages/page4/windlichtfolie-xs.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht<br>folie maat xs</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichtzee-s.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht zee<br>maat small</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichttzee-m.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht zee<br>maat medium</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichttzee-l.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht zee<br>maat large</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichttvlam-l.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht gevlamd<br>maat large</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichtthoek-m.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht, vierkant<br>maat medium</text>\r\n    </div>\r\n    <div class="pictureDiv">\r\n      <img class="thumbNail" src="images/indexpages/page4/windlichttbos-xs.jpg"/>\r\n      <br>\r\n      <text class="titles">windlicht bos<br>maat extra small</text>\r\n    </div>\r\n  </div>\r\n</div>\r\n'),
	('logo', NULL, NULL, NULL, NULL, '<div  class="pictureDiv" id="indexPageDinerCandle" onclick="onReverseStringClick(id);">\r\n    <img class="logoNail" src="images/diversen/logo-groen.jpg"/>\r\n</div>\r\n'),
	('shoppingCartItems', '$shoppingCartItems', NULL, NULL, NULL, '<a><span id="shoppingCart" onmouseover="checkCartItems(id);">Winkelwagen<span></a> \r\n<div class="sub-menu-block">$shoppingCartItems</div>		\r\n'),
	('slideshow', '$placeHolderSlides', '$placeHolderDots', '$imagePlaceHolder', NULL, '<div><div class="slideshow-container">$placeHolderSlides</div><div style="text-align:center">$placeHolderDots</div></div>|<div class="mySlides fade"><img src="$imagePlaceHolder" style="height:790px;width:auto"></div>|<span class="dot"/>'),
	('splashPage', '', NULL, '', NULL, '<div id="wrapper">\r\n	<div id="headerDiv"/>\r\n	<div id="textDiv">\r\n		<div id="controlDiv">\r\n			<div id="logoDiv"><img src="images/diversen/logo-groen.jpg"/></div>\r\n			<div id="textHeaderDiv" class="textHeaderDiv"><br>handgemaakte<br>producten.</div>\r\n		</div>\r\n	</div>\r\n</div>'),
	('splashPageInfo', NULL, NULL, NULL, NULL, '<div><div class="slideshow-container"><div class="mySlides fade"><img src="images\\Slideshow\\120210822_122351.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\120211104_120648.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\120211108_131152.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\120211108_133338.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\120211220_115808.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20210724_153919.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20210818_115115.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20210820_123301.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20210913_124017.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20210913_124109.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20211222_125211.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20230601_090535.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20230808_142312.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20230808_142912.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20230808_143353.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20230808_143622.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20230808_143926.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20230808_144016.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20230808_144744.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20231022_152930.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20240807_132139.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20240809_141519.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20240826_131751.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20241218_113117.jpg" style="height:790px;width:auto"></div>\n<div class="mySlides fade"><img src="images\\Slideshow\\20250115_103920.jpg" style="height:790px;width:auto"></div>\n</div><div style="text-align:center"><span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n<span class="dot"/>\n</div></div>'),
	('splashPageInfo_old', NULL, NULL, NULL, NULL, '<div>\r\n	<div/>\r\n	<div>\r\n		<div id="controlDiv">\r\n			<img class="imgSplash" src="images/diversen/splash.jpg" alt="Windlicht">\r\n		</div>\r\n	</div>\r\n</div>'),
	('splashPageInfo_old2', NULL, NULL, NULL, NULL, '<div>\r\n	<div class="slideshow-container">\r\n		<div class="mySlides fade">\r\n			<div class="numbertext">1 / 3</div>\r\n			<img src="images/Art Deco Kaarsen/Symmetrie Relief Kaars/trapeze kaars midden hoog rozefrosteffect.jpg" style="width:100%">\r\n				<div class="text">Caption Text</div>\r\n			</div>\r\n			<div class="mySlides fade">\r\n				<div class="numbertext">2 / 3</div>\r\n				<img src="images/Haakwerken/Knuffels/Schaap/20230808_142312.jpg" style="width:100%">\r\n					<div class="text">Caption Two</div>\r\n				</div>\r\n				<div class="mySlides fade">\r\n					<div class="numbertext">3 / 3</div>\r\n					<img src="images/Kunstmarkt Heiloo/20230624_124406.jpg" style="width:100%">\r\n						<div class="text">Caption Three</div>\r\n					</div>\r\n				</div>\r\n				<br>\r\n					<div style="text-align:center">\r\n						<span class="dot"/>\r\n						<span class="dot"/>\r\n						<span class="dot"/>\r\n					</div>\r\n				</div>\r\n			</br>\r\n		</div>\r\n	</div>		'),
	('splashPageMenu', NULL, NULL, NULL, NULL, 'vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu vertikaal menu  ');

-- Structuur van  tabel trafoBuildDatabase.tb120_tekstblokken wordt geschreven
CREATE TABLE IF NOT EXISTS `tb120_tekstblokken` (
  `Rubriek` varchar(50) NOT NULL,
  `ItemNaam` varchar(50) NOT NULL,
  `Omschrijving` mediumtext,
  `ProductInfo` mediumtext,
  PRIMARY KEY (`Rubriek`,`ItemNaam`) USING BTREE,
  KEY `tb120_textblocks_idx` (`Rubriek`,`ItemNaam`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Structuur van  tabel trafoBuildDatabase.tb230_draadmetrisch wordt geschreven
CREATE TABLE IF NOT EXISTS `tb230_draadmetrisch` (
  `Diameter` decimal(4,2) NOT NULL,
  `Oppervlak` decimal(5,3) DEFAULT NULL,
  `MaxAmp` decimal(4,2) DEFAULT NULL,
  PRIMARY KEY (`Diameter`),
  KEY `MaxAmp` (`MaxAmp`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumpen data van tabel trafoBuildDatabase.tb230_draadmetrisch: ~30 rows (ongeveer)
INSERT INTO `tb230_draadmetrisch` (`Diameter`, `Oppervlak`, `MaxAmp`) VALUES
	(0.05, 0.002, 0.02),
	(0.08, 0.005, 0.06),
	(0.10, 0.008, 0.09),
	(0.12, 0.011, 0.14),
	(0.15, 0.018, 0.21),
	(0.18, 0.026, 0.31),
	(0.20, 0.031, 0.38),
	(0.25, 0.049, 0.59),
	(0.30, 0.071, 0.85),
	(0.35, 0.096, 1.15),
	(0.40, 0.126, 1.50),
	(0.45, 0.159, 1.90),
	(0.50, 0.196, 2.35),
	(0.55, 0.238, 2.85),
	(0.60, 0.283, 3.40),
	(0.65, 0.332, 4.00),
	(0.70, 0.385, 4.60),
	(0.75, 0.442, 5.30),
	(0.80, 0.503, 6.00),
	(0.90, 0.686, 7.00),
	(1.00, 0.785, 9.40),
	(1.10, 0.950, 11.40),
	(1.20, 1.000, 13.60),
	(1.30, 1.000, 15.90),
	(1.40, 2.000, 18.50),
	(1.50, 2.000, 21.20),
	(1.60, 2.000, 24.10),
	(1.80, 3.000, 30.50),
	(2.00, 3.000, 37.70),
	(2.50, 5.000, 59.00);

-- Structuur van  tabel trafoBuildDatabase.tb240_draadawg wordt geschreven
CREATE TABLE IF NOT EXISTS `tb240_draadawg` (
  `AWG` int NOT NULL DEFAULT '0',
  `diameter` decimal(6,4) NOT NULL,
  `oppervlak` decimal(6,4) NOT NULL DEFAULT '0.0000',
  `maxAmps` decimal(3,1) NOT NULL DEFAULT '0.0',
  PRIMARY KEY (`AWG`),
  KEY `maxAmps` (`maxAmps`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumpen data van tabel trafoBuildDatabase.tb240_draadawg: ~19 rows (ongeveer)
INSERT INTO `tb240_draadawg` (`AWG`, `diameter`, `oppervlak`, `maxAmps`) VALUES
	(10, 2.5582, 5.1400, 38.5),
	(11, 2.3048, 4.1720, 31.3),
	(12, 2.0525, 3.3090, 24.8),
	(13, 1.8276, 2.6230, 19.7),
	(14, 1.6277, 2.0810, 15.6),
	(15, 1.4495, 1.6500, 12.4),
	(16, 1.2908, 1.3090, 9.8),
	(17, 1.1495, 1.0380, 7.8),
	(18, 1.0237, 0.8230, 6.2),
	(19, 0.9116, 0.6530, 4.9),
	(20, 0.8118, 0.5180, 3.9),
	(21, 0.7229, 0.4100, 3.1),
	(22, 0.6438, 0.3260, 2.4),
	(23, 0.5733, 0.2580, 1.9),
	(24, 0.5106, 0.2050, 1.5),
	(25, 0.4547, 0.1620, 1.2),
	(26, 0.4049, 0.1290, 1.0),
	(27, 0.3606, 0.1020, 0.8),
	(28, 0.3211, 0.0810, 0.6);

-- Structuur van  tabel trafoBuildDatabase.tb250_trafoblik wordt geschreven
CREATE TABLE IF NOT EXISTS `tb250_trafoblik` (
  `TypeAanduiding` varchar(10) NOT NULL,
  `TongBreedte` decimal(4,2) DEFAULT NULL,
  `OppervlakVensters` decimal(4,2) DEFAULT NULL,
  `OppervlakKern` decimal(4,2) DEFAULT NULL,
  `BreedteEsheet` decimal(4,2) DEFAULT NULL,
  `HoogteEsheet` decimal(4,2) DEFAULT NULL,
  `BreedteIsheet` decimal(4,2) DEFAULT NULL,
  `HoogteIsheet` decimal(4,2) DEFAULT NULL,
  `TotaalOppervlak` decimal(5,2) DEFAULT NULL,
  `GewichtInGrammen` decimal(4,2) DEFAULT NULL,
  `SheetsKilo` int DEFAULT NULL,
  `BreedteVensterMM` int DEFAULT NULL,
  `HoogteVensterMM` int DEFAULT NULL,
  `InkoopPrijs` decimal(10,2) DEFAULT NULL,
  `VerkoopPrijs` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`TypeAanduiding`),
  KEY `MaxAmp` (`TongBreedte`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumpen data van tabel trafoBuildDatabase.tb250_trafoblik: ~11 rows (ongeveer)
INSERT INTO `tb250_trafoblik` (`TypeAanduiding`, `TongBreedte`, `OppervlakVensters`, `OppervlakKern`, `BreedteEsheet`, `HoogteEsheet`, `BreedteIsheet`, `HoogteIsheet`, `TotaalOppervlak`, `GewichtInGrammen`, `SheetsKilo`, `BreedteVensterMM`, `HoogteVensterMM`, `InkoopPrijs`, `VerkoopPrijs`) VALUES
	('EI-105', 3.50, 9.19, 18.38, 10.50, 7.00, 10.50, 1.75, 73.50, 27.84, 36, 17, 52, 6.00, 10.00),
	('EI-108', 3.60, 9.72, 19.44, 10.80, 7.20, 10.80, 1.80, 77.76, 29.45, 34, 18, 54, 6.00, 10.00),
	('EI-120', 4.00, 12.00, 24.00, 12.00, 8.00, 12.00, 2.00, 96.00, 36.36, 28, 20, 60, 6.00, 10.00),
	('EI-133', 4.45, 14.83, 29.66, 13.34, 8.89, 13.34, 2.22, 118.64, 44.93, 22, 22, 66, 6.00, 10.00),
	('EI-48', 1.60, 1.92, 3.84, 4.80, 3.20, 4.80, 0.80, 15.36, 5.82, 172, 8, 24, 6.00, 10.00),
	('EI-54', 1.80, 2.43, 4.86, 5.40, 3.60, 5.40, 0.90, 19.44, 7.36, 136, 9, 27, 6.00, 10.00),
	('EI-60', 2.00, 3.00, 6.00, 6.00, 4.00, 6.00, 1.00, 24.00, 9.09, 110, 10, 30, 6.00, 10.00),
	('EI-66', 2.20, 3.63, 7.26, 6.60, 4.40, 6.60, 1.10, 29.04, 11.00, 91, 11, 33, 6.00, 10.00),
	('EI-75', 2.50, 4.69, 9.38, 7.50, 5.00, 7.50, 1.25, 37.50, 14.20, 70, 12, 37, 6.00, 10.00),
	('EI-84', 2.80, 5.88, 11.76, 8.40, 5.60, 8.40, 1.40, 47.04, 17.82, 56, 14, 42, 6.00, 10.00),
	('EI-96', 3.20, 7.68, 15.36, 9.60, 6.40, 9.60, 1.60, 61.44, 23.27, 43, 16, 48, 6.00, 10.00);

-- Structuur van  tabel trafoBuildDatabase.tb260_wikkeldraad wordt geschreven
CREATE TABLE IF NOT EXISTS `tb260_wikkeldraad` (
  `Artikelnummer` varchar(15) NOT NULL,
  `diameter` decimal(5,3) DEFAULT NULL,
  `gewicht` int DEFAULT NULL,
  `maxtemp` int NOT NULL,
  `producent` varchar(20) DEFAULT NULL,
  `prijs1` decimal(5,2) DEFAULT NULL,
  `prijs3` decimal(5,3) DEFAULT NULL,
  `prijs5` decimal(5,2) DEFAULT NULL,
  `prijs10` decimal(5,3) DEFAULT NULL,
  PRIMARY KEY (`Artikelnummer`),
  KEY `diameter` (`diameter`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumpen data van tabel trafoBuildDatabase.tb260_wikkeldraad: ~22 rows (ongeveer)
INSERT INTO `tb260_wikkeldraad` (`Artikelnummer`, `diameter`, `gewicht`, `maxtemp`, `producent`, `prijs1`, `prijs3`, `prijs5`, `prijs10`) VALUES
	('1030-0600-47', 0.600, 1000, 200, 'SYNFLEX', 35.27, 28.630, 27.39, 0.000),
	('1030-0650-47', 0.650, 1000, 200, 'SYNFLEX', 34.78, 28.030, 26.20, 0.000),
	('1030-0750-47', 0.750, 1000, 200, 'SYNFLEX', 33.57, 27.800, 26.01, 0.000),
	('1030-0800-47', 0.800, 1000, 200, 'SYNFLEX', 34.58, 27.990, 26.02, 0.000),
	('1030-0900-47', 0.900, 1000, 200, 'SYNFLEX', 34.07, 27.160, 25.79, 0.000),
	('1030-1000-47', 1.000, 1000, 200, 'SYNFLEX', 33.38, 27.710, 24.80, 0.000),
	('1040-0200-47', 0.200, 1000, 180, 'SYNFLEX', 51.02, 44.640, 41.45, 0.000),
	('1040-0300-47', 0.300, 1000, 180, 'SYNFLEX', 44.78, 36.620, 34.00, 0.000),
	('1040-0400-47', 0.400, 1000, 180, 'SYNFLEX', 41.29, 33.320, 31.16, 0.000),
	('1040-0500-47', 0.500, 1000, 180, 'SYNFLEX', 37.70, 31.020, 28.36, 0.000),
	('DNE0.10-1.00', 0.100, 1000, 155, 'INDEL', 79.26, 68.450, 61.25, 54.040),
	('DNE0.20-1.00', 0.200, 1000, 155, 'INDEL', 49.66, 42.890, 38.37, 33.800),
	('DNE0.30-1.00', 0.300, 1000, 180, 'INDEL', 47.04, 40.620, 36.34, 32.000),
	('DNE0.40-1.00', 0.400, 1000, 180, 'INDEL', 44.69, 36.080, 32.28, 28.400),
	('DNE0.50-1.00', 0.500, 1000, 180, 'INDEL', 44.11, 35.600, 31.85, 28.100),
	('DNE0.60-1.00', 0.600, 1000, 180, 'INDEL', 44.11, 35.600, 31.85, 28.110),
	('DNE0.65-1.00', 0.650, 1000, 180, 'INDEL', 44.11, 35.600, 31.85, 28.110),
	('DNE0.70-1.00', 0.700, 1000, 200, 'INDEL', 43.81, 35.360, 31.64, 27.920),
	('DNE0.75-1.00', 0.750, 1000, 200, 'INDEL', 43.81, 35.360, 31.64, 27.920),
	('DNE0.80-1.00', 0.800, 1000, 200, 'INDEL', 42.33, 34.170, 30.57, 26.980),
	('DNE0.90-1.00', 0.900, 1000, 200, 'INDEL', 42.33, 34.170, 30.57, 26.980),
	('DNE1.00-1.00', 1.000, 1000, 200, 'INDEL', 42.48, 34.290, 30.68, 27.070);

-- Structuur van  tabel trafoBuildDatabase.tb900_numberstabel wordt geschreven
CREATE TABLE IF NOT EXISTS `tb900_numberstabel` (
  `itemType` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `itemNumber` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumpen data van tabel trafoBuildDatabase.tb900_numberstabel: ~3 rows (ongeveer)
INSERT INTO `tb900_numberstabel` (`itemType`, `itemNumber`) VALUES
	('invoice', 1),
	('quotation', 1),
	('trafo', 1);

-- Structuur van  tabel trafoBuildDatabase.tb910_placeholders wordt geschreven
CREATE TABLE IF NOT EXISTS `tb910_placeholders` (
  `functionName` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `placeHolderString` varchar(5000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`functionName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumpen data van tabel trafoBuildDatabase.tb910_placeholders: ~1 rows (ongeveer)
INSERT INTO `tb910_placeholders` (`functionName`, `placeHolderString`) VALUES
	('saveTrafoOptionsAndGetDetailScreen', '$attr12v$|$attr5v$|$attr6v$|$centerTap$|$centerTap12v$|$centerTap5v$|$centerTap6v$|$centerTapSec$|$filCenterTap12v$|$filCenterTap5v$|$filCenterTap6v$|$filSva12v$|$filSva5v$|$filSva6v$|$filWireSize12v$|$filWireSize5v$|$filWireSize6v$|$hideTap50v$|$NumOfTurns12v$|$NumOfTurns5v$|$NumOfTurns6v$|$NumOfTurnsPrim$|$NumOfTurnsSec$|$pva$|$secAmperage$|$secVoltage$|$sva$|$tap50v$|$turnRatio$|$turnsPerVolt$|$wireSizePrim$|$wireSizeSec$');

-- Structuur van  tabel trafoBuildDatabase.tb920_database_queries wordt geschreven
CREATE TABLE IF NOT EXISTS `tb920_database_queries` (
  `itemName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `queryString` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `parameterList` varchar(250) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fieldList` varchar(250) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `action` varchar(5) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`itemName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumpen data van tabel trafoBuildDatabase.tb920_database_queries: ~0 rows (ongeveer)
INSERT INTO `tb920_database_queries` (`itemName`, `queryString`, `parameterList`, `fieldList`, `action`) VALUES
	('loadPage', 'select * from TB100_HtmlPaginas where id = :id', 'id;', 'inlineHtml;', 'read');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
