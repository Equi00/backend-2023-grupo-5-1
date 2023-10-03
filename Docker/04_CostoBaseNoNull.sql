--Evitar que el precio base de un alojamiento tome un valor nulo en la base (por fuera de la interfaz de usuario).

ALTER TABLE lodgment
ALTER COLUMN base_cost SET NOT NULL; --Esto es igual a ADD CONSTRAINT CHK_BASE_COST CHECK (base_cost IS NOT NULL);

--PRUEBA DE INSERT CON BASE_COST NULL--
INSERT INTO lodgment
(id,accommodation_detail,address, base_cost,bathrooms,bedrooms,capacity,cleaning_service,commission,country, description, image_url,name,other_aspects,owner_id)
VALUES 
(88,
'Algo',
'Prueba Punto4',
NULL, --BASE_COST
1,
3,
4,
false,
1.05,
'Argentina',
'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta, neque eget ullamcorper pellentesque, mi nunc varius lorem, vel molestie nulla mauris nec turpis. Nunc sollicitudin tortor in massa varius laoreet. Aliquam rhoncus enim libero, nec ultrices neque pharetra ornare. Aenean et lacus vel dui sollicitudin eleifend rhoncus nec dolor.', 
'https://www.losandes.com.ar/resizer/p--t4lbcubyFqgvm51EySkpaPgk=/980x640/smart/filters:quality(75):format(webp)/cloudfront-us-east-1.images.arcpublishing.com/grupoclarin/HA2GKZJWGY3TMOJQGE2DANJRHA.jpg',
'Cabania 2',
'Tiene wifi',
2);

SELECT * FROM lodgment WHERE id=88; --No existe el registro 88
