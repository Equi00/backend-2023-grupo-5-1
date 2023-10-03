MATCH (u:User)<-[:RESERVED_BY]-(l:Lodgement)
WITH l, COUNT(u) as reserve_count
WHERE reserve_count > 4
RETURN l
