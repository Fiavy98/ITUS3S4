/** Sélectionner le titre de section (<h2> avec
classe section-title).*/
const title = document.getElementsByClassName("section-title");
console.log(title[0].innerText);

/** Sélectionner tous les liens du menu de
navigation et afficher leur texte (Accueil,
Cours, Contact) */
function aff_lien(){
    const a = document.getElementsByTagName("a");
    for(var i = 0; i<a.length; i++){
        console.log(a[i].innerText);
    }
}

aff_lien();

/**Sélectionner le deuxième article et
afficher son titre (Article 2) */
function article_deux(){
  const article = document.getElementsByTagName("article");
    for(var i = 1; i<article.length; i++){
        const title = document.getElementsByTagName("h3");
        for(var i = 1; i<title.length; i++){
            console.log(title[i].innerText);  
        }
    }
}

article_deux();

/**Sélectionner tous les paragraphes des
articles et afficher leur contenu */

function p_art(){
  const article = document.getElementsByTagName("article");
  const p = document.getElementsByTagName("p");
    for(var i = 0; i<article.length; i++){
        for(var i = 0; i<p.length; i++){
            console.log(p[i].innerText);  
        }
    }
}
p_art();