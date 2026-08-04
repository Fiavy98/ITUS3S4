C'est une simulation d'avion qui gère :

1. VITESSE
   - Vx  →  vitesse horizontale (km/h)
   - Vy  →  vitesse verticale   (km/h)
   - Vtotal = racine(Vx² + Vy²) →  vitesse réelle de l'avion

2. FREINAGE
   - Pas fixe = 10 m/s² = 36 km/h/s
   - Bouton X+ / X-  →  change freinage_x
   - Bouton Y+ / Y-  →  change freinage_y

3. POSITION
   - altitude  →  hauteur par rapport au sol  (m)
   - distance  →  position par rapport à la piste (m), commence négatif

4. CONDITIONS
   - Vtotal < 100 km/h       →  décrochage
   - altitude=0, distance>=0 →  atterrissage réussi
   - altitude=0, distance<0  →  crash avant piste

src/
│
├── main.cpp          # Point d'entrée du programme (création fenêtre + boucle principale SFML)
│
├── Cockpit.hpp       # Déclaration de la classe Cockpit (structure du tableau de bord)
├── Cockpit.cpp       # Implémentation de la logique du cockpit (gestion des instruments, update, draw)
│
├── Gauge.hpp         # Déclaration de la classe Gauge (un instrument / une jauge)
└── Gauge.cpp         # Implémentation de la Gauge (création du cercle, affichage, style)