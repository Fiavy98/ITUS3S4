#pragma once

#include <SFML/Graphics.hpp>
#include <string>

class Compteur
{
private:
    sf::RectangleShape background;
    sf::Font font;
    sf::Text labelAltitude;
    sf::Text valueAltitude;
    sf::Text labelDistance;
    sf::Text valueDistance;
    sf::Text arrowAltitude;

    float altitude;       // en mètres
    float distancePiste;  // en mètres

public:
    Compteur(float x, float y, float width, float height);

    void setAltitude(float alt);
    void setDistancePiste(float dist);

    float getAltitude() const;
    float getDistancePiste() const;

    void update();
    void draw(sf::RenderWindow& window);
};