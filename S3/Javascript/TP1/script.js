// TP1
/*Sélectionner le titre (<h1>) par son id et
 afficher son texte dans la console */
const titre = document.getElementById("titre");
console.log(titre.innerText);

/* Sélectionner tous les paragraphes de
classe intro et afficher chacun d’eux*/
const p = document.getElementsByClassName("intro");
console.log(p[0].innerText);
console.log(p[1].innerText);

/* Sélectionner le premier paragraphe de la
 page (avec un querySelector).*/
const p_un = document.querySelector(".intro");
console.log(p_un.innerText);

/* Sélectionner tous les paragraphes (avec
getElementsByTagName) et afficher le
nombre total. */
const paragraphe = document.getElementsByTagName("p");
console.log(paragraphe.length);




