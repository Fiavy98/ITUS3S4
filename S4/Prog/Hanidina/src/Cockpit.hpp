#pragma once
#include <SFML/Graphics.hpp>
#include "Avion.hpp"

class Cockpit
{
private:
    // Avion
    Avion avion;

public:
    Cockpit();
    void draw(sf::RenderWindow& window);
};