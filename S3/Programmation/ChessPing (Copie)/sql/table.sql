CREATE DATABASE Ping_Cheque;
USE Ping_Cheque;

CREATE TABLE piece(
    id INT NOT NULL,
    nom VARCHAR(20),
    couleur VARCHAR(20),
    img VARCHAR(50)
);

CREATE TABLE puissance(
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_piece INT,
    val INT,
    FOREIGN KEY (id_piece) REFERENCES piece(id)
);




INSERT INTO piece(id,nom,couleur,img) VALUES
(1,'Pion','Blanc','pN.png'),
(2,'Pion','Noir','pB.png'); 

INSERT INTO puissance(id_piece,val) VALUES
(1,1),
(2,1);