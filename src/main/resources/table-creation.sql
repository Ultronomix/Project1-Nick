DROP TABLE ers_reimbursements;

CREATE TABLE ers_reimbursements(
	reimb_id varchar PRIMARY KEY,
	amount decimal NOT NULL,
	submitted timestamp NOT NULL,
	resolved timestamp NOT NULL,
	description varchar NOT NULL,
	payment_id varchar,
	author_id varchar NOT NULL,
	resolver_id varchar,
	status_id varchar NOT NULL,
	type_id varchar NOT NULL
);

ALTER TABLE ers_reimbursements
ADD FOREIGN KEY (author_id)
REFERENCES ers_users(user_id);

ALTER TABLE ers_reimbursements
ADD FOREIGN KEY (resolver_id)
REFERENCES ers_users;

ALTER TABLE ers_reimbursements
ADD FOREIGN KEY (status_id)
REFERENCES ers_reimbursement_statuses(status_id);

ALTER TABLE ers_reimbursements
ADD FOREIGN KEY (type_id)
REFERENCES ers_reimbursement_types(type_id);



DROP TABLE ers_users;

CREATE TABLE ers_users (
	user_id varchar PRIMARY KEY,
	username varchar UNIQUE NOT NULL,
	email varchar UNIQUE NOT NULL,
	PASSWORD varchar NOT NULL,
	given_name varchar NOT NULL,
	surname varchar NOT NULL,
	is_active BIT, 
	role_id varchar
);

ALTER TABLE ers_users
ADD FOREIGN KEY (role_id)
REFERENCES ers.ers_user_roles(role_id);

CREATE TABLE ers_reimbursement_statuses (
	status_id varchar PRIMARY KEY,
	status varchar UNIQUE
);

CREATE TABLE ers_reimbursement_types (
	type_id varchar PRIMARY KEY,
	TYPE varchar UNIQUE
);

DROP TABLE ers_user_roles;

CREATE TABLE ers_user_roles (
	role_id varchar PRIMARY KEY,
	ROLE varchar UNIQUE
);

SELECT *
FROM ers_reimbursement_statuses ers ;

SELECT *
FROM ers_reimbursement_types ert;

SELECT *
FROM ers_user_roles eur;

SELECT *
FROM ers_users eu;