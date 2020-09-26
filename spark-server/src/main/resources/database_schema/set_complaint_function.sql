create function set_complaint("from" character varying, "to" character varying) returns void
    language plpgsql
as
$$
BEGIN
    --     Если есть лайк, то нельзя поставить жалобу.
    if (select count(*) from spark_db.t_users_unity t1
                                 join spark_db.t_user_profile t2 on (t1.user1_id=t2.user_profile_id)
                                 join spark_db.t_user_profile t3 on (t1.user2_id=t3.user_profile_id)
        where t2.login="from" and t3.login="to"
           or t3.login="from" and t2.login="to" and t1.confirmed=true)=0
    then
--         Если записи еще нет, то создаю
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

            --         down rating
            update spark_db.t_user_profile set rating=rating-5 where login="to";
--             Если запись есть, то удаляю
        ELSE
            delete from spark_db.t_complaint
            where postgres.spark_db.t_complaint.to_user=(select user_profile_id from spark_db.t_user_profile where login="to" limit 1)
              and postgres.spark_db.t_complaint.from_user=(select user_profile_id from spark_db.t_user_profile where login="from" limit 1);

            --          up rating
            update spark_db.t_user_profile set rating=rating+5 where login="to";
        END IF;
    end if;
end
$$;

alter function set_complaint(varchar, varchar) owner to sammy;

