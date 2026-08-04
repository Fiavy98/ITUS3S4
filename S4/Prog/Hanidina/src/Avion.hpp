#pragma once
#include <SFML/Graphics.hpp>

class Avion 
{
private:
    sf::Texture         texture;
    sf::Sprite sprite;
    float largeur, hauteur;

public:
    Avion(float largeur, float hauteur);

        // Charge une image PNG
    bool load(const std::string& filename);
    // Dessine l'image
    void draw(sf::RenderWindow& window);

};
