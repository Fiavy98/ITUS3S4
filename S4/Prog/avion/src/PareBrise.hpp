#pragma once
#include <SFML/Graphics.hpp>
#include <vector>

enum class Direction { AVANT, GAUCHE, DROITE, BAS, ARRIERE };

class PareBrise
{
private:
    sf::Texture textures[5];
    sf::Sprite  background;

    struct Nuage {
        sf::Sprite sprite;
        float vitesse;
        float y;
    };
    std::vector<Nuage> nuages;

    Direction vueActuelle;
    float largeur, hauteur;
    float vyActuel; // pour faire bouger les nuages selon Vy

    void initNuages();
    void dessinerNuageShape(sf::RenderTexture& rt, float cx, float cy, float scale);

public:
    PareBrise(float largeur, float hauteur);
    void setVue(Direction dir);
    void setVy(float vy); // vitesse verticale → influence nuages
    void update(bool enPause);
    void draw(sf::RenderWindow& window);
};