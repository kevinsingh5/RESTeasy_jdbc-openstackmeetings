create database openstack_meetings

use openstack_meetings

create table projects(name varchar(255) NOT NULL, description varchar(255) NOT NULL, project_id int NOT NULL AUTO_INCREMENT, PRIMARY KEY(project_id));
