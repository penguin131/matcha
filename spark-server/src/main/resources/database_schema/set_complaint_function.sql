CREATE OR REPLACE FUNCTION "set_complaint"("from" character varying, "to" character varying)
    RETURNS void AS
$body$
BEGIN
    IF (select count(*) from spark_db.t_complaint t1
                                 join spark_db.t_user_profile t2 on (t2.user_profile_id=t1.from_user)
                                 join spark_db.t_user_profile t3 on (t3.user_profile_id=t1.to_user)
        where t2.login="from" and t3.login="to"
       )=0
        and "from"<>"to"
    THEN
        insert into spark_db.t_complaint (from_user, to_user)
        values ((select user_profile_id from spark_db.t_user_profile where login="from" limit 1),
                (select user_profile_id from spark_db.t_user_profile where login="to" limit 1));

        update spark_db.t_user_profile set rating=rating-5 where login="to";
    END IF;
end
$body$
    LANGUAGE 'plpgsql'
