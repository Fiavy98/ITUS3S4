function aff(){
    let name = document.getElementById("name").value;
    let val = document.createElement("p");
    val.textContent="Bonjour  " +name;
    document.getElementById("result").appendChild(val);
}

aff();

function user(){
    var user = prompt();
    let nm = document.getElementsByTagName("title");
    nm[0].textContent=user;
}
user();
function multiple(){
    let tble = document.getElementById("table");
    tble.border="1";
   let nb = document.getElementById("mult").value;
   let titre = document.getElementById("titre");
   titre.textContent="Table de multiplication de "+nb;

     for(var i=0; i<=10; i++){
        let td1 = document.createElement("p");
        td1.textContent=nb;
        document.getElementById("col1").appendChild(td1);

        let td2 = document.createElement("p");
        td2.textContent="x";
        document.getElementById("col2").appendChild(td2);

        let td3 = document.createElement("p");
        td3.textContent=i;
        document.getElementById("col3").appendChild(td3);

        let td4 = document.createElement("p");
        td4.textContent="=";
        document.getElementById("col4").appendChild(td4);

        let td5 = document.createElement("p");
        td5.textContent=nb*i;
        document.getElementById("col5").appendChild(td5);
    }
}

function Table(){
    let btn = document.getElementById("btn").value;
    let table = document.getElementById("bleau");
    table.border="1";

    let ajTitre1 = document.getElementById("th1");
    ajTitre1.textContent="Nom";

    let ajTitre2 = document.getElementById("th2");
    ajTitre2.textContent="Salaire";

    let ajTitre3 = document.getElementById("th3");
    ajTitre3.textContent="Supprimer";

    ajout();
}

    let tabTotal;
    let somme=0;
    let id = 0;

function supp(tr){
    let val = tr.children[1].textContent;
    somme-=Number(val);

    tr.remove();

     let tot = document.getElementById("total");
    tot.textContent="Total salaire : "+somme;
}


function ajout(){
    let inp_nom = document.getElementById("inp_nom").value;
    let inp_salaire = Number(document.getElementById("inp_salaire").value);
    tabTotal=inp_salaire;
    
    somme+=tabTotal;

     id+=1;
    let ajTrval = document.createElement("tr");
    ajTrval.id="tr";
    document.getElementById("bleau").appendChild(ajTrval);

    let ajtd1 = document.createElement("td");
    ajtd1.textContent=inp_nom;
    ajTrval.appendChild(ajtd1);
    
    let ajtd2 = document.createElement("td");
    ajtd2.textContent=inp_salaire;
    ajtd2.classList.add(id);
    ajTrval.appendChild(ajtd2);

    let ajtd3 = document.createElement("td");
    ajTrval.appendChild(ajtd3);

    let button = document.createElement("button");
    button.textContent="Supprimer";
    ajtd3.appendChild(button);

    button.onclick=function(){
        supp(ajTrval);
    };
    
    let tot = document.getElementById("total");
    tot.textContent="Total salaire : "+somme;
}
