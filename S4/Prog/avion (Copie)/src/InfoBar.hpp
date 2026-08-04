#pragma once

#include <SFML/Graphics.hpp>

class InfoBar
{
private:
    sf::RectangleShape background;
    sf::Font font;

    sf::Text labelVDecrochage;
    sf::Text valueVDecrochage;

    sf::Text labelYX;
    sf::Text valueYX;

    sf::Text labelYY;
    sf::Text valueYY;

    // Indicateur GPS/signal (petit cercle décoratif)
    sf::CircleShape signalIcon;

    float vDecrochage;
    float yx;
    float yy;

public:
    InfoBar(float x, float y, float width, float height);

    void setVDecrochage(float v);
    void setYX(float val);
    void setYY(float val);

    void update();
    void draw(sf::RenderWindow& window);
};