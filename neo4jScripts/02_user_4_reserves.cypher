MATCH (u:User)
OPTIONAL MATCH (u)<-[:RESERVED_BY]-(l:Lodgement)
WITH u, COUNT(l) AS numReservations
WHERE numReservations >= 4
RETURN u
