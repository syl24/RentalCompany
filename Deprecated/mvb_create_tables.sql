create table branch ( 
	branch_location varchar(50) not null,
	branch_city varchar(20) not null,
	PRIMARY KEY (branch_location, branch_city) 
);

create table reservations (
	reservations_confNo integer not null PRIMARY KEY,
	vehicletypes_name varchar(20) not null,
	customers_dlicense varchar(20) not null,
	timeperiod_fromdate date not null,
	timeperiod_fromtime time not null,
	timeperiod_todate date not null,
	timeperiod_totime time not null,
	foreign key (vehicletypes_name) references vehicletypes(vehicletypes_name) ON UPDATE CASCADE ON DELETE CASCADE,
	foreign key (customers_dlicense) references customers(customers_dlicense) ON UPDATE CASCADE ON DELETE CASCADE,
	foreign key (timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime) references timeperiod(timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime) ON UPDATE CASCADE
);

create table timeperiod (
	timeperiod_fromdate date not null,
	timeperiod_fromtime time not null,
	timeperiod_todate date not null,
	timeperiod_totime time not null,
	PRIMARY KEY (timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime) ON UPDATE CASCADE
);

create table rentals (
	rentals_id integer not null PRIMARY KEY,
	vehicles_id integer not null,
	customers_dlicense varchar(20) not null,
	timeperiod_fromdate date not null,
	timeperiod_fromtime time not null,
	timeperiod_todate date not null,
	timeperiod_totime time not null,
	rentals_odometer integer,
	rentals_cardname varchar,
	rentals_cardno integer,
	rentals_expdate date,
	reservations_confNo integer,
	foreign key (vehicles_id) references vehicles(vehicles_id) ON UPDATE CASCADE ON DELETE CASCADE,
	foreign key (customers_dlicense) references customers(customers_dlicense) ON UPDATE CASCADE ON DELETE CASCADE,
	foreign key (timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime) references timeperiod(timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime) ON UPDATE CASCADE,
	foreign key (reservations_confNo) references reservations(reservations_confNo) ON UPDATE CASCADE ON DELETE CASCADE
);

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
	foreign key (branch_location, branch_city) references branch(branch_location, branch_city) ON UPDATE CASCADE ON DELETE CASCADE,
	foreign key (vehicletypes_name) references vehicletypes(vehicletypes_name) ON UPDATE CASCADE ON DELETE CASCADE
);

create table vehicletypes (
	vehicletypes_name varchar(20) not null PRIMARY KEY,
	vehicletypes_features varchar(20),
	vehicletypes_wrate integer,
	vehicletypes_drate integer,
	vehicletypes_hrate integer,
	vehicletypes_wirate integer,
	vehicletypes_dirate integer,
	vehicletypes_krate integer,
);

create table customers (
	customers_cellphone varchar(25),
	customers_name varchar(20),
	customers_address varchar (50),
	customers_dlicense varchar(20) not null PRIMARY KEY
);

create table returns (
	rentals_id integer not null PRIMARY KEY,
	returns_date date,
	returns_time time,
	returns_odometer integer,
	returns_fulltank char(5),
	returns_value integer,
	foreign key (rentals_id) references rentals(rentals_id) ON UPDATE CASCADE ON DELETE CASCADE
);

commit;
