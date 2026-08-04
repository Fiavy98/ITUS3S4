#pragma once

#include <SFML/Graphics.hpp>

class vitesse
{
private:
    sf::CircleShape body;

public:
    vitesse(float x, float y);
    
    void draw(sf::RenderWindow& window);
};
