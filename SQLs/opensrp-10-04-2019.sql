-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 10, 2019 at 04:05 AM
-- Server version: 5.7.23-0ubuntu0.16.04.1
-- PHP Version: 7.0.32-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `opensrp`
--

-- --------------------------------------------------------

--
-- Table structure for table `appointment_type`
--

CREATE TABLE `appointment_type` (
  `id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `appointment_type`
--

INSERT INTO `appointment_type` (`id`, `created_at`, `is_active`, `name`, `notes`, `updated_at`) VALUES
(1, '2019-03-26 13:56:53', 1, 'CTC', 'CTC appointments', '2019-03-26 13:56:53'),
(2, '2019-03-26 13:56:53', 1, 'PMTCT', 'PMTCT Appointment', '2019-03-26 13:56:53'),
(3, '2019-03-26 13:56:53', 1, 'TB', 'TB Appointments', '2019-03-26 13:56:53'),
(4, '2019-03-26 13:57:38', 1, 'Drug Clinic', 'Drug Clinic', '2019-03-26 13:56:53');

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

CREATE TABLE `clients` (
  `id` bigint(20) NOT NULL,
  `care_taker_name` varchar(255) DEFAULT NULL,
  `care_taker_phone_number` varchar(255) DEFAULT NULL,
  `care_taker_relationship` varchar(255) DEFAULT NULL,
  `community_based_hiv_service` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_of_birth` datetime DEFAULT NULL,
  `date_of_death` datetime DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `hamlet` varchar(255) DEFAULT NULL,
  `hiv_status` tinyint(1) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `veo` varchar(255) DEFAULT NULL,
  `village` varchar(255) DEFAULT NULL,
  `ward` varchar(255) DEFAULT NULL,
  `registration_reason_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `client_appointments`
--

CREATE TABLE `client_appointments` (
  `appointment_date` datetime NOT NULL,
  `id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_cancelled` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `health_facility_client_id` bigint(20) NOT NULL,
  `followup_referral_id` int(255) DEFAULT NULL,
  `appointment_type` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `client_referral_indicator`
--

CREATE TABLE `client_referral_indicator` (
  `id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `referral_id` bigint(20) DEFAULT NULL,
  `indicator_id` bigint(20) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `health_facilities`
--

CREATE TABLE `health_facilities` (
  `openmrs_UUID` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `facility_ctc_code` varchar(255) DEFAULT NULL,
  `facility_name` varchar(255) DEFAULT NULL,
  `HFR_code` varchar(255) DEFAULT NULL,
  `_id` bigint(20) DEFAULT NULL,
  `parent_openmrs_UUID` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `health_facilities`
--

INSERT INTO `health_facilities` (`openmrs_UUID`, `created_at`, `facility_ctc_code`, `facility_name`, `HFR_code`, `_id`, `parent_openmrs_UUID`, `updated_at`) VALUES
('ed7d4f8d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Chagongwe Dispensary', '100694-9', 1, 'ed78efbd-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed7ddaa5-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mkobwe Dispensary', '105081-4', 2, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed7e9f09-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Chakwale Dispensary', '100699-8', 3, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed7f5c09-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Madege Dispensary', '110854-7', 4, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed7ff0dc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ndogomi Dispensary', '112106-0', 5, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed807a1f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Chanjale Dispensary', '111301-8', 6, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed810aa5-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kumbulu Dispensary', '111862-9', 7, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed819d73-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Gairo Health Center', '101168-3', 8, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed82214d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Gairo Mission Dispensary', '101169-1', 9, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed82b16b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Meshugi Dispensary', '111860-3', 10, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed83273b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Idibo Dispensary', '101478-6', 11, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed838e38-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Leshata Dispensary', '103486-7', 12, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed83ecae-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Chogoali Dispensary', '100871-3', 13, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed846bef-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ijava Dispensary', '101632-8', 14, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed850f75-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'nguyami Dispensary', '109896-1', 15, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed85a076-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Iyogwe Dispensary', '102044-5', 16, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed863327-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Makuyu Dispensary', '112105-2', 17, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed86d031-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kibedya Dispensary', '102591-5', 18, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed876d9e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mandege Dispensary', '104227-4', 19, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed88074f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Msingisi Dispensary', '105474-1', 20, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed88860d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Nongwe Dispensary', '106567-1', 21, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed88f954-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kwipipa Dispensary', '111055-0', 22, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed897473-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Masenge Dispensary', '104395-9', 23, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed89d891-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kisitwi Dispensary', '111861-1', 24, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed8a2d79-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Rubeho Dispensary', '107123-2', 25, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed8a7aea-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Chisano Dispensary', '100843-2', 26, 'ed798e06-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed8ae03f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'TANESCO Kihansi Dispensary', '107742-9', 27, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ed8b3a86-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Chita JKT Dispensary', '100847-3', 28, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edd73511-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Chita Rural Dispensary', '100848-1', 29, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edd7be81-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'KAANAN Dispensary', '111517-9', 30, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edd835e4-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Udagaji Dispensary', '111040-2', 31, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edd8d4f3-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Idete Dispensary', '101473-7', 32, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edd95d2f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Idete Prison Dispensary', '101476-0', 33, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edd9dd9b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kisegese Dispensary', '111057-6', 34, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edda6cc3-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Namwawala Dispensary', '106160-5', 35, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('eddb067d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'MICO Urambo Dispensary', '111519-5', 36, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('eddb9e82-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Nesa Clinic', '106306-4', 37, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('eddc4283-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'St Jude Thaddeus Health Center', '104620-0', 38, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('eddd0b60-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'MEIGHS Dispensary', '104720-8', 39, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edddd08c-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Matema Dispensary', '111036-0', 40, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edde72fc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Huruma Health Center', '111499-0', 41, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede01a76-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kibaoni Health Center', '102583-2', 42, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede0fb3c-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'St. Alphonsa Dispensary', '107568-8', 43, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede1dbc5-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Arafa Kiberege Dispensary', '111666-4', 44, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede2ce79-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kiberege Dispensary', '102595-6', 45, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede3a10a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kiberege Prison Dispensary', '102596-4', 46, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede46223-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Sagamaganga Dispensary', '107227-1', 47, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede513df-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Signali Dispensary', '108482-1', 48, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede5bca6-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'KIDATU Dispensary', '111518-7', 49, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede65e14-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Police Kidatu Dispensary', '111039-4', 50, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede70c3b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'TANESCO Kidatu Dispensary', '107741-1', 51, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede7a67a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Arafa Mkamba Dispensary', '111288-7', 52, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede83752-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Good samaritan Health Center', '111294-5', 53, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede8f39e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Illovo Hospital - Other Hospital', '102873-7', 54, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ede988b1-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mkamba Health Center', '105045-9', 55, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edea2900-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mkamba Private Dispensary', '108507-5', 56, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edeab710-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Msolwa Game Reserve Dispensary', '105488-1', 57, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edeb6d1d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Msolwa Station Dispensary', '105490-7', 58, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edec4229-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kisawasawa Dispensary', '103082-4', 59, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('eded0c25-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kasiga Dispensary', '111859-5', 60, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ededccb5-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kipanga Dispensary', '102991-7', 61, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edee9e3f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Michenga Dispensary', '104816-4', 62, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edef86c6-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Dr Mziray Dispensary', '111682-1', 63, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf05d2f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mang\'ula Health Center', '104264-7', 64, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf0ffdc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Katurukila Dispensary', '102499-1', 65, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf1a2aa-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ipinde Dispensary', '101831-6', 66, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf240f0-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Tanganyika Masagati Dispensary', '107751-0', 67, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf2cd16-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Taweta Dispensary', '107782-5', 68, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf3f044-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mchombe Dispensary', '104693-7', 69, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf48003-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mkangawalo Dispensary', '105052-5', 70, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf513ae-d770-11e8-ba9c-f23c917bb7ec', '2019-04-06 10:56:37', '05-05-02.03', 'Mngeta Health Center', '105250-5', 71, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf5979e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Sonjo Dispensary', '107551-4', 72, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf632ea-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'St. Francis Designated District Hospital', '107585-2', 73, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf6c3e7-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ifakara Dispensary', '111520-3', 74, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf748ad-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'St. James Dispensary', '107592-8', 75, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf7ee71-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mlimba Health Center', '105207-5', 76, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf89b6a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'TAZARA Dispensary', '107791-6', 77, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edf94624-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Merys Dispensary', '111037-8', 78, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edfa07ac-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'KPL Dispensary', '111059-2', 79, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edfaa7ce-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ikwambi Dispensary', '112023-7', 80, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('edfb50e5-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ane Dispensary', '111521-1', 81, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1699b5-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mofu Dispensary', '105264-6', 82, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1733b0-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Bakwata - Mang\'ula Dispensary', '100253-4', 83, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee17aaa0-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mama Tura Dispensary', '111035-2', 84, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1868cb-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mwaya Dispensary', '105983-1', 85, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee19d874-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'TUMAINI Dispensary', '111516-1', 86, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1a6ce2-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ikule Dispensary', '111056-8', 87, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1ae81d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Msolwa A Dispensary', '105487-3', 88, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1b5d22-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Sanje Dispensary', '107291-7', 89, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1bd5fa-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kitete Dispensary', '111058-4', 90, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1c583b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Uchindile Dispensary', '108000-1', 91, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1cea69-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Mpanga Dispensary', '105342-0', 92, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1d92a7-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ngalimila Dispensary', '106334-6', 93, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1e60bb-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Upendo KCY Dispensary', '111038-6', 94, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1efb24-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Utengule Dispensary', '108206-4', 95, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee1f8060-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Dr. Tayari Dispensary', '110793-7', 96, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee202238-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Dr. Mziray Dispensary', '101022-2', 97, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee20b2dc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kikopa Dispensary', '110770-5', 98, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee214a4a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Watani Dispensary', '108323-7', 99, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee21b325-d770-11e8-ba9c-f23c917bb7ec', '2019-04-03 10:22:27', '11-05-02.04', 'Berega Hospital', '102400-9', 100, 'ed7a41a8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee22240a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Chanzuru Dispensary', '100746-7', 101, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee228233-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ilonga Dispensary', '101757-3', 102, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee233187-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ilonga Mission Dispensary', '101759-9', 103, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee238ea3-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Ilonga TTC Dispensary', '101760-7', 104, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee23ed44-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Msimba Dispensary', '105466-7', 105, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee24675d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Dakawa Dispensary', '100945-5', 106, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee24de7a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Dumila Dispensary', '101039-6', 107, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee252e74-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Matongolo Dispensary', '112283-7', 108, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee259285-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'St. Joseph Health Center', '110835-6', 109, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee25f97d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kilosa Mission Dispensary', '102878-6', 110, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee265a10-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21', NULL, 'Kidete Health Center', '102658-2', 111, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:21'),
('ee26a991-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mwasa Dispensary', '105962-5', 112, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee27046b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kilombero Sugar company Health Center', '102118-7', 113, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2762bf-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Ruaha Mission Dispensary', '107109-1', 114, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee27af52-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kidodi Health Center', '102662-4', 115, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee280cfd-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kilangali Dispensary', '102818-2', 116, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee286585-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kivungu Dispensary', '103218-4', 117, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee28b22b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mbamba Dispensary', '104563-2', 118, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee28f5d0-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Serengeti Dispensary', '112280-3', 119, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee294571-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kimamba Health Center', '102892-7', 120, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee29bdd7-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Makwambe Dispensary', '112281-1', 121, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2a34ab-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kisanga Dispensary', '103072-5', 122, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2aab92-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Msange Mission Health Center', '105437-8', 123, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2b231f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Msolwa Mission Dispensary', '105489-9', 124, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2b95ae-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kitete Dispensary', '103176-4', 125, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2c0b22-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'kisongwe Dispensary', '110837-2', 126, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2c914d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Lumbiji Dispensary', '103707-6', 127, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2d1460-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Lumuma Dispensary', '103714-2', 128, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2d9c02-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mabula Dispensary', '111051-9', 129, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2e2da3-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kondoa Dispensary', '111576-5', 130, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2f2b1b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mabwerebwere Dispensary', '111290-3', 131, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee2fafba-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Magole Dispensary', '103974-2', 132, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee301761-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Magomeni Dispensary', '103978-3', 133, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee308bf9-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Magubike Health Center', '103992-4', 134, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee30e222-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Malolo Dispensary', '104186-2', 135, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee53a91d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Malolo Mission Dispensary', '104188-8', 136, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee541550-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kitange Dispensary', '103161-6', 137, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee5475a6-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mamboya Dispensary', '104208-4', 138, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee54f9c0-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mtumbatu Dispensary', '105599-5', 139, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee556bc0-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Chabima Dispensary', '111575-7', 140, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee55bebf-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Changarawe Dispensary', '111574-0', 141, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee56059a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Changarawe Dispensary', '112284-5', 142, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee564a1f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Munisagara Dispensary', '105693-6', 143, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee5694a9-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Usagara Dispensary', '108159-5', 144, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee56e330-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Ihombwe Dispensary', '111049-3', 145, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee574540-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'MICO Dispensary', '104823-0', 146, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee57b1f3-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'MICO Mikumi Dispensary', '111577-3', 147, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee580fbd-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mikumi Dispensary', '104913-9', 148, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee588510-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mikumi CVM Dispensary', '108526-5', 149, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee591103-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mikumi National Park Dispensary', '104917-0', 150, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee597b24-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Salatein Dispensary', '107249-5', 151, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee59dd2d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'St. kizito Hospital', '111050-1', 152, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ee5a4cfe-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mikumi Msimba Dispensary', '104916-2', 153, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed5024d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Care one Dispensary', '111572-4', 154, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed5a4b0-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kilosa Hospital - District Hospital', '102877-8', 155, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed62386-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Tanangozi Dispensary', '107735-3', 156, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed6c8e8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Magereza Kilosa Dispensary', '103956-9', 157, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed73719-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mkombozi Dispensary', '112285-2', 158, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed798a5-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Msowero Dispensary', '105502-9', 159, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed81246-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mvumi Dispensary', '105772-8', 160, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed887be-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Arafa Dispensary', '110833-1', 161, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed8fbc4-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Elbenezer Dispensary', '101065-1', 162, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eed9f3de-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kilombero Sugar Company Clinic - Other Clinic', '111302-6', 163, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeda89e2-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Ruaha Dispensary', '111054-3', 164, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eedb1bbf-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Rudewa Estate Dispensary', '111047-7', 165, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eedba12b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Rudewa Dispensary', '107126-5', 166, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eedc28ed-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Twatwatwa Dispensary', '107965-6', 167, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eedc9a27-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Unone Dispensary', '108107-4', 168, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eedd6a96-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kidogobasi Dispensary', '102664-0', 169, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeddd7bd-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kitete Msindizi Dispensary', '103178-0', 170, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eede9d50-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Ruhembe Dispensary', '107139-8', 171, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eedf0af2-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Tindiga Dispensary', '107828-6', 172, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eedf5ee4-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mhenda Dispensary', '104802-4', 173, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eedfaed0-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mzuma Dispensary', '111573-2', 174, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee02157-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mzuma Dispensary', '111303-4', 175, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee07d98-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Ulaya Health Center', '108070-4', 176, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee0cac8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Uleling\'ombe Dispensary', '108072-0', 177, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee12f55-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Chonwe Dispensary', '100883-8', 178, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee18464-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Vidunda Dispensary', '108250-2', 179, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee1deb8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Vidunda Mission Dispensary', '108251-0', 180, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee24e3e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Zombo Dispensary', '108426-8', 181, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee2d476-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mgolole Health Center', '110307-6', 182, 'ed7ae90e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee34f2e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Vituli Dispensary', '110021-3', 183, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee3d256-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Ardhi Institute Dispensary', '100176-7', 184, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee476ae-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kibwe Dispensary', '110914-9', 185, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee50772-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Magereza Mkoa Dispensary', '103936-1', 186, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee588ce-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Police FFU Dispensary', '107012-7', 187, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee60b4c-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Morogoro Regional Referral Hospital', '105299-2', 188, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee6d30a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Bungu Dispensary', '100570-1', 189, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee756da-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Duthumi Health Center', '101043-8', 190, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee7d4ae-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Bwakira Chini Dispensary', '100646-9', 191, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee86ced-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Bwakwira Juu Dispensary', '100647-7', 192, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee9082e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kinonko Dispensary', '102964-4', 193, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eee99229-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kinonko JWTZ Dispensary', '102965-1', 194, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeea1139-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kasanga Mission Dispensary', '102401-7', 195, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeea927e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kibogwa Dispensary', '110424-9', 196, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeeb27fa-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kibungo Juu Dispensary', '110433-0', 197, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeebe0ff-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Nyingwa Dispensary', '106854-3', 198, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeec740e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'St Karoli Luanga Dispensary', '110292-0', 199, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeed0153-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kichangani Dispensary', '102643-4', 200, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeed804d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'TRC Dispensary', '107882-3', 201, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeee0ce3-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Jordan University Dispensary', '110302-7', 202, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeee9b29-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kidugalo Wazazi Dispensary', '102670-7', 203, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeef202e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Sangasanga Dispensary', '110803-4', 204, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeef9a79-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Seregete B Dispensary', '110802-6', 205, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeefefc0-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'visaraka Dispensary', '110804-2', 206, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef03efb-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kihonda Health Center', '102744-0', 207, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef07c8f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'OPD-MMC Health Center', '112167-2', 208, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef0d1a9-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Presybterian Seminary Dispensary', '110812-5', 209, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef1174f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'St.Charles Borromoe Dispensary', '110035-3', 210, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef1875e-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kihonda Magereza Dispensary', '102745-7', 211, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef20128-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Dar Group Dispensary', '110304-3', 212, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef25ceb-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Ahmadiyya Dispensary', '100055-3', 213, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef2afaf-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'St.Thomas Health Center', '110291-2', 214, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef302cd-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Morogoro TTC Dispensary', '102729-1', 215, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef354ff-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kilakala Dispensary', '102809-1', 216, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef394df-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kilakala Secondary Dispensary', '102810-9', 217, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef3d7c4-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Tuwaenzi Wazee Dispensary', '110306-8', 218, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef4212a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Afya Specialized Clinic - Other Clinic', '110425-6', 219, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef470ae-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Uhuru Health Center', '108543-0', 220, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef4c2dd-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mbuya Dispensary', '104665-5', 221, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef5096c-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Misongeni Dispensary', '111403-2', 222, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef56141-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Legezamwendo Dispensary', '110915-6', 223, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef5acda-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kingolwira Health Center', '102949-5', 224, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef60999-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Lutheran Junior Seminary Dispensary', '103771-2', 225, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef65ebb-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kalundwa Dispensary', '102263-1', 226, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef6a592-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kinole Dispensary', '102959-4', 227, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef6fac3-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kikundi Dispensary', '102798-6', 228, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef75476-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kiroka health centre Health Center', '103038-6', 229, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef7a8a4-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'St.Mathias Dispensary', '110396-9', 230, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef8055a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kibungo kungwe Dispensary', '102630-1', 231, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef85295-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kisaki Gomero Dispensary', '103065-9', 232, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef89ce8-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Matambwe Game Reserve Dispensary', '108528-1', 233, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef8e8e5-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kisaki Station Dispensary', '103064-2', 234, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef933b4-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Nyarutanga Dispensary', '110581-6', 235, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef98c99-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Faith Dispensary', '110411-6', 236, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eef9eb9b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kisemu Dispensary', '103085-7', 237, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefa40f4-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'TTPL Dispensary', '110311-8', 238, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefaaeff-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Ujenzi Mkoa Dispensary', '110811-7', 239, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefb2d73-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kolero Dispensary', '103281-2', 240, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefbb2a9-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Lukange Health Center', '103683-9', 241, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefc2d07-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Matombo Mission Dispensary', '104475-9', 242, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefcaa3b-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mlono Dispensary', '105214-1', 243, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefd22a1-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Lundi Dispensary', '103718-3', 244, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefd88d6-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'St. Mary Dispensary', '107611-6', 245, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefdd7ee-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Umati Clinic Dispensary', '108094-4', 246, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefe2be5-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mafiga Health Center', '110290-4', 247, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefe7e66-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Matuli Dispensary', '110801-8', 248, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eefebed9-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Diguzi Dispensary', '110422-3', 249, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeff0e5a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Holy Cross Health Center', '110131-0', 250, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeff4c1c-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'MICO Kihonda Dispensary', '108513-3', 251, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeff9233-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Malipula Dispensary', '104181-3', 252, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('eeffdc42-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mazimbu Hospital - Other Hospital', '110309-2', 253, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef003dd9-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Shalom Health Center', '110427-2', 254, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef00944a-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'SUA Health Center', '107647-0', 255, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef00db97-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Fulwe Dispensary', '101159-2', 256, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef0136be-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mikese Dispensary', '104898-2', 257, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef018f12-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Aga Khan Health Center', '100044-7', 258, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef01d90d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Uluguru Health Center', '105515-1', 259, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef023354-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Jabal Hira Dispensary', '111501-3', 260, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef0287e6-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'St Harry Dispensary', '107563-9', 261, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef02cc0d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mji Mpya Dispensary', '105025-1', 262, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef0324c9-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kizinga Dispensary', '103248-1', 263, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef0365fa-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Alliance One Dispensary', '100090-0', 264, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef03bbf6-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mkambarani Dispensary', '105048-3', 265, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef041137-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mtego wa Simba Health centre Health Center', '105544-1', 266, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef045035-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Pangawe Sisal Dispensary', '106973-1', 267, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef04a5a1-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Pangawe JWTZ Dispensary', '106972-3', 268, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef04e9cd-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Pangawe Kijijini Dispensary', '106971-5', 269, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef054935-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kidunda Dispensary', '102671-5', 270, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef059ffe-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mkulazi KKKT Dispensary', '105124-2', 271, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef05de4d-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Changa Dispensary', '100732-7', 272, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef0651fe-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mfumbwe Dispensary', '104758-8', 273, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef06b903-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mkuyuni Health Center', '110708-5', 274, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef07121f-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Kibungo chini Mission Dispensary', '102628-5', 275, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef077502-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Mbete Dispensary', '110166-6', 276, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22'),
('ef07d655-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22', NULL, 'Towero Dispensary', '110227-6', 277, 'ed7b7bfc-d770-11e8-ba9c-f23c917bb7ec', '2019-02-26 15:28:22');

-- --------------------------------------------------------

--
-- Table structure for table `health_facility_clients`
--

CREATE TABLE `health_facility_clients` (
  `health_facility_client_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ctc_number` varchar(255) DEFAULT NULL,
  `facility_id` bigint(20) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `indicator`
--

CREATE TABLE `indicator` (
  `id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(255) DEFAULT NULL,
  `name_sw` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `indicator`
--

INSERT INTO `indicator` (`id`, `created_at`, `name`, `name_sw`, `is_active`, `updated_at`) VALUES
(1, '2018-03-21 21:16:30', 'Frequent fever', 'Homa za mara kwa mara', 1, '2018-02-03 08:41:11'),
(2, '2018-03-21 21:16:36', 'Weight loss', 'Kupungua uzito', 1, '2018-02-03 08:41:11'),
(3, '2018-03-21 21:17:03', 'Living with HIV+ spouse', 'Anaishi na mwenza mwenye VVU', 1, '2018-02-03 08:41:11'),
(4, '2018-03-21 21:17:54', 'Risk area/location', 'Eneo hatarishi', 1, '2018-02-03 08:41:11'),
(5, '2018-03-21 21:18:37', 'Highy fever', 'Joto kupanda/homa', 1, '2018-02-03 08:41:11'),
(6, '2018-03-21 21:18:43', 'Vomiting', 'Anatapika', 1, '2018-02-03 08:41:11'),
(7, '2018-03-21 21:19:29', 'Diarrhea', 'Anaharisha', 1, '2018-02-03 08:41:11'),
(8, '2018-03-21 21:19:39', 'Joint pains', 'Maumivu ya viungo', 1, '2018-02-03 08:41:11'),
(9, '2018-03-21 21:20:19', 'Loss of energy', 'Viungo kulegea', 1, '2018-02-03 08:41:11'),
(10, '2018-03-21 21:21:20', 'History of coughing for more than two weeks (HIV-)', 'Kukohoa kwa Zaidi ya wiki mbili (kwa watu wasio na VVU)', 1, '2018-02-03 08:41:11'),
(11, '2018-03-21 21:21:43', 'Persistent coughing (HIV+)', 'Kikohozi cha muda wowote (Kwa wagonjwa wa VVU)', 1, '2018-02-03 08:41:11'),
(12, '2018-03-21 21:21:50', 'Fever', 'Homa', 1, '2018-02-03 08:41:11'),
(13, '2018-03-21 21:24:18', 'Night sweats', 'Kutoka jasho jingi wakati amelala', 1, '2018-02-03 08:41:11'),
(14, '2018-03-21 21:26:51', 'Desire to conceive', 'Hahitaji kupata mtoto siku za karibuni', 1, '2018-02-03 08:41:11'),
(15, '2018-03-21 21:28:19', 'In a sexual relationship without using any family planning methods', 'Yuko kwenye mahusiano ya kingono ila hatumii njia yoyote ya kisassa ya uzazi wa mpango', 1, '2018-02-03 08:41:11'),
(16, '2018-03-21 21:29:36', 'Need/want to change family planning method', 'Anataka kubadili njia ya uzazi wa mpango anayoumia', 1, '2018-02-03 08:41:11'),
(17, '2018-03-21 21:30:43', 'Has not start Antinatal (ANC) Clininc', 'Hajaanza kiniki ya Mama na Mtoto', 1, '2018-02-03 08:41:11'),
(18, '2018-03-21 21:31:03', 'Viginal bleeding', 'Anatoka damu ukeni', 1, '2018-02-03 08:41:11'),
(19, '2018-03-21 21:31:37', 'Swollen legs/EDEMA', 'Amevimba miguu', 1, '2018-02-03 08:41:11'),
(20, '2018-03-21 21:32:47', 'Severe lower abdominal pain', 'Ana maumivu makali tumbo la chini au mgongo', 1, '2018-02-03 08:41:11'),
(21, '2018-03-21 21:33:34', 'History of home delivery', 'Yeyote aliyejifungulia nyumbani', 1, '2018-02-03 08:41:11'),
(22, '2018-03-21 21:34:03', 'Sign of malnutrition ', 'Mwenye dalili za utapiamlo', 1, '2018-02-03 08:41:11'),
(23, '2018-03-21 21:35:21', 'A child below 5 years who is underweight', 'Mtoto chini ya miaka mitano mwenye uzito pungufu', 1, '2018-02-03 08:41:11'),
(24, '2018-03-21 21:35:46', 'A child under 1 year without weight gain', 'Mtoto chini ya Mwaka mmoja asiyeongezeka uzito', 1, '2018-02-03 08:41:11'),
(25, '2018-03-21 21:36:16', 'Alcoholic spouse', 'Mwenza mlevi', 1, '2018-02-03 08:41:11'),
(26, '2019-03-07 03:45:16', 'Has moved out from home', 'Amehama nyumbani', 1, '2018-02-03 08:41:11'),
(27, '2019-03-07 03:45:52', 'Ulcers and scars', 'vidonda na makovu', 1, '2018-02-03 08:41:11'),
(28, '2019-03-07 03:46:27', 'He is weak', 'Amedhoofika', 1, '2018-02-03 08:41:11'),
(29, '2019-03-07 03:46:56', 'He needs more services', 'Anahitaji huduma zaidi', 1, '2018-02-03 08:41:11'),
(30, '2019-03-07 03:47:35', 'Has great level of stress and fear', 'Sonona (msongo wa mawazo)/ kuwa na woga mkuu', 1, '2018-02-03 08:41:11'),
(31, '2019-03-07 03:48:05', 'Threatened to be killed', 'Kutishiwa kuuawa', 1, '2018-02-03 08:41:11'),
(32, '2019-03-07 03:49:22', 'The child has a disability / has trouble walking', 'Mtoto ana ulemavu /hawezi kukaa au anatembea kwa shida', 1, '2018-02-03 08:41:11'),
(33, '2019-03-07 03:48:41', 'The child does not want to go home / go to school', 'Mtoto hataki kurudi nyumbani/kwenda shuleni', 1, '2018-02-03 08:41:12');

-- --------------------------------------------------------

--
-- Table structure for table `push_notifications_users`
--

CREATE TABLE `push_notifications_users` (
  `_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `google_push_notification_token` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_type` int(11) DEFAULT NULL,
  `user_uuid` varchar(255) DEFAULT NULL,
  `facility_uuid` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `referral`
--

CREATE TABLE `referral` (
  `referral_id` bigint(20) NOT NULL,
  `appointment_date` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `facility_id` varchar(255) DEFAULT NULL,
  `from_facility_id` varchar(255) DEFAULT NULL,
  `instance_id` varchar(255) DEFAULT NULL,
  `is_emergency` tinyint(1) DEFAULT NULL,
  `lab_test` int(11) DEFAULT NULL,
  `other_clinical_information` varchar(255) DEFAULT NULL,
  `other_notes` varchar(255) DEFAULT NULL,
  `referral_date` datetime DEFAULT NULL,
  `referral_reason` varchar(255) DEFAULT NULL,
  `referral_source` int(11) DEFAULT NULL,
  `referral_status` int(11) DEFAULT NULL,
  `referral_uuid` varchar(255) DEFAULT NULL,
  `referral_service_id` int(11) DEFAULT NULL,
  `service_provider_uiid` varchar(255) DEFAULT NULL,
  `test_results` int(11) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_id` bigint(20) DEFAULT NULL,
  `referral_feedback_id` bigint(20) DEFAULT NULL,
  `referral_type` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `referral_feedback`
--

CREATE TABLE `referral_feedback` (
  `_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `desc_en` varchar(255) DEFAULT NULL,
  `desc_sw` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `referral_type_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `referral_feedback`
--

INSERT INTO `referral_feedback` (`_id`, `created_at`, `desc_en`, `desc_sw`, `updated_at`, `referral_type_id`) VALUES
(1, '2019-04-06 14:41:42', 'The client is attending clinic', 'Mteja anahudhuria kliniki', '2019-03-15 11:57:38', 1),
(2, '2019-04-06 14:43:48', 'Found the client s/he is will to return back to the clinic.', 'Amepatikana na yupo tayari kurudi kliniki', '2019-03-15 11:57:38', 1),
(3, '2019-04-06 14:46:02', 'Found the client s/he is willing to return to the clinic.', 'Amepatikana na amerudi kliniki', '2019-03-15 11:57:38', 1),
(4, '2019-04-06 14:46:18', 'The client is dead.', 'Amefariki', '2019-03-15 11:57:38', 1),
(5, '2019-04-06 14:47:30', 'The client transferred clinic without notification.', 'Amehamia kituo kingine bila taarifa', '2019-03-15 11:57:38', 1),
(6, '2019-04-06 14:48:08', 'The client has reallocated ', 'Amehama makazi', '2019-03-15 11:57:38', 1),
(7, '2019-04-06 14:48:30', 'Found the client but s/he is not willing to return to the clinic', 'Amepatikana lakini hayupo tayari kurudi klinik', '2019-03-15 11:57:38', 1),
(8, '2019-04-06 14:48:49', 'Client not Found.', 'Hajapatikana', '2019-03-15 11:57:38', 1),
(9, '2019-03-19 08:41:37', 'Received and Attended', 'Amepokelewa na kuhudumiwa', '2019-03-19 08:41:37', 2),
(10, '2019-03-19 08:41:37', 'Received and Referred', 'Amepokelewa na kupewa Rufaa', '2019-03-19 08:41:37', 2);

-- --------------------------------------------------------

--
-- Table structure for table `referral_type`
--

CREATE TABLE `referral_type` (
  `referral_type_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT NULL,
  `referral_type_name` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `referral_type`
--

INSERT INTO `referral_type` (`referral_type_id`, `created_at`, `is_active`, `referral_type_name`, `updated_at`) VALUES
(1, '2019-03-15 12:01:27', 1, 'CHW_TO_FACILITY REFERRAL', '2019-03-15 12:01:27'),
(2, '2019-03-15 12:01:27', 1, 'INTRA-FACILITY REFERRAL', '2019-03-15 12:01:27'),
(3, '2019-03-15 12:01:27', 1, 'INTER-FACILITY REFERRAL', '2019-03-15 12:01:27'),
(4, '2019-03-15 12:01:27', 1, 'FACILITY-TO-CHW REFERRAL', '2019-03-15 12:01:27');

-- --------------------------------------------------------

--
-- Table structure for table `registration_reasons`
--

CREATE TABLE `registration_reasons` (
  `registration_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `desc_en` varchar(255) DEFAULT NULL,
  `desc_sw` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `applicable_to_men` tinyint(1) DEFAULT NULL,
  `applicable_to_women` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `registration_reasons`
--

INSERT INTO `registration_reasons` (`registration_id`, `created_at`, `desc_en`, `desc_sw`, `is_active`, `updated_at`, `applicable_to_men`, `applicable_to_women`) VALUES
(1, '2019-04-06 14:09:18', 'HIV/AIDS Transmission', 'Maambukizo ya VVU ', 1, '2019-03-15 11:26:59', 1, 1),
(2, '2019-04-06 14:08:44', 'Sickle cell disease ', 'Ugonjwa wa seli mundu', 1, '2019-03-15 11:26:59', 1, 1),
(3, '2019-04-06 14:12:34', 'Cardiovascular Disease ', 'Magonjwa ya moyo ', 1, '2019-03-15 11:26:59', 1, 1),
(4, '2019-04-06 14:13:03', 'Diabetes Mellitus/Diabetes', 'Kisukari ', 1, '2019-03-15 11:26:59', 1, 1),
(5, '2019-04-05 08:44:14', 'Cerebral Palsy', 'Mtindio wa ubongo ', 1, '2019-03-15 11:26:59', 1, 1),
(6, '2019-04-05 08:44:14', 'Cancer', 'Saratani', 1, '2019-03-15 11:26:59', 1, 1),
(7, '2019-04-05 08:44:14', 'Tuberculosis', 'Kifua kikuu', 1, '2019-03-15 11:26:59', 1, 1),
(8, '2019-04-06 14:13:46', 'A man having sex with a fellow man', 'Mwanaume anayefanya ngono na mwanaume mwenzake', 1, '2019-03-15 11:26:59', 1, 0),
(9, '2019-04-06 14:14:09', 'A child born with a mother having HIV/ADIS', 'Mtoto aliyezaliwa na mama mwenye VVU', 1, '2019-03-15 11:26:59', 1, 1),
(10, '2019-04-06 14:15:15', 'A child not screed for HIV/ADIS', 'Mtoto ambaye hajapimwa VVU ', 1, '2019-03-15 11:26:59', 1, 1),
(11, '2019-04-06 14:16:46', 'A child with weight Stagnation ', 'Mtoto asiyeongezeka uzito', 1, '2019-03-15 11:26:59', 1, 1),
(12, '2019-04-06 14:21:28', 'A child having frequent fever ', 'Mtoto anayepata homa za mara kwa mara', 1, '2019-03-15 11:26:59', 1, 1),
(13, '2019-04-06 14:22:55', 'A person faced with gender-based violence', 'Mtu aliyenyanyaswa kijinsia', 1, '2019-03-15 11:26:59', 1, 1),
(14, '2019-04-06 14:23:48', 'A drug addict person', 'Mtu anayejidunga', 1, '2019-03-15 11:26:59', 1, 1),
(15, '2019-04-06 14:24:26', 'A pregnant woman ', 'Mama mjamzito', 1, '2019-03-15 11:26:59', 0, 1),
(16, '2019-04-06 14:25:08', 'A breastfeeding woman', 'Mama anayenyonyesha ', 1, '2019-03-15 11:26:59', 0, 1),
(17, '2019-04-06 14:25:32', 'A child living in vulnerable environment.', 'Mtoto anayeishi mazingira hatarishi', 1, '2019-03-15 11:26:59', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

CREATE TABLE `service` (
  `id` bigint(20) NOT NULL,
  `category_name` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `service_name_sw` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`id`, `category_name`, `created_at`, `is_active`, `service_name`, `service_name_sw`, `updated_at`) VALUES
(1, 'Ushauri nasaha Na upimaji', '2019-04-06 13:31:27', 1, 'Counselling and testing centers ', 'Ushauri nasaha Na upimaji', '2019-03-26 15:02:46'),
(2, 'hiv\n', '2019-03-31 12:52:38', 1, 'Clinic of Treatment and Care', 'Kliniki ya Tiba na matunzo (CTC)', '2019-03-26 15:02:46'),
(3, 'magonjwa nyemelezi', '2019-04-06 13:34:50', 1, 'Opportunistic infection diseases', 'kituo cha kutoa huduma za afya kutokana na magonjwa nyemelezi', '2019-03-26 15:02:46'),
(4, 'tb', '2019-03-31 12:52:08', 1, 'Clinic Of Tuberculosis  Treatment', 'Kliniki ya kutibu kifua kikuu', '2019-03-26 15:02:46'),
(5, 'Wajawazito', '2019-04-06 13:59:53', 1, 'RCH services Pregnancy', 'Huduma za kuzuia maambukizi toka kwa mama kwenda kwa mtoto', '2019-03-26 15:02:46'),
(6, 'Wanaonyonyesha', '2019-04-06 14:00:18', 1, 'RCH Breast feeding mothers ', 'Wanaonyonyesha', '2019-03-26 15:02:46'),
(7, 'Watoto<miezi 18 ', '2019-04-06 13:50:57', 1, 'Children<18months ', 'Watoto<miezi 18 ', '2019-03-26 15:02:46'),
(8, 'Huduma ya tohara', '2019-04-06 13:47:55', 1, 'Circumcision Service', 'Huduma ya tohara', '2019-03-26 15:02:46'),
(9, 'gbv', '2019-04-06 13:50:18', 1, 'Gender-based violence services ', 'Huduma ya kuzui ukatili wa kijinsia (dawati la jinsia)', '2019-03-26 15:02:46'),
(10, 'Msaada wa kisheria', '2019-04-06 13:52:33', 1, 'Legal Support Services', 'Msaada wa kisheria', '2019-03-26 15:02:46'),
(11, 'Vikundi vya kusaidiana', '2019-04-06 13:52:57', 1, 'Support Groups', 'Vikundi vya kusaidiana', '2019-03-26 15:02:46'),
(12, 'Huduma za watoto wanaoishi katika mazingira hatarishi na yatima', '2019-04-06 13:54:07', 1, 'A child/orphans living in vulnerable environment', 'Huduma za watoto wanaoishi katika mazingira hatarishi na yatima', '2019-03-26 15:02:46'),
(13, 'Vituo vya wazee ', '2019-04-06 13:55:07', 1, 'Elderly Centers', 'Vituo vya wazee ', '2019-03-26 15:02:46'),
(14, 'Malaria', '2019-03-26 15:02:46', 1, 'Malaria', 'Malaria', '2019-03-26 15:02:46'),
(15, 'fp', '2019-03-26 15:02:46', 1, 'Family Planning', 'Family Planning', '2019-03-26 15:02:46'),
(16, 'nutrition', '2019-03-26 15:02:46', 1, 'Food and Nutrition', 'Food and Nutrition', '2019-03-26 15:02:46'),
(17, 'Wajawazito', '2019-04-06 14:02:01', NULL, 'Pegnnat women', 'ajawazito', '2019-04-06 14:02:01');

-- --------------------------------------------------------

--
-- Table structure for table `service_indicator`
--

CREATE TABLE `service_indicator` (
  `indicator_id` bigint(20) NOT NULL,
  `service_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT NULL,
  `service_indicator_id` bigint(20) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `service_indicator`
--

INSERT INTO `service_indicator` (`indicator_id`, `service_id`, `created_at`, `is_active`, `service_indicator_id`, `updated_at`) VALUES
(1, 2, '2019-03-31 13:36:41', 1, 1, '2019-03-31 13:24:48'),
(2, 2, '2019-03-31 13:36:48', 1, 2, '2019-03-31 13:24:48'),
(3, 2, '2019-03-31 13:36:46', 1, 3, '2019-03-31 13:24:48'),
(4, 2, '2019-03-31 13:36:44', 1, 4, '2019-03-31 13:24:48'),
(5, 14, '2019-03-31 13:26:33', 1, 5, '2019-03-31 13:26:33'),
(6, 14, '2019-03-31 13:26:33', 1, 6, '2019-03-31 13:26:33'),
(7, 14, '2019-03-31 13:26:33', 1, 7, '2019-03-31 13:26:33'),
(8, 14, '2019-03-31 13:26:33', 1, 8, '2019-03-31 13:26:33'),
(9, 14, '2019-03-31 13:26:33', 1, 9, '2019-03-31 13:26:33'),
(10, 4, '2019-03-31 13:28:03', 1, 10, '2019-03-31 13:28:03'),
(11, 4, '2019-03-31 13:28:03', 1, 11, '2019-03-31 13:28:03'),
(12, 4, '2019-03-31 13:28:03', 1, 12, '2019-03-31 13:28:03'),
(13, 4, '2019-03-31 13:28:03', 1, 13, '2019-03-31 13:28:03'),
(14, 4, '2019-03-31 13:28:03', 1, 14, '2019-03-31 13:28:03'),
(15, 15, '2019-03-31 13:36:51', 1, 15, '2019-03-31 13:29:11'),
(16, 15, '2019-03-31 13:36:56', 1, 16, '2019-03-31 13:29:11'),
(17, 15, '2019-03-31 13:36:53', 1, 17, '2019-03-31 13:29:11'),
(18, 5, '2019-03-31 13:30:48', 1, 18, '2019-03-31 13:30:48'),
(19, 5, '2019-03-31 13:30:48', 1, 19, '2019-03-31 13:30:48'),
(20, 5, '2019-03-31 13:30:48', 1, 20, '2019-03-31 13:30:48'),
(21, 5, '2019-03-31 13:30:48', 1, 21, '2019-03-31 13:30:48'),
(23, 16, '2019-03-31 13:33:31', 1, 22, '2019-03-31 13:33:31'),
(24, 16, '2019-03-31 13:33:31', 1, 23, '2019-03-31 13:33:31'),
(25, 16, '2019-03-31 13:33:31', 1, 24, '2019-03-31 13:33:31'),
(26, 9, '2019-03-31 13:35:47', 1, 25, '2019-03-31 13:35:47'),
(27, 9, '2019-03-31 13:35:47', 1, 26, '2019-03-31 13:35:47'),
(28, 9, '2019-03-31 13:35:47', 1, 27, '2019-03-31 13:35:47'),
(29, 9, '2019-03-31 13:35:47', 1, 28, '2019-03-31 13:35:47'),
(30, 9, '2019-03-31 13:35:47', 1, 29, '2019-03-31 13:35:47'),
(31, 9, '2019-03-31 13:35:47', 1, 30, '2019-03-31 13:35:47'),
(32, 9, '2019-03-31 13:35:47', 1, 31, '2019-03-31 13:35:47'),
(33, 9, '2019-03-31 13:35:47', 1, 32, '2019-03-31 13:35:47');

-- --------------------------------------------------------

--
-- Table structure for table `status`
--

CREATE TABLE `status` (
  `id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `status`
--

INSERT INTO `status` (`id`, `created_at`, `is_active`, `name`, `notes`, `updated_at`) VALUES
(-1, '2019-04-03 14:54:32', 1, 'FAILED', 'FAILED', '2019-04-03 14:54:32'),
(0, '2019-04-03 14:55:16', 1, 'PENDING', 'PENDING', '2019-04-03 14:54:32'),
(1, '2019-04-03 14:55:21', 1, 'SUCCESSFUL', 'SUCCESSFUL', '2019-04-03 14:55:09');

-- --------------------------------------------------------

--
-- Table structure for table `TB_clients`
--

CREATE TABLE `TB_clients` (
  `tb_client_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_pregnant` tinyint(1) DEFAULT NULL,
  `makohozi` varchar(255) DEFAULT NULL,
  `other_tests_details` varchar(255) DEFAULT NULL,
  `outcome` varchar(255) DEFAULT NULL,
  `outcome_date` datetime DEFAULT NULL,
  `outcome_details` varchar(255) DEFAULT NULL,
  `patient_type` int(11) DEFAULT NULL,
  `referral_type` int(11) DEFAULT NULL,
  `test_type` int(11) DEFAULT NULL,
  `transfer_type` int(11) DEFAULT NULL,
  `treatment_type` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `veo` varchar(255) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `xray` varchar(255) DEFAULT NULL,
  `health_facility_client_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `TB_encounter`
--

CREATE TABLE `TB_encounter` (
  `tb_patient_id` bigint(20) NOT NULL,
  `appointment_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `encounter_month` int(11) DEFAULT NULL,
  `encounter_year` int(11) DEFAULT NULL,
  `has_finished_previous_month_medication` tinyint(1) DEFAULT NULL,
  `_id` bigint(20) DEFAULT NULL,
  `local_id` varchar(255) DEFAULT NULL,
  `makohozi` varchar(255) DEFAULT NULL,
  `medication_date` datetime DEFAULT NULL,
  `medication_status` tinyint(1) DEFAULT NULL,
  `scheduled_date` datetime DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `weight` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `TB_medication_regimes`
--

CREATE TABLE `TB_medication_regimes` (
  `medication_regime_name` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `_id` bigint(20) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `TB_test_type`
--

CREATE TABLE `TB_test_type` (
  `test_type_name` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `_id` bigint(20) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `unique_ids`
--

CREATE TABLE `unique_ids` (
  `_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `openmrs_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `used_by` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `appointment_type`
--
ALTER TABLE `appointment_type`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_r382kqamghm1sk6c9dfniaxkt` (`registration_reason_id`);

--
-- Indexes for table `client_appointments`
--
ALTER TABLE `client_appointments`
  ADD PRIMARY KEY (`health_facility_client_id`,`appointment_date`),
  ADD UNIQUE KEY `UK_fhs6f0dci14f3hpwcvx6ewl1k` (`id`),
  ADD KEY `FK_4is04y2gingnv2rede9q38due` (`health_facility_client_id`),
  ADD KEY `FK_6p0n70dl5vq35d6iyt006eqrk` (`appointment_type`),
  ADD KEY `FK_a0xhcxlnybwvpyf58bnetijmg` (`status`);

--
-- Indexes for table `client_referral_indicator`
--
ALTER TABLE `client_referral_indicator`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_qskel1v9dqworthqw6ymskia0` (`referral_id`),
  ADD KEY `FK_1f51qktgan96dhf52pwugf3lp` (`indicator_id`,`service_id`);

--
-- Indexes for table `health_facilities`
--
ALTER TABLE `health_facilities`
  ADD PRIMARY KEY (`openmrs_UUID`),
  ADD UNIQUE KEY `UK_4meeb45ygvc81m41d1bqifqvn` (`facility_ctc_code`),
  ADD UNIQUE KEY `UK_4woe1mlf58jf0n7estf3eysua` (`HFR_code`);

--
-- Indexes for table `health_facility_clients`
--
ALTER TABLE `health_facility_clients`
  ADD PRIMARY KEY (`health_facility_client_id`),
  ADD KEY `FK_re09lr25wm726naab2vroymew` (`client_id`);

--
-- Indexes for table `indicator`
--
ALTER TABLE `indicator`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_87vulrsp8m827ifq4b85hgueu` (`name`),
  ADD UNIQUE KEY `UK_88v992wxd8qb0bs0fshyqs2xg` (`name_sw`);

--
-- Indexes for table `push_notifications_users`
--
ALTER TABLE `push_notifications_users`
  ADD PRIMARY KEY (`_id`),
  ADD UNIQUE KEY `UK_t1dfmggf2le64shh5pyn4xuio` (`google_push_notification_token`),
  ADD KEY `FK_bb9vuupw4nowun9i14emt2s78` (`facility_uuid`);

--
-- Indexes for table `referral`
--
ALTER TABLE `referral`
  ADD PRIMARY KEY (`referral_id`),
  ADD UNIQUE KEY `UK_a39y1iph0cmonqatnebk1gxdr` (`instance_id`),
  ADD KEY `FK_9qvj0bnaqrkxnmvgtgc27nreb` (`client_id`),
  ADD KEY `FK_mge1k796q8bdboyikwrvyhtu5` (`referral_feedback_id`),
  ADD KEY `FK_jjgy4ctrdlrt26n9l6m443swv` (`referral_type`);

--
-- Indexes for table `referral_feedback`
--
ALTER TABLE `referral_feedback`
  ADD PRIMARY KEY (`_id`),
  ADD KEY `FK_qmi5hae7yecgst8fl4jytfu8i` (`referral_type_id`);

--
-- Indexes for table `referral_type`
--
ALTER TABLE `referral_type`
  ADD PRIMARY KEY (`referral_type_id`),
  ADD UNIQUE KEY `UK_c4vp57a4m7jamd9yc7k43c1c3` (`referral_type_name`);

--
-- Indexes for table `registration_reasons`
--
ALTER TABLE `registration_reasons`
  ADD PRIMARY KEY (`registration_id`);

--
-- Indexes for table `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_3fdbfgw6oo8g5ivmiyk7tssmo` (`service_name`),
  ADD UNIQUE KEY `UK_le0kw3idtqahm1230kss4yr98` (`service_name_sw`);

--
-- Indexes for table `service_indicator`
--
ALTER TABLE `service_indicator`
  ADD PRIMARY KEY (`indicator_id`,`service_id`),
  ADD KEY `FK_6lo6rs3dyd16rltkwauvnr9gf` (`indicator_id`),
  ADD KEY `FK_tdxmtx9dgbatvwtq6ds0ogqiw` (`service_id`),
  ADD KEY `service_indicator_id` (`service_indicator_id`);

--
-- Indexes for table `status`
--
ALTER TABLE `status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `TB_clients`
--
ALTER TABLE `TB_clients`
  ADD PRIMARY KEY (`tb_client_id`),
  ADD UNIQUE KEY `UK_9wexieak1ox0apqgvu876pwjo` (`health_facility_client_id`),
  ADD KEY `FK_9wexieak1ox0apqgvu876pwjo` (`health_facility_client_id`);

--
-- Indexes for table `TB_encounter`
--
ALTER TABLE `TB_encounter`
  ADD PRIMARY KEY (`tb_patient_id`,`appointment_id`);

--
-- Indexes for table `TB_medication_regimes`
--
ALTER TABLE `TB_medication_regimes`
  ADD PRIMARY KEY (`medication_regime_name`);

--
-- Indexes for table `TB_test_type`
--
ALTER TABLE `TB_test_type`
  ADD PRIMARY KEY (`test_type_name`);

--
-- Indexes for table `unique_ids`
--
ALTER TABLE `unique_ids`
  ADD PRIMARY KEY (`_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `appointment_type`
--
ALTER TABLE `appointment_type`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `clients`
--
ALTER TABLE `clients`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `client_appointments`
--
ALTER TABLE `client_appointments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `client_referral_indicator`
--
ALTER TABLE `client_referral_indicator`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `health_facility_clients`
--
ALTER TABLE `health_facility_clients`
  MODIFY `health_facility_client_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `indicator`
--
ALTER TABLE `indicator`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;
--
-- AUTO_INCREMENT for table `push_notifications_users`
--
ALTER TABLE `push_notifications_users`
  MODIFY `_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `referral`
--
ALTER TABLE `referral`
  MODIFY `referral_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `referral_feedback`
--
ALTER TABLE `referral_feedback`
  MODIFY `_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `referral_type`
--
ALTER TABLE `referral_type`
  MODIFY `referral_type_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `registration_reasons`
--
ALTER TABLE `registration_reasons`
  MODIFY `registration_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT for table `service`
--
ALTER TABLE `service`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT for table `service_indicator`
--
ALTER TABLE `service_indicator`
  MODIFY `service_indicator_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
--
-- AUTO_INCREMENT for table `status`
--
ALTER TABLE `status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `TB_clients`
--
ALTER TABLE `TB_clients`
  MODIFY `tb_client_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `unique_ids`
--
ALTER TABLE `unique_ids`
  MODIFY `_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `clients`
--
ALTER TABLE `clients`
  ADD CONSTRAINT `FK_r382kqamghm1sk6c9dfniaxkt` FOREIGN KEY (`registration_reason_id`) REFERENCES `registration_reasons` (`registration_id`);

--
-- Constraints for table `client_appointments`
--
ALTER TABLE `client_appointments`
  ADD CONSTRAINT `FK_4is04y2gingnv2rede9q38due` FOREIGN KEY (`health_facility_client_id`) REFERENCES `health_facility_clients` (`health_facility_client_id`),
  ADD CONSTRAINT `FK_6p0n70dl5vq35d6iyt006eqrk` FOREIGN KEY (`appointment_type`) REFERENCES `appointment_type` (`id`),
  ADD CONSTRAINT `FK_a0xhcxlnybwvpyf58bnetijmg` FOREIGN KEY (`status`) REFERENCES `status` (`id`);

--
-- Constraints for table `client_referral_indicator`
--
ALTER TABLE `client_referral_indicator`
  ADD CONSTRAINT `FK_1f51qktgan96dhf52pwugf3lp` FOREIGN KEY (`indicator_id`,`service_id`) REFERENCES `service_indicator` (`indicator_id`, `service_id`),
  ADD CONSTRAINT `FK_qskel1v9dqworthqw6ymskia0` FOREIGN KEY (`referral_id`) REFERENCES `referral` (`referral_id`);

--
-- Constraints for table `health_facility_clients`
--
ALTER TABLE `health_facility_clients`
  ADD CONSTRAINT `FK_re09lr25wm726naab2vroymew` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`);

--
-- Constraints for table `push_notifications_users`
--
ALTER TABLE `push_notifications_users`
  ADD CONSTRAINT `FK_bb9vuupw4nowun9i14emt2s78` FOREIGN KEY (`facility_uuid`) REFERENCES `health_facilities` (`openmrs_UUID`);

--
-- Constraints for table `referral`
--
ALTER TABLE `referral`
  ADD CONSTRAINT `FK_9qvj0bnaqrkxnmvgtgc27nreb` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`),
  ADD CONSTRAINT `FK_jjgy4ctrdlrt26n9l6m443swv` FOREIGN KEY (`referral_type`) REFERENCES `referral_type` (`referral_type_id`),
  ADD CONSTRAINT `FK_mge1k796q8bdboyikwrvyhtu5` FOREIGN KEY (`referral_feedback_id`) REFERENCES `referral_feedback` (`_id`);

--
-- Constraints for table `referral_feedback`
--
ALTER TABLE `referral_feedback`
  ADD CONSTRAINT `FK_qmi5hae7yecgst8fl4jytfu8i` FOREIGN KEY (`referral_type_id`) REFERENCES `referral_type` (`referral_type_id`);

--
-- Constraints for table `service_indicator`
--
ALTER TABLE `service_indicator`
  ADD CONSTRAINT `FK_6lo6rs3dyd16rltkwauvnr9gf` FOREIGN KEY (`indicator_id`) REFERENCES `indicator` (`id`),
  ADD CONSTRAINT `FK_tdxmtx9dgbatvwtq6ds0ogqiw` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`);

--
-- Constraints for table `TB_clients`
--
ALTER TABLE `TB_clients`
  ADD CONSTRAINT `FK_9wexieak1ox0apqgvu876pwjo` FOREIGN KEY (`health_facility_client_id`) REFERENCES `health_facility_clients` (`health_facility_client_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
