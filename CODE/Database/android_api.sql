-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  jeu. 30 août 2018 à 22:05
-- Version du serveur :  5.7.21
-- Version de PHP :  5.6.35

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `android_api`
--

-- --------------------------------------------------------

--
-- Structure de la table `emergencys`
--

DROP TABLE IF EXISTS `emergencys`;
CREATE TABLE IF NOT EXISTS `emergencys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email_u` varchar(255) NOT NULL,
  `unique_id_u` varchar(23) NOT NULL,
  `status` varchar(23) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_u` (`email_u`),
  UNIQUE KEY `unique_id_u` (`unique_id_u`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `emergencys`
--

INSERT INTO `emergencys` (`id`, `email_u`, `unique_id_u`, `status`, `created_at`, `updated_at`) VALUES
(8, 'Hafedh. Ing@gmail.com', '5b83353d270cb9.84066804', 'ALERTN', '2018-08-27 00:18:21', '2018-08-27 23:17:16'),
(9, 'Sedki.hergli@supcom.tn', '5b833672dbe371.88297652', 'ALERTS', '2018-08-27 00:23:30', '2018-08-29 12:00:51'),
(10, 'FehmiMtira@gmail.com', '5b8336ab2f1739.33031487', 'OK', '2018-08-27 00:24:27', '2018-08-27 23:55:28');

-- --------------------------------------------------------

--
-- Structure de la table `locations`
--

DROP TABLE IF EXISTS `locations`;
CREATE TABLE IF NOT EXISTS `locations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unique_id_u` varchar(23) NOT NULL,
  `email_u` varchar(255) NOT NULL,
  `lat` varchar(80) NOT NULL,
  `lng` varchar(80) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_id_u` (`unique_id_u`),
  UNIQUE KEY `email_u` (`email_u`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `locations`
--

INSERT INTO `locations` (`id`, `unique_id_u`, `email_u`, `lat`, `lng`, `created_at`, `updated_at`) VALUES
(8, '5b83353d270cb9.84066804', 'Hafedh. Ing@gmail.com', '35.866253', '10.574598', '2018-08-27 00:18:21', '2018-08-27 23:17:16'),
(9, '5b833672dbe371.88297652', 'Sedki.hergli@supcom.tn', '35.83526666666667', '10.592423333333333', '2018-08-27 00:23:30', '2018-08-29 12:01:07'),
(10, '5b8336ab2f1739.33031487', 'FehmiMtira@gmail.com', '35.86022833333333', '10.546576666666665', '2018-08-27 00:24:27', '2018-08-27 23:55:28');

-- --------------------------------------------------------

--
-- Structure de la table `message_u`
--

DROP TABLE IF EXISTS `message_u`;
CREATE TABLE IF NOT EXISTS `message_u` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `message_u`
--

INSERT INTO `message_u` (`id`, `message`, `email`, `updated_at`, `created_at`) VALUES
(4, 'Welcome', 'Hafedh. Ing@gmail.com', '2018-08-27 23:17:16', '2018-08-27 00:18:21'),
(5, 'Welcome', 'Sedki.hergli@supcom.tn', '2018-08-27 23:52:19', '2018-08-27 00:23:30'),
(6, 'Welcome', 'FehmiMtira@gmail.com', '2018-08-27 23:55:28', '2018-08-27 00:24:27');

-- --------------------------------------------------------

--
-- Structure de la table `photos`
--

DROP TABLE IF EXISTS `photos`;
CREATE TABLE IF NOT EXISTS `photos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `photo_name` text NOT NULL,
  `photo_url` text NOT NULL,
  `caption` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `photos`
--

INSERT INTO `photos` (`id`, `photo_name`, `photo_url`, `caption`) VALUES
(3, 'IMG_20160502_223155.jpg', '/storage/52FE-120E/souvenir/IMG_20160502_223155.jpg', '5b83353d2789b9.82511672'),
(4, 'IMG_20160502_223155.jpg', '/storage/52FE-120E/souvenir/IMG_20160502_223155.jpg', '5b833672dbe371.88297652');

-- --------------------------------------------------------

--
-- Structure de la table `sensors`
--

DROP TABLE IF EXISTS `sensors`;
CREATE TABLE IF NOT EXISTS `sensors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sensor_uid` varchar(23) NOT NULL,
  `email_u` varchar(255) NOT NULL,
  `humidity` varchar(80) NOT NULL,
  `temperature` varchar(80) NOT NULL,
  `current` varchar(80) NOT NULL,
  `voltage` varchar(80) NOT NULL,
  `battery_mah` varchar(80) NOT NULL,
  `max_v` varchar(80) NOT NULL,
  `min_v` varchar(80) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sensor_uid` (`sensor_uid`),
  UNIQUE KEY `email_u` (`email_u`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `sensors`
--

INSERT INTO `sensors` (`id`, `sensor_uid`, `email_u`, `humidity`, `temperature`, `current`, `voltage`, `battery_mah`, `max_v`, `min_v`, `created_at`, `updated_at`) VALUES
(5, '5b833672dbe371.88297652', 'Sedki.hergli@supcom.tn', '51%', '32.58 °C', '0.09', '3.63', '100', '9', '1.5', '2018-08-27 00:23:30', '2018-08-29 12:08:03'),
(4, '5b83353d270cb9.84066804', 'Hafedh. Ing@gmail.com', '0', '0', '0.04', '8.3', '100', '9', '6', '2018-08-27 00:18:21', '2018-08-27 23:17:16'),
(6, '5b8336ab2f1739.33031487', 'FehmiMtira@gmail.com', '0', '0', '0', '0', '0', '0', '0', '2018-08-27 00:24:27', '2018-08-27 23:55:28');

-- --------------------------------------------------------

--
-- Structure de la table `supers`
--

DROP TABLE IF EXISTS `supers`;
CREATE TABLE IF NOT EXISTS `supers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(23) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(80) NOT NULL,
  `password` varchar(80) NOT NULL,
  `salt` varchar(80) NOT NULL,
  `unique_id_u` varchar(23) NOT NULL,
  `name_u` varchar(80) NOT NULL,
  `email_u` varchar(255) NOT NULL,
  `phone_u` varchar(80) NOT NULL,
  `lat` varchar(80) NOT NULL,
  `lng` varchar(80) NOT NULL,
  `lat_s` varchar(80) NOT NULL,
  `lng_s` varchar(80) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_id` (`unique_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_u` (`email_u`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `supers`
--

INSERT INTO `supers` (`id`, `unique_id`, `name`, `email`, `phone`, `password`, `salt`, `unique_id_u`, `name_u`, `email_u`, `phone_u`, `lat`, `lng`, `lat_s`, `lng_s`, `created_at`, `updated_at`) VALUES
(8, '5b83353d2789b9.82511672', 'Sedki hergli', 'Sedki.hergli@supcom.tn', '50078501', 'J6QkHNQ+2SYipxELR2buMKBnEadiMjIyYzQxYTAw', 'b222c41a00', '5b83353d270cb9.84066804', 'Hafedh Ben Hassen', 'Hafedh. Ing@gmail.com', '40678465', '35.86003166666667', '10.546048333333333', '35.864841', '10.591264', '2018-08-27 00:18:21', '2018-08-27 23:18:00'),
(9, '5b833672dc21f8.46755367', 'Amira Lessigue', 'Amira.lessigue@gmail.com', '55567890', 'Yz+nhmudlQi9J53AQ2sK1CXMLiI4MTI0NmQ3NGEz', '81246d74a3', '5b833672dbe371.88297652', 'Sedki Hergli', 'Sedki.hergli@supcom.tn', '50626911', '35.86022833333333', '10.546576666666665', '35.864841', '10.591264', '2018-08-27 00:23:30', '2018-08-27 23:54:12'),
(10, '5b8336ab2f55b0.82063862', 'Saleh Bouzid', 'Salehbouzid@gmail.com', '55567345', 'nq9p07PEpc47m7tSs1owdOLLq0ZjYWQ2MWNhMWM0', 'cad61ca1c4', '5b8336ab2f1739.33031487', 'Fehmi Mtira', 'FehmiMtira@gmail.com', '56456888', '35.86022833333333', '10.546576666666665', '35.86022833333333', '10.546576666666665', '2018-08-27 00:24:27', '2018-08-27 23:56:25');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(23) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(80) NOT NULL,
  `password` varchar(80) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `lat` varchar(100) NOT NULL,
  `lng` varchar(100) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `sexe` varchar(10) NOT NULL,
  `name_s` varchar(50) NOT NULL,
  `email_s` varchar(255) NOT NULL,
  `phone_s` varchar(80) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_id` (`unique_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_s` (`email_s`)
) ENGINE=MyISAM AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `unique_id`, `name`, `email`, `phone`, `password`, `salt`, `lat`, `lng`, `created_at`, `updated_at`, `sexe`, `name_s`, `email_s`, `phone_s`) VALUES
(23, '5b83353d270cb9.84066804', 'Hafedh Ben Hassen', 'Hafedh. Ing@gmail.com', '40678465', 'RfzVRDF/pmG5PsLJ/rl8BSa083g2NmY2NDE5M2Jm', '66f64193bf', '35.86003166666667', '10.546048333333333', '2018-08-27 00:18:21', '2018-08-27 23:50:10', 'Male', 'Sedki hergli', 'Sedki.hergli@supcom.tn', '50078501\r\n'),
(24, '5b833672dbe371.88297652', 'Sedki Hergli', 'Sedki.hergli@supcom.tn', '50626911', 'FRDyNN5X7PsPBxzkQelY0muMENM4YmZjYmMyNmVl', '8bfcbc26ee', '35.86022833333333', '10.546576666666665', '2018-08-27 00:23:30', '2018-08-27 23:54:12', 'Male', 'Amira Lessigue', 'Amira.lessigue@gmail.com', '55567890'),
(25, '5b8336ab2f1739.33031487', 'Fehmi Mtira', 'FehmiMtira@gmail.com', '56456888', 'wqnot+CwQXZewgDtd/gv7s5wWhRiMDMwNWE4MGNj', 'b0305a80cc', '35.86022833333333', '10.546576666666665', '2018-08-27 00:24:27', '2018-08-27 23:56:25', 'Male', 'Saleh Bouzid', 'Salehbouzid@gmail.com', '55567345');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
