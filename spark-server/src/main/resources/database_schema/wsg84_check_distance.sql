CREATE OR REPLACE FUNCTION "wsg84_check_distance"("lat1" decimal, "long1" decimal, "lat2" decimal, "long2" decimal, "distance" decimal)
    RETURNS bool AS
$body$
DECLARE
    d2r decimal default (pi() / 180.0);
    dlong decimal;
    dlat decimal;
    a decimal;
    c decimal;
    d decimal;
BEGIN
    dlong = (long2 - long1) * d2r;
    dlat = (lat2 - lat1) * d2r;
    a = pow(sin(dlat/2.0), 2) + cos(lat1*d2r) * cos(lat2*d2r) * pow(sin(dlong/2.0), 2);
    c = 2 * atan2(sqrt(a), sqrt(1-a));
    d = 6367 * c;
    if (distance >= d)
    then return true;
    else
        return false;
    end if;
end
$body$
    LANGUAGE 'plpgsql'
