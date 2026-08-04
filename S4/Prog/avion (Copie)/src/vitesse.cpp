#include "vitesse.hpp"

vitesse::vitesse(float x,float y)
{
    body.setRadius(70);

    body.setPosition(x, y);

    body.setFillColor(sf::Color::Black);

    body.setOutlineThickness(2);

    body.setOutlineColor(sf::Color::White);
}

void vitesse::draw(sf::RenderWindow& window)
{
    window.draw(body);
}