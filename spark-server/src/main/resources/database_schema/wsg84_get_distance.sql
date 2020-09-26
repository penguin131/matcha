create function wsg84_get_distance(lat1 numeric, long1 numeric, lat2 numeric, long2 numeric) returns numeric
    language plpgsql
as
$$
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
    return d;
end
$$;

alter function wsg84_get_distance(numeric, numeric, numeric, numeric) owner to sammy;

