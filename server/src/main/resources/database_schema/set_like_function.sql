/**
  Функция возвращает true, если после выполнения два пользователя подружились. Иначе false
 */
create function set_like("from" character varying, "to" character varying) returns boolean
    language plpgsql
as
$$
declare
--     simple и reverse >0 по идее невозможная ситуация
--     мой лайк
    simple integer=(select count(*) from spark_db.t_users_unity t1
                                             join spark_db.t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                                             join spark_db.t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
                    where t2.login="from" and t3.login="to");
--     меня лайкнули
    reverse integer=(select count(*) from spark_db.t_users_unity t1
                                              join spark_db.t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                                              join spark_db.t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
                     where t2.login="to" and t3.login="from");
--     Меня лайкнули и я подтвердил
    reverseConfirmed boolean=reverse>0 and
                      (select t1.confirmed from spark_db.t_users_unity t1
                                                join spark_db.t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                                                join spark_db.t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
                       where t2.login="to" and t3.login="from" limit 1)=true;
--     Я лайкнул, а потом подтвердили
    simpleConfirmed boolean=simple>0 and
                            (select t1.confirmed from spark_db.t_users_unity t1
                                                          join spark_db.t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                                                          join spark_db.t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
                             where t2.login="from" and t3.login="to" limit 1)=true;
begin
--               Если есть жалоба, то нельзя подружиться
    if (select count(*) from spark_db.t_complaint t1
                                 join spark_db.t_user_profile t2 on (t1.from_user=t2.user_profile_id)
                                 join spark_db.t_user_profile t3 on (t1.to_user=t3.user_profile_id)
        where t2.login="from" and t3.login="to")=0 and "from"<>"to"
    then
------------------------------------        LIKED      -----------------------------------------------------------------
--      Я лайкнул, а потом подтвердили
        if (simpleConfirmed=true)
        then
            delete from spark_db.t_users_unity
            where (select user_profile_id from spark_db.t_user_profile where login="from")=postgres.spark_db.t_users_unity.user1_id
              and (select user_profile_id from spark_db.t_user_profile where login="to")=postgres.spark_db.t_users_unity.user2_id;
            insert into spark_db.t_users_unity (user1_id, user2_id)
            VALUES (
                       (select user_profile_id from spark_db.t_user_profile where login="to" limit 1),
                       (select user_profile_id from spark_db.t_user_profile where login="from" limit 1));

            --         down rating
            update spark_db.t_user_profile set rating=rating-1 where login="to";
--      Меня лайкнули, а я подтвердил
        else if (reverseConfirmed=true)
        then
            update spark_db.t_users_unity set confirmed=false
            where (select user_profile_id from spark_db.t_user_profile where login="to")=postgres.spark_db.t_users_unity.user1_id
              and (select user_profile_id from spark_db.t_user_profile where login="from")=postgres.spark_db.t_users_unity.user2_id;

            --         down rating
            update spark_db.t_user_profile set rating=rating-1 where login="to";
        else if (simple>0 and reverse=0)
        then
--             если я уже ставил лайк этому челу
            delete from spark_db.t_users_unity
            where postgres.spark_db.t_users_unity.user1_id=(select user_profile_id from spark_db.t_user_profile where login="from")
              and postgres.spark_db.t_users_unity.user2_id=(select user_profile_id from spark_db.t_user_profile where login="to");
            --         down rating
            update spark_db.t_user_profile set rating=rating-1 where login="to";
---------------------------------------     NO LIKE     -----------------------------------------------------------------
        else IF simple=0 and reverse=0
        then
            --         insert new user unity
            insert into spark_db.t_users_unity (user1_id, user2_id)
            VALUES (
                       (select user_profile_id from spark_db.t_user_profile where login="from" limit 1),
                       (select user_profile_id from spark_db.t_user_profile where login="to" limit 1));
            --         up rating
            update spark_db.t_user_profile set rating=rating+1 where login="to";
--             Если этот чел уже ставил мне лайк, то мне пофиг, ставлю новый и мы друганы
        elsif simple=0 and reverse>0
        then
            WITH cte_unity AS
                     (select t1.t_users_unity_id
                      from spark_db.t_users_unity t1
                               join spark_db.t_user_profile t2 on (t2.user_profile_id=t1.user1_id)
                               join spark_db.t_user_profile t3 on (t3.user_profile_id=t1.user2_id)
                      where t2.login="to" and t3.login="from")
            update spark_db.t_users_unity set confirmed=true
            where t_users_unity_id in (select * from cte_unity);
            --         up rating
            update spark_db.t_user_profile set rating=rating+1 where login="to";
            return true;
        end if;
        end if;
        end if;
        end if;
    end if;
    return false;
end
$$;

alter function set_like(varchar, varchar) owner to sammy;

