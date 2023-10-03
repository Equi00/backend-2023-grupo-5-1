CREATE OR REPLACE FUNCTION get_users_with_more_than_n_reserves(min_reserves integer)
RETURNS SETOF client AS $$
BEGIN
RETURN QUERY
SELECT u.*
FROM client u
         INNER JOIN reserve r ON u.id = r.user_id
GROUP BY u.id
HAVING COUNT(r.id) > min_reserves;
END;
$$ LANGUAGE plpgsql;
