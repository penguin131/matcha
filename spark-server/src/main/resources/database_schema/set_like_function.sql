create function set_like("from" character varying, "to" character varying) returns void
    language plpgsql
as
$$
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
--             Лайк в обе стороны
        if (simple>0 and reverse>0)
        then
            update spark_db.t_users_unity set confirmed=false
            where (select user_profile_id from spark_db.t_user_profile where login="from")=postgres.spark_db.t_users_unity.user2_id
              and (select user_profile_id from spark_db.t_user_profile where login="to")=postgres.spark_db.t_users_unity.user1_id;
            update spark_db.t_user_profile set rating=rating-1 where login="to";
        else if (simple>0 and reverse=0)
        then
--             Лайк в одну сторону
            delete from spark_db.t_users_unity
            where postgres.spark_db.t_users_unity.user1_id=(select user_profile_id from spark_db.t_user_profile where login="from")
              and postgres.spark_db.t_users_unity.user2_id=(select user_profile_id from spark_db.t_user_profile where login="to");
            update spark_db.t_user_profile set rating=rating-1 where login="to";
            --     Если еще нет лайков
        else IF simple=0 and reverse=0
        then
            --         insert new user unity
            insert into spark_db.t_users_unity (user1_id, user2_id)
            VALUES (
                       (select user_profile_id from spark_db.t_user_profile where login="from"),
                       (select user_profile_id from spark_db.t_user_profile where login="to"));
            --         up rating
            update spark_db.t_user_profile set rating=rating+1 where login="to";
--             Если есть только лайк в обратную сторону
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
        end if;
    end if;
end
$$;

alter function set_like(varchar, varchar) owner to sammy;

