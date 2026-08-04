#include "InfoBar.hpp"
#include <sstream>
#include <iomanip>

InfoBar::InfoBar(float x, float y, float width, float height)
    : vDecrochage(380.f), yx(-40.f), yy(-6.f)
{
    background.setSize(sf::Vector2f(width, height));
    background.setPosition(x, y);
    background.setFillColor(sf::Color(30, 30, 30));
    background.setOutlineThickness(1);
    background.setOutlineColor(sf::Color(80, 80, 80));

    // Label V décrochage
    labelVDecrochage.setFont(font);
    labelVDecrochage.setString("V decrochage: ");
    labelVDecrochage.setCharacterSize(17);
    labelVDecrochage.setFillColor(sf::Color::White);
    labelVDecrochage.setPosition(x + 10, y + 8);

    // Valeur V décrochage (vert)
    valueVDecrochage.setFont(font);
    valueVDecrochage.setCharacterSize(17);
    valueVDecrochage.setFillColor(sf::Color::Green);
    valueVDecrochage.setPosition(x + 175, y + 8);

    // Label yx
    labelYX.setFont(font);
    labelYX.setString("yx = ");
    labelYX.setCharacterSize(17);
    labelYX.setFillColor(sf::Color::White);
    labelYX.setPosition(x + 320, y + 8);

    valueYX.setFont(font);
    valueYX.setCharacterSize(17);
    valueYX.setFillColor(sf::Color::Green);
    valueYX.setPosition(x + 360, y + 8);

    // Label yy
    labelYY.setFont(font);
    labelYY.setString("yy = ");
    labelYY.setCharacterSize(17);
    labelYY.setFillColor(sf::Color::White);
    labelYY.setPosition(x + 450, y + 8);

    valueYY.setFont(font);
    valueYY.setCharacterSize(17);
    valueYY.setFillColor(sf::Color::Green);
    valueYY.setPosition(x + 490, y + 8);

    // Icône signal GPS (petit cercle vert à droite)
    signalIcon.setRadius(8.f);
    signalIcon.setFillColor(sf::Color::Green);
    signalIcon.setPosition(x + width - 30, y + 8);

    update();
}

void InfoBar::setVDecrochage(float v) { vDecrochage = v; update(); }
void InfoBar::setYX(float val)        { yx = val;         update(); }
void InfoBar::setYY(float val)        { yy = val;         update(); }

void InfoBar::update()
{
    std::ostringstream ssV;
    ssV << std::fixed << std::setprecision(0) << vDecrochage << " km/h";
    valueVDecrochage.setString(ssV.str());

    std::ostringstream ssYX;
    ssYX << std::fixed << std::setprecision(0) << yx;
    valueYX.setString(ssYX.str());

    std::ostringstream ssYY;
    ssYY << std::fixed << std::setprecision(0) << yy;
    valueYY.setString(ssYY.str());
}

void InfoBar::draw(sf::RenderWindow& window)
{
    window.draw(background);
    window.draw(labelVDecrochage);
    window.draw(valueVDecrochage);
    window.draw(labelYX);
    window.draw(valueYX);
    window.draw(labelYY);
    window.draw(valueYY);
    window.draw(signalIcon);
}