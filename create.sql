
-- Starting fresh each run, dropping all tables along with their constraints
drop table branch cascade constraints;
drop table timeperiod cascade constraints;
drop table reservations cascade constraints;
drop table rentals cascade constraints;
drop table vehicles cascade constraints;
drop table vehicletypes cascade constraints;
drop table customers cascade constraints;
drop table returns cascade constraints;


-- Creating tables from here!
-- Creating location branch table
create table branch (
	branch_location varchar(50) not null,
	branch_city varchar(20) not null,
	PRIMARY KEY (branch_location, branch_city));

-- Creating Vehicle Type Table
create table vehicletypes (
	vehicletypes_name varchar(20) not null PRIMARY KEY,
	vehicletypes_wrate integer,
	vehicletypes_drate integer,
	vehicletypes_hrate integer,
	vehicletypes_wirate integer,
	vehicletypes_dirate integer,
	vehicletypes_krate integer);

-- Creating Customers Table
create table customers (
	customers_cellphone varchar(25),
	customers_name varchar(20),
	customers_address varchar (50),
	customers_dlicense varchar(20) not null PRIMARY KEY);

-- Creating Timeperiods table
create table timeperiod (
	timeperiod_fromdate date,
	timeperiod_fromtime timestamp,
	timeperiod_todate date,
	timeperiod_totime timestamp,
	PRIMARY KEY (timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime));

-- Creating Vehicles table
create table vehicles (
	vehicles_id integer,
	vehicles_license varchar(20) not null PRIMARY KEY,
	vehicles_make varchar(20),
	vehicles_model varchar(10),
	vehicles_year integer,
	vehicles_color varchar(20),
	vehicles_odometer integer,
	vehicles_status varchar(20) not null,
	vehicletypes_name varchar(20) not null,
	branch_location varchar(50) not null,
	branch_city varchar(20) not null,
	CONSTRAINT FK_Branch FOREIGN KEY (branch_location, branch_city) references branch(branch_location, branch_city) ON DELETE CASCADE,
	CONSTRAINT FK_VehicleTypeNameII FOREIGN KEY (vehicletypes_name) references vehicletypes(vehicletypes_name) ON DELETE CASCADE);

-- Creating Reservations table
create table reservations (
	reservations_confNo integer not null PRIMARY KEY,
	vehicletypes_name varchar(20) not null,
	customers_dlicense varchar(20) not null,
	timeperiod_fromdate date,
	timeperiod_fromtime timestamp,
	timeperiod_todate date,
	timeperiod_totime timestamp,
	CONSTRAINT FK_VehicleTypeName FOREIGN KEY (vehicletypes_name) references vehicletypes(vehicletypes_name) ON DELETE CASCADE,
	CONSTRAINT FK_CustomerDLicense FOREIGN KEY (customers_dlicense) references customers(customers_dlicense) ON DELETE CASCADE,
	CONSTRAINT FK_TimePeriods FOREIGN KEY (timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime) references timeperiod(timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime) ON DELETE CASCADE);

-- Creating Rental table
create table rentals (
	rentals_id integer not null PRIMARY KEY,
	vehicles_license varchar(20) not null,
	customers_dlicense varchar(20) not null,
	timeperiod_fromdate date,
	timeperiod_fromtime timestamp,
	timeperiod_todate date,
	timeperiod_totime timestamp,
	rentals_odometer integer,
	rentals_cardname varchar(50),
	rentals_cardno integer,
	rentals_expdate date,
	reservations_confNo integer,
	CONSTRAINT FK_VehicleLicense FOREIGN KEY (vehicles_license) references vehicles(vehicles_license) ON DELETE CASCADE,
	CONSTRAINT FK_CustomerDLicenseI FOREIGN KEY (customers_dlicense) references customers(customers_dlicense) ON DELETE CASCADE,
	CONSTRAINT FK_TimePeriodsI FOREIGN KEY (timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime) references timeperiod(timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime) ON DELETE CASCADE,
	CONSTRAINT FK_ReservationConfNo FOREIGN KEY (reservations_confNo) references reservations(reservations_confNo) ON DELETE CASCADE);

-- Creating Return table
create table returns (
	rentals_id integer not null PRIMARY KEY,
	returns_date date,
	returns_time timestamp,
	returns_odometer integer,
	returns_fulltank char(10),
	returns_value integer,
	CONSTRAINT FK_RentalID FOREIGN KEY (rentals_id) references rentals(rentals_id) ON DELETE CASCADE);


-- Inserting rows, populating data for demo!
-- Creating Location Branches, 2 branches to save some headaches from demo
insert into branch values ('Main', 'Vancouver');
insert into branch values ('Richmond Centre', 'Richmond');

-- types of vehicles with Week / Day / Hour / Wi / Di / Kilo rates
insert into vehicletypes values ('SUV', 10, 8, 6, 12, 14, 11);
insert into vehicletypes values ('ECONOMY', 9, 7, 5, 11, 13, 10);
insert into vehicletypes values ('COMPACT', 9, 9, 8, 11, 12, 12);
insert into vehicletypes values ('STANDARD', 10, 8, 6, 12, 14, 11);
insert into vehicletypes values ('FULL', 11, 9, 7, 13, 15, 12);
insert into vehicletypes values ('TRUCK', 10, 8, 6, 12, 14, 11);

-- Customers with reservations
insert into customers values ('6047776666', 'Jim', '1111 Vancouver St', 'D101010');
insert into customers values ('6047776667', 'Pat', '1112 Surrey St', 'D101011');
insert into customers values ('6047776668', 'Sam', '1113 Burnaby St', 'D101020');
insert into customers values ('6047776669', 'Jake', '1141 Cedar St', 'D101021');
insert into customers values ('6047776660', 'Yell', '1115 Columbus St', 'D101030');
insert into customers values ('6047736660', 'Yolo', '1215 Columbus St', 'D101031');
-- Customers without reservations
insert into customers values ('6047776666', 'Jim', '1111 Vancouver St', 'D111010');
insert into customers values ('6047756666', 'Jem', '1112 Surrey St', 'D121010');
insert into customers values ('6047636666', 'Jam', '1131 Burnaby St', 'D131010');
insert into customers values ('6047436666', 'Jom', '1511 Cedar St', 'D141010');
insert into customers values ('6047236666', 'Jeem', '6111 Common St', 'D151010');

-- TimePeriods (For Currently rented, past rents, can add more)
-- Current
insert into timeperiod values(TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into timeperiod values(TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 06:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into timeperiod values(TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 07:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into timeperiod values(TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 08:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into timeperiod values(TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 09:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into timeperiod values(TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 10:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));

-- Past
insert into timeperiod values(TO_DATE ('2019-06-01', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-01 09:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-06-05', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-05 09:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into timeperiod values(TO_DATE ('2019-06-02', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-02 10:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-06-06', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-06 12:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into timeperiod values(TO_DATE ('2019-06-04', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-04 06:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-06-09', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-09 18:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into timeperiod values(TO_DATE ('2019-07-02', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-07-02 08:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-07-09', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-07-09 19:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into timeperiod values(TO_DATE ('2019-08-01', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-08-01 01:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-10-07', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-10-07 23:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));

-- Vehicles for Main, Vancouver that's AVAILABLE. 10 query items each
-- SUV
insert into vehicles values (1000, 'a0a0a0', 'Honda', 'Civic', 2002, 'Red', 1111, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1001, 'a0a0a1', 'Honda', 'CRV', 2003, 'Blue', 1112, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1002, 'a0a0a2', 'Honda', 'Mini', 2004, 'Green', 1113, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1003, 'a0a0a3', 'Toyota', 'Camry', 2005, 'Red', 1114, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1004, 'a0a0a4', 'Toyota', 'Rav4', 2006, 'Blue', 1115, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1005, 'a0a0a5', 'Toyota', 'Tundra', 2007, 'Green', 1116, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1006, 'a0a0a6', 'Nissan', 'Sentra', 2003, 'Red', 1117, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1007, 'a0a0a7', 'Nissan', 'GTR', 2004, 'Blue', 1118, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1008, 'a0a0a8', 'Nissan', 'Roller', 2005, 'Green', 1119, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1010, 'a0a0a9', 'Honda', 'Civic', 2003, 'Red', 2111, 'AVAILABLE', 'SUV', 'Main', 'Vancouver');
-- ECONOMY
insert into vehicles values (1011, 'a0a0b1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1012, 'a0a0b2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1013, 'a0a0b3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1014, 'a0a0b4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1015, 'a0a0b5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1016, 'a0a0b6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1017, 'a0a0b7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1018, 'a0a0b8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1019, 'a0a0b9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1020, 'a0a0b0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'ECONOMY', 'Main', 'Vancouver');
-- COMPACT
insert into vehicles values (1021, 'a0a0c1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1022, 'a0a0c2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1023, 'a0a0c3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1024, 'a0a0c4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1025, 'a0a0c5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1026, 'a0a0c6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1027, 'a0a0c7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1028, 'a0a0c8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1029, 'a0a0c9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1030, 'a0a0c0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'COMPACT', 'Main', 'Vancouver');
-- STANDARD
insert into vehicles values (1031, 'a0a0d1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1032, 'a0a0d2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1033, 'a0a0d3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1034, 'a0a0d4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1035, 'a0a0d5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1036, 'a0a0d6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1037, 'a0a0d7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1038, 'a0a0d8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1039, 'a0a0d9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1040, 'a0a0d0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'STANDARD', 'Main', 'Vancouver');
-- FULL
insert into vehicles values (1041, 'a0a0e1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1042, 'a0a0e2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1043, 'a0a0e3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1044, 'a0a0e4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1045, 'a0a0e5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1046, 'a0a0e6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1047, 'a0a0e7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1048, 'a0a0e8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1049, 'a0a0e9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1050, 'a0a0e0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'FULL', 'Main', 'Vancouver');
-- TRUCK
insert into vehicles values (1051, 'a0a0f1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1052, 'a0a0f2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1053, 'a0a0f3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1054, 'a0a0f4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1055, 'a0a0f5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1056, 'a0a0f6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1057, 'a0a0f7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1058, 'a0a0f8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1059, 'a0a0f9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1060, 'a0a0f0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'TRUCK', 'Main', 'Vancouver');
-- NOT AVAILABLE vehicles
insert into vehicles values (1061, 'a0a0g1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'RENTED', 'SUV', 'Main', 'Vancouver');
insert into vehicles values (1062, 'a0a0g2', 'Honda', 'Mini', 2005, 'Green', 2113, 'RENTED', 'ECONOMY', 'Main', 'Vancouver');
insert into vehicles values (1063, 'a0a0g3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'RENTED', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1064, 'a0a0g4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'RENTED', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1065, 'a0a0g5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'RENTED', 'TRUCK', 'Main', 'Vancouver');
insert into vehicles values (1066, 'a0a0g6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'MAINTANANCE', 'COMPACT', 'Main', 'Vancouver');
insert into vehicles values (1067, 'a0a0g7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'MAINTANANCE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1068, 'a0a0g8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'MAINTANANCE', 'FULL', 'Main', 'Vancouver');
insert into vehicles values (1069, 'a0a0g9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'MAINTANANCE', 'STANDARD', 'Main', 'Vancouver');
insert into vehicles values (1070, 'a0a0g0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'MAINTANANCE', 'FULL', 'Main', 'Vancouver');


-- Vehicles for Richmond Centre, Richmond that's AVAILABLE - 10 query items each
insert into vehicles values (2000, 'b0a0a0', 'Honda', 'Civic', 2002, 'Red', 1111, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2001, 'b0a0a1', 'Honda', 'CRV', 2003, 'Blue', 1112, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2002, 'b0a0a2', 'Honda', 'Mini', 2004, 'Green', 1113, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2003, 'b0a0a3', 'Toyota', 'Camry', 2005, 'Red', 1114, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2004, 'b0a0a4', 'Toyota', 'Rav4', 2006, 'Blue', 1115, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2005, 'b0a0a5', 'Toyota', 'Tundra', 2007, 'Green', 1116, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2006, 'b0a0a6', 'Nissan', 'Sentra', 2003, 'Red', 1117, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2007, 'b0a0a7', 'Nissan', 'GTR', 2004, 'Blue', 1118, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2008, 'b0a0a8', 'Nissan', 'Roller', 2005, 'Green', 1119, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2010, 'b0a0a9', 'Honda', 'Civic', 2003, 'Red', 2111, 'AVAILABLE', 'SUV', 'Richmond Centre', 'Richmond');
-- ECONOMY
insert into vehicles values (2011, 'b0a0b1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2012, 'b0a0b2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2013, 'b0a0b3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2014, 'b0a0b4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2015, 'b0a0b5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2016, 'b0a0b6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2017, 'b0a0b7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2018, 'b0a0b8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2019, 'b0a0b9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2020, 'b0a0b0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'ECONOMY', 'Richmond Centre', 'Richmond');
-- COMPACT
insert into vehicles values (2021, 'b0a0c1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2022, 'b0a0c2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2023, 'b0a0c3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2024, 'b0a0c4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2025, 'b0a0c5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2026, 'b0a0c6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2027, 'b0a0c7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2028, 'b0a0c8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2029, 'b0a0c9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2030, 'b0a0c0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'COMPACT', 'Richmond Centre', 'Richmond');
-- STANDARD
insert into vehicles values (2031, 'b0a0d1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2032, 'b0a0d2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2033, 'b0a0d3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2034, 'b0a0d4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2035, 'b0a0d5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2036, 'b0a0d6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2037, 'b0a0d7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2038, 'b0a0d8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2039, 'b0a0d9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2040, 'b0a0d0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'STANDARD', 'Richmond Centre', 'Richmond');
-- FULL
insert into vehicles values (2041, 'b0a0e1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2042, 'b0a0e2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2043, 'b0a0e3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2044, 'b0a0e4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2045, 'b0a0e5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2046, 'b0a0e6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2047, 'b0a0e7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2048, 'b0a0e8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2049, 'b0a0e9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2050, 'b0a0e0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'FULL', 'Richmond Centre', 'Richmond');
-- TRUCk
insert into vehicles values (2051, 'b0a0f1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2052, 'b0a0f2', 'Honda', 'Mini', 2005, 'Green', 2113, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2053, 'b0a0f3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2054, 'b0a0f4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2055, 'b0a0f5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2056, 'b0a0f6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2057, 'b0a0f7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2058, 'b0a0f8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2059, 'b0a0f9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2060, 'b0a0f0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'AVAILABLE', 'TRUCK', 'Richmond Centre', 'Richmond');
-- NOT AVAILABLE vehicles
insert into vehicles values (2061, 'b0a0g1', 'Honda', 'CRV', 2004, 'Blue', 2112, 'RENTED', 'SUV', 'Richmond Centre', 'Richmond');
insert into vehicles values (2062, 'b0a0g2', 'Honda', 'Mini', 2005, 'Green', 2113, 'RENTED', 'ECONOMY', 'Richmond Centre', 'Richmond');
insert into vehicles values (2063, 'b0a0g3', 'Toyota', 'Camry', 2006, 'Red', 2114, 'RENTED', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2064, 'b0a0g4', 'Toyota', 'Rav4', 2007, 'Blue', 2115, 'RENTED', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2065, 'b0a0g5', 'Toyota', 'Tundra', 2008, 'Green', 2116, 'RENTED', 'TRUCK', 'Richmond Centre', 'Richmond');
insert into vehicles values (2066, 'b0a0g6', 'Nissan', 'Sentra', 2004, 'Red', 2117, 'RENTED', 'COMPACT', 'Richmond Centre', 'Richmond');
insert into vehicles values (2067, 'b0a0g7', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'MAINTANANCE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2068, 'b0a0g8', 'Nissan', 'Roller', 2002, 'Green', 2119, 'MAINTANANCE', 'FULL', 'Richmond Centre', 'Richmond');
insert into vehicles values (2069, 'b0a0g9', 'Nissan', 'GTR', 2003, 'Blue', 2118, 'MAINTANANCE', 'STANDARD', 'Richmond Centre', 'Richmond');
insert into vehicles values (2070, 'b0a0g0', 'Nissan', 'Roller', 2002, 'Green', 2119, 'MAINTANANCE', 'FULL', 'Richmond Centre', 'Richmond');

-- Reservation (Conf Num / type / Dlicence / times)
-- Past Reservations
insert into reservations values (10000, 'SUV', 'D101010', TO_DATE ('2019-06-01', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-01 09:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-06-05', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-05 09:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into reservations values (10001, 'ECONOMY', 'D101011',  TO_DATE ('2019-06-02', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-02 10:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-06-06', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-06 12:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into reservations values (10002, 'COMPACT', 'D101020', TO_DATE ('2019-06-04', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-04 06:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-06-09', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-06-09 18:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into reservations values (10003, 'STANDARD', 'D101021', TO_DATE ('2019-07-02', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-07-02 08:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-07-09', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-07-09 19:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into reservations values (10004, 'TRUCK', 'D101030', TO_DATE ('2019-08-01', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-08-01 01:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-10-07', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-10-07 23:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));

-- Currently Rented Reservations
insert into reservations values (10010, 'SUV', 'D101010', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into reservations values (10011, 'ECONOMY', 'D101011', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 06:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into reservations values (10012, 'COMPACT', 'D101020', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 07:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into reservations values (10013, 'STANDARD', 'D101021', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 08:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into reservations values (10014, 'TRUCK', 'D101030', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 09:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));
insert into reservations values (10015, 'FULL', 'D101031', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 10:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'));


-- Rentals for Main location - these have a reservation
insert into rentals values (10000, 'a0a0g1', 'D101010', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), 1000, 'VISA', '1111', TO_DATE ('2019-12-01', 'YYYY-MM-DD'), 10010);
insert into rentals values (10001, 'a0a0g2', 'D101011', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 06:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), 1000, 'VISA', '1112', TO_DATE ('2019-12-01', 'YYYY-MM-DD'), 10011);
insert into rentals values (10003, 'a0a0g3', 'D101021', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 08:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), 1000, 'VISA', '1114', TO_DATE ('2019-12-01', 'YYYY-MM-DD'), 10013);

-- Rentals for Richmond location - these have a reservation
insert into rentals values (10002, 'b0a0g6', 'D101020', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 07:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), 1000, 'VISA', '1113', TO_DATE ('2019-12-01', 'YYYY-MM-DD'), 10012);
insert into rentals values (10004, 'b0a0g5', 'D101030', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 09:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), 1000, 'VISA', '1115', TO_DATE ('2019-12-01', 'YYYY-MM-DD'), 10014);
insert into rentals values (10005, 'b0a0g4', 'D101031', TO_DATE ('2019-11-20', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-20 05:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), TO_DATE ('2019-11-25', 'YYYY-MM-DD'), TO_TIMESTAMP('2019-11-25 10:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF'), 1000, 'VISA', '1116', TO_DATE ('2019-12-01', 'YYYY-MM-DD'), 10015);

commit work;

