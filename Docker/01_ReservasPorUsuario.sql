DROP FUNCTION IF EXISTS reserves_by_client(bigint);
--1 Conocer los alojamientos que reservó un determinado usuario.
CREATE FUNCTION reserves_by_client(client_id bigint)
    RETURNS TABLE(cl_id bigint, lodgment_name character varying, start_date date, end_date date, client_name character varying)
    LANGUAGE 'plpgsql'

AS $$
BEGIN
RETURN QUERY SELECT reserve.id,
			   lodgment.name,
			   reserve.start_date,
			   reserve.end_date,
			   CONCAT(client.name,' ', client.surname) as client_name
		FROM reserve
		INNER JOIN client ON reserve.user_id = client.id
		INNER JOIN lodgment ON reserve.lodgment_id = lodgment.id
		WHERE reserve.user_id = client_id;
END
$$;
