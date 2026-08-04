#pragma once

#include <SFML/Graphics.hpp>

class Compteur
{
private:
    std::string label;

    float valeur;

    sf::RectangleShape body;
    sf::Text texte;
    sf::Font police;
    
public:
    Compteur(const std::string& nom, float x, float y);

    void setValeur(float v);
    void draw(sf::RenderWindow& window);
};
