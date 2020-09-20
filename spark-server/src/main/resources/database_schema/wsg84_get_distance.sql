with CTE as (
    select user_profile_id, latitude, longitude from "spark_db".t_user_profile where login='smight'
)
   , CTE2 as (
    select
        user_profile_id
         ,wsg84_get_distance( (select CTE.latitude from CTE limit 1), (select CTE.longitude from CTE limit 1), latitude, longitude) as distance
    from spark_db.t_user_profile
)
select *
     ,(select id_image from "spark_db".t_image  where u.user_profile_id=user_id and is_main=true limit 1) as photo
     ,(select  count(*) from spark_db.t_users_unity where u.user_profile_id=user2_id or (u.user_profile_id=user1_id and confirmed=true)) as has_like
     ,(select  count(*) from spark_db.t_complaint where u.user_profile_id=to_user) as has_dislike
     ,CTE2.distance
from "spark_db".t_user_profile u
         join CTE2 on CTE2.user_profile_id=u.user_profile_id
where login<>'smight'
  and CTE2.distance<=10
--   and (sex_preferences=0 or sex_preferences is null)
--   and sex=0
--   and rating>=-200
--   and age<=999
--   and age>=0
order by rating desc
       ,(select count(*) from spark_db.t_tag t1, spark_db.t_tag t2
         where t1.user_id=(select user_profile_id from CTE) and t2.user_id=u.user_profile_id and t1.name=t2.name) desc
       ,CTE2.distance

