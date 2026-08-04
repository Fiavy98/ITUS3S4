#include "Cockpit.hpp"
#include <iomanip>
#include <sstream>

Cockpit::Cockpit()
    : avion(1600.f, 350.f)
{
    avion.load("assets/img/GOD.jpg"); 
}

void Cockpit::draw(sf::RenderWindow& window)
{
    avion.draw(window);

}