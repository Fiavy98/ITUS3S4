backend/
├── src/
│   ├── routes/        # toutes les routes de l'API
│   ├── controllers/   # la logique des fonctions de chaque route
│   ├── models/        # la connexion à la base et modèles MySQL
│   └── app.js         # serveur principal Express
├── package.json       # liste des dépendances
└── .env               # informations sensibles (mot de passe DB)


Initialiser le projet Node.js

Dans le dossier backend :
    cd backend
    npm init -y
    Cela crée un package.json avec ton projet
    -y = accepter toutes les options par défaut

Installer les dépendances
    Pour créer un serveur et se connecter à MySQL : 
        npm install express mysql2 dotenv cors



Créer un réseau Docker (si pas déjà)
docker network create fiompiana-net
2️⃣ Connecter les containers au réseau
docker network connect fiompiana-net angular-container
docker network connect fiompiana-net backend-container



Lancer MySQL dans un container
docker run -d \
  --name mysql-container \
  --network fiompiana-net \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=gestion_poulets \
  -p 3306:3306 \
  mysql:8




  src/app/
│
├── services/
│   ├── race.service.ts
│   ├── lot.service.ts
│   └── poulailler.service.ts
│
├── components/
│   ├── race/
│   │   ├── race.component.ts
│   │   └── race.component.html
│   │
│   ├── lot/
│   │   ├── lot.component.ts
│   │   └── lot.component.html
│   │
│   └── poulailler/
│       ├── poulailler.component.ts
│       └── poulailler.component.html
│
├── app.component.ts
└── app.component.html