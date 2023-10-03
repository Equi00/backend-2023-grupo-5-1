DROP TABLE IF EXISTS RegistroCambioDeSaldo;

CREATE TABLE IF NOT EXISTS RegistroCambioDeSaldo (
    Id SERIAL NOT NULL PRIMARY KEY,
    IdUsuario INT NOT NULL,
    Fecha DATE NOT NULL,
    SaldoNuevo INT NOT NULL,
    SaldoViejo INT NOT NULL
);

CREATE OR REPLACE FUNCTION InsertCambioDeSaldo() 
RETURNS trigger
language plpgsql
as
$$
BEGIN
	IF NEW.balance <> OLD.balance THEN
		INSERT INTO RegistroCambioDeSaldo(IdUsuario, Fecha, SaldoNuevo, SaldoViejo)
		VALUES (OLD.id, NOW(), NEW.balance, OLD.balance);
    End IF;
    RETURN NEW;
END;
$$

CREATE OR REPLACE TRIGGER CambioDeSaldo
	AFTER UPDATE ON client
    FOR EACH ROW
    EXECUTE FUNCTION InsertCambioDeSaldo();

UPDATE client
    SET balance=balance+1500
    WHERE client.id = 1;

UPDATE client
    SET name='Jorge'
    WHERE client.id = 2;

SELECT *
    FROM RegistroCambioDeSaldo;