#pragma once

#include <SFML/Graphics.hpp>
#include <string>
#include <functional>

class Freinage
{
private:
    sf::RectangleShape background;
    sf::Font font;

    sf::Text label;

    // Bouton +
    sf::RectangleShape btnPlus;
    sf::Text txtPlus;

    // Bouton -
    sf::RectangleShape btnMoins;
    sf::Text txtMoins;

    float value;
    float step;

    // Callbacks optionnels
    std::function<void(float)> onChange;

public:
    Freinage(const std::string& name, float x, float y, float width, float height, float step = 1.f);

    void setOnChange(std::function<void(float)> callback);

    float getValue() const;
    void setValue(float val);

    // Appeler depuis la boucle d'évènements SFML
    void handleEvent(const sf::Event& event, sf::RenderWindow& window);

    void draw(sf::RenderWindow& window);
};