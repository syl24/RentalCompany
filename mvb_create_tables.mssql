create table branch ( 
	branch_id integer not null PRIMARY KEY,
	branch_name varchar2(20) not null,
	branch_addr varchar2(50),
	branch_city varchar2(20) not null,
	branch_phone integer 
);

create table driver (
	driver_sin integer not null PRIMARY KEY,
	driver_name varchar(20) not null,
	driver_addr varchar(50) not null,
	driver_city varchar(20) not null,
	driver_birthdate date not null,
	driver_phone integer
);

create table license ( 
	license_no NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	driver_sin integer not null,
	license_type char not null,
	license_class integer,
	license_expiry date not null,
	issue_date date not null,
	branch_id integer not null,
	foreign key (driver_sin) references driver,
	foreign key (branch_id) references branch
);

create table exam (
	driver_sin integer not null,
	branch_id integer not null,
	exam_date date not null,
	exam_type char not null,
	exam_score integer,
	PRIMARY KEY (driver_sin, branch_id, exam_date),
	foreign key (driver_sin) references driver,
	foreign key (branch_id) references branch
);

commit;
