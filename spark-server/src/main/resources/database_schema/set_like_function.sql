

CREATE OR REPLACE FUNCTION "set_like"("from" character varying, "to" character varying)
    RETURNS void AS
$body$
declare
    simple integer=(select count(*) from spark_db.t_users_unity t1
                         join spark_db.t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                         join spark_db.t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
                    where t2.login="from" and t3.login="to");
    reverse integer=(select count(*) from spark_db.t_users_unity t1
                         join spark_db.t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                         join spark_db.t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
                    where t2.login="to" and t3.login="from");
begin
--     Если есть жалоба, то нельзя подружиться
    if (select count(*) from spark_db.t_complaint t1
        join spark_db.t_user_profile t2 on (t1.from_user=t2.user_profile_id)
        join spark_db.t_user_profile t3 on (t1.to_user=t3.user_profile_id)
        where t2.login="from" and t3.login="to")=0
    then
--         Если лайк ставится повторно, то удаляется

    --     If t_users_unity not exists
        IF simple=0 and reverse=0
        then
    --         insert new user unity
            insert into spark_db.t_users_unity (user1_id, user2_id)
                VALUES (
                    (select user_profile_id from spark_db.t_user_profile where login="from"),
                    (select user_profile_id from spark_db.t_user_profile where login="to"));
    --         up rating
            update spark_db.t_user_profile set rating=rating+1 where login="to";
    --         if exist reverse t_users_unity
        elsif simple=0 and reverse>0
        then
            WITH cte_unity AS
                (select t1.t_users_unity_id
                from spark_db.t_users_unity t1
                    join spark_db.t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                    join spark_db.t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
                where t2.login="to" and t3.login="from")
    --         set confirmed
            update spark_db.t_users_unity set confirmed=true
            where t_users_unity_id in (select * from cte_unity);
    --         up rating
            update spark_db.t_user_profile set rating=rating+1 where login="to";
        end if;
    end if;
end
$body$
    LANGUAGE 'plpgsql'


