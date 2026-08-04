#include "PareBrise.hpp"
#include <cstdlib>

PareBrise::PareBrise(float largeur, float hauteur)
    : largeur(largeur), hauteur(hauteur), vueActuelle(Direction::AVANT)
{
    // Charger les 5 images de ciel
textures[0].loadFromFile("assets/img/sky_avant.png");
textures[1].loadFromFile("assets/img/sky_gauche.jpg");
textures[2].loadFromFile("assets/img/sky_droite.jpg");
textures[3].loadFromFile("assets/img/sky_bas.jpg");
textures[4].loadFromFile("assets/img/sky_arriere.jpg");

    // Mettre à l'échelle pour remplir le pare-brise
    background.setTexture(textures[0]);
    background.setScale(
        largeur  / textures[0].getSize().x,
        hauteur  / textures[0].getSize().y
    );

    // Charger la texture nuage (optionnel si tu as une image)
    // textureNuage.loadFromFile("assets/img/nuage.png");

    chargerNuages();
}

void PareBrise::chargerNuages()
{
    nuages.clear();
    // Créer 4 nuages répartis sur l'écran
    for(int i = 0; i < 4; i++) {
        Nuage n;
        n.sprite.setTexture(textureNuage);
        // Position aléatoire de départ
        n.sprite.setPosition(
            (float)(rand() % (int)largeur),
            (float)(rand() % (int)(hauteur * 0.6f))  // dans le haut du ciel
        );
        n.sprite.setScale(1.5f, 1.f);
        n.vitesse = 0.2f + (rand() % 4) * 0.1f;  // vitesse variée
        nuages.push_back(n);
    }
}

void PareBrise::setVue(Direction dir)
{
    vueActuelle = dir;

    int index = 0;
    switch(dir) {
        case Direction::AVANT:   index = 0; break;
        case Direction::GAUCHE:  index = 1; break;
        case Direction::DROITE:  index = 2; break;
        case Direction::BAS:     index = 3; break;
        case Direction::ARRIERE: index = 4; break;
    }

    background.setTexture(textures[index]);
    background.setScale(
        largeur  / textures[index].getSize().x,
        hauteur  / textures[index].getSize().y
    );
}

void PareBrise::update(bool enPause)
{
    if(enPause) return;

    // Vitesse des nuages selon la vue
    float vitesseMulti = 1.f;
    if(vueActuelle == Direction::BAS)     vitesseMulti = 2.5f;  // plus vite en vue bas
    if(vueActuelle == Direction::ARRIERE) vitesseMulti = -1.f;  // sens inverse

    for(auto& n : nuages) {
        n.sprite.move(-n.vitesse * vitesseMulti, 0);
        // Recyclage : quand le nuage sort à gauche, il réapparaît à droite
        if(n.sprite.getPosition().x < -200.f)
            n.sprite.setPosition(largeur + 50.f, n.sprite.getPosition().y);
        if(n.sprite.getPosition().x > largeur + 200.f)
            n.sprite.setPosition(-200.f, n.sprite.getPosition().y);
    }
}

void PareBrise::draw(sf::RenderWindow& window)
{
    window.draw(background);
    for(auto& n : nuages)
        window.draw(n.sprite);
}