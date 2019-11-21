create table branch (
	branch_location varchar(50) not null,
	branch_city varchar(20) not null,
	PRIMARY KEY (branch_location, branch_city));

create table vehicletypes (
	vehicletypes_name varchar(20) not null PRIMARY KEY,
	vehicletypes_wrate integer,
	vehicletypes_drate integer,
	vehicletypes_hrate integer,
	vehicletypes_wirate integer,
	vehicletypes_dirate integer,
	vehicletypes_krate integer);

create table customers (
	customers_cellphone varchar(25),
	customers_name varchar(20),
	customers_address varchar (50),
	customers_dlicense varchar(20) not null PRIMARY KEY);

create table timeperiod (
	timeperiod_fromdate date,
	timeperiod_fromtime timestamp,
	timeperiod_todate date,
	timeperiod_totime timestamp,
	PRIMARY KEY (timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime));

create table vehicles (
	vehicles_id integer,
	vehicles_license varchar(20) not null PRIMARY KEY,
	vehicles_make varchar(20),
	vehicles_model varchar(10),
	vehicles_year integer,
	vehicles_color char,
	vehicles_odometer integer,
	vehicles_status varchar(20) not null,
	vehicletypes_name varchar(20) not null,
	branch_location varchar(50) not null,
	branch_city varchar(20) not null,
	CONSTRAINT FK_Branch FOREIGN KEY (branch_location, branch_city) references branch(branch_location, branch_city) ON DELETE CASCADE,
	CONSTRAINT FK_VehicleTypeNameII FOREIGN KEY (vehicletypes_name) references vehicletypes(vehicletypes_name) ON DELETE CASCADE);

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


create table returns (
	rentals_id integer not null PRIMARY KEY,
	returns_date date,
	returns_time timestamp,
	returns_odometer integer,
	returns_fulltank char(10),
	returns_value integer,
	CONSTRAINT FK_RentalID FOREIGN KEY (rentals_id) references rentals(rentals_id) ON DELETE CASCADE);

commit;
