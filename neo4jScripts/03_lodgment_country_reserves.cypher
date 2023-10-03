MATCH (us:User)<-[:RESERVED_BY]-(lod:Lodgement)
WHERE lod.country =~ '.*Argentina.*' AND us.id <> 2
RETURN lod
//Caso de uso viendo un Lodgment de Argentina y yo siendo el Usuario con Id 2

//Enunciado:
//Saber una sugerencia de reservas adicionales para un determinado alojamiento que quiero comprar.
//Esto ocurre cuando otros usuarios compraron dichas reservas y tienen alojamientos reservados con el mismo destino.

