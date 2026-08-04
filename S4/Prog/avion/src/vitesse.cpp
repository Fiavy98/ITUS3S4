#include "PareBrise.hpp"
#include <cstdlib>
#include <ctime>
#include <cmath>

PareBrise::PareBrise(float largeur, float hauteur)
    : largeur(largeur), hauteur(hauteur),
      vueActuelle(Direction::AVANT), vyActuel(0.f)
{
    srand((unsigned)time(nullptr));

    // Créer 5 textures de ciel procéduralement
    // (remplacer par loadFromFile si tu as les images)
    sf::Color couleurs[5] = {
        sf::Color(100, 160, 230),  // AVANT  - bleu ciel
        sf::Color(90,  150, 220),  // GAUCHE - bleu légèrement différent
        sf::Color(90,  150, 220),  // DROITE
        sf::Color(60,  100, 180),  // BAS    - plus sombre (sol)
        sf::Color(80,  130, 200),  // ARRIERE
    };

    for(int i = 0; i < 5; i++) {
        sf::RenderTexture rt;
        rt.create((unsigned)largeur, (unsigned)hauteur);

        // Dégradé ciel
        for(int y = 0; y < (int)hauteur; y++) {
            float t = (float)y / hauteur;
            sf::RectangleShape ligne(sf::Vector2f(largeur, 1));
            ligne.setPosition(0, (float)y);
            sf::Color c = couleurs[i];
            sf::Color cHaut(c.r - 40, c.g - 40, c.b - 20);
            sf::Color cBas (c.r + 30, c.g + 30, c.b + 10);
            ligne.setFillColor(sf::Color(
                (sf::Uint8)(cHaut.r + t*(cBas.r - cHaut.r)),
                (sf::Uint8)(cHaut.g + t*(cBas.g - cHaut.g)),
                (sf::Uint8)(cHaut.b + t*(cBas.b - cHaut.b))
            ));
            rt.draw(ligne);
        }

        // Horizon (vue avant/gauche/droite/arriere)
        if(i != 3) {
            sf::RectangleShape horizon(sf::Vector2f(largeur, 4));
            horizon.setPosition(0, hauteur * 0.70f);
            horizon.setFillColor(sf::Color(180, 210, 240, 180));
            rt.draw(horizon);

            // Sol vert
            sf::RectangleShape sol(sf::Vector2f(largeur, hauteur * 0.30f));
            sol.setPosition(0, hauteur * 0.72f);
            sol.setFillColor(sf::Color(70, 110, 50));
            rt.draw(sol);
        } else {
            // Vue BAS : sol complet
            sf::RectangleShape sol(sf::Vector2f(largeur, hauteur));
            sol.setPosition(0, 0);
            sol.setFillColor(sf::Color(80, 120, 55));
            rt.draw(sol);

            // Piste
            sf::RectangleShape piste(sf::Vector2f(largeur * 0.3f, hauteur));
            piste.setPosition(largeur * 0.35f, 0);
            piste.setFillColor(sf::Color(60, 60, 60));
            rt.draw(piste);

            sf::RectangleShape ligne_piste(sf::Vector2f(largeur * 0.02f, hauteur));
            ligne_piste.setPosition(largeur * 0.495f, 0);
            ligne_piste.setFillColor(sf::Color::White);
            rt.draw(ligne_piste);
        }

        rt.display();
        textures[i].loadFromImage(rt.getTexture().copyToImage());
    }

    background.setTexture(textures[0]);
    background.setScale(
        largeur  / textures[0].getSize().x,
        hauteur  / textures[0].getSize().y
    );

    initNuages();
}

void PareBrise::initNuages()
{
    nuages.clear();

    // Créer une texture de nuage générique
    sf::RenderTexture rt;
    rt.create(200, 100);
    rt.clear(sf::Color::Transparent);

    // Dessiner un nuage avec des ellipses
    struct Blob { float dx, dy, rw, rh; };
    std::vector<Blob> blobs = {
        {0,0,80,40},{-55,12,60,35},{55,12,60,35},{-25,-18,50,30},{25,-18,50,30}
    };
    for(auto& b : blobs) {
        sf::CircleShape e(1.f);
        e.setScale(b.rw, b.rh);
        e.setPosition(100.f + b.dx - b.rw, 50.f + b.dy - b.rh);
        e.setFillColor(sf::Color(245, 248, 255, 230));
        rt.draw(e);
    }
    rt.display();

    static sf::Texture nuageTexture;
    nuageTexture.loadFromImage(rt.getTexture().copyToImage());

    // Créer 6 nuages positionnés aléatoirement
    for(int i = 0; i < 6; i++) {
        Nuage n;
        n.sprite.setTexture(nuageTexture);
        float scale = 0.7f + (rand() % 6) * 0.1f;
        n.sprite.setScale(scale, scale * 0.6f);
        n.sprite.setPosition(
            (float)(rand() % (int)largeur),
            (float)(rand() % (int)(hauteur * 0.60f))
        );
        n.vitesse = 0.15f + (rand() % 5) * 0.08f;
        n.y = n.sprite.getPosition().y;
        nuages.push_back(n);
    }
}

void PareBrise::setVue(Direction dir)
{
    vueActuelle = dir;
    int idx = (int)dir;
    background.setTexture(textures[idx]);
    background.setScale(
        largeur  / textures[idx].getSize().x,
        hauteur  / textures[idx].getSize().y
    );
}

void PareBrise::setVy(float vy)
{
    vyActuel = vy;
}

void PareBrise::update(bool enPause)
{
    if(enPause) return;

    // Vitesse horizontale des nuages
    float vitH = 1.f;
    float vitV = 0.f;

    switch(vueActuelle) {
        case Direction::AVANT:   vitH =  1.0f; break;
        case Direction::ARRIERE: vitH = -1.0f; break;
        case Direction::GAUCHE:  vitH =  0.3f; break;
        case Direction::DROITE:  vitH =  0.3f; break;
        case Direction::BAS:     vitH =  0.0f; vitV = 1.5f; break;
    }

    // Vy influe sur le déplacement vertical des nuages
    vitV += vyActuel * 0.002f;

    for(auto& n : nuages) {
        n.sprite.move(-n.vitesse * vitH, vitV * n.vitesse);

        // Recyclage horizontal
        float x = n.sprite.getPosition().x;
        float y = n.sprite.getPosition().y;
        if(x < -220.f) n.sprite.setPosition(largeur + 50.f, y);
        if(x > largeur + 220.f) n.sprite.setPosition(-200.f, y);

        // Recyclage vertical
        if(y > hauteur) n.sprite.setPosition(x, -60.f);
        if(y < -80.f)   n.sprite.setPosition(x, hauteur + 10.f);
    }
}

void PareBrise::draw(sf::RenderWindow& window)
{
    window.draw(background);
    // Nuages seulement si vue aérienne (pas vue bas)
    if(vueActuelle != Direction::BAS) {
        for(auto& n : nuages)
            window.draw(n.sprite);
    }
}