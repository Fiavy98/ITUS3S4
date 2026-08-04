function change(){
    let titre = document.getElementById("titre");
    titre.textContent = "Modifier avec javascript";

    let p = document.getElementById("paragraphe");
    // p.style.color="blue";
    p.style.fontSize="20px";

    let link = document.getElementById("lien");
    link.setAttribute("href","https://www.google.com");

    let pc = document.getElementById("paragraphe");
    pc.classList.add("rouge");

}

