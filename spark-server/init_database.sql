BEGIN;

DO $$
    BEGIN
        IF (SELECT count(*) FROM pg_user WHERE usename='sammy')=0 THEN
            create user sammy
                superuser
                createdb
                createrole;
        END IF;
    END
$$;


DO $$
    BEGIN
        IF (SELECT count(*) FROM pg_user WHERE usename='postgres')=0 THEN
            create user postgres
                superuser
                createdb
                createrole
                replication
                bypassrls;
        END IF;
    END
$$;

COMMIT;


--
-- PostgreSQL database dump
--

-- Dumped from database version 12.3
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

-- DROP DATABASE IF EXISTS postgres;
--
-- Name: postgres; Type: DATABASE; Schema: -; Owner: postgres
--

-- CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';


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
    is_main boolean DEFAULT false NOT NULL,
    bytes character varying(4000000)
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
-- Name: t_looked_user; Type: TABLE; Schema: spark_db; Owner: sammy
--

CREATE TABLE spark_db.t_looked_user (
    id_looked_user integer NOT NULL,
    from_user integer NOT NULL,
    to_user integer NOT NULL
);


ALTER TABLE spark_db.t_looked_user OWNER TO sammy;

--
-- Name: t_looked_user_id_looked_user_seq; Type: SEQUENCE; Schema: spark_db; Owner: sammy
--

CREATE SEQUENCE spark_db.t_looked_user_id_looked_user_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE spark_db.t_looked_user_id_looked_user_seq OWNER TO sammy;

--
-- Name: t_looked_user_id_looked_user_seq; Type: SEQUENCE OWNED BY; Schema: spark_db; Owner: sammy
--

ALTER SEQUENCE spark_db.t_looked_user_id_looked_user_seq OWNED BY spark_db.t_looked_user.id_looked_user;


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
-- Name: t_search_data; Type: TABLE; Schema: spark_db; Owner: sammy
--

CREATE TABLE spark_db.t_search_data (
    search_data_id integer NOT NULL,
    filter_hash integer NOT NULL,
    user_id integer NOT NULL,
    number integer
);


ALTER TABLE spark_db.t_search_data OWNER TO sammy;

--
-- Name: t_search_data_search_data_id_seq; Type: SEQUENCE; Schema: spark_db; Owner: sammy
--

CREATE SEQUENCE spark_db.t_search_data_search_data_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE spark_db.t_search_data_search_data_id_seq OWNER TO sammy;

--
-- Name: t_search_data_search_data_id_seq; Type: SEQUENCE OWNED BY; Schema: spark_db; Owner: sammy
--

ALTER SEQUENCE spark_db.t_search_data_search_data_id_seq OWNED BY spark_db.t_search_data.search_data_id;


--
-- Name: t_tag; Type: TABLE; Schema: spark_db; Owner: sammy
--

CREATE TABLE spark_db.t_tag (
    tag_td integer NOT NULL,
    name character varying(128) NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE spark_db.t_tag OWNER TO sammy;

--
-- Name: t_tag_tag_td_seq; Type: SEQUENCE; Schema: spark_db; Owner: sammy
--

CREATE SEQUENCE spark_db.t_tag_tag_td_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE spark_db.t_tag_tag_td_seq OWNER TO sammy;

--
-- Name: t_tag_tag_td_seq; Type: SEQUENCE OWNED BY; Schema: spark_db; Owner: sammy
--

ALTER SEQUENCE spark_db.t_tag_tag_td_seq OWNED BY spark_db.t_tag.tag_td;


--
-- Name: t_user_filter; Type: TABLE; Schema: spark_db; Owner: sammy
--

CREATE TABLE spark_db.t_user_filter (
    user_filter_id integer NOT NULL,
    from_user_id integer NOT NULL,
    filter_hash integer NOT NULL,
    counter integer DEFAULT 0 NOT NULL
);


ALTER TABLE spark_db.t_user_filter OWNER TO sammy;

--
-- Name: t_user_filter_user_filter_id_seq; Type: SEQUENCE; Schema: spark_db; Owner: sammy
--

CREATE SEQUENCE spark_db.t_user_filter_user_filter_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE spark_db.t_user_filter_user_filter_id_seq OWNER TO sammy;

--
-- Name: t_user_filter_user_filter_id_seq; Type: SEQUENCE OWNED BY; Schema: spark_db; Owner: sammy
--

ALTER SEQUENCE spark_db.t_user_filter_user_filter_id_seq OWNED BY spark_db.t_user_filter.user_filter_id;


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
    age integer,
    new_mail character varying(256),
    last_auth_date bigint DEFAULT '1601144834213'::bigint NOT NULL
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
-- Name: t_looked_user id_looked_user; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_looked_user ALTER COLUMN id_looked_user SET DEFAULT nextval('spark_db.t_looked_user_id_looked_user_seq'::regclass);


--
-- Name: t_message id_message; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_message ALTER COLUMN id_message SET DEFAULT nextval('spark_db.t_message_id_message_seq'::regclass);


--
-- Name: t_search_data search_data_id; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_search_data ALTER COLUMN search_data_id SET DEFAULT nextval('spark_db.t_search_data_search_data_id_seq'::regclass);


--
-- Name: t_tag tag_td; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_tag ALTER COLUMN tag_td SET DEFAULT nextval('spark_db.t_tag_tag_td_seq'::regclass);


--
-- Name: t_user_filter user_filter_id; Type: DEFAULT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_user_filter ALTER COLUMN user_filter_id SET DEFAULT nextval('spark_db.t_user_filter_user_filter_id_seq'::regclass);


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



--
-- Data for Name: t_image; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--



--
-- Data for Name: t_looked_user; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--



--
-- Data for Name: t_message; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--



--
-- Data for Name: t_search_data; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--



--
-- Data for Name: t_tag; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--



--
-- Data for Name: t_user_filter; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--



--
-- Data for Name: t_user_profile; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--



--
-- Data for Name: t_users_unity; Type: TABLE DATA; Schema: spark_db; Owner: sammy
--



--
-- Name: t_complaint_id_complaint_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_complaint_id_complaint_seq', 19, true);


--
-- Name: t_image_id_image_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_image_id_image_seq', 824, true);


--
-- Name: t_looked_user_id_looked_user_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_looked_user_id_looked_user_seq', 35, true);


--
-- Name: t_message_id_message_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_message_id_message_seq', 197, true);


--
-- Name: t_search_data_search_data_id_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_search_data_search_data_id_seq', 11112, true);


--
-- Name: t_tag_tag_td_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_tag_tag_td_seq', 1173, true);


--
-- Name: t_user_filter_user_filter_id_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_user_filter_user_filter_id_seq', 23, true);


--
-- Name: t_user_profile_user_profile_id_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_user_profile_user_profile_id_seq', 1073, true);


--
-- Name: t_users_unity_t_users_unity_id_seq; Type: SEQUENCE SET; Schema: spark_db; Owner: sammy
--

SELECT pg_catalog.setval('spark_db.t_users_unity_t_users_unity_id_seq', 45, true);


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
-- Name: t_looked_user t_looked_user_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_looked_user
    ADD CONSTRAINT t_looked_user_pk PRIMARY KEY (id_looked_user);


--
-- Name: t_message t_message_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_message
    ADD CONSTRAINT t_message_pk PRIMARY KEY (id_message);


--
-- Name: t_search_data t_search_data_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_search_data
    ADD CONSTRAINT t_search_data_pk PRIMARY KEY (search_data_id);


--
-- Name: t_tag t_tag_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_tag
    ADD CONSTRAINT t_tag_pk PRIMARY KEY (tag_td);


--
-- Name: t_user_filter t_user_filter_pk; Type: CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_user_filter
    ADD CONSTRAINT t_user_filter_pk PRIMARY KEY (user_filter_id);


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
-- Name: t_looked_user_id_looked_user_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_looked_user_id_looked_user_uindex ON spark_db.t_looked_user USING btree (id_looked_user);


--
-- Name: t_message_id_message_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_message_id_message_uindex ON spark_db.t_message USING btree (id_message);


--
-- Name: t_search_data_search_data_id_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_search_data_search_data_id_uindex ON spark_db.t_search_data USING btree (search_data_id);


--
-- Name: t_tag_name_index; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE INDEX t_tag_name_index ON spark_db.t_tag USING btree (name);


--
-- Name: t_tag_tag_td_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_tag_tag_td_uindex ON spark_db.t_tag USING btree (tag_td);


--
-- Name: t_user_filter_user_filter_id_uindex; Type: INDEX; Schema: spark_db; Owner: sammy
--

CREATE UNIQUE INDEX t_user_filter_user_filter_id_uindex ON spark_db.t_user_filter USING btree (user_filter_id);


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
-- Name: t_looked_user t_looked_user_t_user_profile_user_profile_id_fk; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_looked_user
    ADD CONSTRAINT t_looked_user_t_user_profile_user_profile_id_fk FOREIGN KEY (from_user) REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- Name: t_looked_user t_looked_user_t_user_profile_user_profile_id_fk_2; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_looked_user
    ADD CONSTRAINT t_looked_user_t_user_profile_user_profile_id_fk_2 FOREIGN KEY (to_user) REFERENCES spark_db.t_user_profile(user_profile_id);


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
-- Name: t_search_data t_search_data_t_user_profile_user_profile_id_fk; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_search_data
    ADD CONSTRAINT t_search_data_t_user_profile_user_profile_id_fk FOREIGN KEY (user_id) REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- Name: t_tag t_tag_t_user_profile_user_profile_id_fk; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_tag
    ADD CONSTRAINT t_tag_t_user_profile_user_profile_id_fk FOREIGN KEY (user_id) REFERENCES spark_db.t_user_profile(user_profile_id);


--
-- Name: t_user_filter t_user_filter_t_user_profile_user_profile_id_fk; Type: FK CONSTRAINT; Schema: spark_db; Owner: sammy
--

ALTER TABLE ONLY spark_db.t_user_filter
    ADD CONSTRAINT t_user_filter_t_user_profile_user_profile_id_fk FOREIGN KEY (from_user_id) REFERENCES spark_db.t_user_profile(user_profile_id);


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

