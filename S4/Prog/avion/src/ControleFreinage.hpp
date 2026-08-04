#pragma once

#include <SFML/Graphics.hpp>
#include <string>

class ControleFreinage
{
private:
    sf::Font police;
    sf::Text titre;
    
    sf::RectangleShape fondCadre;
    
    // Boutons + et -
    sf::RectangleShape btnPlus;
    sf::RectangleShape btnMoins;
    
    sf::Text textePlus;
    sf::Text texteMoins;

    float valeurFreinage;

public:
    ControleFreinage(const std::string& nomAxe, float x, float y);
    
    void update(const sf::RenderWindow& window); // Pour gérer les clics plus tard si besoin
    void draw(sf::RenderWindow& window);
    
    float getValeur() const { return valeurFreinage; }
};