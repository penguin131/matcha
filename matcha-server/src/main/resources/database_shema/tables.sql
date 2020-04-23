
create table t_user_profile
(
	user_profile_id int not null,
	sex boolean not null,
	sex_preferences int null,
	biography varchar(2048) null,
	constraint t_user_profile_pk
		primary key (user_profile_id)
);

create table t_pictures
(
	picture_id int not null,
	picture blob not null,
	user_profile int null,
	constraint t_pictures_pk
		primary key (picture_id),
	constraint t_pictures___fk
		foreign key (user_profile) references t_user_profile (user_profile_id)
		    on delete cascade
);

create table t_cool
(
	cool_id int not null,
	picture int not null,
	constraint t_cool_pk
		primary key (cool_id),
	constraint t_cool_t_pictures_picture_id_fk
		foreign key (picture) references t_pictures (picture_id)
			on delete cascade
);

create table t_hashtag
(
	hashtag_id int not null,
	name varchar(255) not null,
	profile int null,
	constraint t_hashtag_pk
		primary key (hashtag_id),
	constraint t_hashtag_t_user_profile_user_profile_id_fk
		foreign key (profile) references t_user_profile (user_profile_id)
			on delete cascade
);

