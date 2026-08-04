#include "Compteur.hpp"
#include <sstream>
#include <iomanip>
#include <cmath>

Compteur::Compteur(float x, float y, float width, float height)
    : altitude(1000.f), distancePiste(-7000.f)
{
    // Fond de la barre
    background.setSize(sf::Vector2f(width, height));
    background.setPosition(x, y);
    background.setFillColor(sf::Color(30, 30, 30));
    background.setOutlineThickness(1);
    background.setOutlineColor(sf::Color(80, 80, 80));

    // Chargement police (doit être disponible ou remplacer par votre chemin)
    // font.loadFromFile("assets/font.ttf");

    // Label Altitude
    labelAltitude.setFont(font);
    labelAltitude.setString("Altitude : ");
    labelAltitude.setCharacterSize(18);
    labelAltitude.setFillColor(sf::Color::White);
    labelAltitude.setPosition(x + 10, y + 8);

    // Valeur Altitude (cyan)
    valueAltitude.setFont(font);
    valueAltitude.setCharacterSize(18);
    valueAltitude.setFillColor(sf::Color::Cyan);
    valueAltitude.setPosition(x + 120, y + 8);

    // Flèche altitude
    arrowAltitude.setFont(font);
    arrowAltitude.setString("^");
    arrowAltitude.setCharacterSize(18);
    arrowAltitude.setFillColor(sf::Color::Cyan);
    arrowAltitude.setPosition(x + 220, y + 8);

    // Label Distance piste
    labelDistance.setFont(font);
    labelDistance.setString("| Distance piste : ");
    labelDistance.setCharacterSize(18);
    labelDistance.setFillColor(sf::Color::White);
    labelDistance.setPosition(x + 250, y + 8);

    // Valeur Distance piste (cyan)
    valueDistance.setFont(font);
    valueDistance.setCharacterSize(18);
    valueDistance.setFillColor(sf::Color::Cyan);
    valueDistance.setPosition(x + 450, y + 8);

    update();
}

void Compteur::setAltitude(float alt)
{
    altitude = alt;
    update();
}

void Compteur::setDistancePiste(float dist)
{
    distancePiste = dist;
    update();
}

float Compteur::getAltitude() const { return altitude; }
float Compteur::getDistancePiste() const { return distancePiste; }

void Compteur::update()
{
    // Altitude
    std::ostringstream ssAlt;
    ssAlt << std::fixed << std::setprecision(0) << altitude << " m";
    valueAltitude.setString(ssAlt.str());

    // Flèche selon montée/descente (ici fixe, à adapter selon logique)
    arrowAltitude.setString(altitude >= 0 ? "^" : "v");

    // Distance piste
    std::ostringstream ssDist;
    ssDist << std::fixed << std::setprecision(0) << distancePiste << " m";
    valueDistance.setString(ssDist.str());
}

void Compteur::draw(sf::RenderWindow& window)
{
    window.draw(background);
    window.draw(labelAltitude);
    window.draw(valueAltitude);
    window.draw(arrowAltitude);
    window.draw(labelDistance);
    window.draw(valueDistance);
}