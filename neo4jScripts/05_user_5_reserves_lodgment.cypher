MATCH (usuario:User)<-[:RESERVED_BY]-(lodgment:Lodgement)
WITH usuario, lodgment, count(*) AS reservationCount
WHERE reservationCount > 5
RETURN usuario