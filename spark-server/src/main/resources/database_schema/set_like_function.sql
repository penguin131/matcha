

CREATE OR REPLACE FUNCTION "set_like"("from" character varying, "to" character varying)
    RETURNS void AS
$body$
BEGIN
    --     If t_users_unity already exists
    IF (select count(*) from "spark-db".t_users_unity t1
                                 join "spark-db".t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                                 join "spark-db".t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
        where t2.login="from" and t3.login="to") > 0
    THEN
        WITH cte_unity AS
                 (select t1.t_users_unity_id
                  from "spark-db".t_users_unity t1
                           join "spark-db".t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                           join "spark-db".t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
                  where t2.login="to" and t3.login="from")
--         set confirmed
        update "spark-db".t_users_unity set confirmed=true
        where t_users_unity_id in (select * from cte_unity);
--         up rating
        update "spark-db".t_user_profile set rating=rating+1 where login="to";
--         else if exist reverse t_users_unity
    ELSIF (select count(*) from "spark-db".t_users_unity t1
                                    join "spark-db".t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                                    join "spark-db".t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
           where t2.login="to" and t3.login="from") > 0
    THEN
--         insert new users_unity
        insert into "spark-db".t_users_unity (user1_id, user2_id)
        VALUES ((select user_profile_id from "spark-db".t_user_profile where login="from"),
                (select user_profile_id from "spark-db".t_user_profile where login="to"));
--         up rating
        update "spark-db".t_user_profile set rating=rating+1 where login="to";
    END IF;
end
$body$
    LANGUAGE 'plpgsql'


