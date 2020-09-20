--
-- PostgreSQL database dump
--

-- Dumped from database version 12.4 (Ubuntu 12.4-0ubuntu0.20.04.1)
-- Dumped by pg_dump version 12.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE IF EXISTS postgres;
--
-- Name: postgres; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C.UTF-8' LC_CTYPE = 'C.UTF-8';


ALTER DATABASE postgres OWNER TO postgres;

\connect postgres

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


--
-- Name: spark_db; Type: SCHEMA; Schema: -; Owner: sammy
--

CREATE SCHEMA spark_db;


ALTER SCHEMA spark_db OWNER TO sammy;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: t_complaint; Type: TABLE; Schema: spark_db; Owner: sammy
--

CREATE TABLE spark_db.t_complaint (
    id_complaint integer NOT NULL,
    from_user integer NOT NULL,
    to_user integer NOT NULL
);


ALTER TABLE spark_db.t_complaint OWNER TO sammy;

--
-- Name: t_complaint_id_complaint_seq; Type: SEQUENCE; Schema: spark_db; Owner: sammy
--

CREATE SEQUENCE spark_db.t_complaint_id_complaint_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE spark_db.t_complaint_id_complaint_seq OWNER TO sammy;

--
-- Name: t_complaint_id_complaint_seq; Type: SEQUENCE OWNED BY; Schema: spark_db; Owner: sammy
--

ALTER SEQUENCE spark_db.t_complaint_id_complaint_seq OWNED BY spark_db.t_complaint.id_complaint;


--
-- Name: t_image; Type: TABLE; Schema: spark_db; Owner: sammy
--

CREATE TABLE spark_db.t_image (
    id_image integer NOT NULL,
    user_id integer NOT NULL,
    is_main boolean DEFAULT false NOT NULL
);


ALTER TABLE spark_db.t_image OWNER TO sammy;

--
-- Name: t_image_id_image_seq; Type: SEQUENCE; Schema: spark_db; Owner: sammy
--

CREATE SEQUENCE spark_db.t_image_id_image_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE spark_db.t_image_id_image_seq OWNER TO sammy;

--
-- Name: t_image_id_image_seq; Type: SEQUENCE OWNED BY; Schema: spark_db; Owner: sammy
--

ALTER SEQUENCE spark_db.t_image_id_image_seq OWNED BY spark_db.t_image.id_image;


--
-- Name: t_message; Type: TABLE; Schema: spark_db; Owner: sammy
--

CREATE TABLE spark_db.t_message (
    id_message integer NOT NULL,
    text character varying(2048),
    date bigint,
    "from" integer,
    "to" integer
);


ALTER TABLE spark_db.t_message OWNER TO sammy;

--
-- Name: t_message_id_message_seq; Type: SEQUENCE; Schema: spark_db; Owner: sammy
--

CREATE SEQUENCE spark_db.t_message_id_message_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE spark_db.t_message_id_message_seq OWNER TO sammy;

--
-- Name: t_message_id_message_seq; Type: SEQUENCE OWNED BY; Schema: spark_db; Owner: sammy
--

ALTER SEQUENCE spark_db.t_message_id_message_seq OWNED BY spark_db.t_message.id_message;


--
-- Name: t_user_profile; Type: TABLE; Schema: spark_db; Owner: sammy
--

CREATE TABLE spark_db.t_user_profile (
    user_profile_id integer NOT NULL,
    login character varying(256) NOT NULL,
    password character varying(93) NOT NULL,
    email character varying(256),
    first_name character varying(256),
    last_name character varying(256),
    confirmed boolean DEFAULT false NOT NULL,
    biography character varying(2048),
    sex integer NOT NULL,
    sex_preferences integer,
    confirmed_token character varying(40),
    latitude numeric(25,15) DEFAULT 0.0,
    longitude numeric(25,15) DEFAULT 0.0,
    rating integer DEFAULT 0,
    age integer
);


ALTER TABLE spark_db.t_user_profile OWNER TO sammy;

--
-- Name: t_user_profile_user_profile_id_seq; Type: SEQUENCE; Schema: spark_db; Owner: sammy
--

CREATE SEQUENCE spark_db.t_user_profile_user_profile_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE spark_db.t_user_profile_user_profile_id_seq OWNER TO sammy;

--
-- Name: t_user_profile_user_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: spark_db; Owner: sammy
--

ALTER SEQUENCE spark_db.t_user_profile_user_profile_id_seq OWNED BY spark_db.t_user_profile.user_profile_id;


--
-- Name: t_users_unity; Type: TABLE; Schema: spark_db; Owner: sammy
--

CREATE TABLE spark_db.t_users_unity (
    t_users_unity_id integer NOT NULL,
    user1_id integer,
    user2_id integer,
    confirmed boolean DEFAULT false
);


ALTER TABLE spark_db.t_users_unity OWNER TO sammy;

--
-- Name: t_users_unity_t_users_unity_id_seq; Type: SEQUENCE; Schema: spark_db; Owner: sammy
--

CREATE SEQUENCE spark_db.t_users_unity_t_users_unity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE spark_db.t_users_unity_t_users_unity_id_seq OWNER TO sammy;

--
-- Name: t_users_unity_t_users_unity_id_seq; Type: SEQUENCE OWNED BY; Schema: spark_db; Owner: sammy
--

ALTER SEQUENCE spark_db.t_users_unity_t_users_unity_id_seq OWNED BY spark_db.t_users_unity.t_users_unity_id;


--
-- Name: t_complaint id_complaint; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_complaint ALTER COLUMN id_complaint SET DEFAULT nextval('spark_db.t_complaint_id_complaint_seq'::regclass);


--
-- Name: t_image id_image; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_image ALTER COLUMN id_image SET DEFAULT nextval('spark_db.t_image_id_image_seq'::regclass);


--
-- Name: t_message id_message; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_message ALTER COLUMN id_message SET DEFAULT nextval('spark_db.t_message_id_message_seq'::regclass);


--
-- Name: t_user_profile user_profile_id; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_user_profile ALTER COLUMN user_profile_id SET DEFAULT nextval('spark_db.t_user_profile_user_profile_id_seq'::regclass);


--
-- Name: t_users_unity t_users_unity_id; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_users_unity ALTER COLUMN t_users_unity_id SET DEFAULT nextval('spark_db.t_users_unity_t_users_unity_id_seq'::regclass);


--
-- Data for Name: t_complaint; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--

COPY spark_db.t_complaint (id_complaint, from_user, to_user) FROM stdin;
\.


--
-- Data for Name: t_image; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--

COPY spark_db.t_image (id_image, user_id, is_main) FROM stdin;
\.


--
-- Data for Name: t_message; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--

COPY spark_db.t_message (id_message, text, date, "from", "to") FROM stdin;
164	asfasf	1600344822014	57	57
\.


--
-- Data for Name: t_user_profile; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--

COPY spark_db.t_user_profile (user_profile_id, login, password, email, first_name, last_name, confirmed, biography, sex, sex_preferences, confirmed_token, latitude, longitude, rating, age) FROM stdin;
562	1231213	Tv909unPLrdcI3trt0m5G2+xxeh0PlYymvIM8YPYTVY=$oumbvn31Z3Zg8Xkqd3SC0X7PtZbbncPurjFF0M4w93A=	zininlol1231231@rambler.ru	\N	\N	f	\N	-1	\N	QSDqith1nKVA85QjioI9nkPMcahmN682LovhuQYj	0.000000000000000	0.000000000000000	0	\N
58	﻿Ace	123	sena@price.io	\N	\N	f	\N	0	\N	\N	55.870900000000000	49.351200000000000	6	\N
59	Agent	123	youlanda@pouros.biz	\N	\N	f	\N	0	\N	\N	55.839200000000000	49.188100000000000	17	\N
261	Aqua	123	sharolyn_abbott@marquardt-klocko.org	\N	\N	f	\N	1	\N	\N	55.703100000000000	49.266000000000000	7	\N
271	Blueberry	123	arnulfo@vandervort-hauck.info	\N	\N	f	\N	1	\N	\N	55.890100000000000	48.856400000000000	11	\N
281	Cake Face	123	shizuko@schoen.net	\N	\N	f	\N	1	\N	\N	55.810800000000000	49.330200000000000	8	\N
290	Charming	123	keiko_hickle@mraz-hoppe.biz	\N	\N	f	\N	0	\N	\N	55.754900000000000	49.099100000000000	19	\N
298	Cupcake	123	golden.lowe@lebsack.org	\N	\N	f	\N	1	\N	\N	55.835100000000000	48.950000000000000	-13	\N
306	Dottie	123	hilario.bauch@beatty-powlowski.name	\N	\N	f	\N	0	\N	\N	55.715500000000000	48.800500000000000	-11	\N
317	Honey	123	dennis_stracke@hauck.info	\N	\N	f	\N	1	\N	\N	55.821900000000000	49.158500000000000	-3	\N
60	Alias	123	wanita@stark.com	\N	\N	f	\N	0	\N	\N	55.743900000000000	49.028100000000000	-7	\N
319	Icee	123	taylor_collins@spinka.io	\N	\N	f	\N	0	\N	\N	55.902300000000000	49.150000000000000	-20	\N
321	Iris	123	darrel.greenholt@cartwright-cummerata.info	\N	\N	f	\N	1	\N	\N	55.784000000000000	49.257000000000000	-8	\N
332	Manhattan	123	reginald.mclaughlin@pollich-torp.org	\N	\N	f	\N	0	\N	\N	55.755300000000000	49.033400000000000	-18	\N
61	Alpha	123	hang@kris.org	\N	\N	f	\N	0	\N	\N	55.725400000000000	48.907400000000000	17	\N
343	Munchkin	123	leandro@hyatt-tillman.biz	\N	\N	f	\N	1	\N	\N	55.830600000000000	48.830700000000000	1	\N
352	Petunia	123	nina@crist.info	\N	\N	f	\N	0	\N	\N	55.805600000000000	48.933900000000000	3	\N
361	Popsicle	123	tandy@glover.org	\N	\N	f	\N	0	\N	\N	55.912100000000000	49.042800000000000	5	\N
62	Black Diamond	123	gladys.walker@ohara.name	\N	\N	f	\N	0	\N	\N	55.810700000000000	49.307200000000000	1	\N
63	Black Magic	123	federico.durgan@satterfield-pacocha.io	\N	\N	f	\N	0	\N	\N	55.769400000000000	49.030700000000000	18	\N
368	Rum Punch	123	lavonna.mcclure@bartell.com	\N	\N	f	\N	0	\N	\N	55.788100000000000	49.306800000000000	-9	\N
64	Black Pearl	123	rosio_lehner@senger.name	\N	\N	f	\N	0	\N	\N	55.810000000000000	48.817200000000000	-7	\N
65	Black Widow	123	kaci@hessel-greenfelder.co	\N	\N	f	\N	1	\N	\N	55.873800000000000	49.013500000000000	1	\N
66	Blade	123	kraig_corkery@langosh-bergstrom.info	\N	\N	f	\N	1	\N	\N	55.894800000000000	49.218900000000000	2	\N
67	Blaze	123	rob@marks.io	\N	\N	f	\N	1	\N	\N	55.888900000000000	49.122700000000000	-6	\N
377	Smartie	123	earlene_schoen@nader.net	\N	\N	f	\N	1	\N	\N	55.899200000000000	49.050600000000000	-3	\N
386	Stem	123	graig.bechtelar@ziemann.name	\N	\N	f	\N	1	\N	\N	55.748400000000000	49.101200000000000	-7	\N
395	Sweetie	123	anjanette_bergstrom@huels.io	\N	\N	f	\N	0	\N	\N	55.713700000000000	49.055300000000000	-9	\N
402	Tootsie	123	carolynn@rogahn.co	\N	\N	f	\N	1	\N	\N	55.829800000000000	48.797700000000000	-10	\N
408	Vanilla Bean	123	suzanna@reilly-gerlach.co	\N	\N	f	\N	0	\N	\N	55.910600000000000	49.031300000000000	-14	\N
418	Kanha	123	nelson@bashirian.co	\N	\N	f	\N	1	\N	\N	55.889500000000000	49.188700000000000	5	\N
426	Pooh	123	jeremy.bauch@robel-cruickshank.info	\N	\N	f	\N	0	\N	\N	55.879700000000000	48.986200000000000	2	\N
437	Dimple	123	lyman.franecki@king-thiel.name	\N	\N	f	\N	1	\N	\N	55.769300000000000	48.925100000000000	9	\N
68	Bombshell	123	stanley@rogahn.co	\N	\N	f	\N	1	\N	\N	55.846700000000000	49.142400000000000	-3	\N
69	Buckwild	123	caterina_bechtelar@runte-pagac.co	\N	\N	f	\N	0	\N	\N	55.833100000000000	49.362300000000000	-20	\N
70	Butterfly	123	rusty_schuppe@parker.org	\N	\N	f	\N	1	\N	\N	55.878800000000000	49.145300000000000	20	\N
71	Cadillac	123	young.muller@beier.info	\N	\N	f	\N	0	\N	\N	55.803000000000000	49.003200000000000	-3	\N
444	Akki	123	royce.langworth@greenholt-purdy.net	\N	\N	f	\N	0	\N	\N	55.836200000000000	49.076100000000000	-20	\N
453	Riva	123	latonya_osinski@nikolaus-shields.co	\N	\N	f	\N	0	\N	\N	55.840800000000000	48.835500000000000	-4	\N
464	Aashi	123	burton_moen@connelly-metz.com	\N	\N	f	\N	1	\N	\N	55.904400000000000	49.274000000000000	0	\N
473	Aaru	123	alane.stark@wolf.io	\N	\N	f	\N	1	\N	\N	55.762700000000000	48.866500000000000	-16	\N
481	Babai	123	carrol.kilback@casper.net	\N	\N	f	\N	1	\N	\N	55.747900000000000	49.272200000000000	-18	\N
487	Chhoti	123	dirk_wilkinson@ondricka.co	\N	\N	f	\N	1	\N	\N	55.893500000000000	49.195500000000000	15	\N
491	Dolly	123	dania_boyer@jacobi.io	\N	\N	f	\N	1	\N	\N	55.842900000000000	49.225700000000000	5	\N
498	Hari	123	luciano_stracke@franecki-lynch.net	\N	\N	f	\N	0	\N	\N	55.872700000000000	49.104100000000000	-1	\N
509	Rinky	123	viva_maggio@kilback.org	\N	\N	f	\N	0	\N	\N	55.821700000000000	49.265500000000000	0	\N
518	Tubl	123	johnnie@prohaska-stroman.name	\N	\N	f	\N	1	\N	\N	55.870200000000000	49.339300000000000	6	\N
525	Ishita	123	giselle_schroeder@erdman-armstrong.co	\N	\N	f	\N	0	\N	\N	55.810800000000000	49.258100000000000	-19	\N
536	Mandy	123	stewart@barton-pacocha.com	\N	\N	f	\N	1	\N	\N	55.881700000000000	49.274000000000000	13	\N
545	Sandy	123	ronald@konopelski.net	\N	\N	f	\N	0	\N	\N	55.702500000000000	49.106800000000000	4	\N
552	Yudi	123	kelly.brekke@jacobson-reinger.info	\N	\N	f	\N	0	\N	\N	55.801000000000000	48.959500000000000	-10	\N
563	11111	9pTRNmogHD+gSLx5BD/Ea/0rCIshvEGM2G3QajIeG+k=$2vlxpZR7jbtugJnJeLJc733Cl9ZfJGZl+v1TdP3fGLw=	zininlol11111@rambler.ru	\N	\N	f	\N	-1	\N	UGHO4j6yv7qh2xjmJguLtMwNNqFhUYhiq6NhCYzC	0.000000000000000	0.000000000000000	0	\N
566	111111111	ifI3Xu9gTvTaxaHfkoSZRbo9G/YiaEr7NjWmbTJPeuM=$D+G1rLPgCFI4taxrcmq4pac05BHNB4sd7nOT8e1rmqo=	penguin13asd1@mail.ru	\N	\N	f	\N	-1	\N	Rnilyf5qKFxOPwPtK7WgokKBbxbGwA0lwfGuHpQn	0.000000000000000	0.000000000000000	0	\N
57	smight	YniLyMt6Ht6zoXmYoHKGxkkEkA2dyQWvn6nHmmpMspo=$LOg0Op+i28sKslbq+diL7hOQ+auEsyWksyovdq8FlOs=	zininlol@rambler.ru	\N	\N	t	\N	-1	\N	OX2dZbNf8FUqDJ7SQh6M5vOP2ju60YFn64loFuY6	55.787900000000000	49.123300000000000	0	\N
560	123123	8KQ3loCmKpf2jz1PWTuxlf2KsXr6GwEKhUYSWkIXu3c=$1izbBdT4ClqlfMhsolFqu17Q2LI0SazdVvDL0ktYk8k=	zininlol123123@rambler.ru	\N	\N	f	\N	-1	\N	MMKP56S8oJjwQOkXxpCKD4ZPMk1UswoumfqWssOs	0.000000000000000	0.000000000000000	0	\N
73	Champ	123	lonnie@metz.org	\N	\N	f	\N	0	\N	\N	55.902400000000000	49.175800000000000	9	\N
75	Claws	123	racquel@torp.biz	\N	\N	f	\N	1	\N	\N	55.797300000000000	49.002800000000000	12	\N
76	Countess	123	terrence@abshire.info	\N	\N	f	\N	1	\N	\N	55.756400000000000	49.348500000000000	12	\N
77	Cutlass	123	danial.friesen@ziemann.name	\N	\N	f	\N	1	\N	\N	55.748800000000000	49.146500000000000	-13	\N
78	Dollface	123	willette@wolf-bernhard.com	\N	\N	f	\N	0	\N	\N	55.903200000000000	49.378000000000000	3	\N
79	Dragonfly	123	otha.white@lehner.org	\N	\N	f	\N	0	\N	\N	55.877400000000000	49.328300000000000	3	\N
80	Duchess	123	andres@terry-ruecker.info	\N	\N	f	\N	1	\N	\N	55.704800000000000	49.022000000000000	4	\N
81	Dynamo	123	carey@okon.biz	\N	\N	f	\N	1	\N	\N	55.766300000000000	49.285200000000000	9	\N
82	Eclipse	123	luella@christiansen.net	\N	\N	f	\N	0	\N	\N	55.902600000000000	49.073600000000000	5	\N
83	Empress	123	pearlene.bogisich@swift.info	\N	\N	f	\N	1	\N	\N	55.710900000000000	49.267700000000000	-8	\N
84	Energizer	123	prince@parker.com	\N	\N	f	\N	0	\N	\N	55.811800000000000	49.379800000000000	3	\N
86	Firey	123	victor@wiza.biz	\N	\N	f	\N	1	\N	\N	55.910100000000000	49.279400000000000	-19	\N
87	Heat	123	dorothea@ondricka.org	\N	\N	f	\N	1	\N	\N	55.707700000000000	48.926000000000000	1	\N
88	Hela	123	carly_goldner@cole.name	\N	\N	f	\N	1	\N	\N	55.890600000000000	49.012300000000000	7	\N
89	Heroine	123	jon@ratke.net	\N	\N	f	\N	1	\N	\N	55.842100000000000	49.194800000000000	20	\N
90	Hoops	123	chuck_friesen@spencer.io	\N	\N	f	\N	0	\N	\N	55.723300000000000	49.310800000000000	-3	\N
91	Hottie	123	mikki@gislason.info	\N	\N	f	\N	1	\N	\N	55.796100000000000	49.339000000000000	-15	\N
92	Hurricane	123	ashlyn.bayer@jones.co	\N	\N	f	\N	0	\N	\N	55.738200000000000	49.294800000000000	13	\N
94	Katniss	123	reynaldo.metz@huels.info	\N	\N	f	\N	0	\N	\N	55.887600000000000	49.364100000000000	-18	\N
95	Lady Luck	123	ward@bogisich.co	\N	\N	f	\N	0	\N	\N	55.745500000000000	49.174400000000000	-5	\N
96	Lava	123	iola_denesik@orn.org	\N	\N	f	\N	1	\N	\N	55.767900000000000	49.219400000000000	13	\N
97	Lightning	123	avril.frami@stracke-bergstrom.io	\N	\N	f	\N	0	\N	\N	55.766600000000000	49.140000000000000	6	\N
98	Lioness	123	normand_sawayn@schmitt.io	\N	\N	f	\N	1	\N	\N	55.851000000000000	48.891500000000000	18	\N
99	Marvel	123	cassaundra_hane@herzog.biz	\N	\N	f	\N	1	\N	\N	55.818200000000000	49.211500000000000	19	\N
100	Mayhem	123	gwyn@gleichner.name	\N	\N	f	\N	1	\N	\N	55.734500000000000	48.890500000000000	-2	\N
101	Mighty	123	janee@watsica-hauck.info	\N	\N	f	\N	1	\N	\N	55.760900000000000	48.903700000000000	-5	\N
102	Mustang	123	myrle_donnelly@beahan-friesen.com	\N	\N	f	\N	1	\N	\N	55.892900000000000	49.042600000000000	15	\N
103	Ninja	123	thanh_renner@kuhic.com	\N	\N	f	\N	1	\N	\N	55.830900000000000	49.063400000000000	2	\N
105	Queen	123	debroah@nolan.info	\N	\N	f	\N	0	\N	\N	55.875900000000000	48.793700000000000	-13	\N
106	Radar	123	ian_bauch@mante-damore.biz	\N	\N	f	\N	1	\N	\N	55.884900000000000	49.183500000000000	-13	\N
107	Rebel	123	rhett@mueller.info	\N	\N	f	\N	0	\N	\N	55.784700000000000	49.271800000000000	-20	\N
108	Riot	123	kena.marquardt@altenwerth-conroy.org	\N	\N	f	\N	1	\N	\N	55.718200000000000	49.072000000000000	-5	\N
109	Shadow	123	mozelle@vandervort.com	\N	\N	f	\N	0	\N	\N	55.793300000000000	49.383500000000000	10	\N
110	Slayer	123	millie_schultz@davis-greenholt.info	\N	\N	f	\N	0	\N	\N	55.714200000000000	48.936500000000000	3	\N
111	Sparrow	123	deadra@towne-doyle.co	\N	\N	f	\N	1	\N	\N	55.829200000000000	48.793700000000000	4	\N
113	Spike	123	fredric_schoen@tromp.org	\N	\N	f	\N	1	\N	\N	55.757800000000000	48.967800000000000	17	\N
114	Tailor Made	123	charleen@nikolaus.org	\N	\N	f	\N	1	\N	\N	55.813000000000000	49.323600000000000	19	\N
115	The Evil Queen	123	jamar@kunze.io	\N	\N	f	\N	1	\N	\N	55.905300000000000	49.175800000000000	-19	\N
116	Thunder	123	kelley.lind@goldner-oconner.biz	\N	\N	f	\N	1	\N	\N	55.879300000000000	49.103800000000000	-8	\N
117	Tick Tock	123	idell@blanda-stoltenberg.io	\N	\N	f	\N	1	\N	\N	55.762200000000000	49.126600000000000	-8	\N
118	Trinity	123	greg@mcglynn.biz	\N	\N	f	\N	0	\N	\N	55.713300000000000	48.820500000000000	-7	\N
119	Twister	123	stan@gleason-friesen.biz	\N	\N	f	\N	1	\N	\N	55.707600000000000	48.809400000000000	-17	\N
120	Venus	123	troy@sipes-durgan.name	\N	\N	f	\N	0	\N	\N	55.858100000000000	49.352900000000000	0	\N
122	Warrior Princess	123	milan@lubowitz.co	\N	\N	f	\N	1	\N	\N	55.794800000000000	48.893700000000000	0	\N
123	Wonder Woman	123	avery@will.net	\N	\N	f	\N	1	\N	\N	55.760300000000000	48.863700000000000	15	\N
124	Xena	123	claude.lindgren@powlowski.io	\N	\N	f	\N	0	\N	\N	55.714100000000000	49.210900000000000	20	\N
125	Your Highness	123	edmund_monahan@romaguera-nitzsche.info	\N	\N	f	\N	0	\N	\N	55.824000000000000	49.017000000000000	-2	\N
126	Zelda	123	joaquin.skiles@dare.biz	\N	\N	f	\N	1	\N	\N	55.702500000000000	49.198900000000000	-7	\N
127	Alfalfa	123	jose.kiehn@bayer-gislason.co	\N	\N	f	\N	1	\N	\N	55.800600000000000	49.361800000000000	6	\N
128	Bambi	123	emory@balistreri.net	\N	\N	f	\N	0	\N	\N	55.767800000000000	49.098400000000000	18	\N
129	Beanstalk	123	johnathan_hirthe@jerde-klocko.org	\N	\N	f	\N	1	\N	\N	55.814900000000000	49.174000000000000	10	\N
130	Beefy	123	augusta@wolff.name	\N	\N	f	\N	1	\N	\N	55.871800000000000	49.352300000000000	-1	\N
132	Boots	123	titus@reichert.org	\N	\N	f	\N	0	\N	\N	55.838600000000000	49.287000000000000	5	\N
133	Catnip	123	jamison@upton.name	\N	\N	f	\N	1	\N	\N	55.846900000000000	48.918900000000000	-14	\N
134	Cheeseball	123	charles_emmerich@satterfield.biz	\N	\N	f	\N	1	\N	\N	55.725500000000000	48.976400000000000	10	\N
135	Chewbacca	123	mui.kessler@mueller.com	\N	\N	f	\N	1	\N	\N	55.826800000000000	48.880400000000000	-20	\N
136	Chipmunk	123	hans.haley@gerlach-kshlerin.io	\N	\N	f	\N	0	\N	\N	55.879300000000000	49.299700000000000	-18	\N
137	Chirp	123	lavern_witting@beatty.io	\N	\N	f	\N	1	\N	\N	55.813200000000000	48.831000000000000	16	\N
138	Class Clown	123	jadwiga@hickle-willms.io	\N	\N	f	\N	1	\N	\N	55.859600000000000	49.125100000000000	12	\N
139	Comedy Central	123	sung@lehner.info	\N	\N	f	\N	0	\N	\N	55.868300000000000	48.802100000000000	10	\N
140	Coo Coo	123	jamar@mertz.name	\N	\N	f	\N	1	\N	\N	55.766800000000000	49.118800000000000	14	\N
142	Crazy Queen	123	howard@shanahan.biz	\N	\N	f	\N	1	\N	\N	55.846600000000000	49.058700000000000	0	\N
143	Curly Q	123	suzan@effertz-hodkiewicz.org	\N	\N	f	\N	0	\N	\N	55.710600000000000	49.380400000000000	16	\N
144	Dimples	123	makeda_witting@oconner-mckenzie.com	\N	\N	f	\N	1	\N	\N	55.769200000000000	49.367500000000000	18	\N
145	Doofy	123	chastity@herman-macejkovic.net	\N	\N	f	\N	0	\N	\N	55.765000000000000	49.368300000000000	-19	\N
146	Duck	123	gail.treutel@parker.biz	\N	\N	f	\N	0	\N	\N	55.712700000000000	48.954100000000000	5	\N
147	Energizer Bunny	123	odell_little@spencer.name	\N	\N	f	\N	1	\N	\N	55.797700000000000	48.862800000000000	-6	\N
148	Foodie	123	edmundo@shields.co	\N	\N	f	\N	1	\N	\N	55.706000000000000	49.312200000000000	-13	\N
149	Four Eyes	123	yasmine_huel@weissnat.name	\N	\N	f	\N	0	\N	\N	55.809900000000000	49.362900000000000	9	\N
150	Fun Size	123	clark@littel-borer.org	\N	\N	f	\N	0	\N	\N	55.710700000000000	49.260300000000000	12	\N
152	Fuzzy Wuzzy	123	kareem_ziemann@barrows-wyman.com	\N	\N	f	\N	0	\N	\N	55.862200000000000	49.330300000000000	-8	\N
153	Giggles	123	louie@rutherford-douglas.biz	\N	\N	f	\N	0	\N	\N	55.867800000000000	49.102000000000000	5	\N
154	Goofball	123	mathew@lang.com	\N	\N	f	\N	1	\N	\N	55.709200000000000	49.239400000000000	-11	\N
156	Hangry	123	lanell@kshlerin.io	\N	\N	f	\N	0	\N	\N	55.909200000000000	49.245900000000000	-16	\N
157	Hobbit	123	britni_davis@bartoletti.co	\N	\N	f	\N	0	\N	\N	55.702400000000000	48.925800000000000	-16	\N
158	Itty Bitty	123	ocie.lowe@mertz.com	\N	\N	f	\N	0	\N	\N	55.846500000000000	48.872100000000000	15	\N
159	Joker	123	bruce.wisozk@emard.io	\N	\N	f	\N	0	\N	\N	55.821200000000000	49.025200000000000	10	\N
160	Meatball	123	alec@heathcote.com	\N	\N	f	\N	1	\N	\N	55.867300000000000	49.295400000000000	15	\N
162	Monkey	123	annamaria_brown@konopelski.info	\N	\N	f	\N	1	\N	\N	55.721000000000000	49.287900000000000	-10	\N
163	Mother of Dragons	123	cory@monahan.co	\N	\N	f	\N	0	\N	\N	55.873700000000000	49.202300000000000	9	\N
164	Mouse	123	lonny.bogisich@buckridge-runolfsson.co	\N	\N	f	\N	1	\N	\N	55.779900000000000	49.122700000000000	8	\N
165	Noodles	123	debrah@hirthe-swift.com	\N	\N	f	\N	0	\N	\N	55.866300000000000	49.237700000000000	16	\N
166	Panda	123	tabetha@okuneva.com	\N	\N	f	\N	0	\N	\N	55.713000000000000	49.001900000000000	-8	\N
167	Paws	123	michelle@dooley.net	\N	\N	f	\N	1	\N	\N	55.736600000000000	49.186300000000000	-5	\N
168	Pigtails	123	diedra@haag.co	\N	\N	f	\N	1	\N	\N	55.708600000000000	48.847000000000000	4	\N
170	Pork Chop	123	helene@kerluke.info	\N	\N	f	\N	1	\N	\N	55.893600000000000	49.183600000000000	8	\N
171	Princess Peach	123	france@greenholt.org	\N	\N	f	\N	1	\N	\N	55.873600000000000	49.289000000000000	17	\N
172	Pumpkin	123	lael@rice.org	\N	\N	f	\N	1	\N	\N	55.838900000000000	48.951000000000000	-19	\N
173	Punk	123	billy_ondricka@cassin.co	\N	\N	f	\N	1	\N	\N	55.753800000000000	49.235800000000000	16	\N
174	Quack	123	sook_denesik@green.io	\N	\N	f	\N	1	\N	\N	55.798400000000000	49.277900000000000	6	\N
175	Rabbit Ears	123	tiffany@witting.net	\N	\N	f	\N	0	\N	\N	55.909800000000000	48.953200000000000	-4	\N
176	Silly Goof	123	salley@pacocha-mante.org	\N	\N	f	\N	1	\N	\N	55.896500000000000	48.926200000000000	4	\N
177	Slim Thick	123	cindi.blick@greenholt-durgan.io	\N	\N	f	\N	0	\N	\N	55.883200000000000	49.255000000000000	3	\N
178	Spaghetti	123	gaylord@kohler-mitchell.io	\N	\N	f	\N	1	\N	\N	55.819500000000000	48.948500000000000	-12	\N
179	Spanky	123	abel@hahn.biz	\N	\N	f	\N	0	\N	\N	55.824600000000000	49.124500000000000	2	\N
181	Squirrel	123	benedict@kertzmann-schroeder.info	\N	\N	f	\N	0	\N	\N	55.753200000000000	49.377300000000000	-16	\N
182	Squishy	123	chas@hyatt.info	\N	\N	f	\N	0	\N	\N	55.890000000000000	48.858100000000000	11	\N
183	Stinker	123	dixie@schimmel-gulgowski.co	\N	\N	f	\N	0	\N	\N	55.818100000000000	48.998900000000000	-12	\N
184	String bean	123	olen@quitzon.org	\N	\N	f	\N	0	\N	\N	55.733300000000000	48.860900000000000	7	\N
185	Sugar Mama	123	lauryn@murazik-cassin.name	\N	\N	f	\N	1	\N	\N	55.791700000000000	49.113300000000000	19	\N
186	Tater Tot	123	kathryne@okuneva-macejkovic.biz	\N	\N	f	\N	1	\N	\N	55.767800000000000	48.954600000000000	1	\N
187	Thick Thighs	123	evon_weimann@fahey.biz	\N	\N	f	\N	0	\N	\N	55.708300000000000	49.225600000000000	-16	\N
188	Tiny	123	kathern@watsica.co	\N	\N	f	\N	1	\N	\N	55.869400000000000	48.878900000000000	18	\N
190	Turtle	123	jamaal@powlowski.biz	\N	\N	f	\N	1	\N	\N	55.871900000000000	49.349700000000000	-9	\N
191	Unburnt	123	landon_hane@lubowitz-runolfsdottir.co	\N	\N	f	\N	1	\N	\N	55.825200000000000	48.834600000000000	11	\N
192	Whiskers	123	willetta.weimann@hackett-rath.biz	\N	\N	f	\N	1	\N	\N	55.763300000000000	49.315900000000000	-4	\N
193	Amor	123	emil@bosco.com	\N	\N	f	\N	1	\N	\N	55.802300000000000	49.106700000000000	9	\N
194	Angel Eyes	123	chase@funk.co	\N	\N	f	\N	1	\N	\N	55.895500000000000	48.972500000000000	17	\N
195	Aphrodite	123	leandro@smith.com	\N	\N	f	\N	1	\N	\N	55.831200000000000	48.937300000000000	18	\N
196	Awesome Blossom	123	rosenda@hayes.com	\N	\N	f	\N	1	\N	\N	55.785800000000000	49.200400000000000	8	\N
197	Babe	123	leonie@padberg.biz	\N	\N	f	\N	0	\N	\N	55.762500000000000	48.993500000000000	0	\N
199	Babyschka	123	johnathon.mcdermott@rogahn.biz	\N	\N	f	\N	0	\N	\N	55.857600000000000	49.231500000000000	-20	\N
200	Bae-ritto	123	leigh_wisoky@bernier.net	\N	\N	f	\N	0	\N	\N	55.779500000000000	49.022600000000000	-6	\N
201	Barbie	123	tamera_nitzsche@runte-romaguera.com	\N	\N	f	\N	0	\N	\N	55.911900000000000	49.048300000000000	-14	\N
202	Barbie Doll	123	weston@hettinger.info	\N	\N	f	\N	1	\N	\N	55.736100000000000	48.969600000000000	-18	\N
203	Bear	123	zana@gorczany-torp.io	\N	\N	f	\N	0	\N	\N	55.780000000000000	49.086400000000000	20	\N
204	Belle	123	anjanette@walsh-connelly.com	\N	\N	f	\N	0	\N	\N	55.861000000000000	48.815900000000000	1	\N
205	Blondie	123	cary_stark@ruecker.info	\N	\N	f	\N	0	\N	\N	55.854900000000000	49.340300000000000	10	\N
207	Bright Light	123	jarvis.crist@weber.name	\N	\N	f	\N	0	\N	\N	55.865700000000000	49.247100000000000	4	\N
208	Brown eyes	123	wilfredo@schultz-hyatt.biz	\N	\N	f	\N	1	\N	\N	55.883000000000000	49.053600000000000	-16	\N
209	Bumble Bee	123	noel@ondricka.org	\N	\N	f	\N	0	\N	\N	55.773000000000000	48.981500000000000	5	\N
210	Buttons	123	jorge_kohler@reichert.net	\N	\N	f	\N	0	\N	\N	55.761700000000000	49.342500000000000	-8	\N
211	Care Bear	123	saundra@lehner.name	\N	\N	f	\N	0	\N	\N	55.859500000000000	49.375400000000000	1	\N
212	Cutie	123	kaila@kertzmann-aufderhar.io	\N	\N	f	\N	1	\N	\N	55.873200000000000	49.128200000000000	-10	\N
213	Cutie Patootie	123	colin_rosenbaum@reichert-shanahan.com	\N	\N	f	\N	0	\N	\N	55.748500000000000	49.098100000000000	12	\N
214	Cutie Pie	123	ela@king.info	\N	\N	f	\N	0	\N	\N	55.747500000000000	49.227100000000000	16	\N
215	Doll Face	123	hubert.jakubowski@kunde.com	\N	\N	f	\N	0	\N	\N	55.860400000000000	49.033000000000000	-10	\N
217	Dumpling	123	marshall.trantow@mraz-torphy.biz	\N	\N	f	\N	0	\N	\N	55.860100000000000	49.074500000000000	13	\N
218	Firefly	123	gregory.langworth@legros-harvey.io	\N	\N	f	\N	1	\N	\N	55.877000000000000	49.346600000000000	15	\N
219	Flawless	123	zackary@runolfsson-gislason.com	\N	\N	f	\N	1	\N	\N	55.894800000000000	49.187100000000000	12	\N
220	Genuine	123	terence_kerluke@strosin.net	\N	\N	f	\N	1	\N	\N	55.797400000000000	48.794500000000000	-19	\N
221	Goldie	123	paul_conroy@cormier-huel.net	\N	\N	f	\N	1	\N	\N	55.732000000000000	49.247300000000000	-12	\N
222	Heavenly	123	latoya.zemlak@langosh.name	\N	\N	f	\N	0	\N	\N	55.852500000000000	48.914300000000000	16	\N
223	Jasmine	123	philip_hyatt@ziemann.biz	\N	\N	f	\N	1	\N	\N	55.715200000000000	48.998000000000000	-11	\N
224	Kissy Face	123	carlene_mayert@dooley-hyatt.co	\N	\N	f	\N	0	\N	\N	55.894000000000000	49.116100000000000	14	\N
226	Ladybug	123	dylan@mraz-watsica.com	\N	\N	f	\N	0	\N	\N	55.860800000000000	48.964100000000000	11	\N
227	Light of my life	123	elvia@okon.co	\N	\N	f	\N	0	\N	\N	55.899000000000000	49.324000000000000	3	\N
228	Living Angel	123	marlon_kutch@bashirian.biz	\N	\N	f	\N	0	\N	\N	55.887300000000000	49.163000000000000	19	\N
229	Lotus	123	irwin.prosacco@balistreri-koepp.org	\N	\N	f	\N	1	\N	\N	55.892300000000000	48.879900000000000	19	\N
230	Love bug	123	ralph@feil-jaskolski.com	\N	\N	f	\N	1	\N	\N	55.814100000000000	48.809500000000000	18	\N
231	Lovey Dovey	123	lory.zieme@roberts.com	\N	\N	f	\N	0	\N	\N	55.849500000000000	49.195600000000000	-2	\N
232	Lovie	123	lydia@heathcote-jerde.io	\N	\N	f	\N	0	\N	\N	55.750200000000000	49.157300000000000	0	\N
233	Luscious	123	bryce.rohan@keeling.name	\N	\N	f	\N	0	\N	\N	55.893900000000000	49.330500000000000	-11	\N
235	Mulan	123	jonas.osinski@stokes-lowe.io	\N	\N	f	\N	0	\N	\N	55.798300000000000	49.291200000000000	-2	\N
236	My All	123	antonia@lesch-bogisich.net	\N	\N	f	\N	1	\N	\N	55.737400000000000	48.952800000000000	-10	\N
252	Sweetness	123	jasper@oconnell.biz	\N	\N	f	\N	0	\N	\N	55.789200000000000	49.170000000000000	8	\N
253	Sweetums	123	dahlia_gorczany@russel-bailey.co	\N	\N	f	\N	1	\N	\N	55.709600000000000	49.236800000000000	-20	\N
254	Tea Cup	123	valencia@heaney.info	\N	\N	f	\N	0	\N	\N	55.792600000000000	49.282600000000000	-16	\N
238	Peachy Keen	123	eddy_skiles@damore-roberts.com	\N	\N	f	\N	0	\N	\N	55.900400000000000	48.938200000000000	7	\N
239	Perfect 10	123	katia.bernier@nitzsche.co	\N	\N	f	\N	0	\N	\N	55.814500000000000	49.317000000000000	5	\N
240	Pretty	123	janel@lang.com	\N	\N	f	\N	0	\N	\N	55.876400000000000	49.266000000000000	-12	\N
242	Rolly Polly	123	brenton@treutel-grimes.name	\N	\N	f	\N	1	\N	\N	55.797400000000000	49.375300000000000	-18	\N
243	Sexy	123	josue@hoeger.org	\N	\N	f	\N	0	\N	\N	55.808600000000000	48.897100000000000	-3	\N
244	Shorty	123	arden@christiansen.com	\N	\N	f	\N	1	\N	\N	55.854200000000000	49.120800000000000	-9	\N
245	Shy	123	adolph@rogahn.info	\N	\N	f	\N	1	\N	\N	55.881000000000000	49.291500000000000	3	\N
246	Smiley	123	april@wisoky-kihn.biz	\N	\N	f	\N	0	\N	\N	55.765700000000000	48.891300000000000	3	\N
247	Smitten Kitten	123	saul.prohaska@stehr.name	\N	\N	f	\N	0	\N	\N	55.762700000000000	49.287300000000000	-5	\N
248	Sparkles	123	livia@huels.info	\N	\N	f	\N	0	\N	\N	55.761200000000000	48.798500000000000	-3	\N
249	Sparky	123	bridgette.berge@leannon.co	\N	\N	f	\N	0	\N	\N	55.807300000000000	48.915200000000000	-16	\N
250	Sunshine	123	kelly.simonis@hermann.info	\N	\N	f	\N	0	\N	\N	55.707600000000000	48.804200000000000	2	\N
255	Tiger	123	teresia@stokes-doyle.biz	\N	\N	f	\N	0	\N	\N	55.813100000000000	49.131000000000000	9	\N
256	Twinkle	123	david@legros.io	\N	\N	f	\N	1	\N	\N	55.813000000000000	49.153500000000000	15	\N
257	Twinkly	123	antone_shields@carter-champlin.com	\N	\N	f	\N	1	\N	\N	55.851100000000000	49.125200000000000	5	\N
258	Almond Joy	123	leigh@walsh.io	\N	\N	f	\N	1	\N	\N	55.784600000000000	48.990100000000000	1	\N
259	Apple	123	ronny@dietrich-boehm.biz	\N	\N	f	\N	1	\N	\N	55.787600000000000	49.098100000000000	-1	\N
260	Apple Pie	123	minh.oberbrunner@luettgen.biz	\N	\N	f	\N	0	\N	\N	55.865800000000000	49.169900000000000	7	\N
262	Baby Cake	123	herbert.bashirian@ryan-ebert.net	\N	\N	f	\N	0	\N	\N	55.877800000000000	48.908900000000000	11	\N
263	Banana Bread	123	chassidy@bahringer-runolfsdottir.info	\N	\N	f	\N	0	\N	\N	55.863000000000000	49.293400000000000	-5	\N
264	Banana Muffin	123	lucas@lueilwitz.io	\N	\N	f	\N	0	\N	\N	55.910100000000000	48.852100000000000	-18	\N
265	Banana Pie	123	javier.thompson@nienow-carter.com	\N	\N	f	\N	0	\N	\N	55.737600000000000	49.178300000000000	-13	\N
266	Banana Split	123	wilbur@breitenberg-weimann.name	\N	\N	f	\N	0	\N	\N	55.767000000000000	49.171000000000000	-12	\N
267	Biscuit	123	brad_jakubowski@lakin.biz	\N	\N	f	\N	0	\N	\N	55.876800000000000	49.217300000000000	-16	\N
268	Blackberry	123	estela@konopelski.com	\N	\N	f	\N	0	\N	\N	55.794900000000000	49.251800000000000	16	\N
269	Bloomer	123	dallas@gutmann.com	\N	\N	f	\N	1	\N	\N	55.750000000000000	49.020200000000000	1	\N
270	Blossom	123	diego@bernhard.com	\N	\N	f	\N	0	\N	\N	55.837200000000000	49.364500000000000	-6	\N
272	Bonbon	123	nicholle_dach@miller.org	\N	\N	f	\N	1	\N	\N	55.727200000000000	48.966100000000000	-5	\N
273	Bourbon	123	emelia@flatley-streich.org	\N	\N	f	\N	0	\N	\N	55.861800000000000	49.180000000000000	-5	\N
274	Brandy	123	hector_homenick@volkman.net	\N	\N	f	\N	0	\N	\N	55.792300000000000	48.955400000000000	9	\N
275	Brown Sugar	123	carlene@macgyver-carter.name	\N	\N	f	\N	0	\N	\N	55.745000000000000	49.090400000000000	15	\N
276	Brownie	123	greg@mertz-bahringer.net	\N	\N	f	\N	1	\N	\N	55.827900000000000	49.200500000000000	-16	\N
277	Bun Bun	123	zenaida_sipes@krajcik.biz	\N	\N	f	\N	0	\N	\N	55.910300000000000	48.937500000000000	19	\N
278	Buttercup	123	sol@schiller-jakubowski.co	\N	\N	f	\N	0	\N	\N	55.910200000000000	48.859000000000000	1	\N
279	Butterfingers	123	stanley@harris.io	\N	\N	f	\N	1	\N	\N	55.864000000000000	48.915100000000000	-16	\N
280	Cake	123	felipe@baumbach.name	\N	\N	f	\N	1	\N	\N	55.717600000000000	49.328500000000000	-18	\N
282	Candy Cane	123	gilbert@schneider-boehm.io	\N	\N	f	\N	0	\N	\N	55.806000000000000	49.365000000000000	-19	\N
283	Candy Corn	123	indira.mayert@hickle.co	\N	\N	f	\N	0	\N	\N	55.799700000000000	49.376200000000000	-20	\N
284	Caramel	123	palmira_breitenberg@kris-bailey.name	\N	\N	f	\N	0	\N	\N	55.778600000000000	49.343200000000000	-9	\N
285	Cattail	123	lanelle@nader.co	\N	\N	f	\N	0	\N	\N	55.875500000000000	48.910700000000000	-5	\N
286	Cayenne	123	louie_lueilwitz@wiegand.net	\N	\N	f	\N	1	\N	\N	55.856200000000000	49.027900000000000	-1	\N
287	Champagne	123	sharilyn.considine@ziemann.info	\N	\N	f	\N	1	\N	\N	55.784300000000000	48.814300000000000	-20	\N
288	Chardonnay	123	antone@jakubowski.org	\N	\N	f	\N	0	\N	\N	55.756800000000000	49.214900000000000	4	\N
289	Charm	123	richard@fisher.com	\N	\N	f	\N	1	\N	\N	55.780100000000000	49.153600000000000	-11	\N
291	Cherry	123	leann@lakin.io	\N	\N	f	\N	1	\N	\N	55.911800000000000	49.262400000000000	19	\N
292	Cherry Pie	123	aracely_rowe@hansen.co	\N	\N	f	\N	1	\N	\N	55.713000000000000	49.310600000000000	16	\N
293	Chocolate	123	melani_raynor@hauck.com	\N	\N	f	\N	0	\N	\N	55.722700000000000	49.266500000000000	15	\N
294	Chocolate Chip	123	bonita@hettinger.biz	\N	\N	f	\N	1	\N	\N	55.767400000000000	49.376000000000000	19	\N
295	Chrysanthemum	123	solomon@fay.name	\N	\N	f	\N	1	\N	\N	55.797800000000000	48.948100000000000	4	\N
296	Cinnamon	123	katy_prosacco@douglas.net	\N	\N	f	\N	1	\N	\N	55.738200000000000	49.094000000000000	-14	\N
297	Cookie	123	bruce_sipes@mcdermott-adams.co	\N	\N	f	\N	1	\N	\N	55.865400000000000	48.970500000000000	10	\N
299	Daffodil	123	jarrod_casper@carter.io	\N	\N	f	\N	1	\N	\N	55.712000000000000	49.133200000000000	8	\N
300	Dahlia	123	fidela@ohara.io	\N	\N	f	\N	1	\N	\N	55.904100000000000	49.069800000000000	-4	\N
301	Daisy	123	gena@bergstrom-collier.net	\N	\N	f	\N	1	\N	\N	55.749400000000000	49.161600000000000	17	\N
302	Dandelion	123	misha.mueller@fritsch-hahn.biz	\N	\N	f	\N	1	\N	\N	55.713400000000000	49.290500000000000	-6	\N
303	Delicious	123	morris_herman@gulgowski.co	\N	\N	f	\N	0	\N	\N	55.839500000000000	48.793900000000000	0	\N
304	Delight	123	gaye@bergstrom-king.net	\N	\N	f	\N	0	\N	\N	55.876600000000000	49.014600000000000	2	\N
305	Donut	123	cedrick.thompson@lind.io	\N	\N	f	\N	1	\N	\N	55.757300000000000	49.162300000000000	4	\N
307	Doughnut	123	vincenza@brown.biz	\N	\N	f	\N	0	\N	\N	55.777200000000000	49.305700000000000	20	\N
308	Fennel	123	dudley_waters@kutch-bruen.biz	\N	\N	f	\N	1	\N	\N	55.777100000000000	48.793200000000000	1	\N
309	Fresh	123	jeanmarie_beer@littel.com	\N	\N	f	\N	0	\N	\N	55.893200000000000	49.269500000000000	-10	\N
310	Frosting	123	darby_johns@pagac.info	\N	\N	f	\N	1	\N	\N	55.795300000000000	48.823600000000000	-18	\N
311	Ginger	123	noelle_adams@muller-dibbert.org	\N	\N	f	\N	1	\N	\N	55.770800000000000	49.134900000000000	15	\N
312	Ginger Spice	123	jonathan.koch@moen.co	\N	\N	f	\N	1	\N	\N	55.753500000000000	48.957700000000000	6	\N
313	Gingersnap	123	luella@emmerich-wiegand.com	\N	\N	f	\N	1	\N	\N	55.731800000000000	49.037900000000000	19	\N
314	Good ‘n Plenty	123	hortencia@heathcote.name	\N	\N	f	\N	1	\N	\N	55.746500000000000	49.375300000000000	-12	\N
315	Gummy Bear	123	rhona@crist-corwin.name	\N	\N	f	\N	1	\N	\N	55.731700000000000	48.808700000000000	-16	\N
316	Hershey Kiss	123	cathern@doyle.io	\N	\N	f	\N	1	\N	\N	55.882000000000000	49.370100000000000	-10	\N
318	Hot Cocoa	123	loree@lemke-auer.com	\N	\N	f	\N	0	\N	\N	55.895600000000000	48.825300000000000	-1	\N
320	Ice Pop	123	celena@gerlach-armstrong.org	\N	\N	f	\N	0	\N	\N	55.833900000000000	49.339600000000000	3	\N
322	Jelly Bean	123	kareem@bosco-emard.biz	\N	\N	f	\N	0	\N	\N	55.828500000000000	48.918000000000000	-7	\N
323	Jolly	123	dewayne@bashirian.org	\N	\N	f	\N	0	\N	\N	55.822300000000000	49.096700000000000	8	\N
324	Juice	123	gabriele@murray-bashirian.name	\N	\N	f	\N	0	\N	\N	55.722200000000000	48.975900000000000	20	\N
325	Juicy	123	katherine.abernathy@kihn-macejkovic.net	\N	\N	f	\N	0	\N	\N	55.891200000000000	48.930300000000000	19	\N
326	Junior Mint	123	rubin_hilll@conroy-schuster.co	\N	\N	f	\N	0	\N	\N	55.809800000000000	49.280700000000000	8	\N
327	Kit-Kat	123	vida@jaskolski-kirlin.org	\N	\N	f	\N	0	\N	\N	55.827900000000000	49.036200000000000	-5	\N
328	Leaf	123	lonnie.dubuque@barrows.net	\N	\N	f	\N	0	\N	\N	55.763300000000000	49.179400000000000	13	\N
329	Lemon	123	marco@cassin-borer.name	\N	\N	f	\N	0	\N	\N	55.833900000000000	49.016900000000000	17	\N
330	Lifesaver	123	antonio.kris@streich.info	\N	\N	f	\N	0	\N	\N	55.748900000000000	48.948300000000000	-20	\N
331	Lilac	123	kim_krajcik@moen-botsford.co	\N	\N	f	\N	1	\N	\N	55.868400000000000	48.994100000000000	15	\N
333	Maple	123	marie@trantow.info	\N	\N	f	\N	1	\N	\N	55.758500000000000	49.101600000000000	3	\N
334	Marigold	123	gwyn_kuhn@hettinger-abbott.biz	\N	\N	f	\N	1	\N	\N	55.724700000000000	49.177400000000000	-18	\N
335	Marshmallow	123	nga@wiegand-effertz.io	\N	\N	f	\N	1	\N	\N	55.711400000000000	48.840300000000000	-5	\N
336	Martini	123	chan@bechtelar-parker.com	\N	\N	f	\N	1	\N	\N	55.826900000000000	48.791100000000000	14	\N
337	Merlot	123	karleen@rau.co	\N	\N	f	\N	1	\N	\N	55.793200000000000	49.121800000000000	15	\N
338	Moonflower	123	titus@daniel-christiansen.co	\N	\N	f	\N	0	\N	\N	55.875200000000000	49.028900000000000	20	\N
339	Moonshine	123	spring.cummings@terry-zieme.biz	\N	\N	f	\N	1	\N	\N	55.787600000000000	48.838600000000000	-10	\N
340	Moscato	123	brock@metz.org	\N	\N	f	\N	1	\N	\N	55.811000000000000	48.917100000000000	4	\N
341	Muffin	123	cinderella_brekke@schinner.biz	\N	\N	f	\N	1	\N	\N	55.793800000000000	48.967200000000000	15	\N
342	Mulberry	123	dorothy@stokes-russel.name	\N	\N	f	\N	1	\N	\N	55.773100000000000	48.899700000000000	9	\N
344	Mushroom	123	carol_ebert@ratke-bins.co	\N	\N	f	\N	1	\N	\N	55.907500000000000	48.980100000000000	15	\N
345	Oreo	123	stephine.lowe@schroeder.net	\N	\N	f	\N	1	\N	\N	55.732300000000000	49.301800000000000	-8	\N
346	Peaches	123	rosetta@price.org	\N	\N	f	\N	0	\N	\N	55.807900000000000	48.980700000000000	-2	\N
347	Pecan	123	korey@kertzmann-strosin.biz	\N	\N	f	\N	1	\N	\N	55.894400000000000	49.328300000000000	15	\N
348	Pecan Pie	123	glynda@daugherty-schroeder.name	\N	\N	f	\N	1	\N	\N	55.857300000000000	49.114000000000000	1	\N
349	Peep	123	christopher@koss.com	\N	\N	f	\N	1	\N	\N	55.766900000000000	49.155600000000000	8	\N
350	Peppermint	123	millard@oconnell-berge.co	\N	\N	f	\N	0	\N	\N	55.702400000000000	49.288200000000000	14	\N
351	Petal	123	cecil.fahey@tromp.com	\N	\N	f	\N	0	\N	\N	55.809600000000000	49.089900000000000	6	\N
353	Pie	123	hollis@oberbrunner-jenkins.name	\N	\N	f	\N	1	\N	\N	55.759000000000000	49.000300000000000	-17	\N
354	Pixie	123	idella.walter@becker.com	\N	\N	f	\N	0	\N	\N	55.865500000000000	49.045200000000000	-3	\N
355	Plum	123	sarah@jerde.co	\N	\N	f	\N	1	\N	\N	55.897300000000000	48.933700000000000	-6	\N
356	Pocky	123	lupe@schmeler.co	\N	\N	f	\N	1	\N	\N	55.903000000000000	48.811800000000000	16	\N
357	Polka-Dot	123	albert_marquardt@buckridge-denesik.org	\N	\N	f	\N	1	\N	\N	55.830100000000000	48.794400000000000	-17	\N
358	Pop Rock	123	nancee@mitchell.info	\N	\N	f	\N	0	\N	\N	55.734200000000000	49.229900000000000	6	\N
359	Pop Tart	123	chanel@glover-schneider.com	\N	\N	f	\N	1	\N	\N	55.728600000000000	48.998500000000000	-19	\N
360	Poppy	123	louis@glover.info	\N	\N	f	\N	0	\N	\N	55.812100000000000	49.041300000000000	-8	\N
362	Protein	123	orval@welch.org	\N	\N	f	\N	1	\N	\N	55.783000000000000	49.026100000000000	2	\N
363	Red Velvet	123	ernestina@kessler-pollich.com	\N	\N	f	\N	0	\N	\N	55.777600000000000	48.872700000000000	19	\N
364	Red Vine	123	nicky@olson-hilpert.name	\N	\N	f	\N	1	\N	\N	55.846200000000000	48.934400000000000	-13	\N
365	Reed	123	allan@weimann.info	\N	\N	f	\N	0	\N	\N	55.899800000000000	49.040800000000000	-7	\N
366	Rose	123	deloris@mraz.org	\N	\N	f	\N	1	\N	\N	55.801300000000000	49.121200000000000	3	\N
367	Rosie	123	darin_johnston@russel.co	\N	\N	f	\N	0	\N	\N	55.851300000000000	49.323800000000000	20	\N
369	Rummy	123	jessica@daniel-herzog.net	\N	\N	f	\N	1	\N	\N	55.744300000000000	49.055400000000000	3	\N
370	S’Mores	123	reginald@king.com	\N	\N	f	\N	1	\N	\N	55.859800000000000	49.083200000000000	-6	\N
371	Sherry	123	linnea@jakubowski.co	\N	\N	f	\N	0	\N	\N	55.741500000000000	49.292900000000000	12	\N
372	Shortbread	123	renaldo_armstrong@gleason.io	\N	\N	f	\N	1	\N	\N	55.893800000000000	49.213700000000000	7	\N
373	Shortcake	123	mohamed@bergnaum-hoeger.com	\N	\N	f	\N	0	\N	\N	55.738400000000000	48.790100000000000	-8	\N
374	Sienna	123	lillian@jast.name	\N	\N	f	\N	1	\N	\N	55.825300000000000	48.964700000000000	-6	\N
375	Sierra	123	joi@doyle.name	\N	\N	f	\N	1	\N	\N	55.869500000000000	49.248900000000000	7	\N
376	Skittle	123	gerry_runolfsdottir@brekke-rice.io	\N	\N	f	\N	1	\N	\N	55.706800000000000	48.845900000000000	14	\N
378	Snapdragon	123	bobbie_runolfsson@thiel.info	\N	\N	f	\N	0	\N	\N	55.880400000000000	49.021700000000000	-16	\N
379	Snappie	123	laure_strosin@gerhold.name	\N	\N	f	\N	1	\N	\N	55.796000000000000	48.931000000000000	13	\N
380	Snapple	123	elfreda.gislason@mueller.org	\N	\N	f	\N	1	\N	\N	55.777600000000000	48.901300000000000	-13	\N
381	Snickerdoodle	123	rashad@bruen.name	\N	\N	f	\N	0	\N	\N	55.740100000000000	49.121900000000000	5	\N
382	Snickers	123	isreal.bergstrom@nienow.com	\N	\N	f	\N	1	\N	\N	55.841900000000000	49.021900000000000	-1	\N
383	Snowdrop	123	winnie_cassin@labadie.org	\N	\N	f	\N	1	\N	\N	55.806400000000000	49.149900000000000	12	\N
384	Soda Pop	123	anh@ledner.net	\N	\N	f	\N	1	\N	\N	55.861600000000000	49.286200000000000	13	\N
385	Starburst	123	thomas@auer.org	\N	\N	f	\N	1	\N	\N	55.722400000000000	49.366300000000000	-7	\N
387	Strawberry	123	casey@vandervort-donnelly.info	\N	\N	f	\N	1	\N	\N	55.764600000000000	48.832800000000000	-12	\N
388	Sugar	123	novella@crona.org	\N	\N	f	\N	1	\N	\N	55.842800000000000	48.793600000000000	-9	\N
389	Sugar Pie	123	carey@lowe.biz	\N	\N	f	\N	1	\N	\N	55.889600000000000	48.801800000000000	-4	\N
390	Sugar Plum	123	jeanene.weber@leffler.io	\N	\N	f	\N	0	\N	\N	55.832800000000000	49.145200000000000	-6	\N
391	Sugar Rush	123	jennefer@schneider.info	\N	\N	f	\N	1	\N	\N	55.733300000000000	49.183300000000000	6	\N
392	Sunflower	123	yen.will@gottlieb.org	\N	\N	f	\N	0	\N	\N	55.909000000000000	48.894900000000000	-13	\N
393	Susie Q	123	tonya@stroman.io	\N	\N	f	\N	0	\N	\N	55.856700000000000	48.979100000000000	8	\N
394	Sweet Tooth	123	jocelyn.rogahn@beer.info	\N	\N	f	\N	0	\N	\N	55.721300000000000	49.307300000000000	16	\N
396	Sweetie Pie	123	jolene_kunze@kling.co	\N	\N	f	\N	0	\N	\N	55.889700000000000	48.932600000000000	-1	\N
397	Taffy	123	soo@boyer.org	\N	\N	f	\N	1	\N	\N	55.709200000000000	49.067700000000000	14	\N
398	Tansy	123	arlene@little.org	\N	\N	f	\N	1	\N	\N	55.882000000000000	48.860000000000000	-17	\N
399	Tart	123	chandra_cremin@volkman-larson.name	\N	\N	f	\N	0	\N	\N	55.864200000000000	48.925900000000000	-10	\N
400	Tequila	123	claude@halvorson.org	\N	\N	f	\N	1	\N	\N	55.734700000000000	49.247400000000000	-2	\N
401	Thumbprint	123	matha.schaefer@pacocha.biz	\N	\N	f	\N	0	\N	\N	55.705600000000000	48.896000000000000	-19	\N
403	Tootsie Pop	123	antoine_runte@rau.com	\N	\N	f	\N	1	\N	\N	55.846900000000000	48.855800000000000	18	\N
404	Tootsie Roll	123	elmer@marquardt-hoeger.info	\N	\N	f	\N	1	\N	\N	55.726500000000000	49.147700000000000	-13	\N
405	Tulip	123	barbie_okeefe@stracke.net	\N	\N	f	\N	1	\N	\N	55.872500000000000	48.809400000000000	-1	\N
406	Twix	123	elliott@schimmel.info	\N	\N	f	\N	0	\N	\N	55.871300000000000	49.089800000000000	20	\N
407	Twizzler	123	jeanne.reynolds@towne-gutkowski.com	\N	\N	f	\N	0	\N	\N	55.761600000000000	49.347000000000000	19	\N
409	Vitamin C	123	dominic_gusikowski@smitham-grant.com	\N	\N	f	\N	1	\N	\N	55.751700000000000	48.983100000000000	-18	\N
410	Whirly	123	royce.rutherford@okeefe.com	\N	\N	f	\N	1	\N	\N	55.710300000000000	48.967500000000000	1	\N
411	Wild Rose	123	hassan@lowe-haley.com	\N	\N	f	\N	0	\N	\N	55.790400000000000	49.212100000000000	15	\N
412	Wildberry	123	janette@sauer.io	\N	\N	f	\N	1	\N	\N	55.875300000000000	49.166100000000000	-13	\N
413	Willow	123	hai.bode@terry.info	\N	\N	f	\N	0	\N	\N	55.740300000000000	49.021900000000000	20	\N
414	Wonka	123	malcolm@roob.co	\N	\N	f	\N	0	\N	\N	55.716700000000000	49.279100000000000	2	\N
415	Chikoo	123	daria_kuvalis@davis.name	\N	\N	f	\N	0	\N	\N	55.888200000000000	49.058400000000000	-9	\N
416	Pihu	123	erin@pollich.com	\N	\N	f	\N	1	\N	\N	55.878100000000000	48.905200000000000	6	\N
417	Golu	123	jack@towne.biz	\N	\N	f	\N	1	\N	\N	55.736000000000000	49.037700000000000	6	\N
419	Guddu	123	charlie_jacobi@stroman.info	\N	\N	f	\N	1	\N	\N	55.828000000000000	49.151300000000000	-3	\N
420	Gudiya	123	josiah_corwin@jacobson.info	\N	\N	f	\N	1	\N	\N	55.834300000000000	48.984800000000000	-19	\N
421	Misht	123	aleen@runolfsson.co	\N	\N	f	\N	0	\N	\N	55.768000000000000	49.321800000000000	6	\N
422	Shree	123	alonso@hoeger.com	\N	\N	f	\N	1	\N	\N	55.760500000000000	48.973000000000000	-9	\N
423	Piya	123	petra.heathcote@kohler.biz	\N	\N	f	\N	1	\N	\N	55.897100000000000	49.151700000000000	1	\N
424	Adi	123	freddie.ledner@daniel.info	\N	\N	f	\N	0	\N	\N	55.826200000000000	48.817800000000000	-9	\N
425	Kuhu	123	dino.jaskolski@marquardt.biz	\N	\N	f	\N	1	\N	\N	55.849000000000000	49.282800000000000	-8	\N
427	Brishti	123	luciano@bayer.info	\N	\N	f	\N	1	\N	\N	55.736700000000000	48.870400000000000	-6	\N
428	Nutz	123	tempie@rosenbaum-pfeffer.io	\N	\N	f	\N	0	\N	\N	55.885800000000000	49.297200000000000	-8	\N
429	Shinoo	123	clinton.corkery@bartell.net	\N	\N	f	\N	1	\N	\N	55.902700000000000	49.161700000000000	4	\N
430	Chintu	123	dianna@kuhic-bartoletti.net	\N	\N	f	\N	1	\N	\N	55.783300000000000	49.357000000000000	-13	\N
431	Chimpu	123	tony@heaney-rippin.biz	\N	\N	f	\N	0	\N	\N	55.907900000000000	49.284000000000000	-3	\N
432	Piku	123	lindsay_bruen@okeefe.biz	\N	\N	f	\N	1	\N	\N	55.829500000000000	49.194100000000000	-12	\N
433	Pappu	123	dan.runolfsdottir@batz.io	\N	\N	f	\N	1	\N	\N	55.743500000000000	49.148300000000000	3	\N
434	Chikki	123	vaughn@mcclure.com	\N	\N	f	\N	0	\N	\N	55.879900000000000	49.199100000000000	19	\N
435	Raja	123	lavina_farrell@hayes.name	\N	\N	f	\N	0	\N	\N	55.827300000000000	49.253500000000000	10	\N
436	Rani	123	cristi@glover.co	\N	\N	f	\N	1	\N	\N	55.791300000000000	49.039500000000000	-17	\N
438	Dimpy	123	mack.kozey@hagenes.net	\N	\N	f	\N	1	\N	\N	55.796700000000000	48.996500000000000	17	\N
439	Proshu	123	myles@ullrich.info	\N	\N	f	\N	1	\N	\N	55.794900000000000	48.803400000000000	-10	\N
440	Prishu	123	christopher@considine.net	\N	\N	f	\N	0	\N	\N	55.866400000000000	48.980800000000000	0	\N
441	Chinu	123	derick@ferry.net	\N	\N	f	\N	0	\N	\N	55.730200000000000	49.337300000000000	-7	\N
442	Babloo	123	kenna@hartmann.co	\N	\N	f	\N	0	\N	\N	55.882600000000000	49.239100000000000	-2	\N
443	Babli	123	tanika.hahn@schinner.info	\N	\N	f	\N	1	\N	\N	55.800600000000000	49.216500000000000	-15	\N
445	Amu	123	francisco@legros.com	\N	\N	f	\N	1	\N	\N	55.888100000000000	48.874200000000000	-17	\N
446	Sunny	123	arden_fay@king-monahan.name	\N	\N	f	\N	0	\N	\N	55.716800000000000	49.199700000000000	8	\N
447	Raj	123	lavinia.feest@bailey.info	\N	\N	f	\N	1	\N	\N	55.891400000000000	49.034400000000000	12	\N
448	Raji	123	kim_harris@durgan.io	\N	\N	f	\N	1	\N	\N	55.890200000000000	49.378300000000000	9	\N
449	Rishu	123	wm@feeney.com	\N	\N	f	\N	1	\N	\N	55.868400000000000	48.873600000000000	-1	\N
450	Rosy	123	dorsey_russel@reilly.org	\N	\N	f	\N	1	\N	\N	55.811800000000000	49.368100000000000	2	\N
451	June	123	kenyetta@pagac.info	\N	\N	f	\N	1	\N	\N	55.719200000000000	48.846600000000000	-16	\N
452	Ria	123	vince.bahringer@fritsch-nitzsche.info	\N	\N	f	\N	1	\N	\N	55.895800000000000	49.184700000000000	-6	\N
454	Cashew	123	hyon_schiller@schuppe-lubowitz.info	\N	\N	f	\N	1	\N	\N	55.888800000000000	49.311100000000000	-10	\N
455	Benu	123	michaele_koepp@torp.net	\N	\N	f	\N	1	\N	\N	55.751500000000000	49.106300000000000	10	\N
456	Betoo	123	wilfredo@brekke.biz	\N	\N	f	\N	1	\N	\N	55.726800000000000	49.156100000000000	14	\N
457	Oshu	123	isiah@aufderhar-rath.info	\N	\N	f	\N	1	\N	\N	55.745500000000000	49.091300000000000	15	\N
458	Rick	123	young@muller-tremblay.info	\N	\N	f	\N	1	\N	\N	55.724900000000000	48.846200000000000	-19	\N
459	Vini	123	mandie_oconnell@casper.biz	\N	\N	f	\N	0	\N	\N	55.903900000000000	49.330500000000000	-7	\N
460	Luv	123	roy@cremin.org	\N	\N	f	\N	1	\N	\N	55.838700000000000	48.958900000000000	-16	\N
461	Rini	123	janiece_watsica@kihn.com	\N	\N	f	\N	0	\N	\N	55.886600000000000	49.070300000000000	-14	\N
462	Tuli	123	emeline@glover.info	\N	\N	f	\N	0	\N	\N	55.838700000000000	49.305700000000000	19	\N
463	Joy	123	elizbeth@daugherty-johnston.net	\N	\N	f	\N	1	\N	\N	55.835500000000000	48.936600000000000	7	\N
465	Bao	123	nathanial.wiegand@swift.net	\N	\N	f	\N	1	\N	\N	55.897800000000000	49.255300000000000	-18	\N
466	Cuckoo	123	iris@prohaska.biz	\N	\N	f	\N	0	\N	\N	55.721000000000000	49.119200000000000	0	\N
467	Echo	123	nathaniel.turner@von.biz	\N	\N	f	\N	1	\N	\N	55.885600000000000	49.252100000000000	3	\N
468	Fifi	123	deon.heller@mayert.com	\N	\N	f	\N	0	\N	\N	55.774000000000000	48.971000000000000	-3	\N
469	Lala	123	lee_smith@kassulke.io	\N	\N	f	\N	0	\N	\N	55.851100000000000	49.345900000000000	-13	\N
470	Ricky	123	louie@klein-lueilwitz.name	\N	\N	f	\N	1	\N	\N	55.854400000000000	49.216400000000000	-9	\N
471	Shoona	123	lai@conroy-price.io	\N	\N	f	\N	0	\N	\N	55.755700000000000	48.975200000000000	-12	\N
472	Tolu	123	susana@kling-frami.name	\N	\N	f	\N	1	\N	\N	55.866700000000000	49.038500000000000	10	\N
474	Abhi	123	arthur.kutch@kirlin.io	\N	\N	f	\N	1	\N	\N	55.842400000000000	49.180700000000000	-13	\N
475	Ani	123	charlena_oconnell@brekke-green.co	\N	\N	f	\N	1	\N	\N	55.861900000000000	49.160900000000000	-10	\N
476	Anu	123	elane@mckenzie.io	\N	\N	f	\N	1	\N	\N	55.881400000000000	49.064300000000000	-5	\N
477	Ajju	123	fredric@waelchi.co	\N	\N	f	\N	1	\N	\N	55.900100000000000	49.340000000000000	15	\N
478	Anju	123	abe@russel.co	\N	\N	f	\N	0	\N	\N	55.766500000000000	48.831100000000000	12	\N
479	Anshu	123	wendie.sipes@boehm.com	\N	\N	f	\N	0	\N	\N	55.781800000000000	48.963200000000000	18	\N
480	Appu	123	nick_hills@leuschke.io	\N	\N	f	\N	0	\N	\N	55.819300000000000	48.833300000000000	15	\N
482	Babun	123	an.marks@veum.net	\N	\N	f	\N	1	\N	\N	55.884900000000000	49.024600000000000	12	\N
483	Bibhu	123	lanny.franecki@auer.info	\N	\N	f	\N	1	\N	\N	55.880400000000000	49.315700000000000	7	\N
484	Bela	123	cedric.johns@armstrong.info	\N	\N	f	\N	1	\N	\N	55.784600000000000	48.943400000000000	-19	\N
485	Binny	123	kathryn_bode@rath.co	\N	\N	f	\N	0	\N	\N	55.907800000000000	49.014500000000000	1	\N
486	Charu	123	irena@murray-kling.io	\N	\N	f	\N	0	\N	\N	55.781900000000000	49.309400000000000	-15	\N
72	Captain Marvel	123	vilma@rice-rolfson.co	\N	\N	f	\N	1	\N	\N	55.838100000000000	49.187100000000000	18	\N
74	Charlie’s Angel	123	jonathon.oconnell@west.com	\N	\N	f	\N	0	\N	\N	55.861900000000000	48.935900000000000	10	\N
85	Fighter	123	elbert_cummings@ruecker-marvin.net	\N	\N	f	\N	1	\N	\N	55.854500000000000	49.067800000000000	16	\N
93	Jessica Rabbit	123	terese_heathcote@nienow.name	\N	\N	f	\N	0	\N	\N	55.745800000000000	49.229300000000000	-17	\N
104	Onyx	123	cleo_abshire@howell-littel.info	\N	\N	f	\N	0	\N	\N	55.836300000000000	49.320400000000000	-3	\N
112	Sphinx	123	andree_bradtke@mertz.org	\N	\N	f	\N	1	\N	\N	55.754100000000000	49.308600000000000	-4	\N
121	Vicious	123	tania_cartwright@kirlin-oconnell.co	\N	\N	f	\N	1	\N	\N	55.778600000000000	49.340700000000000	-4	\N
131	BFG – For Big Friendly Giant	123	shanti@stroman.name	\N	\N	f	\N	1	\N	\N	55.768300000000000	48.917300000000000	10	\N
141	Corny	123	elias.hettinger@monahan-bernhard.name	\N	\N	f	\N	0	\N	\N	55.742900000000000	49.365500000000000	-13	\N
151	Funny Girl	123	arlene@moen-stark.biz	\N	\N	f	\N	1	\N	\N	55.823700000000000	49.322400000000000	3	\N
155	Gopher	123	porfirio@koepp-toy.co	\N	\N	f	\N	0	\N	\N	55.795300000000000	49.161900000000000	-4	\N
161	Mittens	123	lorenza.turner@bergnaum.name	\N	\N	f	\N	0	\N	\N	55.901500000000000	49.167700000000000	-12	\N
488	Chhotu	123	darin.emmerich@feeney.biz	\N	\N	f	\N	0	\N	\N	55.808500000000000	49.093200000000000	7	\N
489	Dicki	123	hershel@herzog-donnelly.org	\N	\N	f	\N	1	\N	\N	55.707400000000000	49.262900000000000	5	\N
490	Dipu	123	beverly_wilderman@schmeler.biz	\N	\N	f	\N	1	\N	\N	55.829900000000000	49.253000000000000	-4	\N
492	Dola	123	sidney.kihn@dickinson.co	\N	\N	f	\N	0	\N	\N	55.892700000000000	48.817200000000000	-15	\N
493	Dona	123	harrison_rosenbaum@heaney-ratke.co	\N	\N	f	\N	1	\N	\N	55.739100000000000	49.088000000000000	-15	\N
494	Dushtu	123	paola@haley-mccullough.net	\N	\N	f	\N	0	\N	\N	55.832000000000000	49.029400000000000	11	\N
495	Gopi	123	nina.kihn@stroman-fahey.net	\N	\N	f	\N	0	\N	\N	55.748100000000000	48.846100000000000	6	\N
496	Gitu	123	boyce@durgan-rosenbaum.biz	\N	\N	f	\N	1	\N	\N	55.750100000000000	49.165000000000000	20	\N
497	Goli	123	kelvin@lowe.co	\N	\N	f	\N	1	\N	\N	55.805300000000000	49.007100000000000	-20	\N
499	Ishu	123	jasper@beier-wisozk.biz	\N	\N	f	\N	1	\N	\N	55.891300000000000	49.102100000000000	-3	\N
500	Janu	123	grover_bins@leffler-bins.name	\N	\N	f	\N	0	\N	\N	55.903100000000000	48.803600000000000	14	\N
501	Laado	123	dennis.stiedemann@streich-schinner.org	\N	\N	f	\N	1	\N	\N	55.875200000000000	48.917400000000000	9	\N
502	Lolo	123	merlyn.mraz@bogan.name	\N	\N	f	\N	0	\N	\N	55.732600000000000	48.960300000000000	-1	\N
503	Laajo	123	sparkle_predovic@hoppe.com	\N	\N	f	\N	1	\N	\N	55.881000000000000	49.340300000000000	-4	\N
504	Lalli	123	arlette_lehner@collier.net	\N	\N	f	\N	1	\N	\N	55.714000000000000	49.092800000000000	-4	\N
505	Mili	123	omar@green.io	\N	\N	f	\N	0	\N	\N	55.782700000000000	48.874400000000000	13	\N
506	Mitu	123	levi@roob-graham.io	\N	\N	f	\N	0	\N	\N	55.867300000000000	48.832100000000000	17	\N
507	Nilu	123	bradford@muller-wyman.co	\N	\N	f	\N	0	\N	\N	55.786300000000000	48.835000000000000	-4	\N
508	Niru	123	sunshine_quitzon@boehm.org	\N	\N	f	\N	1	\N	\N	55.750500000000000	49.333100000000000	3	\N
510	Pinky	123	lelah_nitzsche@larkin.info	\N	\N	f	\N	1	\N	\N	55.758100000000000	49.058000000000000	-18	\N
511	Vinu	123	ryan@fay.com	\N	\N	f	\N	0	\N	\N	55.811300000000000	49.225600000000000	19	\N
512	Vishu	123	marshall.medhurst@flatley.co	\N	\N	f	\N	1	\N	\N	55.792700000000000	49.284700000000000	-18	\N
513	Veeru	123	charley@bins-bergstrom.net	\N	\N	f	\N	0	\N	\N	55.781900000000000	48.807800000000000	19	\N
514	Vicky	123	veta@sanford-schultz.co	\N	\N	f	\N	1	\N	\N	55.808300000000000	49.032900000000000	1	\N
515	Sanju	123	catherin@emard.name	\N	\N	f	\N	0	\N	\N	55.852500000000000	48.829900000000000	-12	\N
516	Sonu	123	peter@towne-mccullough.biz	\N	\N	f	\N	1	\N	\N	55.775800000000000	48.955200000000000	15	\N
517	Tasu	123	rosario@rodriguez.io	\N	\N	f	\N	0	\N	\N	55.886200000000000	48.830100000000000	6	\N
519	Vasu	123	sheryll_weissnat@hagenes-strosin.net	\N	\N	f	\N	0	\N	\N	55.876500000000000	49.169400000000000	-9	\N
520	Viju	123	benny@becker-oreilly.net	\N	\N	f	\N	1	\N	\N	55.797800000000000	49.298000000000000	-16	\N
521	Bittu	123	alba.vandervort@schowalter.net	\N	\N	f	\N	1	\N	\N	55.705700000000000	49.072700000000000	8	\N
522	Aaradhya	123	maddie.ritchie@huel-fadel.io	\N	\N	f	\N	1	\N	\N	55.842100000000000	49.200800000000000	14	\N
523	Ayaan	123	vesta_gerhold@wilderman.co	\N	\N	f	\N	1	\N	\N	55.867300000000000	49.019300000000000	20	\N
524	Drishti	123	fanny@hickle.io	\N	\N	f	\N	0	\N	\N	55.778700000000000	48.996100000000000	-9	\N
526	Kabir	123	carla@lang.biz	\N	\N	f	\N	0	\N	\N	55.775100000000000	48.883500000000000	16	\N
527	Laksh	123	everett@wunsch.biz	\N	\N	f	\N	1	\N	\N	55.811600000000000	48.846400000000000	-1	\N
528	Mishka	123	delta.wilkinson@mcclure.net	\N	\N	f	\N	1	\N	\N	55.830700000000000	49.063600000000000	10	\N
529	Ranbir	123	spring@bode.org	\N	\N	f	\N	1	\N	\N	55.797400000000000	49.048300000000000	-8	\N
530	Rudra	123	dong@fadel-hagenes.io	\N	\N	f	\N	0	\N	\N	55.850000000000000	49.153900000000000	-3	\N
531	Shanaya	123	phillip@kuphal.biz	\N	\N	f	\N	0	\N	\N	55.852400000000000	49.356600000000000	11	\N
532	Alee	123	les.kertzmann@thiel.biz	\N	\N	f	\N	1	\N	\N	55.767200000000000	49.136300000000000	-15	\N
533	Coco	123	anthony@spencer.io	\N	\N	f	\N	0	\N	\N	55.836900000000000	49.214400000000000	-10	\N
534	Kiku	123	deirdre.kshlerin@hintz.org	\N	\N	f	\N	1	\N	\N	55.820200000000000	48.809900000000000	6	\N
535	Bunz	123	anisha@west.com	\N	\N	f	\N	0	\N	\N	55.718500000000000	49.259200000000000	13	\N
537	Maddy	123	stacey@jakubowski-koepp.co	\N	\N	f	\N	0	\N	\N	55.858500000000000	49.018500000000000	3	\N
538	Sophie	123	calandra@nienow-zulauf.net	\N	\N	f	\N	0	\N	\N	55.762500000000000	48.856800000000000	9	\N
539	Bebo	123	elissa@smitham.net	\N	\N	f	\N	1	\N	\N	55.881700000000000	49.280900000000000	1	\N
540	Amee	123	bulah@lind.biz	\N	\N	f	\N	1	\N	\N	55.705600000000000	48.834600000000000	3	\N
541	Nikki	123	quinn_miller@yundt.biz	\N	\N	f	\N	0	\N	\N	55.784700000000000	48.960900000000000	-2	\N
542	Tim	123	jerrell@gutmann.name	\N	\N	f	\N	0	\N	\N	55.768500000000000	49.245200000000000	2	\N
543	Tisca	123	millard@wiegand.info	\N	\N	f	\N	1	\N	\N	55.907900000000000	48.875400000000000	17	\N
544	Toshi	123	theodore@goyette-mohr.io	\N	\N	f	\N	0	\N	\N	55.723300000000000	48.996300000000000	-5	\N
546	Alia	123	shara_kemmer@purdy.biz	\N	\N	f	\N	1	\N	\N	55.746100000000000	48.946900000000000	16	\N
547	Arya	123	leopoldo@schaefer.com	\N	\N	f	\N	1	\N	\N	55.729500000000000	48.995900000000000	-3	\N
548	Sonam	123	bee_wiegand@halvorson-stoltenberg.co	\N	\N	f	\N	0	\N	\N	55.901200000000000	48.835400000000000	7	\N
549	Pari	123	rosendo.schultz@zieme.info	\N	\N	f	\N	0	\N	\N	55.879900000000000	48.969300000000000	6	\N
550	Yuri	123	hsiu.wunsch@grimes.name	\N	\N	f	\N	0	\N	\N	55.881300000000000	49.244600000000000	19	\N
551	Yug	123	collin.bins@rempel.net	\N	\N	f	\N	1	\N	\N	55.801200000000000	49.337900000000000	-16	\N
553	Veda	123	aaron@damore.biz	\N	\N	f	\N	1	\N	\N	55.847700000000000	48.949100000000000	-6	\N
554	Mahi	123	hunter_tremblay@upton.biz	\N	\N	f	\N	0	\N	\N	55.889300000000000	49.225700000000000	12	\N
555	Asmi	123	normand@gleichner-nader.net	\N	\N	f	\N	0	\N	\N	55.910600000000000	49.308000000000000	-17	\N
556	Avni	123	rachael@feest.net	\N	\N	f	\N	0	\N	\N	55.735300000000000	48.811400000000000	17	\N
557	Aroo	123	quinn@luettgen-gorczany.info	\N	\N	f	\N	1	\N	\N	55.910700000000000	48.990400000000000	4	\N
169	Pip Squeak	123	mikel.bailey@schowalter-labadie.co	\N	\N	f	\N	1	\N	\N	55.752100000000000	48.987200000000000	13	\N
180	Squints	123	delorse_bergnaum@boehm.info	\N	\N	f	\N	0	\N	\N	55.740200000000000	48.853300000000000	-4	\N
189	Troublemaker	123	allan@altenwerth.com	\N	\N	f	\N	0	\N	\N	55.853700000000000	49.061400000000000	-1	\N
198	Baby Face	123	del_herzog@emmerich.info	\N	\N	f	\N	0	\N	\N	55.825400000000000	49.358700000000000	18	\N
206	Boo Boo Bear	123	alton.tromp@osinski-connelly.co	\N	\N	f	\N	1	\N	\N	55.851800000000000	48.847900000000000	-5	\N
216	Dream Girl	123	charlyn.hickle@cole.info	\N	\N	f	\N	0	\N	\N	55.718200000000000	49.380200000000000	8	\N
225	Kitten	123	carolynn_nienow@konopelski.org	\N	\N	f	\N	1	\N	\N	55.762000000000000	49.202000000000000	14	\N
234	Mon Cheri	123	darin.homenick@dach.name	\N	\N	f	\N	1	\N	\N	55.749200000000000	49.224300000000000	4	\N
237	My Beloved	123	jeff@haag-nader.org	\N	\N	f	\N	0	\N	\N	55.710100000000000	48.854100000000000	1	\N
241	Rainbow	123	willard.witting@paucek-bechtelar.co	\N	\N	f	\N	0	\N	\N	55.835400000000000	49.203900000000000	-17	\N
251	Sweetheart	123	korey@sauer-hilll.info	\N	\N	f	\N	0	\N	\N	55.753400000000000	49.022800000000000	12	\N
558	bfalmer	QPa1WAEX5DWlPoEU3WRQZ0nUWdUvA/DLXPYuCkFXDOs=$XcW1m09rjHAywl0pPN8k3mXzQ3Go5zVBtI0BGxBUVPA=	texetomg@gmail.com	\N	\N	f	\N	-1	\N	AVnvQ4K4tgVSe21TIiVtfsJlbOggpem7qRpeOwpE	0.000000000000000	0.000000000000000	0	\N
559	123	hhT3Sczfh4MtTyeULHC6Eb0NXXprx63wJuCQ9jnAhhI=$nBXw2VCDOCo+nhoigU7kSaFa1sZjOw7PKG9k5Gd1BLQ=	zininlol111@rambler.ru	\N	\N	f	\N	-1	\N	CDtUosXvnwH6MlKMATLxDX6IiKEhThgIW7Kdp0Ce	0.000000000000000	0.000000000000000	0	\N
561	123123123	Xnp7pg2x/FbSTdj+UqYBsrRLJSKQFnbbQ/s3N4XBET8=$Ootwp5HIusLHsnNdHDLOH7PtUdpAMtFvF492zx/HeB4=	lalala@rambler.ru	\N	\N	f	\N	-1	\N	Ie2hquroTtdRWERR5MOyrrH6SCrZxgpyO2KX4ilH	0.000000000000000	0.000000000000000	0	\N
564	1231231231	XlpYyQ0Q4tpVp7F2hlRRcfnw+qFDzFGRnZWaAJp84sM=$4tUB7HkGevwDZ/dEsbaZJ4M4/4fOiXbCfkIZ61rxKws=	lalala1@rambler.ru	\N	\N	f	\N	-1	\N	grvsxtSSGYUySJoJlJvtTZShUvnYzu51C3ZgpRWy	0.000000000000000	0.000000000000000	0	\N
565	111111	mBYT1TaHtX4pgRS1HbtNg0EGlTSRrCyOF823nZ/xgcE=$6ShyWbe6mHrTmuUkhhxv9w4fykeazqOBXmRxLxDY2lY=	zininlol111111@rambler.ru	\N	\N	f	\N	-1	\N	sCPmK7RUo3pH8WwHecPue1klZNABqZYxixDi1AfZ	0.000000000000000	0.000000000000000	0	\N
\.


--
-- Data for Name: t_users_unity; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--

COPY spark_db.t_users_unity (t_users_unity_id, user1_id, user2_id, confirmed) FROM stdin;
\.


--
-- Name: t_complaint_id_complaint_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_complaint_id_complaint_seq', 7, true);


--
-- Name: t_image_id_image_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_image_id_image_seq', 41, true);


--
-- Name: t_message_id_message_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_message_id_message_seq', 164, true);


--
-- Name: t_user_profile_user_profile_id_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_user_profile_user_profile_id_seq', 566, true);


--
-- Name: t_users_unity_t_users_unity_id_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_users_unity_t_users_unity_id_seq', 23, true);


--
-- Name: t_complaint t_complaint_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_complaint
    ADD CONSTRAINT t_complaint_pk PRIMARY KEY (id_complaint);


--
-- Name: t_image t_image_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_image
    ADD CONSTRAINT t_image_pk PRIMARY KEY (id_image);


--
-- Name: t_message t_message_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_message
    ADD CONSTRAINT t_message_pk PRIMARY KEY (id_message);


--
-- Name: t_user_profile t_user_profile_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_user_profile
    ADD CONSTRAINT t_user_profile_pk PRIMARY KEY (user_profile_id);


--
-- Name: t_users_unity t_users_unity_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_users_unity
    ADD CONSTRAINT t_users_unity_pk PRIMARY KEY (t_users_unity_id);


--
-- Name: t_complaint_id_complaint_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_complaint_id_complaint_uindex ON spark_db.t_complaint USING btree (id_complaint);


--
-- Name: t_image_id_image_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_image_id_image_uindex ON spark_db.t_image USING btree (id_image);


--
-- Name: t_message_id_message_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_message_id_message_uindex ON spark_db.t_message USING btree (id_message);


--
-- Name: t_user_profile_login_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_user_profile_login_uindex ON spark_db.t_user_profile USING btree (login);


--
-- Name: t_user_profile_user_profile_id_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_user_profile_user_profile_id_uindex ON spark_db.t_user_profile USING btree (user_profile_id);


--
-- Name: t_user_profile_verification_token_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_user_profile_verification_token_uindex ON spark_db.t_user_profile USING btree (confirmed_token);


--
-- Name: t_users_unity_t_users_unity_id_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_users_unity_t_users_unity_id_uindex ON spark_db.t_users_unity USING btree (t_users_unity_id);


--
-- Name: t_complaint t_complaint_t_user_profile_user_profile_id_fk; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_complaint
    ADD CONSTRAINT t_complaint_t_user_profile_user_profile_id_fk FOREIGN KEY (from_user) REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- Name: t_complaint t_complaint_t_user_profile_user_profile_id_fk_2; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_complaint
    ADD CONSTRAINT t_complaint_t_user_profile_user_profile_id_fk_2 FOREIGN KEY (to_user) REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- Name: t_image t_image_t_user_profile_user_profile_id_fk; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_image
    ADD CONSTRAINT t_image_t_user_profile_user_profile_id_fk FOREIGN KEY (user_id) REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- Name: t_message t_message_t_user_profile_user_profile_id_fk; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_message
    ADD CONSTRAINT t_message_t_user_profile_user_profile_id_fk FOREIGN KEY ("from") REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- Name: t_message t_message_t_user_profile_user_profile_id_fk_2; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_message
    ADD CONSTRAINT t_message_t_user_profile_user_profile_id_fk_2 FOREIGN KEY ("to") REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- Name: t_users_unity t_users_unity_t_user_profile__fk1; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_users_unity
    ADD CONSTRAINT t_users_unity_t_user_profile__fk1 FOREIGN KEY (user1_id) REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- Name: t_users_unity t_users_unity_t_user_profile__fk2; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_users_unity
    ADD CONSTRAINT t_users_unity_t_user_profile__fk2 FOREIGN KEY (user2_id) REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- PostgreSQL database dump complete
--

